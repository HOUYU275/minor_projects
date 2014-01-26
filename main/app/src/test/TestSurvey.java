package test;

import survey.ClassificationResult;
import survey.FeatureSelectionDetailDouble;
import survey.FeatureSelectionResult;
import util.ClassifierCrossValidator;
import util.CrossValidation;
import util.ParameterConfigurator;
import util.Registry;
import weka.attributeSelection.*;
import weka.core.Instances;

import java.io.*;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: rrd09
 * Date: 05/12/12
 * Time: 15:37
 */
public class TestSurvey {

    private static int topRepeats = 2;
    private static int folds = 10;
    private static int botRepeats = 5;
    private static int foldOffset = 0;
    private static String datasetsString = "CES";
    private static String classifierString1 = "J48";
    private static String classifierString2 = "NB";
    private static String classifierString3 = "VQNN";
    private static String[] classifierStrings = new String[]{
            "J48", "NB", "VQNN"
    };
    private static String evaluationString = "PCFS";
    private static String[] datasets;
    private static String classifier1;
    private static String classifier2;
    private static String classifier3;
    private static ASEvaluation evaluation;
    private static Vector<ASSearch> searches;
    private static boolean saveToDisk = true;
    private static String[] searchesString = new String[]{
//            "HC",
            "ABC",
            "ACO",
            "AIS",
            "FF",
            "GA",
            "HS",
            "MS",
            "PSO",
            "SA",
            "TS",
    };
    /*private static String[] searchesString = new String[]{
            "ABC",
            "ACO",
            "AIS",
            "FF",
            "GA",
            "HC",
            "HS",
            "MS",
            "PSO",
            "SA",
            "RS",
            "TS",
    };*/
    private static String[] searchesStringFull = new String[]{
            "ACO",
            "ABC",
            "FF",
            "GA",
            "HC",
            "HS",
            "MS",
            "PSO",
            "SA",
            "SS",
            "RS",
            "TS"};
    private static Random random = new Random();

    public static void main(String[] args) throws Exception {

        applySettings();
        reportSettings();
        buildDirectories(datasets, searchesString);
        for (String dataset : datasets) {
            switch (args[0]) {
                case "s":
                    search(dataset);
                    break;
                case "v":
                    validate(dataset);
                    break;
                case "vr":
                    validateRaw(dataset);
                    break;
                case "c":
                    check(dataset);
                    break;
                case "del":
                    System.out.println("Confirm Deletion for " + dataset + " " + evaluationString);
                    Scanner scanner = new Scanner(System.in);
                    if (scanner.next().equals("DELETE")) delete(dataset, evaluationString);
                    break;
                case "d":
                    display(dataset, "HC");
                    display(dataset, "ABC");
                    display(dataset, "ACO");
                    break;
                case "fd":
                    fancyDisplay(dataset);
                    break;
                case "dcr":
                    displayClassification(dataset, "raw");
                    break;
                case "dcf":
                    //ArrayList<String[]> results = new ArrayList<>();
                    System.out.print(dataset + "\t");
                    for (String searchString : searchesString) {
                        String[] results = displayClassificationFancy(dataset, searchString);
                        for (int j = 0; j < classifierStrings.length - 1; j++) System.out.print(results[j] + "\t");

                    }
                    System.out.println();
                    break;
                case "df":
                    displayFeatureSelection(dataset, "ACO");
                    break;
                default:
                    search(dataset);
                    break;
            }
            /*ClassificationResult classificationResult = serialiseLoadClassificationResultRaw(dataset);
            System.out.println(dataset + "\n" + classificationResult.aggregate());s*/
        }

        System.exit(0);
    }

    public static void check(String dataset) {
        for (int t = 0; t < topRepeats; t++) {
            for (String searchString : searchesStringFull) {
                File file = new File(Registry.surveyPath + t + Registry.separator + searchString + Registry.separator + dataset + Registry.separator + "CFS");
                if (!file.exists()) {
                    System.out.println(t + "\t" + searchString + "\t" + dataset + "\t" + "CFS");
                }
                /*file = new File(Registry.surveyPath + t + Registry.separator + searchString + Registry.separator + dataset + Registry.separator + "PCFS");
                if (!file.exists()) {
                    System.out.println(t + "\t" + searchString + "\t" + dataset + "\t" + "PCFS");
                }*/
                /*file = new File(Registry.surveyPath + t + Registry.separator + searchString + Registry.separator + dataset + Registry.separator + "FRFS");
                if (!file.exists()) {
                    System.out.println(t + "\t" + searchString + "\t" + dataset + "\t" + "FRFS");
                }*/
            }
        }

    }

    private static void reportSettings() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Data set : ").append(datasetsString).append(" (").append(datasets.length).append(")\n");
        buffer.append("Classifiers : ").append(classifierString1).append(" ").append(classifierString2).append(" ").append(classifierString3).append("\n");
        buffer.append("Evaluation : ").append(evaluationString).append("\n");
        buffer.append("Repeat : ").append(topRepeats).append(" x ").append(folds).append(" x ").append(botRepeats).append("\n");
        buffer.append("Searches : \n\t");
        for (String s : searchesString) buffer.append(s).append("\t\t");
        buffer.append("(").append(searchesString.length).append(")\n");
        System.out.println(buffer);
    }

    public static void display(String dataset, String search) throws ClassNotFoundException {
        System.out.println(dataset + " - display - " + search);
        for (int t = 0; t < 2; t++) {
            for (int f = 0; f < 10; f++) {
                FeatureSelectionResult result = null;
                try {
                    result = load(t, search, dataset, evaluationString, f);
                } catch (IOException e) {
                    System.out.println(t + "\t" + f + "\t" + "null");
                }
                System.out.println(result);
            }
        }
    }

    public static void fancyDisplay(String dataset) throws ClassNotFoundException {
        StringBuilder buffer = new StringBuilder();
        buffer.append(dataset).append("\t");
        for (String s : searchesString) {
            Vector<FeatureSelectionDetailDouble> details = new Vector<>();
            int count = 0;
            search:
            for (int t = 0; t < topRepeats; t++) {
                for (int f = 0; f < folds; f++) {
                    FeatureSelectionResult result = null;
                    try {
                        result = load(t, s, dataset, evaluationString, f);
                        details.add(result.aggregate());
                    } catch (IOException e) {
                        //System.out.println(t + "\t" + f + "\t" + "null");
                        break search;
                    }
                }
            }
            FeatureSelectionDetailDouble merged = FeatureSelectionDetailDouble.merge(details);
            buffer.append(merged.getMerit()).append("\t").append(merged.getSize()).append("\t");
        }
        System.out.println(buffer.toString());
    }

    public static void displayClassification(String dataset, String search) throws ClassNotFoundException {
        for (String classifierString : classifierStrings) {
            System.out.println(dataset + " - display - " + search + " - " + classifierString);
            ClassificationResult result = null;
            try {
                result = load(classifierString, search, dataset, evaluationString);
            } catch (IOException e) {
                System.out.println("null");
            }
            System.out.println(result.summary() + "\n");
        }
    }

    public static String[] displayClassificationFancy(String dataset, String search) throws ClassNotFoundException {
        String[] returnStrings = new String[classifierStrings.length];
        for (int i = 0; i < classifierStrings.length; i++) {
            //System.out.println(dataset + " - display - " + search + " - " + classifierStrings[i]);
            ClassificationResult result = null;
            try {
                result = load(classifierStrings[i], search, dataset, evaluationString);
            } catch (IOException e) {
                System.out.println("null");
            }
            BigDecimal a = new BigDecimal(result.getSummary().getPercent_correct());
            a = a.divide(new BigDecimal("1"), 2, BigDecimal.ROUND_HALF_UP);
            returnStrings[i] = a.toPlainString();
        }
        return returnStrings;
    }

    public static void displayFeatureSelection(String dataset, String search) throws ClassNotFoundException {
        System.out.println(dataset + " - display - " + search + " - " + classifierString1);

        ClassificationResult result = null;
        try {
            result = load(classifierString1, search, dataset, evaluationString);
        } catch (IOException e) {
            System.out.println("null");
        }
        System.out.println(result.summary() + "\n");
    }
/*
    public static void delete(String dataset, String evaluationString) throws ClassNotFoundException {
        System.out.println(dataset + " - delete");
        //for (int t = 0; t < topRepeats; t++) {
        //for (int f = 0; f < folds; f++) {
        for (String s : searchesString) {
            File file = new File(Registry.surveyPath
                    + dataset + Registry.separator
                    + s + Registry.separator
                    //+ t + evaluationString + f);
                    + evaluationString + "_" + classifierString1);
            System.out.println(evaluationString + "_" + classifierString1 + "\t" + file.delete());
            file = new File(Registry.surveyPath
                    + dataset + Registry.separator
                    + s + Registry.separator
                    //+ t + evaluationString + f);
                    + evaluationString + "_" + classifierString2);
            System.out.println(evaluationString + "_" + classifierString2 + "\t" + file.delete());
            file = new File(Registry.surveyPath
                    + dataset + Registry.separator
                    + s + Registry.separator
                    //+ t + evaluationString + f);
                    + evaluationString + "_" + classifierString3);
            System.out.println(evaluationString + "_" + classifierString3 + "\t" + file.delete());
        }
        //}
        //}
    }*/

    public static void delete(String dataset, String evaluationString) throws ClassNotFoundException {
        System.out.println(dataset + " - delete");
        for (int t = 0; t < topRepeats; t++) {
            for (int f = 0; f < folds; f++) {
                for (String s : searchesString) {
                    File file = new File(Registry.surveyPath
                            + dataset + Registry.separator
                            + s + Registry.separator
                            + t + evaluationString + f);
                    System.out.println(evaluationString + "_" + "\t" + file.delete());
                }
            }
        }
    }

    public static void search(String dataset) throws Exception {

        System.out.println(dataset + " - search");

        for (int t = 0; t < topRepeats; t++) {

            CrossValidation crossValidation = new CrossValidation(dataset, t);
            long start, time;

            for (int f = foldOffset; f < folds; f++) {

                Instances training = crossValidation.getTrainingFold(f);
                if (evaluation instanceof CachedEvaluator) ((CachedEvaluator) evaluation).clear();
                evaluation.buildEvaluator(training);

                for (int s = 0; s < searches.size(); s++) {

                    if (!exist(dataset, searchesString[s], t, evaluationString, f)) {
                        FeatureSelectionResult result = new FeatureSelectionResult(dataset, t, f, botRepeats, searchesString[s], evaluationString);
                        if (evaluation instanceof InterruptingEvaluator) {
                            if (searches.get(s) instanceof HillClimber) ((InterruptingEvaluator) evaluation).turnOff();
                            else ((InterruptingEvaluator) evaluation).reset();
                        }
                        if (searches.get(s) instanceof AntColonySearch) {
                            ((AntColonySearch) searches.get(s)).reset();
                        }
                        int b;
                        double merit;
                        int[] repeat0 = null;
                        for (b = 0; b < botRepeats; b++) {
                            try {
                                ParameterConfigurator.setSeed(searches.get(s), random.nextInt());
                                if (searches.get(s) instanceof HarmonySearch) {
                                    ((HarmonySearch) searches.get(s)).setMusicianHint(Registry.getMusicianHint(dataset, evaluationString));
                                }
                                start = System.currentTimeMillis();
                                int[] subset = searches.get(s).search(evaluation, training);
                                if (b == 0) repeat0 = subset.clone();
                                time = System.currentTimeMillis() - start;
                                if (evaluation instanceof InterruptingEvaluator)
                                    ((InterruptingEvaluator) evaluation).reset();
                                merit = ((SubsetEvaluator) evaluation).evaluateSubset(NatureInspiredCommon.toBitSet(subset, training.classIndex()));
                                System.out.println(dataset + "\t" + t + "\t" + f + "\t" + b + "\t" + searchesString[s] + "\t" + merit + "\t" + subset.length + "\t" + time + "\t" + NatureInspiredCommon.getString(subset));
                                if ((b == 1) && Arrays.equals(subset, repeat0)) {
                                    for (int h = 1; h < botRepeats; h++)
                                        result.set(h, merit, subset.length, time, subset);
                                    System.out.println(dataset + "\t" + t + "\t" + f + "\tx\t" + searchesString[s] + "\t" + "Skipped Rest");
                                    break;
                                }
                                if ((searches.get(s) instanceof HillClimber) || (searches.get(s) instanceof TabuSearch)) {
                                    for (int h = 0; h < botRepeats; h++)
                                        result.set(h, merit, subset.length, time, subset);
                                    break;
                                } else result.set(b, merit, subset.length, time, subset);

                            } catch (NullPointerException e) {
                                System.err.println(dataset + "\t" + t + "\t" + f + "\t" + searchesString[s] + "\t" + "Null Pointer");
                                b--;
                            }
                        }
                        save(result);
                    }
                }
                System.gc();
            }
        }
    }

    /*public static void search(String dataset) throws Exception {
        System.out.println(dataset + " - search");

        for (int t = 0; t < topRepeats; t++) {
            CrossValidation crossValidation = new CrossValidation(dataset, t);
            long start, time;

            for (int f = 0; f < folds; f++) {
                Instances training = crossValidation.getTrainingFold(f);
                ((CachedEvaluator) evaluation).clear();
                evaluation.buildEvaluator(training);
                //int count = 0;

                for (int s = 0; s < searches.size(); s++) {
                    if (!exist(dataset, searchesString[s], t, evaluationString, f)) {
                        FeatureSelectionResult result = new FeatureSelectionResult(dataset, t, f, botRepeats, searchesString[s], evaluationString);
                        if (searches.get(s) instanceof HillClimber) ((InterruptingEvaluator) evaluation).turnOff();
                        else ((InterruptingEvaluator) evaluation).reset();
                        if (searches.get(s) instanceof AntColonySearch) {
                            ((AntColonySearch) searches.get(s)).reset();
                        }
                        int b = 0;
                        double merit = 0;
                        int[] repeat0 = null;
                        try {
                            for (b = 0; b < botRepeats; b++) {
                                ParameterConfigurator.setSeed(searches.get(s), random.nextInt());
                                if (searches.get(s) instanceof HarmonySearch) {
                                    ((HarmonySearch) searches.get(s)).setMusicianHint(Registry.getMusicianHint(dataset, "FRFS"));
                                }
                                start = System.currentTimeMillis();
                                int[] subset = searches.get(s).search(evaluation, training);
                                if (b == 0) repeat0 = subset.clone();
                                time = System.currentTimeMillis() - start;
                                ((InterruptingEvaluator) evaluation).reset();
                                merit = ((SubsetEvaluator) evaluation).evaluateSubset(toBitSet(subset, training.classIndex()));
                                System.out.println(dataset + "\t" + t + "\t" + f + "\t" + b + "\t" + searchesString[s] + "\t" + merit + "\t" + subset.length + "\t" + time + "\t" + ((CachedEvaluator) evaluation).size() + "\t" + getString(subset));
                                if ((b == 1) && Arrays.equals(subset, repeat0)) {
                                    for (int h = 1; h < botRepeats; h++)
                                        result.set(h, merit, subset.length, time, subset);
                                    System.out.println(dataset + "\t" + t + "\t" + f + "\tx\t" + searchesString[s] + "\t" + "Skipped Rest");
                                    break;
                                }
                                if ((searches.get(s) instanceof HillClimber) || (searches.get(s) instanceof TabuSearch)) {
                                    for (int h = 0; h < botRepeats; h++)
                                        result.set(h, merit, subset.length, time, subset);
                                    break;
                                } else result.set(b, merit, subset.length, time, subset);
                            }

                            save(result);
                        } catch (NullPointerException e) {
                            System.err.println(dataset + "\t" + t + "\t" + f + "\t" + searchesString[s] + "\t" + "Null Pointer");
                            *//*for (int h = b; h < botRepeats; h++)
                                result.set(h, merit, repeat0.length, 0, repeat0);
                            break;*//*
                        }
                    }
                }

                System.gc();
            }
        }
    }*/

/*    public static void searchOld(String dataset) throws Exception {
        System.out.println(dataset + " - search");
        for (int t = 0; t < topRepeats; t++) {
            CrossValidation crossValidation = new CrossValidation(dataset, false);
            long start, time;
            int count = 0;
            for (ASSearch search : searches) {
                if (!exist(t, dataset, searchesString[count], evaluationString)) {
                    FeatureSelectionResult result = new FeatureSelectionResult(dataset, topRepeats, folds, botRepeats, search.getClass().toString().replace("class weka.attributeSelection.", ""), evaluationString);
                    for (int f = 0; f < folds; f++) {
                        Instances training = crossValidation.getTrainingFold(f);
                        evaluation.buildEvaluator(training);
                        ((CachedEvaluator) evaluation).clear();
                        for (int b = 0; b < botRepeats; b++) {
                            ParameterConfigurator.setSeed(search, random.nextInt());
                            start = System.currentTimeMillis();
                            int[] subset = search.search(evaluation, training);
                            time = System.currentTimeMillis() - start;
                            ((InterruptingEvaluator) evaluation).reset();
                            double merit = ((SubsetEvaluator) evaluation).evaluateSubset(toBitSet(subset, training.classIndex()));
                            System.out.println(dataset + "\t" + t + "\t" + f + "\t" + b + "\t" + search.getClass().toString().replace("class weka.attributeSelection.", "") + "\t" + merit + "\t" + subset.length + "\t" + time + "\t" + getString(subset));
                            if ((search instanceof HillClimber) || (search instanceof TabuSearch)) {
                                for (int h = b; h < botRepeats; h++)
                                    result.set(t, f, h, merit, subset.length, time, subset);
                                break;
                            }
                            result.set(t, f, b, merit, subset.length, time, subset);
                        }
                    }
                    serialiseSave(searchesString[count], dataset, result, t);
                    System.gc();
                }
                count++;
            }
        }
    }*/

    public static boolean exist(String dataset, String search, int topRepeat, String evaluation, int fold) {
        if (!saveToDisk) return false;
        File file = new File(Registry.surveyPath
                + dataset + Registry.separator
                + search + Registry.separator
                + topRepeat + evaluation + fold);
        if (file.exists()) {
            System.out.println(dataset + "\t" + search + "\t" + topRepeat + "\t" + evaluation + "\t" + fold + "\tExist, Skipping ...");
            return true;
        }
        return false;
    }

    /*public static ClassificationResult[] validate(FeatureSelectionResult[] results) throws Exception {
        ClassificationResult[] classificationResults = new ClassificationResult[results.length];
        for (int r = 0; r < results.length; r++) {
            classificationResults[r] = new ClassificationResult(results[r], classifier);
            for (int t = 0; t < results[r].getTopRepeat(); t++) {
                CrossValidation crossValidation = new CrossValidation(results[r].getDataset(), t);
                for (int f = 0; f < folds; f++) {
                    Instances training = crossValidation.getTrainingFold(f);
                    Instances testing = crossValidation.getTestingFold(f);
                    for (int b = 0; b < botRepeats; b++) {
                        int[] subset = results[r].get(t, f, b).getSubset();
                        //System.out.println(ClassifierCrossValidator.validateFilteredClassifierComprehensive(classifier, subset, training, testing));
                    }
                }
            }
            System.gc();
        }
        return classificationResults;
    }*/

    public static void validate(String dataset) throws Exception {
        System.out.println(dataset + " - validate");

        for (String s : searchesString) {
            System.out.println(dataset + " - " + s);
            //ClassificationResult classificationResult1 = new ClassificationResult(dataset, s, topRepeats, folds, botRepeats, classifierString1, evaluationString);
            ClassificationResult classificationResult2 = new ClassificationResult(dataset, s, topRepeats, folds, botRepeats, classifierString2, evaluationString);
            //ClassificationResult classificationResult3 = new ClassificationResult(dataset, s, topRepeats, folds, botRepeats, classifierString3, evaluationString);

            for (int t = 0; t < topRepeats; t++) {
                CrossValidation crossValidation = new CrossValidation(dataset, t);

                for (int f = 0; f < folds; f++) {
                    FeatureSelectionResult result = load(t, s, dataset, evaluationString, f);
                    Instances training = crossValidation.getTrainingFold(f);
                    Instances testing = crossValidation.getTestingFold(f);

                    for (int b = 0; b < botRepeats; b++) {
                        int[] subset = result.get(b).getSubset();
                        //classificationResult1.set(t, f, b, ClassifierCrossValidator.validateFilteredClassifierComprehensive(classifier1, subset, training, testing));
                        classificationResult2.set(t, f, b, ClassifierCrossValidator.validateFilteredClassifierComprehensive(classifier2, subset, training, testing));
                        //classificationResult3.set(t, f, b, ClassifierCrossValidator.validateFilteredClassifierComprehensive(classifier3, subset, training, testing));
                    }
                    System.out.println(dataset + " - " + s + "\t" + t + "\t" + f);
                }

                System.gc();
            }
            //System.out.println(classificationResult1.summary());
            //save(classificationResult1);
            System.out.println(classificationResult2.summary());
            save(classificationResult2);
            //System.out.println(classificationResult3.summary());
            //save(classificationResult3);
        }
    }

    private static void save(ClassificationResult result) throws IOException {
        if (!saveToDisk) return;
        FileOutputStream fileOutputStream = new FileOutputStream(Registry.surveyPath
                + result.getDataset() + Registry.separator
                + result.getSearch() + Registry.separator
                + result.getEvaluator() + "_" + result.getClassifier());
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(result);
        objectOutputStream.close();
    }

    private static void saveRaw(ClassificationResult result) throws IOException {
        if (!saveToDisk) return;
        FileOutputStream fileOutputStream = new FileOutputStream(Registry.surveyPath
                + result.getDataset() + Registry.separator
                + "raw" + Registry.separator
                + "raw" + "_" + result.getClassifier());
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(result);
        objectOutputStream.close();
    }

    private static void save(FeatureSelectionResult result) throws IOException {
        if (!saveToDisk) return;
        FileOutputStream fileOutputStream = new FileOutputStream(Registry.surveyPath
                + result.getDataset() + Registry.separator
                + result.getSearch() + Registry.separator
                + result.getTopRepeat() + result.getEvaluator() + result.getFold());
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(result);
        objectOutputStream.close();
    }

    public static void validateRaw(String dataset) throws Exception {
        System.out.println(dataset + " - validate raw");
        ClassificationResult classificationResult1 = new ClassificationResult(dataset, "raw", topRepeats, folds, 1, classifierString1, "raw");
        ClassificationResult classificationResult2 = new ClassificationResult(dataset, "raw", topRepeats, folds, 1, classifierString2, "raw");
        ClassificationResult classificationResult3 = new ClassificationResult(dataset, "raw", topRepeats, folds, 1, classifierString3, "raw");
        for (int t = 0; t < topRepeats; t++) {
            CrossValidation crossValidation = new CrossValidation(dataset, t);
            for (int f = 0; f < folds; f++) {
                Instances training = crossValidation.getTrainingFold(f);
                Instances testing = crossValidation.getTestingFold(f);
                System.out.println(dataset + "\t" + t + "\t" + f + "\t");
                classificationResult1.set(t, f, 0, ClassifierCrossValidator.validateRawClassifierComprehensive(classifier1, training, testing));
                classificationResult2.set(t, f, 0, ClassifierCrossValidator.validateRawClassifierComprehensive(classifier2, training, testing));
                classificationResult3.set(t, f, 0, ClassifierCrossValidator.validateRawClassifierComprehensive(classifier3, training, testing));
                System.out.println(dataset + " - " + "raw" + "\t" + t + "\t" + f);
            }
        }
        System.out.println(classificationResult1.summary());
        saveRaw(classificationResult1);
        System.out.println(classificationResult2.summary());
        saveRaw(classificationResult2);
        System.out.println(classificationResult3.summary());
        saveRaw(classificationResult3);
        System.gc();
    }

    public static void applySettings() {
        datasets = Registry.getDatasets(datasetsString);
        classifier1 = Registry.getClassifier(classifierString1);
        classifier2 = Registry.getClassifier(classifierString2);
        classifier3 = Registry.getClassifier(classifierString3);
        evaluation = Registry.getEvaluation(evaluationString);
        searches = Registry.getSearches(searchesString);
    }

    /*private static void serialiseSave(String dataset, FeatureSelectionResult[] featureSelectionResults) throws IOException {
        *//*DateFormat dateFormatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.UK);
        FileOutputStream fileOutputStream = new FileOutputStream(Registry.surveyPath + dataset + "_" + evaluationString + "_(" + searches.size() + ")_" + dateFormatter.format((new Date())));*//*
        if (!saveToDisk) return;
        FileOutputStream fileOutputStream = new FileOutputStream(Registry.surveyPath + dataset + "_" + evaluationString + "_(" + featureSelectionResults.length + ")");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(featureSelectionResults);
        objectOutputStream.close();
    }*/

    private static void serialiseSave(String search, String dataset, FeatureSelectionResult featureSelectionResults, int repeat) throws IOException {
        if (!saveToDisk) return;
        FileOutputStream fileOutputStream = new FileOutputStream(Registry.surveyPath + repeat + Registry.separator + search + Registry.separator + dataset + Registry.separator + "" + evaluationString);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(featureSelectionResults);
        objectOutputStream.close();
    }


    /*private static void serialiseSave(String dataset, ClassificationResult[] classificationResults) throws IOException {
        *//*DateFormat dateFormatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.UK);
        FileOutputStream fileOutputStream = new FileOutputStream(Registry.surveyPath + dataset + "_" + evaluationString + "_(" + searches.size() + ")_" + dateFormatter.format((new Date())));*//*
        if (!saveToDisk) return;
        FileOutputStream fileOutputStream = new FileOutputStream(Registry.surveyPath + dataset + "_" + classifier + "_(" + searches.size() + ")");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(classificationResults);
        objectOutputStream.close();
    }*/

    /*private static void serialiseSave(String dataset, ClassificationResult classificationResult) throws IOException {
        *//*DateFormat dateFormatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.UK);
        FileOutputStream fileOutputStream = new FileOutputStream(Registry.surveyPath + dataset + "_" + evaluationString + "_(" + searches.size() + ")_" + dateFormatter.format((new Date())));*//*
        if (!saveToDisk) return;
        FileOutputStream fileOutputStream = new FileOutputStream(Registry.surveyPath + dataset + "_" + classifier + "_(raw)");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(classificationResult);
        objectOutputStream.close();
    }*/

    private static void serialiseSave(String classifier, String search, String dataset, ClassificationResult classificationResult, int repeat) throws IOException {
        /*DateFormat dateFormatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.UK);
        FileOutputStream fileOutputStream = new FileOutputStream(Registry.surveyPath + dataset + "_" + evaluationString + "_(" + searches.size() + ")_" + dateFormatter.format((new Date())));*/
        if (!saveToDisk) return;
        FileOutputStream fileOutputStream = new FileOutputStream(Registry.surveyPath + repeat + Registry.separator + search + Registry.separator + dataset + Registry.separator + "" + classifier);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(classificationResult);
        objectOutputStream.close();
    }

    /*private static FeatureSelectionResult[] serialiseLoadFeatureSelectionResult(String dataset) throws IOException, ClassNotFoundException {
        FeatureSelectionResult[] featureSelectionResults;
        FileInputStream fileInputStream = new FileInputStream(Registry.surveyPath + dataset + "_" + evaluationString + "_(" + searches.size() + ")");
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        featureSelectionResults = (FeatureSelectionResult[]) objectInputStream.readObject();
        objectInputStream.close();
        return featureSelectionResults;
    }*/

    private static FeatureSelectionResult serialiseLoadFeatureSelectionResult(String search, String dataset, int repeat) throws IOException, ClassNotFoundException {
        FeatureSelectionResult featureSelectionResult;
        FileInputStream fileInputStream = new FileInputStream(Registry.surveyPath + repeat + Registry.separator + search + Registry.separator + dataset + Registry.separator + evaluationString);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        featureSelectionResult = (FeatureSelectionResult) objectInputStream.readObject();
        objectInputStream.close();
        return featureSelectionResult;
    }

    private static FeatureSelectionResult load(int topRepeat, String search, String dataset, String evaluation, int fold) throws IOException, ClassNotFoundException {
        FeatureSelectionResult featureSelectionResult;
        FileInputStream fileInputStream = new FileInputStream(Registry.surveyPath
                + dataset + Registry.separator
                + search + Registry.separator
                + topRepeat + evaluation + fold);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        featureSelectionResult = (FeatureSelectionResult) objectInputStream.readObject();
        objectInputStream.close();
        return featureSelectionResult;
    }

    private static ClassificationResult load(String classifierString, String search, String dataset, String evaluation) throws IOException, ClassNotFoundException {
        ClassificationResult result;
        FileInputStream fileInputStream;
        if (search.equals("raw")) {
            fileInputStream = new FileInputStream(Registry.surveyPath
                    + dataset + Registry.separator
                    + search + Registry.separator
                    + "raw" + "_" + classifierString);
        } else {
            fileInputStream = new FileInputStream(Registry.surveyPath
                    + dataset + Registry.separator
                    + search + Registry.separator
                    + evaluation + "_" + classifierString);
        }
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        result = (ClassificationResult) objectInputStream.readObject();
        objectInputStream.close();
        return result;
    }

    /*private static ClassificationResult[] serialiseLoadClassificationResult(String dataset) throws IOException, ClassNotFoundException {
        ClassificationResult[] featureSelectionResults;
        FileInputStream fileInputStream = new FileInputStream(Registry.surveyPath + dataset + "_" + classifier + "_(" + searches.size() + ")");
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        featureSelectionResults = (ClassificationResult[]) objectInputStream.readObject();
        objectInputStream.close();
        return featureSelectionResults;
    }*/

    private static ClassificationResult serialiseLoadClassificationResultRaw(String classifier, String dataset) throws IOException, ClassNotFoundException {
        ClassificationResult featureSelectionResults;
        FileInputStream fileInputStream = new FileInputStream(Registry.surveyPath + dataset + "_" + classifier + "_(raw)");
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        featureSelectionResults = (ClassificationResult) objectInputStream.readObject();
        objectInputStream.close();
        return featureSelectionResults;
    }

    private static void buildDirectories(String[] datasets, String[] searches) {
        File file = new File(Registry.surveyPath);
        if (!file.exists()) {
            file.mkdir();
            System.out.println("Result Directory Built");
        }
        for (String dataset : datasets) {
            String datasetDirectory = Registry.surveyPath + System.getProperty("file.separator") + dataset;
            file = new File(datasetDirectory);
            if (!file.exists()) {
                file.mkdir();
                System.out.println("--- " + "Directory Built for " + dataset);
            }
            for (String search : searches) {
                String searchDirectory = datasetDirectory + System.getProperty("file.separator") + search;
                file = new File(searchDirectory);
                if (!file.exists()) {
                    file.mkdir();
                    System.out.println("--- --- " + search + " Directory Built");
                }
            }
            String rawDirectory = datasetDirectory + System.getProperty("file.separator") + "raw";
            file = new File(rawDirectory);
            if (!file.exists()) {
                file.mkdir();
                System.out.println("--- --- " + "raw" + " Directory Built");
            }
        }
        /*File file = new File(Registry.surveyPath);
        if (!file.exists()) {
            file.mkdir();
            System.out.println("Result Directory Built");
        }
        for (int r = 0; r < repeats; r++) {
            String repeatDirectory = Registry.surveyPath + r;
            file = new File(repeatDirectory);
            if (!file.exists()) {
                file.mkdir();
                System.out.println(r + " Directory Built");
            }
            for (String search : searches) {
                String searchDirectory = repeatDirectory + System.getProperty("file.separator") + search;
                file = new File(searchDirectory);
                if (!file.exists()) {
                    file.mkdir();
                    System.out.println(r + " --- " + search + " Directory Built");
                }
                for (String dataset : datasets) {
                    String datasetDirectory = searchDirectory + System.getProperty("file.separator") + dataset;
                    file = new File(datasetDirectory);
                    if (!file.exists()) {
                        file.mkdir();
                        System.out.println(r + " --- --- " + "Directory Built for " + dataset);
                    }
                }
            }
            String searchDirectory = repeatDirectory + System.getProperty("file.separator") + "raw";
            file = new File(searchDirectory);
            if (!file.exists()) {
                file.mkdir();
                System.out.println(r + " --- " + "raw" + " Directory Built");
            }
            for (String dataset : datasets) {
                String datasetDirectory = searchDirectory + System.getProperty("file.separator") + dataset;
                file = new File(datasetDirectory);
                if (!file.exists()) {
                    file.mkdir();
                    System.out.println(r + " --- --- " + "Directory Built for " + dataset);
                }
            }
        }*/
    }
}
