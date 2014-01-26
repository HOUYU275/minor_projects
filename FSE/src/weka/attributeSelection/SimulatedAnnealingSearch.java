package weka.attributeSelection;

import weka.core.Instances;
import weka.core.OptionHandler;
import weka.core.TechnicalInformation;
import weka.core.TechnicalInformationHandler;

import java.util.BitSet;
import java.util.Enumeration;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: rrd09
 * Date: 04/12/12
 * Time: 18:08
 */
public class SimulatedAnnealingSearch extends ASSearch implements OptionHandler, TechnicalInformationHandler {

    private double maxTemperature = 200;
    private double coolingRate = 0.9;

    private int numAttributes;
    private SubsetEvaluator evaluator;
    private Random random;

    private BitSet best;
    private double bestScore = Double.NEGATIVE_INFINITY;

    private double numberChanges = 100;
    private int equilibriumCount;
    private double perturbationPercent = 0.2;

    private double boltzmannConstant = 1.3807E-23;

    private int maxIteration = 50000;
    private int counter = 0;

    private int seed;

    private boolean transitDown(double scoreDifference, double currentTemperature) {
        double probability = Math.exp(scoreDifference * -1 / (currentTemperature));
        //System.out.println(probability + " " + counter);
        counter++;
        return (random.nextDouble() < probability);
    }

    private BitSet perturbation(BitSet bitSet) {
        BitSet perturbedBitSet = (BitSet) bitSet.clone();
        int numberPerturbations = (int) (perturbationPercent * numAttributes) + 1;
        while (numberPerturbations > 0) {
            perturbedBitSet.flip(random.nextInt(numAttributes));
            numberPerturbations--;
        }
        //if (perturbedBitSet.cardinality() <= 0) perturbedBitSet.set(random.nextInt(numAttributes - 1));
        return perturbedBitSet;
    }

    private boolean checkEquilibrium() {
        return equilibriumCount == numberChanges;

    }

    @Override
    public int[] search(ASEvaluation ASEvaluator, Instances data) throws Exception {
        try {
            double currentTemperature = maxTemperature;
            int currentIteration = 0;
            evaluator = NatureInspiredCommon.convert(ASEvaluator);
            if (evaluator instanceof FuzzyRoughSubsetEval) currentTemperature /= 5;
            numAttributes = data.numAttributes() - 1;
            random = new Random(seed);
            best = new BitSet(numAttributes);
            BitSet current;
            while ((currentTemperature > 0.0001) && (currentIteration++ < maxIteration)) {
                current = perturbation(best);
                double currentScore = NatureInspiredCommon.getMerit(current, evaluator, numAttributes);
                double scoreDifference = bestScore - currentScore;
                if (scoreDifference <= 0 || transitDown(scoreDifference, currentTemperature)) {
                    best = current;
                    bestScore = currentScore;
                    equilibriumCount++;
                }
                if (checkEquilibrium()) {
                    equilibriumCount = 0;
                    currentTemperature = currentTemperature * coolingRate;
                    perturbationPercent = perturbationPercent * coolingRate;
                }
            }
        }
        catch (MaximumEvaluationReachedException e) {
            return NatureInspiredCommon.toIntArray(best, numAttributes);
        }
        return NatureInspiredCommon.toIntArray(best, numAttributes);
    }


    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    public int getMaxIteration() {
        return maxIteration;
    }

    public void setMaxIteration(int maxIteration) {
        this.maxIteration = maxIteration;
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
}
