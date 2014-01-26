package app;

import fuzzy.FuzzyNumber;
import fuzzy.TriangularFuzzyNumber;
import hierarchicalinterpolation.control.BackwardProcess;
import hierarchicalinterpolation.control.Process;
import hierarchicalinterpolation.model.Observation;
import hierarchicalinterpolation.model.Rule;
import hierarchicalinterpolation.model.RuleBase;
import originalharmonysearch.applications.interpolation.InterpolationHarmonyComparator;
import originalharmonysearch.applications.interpolation.InterpolationHarmonyMemory;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by IntelliJ IDEA.
 * User: rxd846
 * Date: Feb 1, 2010
 * Time: 2:24:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestMain {

    private static String ruleBaseFilePath = "RuleBaseRaw.txt";
    private static String observationFilePath = "o1.txt";
    //  private static String offsetFilePath = "C:\\Users\\shj1\\IdeaProjects\\excution time test for parametric approach\\FuzzyHierarchicalInterpolation\\src\\offset.txt";


    private static Random random = new Random();

    private static Rule[] closestRules;

    private static int missingIndex1 = 2;
    private static int missingIndex2 = 4;
    private static int numberClosestRules = 2;
    private static int numberAntecedents = 5;
    private static float[] range = new float[numberAntecedents];
    private static float minConsequenceRange = Float.MAX_VALUE;
    private static float maxConsequenceRange = -Float.MAX_VALUE;
    //private static int slice = 6;

    public static void main(String args[]) throws Exception {

        RuleBase ruleBase = parseRuleBase(ruleBaseFilePath);
        /*Observation observation = new Observation(ruleBase.get(0));
        observation.setAntecedent(0, new TriangularFuzzyNumber(2,3,4));
        observation.setAntecedent(1, new TriangularFuzzyNumber(2,3,4));
        observation.setAntecedent(2, new TriangularFuzzyNumber(2,3,4));
        observation.setAntecedent(3, new TriangularFuzzyNumber(2,3,4));
        observation.setAntecedent(4, new TriangularFuzzyNumber(2,3,4));*/
        int observationIndex = random.nextInt(ruleBase.size());
        Observation originalObservation = new Observation(ruleBase.get(observationIndex));
        for (int a = 0; a < ruleBase.get(observationIndex).getNumberOfAntecedents(); a++) {
            originalObservation.setAntecedent(a, ruleBase.get(observationIndex).getAntecedent(a));
        }
        originalObservation.setConsequence(ruleBase.get(observationIndex).getConsequence());
        Observation observation =  new Observation(ruleBase.get(observationIndex));
        for (int a = 0; a < ruleBase.get(observationIndex).getNumberOfAntecedents(); a++) {
            observation.setAntecedent(a, ruleBase.get(observationIndex).getAntecedent(a));
        }
        observation.setConsequence(ruleBase.get(observationIndex).getConsequence());
        ruleBase.remove(observationIndex);

        for (int i = 1; i <= 10; i++) {
            String offsetFilePath = "offset" + i + ".txt";
            FileOutputStream ostream = new FileOutputStream(offsetFilePath);
            DataOutputStream out = new DataOutputStream(ostream);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));

            bw.write("slice consequenceoffset averageantecedentoffset excutedtime effective");
            bw.newLine();

            long t1 = System.nanoTime();

            {
                observation.setAntecedent(missingIndex1, null);
                observation.setAntecedent(missingIndex2, null);

                closestRules = Process.getClosestRules(ruleBase, observation, numberClosestRules, true);

                BackwardProcess backwardProcess = new BackwardProcess(ruleBase, observation, closestRules, missingIndex1, missingIndex2);

                double[] totals = new double[0];
                //try {
                    totals = Process.calculateTotals(closestRules, observation, missingIndex1, missingIndex2);
                //} catch (Exception e) {
                //    main(null);
                //}

                InterpolationHarmonyMemory harmonyMemory = new InterpolationHarmonyMemory(Process.createParameterRanges(), Process.calculateValueRanges(totals), totals, backwardProcess);
                harmonyMemory.fill();
                harmonyMemory.iterate(100000);

                double smallestOffset = harmonyMemory.best().getMerit();
                double[] parameters = ((InterpolationHarmonyComparator) harmonyMemory.getHarmonyComparator()).translate(harmonyMemory.best());
                Rule rule = backwardProcess.backwardInterpolate(parameters);
                bw.write("0" + " " + smallestOffset + " ");
                double antecedentOffset1 = Math.abs(originalObservation.getAntecedent(missingIndex1).getRepresentativeValue() - rule.getAntecedent(missingIndex1).getRepresentativeValue()) / range[missingIndex1];
                double antecedentOffset2 = Math.abs(originalObservation.getAntecedent(missingIndex2).getRepresentativeValue() - rule.getAntecedent(missingIndex2).getRepresentativeValue()) / range[missingIndex2];
                System.out.println("smallestOffset = " + smallestOffset);
                System.out.println("missingAntecedent1 Offset = " + antecedentOffset1);
                System.out.println("missingAntecedent2 Offset = " + antecedentOffset2);

                bw.write((antecedentOffset1 + antecedentOffset2) / 2 + " ");

            }
            long t2 = System.nanoTime();
            //System.out.println("Your program has executed for " + (int)((t2-t1)/1000)+ " seconds " + ((t2-t1) % 1000) + " micro seconds");
            double t = t2 - t1;
            bw.write(t / 1000000000 + " ");
            System.out.println("excute time= " + t);
            bw.newLine();
            //}

            bw.close();
            out.close();
            ostream.close();
        }


    }


    /*public static void main(String args[]) throws Exception {

        RuleBase ruleBase = parseRuleBase();
        RuleBase observations = parseObservation();
        //Process.printRules(observations);
        Observation observation = new Observation(ruleBase.get(0));


        for (int i = 1; i <= 10; i++) {
            String offsetFilePath = "C:\\Users\\shj1\\IdeaProjects\\excution time test for parametric approach\\FuzzyHierarchicalInterpolation\\src\\offset" + i + ".txt";
            FileOutputStream ostream = new FileOutputStream(offsetFilePath);
            DataOutputStream out = new DataOutputStream(ostream);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));

            bw.write("slice consequenceoffset averageantecedentoffset excutedtime effective");
            bw.newLine();

            for (slice = 6; slice <= 100; slice++) {

                long t1 = System.nanoTime();

                {

                    // observation = (Observation) observations.get(i);

                    observation.getAntecedents()[0] = observations.get(0).getAntecedent(0);
                    observation.getAntecedents()[1] = observations.get(0).getAntecedent(1);
                    observation.getAntecedents()[2] = observations.get(0).getAntecedent(2);
                    observation.getAntecedents()[3] = observations.get(0).getAntecedent(3);
                    observation.getAntecedents()[4] = observations.get(0).getAntecedent(4);
                    observation.setConsequence(observations.get(0).getConsequence());

                    Process.printRule(observation);

                    observation.setAntecedent(missingIndex1, new TriangularFuzzyNumber());
                    observation.setAntecedent(missingIndex2, new TriangularFuzzyNumber());

                    closestRules = Process.getClosestRules(ruleBase, observation, numberClosestRules, true);

                    BackwardProcess backwardProcess = new BackwardProcess(ruleBase, observation, closestRules, missingIndex1, missingIndex2);

                    double[][] parameters = Process.calculateParameters(closestRules, observation, missingIndex1, missingIndex2, slice);

                    double smallestOffset = Double.MAX_VALUE;
                    int smallestOffsetIndex = -1;
                    for (int j = 0; j < parameters.length; j++) {
                        Rule rule = backwardProcess.backwardInterpolate(parameters[j]);
                        double offset = backwardProcess.evaluateForwardConsequence(rule) / rangeC;
                        smallestOffsetIndex = (offset < smallestOffset) ? j : smallestOffsetIndex;
                        smallestOffset = (offset < smallestOffset) ? offset : smallestOffset;
                    }
                    System.out.println("slice = " + slice);

                    bw.write(slice + " " + smallestOffset + " ");

                    *//*    System.out.println("Parameters Used:");
                                        for ( int k = 0; k < parameters[smallestOffsetIndex].length; k++) {
                                            System.out.println(" [" + k + "]\t" + parameters[smallestOffsetIndex][k]);

                                        }
                    *//*
                    Rule rule = backwardProcess.backwardInterpolate(parameters[smallestOffsetIndex]);
                    double antecedentOffset1 = Math.abs(observation.getAntecedent(missingIndex1).getRepresentativeValue() - rule.getAntecedent(missingIndex1).getRepresentativeValue()) / range[missingIndex1];
                    double antecedentOffset2 = Math.abs(observation.getAntecedent(missingIndex2).getRepresentativeValue() - rule.getAntecedent(missingIndex2).getRepresentativeValue()) / range[missingIndex2];
                    System.out.println("missingAntecedent1 Offset = " + antecedentOffset1);
                    System.out.println("missingAntecedent2 Offset = " + antecedentOffset2);

                    bw.write((antecedentOffset1 + antecedentOffset2) / 2 + " ");


                }
                long t2 = System.nanoTime();
                //System.out.println("Your program has executed for " + (int)((t2-t1)/1000)+ " seconds " + ((t2-t1) % 1000) + " micro seconds");
                double t = t2 - t1;
                bw.write(t / 1000000000 + " ");
                System.out.println("excute time= " + t);
                bw.newLine();
            }

            bw.close();
            out.close();
            ostream.close();
        }


    }
*/

    public static RuleBase parseRuleBase(String ruleBaseFilePath) throws IOException {
        Scanner scanner = new Scanner(new File(ruleBaseFilePath));
        ArrayList<float[]> data = new ArrayList<float[]>();
        String line;
        while (scanner.hasNext()) {
            line = scanner.nextLine();
            String[] splitLine = line.split(" ");
            float[] currentData = new float[splitLine.length];
            for (int j = 0; j < currentData.length; j++) {
                currentData[j] = Float.parseFloat(splitLine[j]);
            }
            data.add(currentData);
        }

        //int numberAntecedents = data.get(0).length - 1;
        float[] minAntecedentRanges = new float[numberAntecedents];
        float[] maxAntecedentRanges = new float[numberAntecedents];
        for (int j = 0; j < numberAntecedents; j++) {
            float minAntecedentRange = Float.MAX_VALUE;
            float maxAntecedentRange = -Float.MAX_VALUE;
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i)[j] < minAntecedentRange) minAntecedentRange = data.get(i)[j];
                if (data.get(i)[j] > maxAntecedentRange) maxAntecedentRange = data.get(i)[j];
            }
            minAntecedentRanges[j] = minAntecedentRange;
            maxAntecedentRanges[j] = maxAntecedentRange;
            range[j] = maxAntecedentRange - minAntecedentRange;
        }

        // float minConsequenceRange = -2;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i)[numberAntecedents] < minConsequenceRange)
                minConsequenceRange = data.get(i)[numberAntecedents];
        }

        // float maxConsequenceRange = 2;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i)[numberAntecedents] > maxConsequenceRange)
                maxConsequenceRange = data.get(i)[numberAntecedents];
        }

        RuleBase ruleBase = new RuleBase(
                minAntecedentRanges, maxAntecedentRanges, minConsequenceRange, maxConsequenceRange);

        for (int i = 0; i < data.size(); i++) {
            ruleBase.add(
                    new FuzzyNumber[]{
                            new TriangularFuzzyNumber(data.get(i)[0] - 1, data.get(i)[0], data.get(i)[0] + 1),
                            new TriangularFuzzyNumber(data.get(i)[1] - 1, data.get(i)[1], data.get(i)[1] + 1),
                            new TriangularFuzzyNumber(data.get(i)[2] - 1, data.get(i)[2], data.get(i)[2] + 1),
                            new TriangularFuzzyNumber(data.get(i)[3] - 1, data.get(i)[3], data.get(i)[3] + 1),
                            new TriangularFuzzyNumber(data.get(i)[4] - 1, data.get(i)[4], data.get(i)[4] + 1),
                    },
                    new TriangularFuzzyNumber(data.get(i)[5] - 1, data.get(i)[5], data.get(i)[5] + 1));
        }
        scanner.close();

        return ruleBase;
    }

    /*public static RuleBase parseObservation() throws IOException {
        FileInputStream stream = new FileInputStream(observationFilePath);
        DataInputStream in = new DataInputStream(stream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        ArrayList<float[]> data = new ArrayList<float[]>();
        String line = br.readLine();
        while (line != null) {

            String[] splitLine = line.split(" ");
            float[] currentData = new float[splitLine.length];
            for (int j = 0; j < currentData.length; j++) {
                currentData[j] = Float.parseFloat(splitLine[j]);
            }
            data.add(currentData);
            line = br.readLine();
        }

        // int numberAntecedents = data.get(0).length - 1;
        float[] minAntecedentRanges = new float[numberAntecedents];
        float[] maxAntecedentRanges = new float[numberAntecedents];
        for (int j = 0; j < numberAntecedents; j++) {
            float minAntecedentRange = Float.MAX_VALUE;
            float maxAntecedentRange = Float.MIN_VALUE;
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i)[j] < minAntecedentRange) minAntecedentRange = data.get(i)[j];
                if (data.get(i)[j] > maxAntecedentRange) maxAntecedentRange = data.get(i)[j];
            }
            minAntecedentRanges[j] = minAntecedentRange;
            maxAntecedentRanges[j] = maxAntecedentRange;
        }

        float minConsequenceRange = -2;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i)[numberAntecedents] < minConsequenceRange)
                minConsequenceRange = data.get(i)[numberAntecedents];
        }

        float maxConsequenceRange = 2;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i)[numberAntecedents] > maxConsequenceRange)
                maxConsequenceRange = data.get(i)[numberAntecedents];
        }
        RuleBase observation = new RuleBase(
                minAntecedentRanges, maxAntecedentRanges, minConsequenceRange, maxConsequenceRange);

        for (int i = 0; i < data.size(); i++) {
            observation.add(
                    new FuzzyNumber[]{
                            new TriangularFuzzyNumber(data.get(i)[0] - random.nextFloat(), data.get(i)[0], data.get(i)[0] + random.nextFloat()),
                            new TriangularFuzzyNumber(data.get(i)[1] - random.nextFloat(), data.get(i)[1], data.get(i)[1] + random.nextFloat()),
                            new TriangularFuzzyNumber(data.get(i)[2] - random.nextFloat(), data.get(i)[2], data.get(i)[2] + random.nextFloat()),
                            new TriangularFuzzyNumber(data.get(i)[3] - random.nextFloat(), data.get(i)[3], data.get(i)[3] + random.nextFloat()),
                            new TriangularFuzzyNumber(data.get(i)[4] - random.nextFloat(), data.get(i)[4], data.get(i)[4] + random.nextFloat()),
                    },
                    new TriangularFuzzyNumber(data.get(i)[5] - random.nextFloat(), data.get(i)[5], data.get(i)[5] + random.nextFloat()));
        }

        br.close();
        in.close();
        stream.close();

        return observation;
    }
*/
    public static void testForwardInterpolation(RuleBase ruleBase, Observation observation) throws Exception, InstantiationException {
        Rule[] closestRules = Process.getClosestRules(ruleBase, observation, numberClosestRules, true);
        Rule intermediateRule = Process.getIntermediateFuzzyRule(closestRules, observation, observation.getAntecedents().length);
        Rule transformedRule = Process.transform(intermediateRule, observation, observation.getAntecedents().length);
        System.out.print("Forward interpolation result:" + transformedRule.getConsequence() + " " + transformedRule.getConsequence().getRepresentativeValue());
    }
}
