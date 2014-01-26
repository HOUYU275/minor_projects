package test;

import fuzzy.FuzzyNumber;
import fuzzyharmonysearch.applications.fuzzyspring.FuzzySpringHarmonyMemory;
import fuzzyharmonysearch.core.ValueRange;
import originalharmonysearch.applications.beam.BeamHarmonyMemory;
import originalharmonysearch.applications.constraintfour.ConstraintFourHarmonyMemory;
import originalharmonysearch.applications.spring.SpringHarmonyMemory;
import originalharmonysearch.applications.vessel.VesselHarmonyMemory;
import util.Dataset;
import util.GraphingData;
import weka.attributeSelection.*;
import weka.classifiers.Classifier;
import weka.classifiers.meta.FeatureSelectionEnsemble;
import weka.core.Instances;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Random;

/*import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math.stat.inference.TTestImpl;*/

/**
 * Created by IntelliJ IDEA.
 * User: rxd846
 * Date: Feb 1, 2010
 * Time: 2:24:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestMain {

    public static int[] consolidate(int[] input, int j, int k) {
        if (j >= k) return input;
        if (input[j] == -1) {
            if (input[k] == -1) return consolidate(input, j, k -1);
            else {
                input[j] = input[k];
                input[k] = -1;
                return consolidate(input, j + 1, k - 1);
            }
        }
        else return consolidate(input, j + 1, k);
    }



    public static void main(String args[]) throws Exception {

        int[] testInput = new int[3];
        for (int i = 0; i < testInput.length; i++) {
            testInput[i] = (new Random()).nextInt(2) - 1;
        }
        for (int i : testInput) System.out.print(i + "  ");
        System.out.println();
        int[] output = consolidate(testInput, 0, testInput.length - 1);
        for (int i : output) System.out.print(i + "  ");
        System.out.println();

        System.out.println("\nEvaluating Dataset - " + "COPD area data2 fs");
        int numFolds = 10;

        Instances data;
        data = Dataset.getDataset("COPD area data2 fs");
        data.setClassIndex(data.numAttributes() - 1);

        FeatureSelectionEnsemble featureSelectionEnsemble = new FeatureSelectionEnsemble();
        featureSelectionEnsemble.buildClassifier(data);
        featureSelectionEnsemble.classifyInstance(data.instance(0));

        //classifierAggregation(new VQNN(), 3, "COPD area data2_fs");

        //testFeatureSelectionEnsemble(new VQNN());

        /*double merits = 0;
        for (int i = 0; i < 10; i++) {
            merits += fuzzyOptimisationTest();
        }
        System.out.println("AVG = " + merits / 10);*/

        //testFeatureSelectionEnsemble(new FuzzyRoughNN());
        //testFeatureSelectionEnsemblePairedTTest(new FuzzyRoughNN(), 0, true);
        //testFeatureSelectionEnsemble(new VQNN());
        //testFeatureSelectionEnsembleMixed();
        //testFeatureSelectionEnsemble(new FuzzyRoughNN());

        /*int[][] m = new int[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j ++) {
                m[i][j] = (new Random()).nextInt(10);
                System.out.println(m[i][j] + " ");
            }
            System.out.println();
        }
        printrr(m);*/

        //testHierarchicalHarmonySearch("CfsSubsetEval", 50000, false, 1);

        /*JFrame f1 = new JFrame();
        f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GraphingData graphingData1 = new GraphingData();
        graphingData1.setData(((PigsHarmonyMemory)featureSelectionHarmonyMemory).getHistoricalAcceptedValues()[0]);
        f1.add(graphingData1);
        f1.setSize(1000,1000);
        f1.setLocation(200,200);
        f1.setVisible(true);

        JFrame f2 = new JFrame();
        f2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GraphingData graphingData2 = ne w GraphingData();
        graphingData2.setData(((PigsHarmonyMemory)featureSelectionHarmonyMemory).getHistoricalAcceptedValues()[1]);
        f2.add(graphingData2);
        f2.setSize(800,800);
        f2.setLocation(200,200);
        f2.setVisible(true);*/

        //System.out.println(0 / 0);

        //ResultProcessor.processResults("artificial2_0.txt", "artificial2_1.txt", "artificial2_fnn.arff");
        //String result = ResultProcessor.readFile("artificial4_frnn2_s.arff", 0).toString();
        //System.out.println(result);
        //harmonySearch("sonar", "CfsSubsetEval", 1000, 10);
        //harmonySearch("sonar", "FuzzyRoughSubsetEval", 1000, 10);

        //testFeatureSelectionEnsemble(new FuzzyRoughNN());

        //ResultFormatter.stripNumbers();
        //testFeatureSelectionEnsemble(new FuzzyRoughNN());
        //testFeatureSelectionEnsemble(new VQNN());
        //testFeatureSelectionEnsembleMixed();
        //testFeatureSelectionEnsemble(new FuzzyRoughNN());

        //vessel(HarmonyMemory.createParameterRanges(20, 0.5, 0.8, 0.1), 1000, 5000);
        //vessel(HarmonyMemory.createParameterRanges(20, 20, 0.8, 0.8, 0.85, 0.85, 0.001, 0.001), 100, 10000);
        //constraintFour(HarmonyMemory.createParameterRanges(20, 0.5, 0.8, 0.1), 100, 5000);
        //spring(HarmonyMemory.createParameterRanges(20, 0.5, 0.8, 0.1), 1000, 1000);
        //beam(HarmonyMemory.createParameterRanges(20, 0.5, 0.8, 0.1), 1000, 10000);
        /*System.out.println("\nVar 20, 0.5, 0.8, 0.1");
        beam(HarmonyMemory.createParameterRanges(20, 0.5, 0.8, 0.1), 100, 10000);
        System.out.println("\nVar 20, 0.5");
        beam2(100, 10000);
        System.out.println("\nFixed 20, 20, 0.8, 0.8, 0.85, 0.85, 0.001, 0.001");
        beam3(100, 10000);*/
    }

    public static double fuzzyOptimisationTest() throws Exception {
        System.out.println(FuzzyNumber.fuzzyLessThan(3.2, 3.1, 0.3));

        /*ValueRange[] valueRanges = new ValueRange[3];
        valueRanges[0] = new ValueRange(0, 20, true);
        for (int i = 1; i < valueRanges.length; i++) {
            valueRanges[i] = new ValueRange(0, 20, true);
        }

        PigsHarmonyMemory harmonyMemory =
                new PigsHarmonyMemory(
                        HarmonyMemory.createParameterRanges(20, 0.5),
                        valueRanges, new Random(new Random().nextLong()));*/

        /*SpringHarmonyMemory harmonyMemory =
                new SpringHarmonyMemory(
                        HarmonyMemory.createParameterRanges(20, 0.5),
                        new ValueRange[]{
                                new ValueRange(0, 0.1, true),
                                new ValueRange(0, 0.5, true),
                                new ValueRange(8, 15, true)});

        harmonyMemory.fill();
        harmonyMemory.iterate(20000);

        System.out.println("Harmony: " + harmonyMemory.best().toString());
        System.out.println("Merit: " + harmonyMemory.best().getMerit());
        return harmonyMemory.best().getMerit();*/

        FuzzySpringHarmonyMemory harmonyMemory =
                new FuzzySpringHarmonyMemory(
                        ValueRange.createParameterRanges(20, 0.5),
                        new ValueRange[]{new ValueRange(0, 0.1, true), new ValueRange(0, 0.5, true), new ValueRange(8, 15, true)});

        harmonyMemory.fill();
        harmonyMemory.iterate(5000);

        System.out.println("Harmony: " + harmonyMemory.best().toString());
        System.out.println("Merit: " + harmonyMemory.best().getMerit().toString() + " - " + harmonyMemory.best().getMerit().getRepresentativeValue());
        return harmonyMemory.best().getMerit().getRepresentativeValue();

        //pigsHarmonyMemory.printHarmonies();

        //System.out.println("Merit: " + harmonyMemory.best().getMerit());

        //((FuzzySpringHarmonyComparator) harmonyMemory.getHarmonyComparator()).checkConstraintVerbose(harmonyMemory.best());

        //plotGraph(((PigsHarmonyMemory) featureSelectionHarmonyMemory).getHistoricalAcceptedValues()[2]);
    }

    public static void plotGraph(double[] input) {
        JFrame f3 = new JFrame();
        f3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GraphingData graphingData3 = new GraphingData();
        graphingData3.setData(input);
        f3.add(graphingData3);
        f3.setSize(1200, 1000);
        f3.setLocation(200, 200);
        f3.setVisible(true);
    }

    public static String computeCore(String databaseName, String evaluationMethod) throws Exception {
        Instances instances = null;
        try {
            instances = Dataset.getDataset(databaseName);
            instances.setClassIndex(instances.numAttributes() - 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ASEvaluation asEvaluation = null;
        try {
            asEvaluation = (ASEvaluation) Class.forName("weka.attributeSelection." + evaluationMethod).newInstance();
            asEvaluation.buildEvaluator(instances);
            return ((FuzzyRoughSubsetEval) asEvaluation).computeCore().toString();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static int greedyStepwise(String databaseName, String evaluationMethod) throws Exception {
        Instances instances = null;
        try {
            instances = Dataset.getDataset(databaseName);
            instances.setClassIndex(instances.numAttributes() - 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ASEvaluation asEvaluation = null;
        try {
            asEvaluation = (ASEvaluation) Class.forName("weka.attributeSelection." + evaluationMethod).newInstance();
            asEvaluation.buildEvaluator(instances);
            System.out.println(((FuzzyRoughSubsetEval) asEvaluation).computeCore().toString());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        int size = instances.numAttributes();
        int[] searchResult = null;

        GreedyStepwise greedyStepwise = new GreedyStepwise();
        searchResult = greedyStepwise.search(asEvaluation, instances);

        BitSet bitSet = new BitSet(size);

        for (int i = 0; i < searchResult.length; i++) {
            bitSet.set(searchResult[i]);
        }

        for (int attribute : searchResult) System.out.print(attribute + " ");
        System.out.println("- (" + searchResult.length + ") - " + ((SubsetEvaluator) asEvaluation).evaluateSubset(bitSet));

        System.gc();

        return searchResult.length;
    }

    public static void hierarchicalHarmonySearch(
            String databaseName,
            String evaluationMethod,
            int maxIteration,
            boolean iterative) throws Exception {
        Instances instances = null;
        try {
            instances = Dataset.getDataset(databaseName);
            instances.setClassIndex(instances.numAttributes() - 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ASEvaluation asEvaluation = null;
        try {
            asEvaluation = (ASEvaluation) Class.forName("weka.attributeSelection." + evaluationMethod).newInstance();
            asEvaluation.buildEvaluator(instances);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        int size = instances.numAttributes();
        //int[] searchResult = null;
        double merit = 0d;
        int[] searchResult = null;
        //HierarchicalHarmonySearch harmonySearch = new HierarchicalHarmonySearch();
        HierarchicalHarmonySearch harmonySearch = new HierarchicalHarmonySearch();

        if (iterative) {
            while (size > 0) {
                System.out.print(size + "->");
                harmonySearch.setIteration(maxIteration);
                harmonySearch.setNumMusicians(size);
                harmonySearch.search(asEvaluation, instances);

                if ((harmonySearch.getM_bestMerit() >= merit) & (harmonySearch.getSearchResult().length < size)) {
                    size = harmonySearch.getSearchResult().length;
                    merit = harmonySearch.getM_bestMerit();
                    searchResult = harmonySearch.getSearchResult();
                } else if (harmonySearch.getM_bestMerit() >= merit) {
                    merit = harmonySearch.getM_bestMerit();
                    searchResult = harmonySearch.getSearchResult();
                    break;
                } else {
                    break;
                }
            }
        } else {
            harmonySearch.setIteration(maxIteration);
            harmonySearch.setNumMusicians(size);
            harmonySearch.search(asEvaluation, instances);
            merit = harmonySearch.getM_bestMerit();
            searchResult = harmonySearch.getSearchResult();
        }

        BitSet bitSet = new BitSet(instances.numAttributes());

        for (int i = 0; i < searchResult.length; i++) {
            bitSet.set(searchResult[i]);
        }
        System.out.print("\t");
        for (int attribute : searchResult) System.out.print(attribute + " ");
        System.out.println("\t" + searchResult.length + "\t" + merit);

        System.gc();
    }

    public static void iterativeSearch(String databaseName, String evaluationMethod, int maxIteration) throws Exception {
        Instances instances = null;
        try {
            instances = Dataset.getDataset(databaseName);
            instances.setClassIndex(instances.numAttributes() - 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ASEvaluation asEvaluation = null;
        try {
            asEvaluation = (ASEvaluation) Class.forName("weka.attributeSelection." + evaluationMethod).newInstance();
            asEvaluation.buildEvaluator(instances);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        int size = instances.numAttributes();
        //int[] searchResult = null;
        double merit = 0d;
        int[] searchResult = null;
        //HierarchicalHarmonySearch harmonySearch = new HierarchicalHarmonySearch();
        PSOSearch psoSearch = new PSOSearch();
        //GreedyStepwise greedyStepwise = new GreedyStepwise();
        //greedyStepwise.setSearchBackwards(true);
        /*while (size > 0) {
            System.out.println(size + "->");
            harmonySearch.setIteration(maxIteration);
            harmonySearch.setNumMusicians(size);
            harmonySearch.search(asEvaluation, instances);

            if ((harmonySearch.getM_bestMerit() >= merit) & (harmonySearch.getSearchResult().length < size)) {
                size = harmonySearch.getSearchResult().length;
                merit = harmonySearch.getM_bestMerit();
                searchResult = harmonySearch.getSearchResult();
            } else if (harmonySearch.getM_bestMerit() >= merit) {
                merit = harmonySearch.getM_bestMerit();
                searchResult = harmonySearch.getSearchResult();
                break;
            } else {
                break;
            }
        }*/
        searchResult = psoSearch.search(asEvaluation, instances);
        //merit = hillClimber.getM_bestMerit();
        BitSet bitSet = new BitSet(instances.numAttributes());

        for (int i = 0; i < searchResult.length; i++) {
            bitSet.set(searchResult[i]);
        }

        for (int attribute : searchResult) System.out.print(attribute + " ");
        System.out.println("\t" + searchResult.length + "\t" + ((SubsetEvaluator) asEvaluation).evaluateSubset(bitSet));

        System.gc();
    }

    public static void iterativeHarmonySearch(String databaseName, String evaluationMethod, int maxIteration, int musicianSize) throws Exception {
        Instances instances = null;
        try {
            instances = Dataset.getDataset(databaseName);
            instances.setClassIndex(instances.numAttributes() - 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ASEvaluation asEvaluation = null;
        try {
            asEvaluation = (ASEvaluation) Class.forName("weka.attributeSelection." + evaluationMethod).newInstance();
            asEvaluation.buildEvaluator(instances);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        int size = musicianSize;
        //int[] searchResult = null;
        double merit = 0d;
        HarmonySearch harmonySearch = new HarmonySearch();

        while (size > 0) {

            harmonySearch.setIteration(maxIteration);
            harmonySearch.setNumMusicians(size);
            harmonySearch.search(asEvaluation, instances);

            if ((harmonySearch.getM_bestMerit() >= merit) && (harmonySearch.getSearchResult().length < size)) {
                size = harmonySearch.getSearchResult().length;
                merit = harmonySearch.getM_bestMerit();
            } else {
                break;
            }
        }

        BitSet bitSet = new BitSet(instances.numAttributes());

        for (int i = 0; i < harmonySearch.getSearchResult().length; i++) {
            bitSet.set(harmonySearch.getSearchResult()[i]);
        }

        for (int attribute : harmonySearch.getSearchResult()) System.out.print(attribute + " ");
        System.out.println("- (" + harmonySearch.getSearchResult().length + ") - " + ((SubsetEvaluator) asEvaluation).evaluateSubset(bitSet));

        System.gc();
    }

    public static void harmonySearch(String databaseName, String evaluationMethod, int maxIteration, int ensembleSize) throws Exception {

        Instances instances = null;
        try {
            instances = Dataset.getDataset(databaseName);
            instances.setClassIndex(instances.numAttributes() - 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ASEvaluation asEvaluation = null;
        try {
            asEvaluation = (ASEvaluation) Class.forName("weka.attributeSelection." + evaluationMethod).newInstance();
            asEvaluation.buildEvaluator(instances);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        HarmonySearch harmonySearch = new HarmonySearch();
        harmonySearch.setIteration(maxIteration);
        harmonySearch.setNumMusicians(ensembleSize);

        harmonySearch.search(asEvaluation, instances);
    }

    public static ArrayList<Classifier[]> buildBaseClassifiers(
            Classifier baseClassifier,
            int size,
            int poolMethod,
            Instances[] trainingTestingFolds
    ) throws Exception {
        ArrayList<Classifier[]> baseClassifiers = new ArrayList<Classifier[]>();

        FeatureSelectionEnsemble classifier = new FeatureSelectionEnsemble();
        for (int i = 0; i < trainingTestingFolds.length / 2; i++) {
            baseClassifiers.add(classifier.generateBaseClassifiers(baseClassifier, size, poolMethod, trainingTestingFolds[i]));
        }
        return baseClassifiers;
    }

    public static Classifier[] buildFeatureSelectionEnsembles(
            Classifier baseClassifier,
            int size,
            int poolMethod,
            Instances[] trainingTestingFolds) throws Exception {

        Classifier[] prebuiltClassifiers = new Classifier[trainingTestingFolds.length / 2];

        for (int i = 0; i < trainingTestingFolds.length / 2; i++) {
            FeatureSelectionEnsemble featureSelectionEnsemble = new FeatureSelectionEnsemble();
            featureSelectionEnsemble.setPool(
                    featureSelectionEnsemble.generateBaseClassifiers(baseClassifier, size, poolMethod, trainingTestingFolds[i]));
            featureSelectionEnsemble.buildDecisionMatrix(trainingTestingFolds[i]);
            prebuiltClassifiers[i] = featureSelectionEnsemble;
        }
        System.gc();
        return prebuiltClassifiers;
    }

    /*public static void testIterativeHarmonySearch(
            String databaseName,
            String evaluationMethod,
            int maxIteration,
            boolean iterative,
            int parameterMode,
            int repeat) throws Exception {
        System.out.println("Current Database: " + databaseName);
        System.out.println("Full Accuracy = " + fullAccuracy(databaseName, evaluationMethod, maxIteration, iterative));
        double accuracy = 0;
        for (int i = 0; i < repeat; i++) {
            accuracy += iterativeHarmonySearch(databaseName, evaluationMethod, maxIteration, iterative, parameterMode);
        }
        System.out.println("Average Accuracy = " + accuracy / repeat + "");
    }*/

    public static void testHierarchicalHarmonySearch(
            String databaseName,
            String evaluationMethod,
            int maxIteration,
            boolean iterative,
            int repeat) throws Exception {
        System.out.println("Current Database: " + databaseName);
        for (int i = 0; i < repeat; i++) {
            hierarchicalHarmonySearch(databaseName, evaluationMethod, maxIteration, iterative);
        }
    }

    public static void testSearch(
            String databaseName,
            String evaluationMethod,
            int maxIteration,
            boolean iterative,
            int repeat) throws Exception {
        System.out.println("Current Database: " + databaseName);
        for (int i = 0; i < repeat; i++) {
            iterativeSearch(databaseName, evaluationMethod, maxIteration);
        }
    }

    /*public static void testIterativeSearch(String databaseName, String evaluationMethod, int maxIteration) throws Exception {
        System.out.println("Current Database: " + databaseName);
        //fullSearch(databaseName);
        //computeCore(databaseName, evaluationMethod);
        //int musicianSize = greedyStepwise(databaseName, evaluationMethod);
        iterativeSearch(databaseName, evaluationMethod, maxIteration);
        iterativeSearch(databaseName, evaluationMethod, maxIteration);
        iterativeSearch(databaseName, evaluationMethod, maxIteration);
        iterativeSearch(databaseName, evaluationMethod, maxIteration);
        iterativeSearch(databaseName, evaluationMethod, maxIteration);
    }*/

    public static void fullSearch(String databaseName) {
        Instances instances = null;
        try {
            instances = Dataset.getDataset(databaseName);
            instances.setClassIndex(instances.numAttributes() - 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FuzzyRoughSubsetEval fuzzyRoughSubsetEval = new FuzzyRoughSubsetEval();
        try {
            fuzzyRoughSubsetEval.buildEvaluator(instances);
        } catch (Exception e) {
            e.printStackTrace();  //TODO: Automatically Generated Catch Statement
        }

        try {
            (new FullReductSearch()).search(fuzzyRoughSubsetEval, instances);
        } catch (Exception e) {
            e.printStackTrace();  //TODO: Automatically Generated Catch Statement
        }
    }

    public static void testHierarchicalHarmonySearch(String evaluationMethod, int maxIteration, boolean iterative, int repeat) throws Exception {
        for (int i = 11; i < 12; i++) {
            testHierarchicalHarmonySearch(selectDatabase(i), evaluationMethod, maxIteration, iterative, repeat);
        }
    }

    public static void testSearch(String evaluationMethod, int maxIteration, boolean iterative, int repeat) throws Exception {
        for (int i = 9; i < 11; i++) {
            testSearch(selectDatabase(i), evaluationMethod, maxIteration, iterative, repeat);
        }
    }

    public static String selectDatabase(int index) {
        switch (index) {
            case 0:
                return "cleveland";
            case 1:
                return "ecoli";
            case 2:
                return "glassScaled";
            case 3:
                return "heart";
            case 4:
                return "ionosphere";
            case 5:
                return "sonar";
            case 6:
                return "2-completed";
            case 7:
                return "3-completed";
            case 8:
                return "wineScaled";
            case 9:
                return "olitos";
            case 10:
                return "arrhythmia";
            case 13:
                return "web";
            case 14:
                return "waveform-+noise";
            case 15:
                return "multifeat";
            case 16:
                return "wisconsin";
            default:
                return "";

        }
    }

    /*public static void testIterativeSearch(String evaluationMethod, int maxIteration) throws Exception {
        *//*crossValidateFeatureSelectionEnsemble("2", baseC);
       crossValidateFeatureSelectionEnsemble("3", baseC);*//*
        String currentDatabase;
        *//*currentDatabase = "wisconsin";
      testIterativeHarmonySearch(currentDatabase, evaluationMethod, maxIteration);*//*
        currentDatabase = "cleveland";
        testIterativeSearch(currentDatabase, evaluationMethod, maxIteration);
        currentDatabase = "ecoli";
        testIterativeSearch(currentDatabase, evaluationMethod, maxIteration);
        currentDatabase = "glassScaled";
        testIterativeSearch(currentDatabase, evaluationMethod, maxIteration);
        currentDatabase = "heart";
        testIterativeSearch(currentDatabase, evaluationMethod, maxIteration);
        currentDatabase = "ionosphere";
        testIterativeSearch(currentDatabase, evaluationMethod, maxIteration);
        currentDatabase = "sonar";
        testIterativeSearch(currentDatabase, evaluationMethod, maxIteration);
        currentDatabase = "2";
        testIterativeSearch(currentDatabase, evaluationMethod, maxIteration);
        currentDatabase = "3";
        testIterativeSearch(currentDatabase, evaluationMethod, maxIteration);
        *//*currentDatabase = "waveform-+noise";
      testIterativeSearch(currentDatabase, evaluationMethod, maxIteration);*//*
        currentDatabase = "wineScaled";
        testIterativeSearch(currentDatabase, evaluationMethod, maxIteration);
    }*/

    public static void testFeatureSelectionEnsemblePairedTTest(Classifier baseC, int poolMethod, boolean isSupervised) throws Exception {
        /*crossValidateFeatureSelectionEnsemble("2", baseC);
        crossValidateFeatureSelectionEnsemble("3", baseC);*/
        //crossValidateFeatureSelectionEnsemble("waveform-+noise", baseC);
        crossValidateFeatureSelectionEnsemblePairedTTest("cleveland", baseC, poolMethod, isSupervised);
        crossValidateFeatureSelectionEnsemblePairedTTest("ecoli", baseC, poolMethod, isSupervised);
        crossValidateFeatureSelectionEnsemblePairedTTest("glassScaled", baseC, poolMethod, isSupervised);
        crossValidateFeatureSelectionEnsemblePairedTTest("heart", baseC, poolMethod, isSupervised);
        crossValidateFeatureSelectionEnsemblePairedTTest("ionosphere", baseC, poolMethod, isSupervised);
        crossValidateFeatureSelectionEnsemblePairedTTest("sonar", baseC, poolMethod, isSupervised);
        crossValidateFeatureSelectionEnsemblePairedTTest("3-completed", baseC, poolMethod, isSupervised);
        crossValidateFeatureSelectionEnsemblePairedTTest("2-completed", baseC, poolMethod, isSupervised);
        crossValidateFeatureSelectionEnsemblePairedTTest("wineScaled", baseC, poolMethod, isSupervised);
        //crossValidateFeatureSelectionEnsemble("2", baseC);
    }

    public static void testFeatureSelectionEnsemble(Classifier baseC) throws Exception {
        /*crossValidateFeatureSelectionEnsemble("2", baseC);
        crossValidateFeatureSelectionEnsemble("3", baseC);*/
        //crossValidateFeatureSelectionEnsemble("waveform-+noise", baseC);
        //crossValidateFeatureSelectionEnsemble("cleveland", baseC);
        crossValidateFeatureSelectionEnsemble("COPD area data2", baseC);
        //crossValidateFeatureSelectionEnsemble("glassScaled", baseC);
        /*crossValidateFeatureSelectionEnsemble("heart", baseC);
        crossValidateFeatureSelectionEnsemble("ionosphere", baseC);
        crossValidateFeatureSelectionEnsemble("sonar", baseC);
        crossValidateFeatureSelectionEnsemble("3-completed", baseC);
        crossValidateFeatureSelectionEnsemble("2-completed", baseC);
        crossValidateFeatureSelectionEnsemble("wineScaled", baseC);*/
        //crossValidateFeatureSelectionEnsemble("2", baseC);
    }

    public static void testFeatureSelectionEnsembleMixed() throws Exception {
        /*crossValidateFeatureSelectionEnsembleMixedBagging("sonar");
        crossValidateFeatureSelectionEnsembleMixedBagging("wineScaled");
        crossValidateFeatureSelectionEnsembleMixedBagging("heart");
        //crossValidateFeatureSelectionEnsembleMixedBagging("web");
        //crossValidateFeatureSelectionEnsembleMixedBagging("arrhythmia");
        crossValidateFeatureSelectionEnsembleMixedBagging("ecoli");
        crossValidateFeatureSelectionEnsembleMixedBagging("glassScaled");
        crossValidateFeatureSelectionEnsembleMixedBagging("3-completed");
        crossValidateFeatureSelectionEnsembleMixedBagging("2-completed");
        //crossValidateFeatureSelectionEnsembleMixedBagging("waveform-+noise");
        crossValidateFeatureSelectionEnsembleMixedBagging("ionosphere");
        crossValidateFeatureSelectionEnsembleMixedBagging("cleveland");*/

        crossValidateFeatureSelectionEnsembleMixed("sonar");
        crossValidateFeatureSelectionEnsembleMixed("wineScaled");
        crossValidateFeatureSelectionEnsembleMixed("heart");
        //crossValidateFeatureSelectionEnsembleMixed("web");
        //crossValidateFeatureSelectionEnsembleMixed("arrhythmia");
        crossValidateFeatureSelectionEnsembleMixed("ecoli");
        crossValidateFeatureSelectionEnsembleMixed("glassScaled");
        crossValidateFeatureSelectionEnsembleMixed("3-completed");
        crossValidateFeatureSelectionEnsembleMixed("2-completed");
        //crossValidateFeatureSelectionEnsembleMixed("waveform-+noise");
        crossValidateFeatureSelectionEnsembleMixed("ionosphere");
        crossValidateFeatureSelectionEnsembleMixed("cleveland");
    }

    /*public static void crossValidateFeatureSelectionEnsemble(String dataset, Classifier baseC) throws Exception {
        System.out.println("\nEvaluating Dataset - " + dataset);
        Instances data;
        Classifier classifier;
        double result;

        data = Dataset.getDataset(dataset);
        data.setClassIndex(data.numAttributes() - 1);

        classifier = (Classifier) Class.forName(baseC.getClass().getName()).newInstance();
        result = CV.crossValidate(10, classifier, data);
        System.out.println("Base\t" + classifier.getClass().getName() + "\t\t" + result);

        *//*classifier = new Bagging();
        ((Bagging) classifier).setClassifier(new FuzzyRoughNN());
        //((Bagging) classifier).setOptions();
        result = CV.crossValidate(10, classifier, data);
        System.out.println("Bagging " + result);*//*

        classifier = new FeatureSelectionEnsemble();
        ((FeatureSelectionEnsemble) classifier).setBaseClassifier(baseC);
        ((FeatureSelectionEnsemble) classifier).setSelectionMethod(121354);
        result = CV.crossValidate(10, classifier, data);
        System.out.println("FeatureSelection\tRandom\tBagging\t" + result);

        //FeatureSelection + 999 + Partition
        classifier = new FeatureSelectionEnsemble();
        ((FeatureSelectionEnsemble) classifier).setBaseClassifier(baseC);
        ((FeatureSelectionEnsemble) classifier).setSelectionMethod(999);
        result = CV.crossValidate(10, classifier, data);
        System.out.println("FeatureSelection\t999\tBagging\t" + result);

        //FeatureSelection + 111 + Partition
        classifier = new FeatureSelectionEnsemble();
        ((FeatureSelectionEnsemble) classifier).setBaseClassifier(baseC);
        result = CV.crossValidate(10, classifier, data);
        System.out.println("FeatureSelection\t111\tBagging\t" + result);

        //FeatureSelection + 5 + Partition
        classifier = new FeatureSelectionEnsemble();
        ((FeatureSelectionEnsemble) classifier).setBaseClassifier(baseC);
        ((FeatureSelectionEnsemble) classifier).setSelectionMethod(5);
        result = CV.crossValidate(10, classifier, data);
        System.out.println("FeatureSelection\t5\tBagging\t" + result);

        classifier = new FeatureSelectionEnsemble();
        ((FeatureSelectionEnsemble) classifier).setBaseClassifier(baseC);
        ((FeatureSelectionEnsemble) classifier).setSelectionMethod(121354);
        ((FeatureSelectionEnsemble) classifier).setPoolMethod(1);
        result = CV.crossValidate(10, classifier, data);
        System.out.println("FeatureSelection\tRandom\tPartition\t" + result);

        //FeatureSelection + 999 + Bagging
        classifier = new FeatureSelectionEnsemble();
        ((FeatureSelectionEnsemble) classifier).setBaseClassifier(baseC);
        ((FeatureSelectionEnsemble) classifier).setSelectionMethod(999);
        ((FeatureSelectionEnsemble) classifier).setPoolMethod(1);
        result = CV.crossValidate(10, classifier, data);
        System.out.println("FeatureSelection\t999\tPartition\t" + result);

        //FeatureSelection + 111 + Bagging
        classifier = new FeatureSelectionEnsemble();
        ((FeatureSelectionEnsemble) classifier).setBaseClassifier(baseC);
        ((FeatureSelectionEnsemble) classifier).setPoolMethod(1);
        result = CV.crossValidate(10, classifier, data);
        System.out.println("FeatureSelection\t111\tPartition\t" + result);

        //FeatureSelection + 5 + Bagging
        classifier = new FeatureSelectionEnsemble();
        ((FeatureSelectionEnsemble) classifier).setBaseClassifier(baseC);
        ((FeatureSelectionEnsemble) classifier).setSelectionMethod(5);
        ((FeatureSelectionEnsemble) classifier).setPoolMethod(1);
        result = CV.crossValidate(10, classifier, data);
        System.out.println("FeatureSelection\t5\tPartition\t" + result);
    }*/

    public static void crossValidateFeatureSelectionEnsemblePairedTTest(String dataset, Classifier baseC, int poolMethod, boolean isSupervised) throws Exception {
        System.out.println("\nEvaluating Dataset - " + dataset);
        int numFolds = 10;

        Instances data;
        data = Dataset.getDataset(dataset);
        data.setClassIndex(data.numAttributes() - 1);

        double result;
        double baseResult;
        Instances[] trainingTestingFolds = CV.generateCrossValidationFolds(numFolds, data);

        Classifier classifier;
        classifier = (Classifier) Class.forName(baseC.getClass().getName()).newInstance();
        baseResult = CV.crossValidate(classifier, trainingTestingFolds);
        System.out.println("Base\t" + classifier.getClass().getName());

        Classifier[] prebuiltClassifiers;
        //int poolMethod;
        int size = 50;

        //poolMethod = 0; //Bagging;
        prebuiltClassifiers = buildFeatureSelectionEnsembles(baseC, size, poolMethod, trainingTestingFolds);

        System.out.println("FeatureSelection\t" + (isSupervised ? "111" : "5") + "\t" + (poolMethod == 0 ? "Bagging" : "Partition"));
        for (int i = 0; i < 10; i++) {
            result = CV.crossValidateFeatureSelectionEnsemble(prebuiltClassifiers, 999, trainingTestingFolds);
            System.out.print(result);
            result = CV.crossValidateFeatureSelectionEnsemble(prebuiltClassifiers, isSupervised ? 111 : 5, trainingTestingFolds);
            System.out.println("\t" + result);
        }

        /*System.out.println("FeatureSelection\t5\tBagging");
        for (int i = 0; i < 50; i++) {
            result = CV.crossValidateFeatureSelectionEnsemble(prebuiltClassifiers, 5, trainingTestingFolds);
            System.out.println(baseResult + "\t" + result);
        }*/

        //poolMethod = 1;
        /*prebuiltClassifiers = buildFeatureSelectionEnsembles(baseC, size, poolMethod, trainingTestingFolds);

        System.out.println("FeatureSelection\t111\tPartition");
        for (int i = 0; i < 50; i++) {
            result = CV.crossValidateFeatureSelectionEnsemble(prebuiltClassifiers, 111, trainingTestingFolds);
            System.out.println(baseResult + "\t" + result);
        }

        System.out.println("FeatureSelection\t5\tPartition");
        for (int i = 0; i < 50; i++) {
            result = CV.crossValidateFeatureSelectionEnsemble(prebuiltClassifiers, 5, trainingTestingFolds);
            System.out.println(baseResult + "\t" + result);
        }*/

        System.gc();
    }

    public static void crossValidateFeatureSelectionEnsemble(String dataset, Classifier baseC) throws Exception {
        System.out.println("\nEvaluating Dataset - " + dataset);
        int numFolds = 10;

        Instances data;
        data = Dataset.getDataset(dataset);
        data.setClassIndex(data.numAttributes() - 1);

        double result;
        Instances[] trainingTestingFolds = CV.generateCrossValidationFolds(numFolds, data);

        Classifier classifier;
        classifier = (Classifier) Class.forName(baseC.getClass().getName()).newInstance();
        result = CV.crossValidate(classifier, trainingTestingFolds);
        System.out.println("Base\t" + classifier.getClass().getName() + "\t\t" + result);

        Classifier[] prebuiltClassifiers;
        int poolMethod;
        int size = 50;

        System.out.println("Bagging\tFull\tRandom\tFRFS\tU-FRFS");

        //for (int i = 0; i < 10; i++) {
        poolMethod = 0; //Bagging;
        prebuiltClassifiers = buildFeatureSelectionEnsembles(baseC, size, poolMethod, trainingTestingFolds);

        result = CV.crossValidateFeatureSelectionEnsemble(prebuiltClassifiers, 999, trainingTestingFolds);
        System.out.print("\t" + result);

        result = CV.crossValidateFeatureSelectionEnsemble(prebuiltClassifiers, 121354, trainingTestingFolds);
        System.out.print("\t" + result);

        //result = CV.crossValidateFeatureSelectionEnsemble(prebuiltClassifiers, 111, trainingTestingFolds);
        //System.out.println("\t" + result);

        result = CV.crossValidateFeatureSelectionEnsemble(prebuiltClassifiers, 3, trainingTestingFolds);
        System.out.print("\t" + result);

        result = CV.crossValidateFeatureSelectionEnsemble(prebuiltClassifiers, 4, trainingTestingFolds);
        System.out.print("\t" + result);

        /*result = CV.crossValidateFeatureSelectionEnsemble(prebuiltClassifiers, 5, trainingTestingFolds);
    System.out.println("\t" + result + "\n");*/
        //}

        System.out.println("Partition\tFull\tRandom\tFRFS\tU-FRFS");

        //for (int i = 0; i < 10; i++) {
        poolMethod = 1;
        prebuiltClassifiers = buildFeatureSelectionEnsembles(baseC, size, poolMethod, trainingTestingFolds);

        result = CV.crossValidateFeatureSelectionEnsemble(prebuiltClassifiers, 999, trainingTestingFolds);
        System.out.print("\t" + result);

        result = CV.crossValidateFeatureSelectionEnsemble(prebuiltClassifiers, 121354, trainingTestingFolds);
        System.out.print("\t" + result);

        result = CV.crossValidateFeatureSelectionEnsemble(prebuiltClassifiers, 3, trainingTestingFolds);
        System.out.print("\t" + result);

        result = CV.crossValidateFeatureSelectionEnsemble(prebuiltClassifiers, 4, trainingTestingFolds);
        System.out.print("\t" + result);

        /*result = CV.crossValidateFeatureSelectionEnsemble(prebuiltClassifiers, 111, trainingTestingFolds);
    System.out.println("\t" + result);

    result = CV.crossValidateFeatureSelectionEnsemble(prebuiltClassifiers, 5, trainingTestingFolds);
    System.out.println("\t" + result + "\n");*/
        //}

        /*poolMethod = 0; //Bagging;
        prebuiltClassifiers = buildFeatureSelectionEnsembles(baseC, size, poolMethod, trainingTestingFolds);

        result = CV.crossValidateFeatureSelectionEnsemble(prebuiltClassifiers, 999, trainingTestingFolds);
        System.out.println("FeatureSelection\t999\tBagging\t" + result);

        result = CV.crossValidateFeatureSelectionEnsemble(prebuiltClassifiers, 121354, trainingTestingFolds);
        System.out.println("FeatureSelection\tRandom\tBagging\t" + result);

        result = CV.crossValidateFeatureSelectionEnsemble(prebuiltClassifiers, 111, trainingTestingFolds);
        System.out.println("FeatureSelection\t111\tBagging\t" + result);

        result = CV.crossValidateFeatureSelectionEnsemble(prebuiltClassifiers, 5, trainingTestingFolds);
        System.out.println("FeatureSelection\t5\tBagging\t" + result);

        poolMethod = 1;
        prebuiltClassifiers = buildFeatureSelectionEnsembles(baseC, size, poolMethod, trainingTestingFolds);

        result = CV.crossValidateFeatureSelectionEnsemble(prebuiltClassifiers, 999, trainingTestingFolds);
        System.out.println("FeatureSelection\t999\tPartition\t" + result);

        result = CV.crossValidateFeatureSelectionEnsemble(prebuiltClassifiers, 121354, trainingTestingFolds);
        System.out.println("FeatureSelection\tRandom\tPartition\t" + result);

        result = CV.crossValidateFeatureSelectionEnsemble(prebuiltClassifiers, 111, trainingTestingFolds);
        System.out.println("FeatureSelection\t111\tPartition\t" + result);

        result = CV.crossValidateFeatureSelectionEnsemble(prebuiltClassifiers, 5, trainingTestingFolds);
        System.out.println("FeatureSelection\t5\tPartition\t" + result);*/

        System.gc();
    }

    public static void crossValidateFeatureSelectionEnsembleMixed(String dataset) throws Exception {
        System.out.println("\nEvaluating Dataset - " + dataset);
        int numFolds = 10;

        Instances data;
        data = Dataset.getDataset(dataset);
        data.setClassIndex(data.numAttributes() - 1);

        double result;
        Instances[] trainingTestingFolds = CV.generateCrossValidationFolds(numFolds, data);
        Classifier[] prebuiltClassifiers;
        int poolMethod;
        int size = 50;

        poolMethod = 4; //BaggingMixed;          
        prebuiltClassifiers = buildFeatureSelectionEnsembles(null, size, poolMethod, trainingTestingFolds);

        result = CV.crossValidateFeatureSelectionEnsemble(prebuiltClassifiers, 999, trainingTestingFolds);
        System.out.println("FeatureSelection\t999\tMixed Bagging\t" + result);

        result = CV.crossValidateFeatureSelectionEnsemble(prebuiltClassifiers, 121354, trainingTestingFolds);
        System.out.println("FeatureSelection\tRandom\tMixed Bagging\t" + result);

        result = CV.crossValidateFeatureSelectionEnsemble(prebuiltClassifiers, 111, trainingTestingFolds);
        System.out.println("FeatureSelection\t111\tMixed Bagging\t" + result);

        result = CV.crossValidateFeatureSelectionEnsemble(prebuiltClassifiers, 5, trainingTestingFolds);
        System.out.println("FeatureSelection\t5\tMixed Bagging\t" + result);

        poolMethod = 3; //RandomSubspacingMixed;
        prebuiltClassifiers = buildFeatureSelectionEnsembles(null, size, poolMethod, trainingTestingFolds);

        result = CV.crossValidateFeatureSelectionEnsemble(prebuiltClassifiers, 999, trainingTestingFolds);
        System.out.println("FeatureSelection\t999\tMixed Partition\t" + result);

        result = CV.crossValidateFeatureSelectionEnsemble(prebuiltClassifiers, 121354, trainingTestingFolds);
        System.out.println("FeatureSelection\tRandom\tMixed Partition\t" + result);

        result = CV.crossValidateFeatureSelectionEnsemble(prebuiltClassifiers, 111, trainingTestingFolds);
        System.out.println("FeatureSelection\t111\tMixed Partition\t" + result);

        result = CV.crossValidateFeatureSelectionEnsemble(prebuiltClassifiers, 5, trainingTestingFolds);
        System.out.println("FeatureSelection\t5\tMixed Partition\t" + result);

        System.gc();
    }

    /*public static void crossValidateFeatureSelectionEnsembleMixedPartition(String dataset) throws Exception {
        System.out.println("\nEvaluating Dataset - " + dataset);
        Instances data;
        Classifier classifier;
        double result;

        data = Dataset.getDataset(dataset);
        data.setClassIndex(data.numAttributes() - 1);

        //FeatureSelection + 999 + Bagging
        classifier = new FeatureSelectionEnsemble();
        ((FeatureSelectionEnsemble) classifier).setPoolMethod(3);
        ((FeatureSelectionEnsemble) classifier).setSelectionMethod(999);
        ((FeatureSelectionEnsemble) classifier).setPoolMethod(1);
        result = CV.crossValidate(10, classifier, data);
        System.out.println("FeatureSelection\t999\tMixed Partition\t" + result);

        //FeatureSelection + 111 + Bagging
        classifier = new FeatureSelectionEnsemble();
        ((FeatureSelectionEnsemble) classifier).setPoolMethod(3);
        result = CV.crossValidate(10, classifier, data);
        System.out.println("FeatureSelection\t111\tMixed Partition\t" + result);

        //FeatureSelection + 5 + Bagging
        classifier = new FeatureSelectionEnsemble();
        ((FeatureSelectionEnsemble) classifier).setPoolMethod(3);
        ((FeatureSelectionEnsemble) classifier).setSelectionMethod(5);
        result = CV.crossValidate(10, classifier, data);
        System.out.println("FeatureSelection\t5\tMixed Partition\t" + result);
    }

    public static void crossValidateFeatureSelectionEnsembleMixedBagging(String dataset) throws Exception {
        System.out.println("\nEvaluating Dataset - " + dataset);
        Instances data;
        Classifier classifier;
        double result;

        data = Dataset.getDataset(dataset);
        data.setClassIndex(data.numAttributes() - 1);

        //FeatureSelection + 999 + Partition
        classifier = new FeatureSelectionEnsemble();
        ((FeatureSelectionEnsemble) classifier).setPoolMethod(4);
        //((FeatureSelectionEnsemble) classifier).setBaseC(baseC);
        ((FeatureSelectionEnsemble) classifier).setSelectionMethod(999);
        result = CV.crossValidate(10, classifier, data);
        System.out.println("FeatureSelection\t999\tMixed Bagging\t" + result);

        //FeatureSelection + 111 + Partition
        classifier = new FeatureSelectionEnsemble();
        ((FeatureSelectionEnsemble) classifier).setPoolMethod(4);
        result = CV.crossValidate(10, classifier, data);
        System.out.println("FeatureSelection\t111\tMixed Bagging\t" + result);

        //FeatureSelection + 5 + Partition
        classifier = new FeatureSelectionEnsemble();
        ((FeatureSelectionEnsemble) classifier).setPoolMethod(4);
        ((FeatureSelectionEnsemble) classifier).setSelectionMethod(5);
        result = CV.crossValidate(10, classifier, data);
        System.out.println("FeatureSelection\t5\tMixed Bagging\t" + result);
    }*/

    private static void beam(originalharmonysearch.core.ValueRange[] parameterRanges, int cap, int maxIteration) throws Exception {
        originalharmonysearch.core.ValueRange[] beamValueRanges = new originalharmonysearch.core.ValueRange[4];
        beamValueRanges[0] = new originalharmonysearch.core.ValueRange(0.125, 5, true);
        beamValueRanges[1] = new originalharmonysearch.core.ValueRange(0.1, 10, true);
        beamValueRanges[2] = new originalharmonysearch.core.ValueRange(0.1, 10, true);
        beamValueRanges[3] = new originalharmonysearch.core.ValueRange(0.1, 5, true);
        BeamHarmonyMemory beamHarmonyMemory =
                new BeamHarmonyMemory(
                        parameterRanges,
                        beamValueRanges);
        iterativeTest(beamHarmonyMemory, cap, maxIteration);
    }

    /*private static void beam3(int cap, int maxIteration) throws Exception {
        ValueRange[] beamValueRanges = new ValueRange[4];
        beamValueRanges[0] = new ValueRange(0.125, 5, true);
        beamValueRanges[1] = new ValueRange(0.1, 10, true);
        beamValueRanges[2] = new ValueRange(0.1, 10, true);
        beamValueRanges[3] = new ValueRange(0.1, 5, true);
        BeamHarmonyMemory beamHarmonyMemory =
                new BeamHarmonyMemory(
                        HarmonyMemory.createParameterRanges(20, 20, 0.8, 0.8, 0.85, 0.85, 0.001, 0.001),
                        beamValueRanges);
        iterativeTest(beamHarmonyMemory, cap, maxIteration);
    }

    private static void beam2(int cap, int maxIteration) throws Exception {
        ValueRange[] beamValueRanges = new ValueRange[4];
        beamValueRanges[0] = new ValueRange(0.125, 5, true);
        beamValueRanges[1] = new ValueRange(0.1, 10, true);
        beamValueRanges[2] = new ValueRange(0.1, 10, true);
        beamValueRanges[3] = new ValueRange(0.1, 5, true);
        BeamHarmonyMemory beamHarmonyMemory =
                new BeamHarmonyMemory(
                        HarmonyMemory.createParameterRanges(20, 0.5),
                        beamValueRanges);
        iterativeTest(beamHarmonyMemory, cap, maxIteration);
    }*/

    private static void vessel(originalharmonysearch.core.ValueRange[] parameterRanges, int cap, int maxIteration) throws Exception {
        originalharmonysearch.core.ValueRange[] vesselValueRanges = new originalharmonysearch.core.ValueRange[4];
        vesselValueRanges[0] = new originalharmonysearch.core.ValueRange(0, 50, false);
        vesselValueRanges[1] = new originalharmonysearch.core.ValueRange(0, 50, false);
        vesselValueRanges[2] = new originalharmonysearch.core.ValueRange(0, 200, true);
        vesselValueRanges[3] = new originalharmonysearch.core.ValueRange(0, 200, true);
        VesselHarmonyMemory vesselHarmonyMemory = new VesselHarmonyMemory(parameterRanges, vesselValueRanges);
        iterativeTest(vesselHarmonyMemory, cap, maxIteration);
    }

    private static void constraintFour(originalharmonysearch.core.ValueRange[] parameterRanges, int cap, int maxIteration) throws Exception {
        originalharmonysearch.core.ValueRange[] constraintFourValueRanges = new originalharmonysearch.core.ValueRange[3];
        constraintFourValueRanges[0] = new originalharmonysearch.core.ValueRange(0, 10, true);
        constraintFourValueRanges[1] = new originalharmonysearch.core.ValueRange(0, 10, true);
        constraintFourValueRanges[2] = new originalharmonysearch.core.ValueRange(0, 10, true);

        ConstraintFourHarmonyMemory constraintFourHarmonyMemory = new ConstraintFourHarmonyMemory(parameterRanges, constraintFourValueRanges);

        iterativeTest(constraintFourHarmonyMemory, cap, maxIteration);
    }

    private static void spring(originalharmonysearch.core.ValueRange[] parameterRanges, int cap, int maxIteration) throws Exception {

        originalharmonysearch.core.ValueRange[] springValueRanges = new originalharmonysearch.core.ValueRange[3];
        springValueRanges[0] = new originalharmonysearch.core.ValueRange(0, 0.1, true);
        springValueRanges[1] = new originalharmonysearch.core.ValueRange(0, 0.5, true);
        springValueRanges[2] = new originalharmonysearch.core.ValueRange(8, 15, true);

        SpringHarmonyMemory springHarmonyMemory1 = new SpringHarmonyMemory(parameterRanges, springValueRanges);

        iterativeTest(springHarmonyMemory1, cap, maxIteration);
    }

    /*private static void iterativeResetableTest(HarmonyMemory harmonyMemory, int cap, int maxIteration) throws Exception {
            //int cap = 10;
            double[] capSpread = new double[cap];
            double[] capOriginal = new double[cap];
            double[] capInfluence = new double[cap];

            for (int i = 0; i < cap; i++) {
                //System.out.println("No Spread " + i);
                harmonyMemory.setSpread(false);
                harmonyMemory.reset();
                harmonyMemory.fill();
                harmonyMemory.iterate(maxIteration);
                //harmonyMemory.printHarmonies();
                //System.out.println();
                capOriginal[i] = harmonyMemory.best().getMerits()[0];
            }

            for (int i = 0; i < cap; i++) {
                //System.out.println("Spread " + i);
                harmonyMemory.setSpread(true);
                harmonyMemory.reset();
                harmonyMemory.fill();
                harmonyMemory.iterate(maxIteration);
                //harmonyMemory.printHarmonies();
                //System.out.println();
                capSpread[i] = harmonyMemory.best().getMerits()[0];
            }

            for (int i = 0; i < cap; i++) {
                //System.out.println("Spread " + i);
                harmonyMemory.setSpread(false);
                harmonyMemory.setInfluence(true);
                harmonyMemory.reset();
                harmonyMemory.fill();
                harmonyMemory.iterate(maxIteration);
                //harmonyMemory.printHarmonies();
                //System.out.println();
                capInfluence[i] = harmonyMemory.best().getMerits()[0];
            }

            int countSpread = 0;
            int countInfluence = 0;
            DescriptiveStatistics descriptiveStatisticsOriginal = new DescriptiveStatistics();
            DescriptiveStatistics descriptiveStatisticsSpread = new DescriptiveStatistics();
            DescriptiveStatistics descriptiveStatisticsInfluence = new DescriptiveStatistics();
            for (int i = 0; i < cap; i++) {
                //System.out.println(capSpread[i] + " " + capNoSpread[i]);

                descriptiveStatisticsOriginal.addValue(capOriginal[i]);
                descriptiveStatisticsSpread.addValue(capSpread[i]);
                descriptiveStatisticsInfluence.addValue(capInfluence[i]);

                if (capSpread[i] > capOriginal[i]) countSpread++;
                if (capInfluence[i] > capOriginal[i]) countInfluence++;
            }
            System.out.println("Count Spread " + countSpread);
            System.out.println("Count Influence " + countInfluence);
            System.out.println("SD\t" + descriptiveStatisticsOriginal.getStandardDeviation() + "\t" + descriptiveStatisticsSpread.getStandardDeviation() + "\t" + descriptiveStatisticsInfluence.getStandardDeviation());
            System.out.println("Max\t" + descriptiveStatisticsOriginal.getMax() + "\t" + descriptiveStatisticsSpread.getMax() + "\t" + descriptiveStatisticsInfluence.getMax());
            System.out.println("Mean\t" + descriptiveStatisticsOriginal.getMean() + "\t" + descriptiveStatisticsSpread.getMean() + "\t" + descriptiveStatisticsInfluence.getMean());

            TTestImpl tTest = new TTestImpl();
            System.out.println("T-Spread " + tTest.tTest(capSpread, capOriginal));
            System.out.println("T-Influence " + tTest.tTest(capInfluence, capOriginal));
        }*/

    private static void iterativeTest(originalharmonysearch.core.HarmonyMemory harmonyMemory, int cap, int maxIteration) throws Exception {
        //int cap = 10;
        double[] capSpread = new double[cap];
        double[] capOriginal = new double[cap];
        double[] capInfluence = new double[cap];

        for (int i = 0; i < cap; i++) {
            //System.out.println("Spread " + i);
            harmonyMemory.setSpread(true);
            harmonyMemory.reset();
            harmonyMemory.fill();
            harmonyMemory.iterate(maxIteration);
            //harmonyMemory.printHarmonies();
            //System.out.println();
            capSpread[i] = harmonyMemory.best().getMerits()[0];
        }

        for (int i = 0; i < cap; i++) {
            //System.out.println("No Spread " + i);
            harmonyMemory.setSpread(false);
            harmonyMemory.reset();
            harmonyMemory.fill();
            harmonyMemory.iterate(maxIteration);
            //harmonyMemory.printHarmonies();
            //System.out.println();
            capOriginal[i] = harmonyMemory.best().getMerits()[0];
        }

        for (int i = 0; i < cap; i++) {
            //System.out.println("Spread " + i);
            harmonyMemory.setSpread(false);
            harmonyMemory.setInfluence(true);
            harmonyMemory.reset();
            harmonyMemory.fill();
            harmonyMemory.iterate(maxIteration);
            //harmonyMemory.printHarmonies();
            //System.out.println();
            capInfluence[i] = harmonyMemory.best().getMerits()[0];
        }

        int countSpread = 0;
        int countInfluence = 0;
//        DescriptiveStatistics descriptiveStatisticsOriginal = new DescriptiveStatistics();
//        DescriptiveStatistics descriptiveStatisticsSpread = new DescriptiveStatistics();
//        DescriptiveStatistics descriptiveStatisticsInfluence = new DescriptiveStatistics();
//        for (int i = 0; i < cap; i++) {
//            //System.out.println(capSpread[i] + " " + capNoSpread[i]);
//
//            descriptiveStatisticsOriginal.addValue(capOriginal[i]);
//            descriptiveStatisticsSpread.addValue(capSpread[i]);
//            descriptiveStatisticsInfluence.addValue(capInfluence[i]);
//
//            if (capSpread[i] > capOriginal[i]) countSpread++;
//            if (capInfluence[i] > capOriginal[i]) countInfluence++;
//        }
//        System.out.println("Count Spread " + countSpread);
//        System.out.println("Count Influence " + countInfluence);
//        System.out.println("SD\t" + descriptiveStatisticsOriginal.getStandardDeviation() + "\t" + descriptiveStatisticsSpread.getStandardDeviation() + "\t" + descriptiveStatisticsInfluence.getStandardDeviation());
//        System.out.println("Max\t" + descriptiveStatisticsOriginal.getMax() + "\t" + descriptiveStatisticsSpread.getMax() + "\t" + descriptiveStatisticsInfluence.getMax());
//        System.out.println("Mean\t" + descriptiveStatisticsOriginal.getMean() + "\t" + descriptiveStatisticsSpread.getMean() + "\t" + descriptiveStatisticsInfluence.getMean());
//
//        TTestImpl tTest = new TTestImpl();
//        System.out.println("T-Spread " + tTest.tTest(capSpread, capOriginal));
//        System.out.println("T-Influence " + tTest.tTest(capInfluence, capOriginal));
    }

    /*private void oldMain(String[] args) {
    //ResultFormatter.format();
    //new double[][]{{0.5, 0.95}, {0.35, 0.99}, {0.0001, 0.01}, {5, 50}
    //{0, 0.1, 1}, {0, 0.5, 1}, {8, 15, 1}


    *//*ValueRange[] valueRanges = new ValueRange[numMusicians + 2];
        for (int i = 0; i < numMusicians; i++) {
            valueRanges[i] = new ValueRange(0, numNotes, false);
        }*//*
        //valueRanges[numMusicians] = new ValueRange(numHarmonies - 10, numHarmonies + 10, false);;
        //valueRanges[numMusicians + 1] = new ValueRange(0.5, 1, true);;
        //wrapper(30, 10, 0.85, 100, subsetEvaluator, data);


        //SpringHarmonyMemory harmonyMemory = new SpringHarmonyMemory(25, *//*3, *//*0.8);
        //statisticalReport();

        //SpringHarmonyMemory harmonyMemory = new SpringHarmonyMemory(25, 3, 0.8);
        *//*ConstraintFourHarmonyMemory harmonyMemory = new ConstraintFourHarmonyMemory(25, 3, 0.8);

        harmonyMemory.initialise();
        harmonyMemory.fill();
        harmonyMemory.iterate(50000);
        harmonyMemory.printHarmonies();*//*
        *//*System.out.println("Ours " + new SpringEvaluation().checkConstraints(new double[]{0.052310122724562355, 0.37183076590103425, 10.455416343702042}));
        System.out.println("Ours " + new SpringEvaluation().evaluate(new double[]{0.052310122724562355, 0.37183076590103425, 10.455416343702042}));
        //System.out.println(new BeamEvaluation().checkConstraints(new double[]{0.20573,3.47049,9.03662,0.20573}));
        //System.out.println("Arora " + new SpringEvaluation().checkConstraints(new double[]{0.053396,0.399180,9.185400}));
        System.out.println("IHS " + new SpringEvaluation().checkConstraints(new double[]{0.05115438,0.34987116,12.0764321}));
        System.out.println("IHS " + new SpringEvaluation().evaluate(new double[]{0.05115438,0.34987116,12.0764321}));
        //System.out.println("Bele " + new SpringEvaluation().checkConstraints(new double[]{0.050000,0.315900,14.25}));
        System.out.println("Coello " + new SpringEvaluation().checkConstraints(new double[]{0.051989,0.363965,10.890522}));
        System.out.println("Coello " + new SpringEvaluation().evaluate(new double[]{0.051989,0.363965,10.890522}));
*//*
        //System.out.println("Coello " + new BeamEvaluation().checkConstraints(new double[]{0.2088,3.4205,8.9975,0.2100}));
        //System.out.println("Coello " + new BeamEvaluation().evaluate(new double[]{0.2088, 3.4205, 8.9975, 0.2100}));

        //System.out.println("IHS " + new BeamEvaluation().checkConstraints(new double[]{0.20573, 3.47049, 9.03662, 0.20573}));
        //System.out.println("IHS " + new BeamEvaluation().evaluate(new double[]{0.20573, 3.47049, 9.03662, 0.20573}));

        //System.out.println(new UnconTwoEvaluation().evaluate(new double[]{0, -1}));
        //System.out.println(new ConstraintFiveEvaluation().evaluate(new double[]{2.2468258,2.381863}));
        //System.out.println(new ConstraintFourEvaluation().checkConstraints(new double[]{5,5,5}));
        //System.out.println(new ConstraintFourEvaluation().evaluate(new double[]{5,5,5}));
        //System.out.println(new UnconEvaluation().evaluate(new double[]{3.00000,3.99999}));


        //System.out.println("IHS " + new SpringEvaluation().checkConstraints(new double[]{0.05115438,0.34987116,12.0764321}));

        //statisticalReport();


        *//*HMS: 5 MS: 2 MI: 500000
        Parameters: {0.5,0.95}, {0.35,0.99}, {1.0E-5,0.01}
        Best Solution Found at : 457903
        (1)	-2.0224735637727037E-7	-1.0000013261429128	- 3.0000000000095626
        Number of Updates: 468 out of 500000*//*
        HarmonyMemoryOld lastResult = null;
        double temp;
        int count = 0;
        if (args[0].equals("spring")) {
            temp = 1;
            while (temp > 0.012681) {
                System.out.println(count);
                temp = springOld(new double[][]{{0.5, 0.95}, {0.35, 0.99}, {0.0001, 0.01}, {5, 50}}).getBestScore();
                count++;
            }
        } else if (args[0].equals("vessel")) {
            temp = 15000;
            while (temp > 7197.72895) {
                System.out.println(count);
                lastResult = vesselOld(new double[][]{{0.5, 0.95}, {0.35, 0.99}, {0.00001, 0.01}, {5, 10}});
                temp = lastResult.getBestScore();
                count++;
            }
            lastResult.printSummary();
        } else if (args[0].equals("beam")) {
            temp = 5;
            while (temp > 2.1754) {
                System.out.println(count);
                //beamOld(new double[][]{{0.5, 0.95}, {0.35, 0.99}, {0.00001, 0.01}, {5, 10}}).getBestScore();
                lastResult = beamOld(new double[][]{{0.5, 0.95}, {0.35, 0.99}, {0.00001, 0.01}, {5, 10}});
                temp = lastResult.getBestScore();
                count++;
            }
            lastResult.printSummary();
            *//*HMS: 5 MS: 4 MI: 500000
            Parameters: {0.5,0.95}, {0.35,0.99}, {1.0E-5,0.01}
            Best Solution Found at : 424777
            (2)	0.21228524426298637	8.050951198000964	7.878075365900629	0.21229951804353825	- 2.175095482060236
            Number of Updates: 4074 out of 500000*//*
        } else if (args[0].equals("disjoint")) {
            temp = 20;
            while (temp > 13.590842061409647) {
                System.out.println(count);
                temp = disjointOld(new double[][]{{0.5, 0.95}, {0.35, 0.99}, {0.00001, 0.01}, {25, 50}}).getBestScore();
                count++;
            }
        } else if (args[0].equals("constraintfive")) {
            temp = 20;
            while (temp > 13.590841776379554) {
                System.out.println(count);
                temp = constraintFiveOld(new double[][]{{0.5, 0.95}, {0.35, 0.99}, {0.00001, 0.01}, {25, 50}}).getBestScore();
                count++;
            }
        } else if (args[0].equals("constraintfour")) {
            temp = 0;
            *//*int best = 0;
            for (int i=0; i<1000; i++) {
                System.out.println(count);
                double now = constraintFourOld(new double[][]{{0.5, 0.95}, {0.35, 0.999}, {0.0000001, 0.01}, {25, 50}});
                if (now > temp) {
                    best = i;
                    temp = now;
                }
                count++;
            }
            System.out.println(best);*//*
            while (temp < 0.9999999999999999) {
                System.out.println(count);
                temp = constraintFourOld(new double[][]{{0.5, 0.95}, {0.35, 0.99}, {0.0000000001, 0.01}, {25, 50}}).getBestScore();
                count++;
            }
        } else if (args[0].equals("uncon")) {
            temp = 2;
            while (temp > 1.000000000064621) {
                System.out.println(count);
                temp = unconOld(new double[][]{{0.5, 0.95}, {0.35, 0.99}, {0.00000001, 0.01}, {25, 50}}).getBestScore();
                count++;
            }
        } else if (args[0].equals("uncontwo")) {
            temp = 4;
            while (temp > 3.000000000095626) {
                System.out.println(count);
                lastResult = unconTwoOld(new double[][]{{0.5, 0.95}, {0.35, 0.99}, {0.000001, 0.01}, {25, 50}});
                temp = lastResult.getBestScore();
                count++;

                *//*HMS: 5 MS: 2 MI: 500000
                Parameters: {0.5,0.95}, {0.35,0.99}, {1.0E-6,0.01}
                Best Solution Found at : 321144
                (0)	-7.316132771698078E-8	-0.9999999798627552	- 3.0000000000018066
                Number of Updates: 397 out of 500000*//*

            }
            lastResult.printSummary();
        }


        //reconstruct harmony memory if downsized
        //springOld(new double[][] {{0.5,0.95},{0.35,0.99},{0.0005, 0.05}});
        //springOld(new double[][] {{0.75,0.95},{0.35,0.99},{0.0005, 0.05}});
        //springOld(new double[][] {{0.95,0.95},{0.35,0.99},{0.0005, 0.05}});
        *//*double temp = 5;
        int count = 0;
        while (temp >= 1.7248) {
            System.out.println(count);
            temp = beamOld(new double[][]{{0.5, 0.95}, {0.35, 0.99}, {0.00001, 0.01}, {25, 50}});
            count++;
        }*//*
        *//*double temp = 15000;
        int count = 0;
        while (temp >= 7197.730) {
            System.out.println(count);
            temp = vesselOld(new double[][]{{0.5, 0.95}, {0.35, 0.99}, {0.00001, 0.01}, {25, 50}});
            count++;
        }*//*

        //System.out.println(count);
        *//*springOld(new double[][]{{0.5, 0.95}, {0.35, 0.99}, {0.0001, 0.01}, {50, 50}});
        springOld(new double[][]{{0.5, 0.95}, {0.35, 0.99}, {0.0001, 0.01}, {40, 50}});
        springOld(new double[][]{{0.5, 0.95}, {0.35, 0.99}, {0.0001, 0.01}, {30, 50}});
        springOld(new double[][]{{0.5, 0.95}, {0.35, 0.99}, {0.0001, 0.01}, {20, 50}});
        springOld(new double[][]{{0.5, 0.95}, {0.35, 0.99}, {0.0001, 0.01}, {10, 50}});
        springOld(new double[][]{{0.5, 0.95}, {0.35, 0.99}, {0.0001, 0.01}, {5, 50}});*//*
        //closer harmony size better result?

        *//*springOld(new double[][]{{0.75, 0.95}, {0.35, 0.99}, {0.0005, 0.05}});
        springOld(new double[][]{{0.75, 0.95}, {0.35, 0.99}, {0.0005, 0.05}});
        springOld(new double[][]{{0.75, 0.95}, {0.35, 0.99}, {0.0005, 0.05}});

        springOld(new double[][]{{0.95, 0.95}, {0.35, 0.99}, {0.0005, 0.05}});
        springOld(new double[][]{{0.95, 0.95}, {0.35, 0.99}, {0.0005, 0.05}});
        springOld(new double[][]{{0.95, 0.95}, {0.35, 0.99}, {0.0005, 0.05}});*//*


        //springOld(new double[][] {{0.5,0.95},{0.55,0.99},{0.0005, 0.05}});
        //springOld(new double[][] {{0.5,0.95},{0.75,0.99},{0.0005, 0.05}});

        *//*unconOld(new double[][]{{0.5, 0.95}, {0.35, 0.99}, {0.000001, 4}});
        unconOld(new double[][]{{0.5, 0.95}, {0.35, 0.99}, {0.000001, 4}});
        unconOld(new double[][]{{0.5, 0.95}, {0.35, 0.99}, {0.000001, 4}});*//*


        //springOld(new double[][] {{0.95,0.95},{0.35,0.99},{0.0005, 0.05}});
        //springOld(new double[][] {{0.95,0.95},{0.55,0.99},{0.0005, 0.05}});
        //springOld(new double[][] {{0.95,0.95},{0.75,0.99},{0.0005, 0.05}});

        //double[] temp;
        *//*int musicianSize = 3;
        int memorySize = 4;
        int maxIteration = 500000;
        //int lastBestIteration = 0;
        double[][] valueRanges = new double[][] {{0,0.1}, {0,0.5}, {0,20}};
        double[][] parameterRanges = new double[][] {{0.5,0.95},{0.35,0.99},{0.0005, 0.05}};
        //HarmonyMemoryOld harmonyMemory = new HarmonyMemoryOld(musicianSize, memorySize, maxIteration, *//**//*0.95, 0.7, 0.05, *//**//*true, valueRanges, parameterRanges);
        HarmonyMemoryOld harmonyMemory = new HarmonyMemoryOld(musicianSize, memorySize, maxIteration, true, valueRanges, parameterRanges);
        harmonyMemory.fill();
        harmonyMemory.iterate();*//*
        *//*for (int i=0; i<memorySize; i++) {
            temp = harmonyMemory.newHarmony();
            while (!SpringEvaluation.checkConstraints(temp)) {
                temp = harmonyMemory.newHarmony();
            }
            harmonyMemory.addHarmony(temp, i);
        }
        //harmonyMemory.printHorizontally();
        harmonyMemory.updateScores();*//*
        //harmonyMemory.printWorst();

        //System.out.println(harmonyMemory.newHarmony());

        //harmonyMemory.updateHarmonyMemory(new double[] {1,2,3,4,5,6,7,8,9,0});
        *//*for (int i=0; i<maxIteration; i++) {
            //System.out.println(i);
            temp = harmonyMemory.newHarmony();
            while (!SpringEvaluation.checkConstraints(temp)) {
                temp = harmonyMemory.newHarmony();
            }
            harmonyMemory.updateHarmonyMemory(temp);
            *//**//*if (harmonyMemory.updateHarmonyMemory(temp)) {
                //System.out.println(i);
                lastBestIteration = i;
            }*//**//*
        }*//*

        //harmonyMemory.printSummary();

        //harmonyMemory.printHorizontally();
        //harmonyMemory.updateScores();
        //harmonyMemory.printWorst();
        *//*harmonyMemory.printBest();
        System.out.println("Number of Updates: " + harmonyMemory.getUpdateCount() + " out of " + maxIteration);
        System.out.println("Last Best Iteration: " + lastBestIteration);*//*
    }*/

    private static double standardDeviation(double[] inputs) {
        double standardDeviation = 0d;
        double mean = mean(inputs);

        for (double input : inputs) {
            standardDeviation += Math.pow(input - mean, 2);
        }
        return Math.sqrt(standardDeviation / inputs.length);
    }

    private static double mean(double[] inputs) {
        double mean = 0d;
        for (double input : inputs) {
            mean += input;
        }
        return mean / inputs.length;
    }

/*    private static void statisticalReport() {
        HarmonyMemoryOld result;
        double avgUpdateCount;
        double lowerBestScore;
        double upperBestScore;
        double avgBestScore;
        double avgLastBestIteration;
        int numberOfRuns = 100;
        double[] results;

        NumberFormat formatter = new DecimalFormat("#.00");
        for (int j = 5; j <= 10; j++) {
            lowerBestScore = Double.MAX_VALUE;
            upperBestScore = 0d;
            avgUpdateCount = 0d;
            avgBestScore = 0d;
            avgLastBestIteration = 0d;
            results = new double[numberOfRuns];
            for (int i = 0; i < numberOfRuns; i++) {

                //MS
                //result = vesselOld(new double[][]{{0.85,0.85}, {0.8, 0.8}, {0.05, 0.05}, {(j-4) * 5, (j-4) * 10}});

                //HMCR
                result = vesselOld(new double[][]{{j / 10, 1}, {0.8, 0.8}, {0.05, 0.05}, {20, 20}});

                //result = vesselOld(new double[][]{{j/10, 1}, {0.8, 0.8}, {0.05, 0.05}, {20, 20}});
                //result = vesselOld(new double[][]{{0.85, 0.85}, {j/10, 1}, {0.01, 0.01}, {20, 20}});
                //result = vesselOld(new double[][]{{0.85, 0.85}, {0.9, 0.9}, {1/Math.pow(10, j-3), 1/Math.pow(10, j-4)}, {20, 20}});
                //result = vesselOld(new double[][]{{0.85, 0.85}, {0.8, 0.8}, {1 / Math.pow(10, j - 3), 0.01}, {20, 20}});
                avgUpdateCount += result.getUpdateCount();
                avgBestScore += result.getBestScore();
                results[i] = result.getBestScore();
                if (result.getBestScore() < lowerBestScore) {
                    lowerBestScore = result.getBestScore();
                }
                if (result.getBestScore() > upperBestScore) {
                    upperBestScore = result.getBestScore();
                }

                //System.out.println(result.getBestScore() + ",");

                avgLastBestIteration += result.getLastBestIteration();
            }
            //System.out.println();
            //MS
            //System.out.println((j-4) * 5 + "--" + (j-4) * 10 + " & " + formatter.format(avgBestScore/numberOfRuns)*//* + " & " + formatter.format(lowerBestScore)  + " & " +  formatter.format(upperBestScore)*//*  + " & " + formatter.format(standardDeviation(results))  + " & " + avgUpdateCount/numberOfRuns + " & " + avgLastBestIteration/numberOfRuns + " \\\\");

            //HMCR
            System.out.println((j + 0d) / 10 + "--" + 1 + " & " + formatter.format(avgBestScore / numberOfRuns)*//* + " & " + formatter.format(lowerBestScore)  + " & " +  formatter.format(upperBestScore)*//* + " & " + formatter.format(standardDeviation(results)) + " & " + avgUpdateCount / numberOfRuns + " & " + avgLastBestIteration / numberOfRuns + " \\\\");

            //System.out.println(1 / Math.pow(10, j - 3) + "--" + 0.01 + " & " + avgBestScore / numberOfRuns + " & " + avgUpdateCount / numberOfRuns + " & " + avgLastBestIteration / numberOfRuns + " //");
            lowerBestScore = Double.MAX_VALUE;
            upperBestScore = 0d;
            avgUpdateCount = 0d;
            avgBestScore = 0d;
            avgLastBestIteration = 0d;
            results = new double[numberOfRuns];
            for (int i = 0; i < numberOfRuns; i++) {

                //MS
                //result = vesselOld(new double[][]{{0.85,0.85}, {0.8, 0.8}, {0.05, 0.05}, {(j-4) * 10, (j-4) * 10}});

                //HMCR
                result = vesselOld(new double[][]{{j / 10, j / 10}, {0.8, 0.8}, {0.05, 0.05}, {20, 20}});

                //result = vesselOld(new double[][]{{0.85,0.85}, {j/10, j/10}, {0.01, 0.01}, {20, 20}});
                //result = vesselOld(new double[][]{{0.85, 0.85}, {0.8, 0.8}, {1 / Math.pow(10, j - 3), 1 / Math.pow(10, j - 3)}, {20, 20}});
                if (result.getBestScore() < lowerBestScore) {
                    lowerBestScore = result.getBestScore();
                }
                if (result.getBestScore() > upperBestScore) {
                    upperBestScore = result.getBestScore();
                }
                results[i] = result.getBestScore();
                avgUpdateCount += result.getUpdateCount();
                avgBestScore += result.getBestScore();
                avgLastBestIteration += result.getLastBestIteration();
            }

            //MS
            //System.out.println((j-4) * 10 + " & " + formatter.format(avgBestScore/numberOfRuns)*//* + " & " + formatter.format(lowerBestScore)  + " & " +  formatter.format(upperBestScore)*//*  + " & " +  formatter.format(standardDeviation(results))  + " & " + avgUpdateCount/numberOfRuns + " & " + avgLastBestIteration/numberOfRuns + " \\\\");

            //HMCR
            System.out.println((j + 0d) / 10 + " & " + formatter.format(avgBestScore / numberOfRuns)*//* + " & " + formatter.format(lowerBestScore)  + " & " +  formatter.format(upperBestScore)*//* + " & " + formatter.format(standardDeviation(results)) + " & " + avgUpdateCount / numberOfRuns + " & " + avgLastBestIteration / numberOfRuns + " \\\\");

            //System.out.println(1 / Math.pow(10, j - 3) + " & " + avgBestScore / numberOfRuns + " & " + avgUpdateCount / numberOfRuns + " & " + avgLastBestIteration / numberOfRuns + " //");
            System.out.println("\\hline");
        }
    }

    //springOld(new double[][]{{0.5, 0.95}, {0.35, 0.99}, {0.0001, 0.01}, {5, 50}})

    private static HarmonyMemoryOld springOld(double[][] parameterRanges) {
        HarmonyMemoryOld harmonyMemory = new HarmonyMemoryOld(
                3, 50, 50000, true,
                new double[][]{{0, 0.1, 1}, {0, 0.5, 1}, {8, 15, 1}},
                parameterRanges, new SpringEvaluation());
        harmonyMemory.fill();
        harmonyMemory.iterate();
        //harmonyMemory.printSummary();
        return harmonyMemory;
    }

    private static HarmonyMemoryOld vesselOld(double[][] parameterRanges) {
        HarmonyMemoryOld harmonyMemory = new HarmonyMemoryOld(
                4, 20, 2000, true,
                //new double[][] {{17,19,0}, {9,11,0}, {58,59,1}, {43,44,1}},
                new double[][]{{0, 50, 0}, {0, 50, 0}, {0, 200, 1}, {0, 200, 1}},
                parameterRanges, new VesselEvaluation());
        harmonyMemory.fill();
        harmonyMemory.iterate();
        //harmonyMemory.printSummary();
        return harmonyMemory;
        //System.out.println(18 * 0.0625 + " " + 10 * 0.0625);
        //System.out.println(0.6224 * 1.125 * 58.29015 * 43.69268 + 1.7781 * 0.625 * Math.pow(58.29015, 2) + 3.1661 * Math.pow(1.125, 2) * 43.69268 + 19.84 * Math.pow(1.125, 2) * 58.29015);
    }

    private static HarmonyMemoryOld beamOld(double[][] parameterRanges) {
        HarmonyMemoryOld harmonyMemory = new HarmonyMemoryOld(
                4, 10, 500000, true,
                new double[][]{{0.125, 5, 1}, {0.1, 10, 1}, {0.1, 10, 1}, {0.1, 5, 1}},
                //new double[][] {{0.2,0.21,1}, {3.4,3.5,1}, {8.9,9.1,1}, {0.2,0.211,1}},
                parameterRanges, new BeamEvaluation());
        harmonyMemory.fill();
        harmonyMemory.iterate();
        //harmonyMemory.printSummary();
        return harmonyMemory;
        //System.out.println(18 * 0.0625 + " " + 10 * 0.0625);
        //System.out.println(0.6224 * 1.125 * 58.29015 * 43.69268 + 1.7781 * 0.625 * Math.pow(58.29015, 2) + 3.1661 * Math.pow(1.125, 2) * 43.69268 + 19.84 * Math.pow(1.125, 2) * 58.29015);
    }

    private static HarmonyMemoryOld disjointOld(double[][] parameterRanges) {
        HarmonyMemoryOld harmonyMemory = new HarmonyMemoryOld(
                2, 50, 500000, true,
                new double[][]{{2, 3, 1}, {2, 3, 1}},
                parameterRanges, new DisjointEvaluation());
        harmonyMemory.fill();
        harmonyMemory.iterate();
        //harmonyMemory.printSummary();
        return harmonyMemory;
        //System.out.println(18 * 0.0625 + " " + 10 * 0.0625);
        //System.out.println(0.6224 * 1.125 * 58.29015 * 43.69268 + 1.7781 * 0.625 * Math.pow(58.29015, 2) + 3.1661 * Math.pow(1.125, 2) * 43.69268 + 19.84 * Math.pow(1.125, 2) * 58.29015);
    }

    private static HarmonyMemoryOld constraintFiveOld(double[][] parameterRanges) {
        HarmonyMemoryOld harmonyMemory = new HarmonyMemoryOld(
                2, 50, 500000, true,
                new double[][]{{2, 3, 1}, {2, 3, 1}},
                parameterRanges, new ConstraintFiveEvaluation());
        harmonyMemory.fill();
        harmonyMemory.iterate();
        //harmonyMemory.printSummary();
        return harmonyMemory;
        //System.out.println(18 * 0.0625 + " " + 10 * 0.0625);
        //System.out.println(0.6224 * 1.125 * 58.29015 * 43.69268 + 1.7781 * 0.625 * Math.pow(58.29015, 2) + 3.1661 * Math.pow(1.125, 2) * 43.69268 + 19.84 * Math.pow(1.125, 2) * 58.29015);
    }

    private static HarmonyMemoryOld constraintFourOld(double[][] parameterRanges) {
        HarmonyMemoryOld harmonyMemory = new HarmonyMemoryOld(
                3, 50, 200000, false,
                new double[][]{{0, 10, 1}, {0, 10, 1}, {0, 10, 1}},
                parameterRanges, new ConstraintFourEvaluation());
        harmonyMemory.fill();
        harmonyMemory.iterate();
        //harmonyMemory.printSummary();
        return harmonyMemory;
        //System.out.println(18 * 0.0625 + " " + 10 * 0.0625);
        //System.out.println(0.6224 * 1.125 * 58.29015 * 43.69268 + 1.7781 * 0.625 * Math.pow(58.29015, 2) + 3.1661 * Math.pow(1.125, 2) * 43.69268 + 19.84 * Math.pow(1.125, 2) * 58.29015);
    }

    private static HarmonyMemoryOld unconOld(double[][] parameterRanges) {
        HarmonyMemoryOld harmonyMemory = new HarmonyMemoryOld(
                2, 50, 500000, true,
                new double[][]{{-50, 50, 1}, {-50, 50, 1}},
                parameterRanges, new UnconEvaluation());
        harmonyMemory.fill();
        harmonyMemory.iterate();
        //harmonyMemory.printSummary();
        return harmonyMemory;
        //System.out.println(18 * 0.0625 + " " + 10 * 0.0625);
        //System.out.println(0.6224 * 1.125 * 58.29015 * 43.69268 + 1.7781 * 0.625 * Math.pow(58.29015, 2) + 3.1661 * Math.pow(1.125, 2) * 43.69268 + 19.84 * Math.pow(1.125, 2) * 58.29015);
    }

    private static HarmonyMemoryOld unconTwoOld(double[][] parameterRanges) {
        HarmonyMemoryOld harmonyMemory = new HarmonyMemoryOld(
                2, 50, 500000, true,
                new double[][]{{-50, 50, 1}, {-50, 50, 1}},
                parameterRanges, new UnconTwoEvaluation());
        harmonyMemory.fill();
        harmonyMemory.iterate();
        //harmonyMemory.printSummary();
        return harmonyMemory;
        //System.out.println(18 * 0.0625 + " " + 10 * 0.0625);
        //System.out.println(0.6224 * 1.125 * 58.29015 * 43.69268 + 1.7781 * 0.625 * Math.pow(58.29015, 2) + 3.1661 * Math.pow(1.125, 2) * 43.69268 + 19.84 * Math.pow(1.125, 2) * 58.29015);
    }*/

    private static void classifierAggregation(Classifier baseClassifier, int groupSize, String dataset) throws Exception {
        System.out.println("\nEvaluating Dataset - " + dataset);
        int numFolds = 10;

        Instances data;
        data = Dataset.getDataset(dataset);
        data.setClassIndex(data.numAttributes() - 1);

        Instances[] trainingFolds = new Instances[numFolds];
        Instances[] testingFolds = new Instances[numFolds];
        CV.generateCrossValidationFoldsGrouped(data, trainingFolds, testingFolds, groupSize);

        Classifier classifier;
        classifier = (Classifier) Class.forName(baseClassifier.getClass().getName()).newInstance();

        System.out.println(CV.crossValidateGrouped(classifier, trainingFolds, testingFolds, groupSize));

        //baseResult = CV.crossValidate(classifier, trainingTestingFolds);
        //System.out.println("Base\t" + classifier.getClass().getName());
    }

}
