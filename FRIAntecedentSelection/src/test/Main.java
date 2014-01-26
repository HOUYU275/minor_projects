package test;

import control.RuleBaseGenerator;
import control.TransformationBasedInterpolation;
import model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 15/01/13
 * Time: 17:40
 */
public class Main {

    private static double spread = 2;

    public static Temp t = new Temp();

    static double[] exportCFSWeights = new double[]{0.5, 0.001, 0.001, 0.001, 1, 0.001, 1, 0.5, 0.001, 0.001, 0.001, 0.001, 0.001, 1, 0.001};

    static double[] exportFRFSWeights = new double[]{0.7, 0.05, 0.001, 0.05, 0.8, 0.05, 0.7, 0.8, 0.05, 0.05, 0.05, 0.05, 0.05, 0.9, 0.05};

    public  static RuleBase ruleBase;

      /*      5,7,14 : 3
            4
            6
            13


            2,5,8,10,14 : 5
            1
            4
            7
            9
            13*/

    public static void main(String[] args) throws Exception {
        RuleBaseGenerator ruleBaseGenerator = new RuleBaseGenerator(new ComplexFunction(), spread);
        ruleBase = ruleBaseGenerator.generateRuleBase(0);
        ruleBase.load("export");

        Rule rule;
        Rule observation;
        Rule weightedObservationCFS;
        Rule weightedObservationFRFS;
        Rule trimmedObservationCFS;
        Rule trimmedObservationFRFS;

        ArrayList<Double> originalAccuracies = new ArrayList<Double>();
        ArrayList<Double> weightedAccuraciesCFS = new ArrayList<Double>();
        ArrayList<Double> weightedAccuraciesFRFS = new ArrayList<Double>();
        ArrayList<Double> trimmedAccuraciesCFS = new ArrayList<Double>();
        ArrayList<Double> trimmedAccuraciesFRFS = new ArrayList<Double>();

        Mask maskCFS = new Mask(new int[]{0, 4, 7, 13});
        RuleBase trimBaseCFS = (RuleBase) ruleBase.clone();
        trimBaseCFS.trim(maskCFS);

        Mask maskFRFS = new Mask(new int[]{1, 4, 7, 9, 13});
        RuleBase trimBaseFRFS = (RuleBase) ruleBase.clone();
        trimBaseFRFS.trim(maskFRFS);

        System.out.println("Count\tOriginal\tWeightedCFS\tWeightedFRFS");
        int count = 200;
        while (count > 0) {
            try {
                StringBuffer reportBuffer = new StringBuffer();

                rule = ruleBaseGenerator.generateRule();
                ArrayList<Integer> missingIndexes = new ArrayList<Integer>();
                missingIndexes.add(1);
                missingIndexes.add(4);
                missingIndexes.add(13);
                int trimmedIndex = new Random().nextInt(missingIndexes.size());
                int missingIndex = missingIndexes.get(trimmedIndex);//new Random().nextInt(rule.getAntecedents().length);

                observation = new Rule(rule.getAntecedents().clone(), rule.getConsequent().clone());

                observation = TransformationBasedInterpolation.interpolateWeighted(ruleBase, observation, exportFRFSWeights);
                //observation = TransformationBasedInterpolation.interpolate(ruleBase, observation);
                //observation.setAntecedent(missingIndex, new FuzzyNumber());
                weightedObservationCFS = new Rule(observation.getAntecedents().clone(), observation.getConsequent().clone());
                weightedObservationFRFS = new Rule(observation.getAntecedents().clone(), observation.getConsequent().clone());
                trimmedObservationCFS = new Rule(observation.getAntecedents().clone(), observation.getConsequent().clone());
                trimmedObservationFRFS = new Rule(observation.getAntecedents().clone(), observation.getConsequent().clone());

                observation = TransformationBasedInterpolation.backwardInterpolate(ruleBase, observation, missingIndex);
                double originalAccuracy = ruleBase.verify(observation, rule, missingIndex);
                reportBuffer.append(count + "\t" + originalAccuracy + "\t");

                weightedObservationCFS = TransformationBasedInterpolation.backwardInterpolateWeighted(ruleBase, weightedObservationCFS, missingIndex, exportCFSWeights);

                double weightedAccuracyCFS = ruleBase.verify(weightedObservationCFS, rule, missingIndex);
                reportBuffer.append("" + weightedAccuracyCFS + "\t");

                weightedObservationFRFS = TransformationBasedInterpolation.backwardInterpolateWeighted(ruleBase, weightedObservationFRFS, missingIndex, exportFRFSWeights);
                double weightedAccuracyFRFS = ruleBase.verify(weightedObservationFRFS, rule, missingIndex);
                reportBuffer.append("" + weightedAccuracyFRFS + "\t");

                trimmedObservationCFS.trim(maskCFS);
                trimmedObservationCFS = TransformationBasedInterpolation.backwardInterpolateWeighted(trimBaseCFS, trimmedObservationCFS, missingIndex, exportCFSWeights);
                double trimmedAccuracyCFS = Math.abs(trimmedObservationCFS.getAntecedents()[trimmedIndex].rep() - rule.getAntecedents()[missingIndex].rep())/rule.getRuleBase().getRange(missingIndex);
                reportBuffer.append("" + trimmedAccuracyCFS + "\t");

                trimmedObservationFRFS.trim(maskFRFS);
                trimmedObservationFRFS = TransformationBasedInterpolation.backwardInterpolateWeighted(trimBaseFRFS, trimmedObservationFRFS, missingIndex, exportFRFSWeights);
                double trimmedAccuracyFRFS = Math.abs(trimmedObservationFRFS.getAntecedents()[trimmedIndex].rep() - rule.getAntecedents()[missingIndex].rep())/rule.getRuleBase().getRange(missingIndex);
                reportBuffer.append("" + trimmedAccuracyFRFS);

                System.out.println(reportBuffer.toString());
                originalAccuracies.add(originalAccuracy);
                weightedAccuraciesCFS.add(weightedAccuracyCFS);
                weightedAccuraciesFRFS.add(weightedAccuracyFRFS);
                trimmedAccuraciesCFS.add(trimmedAccuracyCFS);
                trimmedAccuraciesFRFS.add(trimmedAccuracyFRFS);

                count--;
            } catch (NoMatchingRuleException e) {
                //e.printStackTrace();
            }
        }
        System.out.println("[O] " + average(originalAccuracies) + " - [WC] " + average(weightedAccuraciesCFS) + " - [WF] " + average(weightedAccuraciesFRFS) + " - [TC] " + average(trimmedAccuraciesCFS) + " - [TF] " + average(trimmedAccuraciesFRFS));
    }

    public static void forwardMain(String[] args) throws Exception {
        RuleBaseGenerator ruleBaseGenerator = new RuleBaseGenerator(new ComplexFunction(), spread);
//        RuleBase ruleBase = ruleBaseGenerator.generateRuleBase(500);
//        ruleBase.export("explosion");

        //RuleBaseGenerator ruleBaseGenerator = new RuleBaseGenerator(new ExplosionFunction(), spread);
//        RuleBase ruleBase = ruleBaseGenerator.generateRuleBase(500);
//        ruleBase.export("explosion");

        RuleBase ruleBase = ruleBaseGenerator.generateRuleBase(0);
        ruleBase.load("export");

        //[0 4 6 7 13]
        Mask mask1 = new Mask(new int[]{0, 4, 7, 13});
        RuleBase trimBase = (RuleBase) ruleBase.clone();
        trimBase.trim(mask1);

        Mask maskFRFS = new Mask(new int[]{1, 4, 7, 9, 13});
        RuleBase trimBaseFRFS = (RuleBase) ruleBase.clone();
        trimBaseFRFS.trim(maskFRFS);

        Rule observation;
        Rule weightedObservation;
        Rule weightedObservationFRFS;
        Rule trimmedObservation;
        Rule trimmedObservationFRFS;

        /*observation = new Rule(ruleBase.get(0));
        observation.getAntecedents()[0] = new FuzzyNumber(8.0, 8.5, 9);
        observation.getAntecedents()[1] = new FuzzyNumber(5.8, 7.5, 8);
        observation.getAntecedents()[2] = new FuzzyNumber(5.0, 5.5, 6.0);
        observation.getAntecedents()[3] = new FuzzyNumber(1.5, 2.0, 3);
        observation.getAntecedents()[4] = new FuzzyNumber(5.5, 6.0, 6.5);
        weightedObservation = new Rule(observation.getAntecedents().clone(), ruleBase);

        Rule result = TransformationBasedInterpolation.interpolate(ruleBase, observation);
        System.out.println(result);

        //double[] weights = new double[]{0.2765, 0.2461, 0.3312, 0.1163, 0.0299};
        weightedObservation = new Rule(observation.getAntecedents().clone(), ruleBase);
        weightedObservation = TransformationBasedInterpolation.interpolateWeighted(ruleBase, weightedObservation, exportCFSWeights);
        System.out.println(weightedObservation);

        //double[] weights2 = new double[]{0.2220, 0.3228, 0.2904, 0.0833, 0.0814};
        weightedObservation = new Rule(observation.getAntecedents().clone(), ruleBase);
        weightedObservation = TransformationBasedInterpolation.interpolateWeighted(ruleBase, weightedObservation, exportCFSWeights);
        System.out.println(weightedObservation);

        ruleBase.verify(weightedObservation);
*/
        ArrayList<Double> originalAccuracies = new ArrayList<Double>();
        ArrayList<Double> weightedAccuracies = new ArrayList<Double>();
        ArrayList<Double> weightedAccuraciesFRFS = new ArrayList<Double>();
        ArrayList<Double> trimmedAccuracies = new ArrayList<Double>();
        ArrayList<Double> trimmedAccuraciesFRFS = new ArrayList<Double>();

        System.out.println("Count\tOriginal\tWeightedCFS\tWeightedFRFS\tTrimmed\tTrimmedFRFS");
        int count = 200;
        while (count > 0) {
            try {
                StringBuffer reportBuffer = new StringBuffer();

                observation = ruleBaseGenerator.generateObservation(ruleBase, false);
                weightedObservation = new Rule(observation.getAntecedents().clone(), ruleBase);
                weightedObservationFRFS = new Rule(observation.getAntecedents().clone(), ruleBase);
                trimmedObservation = new Rule(observation.getAntecedents().clone(), trimBase);
                trimmedObservationFRFS = new Rule(observation.getAntecedents().clone(), trimBaseFRFS);

                observation = TransformationBasedInterpolation.interpolate(ruleBase, observation);
                //reportBuffer.append("[O] " + observation + "\n");

                double originalAccuracy = ruleBase.verify(observation);
                reportBuffer.append(count + "\t" + originalAccuracy + "\t");

                //weights = new double[]{0.2765, 0.2461, 0.3312, 0.1163, 0.0299};

                /*double[] weights = new double[]{
                        0.5, 0.001, 0.001, 0.001, 1, 0.001, 1, 0.5, 0.001, 0.001, 0.001, 0.001, 0.001, 1, 0.001
                };*/

                weightedObservation = TransformationBasedInterpolation.interpolateWeighted(ruleBase, weightedObservation, exportCFSWeights);
                //reportBuffer.append("[W] " + weightedObservation + "\n");

                double weightedAccuracy = ruleBase.verify(weightedObservation);
                reportBuffer.append("" + weightedAccuracy + "\t");

                weightedObservationFRFS = TransformationBasedInterpolation.interpolateWeighted(ruleBase, weightedObservationFRFS, exportFRFSWeights);
                double weightedAccuracyFRFS = ruleBase.verify(weightedObservationFRFS);
                reportBuffer.append("" + weightedAccuracyFRFS + "\t");

//                System.out.println("0 -> " + observation.getAntecedents()[0].rep());
//                observation = TransformationBasedInterpolation.backwardInterpolate(ruleBase, observation, 0);
//                System.out.println("0B -> " + observation.getAntecedents()[0].rep());

                trimmedObservation.trim(mask1);
                trimmedObservation = TransformationBasedInterpolation.interpolate(trimBase, trimmedObservation);
                double trimmedAccuracy = trimBase.verify(trimmedObservation);
                reportBuffer.append("" + trimmedAccuracy + "\t");

                trimmedObservationFRFS.trim(maskFRFS);
                trimmedObservationFRFS = TransformationBasedInterpolation.interpolate(trimBaseFRFS, trimmedObservationFRFS);
                double trimmedAccuracyFRFS = trimBaseFRFS.verify(trimmedObservationFRFS);
                reportBuffer.append("" + trimmedAccuracyFRFS);

//                System.out.println("0 -> " + trimmedObservation.getAntecedents()[0].rep());
//                trimmedObservation = TransformationBasedInterpolation.backwardInterpolate(trimBase, trimmedObservation, 0);
//                System.out.println("0B -> " + trimmedObservation.getAntecedents()[0].rep());

                System.out.println(reportBuffer.toString());
                originalAccuracies.add(originalAccuracy);
                weightedAccuracies.add(weightedAccuracy);
                weightedAccuraciesFRFS.add(weightedAccuracyFRFS);
                trimmedAccuracies.add(trimmedAccuracy);
                trimmedAccuracies.add(trimmedAccuracyFRFS);

                count--;
            } catch (NoMatchingRuleException e) {
                //e.printStackTrace();
            }
        }
        System.out.println("[O] " + average(originalAccuracies) + " - [WC] " + average(weightedAccuracies) + " - [WF] " + average(weightedAccuraciesFRFS) + " - [TC] " + average(trimmedAccuracies) + " - [TF] " + average(trimmedAccuraciesFRFS));
    }

    public static double average(ArrayList<Double> numbers) {
        double sum = 0;
        for (int d = 0; d < numbers.size(); d++) sum += numbers.get(d);
        return sum / numbers.size();
    }

    /*DynamicInterpolation interpolation = new DynamicInterpolation();
    RuleBaseGenerator ruleBaseGenerator = new RuleBaseGenerator(new SimpleFunction(), spread);

    RuleBase original = ruleBaseGenerator.generateRuleBase(100);
    RuleBase interpolated = new RuleBase(numAntecedents);

    int count = 0;
    int repeat = 0;
    int numBlocks = (int) Math.pow(numIntervals, numAntecedents);
    System.out.println("Iteration\tOriginal\tInterpolated\tCount\tTotal");
    //while (count != numBlocks) {
    while (repeat != 100) {
        if (interpolated.size() == targetBlockSize * numBlocks) {
            //while (interpolated.size() > targetBlockSize * blocks.length / 2) interpolated.remove((new Random()).nextInt(interpolated.size()));
            interpolated.clear();
        }
        interpolation.fill(original, interpolated, targetBlockSize * numBlocks);
        count = interpolation.dynamicInterpolation(original, interpolated, numIntervals, targetBlockSize);
        //System.out.println(repeat + "\t" + original.size() + "\t" + interpolated.size() + "\t" + count + "\t" + numBlocks);
        repeat++;
        System.out.println(t.pop());
    }
    System.out.println(repeat);*/
}
