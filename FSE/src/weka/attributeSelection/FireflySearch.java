package weka.attributeSelection;

import weka.core.Instances;
import weka.core.OptionHandler;
import weka.core.TechnicalInformation;
import weka.core.TechnicalInformationHandler;

import java.util.BitSet;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: rrd09
 * Date: 05/12/12
 * Time: 21:55
 */
public class FireflySearch extends ASSearch implements OptionHandler, TechnicalInformationHandler {

    private static final long serialVersionUID = 4366460686402417836L;
    private SubsetEvaluator evaluator;
    private Random random;
    private int maxIteration = 500;
    private int numAttributes;
    private Firefly[] fireflies;
    private int numFireflies = 20;
    private BitSet best;
    private double bestScore = -Double.MAX_VALUE;
    private int seed;

    private double getMergedScore(double xi, double xj) {
        return xi + random.nextDouble() * (xi - xj);
    }

    private Firefly update(Firefly fi, Firefly fj) {
        BitSet temp = (BitSet) fi.getSubset().clone();
        double gamma = 0.1d / numAttributes;
        int distance = getDistance(fi, fj);

        double parameter = Math.exp(-gamma * distance * distance);
        int vel = (int) (parameter * distance);
        if (vel > 10) vel = 10;

        int index;
        for (int a = 0; a < vel; a++) {
            index = random.nextInt(numAttributes);
            temp.set(index, fj.getSubset().get(index));
        }

        return new Firefly(temp);
    }

    private double getBrightness(Firefly fi, Firefly fj) {
        //BitSet temp = (BitSet) fi.getSubset().clone();
        double gamma = 0.1d / numAttributes;
        int distance = getDistance(fi, fj);

        double parameter = Math.exp(-gamma * distance * distance);
        return fj.getMerit() * parameter * (numAttributes - fj.getSize()) / numAttributes;
    }

    @Override
    public int[] search(ASEvaluation ASEvaluator, Instances data) throws Exception {
        try {
            numAttributes = data.numAttributes() - 1;
            evaluator = NatureInspiredCommon.convert(ASEvaluator);
            //((InterruptingEvaluator) evaluator).turnOff();
            random = new Random(seed);
            bestScore = -Double.MAX_VALUE;
            best = null;
            fireflies = new Firefly[numFireflies];
            for (int f = 0; f < numFireflies; f++) {
                fireflies[f] = new Firefly(NatureInspiredCommon.randomBitSet(numAttributes, random));
                fireflies[f].assignMerit();
            }

            Firefly[] tempFireFlies;
            int currentIteration = 0;
            while (currentIteration++ < maxIteration) {
                tempFireFlies = new Firefly[numFireflies];
                for (int i = 0; i < numFireflies; i++) {
                    double maxBrightness = -Double.MAX_VALUE;
                    int maxIndex = -1;
                    for (int j = 0; j < numFireflies; j++) {
                        if (i != j) {
                            double brightness = getBrightness(fireflies[i], fireflies[j]);
                            if (brightness > maxBrightness) {
                                maxBrightness = brightness;
                                maxIndex = j;
                            }
                        }
                        //if ((i != j) && (fireflies[j].getMerit() > fireflies[i].getMerit()))
                    }
                    tempFireFlies[i] = update(fireflies[i], fireflies[maxIndex]);
                    tempFireFlies[i].assignMerit();
                    if (tempFireFlies[i].getMerit() < fireflies[i].getMerit())
                        tempFireFlies[i] = (Firefly) fireflies[i].clone();
                }
                fireflies = tempFireFlies;
                for (int i = 0; i < numFireflies; i++) {
                    if (fireflies[i].getMerit() > bestScore) {
                        bestScore = fireflies[i].getMerit();
                        best = (BitSet) fireflies[i].getSubset().clone();
                    } else if ((fireflies[i].getMerit() == bestScore) && (fireflies[i].getSize() < best.cardinality())) {
                        bestScore = fireflies[i].getMerit();
                        best = (BitSet) fireflies[i].getSubset().clone();
                    }
                }
            }
        } catch (MaximumEvaluationReachedException e) {
            return NatureInspiredCommon.toIntArray(best, numAttributes);
        }
        return NatureInspiredCommon.toIntArray(best, numAttributes);
    }

    private int getDistance(Firefly fa, Firefly fb) {
        BitSet xor = (BitSet) fa.getSubset().clone();
        xor.xor(fb.getSubset());
        return xor.cardinality();
        //return Math.abs(fa.getSize() - fb.getSize());
    }

    /*@Override
    public int[] search(ASEvaluation ASEvaluator, Instances data) throws Exception {
        try {
            numAttributes = data.numAttributes() - 1;
            evaluator = convertEvaluation(ASEvaluator);
            //maxIteration = maxIteration / numAttributes;
            random = new Random(seed);
            bestScore = -Double.MAX_VALUE;
            best = null;
            *//*BitSet full = new BitSet(numAttributes);
            for (int i = 0; i < numAttributes; i++) {
                full.set(i);
            }*//*
			//double fullScore = evaluator.evaluateSubset(full);
            fireflies = new Firefly[numAttributes];
            for (int f = 0; f < numAttributes; f++) {
                fireflies[f] = new Firefly(f, numAttributes);
                fireflies[f].assignMerit();
            }
            int currentIteration = 0;
            Firefly[] tempFireFlies;
            double[][] scores = new double[numAttributes][numAttributes];
            while (currentIteration < maxIteration) {
                tempFireFlies = new Firefly[numAttributes];
                for (int i = 0; i < numAttributes; i++) {
                    double minDifference = Double.MAX_VALUE;
                    int minIndex = -1;
                    for (int j = 0; j < numAttributes; j++) {
                        if (i != j) {
                            if (fireflies[j].getMerit() > fireflies[i].getMerit()) {
                                double score = 0;
                                if (j > i) {
                                    score = getScore(fireflies[j].mergeWith(fireflies[i]));
                                    scores[i][j] = score;
                                }
                                else {
                                    if (scores[j][i] == 0) score = getScore(fireflies[j].mergeWith(fireflies[i]));
                                    else score = scores[j][i];
                                }
//                                double score = getMergedScore(fireflies[i].getMerit(), fireflies[j].getMerit());
                                if (score > fireflies[j].getMerit()) {
                                    if (1.1 - score < minDifference) {
                                        minDifference = 1.1 - score;
                                        minIndex = j;
                                    }
                                }
                            }
                        }
                    }
                    if (minIndex >= 0) {
                        tempFireFlies[i] = merge(fireflies[i], fireflies[minIndex]);
                        tempFireFlies[i].assignMerit();
                    } else tempFireFlies[i] = (Firefly) fireflies[i].clone();
                    if (tempFireFlies[i].getMerit() > bestScore) {
                        bestScore = tempFireFlies[i].getMerit();
                        best = (BitSet) tempFireFlies[i].getSubset().clone();
                    } else if ((tempFireFlies[i].getMerit() == bestScore) && (tempFireFlies[i].getSize() < best.cardinality())) {
                        bestScore = tempFireFlies[i].getMerit();
                        best = (BitSet) tempFireFlies[i].getSubset().clone();
                    }
                }
                fireflies = tempFireFlies;
                currentIteration++;
                //System.out.println(currentIteration);
            }
        } catch (MaximumEvaluationReachedException e) {
            return attributeList(best);
        }
        return attributeList(best);
    }
*/
    @Override
    public Enumeration listOptions() {
        return null;
    }

    @Override
    public void setOptions(String[] options) throws Exception {

    }

    @Override
    public String[] getOptions() {
        return new String[0];
    }

    @Override
    public TechnicalInformation getTechnicalInformation() {
        return null;
    }

    private double getAttractiveness(Firefly fly1, Firefly fly2) throws Exception {
        return evaluator.evaluateSubset(fly1.mergeWith(fly2));
    }

    private Hashtable<BitSet, Double> lookupTable;

    public void setSeed(int seed) {
        this.seed = seed;
    }

    public Firefly merge(Firefly flyA, Firefly flyB) {
        BitSet merged = new BitSet();
        for (int i = flyA.getSubset().nextSetBit(0); i < numAttributes; i = flyA.getSubset().nextSetBit(i + 1)) {
            if (i == -1) break;
            merged.set(i);
        }
        for (int i = flyB.getSubset().nextSetBit(0); i < numAttributes; i = flyB.getSubset().nextSetBit(i + 1)) {
            if (i == -1) break;
            merged.set(i);
        }
        return new Firefly(merged);
    }

    protected class Firefly implements Cloneable {
        private BitSet subset;
        private double merit;

        public Firefly(int feature, int numAttributes) {
            subset = new BitSet(numAttributes);
            subset.set(feature);
        }

        public Firefly(BitSet subset) {
            this.subset = subset;
        }

        public BitSet getSubset() {
            return subset;
        }

        public void setSubset(BitSet subset) {
            this.subset = subset;
        }

        public double getMerit() {
            return merit;
        }

        public void setMerit(double merit) {
            this.merit = merit;
        }

        public void assignMerit() throws Exception {
            this.merit = NatureInspiredCommon.getMerit(subset, evaluator, numAttributes);
        }

        public int getSize() {
            return subset.cardinality();
        }

        public BitSet mergeWith(Firefly fly) {
            BitSet merged = new BitSet();
            for (int i = subset.nextSetBit(0); i < numAttributes; i = subset.nextSetBit(i + 1)) {
                if (i == -1) break;
                merged.set(i);
            }
            for (int i = fly.getSubset().nextSetBit(0); i < numAttributes; i = fly.getSubset().nextSetBit(i + 1)) {
                if (i == -1) break;
                merged.set(i);
            }
            return merged;
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            Firefly fly = new Firefly((BitSet) subset.clone());
            fly.setMerit(merit);
            return fly;
        }
    }
}