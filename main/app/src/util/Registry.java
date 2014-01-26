package util;

import weka.attributeSelection.*;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.SMO;
import weka.classifiers.rules.PART;
import weka.classifiers.trees.J48;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 27/01/12
 * Time: 11:52
 */
public class Registry {

    public static final String separator = System.getProperty("file.separator");
    public static final String root =
            ".." + separator + ".." + separator;
    public static final String datasetPath =
            root + "data" + separator;
    public static final String serialisationPath =
            root + "papers\\ces_journal\\output\\";
    public static final String surveyPath =
            root + "papers" + separator + "nifs_survey" + separator + "results" + separator;
    public static final String crossValidationCachePath =
            root + "data" + separator + "cache" + separator;
    public static final String ensembleCachePath =
            root + "data\\artificial\\";
    public static final String[] ensembleMethods = new String[]{
            "bagging",
            "subspace"
    };
    public static final int[] ensembleSizes = new int[]{
            //50,
            //100,
            200,
    };
    public static final Classifier[] ensembleClassifiers = new Classifier[]{
            new J48(),
            new NaiveBayes(),
            new PART(),
            new SMO(),
    };
    public static final String J48 = "weka.classifiers.trees.J48";
    public static final String PART = "weka.classifiers.rules.PART";
    public static final String JRIP = "weka.classifiers.rules.JRip";
    public static final String NN = "weka.classifiers.lazy.IBk";
    public static final String VQNN = "weka.classifiers.fuzzy.VQNN";
    public static final String NB = "weka.classifiers.bayes.NaiveBayes";

    public static final int FA = 1;
    public static final int FR = 2;
    public static final int IA = 3;
    public static final int IR = 4;
    public static final int AR = 0;

    public static String[] getDatasets(String keyword) {
        //System.out.println("Dataset\t" + keyword);
        switch (keyword) {
            case "common":
                return Dataset.common;
            case "simple":
                return Dataset.simple;
            case "CES":
                return Dataset.CES;
            case "CES1":
                return Dataset.CES1;
            case "CESM":
                return Dataset.CESM;
            case "CES2":
                return Dataset.CES2;
            case "CESLOW":
                return Dataset.CESLOW;
            case "CESHIGH":
                return Dataset.CESHIGH;
            case "hd":
                return Dataset.hd;
            case "sizeComparison":
                return Dataset.sizeComparison;
            case "ijcai":
                return Dataset.ijcai;
            default:
                return Dataset.simple;
        }
    }

    public static String getClassifier(String keyword) {
        //System.out.println("Classifier\t" + keyword);
        switch (keyword) {
            case "J48":
                return J48;
            case "PART":
                return PART;
            case "JRIP":
                return JRIP;
            case "NN":
                return NN;
            case "VQNN":
                return VQNN;
            case "NB":
                return NB;
            default:
                return J48;
        }
    }

    public static ASEvaluation getEvaluation(String keyword) {
        //System.out.println("Evaluation\t" + keyword);
        switch (keyword) {
            case "CFS":
                return new CfsSubsetEval();
            case "PCFS":
                return new ConsistencySubsetEval();
            case "FRFS":
                return new FuzzyRoughSubsetEval();
            case "CHI":
                return new ChiSquaredAttributeEval();
            case "RELIEF":
                return new ReliefFAttributeEval();
            case "IG":
                return new InfoGainAttributeEval();
            case "SU":
                return new SymmetricalUncertAttributeEval();
            case "WP":
                return new WrapperSubsetEval();
            default:
                return new CfsSubsetEval();
        }
    }

    public static int getMusicianHint(String datasetName, String evaluation) {
        if (evaluation.equals("weka.attributeSelection.FuzzyRoughSubsetEval")) evaluation = "FRFS";
        else if (evaluation.equals("weka.attributeSelection.CfsSubsetEval")) evaluation = "CFS";
        else if (evaluation.equals("weka.attributeSelection.ConsistencySubsetEval")) evaluation = "PCFS";
        //System.out.println(evaluation);
        switch (datasetName) {
            case "arrhythmia":
                if (evaluation.equals("CFS")) return 40;
                else if (evaluation.equals("PCFS")) return 60;
                else if (evaluation.equals("FRFS")) return 20;
                else return 40;
            case "cleveland":
                if (evaluation.equals("CFS")) return 10;
                else if (evaluation.equals("PCFS")) return 10;
                else if (evaluation.equals("FRFS")) return 10;
                else return 10;
            case "ecoli":
                if (evaluation.equals("CFS")) return 10;
                else if (evaluation.equals("PCFS")) return 10;
                else if (evaluation.equals("FRFS")) return 10;
                else return 10;
            case "glass":
                if (evaluation.equals("CFS")) return 10;
                else if (evaluation.equals("PCFS")) return 10;
                else if (evaluation.equals("FRFS")) return 10;
                else return 10;
            case "handwritten":
                if (evaluation.equals("CFS")) return 100;
                else if (evaluation.equals("PCFS")) return 30;
                else if (evaluation.equals("FRFS")) return 25;
                else return 100;
            case "ionosphere":
                if (evaluation.equals("CFS")) return 15;
                else if (evaluation.equals("PCFS")) return 10;
                else if (evaluation.equals("FRFS")) return 10;
                else return 12;
            case "libras":
                if (evaluation.equals("CFS")) return 40;
                else if (evaluation.equals("PCFS")) return 25;
                else if (evaluation.equals("FRFS")) return 10;
                else return 25;
            case "multifeat":
                if (evaluation.equals("CFS")) return 200;
                else if (evaluation.equals("PCFS")) return 20;
                else if (evaluation.equals("FRFS")) return 25;
                else return 50;
            case "ozone":
                if (evaluation.equals("CFS")) return 30;
                else if (evaluation.equals("PCFS")) return 25;
                else if (evaluation.equals("FRFS")) return 25;
                else return 25;
            case "secom":
                if (evaluation.equals("CFS")) return 30;
                else if (evaluation.equals("PCFS")) return 100;
                else if (evaluation.equals("FRFS")) return 30;
                else return 50;
            case "sonar":
                if (evaluation.equals("CFS")) return 25;
                else if (evaluation.equals("PCFS")) return 15;
                else if (evaluation.equals("FRFS")) return 10;
                else return 20;
            case "water3":
                if (evaluation.equals("CFS")) return 15;
                else if (evaluation.equals("PCFS")) return 15;
                else if (evaluation.equals("FRFS")) return 10;
                else return 15;
            case "waveform":
                if (evaluation.equals("CFS")) return 20;
                else if (evaluation.equals("PCFS")) return 15;
                else if (evaluation.equals("FRFS")) return 20;
                else return 20;
            default:
                return 0;
        }
    }

    public static ASSearch getSearch(String keyword) {
        switch (keyword) {
            case "ACO":
                return new AntColonySearch();
            case "ABC":
                return new ArtificialBeeColonySearch();
            case "AIS":
                return new ArtificialImmuneSystemSearch();
            case "FF":
                return new FireflySearch();
            case "GA":
                return new GeneticSearch();
            case "GS":
                return new GreedyStepwise();
            case "HC":
                return new HillClimber();
            case "HS":
                return new HarmonySearch();
            case "MS":
                return new MemeticSearch();
            case "PSO":
                return new PSOSearch();
            case "SA":
                return new SimulatedAnnealingSearch();
            case "SS":
                return new ScatterSearchV1();
            case "RS":
                return new RandomSearch();
            case "TS":
                return new TabuSearch();
            default:
                return new HarmonySearch();
        }
    }

    public static Vector<ASSearch> getSearches(String[] keywords) {
        Vector<ASSearch> searches = new Vector<>();
        for (String s : keywords) searches.add(getSearch(s));
        return searches;
    }
}
