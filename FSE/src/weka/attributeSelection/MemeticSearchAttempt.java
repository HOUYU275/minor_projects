/**
 * Created with IntelliJ IDEA.
 * User: rrd09
 * Date: 05/12/12
 * Time: 11:06
 */
package weka.attributeSelection;

import weka.core.Instances;
import weka.core.OptionHandler;
import weka.core.TechnicalInformation;
import weka.core.TechnicalInformationHandler;

import java.io.Serializable;
import java.util.*;

public class MemeticSearchAttempt extends ASSearch implements OptionHandler, TechnicalInformationHandler {

    private static final long serialVersionUID = -1618264232838472679L;

    private SubsetEvaluator evaluator;
    private int numAttributes;

    private Vector<Chromosome> chromosomes;
    private BitSet globalBest;
    private double globalBestMerit;
    private BitSet localBest;
    private double localBestMerit;
    private double sumMerit;
    private Random random;
    private int seed = 1;

    private int numChromosomes = 20;
    private double crossoverRate = 0.6;
    private double mutationRate = 0.1;
    private int maxGenerations = 5000;

    public MemeticSearchAttempt() {
    }

    public int[] search(ASEvaluation ASEval, Instances data)
            throws Exception {

        globalBest = null;
        globalBestMerit = -Double.MAX_VALUE;
        localBest = null;
        localBestMerit = -Double.MAX_VALUE;

        evaluator = convertEvaluation(ASEval);
        numAttributes = data.numAttributes() - 1;
        random = new Random(seed);
        //lookupTable = null;

        chromosomes = new Vector<>(numChromosomes);
        for (int c = 0; c < numChromosomes; c++) {
            chromosomes.add(new Chromosome(NatureInspiredCommon.randomBitSet(numAttributes, random)));
        }
        evaluateChromosomes();

        int currentGeneration = 0;
        do {
            generation();
            evaluateChromosomes();
            Collections.sort(chromosomes);
            quickLocalSearch(chromosomes.get(numChromosomes - 1));
            quickLocalSearch(chromosomes.get(numChromosomes - 2));
        } while (currentGeneration++ < maxGenerations);
        return attributeList(globalBest);
    }

    private int[] attributeList(BitSet group) {
        int[] features = new int[group.cardinality()];
        int index = 0;
        for (int i = group.nextSetBit(0); i < numAttributes; i = group.nextSetBit(i + 1)) {
            if (i < 0) break;
            features[index++] = i;
        }
        return features;
    }

    private void localSearch(Chromosome chromosome) throws Exception {
        BitSet temp = (BitSet) chromosome.getBitSet().clone();
        double score = chromosome.getMerit();
        double tempScore, bestDifference = -Double.MAX_VALUE;
        int addIndex = -1;
        //add
        for (int i = temp.nextClearBit(0); i < numAttributes; i = temp.nextClearBit(i + 1)) {
            if (i == -1) break;
            temp.set(i);
            tempScore = evaluator.evaluateSubset(temp);
            if (tempScore - score > bestDifference) {
                bestDifference = tempScore - score;
                addIndex = i;
            }
            temp.clear(i);
        }
        if (bestDifference < 0) return;
        temp.set(addIndex);
        int deleteIndex = -1;
        bestDifference = -Double.MAX_VALUE;
        score = evaluator.evaluateSubset(temp);
        //delete
        for (int i = temp.nextSetBit(0); i < numAttributes; i = temp.nextSetBit(i + 1)) {
            if (i == -1) break;
            temp.clear(i);
            tempScore = evaluator.evaluateSubset(temp);
            if (tempScore - score > bestDifference) {
                bestDifference = tempScore - score;
                deleteIndex = i;
            }
            temp.set(i);
        }
        if (bestDifference > 0) temp.clear(deleteIndex);
        chromosome.setBitSet(temp);
        chromosome.assignMerit();
    }

    private void quickLocalSearch(Chromosome chromosome) throws Exception {
        BitSet temp = (BitSet) chromosome.getBitSet().clone();
        double score = chromosome.getMerit();
        double tempScore, bestDifference = -Double.MAX_VALUE;
        int bestIndex = -1;
        int iterations = random.nextInt(numAttributes);
        for (int i = 0; i < iterations; i++) {
            temp.flip(i);
            tempScore = evaluator.evaluateSubset(temp);
            if (tempScore - score > bestDifference) {
                bestDifference = tempScore - score;
                bestIndex = i;
            }
            temp.flip(i);
        }
        if (bestDifference > 0) chromosome.getBitSet().flip(bestIndex);
        chromosome.assignMerit();
    }

    private void generation() throws Exception {
        //Collections.sort(chromosomes);
        Vector<Chromosome> newChromosomes = new Vector<>(numChromosomes);
        newChromosomes.add(new Chromosome((BitSet) globalBest.clone()));
        newChromosomes.add(new Chromosome((BitSet) globalBest.clone()));

        int parent1, parent2;
        for (int j = 2; j < numChromosomes; j += 2) {
            parent1 = select();
            parent2 = select();
            newChromosomes.add((Chromosome) chromosomes.get(parent1).clone());
            newChromosomes.add((Chromosome) chromosomes.get(parent2).clone());
            // if parents are equal mutate one bit
            if (parent1 == parent2) newChromosomes.get(j).getBitSet().flip(random.nextInt(numAttributes));
            else {
                // crossover
                double r = random.nextDouble();
                if (numAttributes >= 3) {
                    if (r < crossoverRate) {
                        // cross point
                        int cp = random.nextInt(numAttributes - 2) + 1;
                        for (int i = 0; i < cp; i++) {
                            newChromosomes.get(j).getBitSet().set(i, chromosomes.get(parent2).getBitSet().get(i));
                            newChromosomes.get(j + 1).getBitSet().set(i, chromosomes.get(parent1).getBitSet().get(i));
                        }
                    }
                }
                // mutate
                for (int i = 0; i < numAttributes; i++) {
                    if (random.nextDouble() < mutationRate) newChromosomes.get(j).getBitSet().flip(i);
                    if (random.nextDouble() < mutationRate) newChromosomes.get(j + 1).getBitSet().flip(i);
                }
            }
        }
        chromosomes = newChromosomes;
    }

    private int select() {
        double sum = 0;
        double r = random.nextDouble() * sumMerit;
        for (int i = 0; i < numChromosomes; i++) {
            sum += chromosomes.get(i).getMerit();
            if (sum >= r) return i;
        }
        return 0;
    }

    private SubsetEvaluator convertEvaluation(ASEvaluation ASEvaluator) throws Exception {
        if (!(ASEvaluator instanceof SubsetEvaluator))
            throw new Exception(ASEvaluator.getClass().getName() + " is not a Subset evaluator!");
        return (SubsetEvaluator) ASEvaluator;
    }

    private void evaluateChromosomes() throws Exception {
        localBest = null;
        localBestMerit = -Double.MAX_VALUE;
        sumMerit = 0;
        for (int c = 0; c < numChromosomes; c++) {
            chromosomes.get(c).assignMerit();
            sumMerit += chromosomes.get(c).getMerit();
            if (chromosomes.get(c).getMerit() > localBestMerit) {
                localBestMerit = chromosomes.get(c).getMerit();
                localBest = (BitSet) chromosomes.get(c).getBitSet().clone();
            }
            if (chromosomes.get(c).getMerit() > globalBestMerit) {
                globalBestMerit = chromosomes.get(c).getMerit();
                globalBest = (BitSet) chromosomes.get(c).getBitSet().clone();
            }
        }
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

    public void setMaxGenerations(int maxGenerations) {
        this.maxGenerations = maxGenerations;
    }



    /*private Hashtable<BitSet, Double> lookupTable;

    private double getScore(BitSet bitSet) throws Exception {
        if (lookupTable == null) lookupTable = new Hashtable<>(maxGenerations);
        double score = evaluator.evaluateSubset(bitSet);
        double size = bitSet.cardinality();
        if (!lookupTable.containsKey(bitSet)) {
            double merit = score + (numAttributes - size) * 0.1 / numAttributes;
            lookupTable.put(bitSet, merit);
            return merit;
        } else {
            return lookupTable.get(bitSet);
        }
    }*/

    protected class Chromosome implements Cloneable, Serializable, Comparable {
        private static final long serialVersionUID = -2930607837482622224L;
        private BitSet bitSet;
        private double merit;

        public Chromosome() {
        }

        public Chromosome(BitSet bitSet) {
            this.bitSet = bitSet;
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            super.clone();
            Chromosome temp = new Chromosome();
            temp.setMerit(merit);
            temp.setBitSet((BitSet) bitSet.clone());
            return temp;
        }

        @Override
        public int compareTo(Object o) {
            if (this.merit > ((Chromosome) o).getMerit()) return 1;
            if (this.merit < ((Chromosome) o).getMerit()) return -1;
            return 0;
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
    }

}
