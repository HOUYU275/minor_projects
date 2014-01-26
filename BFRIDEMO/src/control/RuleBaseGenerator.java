package control;

import fuzzy.FuzzyNumber;
import model.Observation;
import model.Rule;
import model.TestFunction;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 28/07/11
 * Time: 20:11
 * To change this template use File | Settings | File Templates.
 */
public class RuleBaseGenerator {

    private double[] minAntecedentRanges;
    private double[] maxAntecedentRanges;
    private double minConsequenceRange = Double.MAX_VALUE;
    private double maxConsequenceRange = Double.MIN_VALUE;

    private void containRule(ArrayList<Rule> rules, Rule rule) {
        for (int i = 0; i < rules.size(); i++) {
            if (rules.get(i).getAntecedents()[0].equals(rule.getAntecedents()[0]) &
                    rules.get(i).getAntecedents()[1].equals(rule.getAntecedents()[1]) &
                    rules.get(i).getAntecedents()[2].equals(rule.getAntecedents()[2])) {
                System.out.println("CNM");
            }
            /*boolean identical = false;

            for (int j = 0; j < rules.get(i).getAntecedents().length; j++) {
                identical = identical | rules.get(i).getAntecedents()[j].equals(rule.getAntecedents()[j]);
                if () identical = identical |
            }

            for (FuzzyNumber fuzzyNumber : rules.get(i).getAntecedents()) {
                if (fuzzyNumber.equals(rule.getAntecedents()[i]))
            }*/
        }
    }

    public int[] getNextIndices(int[] currentIndices, double[] ranges) {
        int[] nextIndices = new int[ranges.length];

        if (currentIndices == null) {
            for (int i = 0; i < nextIndices.length; i++) {
                nextIndices[i] = 0;
            }
            return nextIndices;
        }

        boolean increment = false;
        for (int i = nextIndices.length - 1; i >= 0; i--) {
            if (i == nextIndices.length - 1) {
                if (currentIndices[i] == ranges[i]) {
                    increment = true;
                    nextIndices[i] = 0;
                } else {
                    nextIndices[i] = currentIndices[i] + 1;
                }
            } else {
                if (increment == true) {
                    if (currentIndices[i] == ranges[i]) {
                        if (i == 0) return null;
                        increment = true;
                        nextIndices[i] = 0;
                    } else {
                        increment = false;
                        nextIndices[i] = currentIndices[i] + 1;
                    }
                } else {
                    nextIndices[i] = currentIndices[i];
                }
            }

        }
        return nextIndices;
    }

    public Observation newObservation(int numberOfVariables, double[] ranges) {
        FuzzyNumber[] observationAntecedents = new FuzzyNumber[numberOfVariables];
        Random random = new Random();
        double a1 = 1 + random.nextInt((int) ranges[0]);
        double a2 = 1 + random.nextInt((int) ranges[1]);
        double a3 = 1 + random.nextInt((int) ranges[2]);
        observationAntecedents[0] = new FuzzyNumber(a1 - 0.9, a1 + 0.1, a1 + 1.1);
        observationAntecedents[1] = new FuzzyNumber(a2 - 0.9, a2 + 0.1, a2 + 1.1);
        observationAntecedents[2] = new FuzzyNumber(a3 - 0.9, a3 + 0.1, a3 + 1.1);

        Observation observation = new Observation(observationAntecedents);
        return observation;
    }

    public double[] getCrispInputs(int numberOfVariables, Observation observation) {
        double[] crispInput = new double[numberOfVariables];
        for (int i = 0; i < crispInput.length; i++) {
            crispInput[i] = observation.getAntecedents()[i].getRep();
        }
        return crispInput;
    }

    private void assignValueRanges(double[][] antecedentCentroids) {

        minAntecedentRanges = new double[antecedentCentroids.length];
        maxAntecedentRanges = new double[antecedentCentroids.length];

        for (int i = 0; i < antecedentCentroids.length; i++) {
            minAntecedentRanges[i] = Double.MAX_VALUE;
            maxAntecedentRanges[i] = Double.MIN_VALUE;
            for (int j = 0; j < antecedentCentroids[i].length; j++) {
                if (antecedentCentroids[i][j] < minAntecedentRanges[i]) {
                    //System.out.println(antecedentCentroids[i][j]);
                    minAntecedentRanges[i] = antecedentCentroids[i][j];
                }
                if (antecedentCentroids[i][j] > maxAntecedentRanges[i])
                    maxAntecedentRanges[i] = antecedentCentroids[i][j];
            }
            minAntecedentRanges[i] = roundToOneDecimal(minAntecedentRanges[i] - 1d);
            maxAntecedentRanges[i] = roundToOneDecimal(maxAntecedentRanges[i] + 1d);
        }

        /*for (int i = 0; i < antecedentCentroids.length; i++) {
            System.out.println(i + " range = " + minAntecedentRanges[i] + " - " + maxAntecedentRanges[i]);
        }*/

    }

    public ArrayList<Rule> generateRules(TestFunction testFunction, int numberOfFuzzySetsPerVariable) {
        int numberOfRules = (int) (Math.pow(numberOfFuzzySetsPerVariable, testFunction.getNumberOfVariables()));
        ArrayList<Rule> rules = new ArrayList<Rule>();
        double[][] antecedentCentroids = new double[testFunction.getNumberOfVariables()][numberOfFuzzySetsPerVariable];
        double[] consequenceCentroids = new double[numberOfRules];
        Random random = new Random();

        for (int i = 0; i < testFunction.getNumberOfVariables(); i++) {
            double min = testFunction.getMinAntecedentRanges()[i];
            double max = testFunction.getMaxAntecedentRanges()[i];
            for (int j = 0; j < numberOfFuzzySetsPerVariable; j++) {
                double spread = (max - min) / numberOfFuzzySetsPerVariable;
                antecedentCentroids[i][j] = roundToOneDecimal(min + spread * j + random.nextDouble() * spread);
            }
        }

        assignValueRanges(antecedentCentroids);

        double[] ranges = new double[testFunction.getNumberOfVariables()];
        for (int i = 0; i < testFunction.getNumberOfVariables(); i++) {
            ranges[i] = numberOfFuzzySetsPerVariable - 1;
        }
        int[] currentIndices = getNextIndices(null, ranges);

        //double[] consequences = new double[numberOfRules];

        while (rules.size() < numberOfRules) {
            double[] input = new double[testFunction.getNumberOfVariables()];
            FuzzyNumber[] fuzzyInput = new FuzzyNumber[testFunction.getNumberOfVariables()];

            for (int i = 0; i < testFunction.getNumberOfVariables(); i++) {
                fuzzyInput[i] = new FuzzyNumber(
                        roundToOneDecimal(antecedentCentroids[i][currentIndices[i]] - random.nextDouble() * 1),
                        roundToOneDecimal(antecedentCentroids[i][currentIndices[i]]),
                        roundToOneDecimal(antecedentCentroids[i][currentIndices[i]] + random.nextDouble() * 1));
                input[i] = fuzzyInput[i].getRep();
            }

            consequenceCentroids[rules.size()] = roundToOneDecimal(testFunction.calculate(input));
            FuzzyNumber fuzzyResult = new FuzzyNumber(
                    consequenceCentroids[rules.size()] - 1d,
                    consequenceCentroids[rules.size()],
                    consequenceCentroids[rules.size()] + 1d);
            Rule rule = new Rule(fuzzyInput, fuzzyResult,
                    maxAntecedentRanges,
                    minAntecedentRanges,
                    testFunction.getMaxConsequenceRange(),
                    testFunction.getMinConsequenceRange());
            rules.add(rule);

            currentIndices = getNextIndices(currentIndices, ranges);
        }

        for (int i = 0; i < numberOfRules; i++) {
            if (consequenceCentroids[i] < minConsequenceRange) minConsequenceRange = consequenceCentroids[i];
            if (consequenceCentroids[i] > maxConsequenceRange) maxConsequenceRange = consequenceCentroids[i];
        }

        for (int i = 0; i < numberOfRules; i++) {
            rules.get(i).setMaxConsequenceRange(maxConsequenceRange + 1d);
            rules.get(i).setMinConsequenceRange(minConsequenceRange - 1d);
        }

        return rules;
    }

    public ArrayList<Rule> generate(TestFunction testFunction, int numberOfInput) {
        ArrayList<Rule> rules = new ArrayList<Rule>();
        double[][] repValues = new double[testFunction.getNumberOfVariables()][numberOfInput];
        double[][] leftValues = new double[testFunction.getNumberOfVariables()][numberOfInput];
        double[][] rightValues = new double[testFunction.getNumberOfVariables()][numberOfInput];
        double[] result = new double[40];
        double[] leftResult = new double[40];
        double[] rightResult = new double[40];
        Random random = new Random();
        for (int i = 0; i < testFunction.getNumberOfVariables(); i++) {
            double min = testFunction.getMinAntecedentRanges()[i];
            double max = testFunction.getMaxAntecedentRanges()[i];
            for (int j = 0; j < numberOfInput; j++) {
                int spread = (int) Math.floor((max - min) / numberOfInput);
                repValues[i][j] = min + spread * j + random.nextInt(spread);

            }
        }

        for (int j = 0; j < 38; j++) {
            double[] input = new double[testFunction.getNumberOfVariables()];
            FuzzyNumber[] fuzzyInput = new FuzzyNumber[testFunction.getNumberOfVariables()];
            for (int i = 0; i < testFunction.getNumberOfVariables(); i++) {
                input[i] = repValues[i][random.nextInt(numberOfInput)];
                fuzzyInput[i] = new FuzzyNumber(input[i] - 1d, input[i], input[i] + 1d);
            }
            result[j] = roundToOneDecimal(testFunction.calculate(input));
            leftResult[j] = result[j] - 1 - random.nextInt(2);
            rightResult[j] = 2 * result[j] - leftResult[j];
            FuzzyNumber fuzzyResult = new FuzzyNumber(leftResult[j], result[j], rightResult[j]);
            Rule rule = new Rule(fuzzyInput, fuzzyResult, testFunction.getMaxAntecedentRanges(),
                    testFunction.getMinAntecedentRanges(), testFunction.getMaxConsequenceRange(),
                    testFunction.getMinConsequenceRange());
            if (!rules.contains(rule)) {
                containRule(rules, rule);
                rules.add(rule);
            } else {
                j = j - 1;
            }
            /* (FuzzyNumber[] antecedents, FuzzyNumber consequence,
        double[] maxAntecedentRanges, double[] minAntecedentRanges,
        double maxConsequenceRange, double minConsequenceRange) {*/
        }

        double[] input38 = new double[testFunction.getNumberOfVariables()];
        FuzzyNumber[] fuzzyInput = new FuzzyNumber[testFunction.getNumberOfVariables()];
        for (int i = 0; i < testFunction.getNumberOfVariables(); i++) {
            input38[i] = repValues[i][0];
            fuzzyInput[i] = new FuzzyNumber(input38[i] - 1d, input38[i], input38[i] + 1d);
        }
        result[38] = roundToOneDecimal(testFunction.calculate(input38));
        leftResult[38] = result[38] - 1d;
        rightResult[38] = 2 * result[38] - leftResult[38];
        FuzzyNumber fuzzyResult = new FuzzyNumber(leftResult[38], result[38], rightResult[38]);
        Rule rule38 = new Rule(fuzzyInput, fuzzyResult, testFunction.getMaxAntecedentRanges(),
                testFunction.getMinAntecedentRanges(), testFunction.getMaxConsequenceRange(),
                testFunction.getMinConsequenceRange());
        rules.add(rule38);

        double[] input39 = new double[testFunction.getNumberOfVariables()];
        FuzzyNumber[] fuzzyInput39 = new FuzzyNumber[testFunction.getNumberOfVariables()];
        for (int i = 0; i < testFunction.getNumberOfVariables(); i++) {
            input39[i] = repValues[i][numberOfInput - 1];
            fuzzyInput39[i] = new FuzzyNumber(input39[i] - 1d, input39[i], input39[i] + 1d);
        }
        result[39] = roundToOneDecimal(testFunction.calculate(input39));
        leftResult[39] = result[39] - 1d;
        rightResult[39] = 2 * result[39] - leftResult[39];
        FuzzyNumber fuzzyResult39 = new FuzzyNumber(leftResult[39], result[39], rightResult[39]);
        Rule rule39 = new Rule(fuzzyInput39, fuzzyResult39, testFunction.getMaxAntecedentRanges(),
                testFunction.getMinAntecedentRanges(), testFunction.getMaxConsequenceRange(),
                testFunction.getMinConsequenceRange());
        rules.add(rule39);


        return rules;

    }

    private double roundToOneDecimal(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.#");
        return Double.valueOf(twoDForm.format(d));
    }

}
