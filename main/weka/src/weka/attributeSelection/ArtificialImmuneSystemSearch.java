package weka.attributeSelection;

import weka.core.Instances;
import weka.core.OptionHandler;
import weka.core.TechnicalInformation;
import weka.core.TechnicalInformationHandler;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 30/12/12
 * Time: 23:27
 */
public class ArtificialImmuneSystemSearch extends ASSearch implements OptionHandler, TechnicalInformationHandler{
    private static final long serialVersionUID = -295745847400777911L;
    private SubsetEvaluator evaluator;
    private Random random;
    private int maxIteration = 500;
    private int numAttributes;
    private int numAntibodies = 30;
    private BitSet best;
    private double bestMerit = -Double.MAX_VALUE;
    private int seed;

    @Override
    public int[] search(ASEvaluation ASEvaluator, Instances data) throws Exception {
        try {
            numAttributes = data.numAttributes() - 1;
            evaluator = NatureInspiredCommon.convert(ASEvaluator);
            random = new Random(seed);
            bestMerit = -Double.MAX_VALUE;
            best = null;
            Antibody[] antibodies = new Antibody[numAntibodies];
            for (int a = 0; a < numAntibodies; a++) {
                antibodies[a] = new Antibody(NatureInspiredCommon.randomBitSet(numAttributes, random));
                antibodies[a].assignMerit();
            }
            int currentIteration = 0;
            while (currentIteration++ < maxIteration) {
                Vector<Antibody> antibodyVector = new Vector<>();
                for (int a = 0; a < numAntibodies; a++) {
                    if (antibodies[a].getMerit() > bestMerit) {
                        bestMerit = antibodies[a].getMerit();
                        best = (BitSet) antibodies[a].getBitSet().clone();
                    } else if ((antibodies[a].getMerit() == bestMerit) && (antibodies[a].getBitSet().cardinality() < best.cardinality())) {
                        bestMerit = antibodies[a].getMerit();
                        best = (BitSet) antibodies[a].getBitSet().clone();
                    }
                    antibodyVector.add(antibodies[a]);
                    clone(antibodies[a], antibodyVector);
                }
                for (int i = 0; i < numAntibodies / 10; i++) {
                    Antibody a = new Antibody(NatureInspiredCommon.randomBitSet(numAttributes, random));
                    a.assignMerit();
                    antibodyVector.add(a);
                }
                Collections.sort(antibodyVector);
                for (int a = 0; a < numAntibodies; a++) antibodies[a] = antibodyVector.get(antibodyVector.size() - a - 1);
            }
        } catch (MaximumEvaluationReachedException e) {
            return NatureInspiredCommon.toIntArray(best, numAttributes);
        }
        return NatureInspiredCommon.toIntArray(best, numAttributes);
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

    public void setSeed(int seed) {
        this.seed = seed;
    }

    public void setMaxIteration(int maxIteration) {
        this.maxIteration = maxIteration;
    }

    private void clone(Antibody antibody, Vector<Antibody> antibodyVector) throws Exception {
        int cloneCount = (int) Math.round(Math.exp(antibody.getMerit() - bestMerit) * numAntibodies / 10d);
        int mutateCount = numAntibodies / 10 - cloneCount + 1;
        for (int i = 0; i < cloneCount; i++) {
            Antibody cloned = (Antibody) antibody.clone();
            for (int j = 0; j < mutateCount; j++) cloned.getBitSet().flip(random.nextInt(numAttributes));
            cloned.assignMerit();
            antibodyVector.add(cloned);
        }
    }

    class Antibody implements Cloneable, Comparable {
        private BitSet bitSet;
        private double merit;

        public Antibody(BitSet bitSet) {
            this.bitSet = bitSet;
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

        public void assignMerit() throws Exception {
            this.merit = NatureInspiredCommon.getMerit(bitSet, evaluator, numAttributes);
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            Antibody fly = new Antibody((BitSet) bitSet.clone());
            fly.setMerit(merit);
            return fly;
        }

        @Override
        public int compareTo(Object o) {
            Antibody a = (Antibody) o;
            if (merit > a.getMerit()) return 1;
            else if ((merit == a.getMerit()) && (bitSet.cardinality() < a.getBitSet().cardinality())) return 1;
            else if ((merit == a.getMerit()) && (bitSet.cardinality() == a.getBitSet().cardinality())) return 0;
            return -1;
        }
    }

}