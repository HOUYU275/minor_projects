package weka.attributeSelection;

import weka.core.Instances;
import weka.core.OptionHandler;
import weka.core.TechnicalInformation;
import weka.core.TechnicalInformationHandler;

import java.util.BitSet;
import java.util.Enumeration;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: Ren
 * Date: 06/12/12
 * Time: 14:06
 */
public class ArtificialBeeColonySearch extends ASSearch implements OptionHandler, TechnicalInformationHandler {

    private static final long serialVersionUID = -5935654046444637146L;
    private SubsetEvaluator evaluator;
    private Random random;
    private int seed;
    private int maxIteration = 1000;
    private int numAttributes;
    //private EmployedBee[] employedBees;

    private int numBees = 20;

    private BitSet best = null;
    private double bestMerit = -Double.MAX_VALUE;

    public int getMaxIteration() {
        return maxIteration;
    }

    public void setMaxIteration(int maxIteration) {
        this.maxIteration = maxIteration;
    }

    @Override
    public int[] search(ASEvaluation ASEvaluator, Instances data) throws Exception {

        try {
            numAttributes = data.numAttributes() - 1;
            evaluator = NatureInspiredCommon.convert(ASEvaluator);
            maxIteration = 1000;
            if (evaluator instanceof FuzzyRoughSubsetEval) maxIteration /= 5;
            //((InterruptingEvaluator) evaluator).turnOff();
            best = null;
            bestMerit = -Double.MAX_VALUE;
            random = new Random(seed);

            EmployedBee[] employedBees = new EmployedBee[numBees];
            OnlookerBee[] onlookerBees = new OnlookerBee[numBees];
            for (int f = 0; f < numBees; f++) {
                employedBees[f] = new EmployedBee();
                onlookerBees[f] = new OnlookerBee();
            }

            double sumMerit;
            int currentIteration = 0;
            do {
                sumMerit = 0;
                for (int e = 0; e < numBees; e++) {
                    if (!employedBees[e].inspected) employedBees[e] = new EmployedBee();
                    else employedBees[e].adjust();
                    if (onlookerBees[e].getMerit() > bestMerit || (onlookerBees[e].getMerit() == bestMerit && onlookerBees[e].getBitSet().cardinality() < best.cardinality())) {
                        best = (BitSet) onlookerBees[e].getBitSet().clone();
                        bestMerit = onlookerBees[e].getMerit();
                    }
                    sumMerit += employedBees[e].getMerit();
                    employedBees[e].probability = sumMerit;
                    employedBees[e].inspected = false;
                }
                for (int o = 0; o < numBees; o++) {
                    double threshold = random.nextDouble() * sumMerit;
                    for (int e = 0; e < numBees; e++) {
                        if (employedBees[e].probability >= threshold) {
                            onlookerBees[o].inspect(employedBees[e]);
                            break;
                        }
                    }
                }
                //System.out.println(currentIteration + "\t" + bestMerit);
            } while (currentIteration++ < maxIteration);
        } catch (MaximumEvaluationReachedException e) {
            return NatureInspiredCommon.toIntArray(best, numAttributes);
        }

        return NatureInspiredCommon.toIntArray(best, numAttributes);
    }

    /*@Override
    public int[] search(ASEvaluation ASEvaluator, Instances data) throws Exception {

        try {
            numAttributes = data.numAttributes() - 1;
            evaluator = NatureInspiredCommon.convert(ASEvaluator);
            maxIteration = 1000;
            if (evaluator instanceof FuzzyRoughSubsetEval) maxIteration /= 5;
            employedBees = new EmployedBee[numBees];
            best = null;
            bestMerit = -Double.MAX_VALUE;
            random = new Random(seed);
            for (int f = 0; f < numAttributes; f++) {
                employedBees[f] = new EmployedBee();
            }

            int currentIteration = 0;
            BitSet[] mergedBitSets = null;
            do {
                double meritSum = 0;
                double meritBound = 0;
                for (int f = 0; f < numAttributes; f++) {
                    if (mergedBitSets != null) {
                        employedBees[f].setBitSet(mergedBitSets[f]);
                    }
                    employedBees[f].assignMerit();
                    if (employedBees[f].getMerit() > bestMerit) {
                        bestMerit = employedBees[f].getMerit();
                        best = (BitSet) employedBees[f].getBitSet().clone();
                    }
                    meritSum += employedBees[f].getMerit();
                }
                for (int f = 0; f < numAttributes; f++) {
                    meritBound += employedBees[f].getMerit();
                    employedBees[f].setProbabilityBound(meritBound / meritSum);
                }
                mergedBitSets = new BitSet[numAttributes];
                for (int f = 0; f < numAttributes; f++) {
                    double probability = random.nextDouble();
                    int selectedIndex = numAttributes - 1;
                    for (int e = 0; e < numAttributes; e++) {
                        if (employedBees[e].getProbabilityBound() > probability) {
                            selectedIndex = e;
                            break;
                        }
                    }
                    //if (getMergedScore(employedBees[f], employedBees[selectedIndex]) > employedBees[f].getMerit()) {
                    if (getMergedScore(employedBees[f].getMerit(), employedBees[selectedIndex].getMerit()) > employedBees[f].getMerit()) {
                        //mergedBitSets[f] = merge(employedBees[f].getBitSet(), employedBees[selectedIndex].getBitSet());
                        mergedBitSets[f] = update(employedBees[f].getBitSet(), employedBees[selectedIndex].getBitSet());
                    } else {
                        mergedBitSets[f] = NatureInspiredCommon.randomBitSet(numAttributes, random);
                    }
                }

                currentIteration++;
            } while (currentIteration < maxIteration);
        } catch (MaximumEvaluationReachedException e) {
            return NatureInspiredCommon.toIntArray(best, numAttributes);
        }

        return NatureInspiredCommon.toIntArray(best, numAttributes);
    }
*/

    private double getMergedScore(EmployedBee employedBee, EmployedBee employedBee1) throws Exception {
        BitSet merged = merge(employedBee.getBitSet(), employedBee1.getBitSet());
        return evaluator.evaluateSubset(merged);
    }

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

    private double getMergedScore(double xi, double xj) {
        return xi + random.nextDouble() * (xi - xj);
    }

    private BitSet merge(BitSet a, BitSet b) {
        BitSet merged = new BitSet();
        for (int i = a.nextSetBit(0); i < numAttributes; i = a.nextSetBit(i + 1)) {
            if (i == -1) break;
            merged.set(i);
        }
        for (int i = b.nextSetBit(0); i < numAttributes; i = b.nextSetBit(i + 1)) {
            if (i == -1) break;
            merged.set(i);
        }
        return merged;
    }

    private int getDistance(BitSet a, BitSet b) {
        BitSet xor = (BitSet) a.clone();
        xor.xor(b);
        return xor.cardinality();
    }

    private BitSet update(BitSet a, BitSet b) {
        BitSet temp = (BitSet) a.clone();
        int distance = getDistance(a, b);

        int changes = random.nextInt(distance);
        int index;
        for (int i = 0; i < changes; i++) {
            index = random.nextInt(numAttributes);
            temp.set(index, b.get(index));
        }

        return temp;
    }

    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    protected class Bee {
        private BitSet bitSet;
        private double merit;

        public Bee(BitSet bitSet) throws Exception {
            this.bitSet = bitSet;
        }

        public Bee() {
            this.bitSet = new BitSet();
        }

        public BitSet getBitSet() {
            return bitSet;
        }

        public void setBitSet(BitSet bitSet) {
            this.bitSet = bitSet;
        }

        public double getMerit() {
            return merit;
        }

        public void setMerit(double merit) {
            this.merit = merit;
        }

        public double assignMerit() throws Exception {
            merit = NatureInspiredCommon.getMerit(bitSet, evaluator, numAttributes);
            return merit;
        }
    }

    protected class EmployedBee extends Bee {
        private double probability;
        private boolean inspected = false;

        public EmployedBee(int f) throws Exception {
            super(new BitSet());
            getBitSet().set(f);
            //assignMerit();
        }

        public EmployedBee() throws Exception {
            super(NatureInspiredCommon.randomBitSet(numAttributes, random));
            assignMerit();
        }

        private void adjust() throws Exception {
            BitSet temp = (BitSet) getBitSet().clone();
            if (getBitSet().isEmpty()) setBitSet(NatureInspiredCommon.randomBitSet(numAttributes, random));
            int count = random.nextInt(getBitSet().cardinality()) + 1;
            for (int i = 0; i < count; i++) temp.flip(random.nextInt(numAttributes));
            double tempMerit = NatureInspiredCommon.getMerit(temp, evaluator, numAttributes);
            if (tempMerit > getMerit()) {// || (tempMerit == getMerit() && temp.cardinality() < getBitSet().cardinality())) {
                setBitSet(temp);
                setMerit(tempMerit);
            }
        }
    }

    protected class OnlookerBee extends Bee {

        public OnlookerBee(BitSet bitSet) throws Exception {
            super(bitSet);
            //assignMerit();
        }

        public OnlookerBee() {
            super();
        }

        public void inspect(EmployedBee employedBee) throws Exception {
            employedBee.inspected = true;
            adjust(employedBee);
        }

        private void adjust(EmployedBee employedBee) throws Exception {
            if (getBitSet() == null) {
                setBitSet((BitSet) employedBee.getBitSet().clone());
                setMerit(employedBee.getMerit());
                return;
            }
            BitSet temp = (BitSet) getBitSet().clone();
            BitSet xor = NatureInspiredCommon.xor(getBitSet(), employedBee.getBitSet());
            int count = xor.isEmpty() ? random.nextInt(numAttributes) : random.nextInt(xor.cardinality());
            if (count > 10) count = 10;
            int index;
            for (int i = 0; i < count; i++) {
                index = random.nextInt(numAttributes);
                temp.set(index, employedBee.getBitSet().get(index));
            }
            double tempMerit = NatureInspiredCommon.getMerit(temp, evaluator, numAttributes);
            if (tempMerit > employedBee.getMerit() || ((tempMerit == employedBee.getMerit()) && temp.cardinality() < employedBee.getBitSet().cardinality())) {
                setBitSet(temp);
                setMerit(tempMerit);
            } else if (employedBee.getMerit() > getMerit() || (employedBee.getMerit() == getMerit() && employedBee.getBitSet().cardinality() < getBitSet().cardinality())) {
                setBitSet((BitSet) employedBee.getBitSet().clone());
                setMerit(employedBee.getMerit());
            }
        }
    }

        /*private void adjust(EmployedBee employedBee) throws Exception {
            BitSet temp = (BitSet) employedBee.getBitSet().clone();
            int count = random.nextInt(employedBee.getBitSet().cardinality()) + 1;
            for (int i = 0; i < count; i++) temp.flip(random.nextInt(numAttributes));
            double tempMerit = NatureInspiredCommon.getMerit(temp, evaluator, numAttributes);
            if ((tempMerit > employedBee.getMerit()) || ((tempMerit == employedBee.getMerit()) && temp.cardinality() < employedBee.getBitSet().cardinality())) {
                setBitSet(temp);
                setMerit(tempMerit);
            } else {
                setBitSet((BitSet) employedBee.getBitSet().clone());
                setMerit(employedBee.getMerit());
            }
        }*/
}