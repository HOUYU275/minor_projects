package online;

import weka.attributeSelection.*;

import java.util.ArrayList;
import java.util.BitSet;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 07/01/13
 * Time: 14:55
 */
public class TestOnlineFeatureSelection {

    /*arrhythmia & 280 & 452 & 16 & 65.97 & 61.40 & 61.55\\
    handwritten & 257 & 1593 & 10 & 75.74 & 86.21 & 77.68\\
    libras & 91 & 360 & 15 & 68.24 & 63.63 & 67.25\\
    multifeat & 650 & 2000 & 10 & 94.54 & 95.30 & 98.03\\
    ozone & 73 & 2534 & 2 & 92.70 & 67.66 & 93.69\\
    secom & 591 & 1567 & 2 & 89.56 & 30.04 & 93.36\\
    waveform & 41 & 699 & 2 & 75.49 & 79.99 & 79.49\\*/

/*
    private InstanceCreator creator;
    private HillClimber HC = new HillClimber();
    private HarmonySearch HS = new HarmonySearch();
    private StringBuffer buffer;
    //private int repeat = 20;
    private String mode = "FR";
    private static String evaluationString = "FRFS";

    private static String[] datasets = new String[]{
//            "arrhythmia",
//            "handwritten",
//            "libras",
//            "multifeat",
//            "ozone",
            "secom",
    };

    public static void main(String[] args) throws Exception {
        TestOnlineFeatureSelection test = new TestOnlineFeatureSelection();
        for (String dataset : datasets) test.work(dataset);
        System.exit(0);
    }

    public void work(String dataset) throws Exception {
        buffer = new StringBuffer();
        buffer.append(dataset).append("\n");
        buffer.append(mode).append("\n");
        creator = new InstanceCreator(dataset);
        initialise(mode);
        full();
        int[] result = evaluate(HS);
        int repeat = 20;
        while (repeat-- > 0) result = simulate(result, mode);
        terminate(result, mode);
        System.out.println(buffer.toString() + "\n");

    }

    private int[] simulate(int[] result, String mode) throws Exception {
        switch (mode) {
            case "FA":
                System.out.print(creator.addFeatures(creator.getRandom().nextInt((creator.getOriginal().numAttributes() - creator.getCreated().numAttributes()) / 5)));
                break;
            case "FR":
                System.out.print(creator.removeFeatures(creator.getRandom().nextInt(creator.getCreated().numAttributes() / 5)));
                break;
            case "IA":
                System.out.print(creator.addInstances(creator.getRandom().nextInt((creator.getOriginal().numInstances() - creator.getCreated().numInstances()) / 5)));
                break;
            case "IR":
                System.out.print(creator.removeInstances(creator.getRandom().nextInt(creator.getCreated().numInstances() / 5)));
                break;
        }
        System.out.println();
        creator.create();
        System.out.println();
        System.out.println("Created: " + (creator.getCreated().numAttributes() - 1) + " features (" + creator.getCreated().classIndex() + "), " + creator.getCreated().numInstances() + " instances.");
        buffer.append(creator.getCreated().numAttributes() - 1).append("\t").append(creator.getCreated().numInstances()).append("\t");
        result = creator.convert(result);
        creator.build();
        double merit = creator.getEvaluation().evaluateSubset(NatureInspiredCommon.toBitSet(result, creator.getCreated().classIndex()));
        System.out.println("Converted Subset = " + NatureInspiredCommon.getString(result) + " (" + result.length + ") " + merit);
        buffer.append(merit).append("\t").append(result.length).append("\t").append(creator.test(result, "J48")).append("\t").append(creator.test("J48")).append("\t").append(NatureInspiredCommon.getString(result)).append("\n");
        double fullMerit = full();
        if (merit != fullMerit) result = online(result);
        System.out.println();
        return result;
    }

    private int[] terminate(int[] result, String mode) throws Exception {
        switch (mode) {
            case "FA":
                creator.fullFeatureMask();
                break;
            case "FR":
                break;
            case "IA":
                creator.fullInstanceMask();
                break;
            case "IR":
                break;
        }
        System.out.println();
        creator.create();
        System.out.println();
        System.out.println("Created: " + (creator.getCreated().numAttributes() - 1) + " features (" + creator.getCreated().classIndex() + "), " + creator.getCreated().numInstances() + " instances.");
        buffer.append(creator.getCreated().numAttributes() - 1).append("\t").append(creator.getCreated().numInstances()).append("\t");
        result = creator.convert(result);
        creator.build();
        double merit = creator.getEvaluation().evaluateSubset(NatureInspiredCommon.toBitSet(result, creator.getCreated().classIndex()));
        System.out.println("Converted Subset = " + NatureInspiredCommon.getString(result) + " (" + result.length + ") " + merit);
        buffer.append(merit).append("\t").append(result.length).append("\t").append(creator.test(result, "J48")).append("\t").append(creator.test("J48")).append("\t").append(NatureInspiredCommon.getString(result)).append("\n");
        //result = evaluate(HC);
        double fullMerit = full();
        if (merit != fullMerit) {
            result = online(result);
            //result = evaluate(HC);
        }
        //full();
        System.out.println();
        return result;
    }

    private void initialise(String mode) throws Exception {
        System.out.println("Original: " + (creator.getOriginal().numAttributes() - 1) + " features (" + creator.getOriginal().classIndex() + "), " + creator.getOriginal().numInstances() + " instances.");
        switch (mode) {
            case "FA":
                creator.randomFeatureMask(creator.getOriginal().numAttributes() / 10);
                creator.fullInstanceMask();
                break;
            case "FR":
                creator.fullFeatureMask();
                creator.fullInstanceMask();
                break;
            case "IA":
                creator.fullFeatureMask();
                creator.randomInstanceMask(creator.getOriginal().numInstances() / 10);
                break;
            case "IR":
                creator.fullFeatureMask();
                creator.fullInstanceMask();
                break;
        }
        creator.create();
        System.out.println("Created: " + (creator.getCreated().numAttributes() - 1) + " features (" + creator.getCreated().classIndex() + "), " + creator.getCreated().numInstances() + " instances.");
    }

    private double full() throws Exception {
        creator.build();
        int[] fullSubset = NatureInspiredCommon.fullSubset(creator.getCreated().numAttributes() - 1);
        double originalFullMerit = creator.getEvaluation().evaluateSubset(NatureInspiredCommon.toBitSet(fullSubset, creator.getCreated().classIndex()));
        System.out.println("Full Merit = " + originalFullMerit + " (" + fullSubset.length + ")");
        return originalFullMerit;
    }

    private int[] evaluate(ASSearch search) throws Exception {
        //HillClimber search = new HillClimber();
        //HarmonySearch search = new HarmonySearch();
        int[] result = search.search((ASEvaluation) creator.getEvaluation(), creator.getCreated());
        //((InterruptingEvaluator) evaluation).reset();
        double merit = ((SubsetEvaluator) creator.getEvaluation()).evaluateSubset(NatureInspiredCommon.toBitSet(result, creator.getCreated().classIndex()));
        System.out.println("Selected Subset = " + NatureInspiredCommon.getString(result) + " (" + result.length + ") " + merit);
        buffer.append(creator.getCreated().numAttributes() - 1).append("\t").append(creator.getCreated().numInstances()).append("\t");
        buffer.append(merit).append("\t").append(result.length).append("\t").append(creator.test(result, "J48")).append("\t").append(creator.test("J48")).append("\t").append(NatureInspiredCommon.getString(result)).append("\n");
        return result;
    }

    public int[] online(int[] result) throws Exception {
        BitSet current = NatureInspiredCommon.toBitSet(result, creator.getCreated().classIndex());
        ArrayList<Integer> newFeatures = creator.getFeatureAdditions();
        */
/*Instances newInstances = creator.getInstanceAdditions(true);
        if (newInstances.numInstances() > 0) {
            System.out.println("New Instances : " + ((Gamma)((FuzzyRoughSubsetEval) evaluation).getMeasure()).calculate(current, newInstances));
            newInstances = creator.getInstanceAdditions(false);
            System.out.println("Old Instances : " + ((Gamma)((FuzzyRoughSubsetEval) evaluation).getMeasure()).calculate(current, newInstances));
        }*//*

        int bestIndex = -1;
        double bestMerit = ((SubsetEvaluator) creator.getEvaluation()).evaluateSubset(current);
        //System.out.println(additions.size());
        if (newFeatures.size() == 0)
            newFeatures = creator.getUnselectedFeatures(current, creator.getCreated().numAttributes() - 1);
        double fullMerit = full();
        outer:
        while (!newFeatures.isEmpty()) {
            bestIndex = -1;
            for (int i = 0; i < newFeatures.size(); i++) {
                current.set(newFeatures.get(i));
                double merit = ((SubsetEvaluator) creator.getEvaluation()).evaluateSubset(current);
                if (merit == fullMerit) {
                    bestMerit = merit;
                    current.set(newFeatures.get(i));
                    System.out.println("+ " + newFeatures.get(i) + " @ " + bestMerit);
                    break outer;
                }
                if (merit > bestMerit) {
                    bestIndex = i;
                    bestMerit = merit;
                }
                current.clear(newFeatures.get(i));
            }
            if (bestIndex < 0) break;
            System.out.println("+ " + newFeatures.get(bestIndex) + " @ " + bestMerit);
            current.set(newFeatures.get(bestIndex));
            newFeatures.remove(bestIndex);
        }
        double merit = creator.getEvaluation().evaluateSubset(current);
        for (int i = current.nextSetBit(0); i < creator.getCreated().numAttributes() - 1 & i != -1; i = current.nextSetBit(i + 1)) {
            current.clear(i);
            double tempMerit = ((SubsetEvaluator) creator.getEvaluation()).evaluateSubset(current);
            if (tempMerit != merit) current.set(i);
        }
        int[] newResult = NatureInspiredCommon.toIntArray(current, creator.getCreated().numAttributes() - 1);
        System.out.println("Online Subset = " + NatureInspiredCommon.getString(newResult));
        System.out.println("Online = " + bestMerit + " (" + newResult.length + ")");
        buffer.append(creator.getCreated().numAttributes() - 1).append("\t").append(creator.getCreated().numInstances()).append("\t");
        buffer.append(bestMerit).append("\t").append(newResult.length).append("\t").append(creator.test(result, "J48")).append("\t").append(creator.test("J48")).append("\t").append(NatureInspiredCommon.getString(newResult)).append("\n");
        return newResult;
    }
*/

}
