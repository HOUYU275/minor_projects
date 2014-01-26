package online;

import featuresubsetensemble.FeatureSubsetEnsemble;
import survey.ClassificationDetail;
import util.ClassifierCrossValidator;
import util.CrossValidation;
import util.Registry;
import weka.attributeSelection.HillClimber;
import weka.attributeSelection.NatureInspiredCommon;
import weka.core.Instances;

import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 07/01/13
 * Time: 12:31
 */
public class InstanceCreator {

    private Instances original;
    private Instances testing;
    private Instances created;
    private BitSet currentFeatures;
    private BitSet tempFeatures;
    private BitSet lastFeatures;
    private BitSet currentInstances;
    private BitSet tempInstances;
    private BitSet lastInstances;
    private Random random = new Random();
    private HillClimber search;
    private int interval;

    /*public InstanceCreator(Instances original) {
        this.original = original;
    }*/

    public InstanceCreator(String dataset) {
        CrossValidation crossValidation = new CrossValidation(dataset, 1);
        this.original = crossValidation.getTrainingFold(1);
        this.testing = crossValidation.getTestingFold(1);
        this.search = new HillClimber();
    }

    public static void main(String[] args) throws IOException {

    }

    public Instances create() {
        if (currentFeatures != null) lastFeatures = (BitSet) currentFeatures.clone();
        currentFeatures = (BitSet) tempFeatures.clone();
        if (currentInstances != null) lastInstances = (BitSet) currentInstances.clone();
        currentInstances = (BitSet) tempInstances.clone();
        Instances created = new Instances(original);
        int[] featureMask = NatureInspiredCommon.toIntArrayInverse(currentFeatures, original.numAttributes() - 1);
        int[] instanceMask = NatureInspiredCommon.toIntArrayInverse(currentInstances, original.numInstances());
        for (int i = 0; i < featureMask.length; i++) created.deleteAttributeAt(featureMask[i] - i);
        for (int i = 0; i < instanceMask.length; i++) created.delete(instanceMask[i] - i);
        this.created = created;
        return created;
    }

    public Instances createTest() {
        Instances t = new Instances(testing);
        int[] featureMask = NatureInspiredCommon.toIntArrayInverse(currentFeatures, original.numAttributes() - 1);
        for (int i = 0; i < featureMask.length; i++) t.deleteAttributeAt(featureMask[i] - i);
        return t;
    }

    public double test(int[] subset, String classifier) throws Exception {
        ClassificationDetail result = ClassifierCrossValidator.validateFilteredClassifierComprehensive(Registry.getClassifier(classifier), subset, created, createTest());
        return result.getPercent_correct();
    }

    public double test(String classifier) throws Exception {
        ClassificationDetail result = ClassifierCrossValidator.validateRawClassifierComprehensive(Registry.getClassifier(classifier), created, createTest());
        return result.getPercent_correct();
    }

    public BitSet getCurrentFeatures() {
        return currentFeatures;
    }

    public Instances getCreated() {
        return created;
    }

    public ArrayList<Integer> getFeatureAdditions() {
        ArrayList<Integer> additions = new ArrayList<>();
        int index = 0;
        for (int a = currentFeatures.nextSetBit(0); a < original.numAttributes() - 1 & a != -1; a = currentFeatures.nextSetBit(a + 1)) {
            if (!lastFeatures.get(a)) additions.add(index);
            index++;
        }
        return additions;
    }

    public int[] search(DataSetEvaluator evaluator) throws Exception {
        return search.search(evaluator.getASEvaluation(), created);
    }

    public int[] convert(int[] result) {
        int[] lastFeatures = NatureInspiredCommon.toIntArray(this.lastFeatures, original.numAttributes() - 1);
        int[] temp = new int[result.length];
        for (int i = 0; i < result.length; i++) temp[i] = lastFeatures[result[i]];
        BitSet tempBitSet = NatureInspiredCommon.toBitSet(temp, original.classIndex());
        tempBitSet.and(currentFeatures);
        int[] converted = new int[tempBitSet.cardinality()];
        int[] currentFeatures = NatureInspiredCommon.toIntArray(this.currentFeatures, original.numAttributes() - 1);
        int index = 0;
        for (int i = 0; i < currentFeatures.length; i++) {
            if (tempBitSet.get(currentFeatures[i])) converted[index++] = i;
        }
        return converted;
    }

    public FeatureSubsetEnsemble convertEvaluate(FeatureSubsetEnsemble ensemble, DataSetEvaluator evaluator) {
        for (int i = 0; i < ensemble.getRawSubsets().size(); i++) {
            ensemble.getRawSubsets().set(i, convert(ensemble.getRawSubsets().get(i)));
            try {
                ensemble.getMerits().set(i, evaluator.evaluate(ensemble.getRawSubsets().get(i)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ensemble;
    }

    public Instances getInstanceAdditions(boolean andNot) {
        Instances created = new Instances(original);
        BitSet temp = (BitSet) currentInstances.clone();
        if (andNot) temp.andNot(lastInstances);
        else temp = (BitSet) lastInstances.clone();
        int[] featureMask = NatureInspiredCommon.toIntArrayInverse(currentFeatures, original.numAttributes() - 1);
        int[] instanceMask = NatureInspiredCommon.toIntArrayInverse(temp, original.numInstances());
        for (int i = 0; i < featureMask.length; i++) created.deleteAttributeAt(featureMask[i] - i);
        for (int i = 0; i < instanceMask.length; i++) created.delete(instanceMask[i] - i);
        return created;
    }


    /*public void randomMask() {
        tempFeatures = new BitSet(original.numAttributes() - 1);
        int total = random.nextInt(original.numAttributes() - 1);
        for (int i = 0; i < total; i++) {
            tempFeatures.set(random.nextInt(original.numAttributes() - 1));
        }
    }*/

    public ArrayList<Integer> getUnselectedFeatures(BitSet current, int total) {
        ArrayList<Integer> unselected = new ArrayList<>();
        for (int a = 0; a < total; a++) {
            if (!current.get(a)) unselected.add(a);
        }
        return unselected;
    }

    public void randomFeatureMask(int size) {
        tempFeatures = new BitSet(original.numAttributes() - 1);
        while (size > tempFeatures.cardinality()) tempFeatures.set(random.nextInt(original.numAttributes() - 1));
    }

    public void fullFeatureMask() {
        tempFeatures = new BitSet(original.numAttributes() - 1);
        for (int i = 0; i < original.numAttributes() - 1; i++) tempFeatures.set(i);
    }

    public void randomInstanceMask(int size) {
        tempInstances = new BitSet(original.numInstances());
        while (size > tempInstances.cardinality()) tempInstances.set(random.nextInt(original.numInstances()));
    }

    public void fullInstanceMask() {
        tempInstances = new BitSet(original.numInstances());
        for (int i = 0; i < original.numInstances(); i++) tempInstances.set(i);
    }

    public String addFeatures(int number) {
        //System.out.println("t=" + tempFeatures.cardinality() + " o=" + original.numAttributes() + " n=" + number);
        String returnString = "F + " + number + " ";
        while (number-- > 0) {
            if (tempFeatures.cardinality() >= original.numAttributes()) break;
            int randomIndex = random.nextInt(original.numAttributes() - 1);
            while (tempFeatures.get(randomIndex)) randomIndex = random.nextInt(original.numAttributes() - 1);
            tempFeatures.set(randomIndex);
        }
        return returnString;
    }

    public String removeFeatures(int number) {
        String returnString = "F - " + number + " ";
        while (number-- > 0) {
            if (tempFeatures.cardinality() < original.numAttributes() / 5) break;
            int randomIndex = random.nextInt(original.numAttributes() - 1);
            while (!tempFeatures.get(randomIndex)) randomIndex = random.nextInt(original.numAttributes() - 1);
            tempFeatures.clear(randomIndex);
            //returnString += randomIndex + ",";
        }
        return returnString;
    }

    public String addInstances(int number) {
        String returnString = "I + " + number + " ";
        while (number-- > 0) {
            if (tempInstances.cardinality() >= original.numInstances()) break;
            int randomIndex = random.nextInt(original.numInstances());
            while (tempInstances.get(randomIndex)) randomIndex = random.nextInt(original.numInstances());
            tempInstances.set(randomIndex);
        }
        return returnString;
    }

    public String removeInstances(int number) {
        String returnString = "I - " + number + " ";
        while (number-- > 0) {
            if (tempInstances.cardinality() < original.numInstances() / 5) break;
            int randomIndex = random.nextInt(original.numInstances());
            while (!tempInstances.get(randomIndex)) randomIndex = random.nextInt(original.numInstances());
            tempInstances.clear(randomIndex);
        }
        return returnString;
    }

    /*public Instances addCreate(int number) {
        while (number-- > 0) {
            int randomIndex = random.nextInt(original.numAttributes() - 1);
            mask.set(randomIndex);
        }
        return create();
    }

    public Instances removeCreate(int number) {
        while (number-- > 0) {
            int randomIndex = random.nextInt(original.numAttributes() - 1);
            mask.clear(randomIndex);
        }
        return create();
    }*/


    /*public void setOriginal(Instances original) {
        this.original = original;
    }

    public BitSet getMask() {
        return currentFeatures;
    }

    public void setMask(BitSet mask) {
        this.currentFeatures = mask;
    }*/

    public Instances getOriginal() {
        return original;
    }

    public Random getRandom() {
        return random;
    }

    public BitSet convertEvaluateHarmony(BitSet bitSet) throws Exception {
        BitSet temp = (BitSet) bitSet.clone();
        temp.and(currentFeatures);
        BitSet converted = new BitSet(currentFeatures.cardinality());
        int index = 0;
        for (int a = currentFeatures.nextSetBit(0); a < original.numAttributes() - 1 & a != -1; a = currentFeatures.nextSetBit(a + 1)) {
            if (temp.get(a)) converted.set(index);
            index++;
        }
        return converted;
    }

    public int[] convertHarmony(BitSet bitSet) throws Exception {
        BitSet temp = (BitSet) bitSet.clone();
        temp.and(currentFeatures);
        BitSet converted = new BitSet(currentFeatures.cardinality());
        int index = 0;
        for (int a = currentFeatures.nextSetBit(0); a < original.numAttributes() - 1 & a != -1; a = currentFeatures.nextSetBit(a + 1)) {
            if (temp.get(a)) converted.set(index);
            index++;
        }
        return NatureInspiredCommon.toIntArray(converted, created.numAttributes());
    }

    public void initialise(int mode) throws Exception {
        System.out.println("Original: " + (getOriginal().numAttributes() - 1) + " features (" + getOriginal().classIndex() + "), " + getOriginal().numInstances() + " instances.");
        switch (mode) {
            case 0:
                //full
                fullFeatureMask();
                fullInstanceMask();
                break;
            case 1:
                //fa
                randomFeatureMask(getOriginal().numAttributes() / 5);
                fullInstanceMask();
                break;
            case 2:
                //fr
                fullFeatureMask();
                fullInstanceMask();
                break;
            case 3:
                //ia
                fullFeatureMask();
                randomInstanceMask(getOriginal().numInstances() / 5);
                break;
            case 4:
                //ir
                fullFeatureMask();
                fullInstanceMask();
                break;
            default:
                randomFeatureMask(getOriginal().numAttributes() / 2);
                randomInstanceMask(getOriginal().numInstances() / 2);
                break;
        }
        create();
        System.out.println("Created: " + (getCreated().numAttributes() - 1) + " features (" + getCreated().classIndex() + "), " + getCreated().numInstances() + " instances.");
    }

    public String change(int mode) throws Exception {
        switch (mode) {
            case 0:
                return change();
            case 1:
                return addFeatures();
            case 2:
                return removeFeatures();
            case 3:
                return addInstances();
            case 4:
                return removeInstances();
            default:
                return change();
        }
    }

    public String change() throws Exception {
        StringBuilder buffer = new StringBuilder();
        int multiplier = getRandom().nextInt(4) + 1;
        //buffer.append(addFeatures(getOffset(original.numAttributes() - created.numAttributes(), original.numAttributes())));
        while (multiplier-- > 0) {
            int randomEvent = getRandom().nextInt(4);
            switch (randomEvent) {
                case 0:
                    buffer.append(removeFeatures(getOffset(created.numAttributes(), original.numAttributes())));
                    break;
                case 1:
                    buffer.append(addFeatures(getOffset(original.numAttributes() - created.numAttributes(), original.numAttributes())));
                    break;
                case 2:
                    buffer.append(removeInstances(getOffset(created.numInstances(), original.numInstances())));
                    break;
                case 3:
                    buffer.append(addInstances(getOffset(original.numInstances() - created.numInstances(), original.numInstances())));
                    break;
            }
        }
        create();
        //buffer.append("\t").append(created.numAttributes()).append("\t").append(created.numInstances());
        return buffer.toString();
    }

    public String addFeatures() throws Exception {
        String s = addFeatures(getOffset(original.numAttributes() - created.numAttributes(), original.numAttributes()));
        create();
        return s;
    }

    public String removeFeatures() throws Exception {
        String s = removeFeatures(getOffset(created.numAttributes(), original.numAttributes()));
        create();
        return s;
    }

    public String addInstances() throws Exception {
        String s = addInstances(getOffset(original.numInstances() - created.numInstances(), original.numInstances()));
        create();
        return s;
    }

    public String removeInstances() throws Exception {
        String s = removeInstances(getOffset(created.numInstances(), original.numInstances()));
        create();
        return s;
    }

    public int getOffset(int n, int total) {
        //if (n <= 5) return 1;
        int result = n / 2 / interval + getRandom().nextInt(n / 2 / interval);
        //if (this.interval < 3) result = n / interval;
        // (-e^(-x/6) + 1)/3
        //int result = (int) (n * getRandom().nextDouble() * (-Math.exp(-((double) n) / ((double) total)) + 1) / 2);
        //System.out.println("n = " + n + " t = " + total + ", -> " + result);
        return result;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getInterval() {
        return interval;
    }
}
