package util;

import weka.attributeSelection.ASEvaluation;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * User: rrd09
 * Date: 09/11/11
 * Time: 15:08
 */
public class Dataset {

    public static Instances getDataset(String fileName) throws IOException {
        File file = new File(Registry.datasetPath + fileName + ".arff");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        Instances data = new Instances(reader);
        reader.close();
        data.setClassIndex(data.numAttributes() - 1);
        return data;
    }

    public static ASEvaluation getASEvaluation(String asEvaluationName) {
        ASEvaluation asEvaluation = null;
        try {
            asEvaluation = (ASEvaluation) Class.forName("weka.attributeSelection." + asEvaluationName).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return asEvaluation;
    }

    public static ASEvaluation getASEvaluation(String asEvaluationName, Instances instances) {
        ASEvaluation asEvaluation = null;
        try {
            asEvaluation = (ASEvaluation) Class.forName("weka.attributeSelection." + asEvaluationName).newInstance();
            asEvaluation.buildEvaluator(instances);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return asEvaluation;
    }

    public static String[] hdDatasets = new String[]{"isolet", "multiplefeature", "secom", "waveform", "web",};

    public static final String[] hd = new String[]{
            "ionosphere",
            "water3",
            "waveform",
            "sonar",
            "ozone",
            "libras",
            "arrhythmia",
            "secom",
            //"isolet",
            "multifeat",
    };

    public static String[] ijcai = new String[]{
            "arrhythmia",
            "handwritten",
            "multifeat",
            "ozone",
            "secom",
    };


    public static final String[] CES = new String[]{
//            "arrhythmia",
//            "cleveland",
//            "ecoli",
//            "glass",
//            "handwritten",
//            "heart",
//            "ionosphere",
//            "libras",
//            "multifeat",
//            "ozone",
            "secom",
            "sonar",
            "water",
            "waveform",
    };
    public static final String[] CES2 = new String[]{
            //"arrhythmia",
//            "cleveland",
//            "ecoli",
//            "glass",
            //"handwritten",
//            "heart",
//            "ionosphere",
//            "libras",
//            "multifeat",
//            "ozone",
            //"secom",
//            "sonar",
//            "water",
            "waveform",
    };
    public static final String[] CES1 = new String[]{
            "ozone",
//            "secom",
//            "sonar",
//            "water",
//            "waveform",
    };
    public static final String[] CESLOW = new String[]{
            "sonar",
            "water"
            //"ozone",
            //"secom",
            //"waveform",
            /*"cleveland",
            "ecoli",
            "glass",
            "heart",*/
            /*"ionosphere",
            "libras",
            "sonar",
            "water",*/
    };
    public static final String[] CESHIGH = new String[]{
            //"arrhythmia",
            //"handwritten",
            //"ecoli",
            "multifeat",
    };

    public static final String[] sizeComparison = new String[]{
            /*"arrhythmia",
            "handwritten",
            "libras",
            "multifeat",*/
            "sonar",
            "waveform",
    };

    public static final String[] CESM = new String[]{
            "multifeat",
            "ozone",
            "secom",
            "sonar",
            "water3",
            "waveform",
    };

    public static final String[] featureSubsetEnsemble = new String[]{
            "arrhythmia",
            "cleveland",
            "ecoli",
            "glass",
            "ionosphere",
            "isolet",
            "libras",
            "multifeat",
            "ozone",
            "secom",
            "sonar",
            "water3",
            "waveform",
            //"isolet",
            //"multifeat",
    };

    public static final String[] turing_high = new String[]{
            //"waveform",
            "arrhythmia",
//            "ozone",
//            "libras",

            "secom",
//            "isolet",
            "multifeat",
    };

    public static final String[] turing_vh = new String[]{
            //"waveform",
            "secom",
            "isolet",
            "multifeat",
    };

    public static final String[] hdExtra = new String[]{"arrhythmia", "secom", "isolet", "multifeat",};

    public static String[] frDatasets = new String[]{"ionosphere", "3-completed",
            //"ozone",
            //"secom",
            //"multifeat",
            "sonar", "libras", "arrhythmia",};

    public static String[] count10 = new String[]{"waveform", "secom", "isolet", "multifeat",

    };

    public static String[] count5 = new String[]{

            "ozone", "libras", "arrhythmia",};

    public static String[] conDatasets = new String[]{
            //"arrhythmia",
            //"hillvalley",
            //"ionosphere",
            //"isolet",
            //"libras",
            "multifeat",
            //"ozone",
            //"secom",
            //"sonar",
            //"3-completed",
            //"waveform",
    };

    public static String[] wisconsin = new String[]{"multifeat"};

    public static String[] smcb = new String[]{"ionosphere", "water3", "waveform", "sonar", "ozone", "libras", "arrhythmia", "secom", "isolet", "multifeat",};

    public static String[] cfsDatasets = new String[]{"arrhythmia",
            //"hillvalley",
            "ionosphere",
            //"isolet",
            "libras",
            //"multifeat",
            "ozone",
            //"secom",
            "sonar", "3-completed",
            //"waveform",
    };

    public static String[] fullDatasets = new String[]{"2-completed", "3-completed", "arrhythmia", "cleveland", "ecoli", "glassScaled", "heart", "ionosphere", "olitos", "web", "wineScaled", "wisconsin"};

    public static String[] common = new String[]{
            "cleveland",
            "ecoli",
            "glass", "heart", "ionosphere",
            //"sonar",
            //"2-completed",
            //"3-completed",
            "wine",
            //"wisconsin"
    };

    public static String[] complexDatasets = new String[]{"arrhythmia", "ionosphere", "olitos", "sonar", "2-completed", "3-completed",};

    public static String[] exampleDatasets = new String[]{"ionosphere",};

    public static String[] simple = new String[]{
            //"water2",
            //"water3",
            //"ecoli",
            //"wisconsin",
            //"cleveland",
            "glass",
            "heart",
            "ionosphere",
            "olitos",
            "sonar",
            "water3",
            "wine",
            //"web",
            //"water2",

    };

    public static String[] final9 = new String[]{
            //"cleveland",
            /*"glass",
            "heart",
            "ionosphere",
            "olitos",
            "water2",
            "water3",
            "web",*/
            "wine"};

    public static String[] hard = new String[]{"libras", "arrhythmia"};

}
