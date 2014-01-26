package hierarchicalinterpolation.control;

import fuzzy.FuzzyNumber;
import fuzzy.TrapezoidalFuzzyNumber;
import fuzzy.TriangularFuzzyNumber;
import hierarchicalinterpolation.model.Observation;
import hierarchicalinterpolation.model.Rule;
import hierarchicalinterpolation.model.RuleBase;
import hierarchicalinterpolation.model.RuleComparator;
import originalharmonysearch.core.HarmonyMemory;
import originalharmonysearch.core.ValueRange;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 28/07/11
 * Time: 14:17
 * To change this template use File | Settings | File Templates.
 */
public class Process {

    private boolean verbose = true;
    private static int numberClosestRules = 2;
    //private static int slice = 20;
    static double[] temp0;
    static double[] temp1;


    public static double getDelta(FuzzyNumber aStar, FuzzyNumber a1, FuzzyNumber a2) {
        return 1f / Math.abs((a1.getRepresentativeValue() - aStar.getRepresentativeValue()) / (a1.getRepresentativeValue() - a2.getRepresentativeValue()));
    }


    public static float[][] getNormalisedTermWeights(Observation observation, Rule[] rules, int missingIndex1, int missingIndex2) {
        float[][] termWeights = calculateTermWeights(observation, rules, missingIndex1, missingIndex2);
        float wSum = 0;
        for (int i = 0; i < observation.getNumberOfAntecedents(); i++) {
            if (i != missingIndex1 && i != missingIndex2) {
                wSum = 0;
                for (int j = 0; j < rules.length; j++) {
                    wSum += termWeights[j][i];
                }
                for (int j = 0; j < rules.length; j++) {
                    termWeights[j][i] = termWeights[j][i] / wSum;
                    //System.out.println("A*" + i + "NormlisedtermWeights " + ":" + termWeights[j][i]);
                }
            }
        }
        if (missingIndex1 < observation.getNumberOfAntecedents() && missingIndex2 < observation.getNumberOfAntecedents()) {
            wSum = 0;
            for (int j = 0; j < rules.length; j++) {
                wSum += termWeights[j][observation.getNumberOfAntecedents()];
            }
            for (int j = 0; j < rules.length; j++) {
                termWeights[j][observation.getNumberOfAntecedents()] = termWeights[j][observation.getNumberOfAntecedents()] / wSum;
                //System.out.println("B*" + "NormlisedtermWeights to B" + j + ":" + termWeights[j][observation.getNumberOfAntecedents()]);
            }
        }
        /*    System.out.println("Normalised Weights:");
        printArrays(termWeights);*/
        return termWeights;
    }

    public static float[][] calculateTermWeights(Observation observation, Rule[] rules, int missingIndex1, int missingIndex2) {
        //termWeights is a N*(K+1) matrix of N rules, and K antecedents + 1 consequence
        float[][] termWeights = new float[rules.length][observation.getAntecedents().length + 1];
        for (int i = 0; i < rules.length; i++) {
            for (int j = 0; j < rules[i].getAntecedents().length; j++) {
                if (j != missingIndex1 && j != missingIndex2) {
                    // System.out.println("rules" + i + "Antecedent" + j + rules[i].getAntecedent(j).toStringInt() + " vs " + "observation.Antecedent" + j + observation.getAntecedent(j).toStringInt());
                    float distance = rules[i].getAntecedent(j).absDistanceTo(observation.getAntecedent(j));
                    termWeights[i][j] = 1f / distance;
                    //System.out.println("A*" + j + "termWeights to R" + i + j + ":" + termWeights[i][j]);
                }
            }
            if (missingIndex1 < observation.getNumberOfAntecedents() && missingIndex2 < observation.getNumberOfAntecedents()) {
                termWeights[i][observation.getNumberOfAntecedents()] = 1f / rules[i].getConsequence().absDistanceTo(observation.getConsequence());
                //System.out.println("B*" + "termWeights to B" + i + ":" + termWeights[i][observation.getNumberOfAntecedents()]);
            }
        }
        /*   System.out.println("Raw Term Weights:");
        printArrays(termWeights);*/
        return termWeights;
    }

//    public static float[] getTotalNormalisedWeights(Rule[] rules, Observation observation, int missingIndex1, int missingIndex2)
//            throws InstantiationException, IllegalAccessException {
//        float[][] normalisedWeights = getNormalisedTermWeights(observation, rules, missingIndex1, missingIndex2);
//
//        //Rule intermediateRule = getIntermediateFuzzyRule(rules, normalisedWeights, missingIndex1, missingIndex2);
//        float[] totalNormalisedWeights = new float[rules.length];
//        // float totalNormalisedWeights = 0;
//        if ((missingIndex1 >= 0) & (missingIndex1 < observation.getAntecedents().length) && (missingIndex2 >= 0) & (missingIndex2 < observation.getAntecedents().length)) {
//            //backward
//            float[] wSum = new float[rules.length];
//            for (int i = 0; i < rules.length; i++) {
//                wSum[i] = 0;
//                for (int j = 0; j < rules[0].getNumberOfAntecedents(); j++) {
//                    if (j != missingIndex1 && j != missingIndex2) {
//                        wSum[i] = wSum[i] + normalisedWeights[i][j];
//                    }
//                }
//
//                totalNormalisedWeights[i] = normalisedWeights[i][rules[0].getNumberOfAntecedents()] *
//                        rules[0].getNumberOfAntecedents() - wSum[i];
//
//
//            }
//
//        }
//        return totalNormalisedWeights;
//    }


    public static Rule getIntermediateFuzzyRule(Rule[] rules, float[][] normalisedWeights, int missingIndex)
            throws IllegalAccessException, InstantiationException {
        float[] points;
        Rule intermediateFuzzyRule = new Rule(rules[0]);
        for (int i = 0; i < rules[0].getNumberOfAntecedents(); i++) {
            if (i != missingIndex) {
                points = new float[rules[0].getAntecedent(i).getPoints().length];
                for (int j = 0; j < rules.length; j++) {
                    for (int k = 0; k < points.length; k++) {
                        points[k] = points[k] + normalisedWeights[j][i] * rules[j].getAntecedent(i).getPoint(k);
                    }
                }
                FuzzyNumber intermediateFuzzyTerm = rules[0].getAntecedent(i).getClass().newInstance();
                intermediateFuzzyTerm.setPoints(points);
                intermediateFuzzyRule.setAntecedent(i, intermediateFuzzyTerm);
                //System.out.println("A''[" + i + "] = " + intermediateFuzzyRule.getAntecedent(i));
            }
        }
        if (missingIndex < rules[0].getNumberOfAntecedents()) {
            points = new float[rules[0].getConsequence().getPoints().length];
            for (int j = 0; j < rules.length; j++) {
                for (int k = 0; k < points.length; k++) {
                    points[k] = points[k] + normalisedWeights[j][rules[0].getNumberOfAntecedents()] *
                            rules[j].getConsequence().getPoint(k);
                }
            }
            FuzzyNumber intermediateConsequence = rules[0].getConsequence().getClass().newInstance();
            intermediateConsequence.setPoints(points);
            intermediateFuzzyRule.setConsequence(intermediateConsequence);
            //System.out.println("B'' = " + intermediateFuzzyRule.getConsequence());
        }
        //System.out.println("\nIntermediate Rule:\n" + intermediateFuzzyRule);
        return intermediateFuzzyRule;
    }


    public static Rule getIntermediateFuzzyRule(Rule[] rules, float[][] normalisedWeights, int missingIndex1, int missingIndex2,
                                                double weightsParameters1, double weightsParameters2, double weightsParameters3, double weightsParameters4) {
        float[] points;
        Rule intermediateFuzzyRule = new Rule(rules[0]);
        for (int i = 0; i < rules[0].getNumberOfAntecedents(); i++) {
            if (i != missingIndex1 && i != missingIndex2) {
                points = new float[rules[0].getAntecedent(i).getPoints().length];

                for (int j = 0; j < rules.length; j++) {
                    for (int k = 0; k < points.length; k++) {
                        points[k] = points[k] + normalisedWeights[j][i] * rules[j].getAntecedent(i).getPoint(k);

                    }
                }
                FuzzyNumber intermediateFuzzyTerm = null;
                try {
                    intermediateFuzzyTerm = rules[0].getAntecedent(i).getClass().newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (IllegalAccessException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

                intermediateFuzzyTerm.setPoints(points);

                intermediateFuzzyRule.setAntecedent(i, intermediateFuzzyTerm);

                //System.out.println("A''[" + i + "] = " + intermediateFuzzyRule.getAntecedent(i));
            }
            points = new float[rules[0].getAntecedent(i).getPoints().length];
            for (int k = 0; k < points.length; k++) {
                points[k] = (float) (points[k] + weightsParameters1 * rules[0].getAntecedent(missingIndex1).getPoint(k) +
                        weightsParameters2 * rules[1].getAntecedent(missingIndex1).getPoint(k));

            }
            FuzzyNumber intermediateFuzzyTerm = null;
            try {
                intermediateFuzzyTerm = rules[0].getAntecedent(i).getClass().newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IllegalAccessException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            intermediateFuzzyTerm.setPoints(points);

            intermediateFuzzyRule.setAntecedent(missingIndex1, intermediateFuzzyTerm);

            points = new float[rules[0].getAntecedent(i).getPoints().length];
            for (int k = 0; k < points.length; k++) {
                points[k] = (float) (points[k] + weightsParameters3 * rules[0].getAntecedent(missingIndex2).getPoint(k) +
                        weightsParameters4 * rules[1].getAntecedent(missingIndex2).getPoint(k));

            }
            try {
                intermediateFuzzyTerm = rules[0].getAntecedent(i).getClass().newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IllegalAccessException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            intermediateFuzzyTerm.setPoints(points);

            intermediateFuzzyRule.setAntecedent(missingIndex2, intermediateFuzzyTerm);

        }
        if (missingIndex1 < rules[0].getNumberOfAntecedents() && missingIndex2 < rules[0].getNumberOfAntecedents()) {
            points = new float[rules[0].getConsequence().getPoints().length];

            for (int j = 0; j < rules.length; j++) {
                for (int k = 0; k < points.length; k++) {
                    points[k] = points[k] + normalisedWeights[j][rules[0].getNumberOfAntecedents()] *
                            rules[j].getConsequence().getPoint(k);

                }
            }
            FuzzyNumber intermediateConsequence = null;
            try {
                intermediateConsequence = rules[0].getConsequence().getClass().newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IllegalAccessException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            intermediateConsequence.setPoints(points);
            intermediateFuzzyRule.setConsequence(intermediateConsequence);
            //System.out.println("B'' = " + intermediateFuzzyRule.getConsequence());
        }
        // System.out.println("\nIntermediate Rule:\n" + intermediateFuzzyRule);
        return intermediateFuzzyRule;
    }


    public static void printParameters(double[][] parameters, String filename) {

        try {
            FileOutputStream ostream = new FileOutputStream("C:\\Documents and Settings\\jin\\IdeaProjects\\FuzzyHierarchicalInterpolation\\src\\" + filename + ".txt");
            DataOutputStream out = new DataOutputStream(ostream);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));


            int ParametersNumber = parameters.length;
            //System.out.println("ParametersNumber" + ParametersNumber);
            for (int i = 0; i < parameters.length; i++) {

                bw.write("Number" + i + ":  ");
                for (int j = 0; j < parameters[0].length; j++)
                    bw.write(parameters[i][j] + " ");
                bw.newLine();

            }
            bw.close();
            out.close();
            ostream.close();
        } catch (Exception e) {

        }
    }


    public static Rule[] multiShift(Rule intermediateRule, Observation observation, int missingIndex1, int missingIndex2, int slice) {
        Rule shiftedIntermediateRule = new Rule(intermediateRule);
        float delta = 0, deltaSum = 0;

        Rule[] shiftedRule = new Rule[slice];
        if (missingIndex1 >= intermediateRule.getNumberOfAntecedents() && missingIndex2 >= intermediateRule.getNumberOfAntecedents()) {
            //forward
            for (int i = 0; i < intermediateRule.getNumberOfAntecedents(); i++) {
                float distance = observation.getAntecedent(i).distanceTo(intermediateRule.getAntecedent(i));
                delta = distance /
                        (intermediateRule.getMaxAntecedentRanges()[i] - intermediateRule.getMinAntecedentRanges()[i]);
                shiftedIntermediateRule.setAntecedent(i, intermediateRule.getAntecedent(i).adds(delta *
                        (intermediateRule.getMaxAntecedentRanges()[i] - intermediateRule.getMinAntecedentRanges()[i])));
                deltaSum += delta;
                // System.out.println("Delta A" + i + " " + delta);
            }
            delta = deltaSum / intermediateRule.getNumberOfAntecedents();
            //  System.out.println("Delta B " + delta);
            shiftedIntermediateRule.setConsequence(intermediateRule.getConsequence().adds(
                    delta * (intermediateRule.getMaxConsequenceRange() - intermediateRule.getMinConsequenceRange())));
            // System.out.println("(f)shifted B'" + ":" + shiftedIntermediateRule.getConsequence() + " " + shiftedIntermediateRule.getConsequence().getRepresentativeValue());
        } else {
            for (int i = 0; i < intermediateRule.getNumberOfAntecedents(); i++) {
                if ((i != missingIndex1) && (i != missingIndex2)) {
                    delta = observation.getAntecedent(i).distanceTo(intermediateRule.getAntecedent(i)) /
                            (intermediateRule.getMaxAntecedentRanges()[i] - intermediateRule.getMinAntecedentRanges()[i]);
                    shiftedIntermediateRule.setAntecedent(i, intermediateRule.getAntecedent(i).adds(delta *
                            (intermediateRule.getMaxAntecedentRanges()[i] - intermediateRule.getMinAntecedentRanges()[i])));
                    // System.out.println("shifted A'" + i + ":" + shiftedIntermediateRule.getAntecedent(i));
                    deltaSum += delta;
                    // System.out.println("Delta A" + i + " " + delta);
                }
            }
            delta = observation.getConsequence().distanceTo(intermediateRule.getConsequence()) /
                    (intermediateRule.getMaxConsequenceRange() - intermediateRule.getMinConsequenceRange());
            //  System.out.println("Delta B " + delta);
            shiftedIntermediateRule.setConsequence(intermediateRule.getConsequence().adds(delta *
                    (intermediateRule.getMaxConsequenceRange() - intermediateRule.getMinConsequenceRange())));
            // System.out.println("(b)shifted B'" + ":" + shiftedIntermediateRule.getConsequence() + " " + shiftedIntermediateRule.getConsequence().getRepresentativeValue());

            delta = delta * intermediateRule.getNumberOfAntecedents() - deltaSum;

            double[][] multiDelta = calculateParametersDelta(delta, slice);

            Process.printParameters(multiDelta, "DeltaFile");

            for (int i = 0; i < multiDelta.length; i++) {
                shiftedRule[i] = new Rule(intermediateRule);
            }


            for (int i = 0; i < multiDelta.length; i++) {

                for (int j = 0; j < intermediateRule.getNumberOfAntecedents(); j++) {
                    if ((j != missingIndex1) && (j != missingIndex2)) {

                        shiftedRule[i].setAntecedent(j, shiftedIntermediateRule.getAntecedent(j));
                    }
                }

                shiftedRule[i].setAntecedent(missingIndex1, intermediateRule.getAntecedent(missingIndex1).adds(
                        multiDelta[i][0] * (intermediateRule.getMaxAntecedentRanges()[missingIndex1] -
                                intermediateRule.getMinAntecedentRanges()[missingIndex1])));

                shiftedRule[i].setAntecedent(missingIndex2, intermediateRule.getAntecedent(missingIndex2).adds(
                        multiDelta[i][1] * (intermediateRule.getMaxAntecedentRanges()[missingIndex2] -
                                intermediateRule.getMinAntecedentRanges()[missingIndex2])));

                shiftedRule[i].setConsequence(shiftedIntermediateRule.getConsequence());

            }

        }
        return shiftedRule;
    }


    public static double[][] calculateParametersDelta(float weights, int slice) {
        double[][] parameters = new double[slice][2];
        double[][] parameters1 = calculatedParametersLM(weights, slice);

        int index = 0;
        for (int i = 0; i < slice; i++) {

            parameters[index][0] = parameters1[i][0];
            parameters[index][1] = parameters1[i][1];


            index = index + 1;
        }

        return parameters;
    }


    public static FuzzyNumber getIntermediateFuzzyTerm(FuzzyNumber a1, FuzzyNumber a2, double delta) {
        double newLeftShoulder = (1 - delta) * a1.getPoint(0) + delta * a2.getPoint(0);
        double newCentroid = (1 - delta) * a1.getPoint(1) + delta * a2.getPoint(1);
        double newRightShoulder = (1 - delta) * a1.getPoint(2) + delta * a2.getPoint(2);
        return new TriangularFuzzyNumber(newLeftShoulder, newCentroid, newRightShoulder);
    }

    public static double getScaleRate(FuzzyNumber aDash, FuzzyNumber aStar) {
        return (aStar.getPoint(2) - aStar.getPoint(0)) /
                (aDash.getPoint(2) - aDash.getPoint(0));
    }

    public static FuzzyNumber scale(FuzzyNumber fuzzyNumber, double scaleRate) throws Exception {
        if (scaleRate < 0)
            scaleRate = -scaleRate;
        //  throw new Exception("Scale Rate (" + scaleRate + ") cannot be less than 0");
        double newLeftShoulder = (fuzzyNumber.getPoint(0) * (1 + 2 * scaleRate) +
                fuzzyNumber.getPoint(1) * (1 - scaleRate) +
                fuzzyNumber.getPoint(2) * (1 - scaleRate)) / 3;
        double newCentroid = (fuzzyNumber.getPoint(0) * (1 - scaleRate) +
                fuzzyNumber.getPoint(1) * (1 + 2 * scaleRate) +
                fuzzyNumber.getPoint(2) * (1 - scaleRate)) / 3;
        double newRightShoulder = (fuzzyNumber.getPoint(0) * (1 - scaleRate) +
                fuzzyNumber.getPoint(1) * (1 - scaleRate) +
                fuzzyNumber.getPoint(2) * (1 + 2 * scaleRate)) / 3;
        return new TriangularFuzzyNumber(newLeftShoulder, newCentroid, newRightShoulder);
    }

    public static double getMoveRatio(FuzzyNumber aDashScaled, FuzzyNumber aStar) throws Exception {
        double moveRatio;
        if (aStar.getPoint(0) >= aDashScaled.getPoint(0)) {
            moveRatio = (aStar.getPoint(0) - aDashScaled.getPoint(0)) * 3 /
                    (aDashScaled.getPoint(1) - aDashScaled.getPoint(0));
            /* if ((moveRatio < 0) || (moveRatio > 1))
 throw new Exception("Move Ratio (" + moveRatio + ") cannot exceed [0,1]");*/
        } else {
            moveRatio = (aStar.getPoint(0) - aDashScaled.getPoint(0)) * 3 /
                    (aDashScaled.getPoint(2) - aDashScaled.getPoint(1));
            /*         if ((moveRatio > 0) || (moveRatio < -1))
 throw new Exception("Move Ratio (" + moveRatio + ") cannot exceed [-1,0]");*/

        }
        return moveRatio;
    }

    public static FuzzyNumber move(FuzzyNumber fuzzyNumber, double moveRatio) {
        double moveRate = (moveRatio >= 0) ? moveRatio * (fuzzyNumber.getPoint(1) - fuzzyNumber.getPoint(0)) / 3
                : moveRatio * (fuzzyNumber.getPoint(2) - fuzzyNumber.getPoint(1)) / 3;
        return new TriangularFuzzyNumber(fuzzyNumber.getPoint(0) + moveRate,
                fuzzyNumber.getPoint(1) - 2 * moveRate,
                fuzzyNumber.getPoint(2) + moveRate);
    }

    public static Rule multigetIntermediateRule(Rule rule1, Rule rule2, Observation observation, int dimensionToBeInterpolated1,
                                                int dimensionToBeInterpolated2, double delta1, double delta2) {
        double deltaSum = 0;
        double delta;
        Rule intermediateRule = new Rule(rule1);

        //backward
        for (int i = 0; i < observation.getAntecedents().length; i++) {
            if ((i != dimensionToBeInterpolated1) && (i != dimensionToBeInterpolated2)) {
                delta = getDelta(observation.getAntecedents()[i],
                        rule1.getAntecedents()[i],
                        rule2.getAntecedents()[i]);
                //System.out.println(i+ "'th dimension delta:" + delta);
                deltaSum = deltaSum + delta;
                intermediateRule.getAntecedents()[i] =
                        getIntermediateFuzzyTerm(rule1.getAntecedents()[i], rule2.getAntecedents()[i], delta);
            }
        }
        delta = getDelta(observation.getConsequence(),
                rule1.getConsequence(),
                rule2.getConsequence());
        //System.out.println(" Consequence delta:" + delta);
        intermediateRule.setConsequence(
                getIntermediateFuzzyTerm(rule1.getConsequence(), rule2.getConsequence(), delta));

        //delta = delta * observation.getAntecedents().length - deltaSum;
        //System.out.println(dimensionToBeInterpolated+ "'th antecedent delta:" +delta );
        intermediateRule.getAntecedents()[dimensionToBeInterpolated1] =
                getIntermediateFuzzyTerm(
                        rule1.getAntecedents()[dimensionToBeInterpolated1],
                        rule2.getAntecedents()[dimensionToBeInterpolated1], delta1);
        intermediateRule.getAntecedents()[dimensionToBeInterpolated2] =
                getIntermediateFuzzyTerm(
                        rule1.getAntecedents()[dimensionToBeInterpolated2],
                        rule2.getAntecedents()[dimensionToBeInterpolated2], delta2);
        return intermediateRule;
    }

    public static float[][] fowardCalculateTermWeights(Rule observation, Rule[] rules, int missingIndex) {
        //termWeights is a N*(K+1) matrix of N rules, and K antecedents + 1 consequence
        float[][] termWeights = new float[rules.length][observation.getAntecedents().length + 1];
        for (int i = 0; i < rules.length; i++) {
            for (int j = 0; j < rules[i].getAntecedents().length; j++) {
                if (j != missingIndex) {
                    // System.out.println("rules" + i + "Antecedent" + j + rules[i].getAntecedent(j).toStringInt() + " vs " + "observation.Antecedent" + j + observation.getAntecedent(j).toStringInt());
                    float distance = rules[i].getAntecedent(j).absDistanceTo(observation.getAntecedent(j));
                    termWeights[i][j] = 1f / distance;
                    //System.out.println("A*" + j + "termWeights to R" + i + j + ":" + termWeights[i][j]);
                }
            }
            if (missingIndex < observation.getNumberOfAntecedents()) {
                termWeights[i][observation.getNumberOfAntecedents()] = 1f / rules[i].getConsequence().absDistanceTo(observation.getConsequence());
                //System.out.println("B*" + "termWeights to B" + i + ":" + termWeights[i][observation.getNumberOfAntecedents()]);
            }
        }
        //System.out.println("Raw Term Weights:");
        //printArrays(termWeights);
        return termWeights;
    }


    public static float[][] getNormalisedTermWeights(Rule observation, Rule[] rules, int missingIndex) {
        float[][] termWeights = fowardCalculateTermWeights(observation, rules, missingIndex);
        float wSum = 0;
        for (int i = 0; i < observation.getNumberOfAntecedents(); i++) {
            if (i != missingIndex) {
                wSum = 0;
                for (int j = 0; j < rules.length; j++) {
                    wSum += termWeights[j][i];
                }
                for (int j = 0; j < rules.length; j++) {
                    termWeights[j][i] = termWeights[j][i] / wSum;
                    //System.out.println("A*" + i + "NormlisedtermWeights " + ":" + termWeights[j][i]);
                }
            }
        }
        if (missingIndex < observation.getNumberOfAntecedents()) {
            wSum = 0;
            for (int j = 0; j < rules.length; j++) {
                wSum += termWeights[j][observation.getNumberOfAntecedents()];
            }
            for (int j = 0; j < rules.length; j++) {
                termWeights[j][observation.getNumberOfAntecedents()] = termWeights[j][observation.getNumberOfAntecedents()] / wSum;
                //System.out.println("B*" + "NormlisedtermWeights to B" + j + ":" + termWeights[j][observation.getNumberOfAntecedents()]);
            }
        }
        //System.out.println("Normalised Weights:");
        //printArrays(termWeights);
        return termWeights;
    }


    public static Rule getIntermediateFuzzyRule(Rule[] rules, Rule observation, int missingIndex)
            throws InstantiationException, IllegalAccessException {
        float[][] normalisedWeights = getNormalisedTermWeights(observation, rules, missingIndex);
        Rule intermediateRule = getIntermediateFuzzyRule(rules, normalisedWeights, missingIndex);
        if ((missingIndex >= 0) & (missingIndex < observation.getAntecedents().length)) {
            //backward
            float[] points = new float[rules[0].getAntecedent(missingIndex).getPoints().length];
            float wSum = 0;
            for (int i = 0; i < rules.length; i++) {
                wSum = 0;
                for (int j = 0; j < rules[0].getNumberOfAntecedents(); j++) {
                    if (j != missingIndex) {
                        wSum += normalisedWeights[i][j];
                    }
                }
                normalisedWeights[i][missingIndex] = normalisedWeights[i][rules[0].getNumberOfAntecedents()] *
                        rules[0].getNumberOfAntecedents() - wSum;
                for (int k = 0; k < points.length; k++) {
                    points[k] = points[k] + normalisedWeights[i][missingIndex] * rules[i].getAntecedent(missingIndex).getPoint(k);
                }
            }
            FuzzyNumber intermediateMissingAntecedent = rules[0].getAntecedent(missingIndex).getClass().newInstance();
            intermediateMissingAntecedent.setPoints(points);
            intermediateRule.setAntecedent(missingIndex, intermediateMissingAntecedent);
            System.out.println("intermediate A''" + missingIndex + "'':" + intermediateRule.getAntecedents()[missingIndex]);
        } else {
            //forward

            float[] points = new float[rules[0].getConsequence().getPoints().length];
            float wSum = 0;
            for (int i = 0; i < rules.length; i++) {
                wSum = 0;
                for (int j = 0; j < rules[0].getNumberOfAntecedents(); j++) {
                    wSum += normalisedWeights[i][j];
                }
                normalisedWeights[i][rules[0].getNumberOfAntecedents()] = wSum / rules[0].getNumberOfAntecedents();
                for (int k = 0; k < points.length; k++) {
                    points[k] = points[k] + normalisedWeights[i][rules[0].getNumberOfAntecedents()] * rules[i].getConsequence().getPoint(k);
                }
            }
            FuzzyNumber intermediateConsequence = rules[0].getConsequence().getClass().newInstance();
            intermediateConsequence.setPoints(points);
            intermediateRule.setConsequence(intermediateConsequence);
            // System.out.println("intermediate B'':" + intermediateRule.getConsequence());


            /*float consequenceWeightSum = 0;
            for (int i = 0; i < rules.length; i++) {
                float consequenceWeight = (float) (1/ Math.abs(intermediateConsequence.distanceTo(rules[i].getConsequence()) -0.092990205*15));
                consequenceWeightSum += consequenceWeight;
            }

            for (int i = 0; i < rules.length; i++) {
                float consequenceWeight2 = (float) (1/ Math.abs(intermediateConsequence.distanceTo(rules[i].getConsequence()) -0.092990205*15));
                consequenceWeight2 = consequenceWeight2 / consequenceWeightSum;
                System.out.print(consequenceWeight2 + "\t");
            }
            System.out.println();*/
        }
        // System.out.println("\n\tNormalised Weights:");
        // printArrays(normalisedWeights);
        Rule shiftedIntermediateRule = shift(intermediateRule, observation, missingIndex);


        float consequenceWeightSum = 0;
        for (int i = 0; i < rules.length; i++) {
            float consequenceWeight = 1 / shiftedIntermediateRule.getConsequence().absDistanceTo(rules[i].getConsequence());
            consequenceWeightSum += consequenceWeight;
        }

        for (int i = 0; i < rules.length; i++) {
            float consequenceWeight2 = 1 / shiftedIntermediateRule.getConsequence().absDistanceTo(rules[i].getConsequence());
            consequenceWeight2 = consequenceWeight2 / consequenceWeightSum;
            //  System.out.print(consequenceWeight2 + "\t");
        }
        // System.out.println();

        return shiftedIntermediateRule;
    }


    public static Rule shift(Rule intermediateRule, Rule observation, int missingIndex) {
        Rule shiftedIntermediateRule = new Rule(intermediateRule);
        float delta = 0, deltaSum = 0;
        if (missingIndex >= intermediateRule.getNumberOfAntecedents()) {
            //forward
            for (int i = 0; i < intermediateRule.getNumberOfAntecedents(); i++) {
                float distance = observation.getAntecedent(i).distanceTo(intermediateRule.getAntecedent(i));
                delta = distance /
                        (intermediateRule.getMaxAntecedentRanges()[i] - intermediateRule.getMinAntecedentRanges()[i]);
                shiftedIntermediateRule.setAntecedent(i, intermediateRule.getAntecedent(i).adds(delta *
                        (intermediateRule.getMaxAntecedentRanges()[i] - intermediateRule.getMinAntecedentRanges()[i])));
                deltaSum += delta;
                // System.out.println("Delta A" + i + " " + delta);
            }
            delta = deltaSum / intermediateRule.getNumberOfAntecedents();
            //  System.out.println("Delta B " + delta);
            shiftedIntermediateRule.setConsequence(intermediateRule.getConsequence().adds(
                    delta * (intermediateRule.getMaxConsequenceRange() - intermediateRule.getMinConsequenceRange())));
            //   System.out.println("(f)shifted B'" + ":" + shiftedIntermediateRule.getConsequence() + " " + shiftedIntermediateRule.getConsequence().getRepresentativeValue());
        } else {
            for (int i = 0; i < intermediateRule.getNumberOfAntecedents(); i++) {
                if (i != missingIndex) {
                    delta = observation.getAntecedent(i).distanceTo(intermediateRule.getAntecedent(i)) /
                            (intermediateRule.getMaxAntecedentRanges()[i] - intermediateRule.getMinAntecedentRanges()[i]);
                    shiftedIntermediateRule.setAntecedent(i, intermediateRule.getAntecedent(i).adds(delta *
                            (intermediateRule.getMaxAntecedentRanges()[i] - intermediateRule.getMinAntecedentRanges()[i])));
                    //   System.out.println("shifted A'" + i + ":" + shiftedIntermediateRule.getAntecedent(i));
                    deltaSum += delta;
                    //  System.out.println("Delta A" + i + " " + delta);
                }
            }
            delta = observation.getConsequence().distanceTo(intermediateRule.getConsequence()) /
                    (intermediateRule.getMaxConsequenceRange() - intermediateRule.getMinConsequenceRange());
            //    System.out.println("Delta B " + delta);
            shiftedIntermediateRule.setConsequence(intermediateRule.getConsequence().adds(delta *
                    (intermediateRule.getMaxConsequenceRange() - intermediateRule.getMinConsequenceRange())));
            //    System.out.println("(b)shifted B'" + ":" + shiftedIntermediateRule.getConsequence() + " " + shiftedIntermediateRule.getConsequence().getRepresentativeValue());

            delta = delta * intermediateRule.getNumberOfAntecedents() - deltaSum;
            //   System.out.println("Delta A" + missingIndex + " " + delta);
            shiftedIntermediateRule.setAntecedent(missingIndex, intermediateRule.getAntecedent(missingIndex).adds(
                    delta * (intermediateRule.getMaxAntecedentRanges()[missingIndex] -
                            intermediateRule.getMinAntecedentRanges()[missingIndex])));
            //    System.out.println("shifted A'" + missingIndex + ":" + shiftedIntermediateRule.getAntecedent(missingIndex));
        }
        return shiftedIntermediateRule;
    }


    public static Rule getIntermediateRule(Rule rule1, Rule rule2, Rule observation, int dimensionToBeInterpolated) {
        double deltaSum = 0;
        double delta;
        Rule intermediateRule = new Rule(rule1);

        if ((dimensionToBeInterpolated >= 0) & (dimensionToBeInterpolated < observation.getAntecedents().length)) {
            //backward
            for (int i = 0; i < observation.getAntecedents().length; i++) {
                if (i != dimensionToBeInterpolated) {
                    delta = getDelta(observation.getAntecedents()[i],
                            rule1.getAntecedents()[i],
                            rule2.getAntecedents()[i]);
                    System.out.println(i + "'th dimension delta:" + delta);
                    deltaSum = deltaSum + delta;
                    intermediateRule.getAntecedents()[i] =
                            getIntermediateFuzzyTerm(rule1.getAntecedents()[i], rule2.getAntecedents()[i], delta);
                }
            }
            delta = getDelta(observation.getConsequence(),
                    rule1.getConsequence(),
                    rule2.getConsequence());
            System.out.println(" Consequence delta:" + delta);
            intermediateRule.setConsequence(
                    getIntermediateFuzzyTerm(rule1.getConsequence(), rule2.getConsequence(), delta));

            delta = delta * observation.getAntecedents().length - deltaSum;
            System.out.println(dimensionToBeInterpolated + "'th antecedent delta:" + delta);
            intermediateRule.getAntecedents()[dimensionToBeInterpolated] =
                    getIntermediateFuzzyTerm(
                            rule1.getAntecedents()[dimensionToBeInterpolated],
                            rule2.getAntecedents()[dimensionToBeInterpolated], delta);

        } else {
            //forward
            for (int i = 0; i < observation.getAntecedents().length; i++) {
                delta = getDelta(observation.getAntecedents()[i],
                        rule1.getAntecedents()[i],
                        rule2.getAntecedents()[i]);
                //       System.out.println(i+ "'th antecedent delta=" + delta);
                deltaSum = deltaSum + delta;
                intermediateRule.getAntecedents()[i] =
                        getIntermediateFuzzyTerm(rule1.getAntecedents()[i], rule2.getAntecedents()[i], delta);
            }
            delta = deltaSum / observation.getAntecedents().length;
            //      System.out.println("Consequent delta=" +delta);
            intermediateRule.setConsequence(
                    getIntermediateFuzzyTerm(rule1.getConsequence(), rule2.getConsequence(), delta));
        }
        //   System.out.println("delta=" + delta);
        for (int j = 0; j < intermediateRule.getAntecedents().length; j++) {
            //      System.out.println("A" + j + "' antecedent = " + intermediateRule.getAntecedents()[j].toString());
        }

        //   System.out.println( "intermediate Consequence = " + intermediateRule.getConsequence().toString());
        return intermediateRule;
    }


    public static Rule multiBackwardInterpolation(Rule intermediateRule,
                                                  Observation observation,
                                                  int missingIndex1,
                                                  int missingIndex2,
                                                  double s1,
                                                  double s2,
                                                  double m1,
                                                  double m2) throws Exception {
        Rule transformedRule = new Rule(intermediateRule);
        double s = 0;
        double m = 0;

        //backward
        for (int i = 0; i < observation.getAntecedents().length; i++) {
            if ((i != missingIndex1) && (i != missingIndex2)) {
                s = getScaleRate(intermediateRule.getAntecedents()[i], observation.getAntecedents()[i]);
                //System.out.println(i + "'th dimension: " + "s=" + s);
                intermediateRule.getAntecedents()[i] = scale(intermediateRule.getAntecedents()[i], s);
                //System.out.println("A" + i  + "'':" + intermediateRule.getAntecedents()[i]);
                m = getMoveRatio(intermediateRule.getAntecedents()[i], observation.getAntecedents()[i]);
                // System.out.println(i + "'th dimension: " + "m=" + m);
                transformedRule.getAntecedents()[i] = observation.getAntecedent(i);

            }
        }
        s = getScaleRate(intermediateRule.getConsequence(), observation.getConsequence());
        intermediateRule.setConsequence(scale(intermediateRule.getConsequence(), s));

        m = getMoveRatio(intermediateRule.getConsequence(), observation.getConsequence());
        transformedRule.setConsequence(move(intermediateRule.getConsequence(), m));


        transformedRule.setConsequence(observation.getConsequence());

        transformedRule.getAntecedents()[missingIndex1] = scale(intermediateRule.getAntecedents()[missingIndex1], s1);
        transformedRule.getAntecedents()[missingIndex2] = scale(intermediateRule.getAntecedents()[missingIndex2], s2);
        //System.out.println("A" + missingIndex1 + "''" +  transformedRule.getAntecedents()[missingIndex1].toString());
        transformedRule.getAntecedents()[missingIndex1] = move(transformedRule.getAntecedents()[missingIndex1], m1);
        transformedRule.getAntecedents()[missingIndex2] = move(transformedRule.getAntecedents()[missingIndex2], m2);
        return transformedRule;
    }


    public static Rule transform(Rule intermediateRule, Rule observation, int index) throws Exception {
        Rule transformedRule = new Rule(intermediateRule);
        double sSum = 0;
        double s;
        double mSum = 0;
        double m;
        if ((index >= 0) & (index < observation.getAntecedents().length)) {
            //backward
            for (int i = 0; i < observation.getAntecedents().length; i++) {
                if (i != index) {
                    s = getScaleRate(intermediateRule.getAntecedents()[i], observation.getAntecedents()[i]);
                    System.out.println(i + "'th dimension: " + "s=" + s);
                    intermediateRule.getAntecedents()[i] = scale(intermediateRule.getAntecedents()[i], s);
                    System.out.println("A" + i + "'':" + intermediateRule.getAntecedents()[i]);
                    sSum = sSum + s;

                    m = getMoveRatio(intermediateRule.getAntecedents()[i], observation.getAntecedents()[i]);
                    System.out.println(i + "'th dimension: " + "m=" + m);
                    transformedRule.getAntecedents()[i] = move(intermediateRule.getAntecedents()[i], m);
                    mSum = mSum + m;
                }
            }

            s = getScaleRate(intermediateRule.getConsequence(), observation.getConsequence());
            intermediateRule.setConsequence(scale(intermediateRule.getConsequence(), s));
            System.out.println("Consequence dimension: " + "s=" + s);
            System.out.println("B'':" + intermediateRule.getConsequence());
            m = getMoveRatio(intermediateRule.getConsequence(), observation.getConsequence());
            transformedRule.setConsequence(move(intermediateRule.getConsequence(), m));
            System.out.println("Consequence dimension: " + "m=" + m);

            s = s * observation.getAntecedents().length - sSum;
            System.out.println(index + " dimension: " + "s=" + s);
            m = m * observation.getAntecedents().length - mSum;
            System.out.println(index + " dimension: " + "m=" + m);
            transformedRule.getAntecedents()[index] = scale(intermediateRule.getAntecedents()[index], s);
            System.out.println("A" + index + "''" + transformedRule.getAntecedents()[index].toString());
            transformedRule.getAntecedents()[index] = move(transformedRule.getAntecedents()[index], m);
            /*   System.out.println("s=" + s);
          System.out.println("m=" + m);*/
        } else {
            //forward
            for (int i = 0; i < observation.getAntecedents().length; i++) {
                s = getScaleRate(intermediateRule.getAntecedents()[i], observation.getAntecedents()[i]);
                //     System.out.println(i + "'dimension:" + "s=" + s);
                intermediateRule.getAntecedents()[i] = scale(intermediateRule.getAntecedents()[i], s);
                sSum = sSum + s;

                m = getMoveRatio(intermediateRule.getAntecedents()[i], observation.getAntecedents()[i]);
                //     System.out.println(i + "'dimension:" + "m=" + m);
                transformedRule.getAntecedents()[i] = move(intermediateRule.getAntecedents()[i], m);
                mSum = mSum + m;
            }
            s = sSum / observation.getAntecedents().length;
            //   System.out.println("Consequent dimension: s=" + s);
            m = mSum / observation.getAntecedents().length;
            //   System.out.println("Consequent dimension: m=" + m);
            transformedRule.setConsequence(move(scale(intermediateRule.getConsequence(), s), m));
        }
        return transformedRule;
    }


    public static void printRules(Rule[] rules) {
        for (int i = 0; i < rules.length; i++) {
            for (FuzzyNumber fuzzyNumber : rules[i].getAntecedents()) {
                System.out.print(fuzzyNumber.toString() + " ");
            }
            System.out.println(rules[i].getConsequence().toString());
        }
    }

    // lamda and m [-1, +1]
    public static double[][] calculatedParametersLM(double total, int slice) {
        double[][] parameters = new double[slice][2];
        parameters[0][0] = (total >= 0) ? total - 1 : -1;
        parameters[0][1] = (total >= 0) ? 1 : total + 1;
        double step = (parameters[0][1] - parameters[0][0]) / (slice - 1);
        for (int i = 1; i < slice; i++) {
            parameters[i][0] = parameters[i - 1][0] + step;
            parameters[i][1] = parameters[i - 1][1] - step;
        }
        return parameters;
    }

    // s [>0]
    public static double[][] calculatedParametersS(double total, int slice) {
        double[][] parameters = new double[slice][2];
        /*parameters[0][0] = 0;
        parameters[0][1] = total;*/
        parameters[0][0] = 0;
        parameters[0][1] = total;
        double step = (parameters[0][1] - parameters[0][0]) / (slice - 1);
        for (int i = 1; i < slice; i++) {
            parameters[i][0] = parameters[i - 1][0] + step;
            parameters[i][1] = parameters[i - 1][1] - step;
        }
        return parameters;
    }

    // w [0, 1]
    public static double[][] calculatedParametersW(double total, int slice) {
        double[][] parameters = new double[slice][2];
        /*parameters[0][0] = 0;
        parameters[0][1] = total;*/
        parameters[0][0] = (total >= 1) ? total - 1 : 0;
        parameters[0][1] = (total >= 1) ? 1 : total;
        double step = (parameters[0][1] - parameters[0][0]) / (slice - 1);
        for (int i = 1; i < slice; i++) {
            parameters[i][0] = parameters[i - 1][0] + step;
            parameters[i][1] = parameters[i - 1][1] - step;
        }
        return parameters;
    }

    public static double[][] calculateParameters(Rule[] rules, Observation observation, int missingIndex1, int missingIndex2, int slice) throws Exception, InstantiationException {
        //20 * 4
        double[][] parameterWeights = Process.calculateParametersWeights(rules, observation, missingIndex1, missingIndex2, slice);
        //20 * 2
        double[][] parameterDelta = Process.calculateParametersDelta(rules, observation, missingIndex1, missingIndex2, slice);
        //400 * 4
        double[][] parameterSM = Process.calculateParametersSM(rules, observation, missingIndex1, missingIndex2, slice);

        double[][] parameters = new double[parameterWeights.length * parameterDelta.length * parameterSM.length][10];

        int currentPointer = 0;
        for (int i = 0; i < parameterWeights.length; i++) {
            for (int j = 0; j < parameterDelta.length; j++) {
                for (int k = 0; k < parameterSM.length; k++) {
                    parameters[currentPointer][0] = parameterWeights[i][0];
                    parameters[currentPointer][1] = parameterWeights[i][1];
                    parameters[currentPointer][2] = parameterWeights[i][2];
                    parameters[currentPointer][3] = parameterWeights[i][3];
                    parameters[currentPointer][4] = parameterDelta[j][0];
                    parameters[currentPointer][5] = parameterDelta[j][1];
                    parameters[currentPointer][6] = parameterSM[k][0];
                    parameters[currentPointer][7] = parameterSM[k][1];
                    parameters[currentPointer][8] = parameterSM[k][2];
                    parameters[currentPointer][9] = parameterSM[k][3];
                    currentPointer++;
                }
            }
        }
        /*double[][] parameters = new double[slice * slice * slice][6];
        double[][] parametersLambda = calculatedParametersLM(totalLambda, slice);
        double[][] parametersS = calculatedParametersS(totalS, slice);
        double[][] parametersM = calculatedParametersLM(totalM, slice);
        int index = 0;
        for (int i = 0; i < slice; i++) {
            for (int j = 0; j < slice; j++) {
                for (int k = 0; k < slice; k++) {
                    parameters[index][0] = parametersLambda[i][0];
                    parameters[index][1] = parametersLambda[i][1];
                    parameters[index][2] = parametersS[j][0];
                    parameters[index][3] = parametersS[j][1];
                    parameters[index][4] = parametersM[k][0];
                    parameters[index][5] = parametersM[k][1];
                    index = index + 1;
                }
            }
        }
        return parameters;*/
        return parameters;
    }


    public static ValueRange[] createParameterRanges() {
        int numHarmonies = 10;
        double HMCR = 0.8;
        return HarmonyMemory.createParameterRanges(numHarmonies, HMCR, 0.8, 0.01);
    }

    public static double[] calculateTotals(Rule[] rules, Observation observation, int missingIndex1, int missingIndex2) throws Exception {
        double[] totals = new double[4];
        totals[0] = Process.getTotalNormalisedWeights(rules, observation, missingIndex1, missingIndex2);
        if (totals[0] > 2 || totals[0] < 0) throw new Exception("Value out of range");
        totals[1] = Process.calculateTotalDelta(rules, observation, missingIndex1, missingIndex2);
        if (totals[1] > 2 || totals[1] < -2)  throw new Exception("Value out of range");
        double[] totalSM = Process.calculateTotalSM(rules, observation, missingIndex1, missingIndex2);
        totals[2] = totalSM[0];
        if (totals[2] < 0) throw new Exception("Value out of range");
        totals[3] = totalSM[1];
        if (totals[3] > 2 || totals[3] < -2)  throw new Exception("Value out of range");
        return totals;
    }

    public static ValueRange[] calculateValueRanges(double[] totals){
        ValueRange[] valueRanges = new ValueRange[4];

        valueRanges[0] = new ValueRange();
        valueRanges[0] .setContinuous(true);
        valueRanges[0] .setMin((totals[0] >= 1) ? totals[0] - 1 : 0);
        valueRanges[0] .setMax((totals[0] >= 1) ? 1 : totals[0]);


        valueRanges[1] = new ValueRange();
        valueRanges[1] .setContinuous(true);
        valueRanges[1] .setMin((totals[1] >= 0) ? totals[1] - 1 : -1);
        valueRanges[1] .setMax((totals[1] >= 0) ? 1 : totals[1] + 1);


        valueRanges[2] = new ValueRange();
        valueRanges[2] .setContinuous(true);
        valueRanges[2] .setMin(0);
        valueRanges[2] .setMax(totals[2]);

        valueRanges[3] = new ValueRange();
        valueRanges[3] .setContinuous(true);
        valueRanges[3] .setMin((totals[3] >= 0) ? totals[3] - 1 : -1);
        valueRanges[3] .setMax((totals[3] >= 0) ? 1 : totals[3] + 1);

        return valueRanges;
    }

    /* public static double[][] calculateWeightsParameters(float[] weights, int slice) {
            double[][] parameters = new double[slice][4];
            double[][] parameters1 = calculatedParametersSW(weights[0], slice);
            //double[][] parameters2 = calculatedParametersLM(weights[1], slice);
            int index = 0;
            for (int i = 0; i < slice; i++) {
                parameters[i][0] = parameters1[i][0];
                parameters[i][1] = parameters1[i][1];
                parameters[i][2] = 1 - parameters1[i][0];
                parameters[i][3] = 1 - parameters1[i][1];
            }

            return parameters;
        }

    */
    public static double[][] calculateWeightsParameters(float[] weights, int slice) {
        double[][] parameters;
        ArrayList<double[]> result;
        if (weights.length == 2) {
            parameters = new double[slice][4];
            double[][] parameters1 = calculatedParametersW(weights[0], slice);
            //double[][] parameters2 = calculatedParametersLM(weights[1], slice);
            int index = 0;
            for (int i = 0; i < slice; i++) {
                parameters[i][0] = parameters1[i][0];
                parameters[i][1] = parameters1[i][1];
                parameters[i][2] = 1 - parameters1[i][0];
                parameters[i][3] = 1 - parameters1[i][1];
            }
        } else {
            parameters = new double[(int) (Math.pow(slice, weights.length))][weights.length * 2];

            ArrayList<double[][]> weightsParameters = new ArrayList<double[][]>();

            for (int i = 0; i < weights.length; i++) {
                double[][] parametersS = calculatedParametersW(weights[i], slice);
                weightsParameters.add(parametersS);
            }

            //
            double[][][] values = {
                    {{0.1, 0.4}, {0.3, 0.4}, {0.2, 0.3}, {0.1, 0.4}, {0.3, 0.4}, {0.2, 0.3}},
                    {{0.1, 0.4}, {0.3, 0.4}, {0.2, 0.3}, {0.1, 0.4}, {0.9, 0.4}, {0.2, 0.3}},
                    {{0.1, 0.4}, {0.3, 0.4}, {0.2, 0.3}, {0.1, 0.4}, {0.3, 0.1}, {0.2, 0.3}},
                    {{0.1, 0.4}, {0.3, 0.4}, {0.2, 0.3}, {0.1, 0.4}, {0.3, 0.4}, {0.4, 0.2}}
            };


/*            double[][][] values = new double[weightsParameters.size()][][];
              for (int i = 0; i < weightsParameters.size(); i++) {
                values[i] = weightsParameters.get(i);
                for (int j = 0; j < values[i].length; j++) {
                    for (int k = 0; k < values[i][j].length; k++)
                        System.out.print(values[i][j][k] + " ");
                    System.out.println();
                }

            }*/
            temp0 = new double[weights.length];
            temp1 = new double[weights.length];
            result = new ArrayList<double[]>();
            IterRun(0, 6, values, result, 4);
            int xxxx = 0;
            System.out.println(123);
            System.out.println(123);
        }
        return parameters;
    }

    public static void IterRun(int kk, int slice, double[][][] values, ArrayList<double[]> result, int k) {
        for (int i = 0; i < slice; i++) {
            temp0[kk] = values[kk][i][0];
            temp1[kk] = values[kk][i][1];

            if (kk == k - 1) {
                double sum1 = 0;
                double sum0 = 0;
                for (int j = 0; j < k; j++) {
                    sum1 += temp1[j];
                    sum0 += temp0[j];
                }
                if (sum0 == 1d && sum1 == 1d) {
                    double[] result0 = new double[temp0.length];
                    double[] result1 = new double[temp1.length];
                    for (int m = 0; m < temp0.length; m++) {
                        result0[m] = temp0[m];
                        result1[m] = temp1[m];
                    }
                    result.add(result0);
                    result.add(result1);
                }
            } else
                IterRun(kk + 1, slice, values, result, k);
        }
    }

    public static double[][] calculateParametersSM(double totalS, double totalM, int slice) {
        double[][] parameters = new double[slice * slice][4];
        double[][] parametersS = calculatedParametersS(totalS, slice);
        double[][] parametersM = calculatedParametersLM(totalM, slice);
        int index = 0;
        for (int i = 0; i < slice; i++) {
            for (int j = 0; j < slice; j++) {

                parameters[index][0] = parametersS[i][0];
                parameters[index][1] = parametersS[i][1];
                parameters[index][2] = parametersM[j][0];
                parameters[index][3] = parametersM[j][1];
                index = index + 1;
            }
        }

        return parameters;
    }


    public static double[][] secondcalculatedParametersL(double total, double lambda, int slice) {
        double[][] parameters = new double[slice][2];
        parameters[0][0] = (total >= 0) ? total - lambda : -lambda;
        parameters[0][1] = (total >= 0) ? lambda : total + lambda;
        double step = (parameters[0][1] - parameters[0][0]) / (slice - 1);
        for (int i = 1; i < slice; i++) {
            parameters[i][0] = parameters[i - 1][0] + step;
            parameters[i][1] = parameters[i - 1][1] - step;
        }
        return parameters;
    }


    public static double[][] secondcalculatedParametersM(double total, double m, int slice) {
        double[][] parameters = new double[slice][2];
        parameters[0][0] = (total >= 0) ? total - m : -m;
        parameters[0][1] = (total >= 0) ? m : total + m;
        double step = (parameters[0][1] - parameters[0][0]) / (slice - 1);
        for (int i = 1; i < slice; i++) {
            parameters[i][0] = parameters[i - 1][0] + step;
            parameters[i][1] = parameters[i - 1][1] - step;
        }
        return parameters;
    }


    public static double[][] secondcalculatedParametersS(double total, double s, int slice) {
        double[][] parameters = new double[slice][2];
        parameters[0][0] = s;
        parameters[0][1] = total;
        double step = (parameters[0][1] - parameters[0][0]) / (slice - 1);
        for (int i = 1; i < slice; i++) {
            parameters[i][0] = parameters[i - 1][0] + step;
            parameters[i][1] = parameters[i - 1][1] - step;
        }
        return parameters;
    }

    public static double[][] secondcalculateParameters(double totalLambda, double totalS, double totalM, int slice, double lambda,
                                                       double s, double m) {
        double[][] parameters = new double[slice * slice * slice][6];
        double[][] parametersLambda = secondcalculatedParametersL(totalLambda, lambda, slice);
        double[][] parametersS = secondcalculatedParametersS(totalS, s, slice);
        double[][] parametersM = secondcalculatedParametersM(totalM, m, slice);
        int index = 0;
        for (int i = 0; i < slice; i++) {
            for (int j = 0; j < slice; j++) {
                for (int k = 0; k < slice; k++) {
                    parameters[index][0] = parametersLambda[i][0];
                    parameters[index][1] = parametersLambda[i][1];
                    parameters[index][2] = parametersS[j][0];
                    parameters[index][3] = parametersS[j][1];
                    parameters[index][4] = parametersM[k][0];
                    parameters[index][5] = parametersM[k][1];
                    index = index + 1;
                }
            }
        }
        return parameters;
    }


    public static void printRules(RuleBase ruleBase) {
        if (ruleBase.get(0).getConsequence() instanceof TrapezoidalFuzzyNumber) {
            for (int i = 0; i < ruleBase.size(); i++) {
                for (FuzzyNumber fuzzyNumber : ruleBase.get(i).getAntecedents()) {
                    System.out.format("(%.2f,%.2f,%.2f,%.2f) \t ", fuzzyNumber.getPoint(0), fuzzyNumber.getPoint(1),
                            fuzzyNumber.getPoint(2), fuzzyNumber.getPoint(3));
                    // System.out.print(fuzzyNumber.toString() + "\t");
                }

                System.out.format("(%.2f,%.2f,%.2f,%.2f) \t \n", ruleBase.get(i).getConsequence().getPoint(0),
                        ruleBase.get(i).getConsequence().getPoint(1), ruleBase.get(i).getConsequence().getPoint(2),
                        ruleBase.get(i).getConsequence().getPoint(2));
                //System.out.println(ruleBase.get(i).getConsequence().toString());
            }
            System.out.println("----------------------------");

        } else
            for (int i = 0; i < ruleBase.size(); i++) {
                for (FuzzyNumber fuzzyNumber : ruleBase.get(i).getAntecedents()) {
                    System.out.format("(%.2f,%.2f,%.2f) \t ", fuzzyNumber.getPoint(0), fuzzyNumber.getPoint(1),
                            fuzzyNumber.getPoint(2));
                    // System.out.print(fuzzyNumber.toString() + "\t");
                }

                System.out.format("(%.2f,%.2f,%.2f) \t \n", ruleBase.get(i).getConsequence().getPoint(0),
                        ruleBase.get(i).getConsequence().getPoint(1), ruleBase.get(i).getConsequence().getPoint(2),
                        ruleBase.get(i));
                //System.out.println(ruleBase.get(i).getConsequence().toString());
            }
        System.out.println("----------------------------");

    }

    public static Rule[] getClosestRules(RuleBase ruleBase,
                                         Rule observation,
                                         int numberOfClosestRules,
                                         boolean useWeightedConsequenceDistance) {
        sortRuleBase(ruleBase, observation, useWeightedConsequenceDistance);
        Rule[] closestRules = new Rule[numberOfClosestRules];
        /*int index = -1;
        while (index < ruleBase.size()) {
            index++;
            Rule rule = ruleBase.get(index);
            for (int a = 0; a < rule.getNumberOfAntecedents(); a++) {
                boolean left = true;
                if (observation.getAntecedent(a) != null) {
                    if (rule.getAntecedent(a).getRepresentativeValue() > observation.getAntecedent(a).getRepresentativeValue()) left &= false;
                }
                if (left) break;
            }

        }
        closestRules[0] = ruleBase.get(index).clone();
        while (index < ruleBase.size()) {
            index++;
            Rule rule = ruleBase.get(index);
            for (int a = 0; a < rule.getNumberOfAntecedents(); a++) {
                boolean right = true;
                if (observation.getAntecedent(a) != null) {
                    if (rule.getAntecedent(a).getRepresentativeValue() < observation.getAntecedent(a).getRepresentativeValue()) right &= false;
                }
                if (right) break;
            }

        }
        closestRules[1] = ruleBase.get(index).clone();*/
        List<Rule> tempClosestRules = ruleBase.subList(0, numberOfClosestRules);
        for (int i = 0; i < numberOfClosestRules; i++) {
            closestRules[i] = tempClosestRules.get(i);
        }
        return closestRules;

    }

    public static void sortRuleBase(RuleBase ruleBase, Rule observation, boolean useWeightedConsequenceDistance) {
        Collections.sort(ruleBase, new RuleComparator(observation, useWeightedConsequenceDistance));
    }

    public static void printRule(Rule rule) {

        for (FuzzyNumber fuzzyNumber : rule.getAntecedents()) {
            if (fuzzyNumber != null) {
                System.out.print(fuzzyNumber.toString() + "\t");
            } else {
                System.out.print("(null, null, null)" + "\t");
            }
        }
        if (rule.getConsequence() != null) {
            System.out.println(rule.getConsequence().toString());
        } else {
            System.out.println("(null, null, null)");
        }
    }

    public static void printArrays(float[][] arrays) {
        //StringBuffer stringBuffer = new StringBuffer();
        for (int j = 0; j < arrays[0].length; j++) {
            for (int i = 0; i < arrays.length; i++) {
                System.out.format("%8.2f", arrays[i][j]);
            }
            System.out.print("\n");
        }
        //System.out.println(stringBuffer.toString());
    }

    public static Rule secondForwardInterpolation(Rule[] rules, Rule transformRule) throws Exception {
        Rule intermediateRule = new Rule(rules[0]);
        Rule transformedRule = new Rule(rules[0]);
        intermediateRule = Process.getIntermediateFuzzyRule(rules, transformRule, transformRule.getAntecedents().length);
        transformedRule = Process.transform(intermediateRule, transformRule, transformRule.getAntecedents().length);

        return transformedRule;
    }


    public static double evaluate(Rule observation, Rule secondForwardInterpolation) throws Exception {
        double BRatio;
        BRatio = Math.abs((observation.getConsequence().getRepresentativeValue() - secondForwardInterpolation.getConsequence().getRepresentativeValue()) / observation.getConsequence().getRepresentativeValue());
        return BRatio;
    }

    public static float getTotalNormalisedWeights(Rule[] rules, Observation observation, int missingIndex1, int missingIndex2)
            throws InstantiationException, IllegalAccessException {
        float[][] normalisedWeights = getNormalisedTermWeights(observation, rules, missingIndex1, missingIndex2);

        //Rule intermediateRule = getIntermediateFuzzyRule(rules, normalisedWeights, missingIndex1, missingIndex2);
        float totalNormalisedWeights = 0;
        // float totalNormalisedWeights = 0;
        if ((missingIndex1 >= 0) & (missingIndex1 < observation.getAntecedents().length) && (missingIndex2 >= 0) & (missingIndex2 < observation.getAntecedents().length)) {
            //backward
            float wSum = 0;
            for (int i = 0; i < rules.length; i++) {
                wSum = 0;
                for (int j = 0; j < rules[0].getNumberOfAntecedents(); j++) {
                    if (j != missingIndex1 && j != missingIndex2) {
                        wSum = wSum + normalisedWeights[i][j];
                    }
                }

                totalNormalisedWeights = normalisedWeights[i][rules[0].getNumberOfAntecedents()] *
                        rules[0].getNumberOfAntecedents() - wSum;


            }

        }
        return totalNormalisedWeights;
    }

    public static double[][] calculateParametersWeights(Rule[] rules, Observation observation, int slice, int missingIndex1, int missingIndex2) throws IllegalAccessException, InstantiationException {
        // float[][] normalisedWeights = Process.getNormalisedTermWeights(observation, rules, missingIndex1, missingIndex2);
        float totalNormalisedWeights = Process.getTotalNormalisedWeights(rules, observation, missingIndex1, missingIndex2);
        double[][] twoWeights = Process.calculatedParametersW(totalNormalisedWeights, slice);
        double[][] weightsParameters = calculateWeightsParameters(twoWeights, slice);

        Process.printParameters(weightsParameters, "WeightsFile");

        return weightsParameters;
    }

    private static double[][] calculateWeightsParameters(double[][] weights, int slice) {

        double[][] parameters = new double[slice][4];
        // double[][] parameters1 = calculatedParametersW(weights[0], slice);
        //double[][] parameters2 = calculatedParametersLM(weights[1], slice);
        int index = 0;
        for (int i = 0; i < slice; i++) {
            parameters[i][0] = weights[i][0];
            parameters[i][1] = 1 - weights[i][0];
            parameters[i][2] = weights[i][1];
            parameters[i][3] = 1 - weights[i][1];
        }

        return parameters;
    }

    public static double[][] calculateParametersDelta(Rule[] rules, Observation observation, int missingIndex1, int missingIndex2, int slice) throws InstantiationException, IllegalAccessException {
        float delta = 0, deltaSum = 0;
        float[][] normalisedWeights = Process.getNormalisedTermWeights(observation, rules, missingIndex1, missingIndex2);
        Rule intermediateRule = new Rule(rules[0]);
        float[] points;
        for (int i = 0; i < rules[0].getNumberOfAntecedents(); i++) {
            if (i != missingIndex1 && i != missingIndex2) {
                points = new float[rules[0].getAntecedent(i).getPoints().length];
                for (int j = 0; j < rules.length; j++) {
                    for (int k = 0; k < points.length; k++) {
                        points[k] = points[k] + normalisedWeights[j][i] * rules[j].getAntecedent(i).getPoint(k);
                    }
                }
                FuzzyNumber intermediateFuzzyTerm = rules[0].getAntecedent(i).getClass().newInstance();
                intermediateFuzzyTerm.setPoints(points);
                intermediateRule.setAntecedent(i, intermediateFuzzyTerm);
            }
        }
        points = new float[rules[0].getConsequence().getPoints().length];

        for (int j = 0; j < rules.length; j++) {
            for (int k = 0; k < points.length; k++) {
                points[k] = points[k] + normalisedWeights[j][rules[0].getNumberOfAntecedents()] *
                        rules[j].getConsequence().getPoint(k);
            }
        }
        FuzzyNumber intermediateConsequence = rules[0].getConsequence().getClass().newInstance();
        intermediateConsequence.setPoints(points);
        intermediateRule.setConsequence(intermediateConsequence);

        for (int i = 0; i < intermediateRule.getNumberOfAntecedents(); i++) {
            if ((i != missingIndex1) && (i != missingIndex2)) {
                delta = observation.getAntecedent(i).distanceTo(intermediateRule.getAntecedent(i)) /
                        (intermediateRule.getMaxAntecedentRanges()[i] - intermediateRule.getMinAntecedentRanges()[i]);
                deltaSum += delta;
            }
        }
        delta = observation.getConsequence().distanceTo(intermediateRule.getConsequence()) /
                (intermediateRule.getMaxConsequenceRange() - intermediateRule.getMinConsequenceRange());
        delta = delta * intermediateRule.getNumberOfAntecedents() - deltaSum;
        double[][] multiDelta = calculateParametersDelta(delta, slice);
        Process.printParameters(multiDelta, "DeltaFile");

        return multiDelta;
    }

    public static double calculateTotalDelta(Rule[] rules, Observation observation, int missingIndex1, int missingIndex2) throws InstantiationException, IllegalAccessException {
        float delta = 0, deltaSum = 0;
        float[][] normalisedWeights = Process.getNormalisedTermWeights(observation, rules, missingIndex1, missingIndex2);
        Rule intermediateRule = new Rule(rules[0]);
        float[] points;
        for (int i = 0; i < rules[0].getNumberOfAntecedents(); i++) {
            if (i != missingIndex1 && i != missingIndex2) {
                points = new float[rules[0].getAntecedent(i).getPoints().length];
                for (int j = 0; j < rules.length; j++) {
                    for (int k = 0; k < points.length; k++) {
                        points[k] = points[k] + normalisedWeights[j][i] * rules[j].getAntecedent(i).getPoint(k);
                    }
                }
                FuzzyNumber intermediateFuzzyTerm = rules[0].getAntecedent(i).getClass().newInstance();
                intermediateFuzzyTerm.setPoints(points);
                intermediateRule.setAntecedent(i, intermediateFuzzyTerm);
            }
        }
        points = new float[rules[0].getConsequence().getPoints().length];

        for (int j = 0; j < rules.length; j++) {
            for (int k = 0; k < points.length; k++) {
                points[k] = points[k] + normalisedWeights[j][rules[0].getNumberOfAntecedents()] *
                        rules[j].getConsequence().getPoint(k);
            }
        }
        FuzzyNumber intermediateConsequence = rules[0].getConsequence().getClass().newInstance();
        intermediateConsequence.setPoints(points);
        intermediateRule.setConsequence(intermediateConsequence);

        for (int i = 0; i < intermediateRule.getNumberOfAntecedents(); i++) {
            if ((i != missingIndex1) && (i != missingIndex2)) {
                delta = observation.getAntecedent(i).distanceTo(intermediateRule.getAntecedent(i)) /
                        (intermediateRule.getMaxAntecedentRanges()[i] - intermediateRule.getMinAntecedentRanges()[i]);
                deltaSum += delta;
            }
        }
        delta = observation.getConsequence().distanceTo(intermediateRule.getConsequence()) /
                (intermediateRule.getMaxConsequenceRange() - intermediateRule.getMinConsequenceRange());
        delta = delta * intermediateRule.getNumberOfAntecedents() - deltaSum;
        return delta;
    }

    public static double[] calculateTotalSM(Rule[] rules, Observation observation, int missingIndex1, int missingIndex2) throws Exception {
        double sSum = 0;
        double s;
        double mSum = 0;
        double m;
        float[][] normalisedWeights = Process.getNormalisedTermWeights(observation, rules, missingIndex1, missingIndex2);
        Rule intermediateRule = new Rule(rules[0]);
        float[] points;
        for (int i = 0; i < rules[0].getNumberOfAntecedents(); i++) {
            if (i != missingIndex1 && i != missingIndex2) {
                points = new float[rules[0].getAntecedent(i).getPoints().length];
                for (int j = 0; j < rules.length; j++) {
                    for (int k = 0; k < points.length; k++) {
                        points[k] = points[k] + normalisedWeights[j][i] * rules[j].getAntecedent(i).getPoint(k);
                    }
                }
                FuzzyNumber intermediateFuzzyTerm = rules[0].getAntecedent(i).getClass().newInstance();
                intermediateFuzzyTerm.setPoints(points);
                intermediateRule.setAntecedent(i, intermediateFuzzyTerm);
            }
        }
        points = new float[rules[0].getConsequence().getPoints().length];

        for (int j = 0; j < rules.length; j++) {
            for (int k = 0; k < points.length; k++) {
                points[k] = points[k] + normalisedWeights[j][rules[0].getNumberOfAntecedents()] *
                        rules[j].getConsequence().getPoint(k);
            }
        }
        FuzzyNumber intermediateConsequence = rules[0].getConsequence().getClass().newInstance();
        intermediateConsequence.setPoints(points);
        intermediateRule.setConsequence(intermediateConsequence);
        double delta = 0;
        Rule shiftedIntermediateRule = new Rule(rules[0]);
        for (int i = 0; i < intermediateRule.getNumberOfAntecedents(); i++) {
            if ((i != missingIndex1) && (i != missingIndex2)) {
                delta = observation.getAntecedent(i).distanceTo(intermediateRule.getAntecedent(i)) /
                        (intermediateRule.getMaxAntecedentRanges()[i] - intermediateRule.getMinAntecedentRanges()[i]);
                shiftedIntermediateRule.setAntecedent(i, intermediateRule.getAntecedent(i).adds(delta *
                        (intermediateRule.getMaxAntecedentRanges()[i] - intermediateRule.getMinAntecedentRanges()[i])));

            }
        }
        delta = observation.getConsequence().distanceTo(intermediateRule.getConsequence()) /
                (intermediateRule.getMaxConsequenceRange() - intermediateRule.getMinConsequenceRange());
        shiftedIntermediateRule.setConsequence(intermediateRule.getConsequence().adds(delta *
                (intermediateRule.getMaxConsequenceRange() - intermediateRule.getMinConsequenceRange())));

        for (int j = 0; j < observation.getAntecedents().length; j++) {
            if ((j != missingIndex1) && (j != missingIndex2)) {
                s = Process.getScaleRate(shiftedIntermediateRule.getAntecedents()[j], observation.getAntecedents()[j]);
                //  System.out.println(j + " s " + s);
                intermediateRule.getAntecedents()[j] = Process.scale(shiftedIntermediateRule.getAntecedents()[j], s);
                sSum = sSum + s;
                m = Process.getMoveRatio(intermediateRule.getAntecedents()[j], observation.getAntecedents()[j]);
                //   System.out.println(j + " m " + m);
                intermediateRule.getAntecedents()[j] = Process.move(intermediateRule.getAntecedents()[j], m);
                mSum = mSum + m;
            }
        }

        s = Process.getScaleRate(shiftedIntermediateRule.getConsequence(), observation.getConsequence());
        intermediateRule.setConsequence(Process.scale(shiftedIntermediateRule.getConsequence(), s));

        m = Process.getMoveRatio(intermediateRule.getConsequence(), observation.getConsequence());
        intermediateRule.setConsequence(Process.move(intermediateRule.getConsequence(), m));
        //0 1
        s = s * observation.getAntecedents().length - sSum;
        //-1 1
        m = m * observation.getAntecedents().length - mSum;
        return new double[]{s, m};
    }

    public static double[][] calculateParametersSM(Rule[] rules, Observation observation, int missingIndex1, int missingIndex2, int slice) throws Exception, InstantiationException {
        double sSum = 0;
        double s;
        double mSum = 0;
        double m;
        float[][] normalisedWeights = Process.getNormalisedTermWeights(observation, rules, missingIndex1, missingIndex2);
        Rule intermediateRule = new Rule(rules[0]);
        float[] points;
        for (int i = 0; i < rules[0].getNumberOfAntecedents(); i++) {
            if (i != missingIndex1 && i != missingIndex2) {
                points = new float[rules[0].getAntecedent(i).getPoints().length];
                for (int j = 0; j < rules.length; j++) {
                    for (int k = 0; k < points.length; k++) {
                        points[k] = points[k] + normalisedWeights[j][i] * rules[j].getAntecedent(i).getPoint(k);
                    }
                }
                FuzzyNumber intermediateFuzzyTerm = rules[0].getAntecedent(i).getClass().newInstance();
                intermediateFuzzyTerm.setPoints(points);
                intermediateRule.setAntecedent(i, intermediateFuzzyTerm);
            }
        }
        points = new float[rules[0].getConsequence().getPoints().length];

        for (int j = 0; j < rules.length; j++) {
            for (int k = 0; k < points.length; k++) {
                points[k] = points[k] + normalisedWeights[j][rules[0].getNumberOfAntecedents()] *
                        rules[j].getConsequence().getPoint(k);
            }
        }
        FuzzyNumber intermediateConsequence = rules[0].getConsequence().getClass().newInstance();
        intermediateConsequence.setPoints(points);
        intermediateRule.setConsequence(intermediateConsequence);
        double delta = 0;
        Rule shiftedIntermediateRule = new Rule(rules[0]);
        for (int i = 0; i < intermediateRule.getNumberOfAntecedents(); i++) {
            if ((i != missingIndex1) && (i != missingIndex2)) {
                delta = observation.getAntecedent(i).distanceTo(intermediateRule.getAntecedent(i)) /
                        (intermediateRule.getMaxAntecedentRanges()[i] - intermediateRule.getMinAntecedentRanges()[i]);
                shiftedIntermediateRule.setAntecedent(i, intermediateRule.getAntecedent(i).adds(delta *
                        (intermediateRule.getMaxAntecedentRanges()[i] - intermediateRule.getMinAntecedentRanges()[i])));

            }
        }
        delta = observation.getConsequence().distanceTo(intermediateRule.getConsequence()) /
                (intermediateRule.getMaxConsequenceRange() - intermediateRule.getMinConsequenceRange());
        shiftedIntermediateRule.setConsequence(intermediateRule.getConsequence().adds(delta *
                (intermediateRule.getMaxConsequenceRange() - intermediateRule.getMinConsequenceRange())));

        for (int j = 0; j < observation.getAntecedents().length; j++) {
            if ((j != missingIndex1) && (j != missingIndex2)) {
                s = Process.getScaleRate(shiftedIntermediateRule.getAntecedents()[j], observation.getAntecedents()[j]);
                //  System.out.println(j + " s " + s);
                intermediateRule.getAntecedents()[j] = Process.scale(shiftedIntermediateRule.getAntecedents()[j], s);
                sSum = sSum + s;
                m = Process.getMoveRatio(intermediateRule.getAntecedents()[j], observation.getAntecedents()[j]);
                //   System.out.println(j + " m " + m);
                intermediateRule.getAntecedents()[j] = Process.move(intermediateRule.getAntecedents()[j], m);
                mSum = mSum + m;
            }
        }

        s = Process.getScaleRate(shiftedIntermediateRule.getConsequence(), observation.getConsequence());
        intermediateRule.setConsequence(Process.scale(shiftedIntermediateRule.getConsequence(), s));

        m = Process.getMoveRatio(intermediateRule.getConsequence(), observation.getConsequence());
        intermediateRule.setConsequence(Process.move(intermediateRule.getConsequence(), m));
        //0 1
        s = s * observation.getAntecedents().length - sSum;
        //-1 1
        m = m * observation.getAntecedents().length - mSum;
        // System.out.println(s + " " + m);
        double[][] parameterSM = Process.calculateParametersSM(s, m, slice);
        Process.printParameters(parameterSM, "SMFile");

        return parameterSM;
    }

    public static Rule[] TwoMissingAntecedentIntermediateRules(Rule[] rules, Observation observation, int missingIndex1, int missingIndex2,
                                                               double[][] weightsParameters) throws Exception, InstantiationException {
        float[][] normalisedWeights = Process.getNormalisedTermWeights(observation, rules, missingIndex1, missingIndex2);
        // float[] totalNormalisedWeights = Process.gettotalNormalisedWeights(rules, observation, missingIndex1, missingIndex2);
        Rule[] intermediateRules1 = new Rule[weightsParameters.length];

        for (int i = 0; i < weightsParameters.length; i++) {
            intermediateRules1[i] = Process.getIntermediateFuzzyRule(rules, normalisedWeights, missingIndex1, missingIndex2,
                    weightsParameters[i][0], weightsParameters[i][1], weightsParameters[i][2], weightsParameters[i][3]);
        }
        return intermediateRules1;
    }

    public static Rule[] TwoMissingAntecedentShiftedRules(Rule[] rules, Rule[] intermediateRules1, Observation observation, int missingIndex1, int missingIndex2,
                                                          double[][] parameterDelta) {

        float delta = 0;
        Rule shiftedIntermediateRule = new Rule(rules[0]);
        Rule[] shiftedRule = new Rule[parameterDelta.length * intermediateRules1.length];

        for (int i = 0; i < intermediateRules1[0].getNumberOfAntecedents(); i++) {
            if ((i != missingIndex1) && (i != missingIndex2)) {
                delta = observation.getAntecedent(i).distanceTo(intermediateRules1[0].getAntecedent(i)) /
                        (intermediateRules1[0].getMaxAntecedentRanges()[i] - intermediateRules1[0].getMinAntecedentRanges()[i]);
                shiftedIntermediateRule.setAntecedent(i, intermediateRules1[0].getAntecedent(i).adds(delta *
                        (intermediateRules1[0].getMaxAntecedentRanges()[i] - intermediateRules1[0].getMinAntecedentRanges()[i])));

            } else shiftedIntermediateRule.setAntecedent(i, (new TriangularFuzzyNumber(0f, 0f, 0f)));
        }
        delta = observation.getConsequence().distanceTo(intermediateRules1[0].getConsequence()) /
                (intermediateRules1[0].getMaxConsequenceRange() - intermediateRules1[0].getMinConsequenceRange());

        shiftedIntermediateRule.setConsequence(intermediateRules1[0].getConsequence().adds(delta *
                (intermediateRules1[0].getMaxConsequenceRange() - intermediateRules1[0].getMinConsequenceRange())));

        for (int i = 0; i < parameterDelta.length * intermediateRules1.length; i++) {
            shiftedRule[i] = new Rule(intermediateRules1[0]);
        }

        for (int i = 0; i < parameterDelta.length * intermediateRules1.length; i++) {

            for (int j = 0; j < intermediateRules1[0].getNumberOfAntecedents(); j++) {
                if ((j != missingIndex1) && (j != missingIndex2)) {
                    shiftedRule[i].setAntecedent(j, shiftedIntermediateRule.getAntecedent(j));
                }
                shiftedRule[i].setConsequence(shiftedIntermediateRule.getConsequence());
            }
        }

        for (int h = 0; h < intermediateRules1.length; h++) {
            for (int k = 0; k < parameterDelta.length; k++) {
                shiftedRule[h * intermediateRules1.length + k].setAntecedent(missingIndex1, intermediateRules1[h].getAntecedent(missingIndex1).adds(
                        parameterDelta[k][0] * (intermediateRules1[h].getMaxAntecedentRanges()[missingIndex1] -
                                intermediateRules1[h].getMinAntecedentRanges()[missingIndex1])));

                shiftedRule[h * intermediateRules1.length + k].setAntecedent(missingIndex2, intermediateRules1[h].getAntecedent(missingIndex2).adds(
                        parameterDelta[k][1] * (intermediateRules1[h].getMaxAntecedentRanges()[missingIndex2] -
                                intermediateRules1[h].getMinAntecedentRanges()[missingIndex2])));

            }
        }
        return shiftedRule;
    }

    public static Rule[] TwoMissingAntecedentTransformedRules(Rule[] shiftedRule, Observation observation, int missingIndex1, int missingIndex2,
                                                              double[][] parameterSM) throws Exception {
        Rule[] transformedRules = new Rule[shiftedRule.length * parameterSM.length];
        for (int i = 0; i < shiftedRule.length; i++) {
            for (int j = 0; j < parameterSM.length; j++) {
                transformedRules[i * shiftedRule.length + j] = Process.multiBackwardInterpolation(shiftedRule[i], observation, missingIndex1, missingIndex2, parameterSM[j][0], parameterSM[j][1], parameterSM[j][2], parameterSM[j][3]);
            }
        }
        return transformedRules;
    }

    public static void initialise() {

    }

    public static void backwardInterpolate(double[] parameters) {


    }

    public static double[] backwardTwoMissingAntecedent(RuleBase ruleBase, Rule[] rules, Observation observation, int missingIndex1, int missingIndex2,
                                                        double[][] parameterWeights, double[][] parameterDelta, double[][] parameterSM) throws Exception, InstantiationException {

        double[] missingAntecedents = new double[2];

        Rule[] intermediateRules1 = new Rule[parameterWeights.length];
        intermediateRules1 = TwoMissingAntecedentIntermediateRules(rules, observation, missingIndex1, missingIndex2, parameterWeights);


        Rule[] shiftedRule = new Rule[parameterDelta.length * intermediateRules1.length];
        shiftedRule = TwoMissingAntecedentShiftedRules(rules, intermediateRules1, observation, missingIndex1, missingIndex2, parameterDelta);

        Rule[] transformedRules = new Rule[parameterSM.length * shiftedRule.length];
        transformedRules = TwoMissingAntecedentTransformedRules(shiftedRule, observation, missingIndex1, missingIndex2, parameterSM);


        double[] evaluate = new double[parameterSM.length * shiftedRule.length];
        double offset = Double.MAX_VALUE;
        int smallestOffIndex = -1;
        int smallestOffsetIndexofSM = -1;
        int smallestOffsetIndexofDelta = -1;
        int smallestOffsetIndexofWeights = -1;

        Rule[] secondForwardRule = new Rule[parameterSM.length * shiftedRule.length];


        for (int i = 0; i < parameterSM.length * shiftedRule.length; i++) {
            rules = Process.getClosestRules(ruleBase, transformedRules[i], numberClosestRules, true);
            secondForwardRule[i] = secondForwardInterpolation(rules, transformedRules[i]);
        }

        for (int i = 0; i < parameterSM.length * shiftedRule.length; i++) {

            evaluate[i] = Process.evaluate(observation, secondForwardRule[i]);
            if ((!((Float) transformedRules[i].getAntecedents()[missingIndex1].getPoint(0)).isNaN()) && (!((Float) transformedRules[i].getAntecedents()[missingIndex2].getPoint(0)).isNaN())) {

                if (evaluate[i] < offset) {
                    offset = evaluate[i];
                    smallestOffIndex = i;
                    smallestOffsetIndexofWeights = i / shiftedRule.length / parameterWeights.length;
                    smallestOffsetIndexofDelta = i / shiftedRule.length % parameterWeights.length;
                    smallestOffsetIndexofSM = i % shiftedRule.length;
                }
            }
        }
        System.out.println("Backward interpolation antecedent: " + missingIndex1 + transformedRules[smallestOffIndex].getAntecedents()[missingIndex1].toString() + transformedRules[smallestOffIndex].getAntecedents()[missingIndex1].getRepresentativeValue());
        System.out.println("Backward interpolation antecedent: " + missingIndex2 + transformedRules[smallestOffIndex].getAntecedents()[missingIndex2].toString() + transformedRules[smallestOffIndex].getAntecedents()[missingIndex2].getRepresentativeValue());
        System.out.println("Second forward interpolation conquence: " + secondForwardRule[smallestOffIndex].getConsequence().toString() + secondForwardRule[smallestOffIndex].getConsequence().getRepresentativeValue());

        System.out.print("The smallest offset is: " + offset + " at ");
        System.out.println("parameters Number (ParameterWeights)(ParameterDelta)and (ParameterSM):" + smallestOffsetIndexofWeights + "  " + smallestOffsetIndexofDelta + "  " + smallestOffsetIndexofSM);


        missingAntecedents[0] = transformedRules[smallestOffIndex].getAntecedents()[missingIndex1].getRepresentativeValue();
        missingAntecedents[1] = transformedRules[smallestOffIndex].getAntecedents()[missingIndex2].getRepresentativeValue();
        return missingAntecedents;
    }

}



