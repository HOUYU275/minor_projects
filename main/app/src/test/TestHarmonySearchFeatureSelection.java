package test;

import util.CrossValidation;
import util.CrossValidator;
import util.Dataset;
import util.FeatureSelectionCrossValidator;
import weka.attributeSelection.ASEvaluation;
import weka.classifiers.fuzzy.VQNN;
import weka.core.Instances;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 04/08/11
 * Time: 12:17
 * To change this template use File | Settings | File Templates.
 */
public class TestHarmonySearchFeatureSelection {

    //private static boolean verbose = true;

    public static void main(String[] args) throws Exception {

        //String[] datasets = Dataset.frDatasets;

        /*Instances instances = Dataset.getDataset("arrhythmia");
        ASEvaluation asEvaluation = Dataset.getASEvaluation("FuzzyRoughSubsetEval", instances);

        HarmonySearch harmonySearch = new HarmonySearch();
        harmonySearch.setIteration(2000);
        harmonySearch.setNumMusicians(instances.numAttributes());
        harmonySearch.search(asEvaluation, instances);
        System.out.println(((FuzzyRoughSubsetEval) asEvaluation).getCount());

        ((FuzzyRoughSubsetEval) asEvaluation).setCount(0);
        GeneticSearch geneticSearch = new GeneticSearch();
        geneticSearch.search(asEvaluation, instances);
        System.out.println(((FuzzyRoughSubsetEval) asEvaluation).getCount());

        ((FuzzyRoughSubsetEval) asEvaluation).setCount(0);
        PSOSearch psoSearch = new PSOSearch();
        psoSearch.search(asEvaluation, instances);
        System.out.println(((FuzzyRoughSubsetEval) asEvaluation).getCount());

        ((FuzzyRoughSubsetEval) asEvaluation).setCount(0);
        GreedyStepwise greedyStepwise = new GreedyStepwise();
        greedyStepwise.search(asEvaluation, instances);
        System.out.println(((FuzzyRoughSubsetEval) asEvaluation).getCount());*/

        //testIterativeSearch("CfsSubsetEval", 3000);
        //testIterativeSearch("FuzzyRoughSubsetEval", 2000);
        //testIterativeSearch("ConsistencySubsetEval", 3000);
        //testIterativeHarmonySearch("FuzzyRoughSubsetEval", 2000, 11, true, 10);
        /*testIterativeHarmonySearch("FuzzyRoughSubsetEval", 1000, 00, false, 10);
        testIterativeHarmonySearch("FuzzyRoughSubsetEval", 1000, 01, false, 10);
        testIterativeHarmonySearch("FuzzyRoughSubsetEval", 1000, 02, false, 10);
        testIterativeHarmonySearch("FuzzyRoughSubsetEval", 1000, 10, false, 10);
        testIterativeHarmonySearch("FuzzyRoughSubsetEval", 1000, 11, false, 10);
        testIterativeHarmonySearch("FuzzyRoughSubsetEval", 1000, 12, false, 10);
        testIterativeHarmonySearch("FuzzyRoughSubsetEval", 1000, 20, false, 10);
        testIterativeHarmonySearch("FuzzyRoughSubsetEval", 1000, 21, false, 10);
        testIterativeHarmonySearch("FuzzyRoughSubsetEval", 1000, 22, false, 10);*/

        /*FeatureSelectionResultCollector[] cfsCollectors =
                testFeatureSelection("CfsSubsetEval", datasets, 10);*/
        /*FeatureSelectionResultCollector[] frCollectors =
                testFeatureSelection("FuzzyRoughSubsetEval", Dataset.smcb, 2000, 11, true, 1);*/
        /*FeatureSelectionResultCollector[] frCollectors =
                testFeatureSelection("ConsistencySubsetEval", Dataset.wisconsin, 2000, 11, false, 1);*/
        FeatureSelectionResultCollector[] cCollectors =
                testFeatureSelectionQuick("CfsSubsetEval", Dataset.turing_high, 50000, 11, false, 5);
        /*FeatureSelectionResultCollector[] cCollectors =
                testFeatureSelection("CfsSubsetEval", Dataset.cfsDatasets, 3000, 11, true, 5);*/
        /*FeatureSelectionResultCollector[] wCollectors =
                testFeatureSelection("WrapperSubsetEval", datasets, 10);*/

        //for (int i=0;i<featureSelectionResults.length;i++) System.out.println(featureSelectionResults[i].toString());
        //testIterativeHarmonySearch("CfsSubsetEval", 3000, 21, false, 10);
        /*testIterativeHarmonySearch("CfsSubsetEval", 3000, 00, false, 10);
        testIterativeHarmonySearch("CfsSubsetEval", 3000, 01, false, 10);
        testIterativeHarmonySearch("CfsSubsetEval", 3000, 02, false, 10);
        testIterativeHarmonySearch("CfsSubsetEval", 3000, 10, false, 10);
        testIterativeHarmonySearch("CfsSubsetEval", 3000, 11, false, 10);
        testIterativeHarmonySearch("CfsSubsetEval", 3000, 12, false, 10);
        testIterativeHarmonySearch("CfsSubsetEval", 3000, 20, false, 10);
        testIterativeHarmonySearch("CfsSubsetEval", 3000, 21, false, 10);
        testIterativeHarmonySearch("CfsSubsetEval", 3000, 22, false, 10);*/
        //testIterativeHarmonySearch("FuzzyRoughSubsetEval", 2000, false, 10);

        //testIterativeHarmonySearch("ConsistencySubsetEval", 3000, false, 25);
        /*testSearch("CfsSubsetEval", 3000, false, 25);
        testSearch("FuzzyRoughSubsetEval", 2000, false, 25);
        testSearch("ConsistencySubsetEval", 3000, false, 25);*/
        //testIterativeHarmonySearch("WrapperSubsetEval", 1000);

        //testIterativeHarmonySearch("CfsSubsetEval", 3000);
        //testIterativeHarmonySearch("FuzzyRoughSubsetEval", 2000);
        //testIterativeHarmonySearch("ConsistencySubsetEval", 3000);
        //testIterativeHarmonySearch("WrapperSubsetEval", 1000);

        //iterativeSearch("heart", "FuzzyRoughSubsetEval", 1000);
        /*iterativeSearch("multifeat", "FuzzyRoughSubsetEval", 1000);
        iterativeSearch("multifeat", "FuzzyRoughSubsetEval", 1000);
        iterativeSearch("multifeat", "FuzzyRoughSubsetEval", 1000);
        iterativeSearch("multifeat", "FuzzyRoughSubsetEval", 1000);*/

        /*iterativeSearch("sonar", "CfsSubsetEval", 1000);
        iterativeSearch("sonar", "CfsSubsetEval", 1000);
        iterativeSearch("sonar", "CfsSubsetEval", 1000);
        iterativeSearch("sonar", "CfsSubsetEval", 1000);
        iterativeSearch("sonar", "CfsSubsetEval", 1000);*/
    }

    public static double fullAccuracy(
            String databaseName) throws Exception {
        Instances instances = null;
        try {
            instances = Dataset.getDataset(databaseName);
            instances.setClassIndex(instances.numAttributes() - 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.gc();
        return CV.crossValidate(10, new VQNN(), instances);
    }

    public static FeatureSelectionResultCollector[] testFeatureSelection(
            String evaluationMethod,
            String[] datasets,
            int repeat) throws Exception {

        CrossValidation crossValidation;
        FeatureSelectionResultCollector[] collectors = new FeatureSelectionResultCollector[datasets.length];
        ExecutorService executor = Executors.newFixedThreadPool(1);

        for (int i = 0; i < datasets.length; i++) {
            crossValidation = new CrossValidation(Dataset.getDataset(datasets[i]));
            FeatureSelectionResultCollector collector = new FeatureSelectionResultCollector(datasets[i], repeat);
            crossValidation.setResultCollector(collector);

            for (int j = 0; j < crossValidation.getNumFolds(); j++) {
                ASEvaluation asEvaluation = Dataset.getASEvaluation(evaluationMethod, crossValidation.getTrainingFold(j));
                executor.execute(newCrossValidator(crossValidation, asEvaluation, "HC", j));
                //executor.execute(newCrossValidator(crossValidation, asEvaluation, "HS", j));
                //executor.execute(newCrossValidator(crossValidation, asEvaluation, "PSO", j));
                //executor.execute(newCrossValidator(crossValidation, asEvaluation, "GA", j));
            }

            collectors[i] = collector;
            System.gc();
        }
        return collectors;
    }

    public static CrossValidator newCrossValidator(
            CrossValidation validation, ASEvaluation asEvaluation, String asSearch, int numFold) {
        FeatureSelectionCrossValidator validator = new FeatureSelectionCrossValidator();
        validator.setMother(validation);
        validator.setNumFold(numFold);
        validator.setAsEvaluation(asEvaluation);
        validator.setAsSearch(asSearch);
        return validator;
    }

    public static FeatureSelectionResultCollector[] testFeatureSelection(
            String evaluationMethod,
            String[] datasets,
            int maxIteration,
            int parameterMode,
            boolean iterative,
            int repeat) throws Exception {

        GregorianCalendar calendar = new GregorianCalendar();
        Date date = calendar.getTime();
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        String s = dateFormat.format(date);
        s = s.replaceAll("[:]", "-");

        BufferedWriter writer = new BufferedWriter(new FileWriter(evaluationMethod + " " + s + ".txt"));

        writer.append("Mode = " + parameterMode);
        writer.newLine();
        writer.append("Method\tFold\tRepeat\tSize\tMerit\tTime\tFeatures");
        writer.newLine();
        writer.flush();

        CrossValidation crossValidation;

        FeatureSelectionResultCollector[] collectors = new FeatureSelectionResultCollector[datasets.length];
        FeatureSelectionCrossValidator validator = new FeatureSelectionCrossValidator();

        for (int i = 0; i < datasets.length; i++) {

            System.out.println(datasets[i]);
            writer.newLine();
            writer.append(datasets[i] + "\t" + datasets[i] + "\t" + datasets[i] + "\t" + datasets[i]);
            writer.newLine();
            writer.newLine();
            crossValidation = new CrossValidation(Dataset.getDataset(datasets[i]));

            double merit = 0d;
            int subsetSize = 0;

            FeatureSelectionResultCollector collector = new FeatureSelectionResultCollector(datasets[i], repeat);

            ASEvaluation asEvaluation = (ASEvaluation) Class.forName(
                    "weka.attributeSelection." + evaluationMethod).newInstance();

            for (int j = 0; j < crossValidation.getNumFolds(); j++) {
                asEvaluation.buildEvaluator(crossValidation.getTrainingFold(j));
                for (int k = 0; k < repeat; k++) {
                    FeatureSelectionResult featureSelectionResult = validator.geneticAlgorithmFeatureSelection(crossValidation.getTrainingFold(j), asEvaluation);
                    System.out.println(featureSelectionResult.toString());
                    writer.append("GA\t" + j + "\t" + k + "\t" + featureSelectionResult.outputFormat());
                    writer.newLine();
                    collector.put(featureSelectionResult.getMethod(), j, k, featureSelectionResult);
                }
                writer.flush();
            }

            for (int j = 0; j < crossValidation.getNumFolds(); j++) {
                asEvaluation.buildEvaluator(crossValidation.getTrainingFold(j));
                for (int k = 0; k < repeat; k++) {
                    FeatureSelectionResult featureSelectionResult = validator.particleSwarmFeatureSelection(crossValidation.getTrainingFold(j), asEvaluation);
                    System.out.println(featureSelectionResult.toString());
                    writer.append("PSO\t" + j + "\t" + k + "\t" + featureSelectionResult.outputFormat());
                    writer.newLine();
                    collector.put(featureSelectionResult.getMethod(), j, k, featureSelectionResult);

                }
                writer.flush();
            }

            for (int j = 0; j < crossValidation.getNumFolds(); j++) {
                asEvaluation.buildEvaluator(crossValidation.getTrainingFold(j));
                for (int k = 0; k < 1; k++) {
                    FeatureSelectionResult featureSelectionResult = validator.hillClimbingFeatureSelection(crossValidation.getTrainingFold(j), asEvaluation);
                    System.out.println(featureSelectionResult.toString());
                    writer.append("HC\t" + j + "\t" + k + "\t" + featureSelectionResult.outputFormat());
                    writer.newLine();
                    collector.put(featureSelectionResult.getMethod(), j, k, featureSelectionResult);
                    //merit += featureSelectionResult.getMerit();
                    //subsetSize += featureSelectionResult.getSubsetSize();
                }
                writer.flush();
            }

            for (int j = 0; j < crossValidation.getNumFolds(); j++) {
                asEvaluation.buildEvaluator(crossValidation.getTrainingFold(j));
                for (int k = 0; k < repeat; k++) {
                    FeatureSelectionResult featureSelectionResult =
                            validator.harmonySearchFeatureSelection(
                                    crossValidation.getTrainingFold(j),
                                    asEvaluation,
                                    maxIteration,
                                    parameterMode,
                                    iterative);
                    System.out.println(featureSelectionResult.toString());
                    writer.append("HS\t" + j + "\t" + k + "\t" + featureSelectionResult.outputFormat());
                    writer.newLine();
                    collector.put(featureSelectionResult.getMethod(), j, k, featureSelectionResult);
                }
                writer.flush();
            }

            //Debug.println(datasets[i] + "\t" + subsetSize / repeat * 10 + "\t" + merit / repeat * 10);
            //featureSelectionResults[i] =
            //new FeatureSelectionResult(datasets[i], null, subsetSize / repeat, merit / repeat);
            collectors[i] = collector;
            System.gc();

        }

        writer.close();
        return collectors;
    }
public static FeatureSelectionResultCollector[] testFeatureSelectionQuick(
            String evaluationMethod,
            String[] datasets,
            int maxIteration,
            int parameterMode,
            boolean iterative,
            int repeat) throws Exception {

        GregorianCalendar calendar = new GregorianCalendar();
        Date date = calendar.getTime();
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        String s = dateFormat.format(date);
        s = s.replaceAll("[:]", "-");

        BufferedWriter writer = new BufferedWriter(new FileWriter(evaluationMethod + " " + s + ".txt"));

        writer.append("Mode = " + parameterMode);
        writer.newLine();
        writer.append("Method\tFold\tRepeat\tSize\tMerit\tTime\tFeatures");
        writer.newLine();
        writer.flush();

        CrossValidation crossValidation;

        FeatureSelectionResultCollector[] collectors = new FeatureSelectionResultCollector[datasets.length];
        FeatureSelectionCrossValidator validator = new FeatureSelectionCrossValidator();

        for (int i = 0; i < datasets.length; i++) {

            System.out.println(datasets[i]);
            writer.newLine();
            writer.append(datasets[i] + "\t" + datasets[i] + "\t" + datasets[i] + "\t" + datasets[i]);
            writer.newLine();
            writer.newLine();
            crossValidation = new CrossValidation(Dataset.getDataset(datasets[i]));

            double merit = 0d;
            int subsetSize = 0;

            FeatureSelectionResultCollector collector = new FeatureSelectionResultCollector(datasets[i], repeat);

            ASEvaluation asEvaluation = (ASEvaluation) Class.forName(
                    "weka.attributeSelection." + evaluationMethod).newInstance();

            for (int j = 0; j < crossValidation.getNumFolds(); j++) {
                asEvaluation.buildEvaluator(crossValidation.getTrainingFold(j));
                for (int k = 0; k < repeat; k++) {
                    FeatureSelectionResult featureSelectionResult = validator.geneticAlgorithmFeatureSelection(crossValidation.getTrainingFold(j), asEvaluation);
                    System.out.println("GA\t" + j + "\t" + k + "\t" + featureSelectionResult.outputFormat());
                    writer.append("GA\t" + j + "\t" + k + "\t" + featureSelectionResult.outputFormat());
                    writer.newLine();
                    collector.put(featureSelectionResult.getMethod(), j, k, featureSelectionResult);
                }
                writer.flush();
            }

            for (int j = 0; j < crossValidation.getNumFolds(); j++) {
                asEvaluation.buildEvaluator(crossValidation.getTrainingFold(j));
                for (int k = 0; k < repeat; k++) {
                    FeatureSelectionResult featureSelectionResult = validator.particleSwarmFeatureSelection(crossValidation.getTrainingFold(j), asEvaluation);
                    System.out.println("PSO\t" + j + "\t" + k + "\t" + featureSelectionResult.outputFormat());
                    writer.append("PSO\t" + j + "\t" + k + "\t" + featureSelectionResult.outputFormat());
                    writer.newLine();
                    collector.put(featureSelectionResult.getMethod(), j, k, featureSelectionResult);

                }
                writer.flush();
            }

            for (int j = 0; j < crossValidation.getNumFolds(); j++) {
                asEvaluation.buildEvaluator(crossValidation.getTrainingFold(j));
                for (int k = 0; k < 1; k++) {
                    FeatureSelectionResult featureSelectionResult = validator.hillClimbingFeatureSelection(crossValidation.getTrainingFold(j), asEvaluation);
                    System.out.println("HC\t" + j + "\t" + k + "\t" + featureSelectionResult.outputFormat());
                    writer.append("HC\t" + j + "\t" + k + "\t" + featureSelectionResult.outputFormat());
                    writer.newLine();
                    collector.put(featureSelectionResult.getMethod(), j, k, featureSelectionResult);
                    //merit += featureSelectionResult.getMerit();
                    //subsetSize += featureSelectionResult.getSubsetSize();
                }
                writer.flush();
            }

            for (int j = 0; j < crossValidation.getNumFolds(); j++) {
                asEvaluation.buildEvaluator(crossValidation.getTrainingFold(j));
                for (int k = 0; k < repeat; k++) {
                    FeatureSelectionResult featureSelectionResult =
                            validator.harmonySearchFeatureSelection(
                                    crossValidation.getTrainingFold(j),
                                    asEvaluation,
                                    maxIteration,
                                    parameterMode,
                                    iterative);
                    System.out.println("HS\t" + j + "\t" + k + "\t" + featureSelectionResult.outputFormat());
                    writer.append("HS\t" + j + "\t" + k + "\t" + featureSelectionResult.outputFormat());
                    writer.newLine();
                    collector.put(featureSelectionResult.getMethod(), j, k, featureSelectionResult);
                }
                writer.flush();
            }

            //Debug.println(datasets[i] + "\t" + subsetSize / repeat * 10 + "\t" + merit / repeat * 10);
            //featureSelectionResults[i] =
            //new FeatureSelectionResult(datasets[i], null, subsetSize / repeat, merit / repeat);
            collectors[i] = collector;
            System.gc();

        }

        writer.close();
        return collectors;
    }

/*public static FeatureSelectionResult harmonySearchFeatureSelection(
    Instances instances,
    String evaluationMethod,
    int maxIteration,
    int parameterMode,
    boolean iterative) throws Exception {

ASEvaluation asEvaluation = (ASEvaluation) Class.forName("weka.attributeSelection." + evaluationMethod).newInstance();
asEvaluation.buildEvaluator(instances);

int size = instances.numAttributes();

double merit = 0d;
int[] searchResult = null;

HarmonySearch harmonySearch = new HarmonySearch();
harmonySearch.setIteration(maxIteration);
harmonySearch.setNumMusicians(size);
harmonySearch.setParameterMode(parameterMode);
harmonySearch.search(asEvaluation, instances);

Debug.println(size + "");
if (iterative) {
    while ((harmonySearch.getM_bestMerit() >= merit)) {
        Debug.println("->" + harmonySearch.getSearchResult().length);
        if ((harmonySearch.getM_bestMerit() == merit) & (harmonySearch.getSearchResult().length == size)) {
            break;
        }

        size = harmonySearch.getSearchResult().length;
        merit = harmonySearch.getM_bestMerit();
        searchResult = harmonySearch.getSearchResult();
        harmonySearch.setNumMusicians(size);
        harmonySearch.search(asEvaluation, instances);
    }
} else {
    harmonySearch.setIteration(maxIteration);
    harmonySearch.setNumMusicians(size);
    harmonySearch.search(asEvaluation, instances);
    merit = harmonySearch.getM_bestMerit();
    searchResult = harmonySearch.getSearchResult();
}

*//*BitSet bitSet = new BitSet(instances.numAttributes());
        if (verbose) {
            for (int i = 0; i < searchResult.length; i++) {
                bitSet.set(searchResult[i]);
            }
            System.out.print("\t");
            for (int attribute : searchResult) System.out.print(attribute + " ");
            System.out.println("\t" + searchResult.length + "\t" + merit);
        }*//*
        return new FeatureSelectionResult("HarmonySearch", searchResult, merit);
    }
*/

/*public static FeatureSelectionResult[] testHarmonySearchFeatureSelection(
            String evaluationMethod, String[] datasets,
            int maxIteration, int parameterMode,
            boolean iterative, int repeat) throws Exception {
        //String[] datasets = exampleDatasets;
        System.out.println("Mode = " + parameterMode);
        CrossValidation crossValidator;
                ;
        FeatureSelectionResult[] featureSelectionResults = new FeatureSelectionResult[datasets.length];
        for (int i = 0; i < datasets.length; i++) {
            System.out.println(datasets[i]);
            crossValidator  = new CrossValidation(Dataset.getDataset(datasets[i]), 10);
            //System.out.println("Full Accuracy = " + fullAccuracy(datasets[i]));
            double accuracy = 0d;
            int repeatCount = 0;
            FeatureSelectionResult featureSelectionResult;
            double merit = 0d;
            int subsetSize = 0;
            while (repeatCount < repeat) {
                featureSelectionResult =
                        harmonySearchFeatureSelection(
                                crossValidator.getTrainingFold(0), evaluationMethod, maxIteration, parameterMode, iterative);
                merit += featureSelectionResult.getMerit();
                subsetSize += featureSelectionResult.getSubsetSize();
                //int[] selectedSubset = featureSelectionResult.getSelectedSubset();
                repeatCount++;
                //System.out.println(datasets[i] + "\t" + subsetSize / (repeat + 0d) + "\t" + merit / (repeat + 0d));
                //for (int k : selectedSubset) System.out.println(k + " ");
                //System.out.println();
            }
            System.out.println(datasets[i] + "\t" + subsetSize / repeat + "\t" + merit / repeat);
            //System.out.println("Average Accuracy = " + accuracy / repeat + "");
            featureSelectionResults[i] =
                    new FeatureSelectionResult(datasets[i], null, subsetSize / repeat, merit / repeat);
        }
        return featureSelectionResults;
    }
*/

}
