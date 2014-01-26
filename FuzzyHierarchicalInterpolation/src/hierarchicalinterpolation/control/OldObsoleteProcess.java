package hierarchicalinterpolation.control;

import fuzzy.FuzzyNumber;
import hierarchicalinterpolation.model.Observation;
import hierarchicalinterpolation.model.Rule;
import hierarchicalinterpolation.model.RuleComparator;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by IntelliJ IDEA.
 * User: shj1
 * Date: 04/08/11
 * Time: 13:22
 * To change this template use File | Settings | File Templates.
 */
public class OldObsoleteProcess {

    /*private boolean verbose = false;

    public static void printRules(ArrayList<Rule> rules) {
        for (int i = 0; i < rules.size(); i++) {
            for (FuzzyNumber fuzzyNumber : rules.get(i).getAntecedents()) {
                System.out.print(fuzzyNumber.toString() + " ");
            }
            System.out.println(rules.get(i).getConsequence().toString());
        }
    }


    private void sortRules(ArrayList<Rule> rules, Observation observation) {
        Collections.sort(rules, new RuleComparator(observation));
        //TestMain.printRules(rules);
    }

    public FuzzyNumber goFlat(ArrayList<Rule> rules, Observation observation) throws Exception {
        sortRules(rules, observation);
        ArrayList<Rule> closestRules = getClosestRules(observation, rules, 5);
        //TestMain.printRules(closestRules);
        FuzzyNumber[] subConsequences = new FuzzyNumber[observation.getAntecedents().length];
        FuzzyNumber accumulatedConsequence = new FuzzyNumber(0, 0, 0);
        for (int i = 0; i < observation.getAntecedents().length; i++) {
            subConsequences[i] = getSubConsequence(closestRules, observation.getAntecedents()[i], i);
            accumulatedConsequence = accumulatedConsequence.adds(subConsequences[i]);
        }
        return accumulatedConsequence.divides(observation.getAntecedents().length);
    }

    public FuzzyNumber go(ArrayList<Rule> rules, Observation observation) throws Exception {
        sortRules(rules, observation);
        ArrayList<Rule> closestRules = getClosestRules(observation, rules, 5);
        if (verbose) printRules(closestRules);
        FuzzyNumber subConsequence = getSubConsequenceMerged(closestRules,
                observation.getAntecedents()[0], observation.getAntecedents()[1],
                0, 1);
        if (verbose) System.out.println("SubConsequence Merged = " + subConsequence.toString());
        FuzzyNumber backwardInterpolatedObservation = backwardFuzzyInterpolation(subConsequence, rules, 2);
        if (verbose)
            System.out.println("backwardInterpolatedObservation = " + backwardInterpolatedObservation.toString());
        for (int i = 2; i < observation.getAntecedents().length; i++) {
            subConsequence = getSubConsequenceMerged(closestRules,
                    backwardInterpolatedObservation, observation.getAntecedents()[i],
                    i, i);
            if (verbose) System.out.println("SubConsequence Merged = " + subConsequence.toString());
            if (i == observation.getAntecedents().length - 1) return subConsequence;
            backwardInterpolatedObservation = backwardFuzzyInterpolation(subConsequence, rules, i);
            if (verbose)
                System.out.println("backwardInterpolatedObservation = " + backwardInterpolatedObservation.toString());
        }
        return subConsequence;
    }

    public FuzzyNumber goBackward(ArrayList<Rule> rules, Observation observation) throws Exception {
        sortRules(rules, observation);
        ArrayList<Rule> closestRules = getClosestRules(observation, rules, 5);
        if (verbose) printRules(closestRules);
        FuzzyNumber subConsequence = getSubConsequenceMerged(closestRules,
                observation.getAntecedents()[2], observation.getAntecedents()[1],
                2, 1);
        if (verbose) System.out.println("SubConsequence Merged = " + subConsequence.toString());
        FuzzyNumber backwardInterpolatedObservation = backwardFuzzyInterpolation(subConsequence, rules, 0);
        if (verbose)
            System.out.println("backwardInterpolatedObservation = " + backwardInterpolatedObservation.toString());
        for (int i = 0; i >= 0; i--) {
            subConsequence = getSubConsequenceMerged(closestRules,
                    backwardInterpolatedObservation, observation.getAntecedents()[i],
                    i, i);
            if (verbose) System.out.println("SubConsequence Merged = " + subConsequence.toString());
            if (i == observation.getAntecedents().length - 1) return subConsequence;
            backwardInterpolatedObservation = backwardFuzzyInterpolation(subConsequence, rules, i);
            if (verbose)
                System.out.println("backwardInterpolatedObservation = " + backwardInterpolatedObservation.toString());
        }
        return subConsequence;
    }

    public FuzzyNumber scaleNew(FuzzyNumber fuzzyNumber, double scaleFactor) {
        double newCentroid = fuzzyNumber.getRep() +
                (((fuzzyNumber.getCentroid() - fuzzyNumber.getLeftShoulder()) * scaleFactor) -
                        ((fuzzyNumber.getRightShoulder() - fuzzyNumber.getCentroid()) * scaleFactor)) / 3;
        double newLeftShoulder = newCentroid - (fuzzyNumber.getCentroid() - fuzzyNumber.getLeftShoulder()) * scaleFactor;
        double newRightShoulder = newCentroid + (fuzzyNumber.getRightShoulder() - fuzzyNumber.getCentroid()) * scaleFactor;
        return new FuzzyNumber(newLeftShoulder, newCentroid, newRightShoulder);
    }

    *//*public static FuzzyNumber move(FuzzyNumber aDashScaled, double moveRate) {
        return new FuzzyNumber(aDashScaled.getLeftShoulder() + moveRate,
                aDashScaled.getCentroid() - 2 * moveRate,
                aDashScaled.getRightShoulder() + moveRate);
    }*//*

    private FuzzyNumber getIntermediateValue(FuzzyNumber fuzzyNumber1, FuzzyNumber fuzzyNumber2,
                                             double relativePlacementFactor) {
        return new FuzzyNumber((1d - relativePlacementFactor) * fuzzyNumber1.getLeftShoulder() +
                relativePlacementFactor * fuzzyNumber2.getLeftShoulder(),
                (1d - relativePlacementFactor) * fuzzyNumber1.getCentroid() +
                        relativePlacementFactor * fuzzyNumber2.getCentroid(),
                (1d - relativePlacementFactor) * fuzzyNumber1.getRightShoulder() +
                        relativePlacementFactor * fuzzyNumber2.getRightShoulder());
    }
    *//*public FuzzyNumber moveBad(FuzzyNumber fuzzyNumber, double moveFactor) {
        double newLeftShoulder = fuzzyNumber.getLeftShoulder() + moveFactor;
        double newCentroid = fuzzyNumber.getCentroid() - 2 * moveFactor;
        double newRightShoulder = fuzzyNumber.getRightShoulder() + moveFactor;
        return new FuzzyNumber(newLeftShoulder, newCentroid, newRightShoulder);
    }*//*

    public FuzzyNumber interpolate(FuzzyNumber aDash, FuzzyNumber aStar, FuzzyNumber bDash) throws Exception {
        if (verbose) System.out.println("aDash = " + aDash);
        if (verbose) System.out.println("aStar = " + aStar);
        double scaleFactor = calculateScaleFactor(aDash, aStar);
        if (verbose) System.out.println("scaleFactor = " + scaleFactor);
        FuzzyNumber scaledSubConsequenceDash = scale(aDash, scaleFactor);
        double moveFactor = calculateMoveFactor(scaledSubConsequenceDash, aStar);
        if (verbose) System.out.println("moveFactor = " + moveFactor);
        return move(scale(bDash, scaleFactor), moveFactor);
    }

    private FuzzyNumber scale(FuzzyNumber fuzzyNumber, double scaleRate) throws Exception {
        if (scaleRate < 0) throw new Exception("Scale Rate (" + scaleRate + ") cannot be less than 0");
        double newLeftShoulder = (fuzzyNumber.getLeftShoulder() * (1 + 2 * scaleRate) +
                fuzzyNumber.getCentroid() * (1 - scaleRate) +
                fuzzyNumber.getRightShoulder() * (1 - scaleRate)) / 3;
        double newCentroid = (fuzzyNumber.getLeftShoulder() * (1 - scaleRate) +
                fuzzyNumber.getCentroid() * (1 + 2 * scaleRate) +
                fuzzyNumber.getRightShoulder() * (1 - scaleRate)) / 3;
        double newRightShoulder = (fuzzyNumber.getLeftShoulder() * (1 - scaleRate) +
                fuzzyNumber.getCentroid() * (1 - scaleRate) +
                fuzzyNumber.getRightShoulder() * (1 + 2 * scaleRate)) / 3;
        return new FuzzyNumber(newLeftShoulder, newCentroid, newRightShoulder);
    }

    private FuzzyNumber move(FuzzyNumber fuzzyNumber, double moveRatio) {
        double moveRate = (moveRatio >= 0) ? moveRatio * (fuzzyNumber.getCentroid() - fuzzyNumber.getLeftShoulder()) / 3
                : moveRatio * (fuzzyNumber.getRightShoulder() - fuzzyNumber.getCentroid()) / 3;
        return new FuzzyNumber(fuzzyNumber.getLeftShoulder() + moveRate,
                fuzzyNumber.getCentroid() - 2 * moveRate,
                fuzzyNumber.getRightShoulder() + moveRate);
    }

    private Rule[] getClosestRules(FuzzyNumber subConsequence,
                                   ArrayList<Rule> rules) {
        Rule[] closestRules = new Rule[2];
        double[] distances = new double[rules.size()];
        for (int i = 0; i < rules.size(); i++) {
            distances[i] = rules.get(i).getConsequence().distanceTo(subConsequence);
        }
        double minDistance1 = Double.MAX_VALUE;
        int minIndex1 = 0;
        double minDistance2 = Double.MAX_VALUE;
        int minIndex2 = 0;

        for (int i = 0; i < rules.size(); i++) {
            if ((Math.abs(distances[i]) < minDistance1) & (distances[i] * distances[minIndex1] < 0)) {
                minDistance2 = minDistance1;
                minIndex2 = minIndex1;
                minDistance1 = Math.abs(distances[i]);
                minIndex1 = i;
            } else if ((Math.abs(distances[i]) < minDistance2) & (distances[i] * distances[minIndex1] < 0)) {
                minDistance2 = Math.abs(distances[i]);
                minIndex2 = i;
            }
        }
        closestRules[0] = rules.get(minIndex1).clone();
        closestRules[1] = rules.get(minIndex2).clone();
        return closestRules;
    }

    private FuzzyNumber backwardFuzzyInterpolation(FuzzyNumber subConsequence, ArrayList<Rule> rules, int dimension) throws Exception {
        Rule[] closestRules = getClosestRules(subConsequence, rules);
        double relativePlacementFactor = getRelativePlacementFactor(subConsequence, closestRules[0], closestRules[1]);
        if (verbose) System.out.println("relativePlacementFactor = " + relativePlacementFactor);
        if (relativePlacementFactor >= 1) throw new Exception("Extraploation Here");
        FuzzyNumber subConsequenceDash = getIntermediateValue(closestRules[0].getConsequence(),
                closestRules[1].getConsequence(), relativePlacementFactor);

        FuzzyNumber backwardInterpolatedDash = getIntermediateValue(closestRules[0].getAntecedents()[dimension],
                closestRules[1].getAntecedents()[dimension], relativePlacementFactor);
        double scaleFactor = calculateScaleFactor(subConsequenceDash, subConsequence);
        FuzzyNumber scaledSubConsequenceDash = scale(subConsequenceDash, scaleFactor);
        double moveFactor = calculateMoveFactor(scaledSubConsequenceDash, subConsequence);
        return move(scale(backwardInterpolatedDash, scaleFactor), moveFactor);
    }

    private FuzzyNumber getIntermediateFuzzyTerm(ArrayList<Rule> closestRules,
                                                 int index, double[] normalisedWeights) {
        FuzzyNumber intermediateFuzzyTerm = new FuzzyNumber(0, 0, 0);
        for (int i = 0; i < normalisedWeights.length; i++) {
            intermediateFuzzyTerm = intermediateFuzzyTerm.adds(
                    closestRules.get(i).getAntecedents()[index].times(normalisedWeights[i]));
        }
        return intermediateFuzzyTerm;
    }

    private FuzzyNumber getIntermediateOutputVariable(ArrayList<Rule> closestRules, Observation observation,
                                                      int index, double[] normalisedWeights) {
        FuzzyNumber subConsequence = new FuzzyNumber(0, 0, 0);
        for (int i = 0; i < normalisedWeights.length; i++) {
            subConsequence = subConsequence.adds(
                    closestRules.get(i).getConsequence().times(normalisedWeights[i]));
        }
        return subConsequence;
    }

    private FuzzyNumber getSubConsequence(ArrayList<Rule> closestRules, FuzzyNumber aStar,
                                          int index) throws Exception {
        FuzzyNumber bDashDash = new FuzzyNumber(0, 0, 0);
        double[] normalisedWeights = getNormalisedWeights(closestRules, aStar, index);
        for (int i = 0; i < normalisedWeights.length; i++) {
            bDashDash = bDashDash.adds(
                    closestRules.get(i).getConsequence().times(normalisedWeights[i]));
        }
        FuzzyNumber aDashDash = getIntermediateFuzzyTerm(closestRules, index, normalisedWeights);
        if (verbose) System.out.println("aDashDash = " + aDashDash.toString());
        double delta = getDelta(aStar, aDashDash,
                closestRules.get(0).getMaxAntecedentRanges()[index],
                closestRules.get(0).getMinAntecedentRanges()[index]);
        if (verbose) System.out.println("delta = " + delta);
        FuzzyNumber aDash = aDashDash.adds(delta * (
                closestRules.get(0).getMaxAntecedentRanges()[index] -
                        closestRules.get(0).getMinAntecedentRanges()[index]));
        FuzzyNumber bDash = bDashDash.adds(delta * (
                closestRules.get(0).getMaxConsequenceRange() -
                        closestRules.get(0).getMinConsequenceRange()));
        if (verbose) System.out.println("bDash = " + bDash.toString());
        FuzzyNumber bStar = interpolate(aDash, aStar, bDash);
        if (verbose) System.out.println("bStar = " + bStar.toString());
        return bStar;
    }

    private double getDelta(FuzzyNumber aStar, FuzzyNumber aDashDash, double max, double min) {
        return (aStar.getRep() - aDashDash.getRep()) / (max - min);
    }

    private ArrayList<Rule> getClosestRules(Observation observation,
                                            ArrayList<Rule> rules, int numberOfClosestRules) {
        ArrayList<Rule> closestRules = new ArrayList<Rule>();
        for (int i = 0; i < numberOfClosestRules; i++) closestRules.add(rules.get(i).clone());
        return closestRules;
    }

    private FuzzyNumber getSubConsequenceMerged(ArrayList<Rule> closestRules,
                                                FuzzyNumber observation1, FuzzyNumber observation2,
                                                int index1, int index2) throws Exception {
        return getSubConsequence(closestRules, observation1, index1)
                .adds(getSubConsequence(closestRules, observation2, index2))
                .divides(2);
    }

    private double[] getNormalisedWeights(ArrayList<Rule> closestRules, FuzzyNumber observation, int index) {
        double[] weights = new double[closestRules.size()];
        double totalWeight = 0d;
        //TODO: asdas
        for (int i = 0; i < closestRules.size(); i++) {
            weights[i] = closestRules.get(i).getWeight(observation, index);
            totalWeight = totalWeight + weights[i];
        }
        for (int i = 0; i < weights.length; i++) {
            weights[i] = weights[i] / totalWeight;
        }
        return weights;
    }

    public double calculateScaleFactor(FuzzyNumber fuzzyNumberDash, FuzzyNumber fuzzyNumberStar) {
        return (fuzzyNumberStar.getRightShoulder() - fuzzyNumberStar.getLeftShoulder()) /
                (fuzzyNumberDash.getRightShoulder() - fuzzyNumberDash.getLeftShoulder());
    }


    public double calculateMoveFactor(FuzzyNumber fuzzyNumberScaled, FuzzyNumber fuzzyNumberStar) {
        if (fuzzyNumberStar.getLeftShoulder() >= fuzzyNumberScaled.getLeftShoulder()) {
            return (fuzzyNumberStar.getLeftShoulder() - fuzzyNumberScaled.getLeftShoulder()) * 3 /
                    (fuzzyNumberScaled.getCentroid() - fuzzyNumberScaled.getLeftShoulder());
        } else {
            return (fuzzyNumberStar.getLeftShoulder() - fuzzyNumberScaled.getLeftShoulder()) * 3 /
                    (fuzzyNumberScaled.getRightShoulder() - fuzzyNumberScaled.getCentroid());
        }
    }

    private double getRelativePlacementFactor(FuzzyNumber subConsequence, Rule rule1, Rule rule2) {
        double relativePlacementFactor = 0d;
        relativePlacementFactor = subConsequence.absDistanceTo(rule1.getConsequence()) /
                rule1.getConsequence().absDistanceTo(rule2.getConsequence());
        return relativePlacementFactor;
    }*/

/*public static FuzzyNumber scale(FuzzyNumber aDash, FuzzyNumber aStar) throws Exception {
double scaleRate = (aStar.getRightShoulder() - aStar.getLeftShoulder()) /
        (aDash.getRightShoulder() - aDash.getLeftShoulder());
if (scaleRate < 0) throw new Exception("Scale Rate (" + scaleRate + ") cannot be less than 0");

double newLeftShoulder, newRightShoulder, newCentroid;
*//*if (Double.isInfinite(scaleRate)) {
            newLeftShoulder = aStar.getLeftShoulder();
            newRightShoulder = aStar.getRightShoulder();
            newCentroid = aDash.getRep() * 3 - newLeftShoulder - newRightShoulder;
        } else {*//*
            newLeftShoulder = (aDash.getLeftShoulder() * (1 + 2 * scaleRate) +
                    aDash.getCentroid() * (1 - scaleRate) +
                    aDash.getRightShoulder() * (1 - scaleRate)) / 3;
            newCentroid = (aDash.getLeftShoulder() * (1 - scaleRate) +
                    aDash.getCentroid() * (1 + 2 * scaleRate) +
                    aDash.getRightShoulder() * (1 - scaleRate)) / 3;
            newRightShoulder = (aDash.getLeftShoulder() * (1 - scaleRate) +
                    aDash.getCentroid() * (1 - scaleRate) +
                    aDash.getRightShoulder() * (1 + 2 * scaleRate)) / 3;
        //}

        return new FuzzyNumber(newLeftShoulder, newCentroid, newRightShoulder);
    }*/


}
