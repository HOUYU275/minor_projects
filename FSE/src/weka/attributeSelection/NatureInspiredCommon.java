package weka.attributeSelection;

import java.util.BitSet;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 17/12/12
 * Time: 10:02
 */
public class NatureInspiredCommon {

    public static BitSet randomBitSet(int numAttributes, Random random) {
        BitSet temp = new BitSet(numAttributes);
        int count = random.nextInt(numAttributes) + 1;
        for (int i = 0; i < count; i++) temp.set(random.nextInt(numAttributes));
        return temp;
    }

    public static BitSet randomBitSet(int numAttributes, Random random, int size) {
        BitSet temp = new BitSet(numAttributes);
        int count = random.nextInt(size) + 1;
        for (int i = 0; i < count; i++) temp.set(random.nextInt(numAttributes));
        return temp;
    }

    public static SubsetEvaluator convert(ASEvaluation ASEvaluator) throws Exception {
        if (!(ASEvaluator instanceof SubsetEvaluator))
            throw new Exception(ASEvaluator.getClass().getName() + " is not a Subset evaluator!");
        return (SubsetEvaluator) ASEvaluator;
    }

    public static int[] toIntArray(BitSet set, int numAttributes) {
        int[] subset = new int[set.cardinality()];
        int index = 0;
        for (int i = set.nextSetBit(0); i < numAttributes & i != -1; i = set.nextSetBit(i + 1)) subset[index++] = i;
        return subset;
    }

    public static int[] toIntArrayInverse(BitSet set, int numAttributes) {
        int[] subset = new int[numAttributes - set.cardinality()];
        int index = 0;
        for (int i = set.nextClearBit(0); i < numAttributes & i != -1; i = set.nextClearBit(i + 1)) subset[index++] = i;
        return subset;
    }

    public static int[] fullSubset(int numAttributes) {
        int[] subset = new int[numAttributes];
        for (int i = 0; i < numAttributes; i++) subset[i] = i;
        return subset;
    }

    public static BitSet toBitSet(int[] input, int classIndex) {
        BitSet set = new BitSet(classIndex);
        for (int anInput : input) set.set(anInput);
        return set;
    }

    public static String getString(int[] input) {
        if (input.length == 0) return "";
        String s = "";
        for (int anInput : input) s += (anInput + ", ");
        return s.substring(0, s.length() - 2);
    }

    public static double getMerit(BitSet bitSet, SubsetEvaluator evaluator, int numAttributes) throws Exception {
        double score = evaluator.evaluateSubset(bitSet);
        double size = bitSet.cardinality();
        return score + (numAttributes - size) * 0.05 / numAttributes;
    }

    public static void updateBest(BitSet currentSet, double currentMerit, BitSet bestSet, double bestMerit) {
        if (bestMerit < currentMerit) {
            bestMerit = currentMerit;
            bestSet = (BitSet) currentSet.clone();
        } else if ((bestMerit == currentMerit) && bestSet.cardinality() > currentSet.size()) {
            bestSet = (BitSet) currentSet.clone();
        }
    }

    public static BitSet xor(BitSet setA, BitSet setB) {
        BitSet temp = (BitSet) setA.clone();
        temp.xor(setB);
        return temp;
    }

    public static BitSet localSearch(BitSet set, double merit, SubsetEvaluator evaluator, int numAttributes) throws Exception {
        if (evaluator instanceof InterruptingEvaluator) ((InterruptingEvaluator) evaluator).turnOff();
        BitSet temp = (BitSet) set.clone();
        double tempMerit, bestDifference, bestMerit;
        int index;
        do {
            bestDifference = -Double.MAX_VALUE;
            bestMerit = merit;
            index = -1;
            for (int i = 0; i < numAttributes; i++) {
                temp.flip(i);
                tempMerit = evaluator.evaluateSubset(temp);
                if (tempMerit - bestMerit > bestDifference) {
                    bestDifference = tempMerit - bestMerit;
                    index = i;
                }
                temp.flip(i);
            }
            if ((index != -1) && (bestDifference > 0)) temp.flip(index);
        } while (index != -1);
        return temp;
    }

}
