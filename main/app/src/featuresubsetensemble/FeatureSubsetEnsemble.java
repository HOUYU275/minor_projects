package featuresubsetensemble;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 15/10/12
 * Time: 09:44
 * To change this template use File | Settings | File Templates.
 */
public class FeatureSubsetEnsemble {

    private ArrayList<FeatureMembershipPair> featureMembershipPairs = new ArrayList<>();
    private int decision;
    private Vector<int[]> rawSubsets;
    private Vector<Double> merits;

    public void getRanks() {
        Collections.sort(featureMembershipPairs);
        System.out.println(this.toString());
    }

    //numberOfFeatures includes the decision feature!
    public FeatureSubsetEnsemble(int numberOfFeatures) {
        for (int i = 0; i < numberOfFeatures - 1; i++) {
            featureMembershipPairs.add(new FeatureMembershipPair(i));
        }
        decision = numberOfFeatures - 1;
    }

    public void setMembership(int feature, double membership) {
        featureMembershipPairs.get(feature).setMembership(membership);
    }

    public void convertFrom(Vector<int[]> subsets, Vector<Double> merits) {
        this.rawSubsets = subsets;
        this.merits = merits;
        int[] featureCounts = new int[featureMembershipPairs.size()];
        //count the number of occurrences for each feature
        for (int i = 0; i < subsets.size(); i++) {
            int[] currentSubset = subsets.get(i);
            for (int j = 0; j < currentSubset.length; j++) {
                if (currentSubset[j] < featureCounts.length) featureCounts[currentSubset[j]]++;
            }
        }
        int highestCount = 0;
        for (int i = 0; i < featureCounts.length; i++) {
            highestCount = (highestCount < featureCounts[i]) ? featureCounts[i] : highestCount;
        }
        for (int i = 0; i < featureCounts.length; i++) {
            if (featureCounts[i] != 0)
                featureMembershipPairs.get(i).setMembership((double) featureCounts[i] / (double) highestCount);
        }
    }

    public void convertFrom(Vector<int[]> subsets) {
        this.rawSubsets = subsets;
        int[] featureCounts = new int[featureMembershipPairs.size()];
        //count the number of occurrences for each feature
        for (int i = 0; i < subsets.size(); i++) {
            int[] currentSubset = subsets.get(i);
            for (int j = 0; j < currentSubset.length; j++) {
                if (currentSubset[j] < featureCounts.length) featureCounts[currentSubset[j]]++;
            }
        }
        int highestCount = 0;
        for (int i = 0; i < featureCounts.length; i++) {
            highestCount = (highestCount < featureCounts[i]) ? featureCounts[i] : highestCount;
        }
        for (int i = 0; i < featureCounts.length; i++) {
            if (featureCounts[i] != 0)
                featureMembershipPairs.get(i).setMembership((double) featureCounts[i] / (double) highestCount);
        }
    }

	public double getFlattenedSize() {
		double sum = 0d;
		for (FeatureMembershipPair pair : featureMembershipPairs) sum += pair.getMembership();
		return sum;
	}

    public ArrayList<FeatureMembershipPair> getFeatureMembershipPairs() {
        return featureMembershipPairs;
    }

    public void setFeatureMembershipPairs(ArrayList<FeatureMembershipPair> featureMembershipPairs) {
        this.featureMembershipPairs = featureMembershipPairs;
    }

    public int[] convertTo(double threshold) {
        ArrayList<Integer> candidates = new ArrayList<>();
        for (int i = 0; i < featureMembershipPairs.size(); i++) {
            if (featureMembershipPairs.get(i).getMembership() >= threshold) candidates.add(featureMembershipPairs.get(i).getFeature());
        }
        int[] converted = new int[candidates.size()];
        for (int i = 0; i < candidates.size(); i++) {
            converted[i] = candidates.get(i);
        }
		if (converted.length == 0) return convertTo(threshold - 0.2);
        return converted;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("FeatureSubsetEnsemble{\n");
        for (int i = 0; i < featureMembershipPairs.size(); i++) {
            buffer.append(featureMembershipPairs.get(i).getFeature() + "\t" + featureMembershipPairs.get(i).getMembership() + "\n");
        }
        buffer.append("decision=" + decision + "}");
        return buffer.toString();
    }

    public Vector<int[]> getRawSubsets() {
        return rawSubsets;
    }

    public void setRawSubsets(Vector<int[]> rawSubsets) {
        this.rawSubsets = rawSubsets;
    }


    public double getRawSubsetSize() {
        double count = 0;
        for (int i = 0; i < this.rawSubsets.size(); i++) {
            count += this.rawSubsets.get(i).length;
        }
        return count / this.rawSubsets.size();
    }

    public double getAveragedMerit() {
        double count = 0;
        for (Double merit : this.merits) count += merit;
        return count / this.merits.size();
    }

    public Vector<Double> getMerits() {
        return merits;
    }
}

/*

public class EnsembleFeatureSubset {
    public static int length = 10;
    public static ArrayList<String> correctedList = new ArrayList<String>();

    public static void main(String[] args) {
        String currentString = "";
        String correctedString = "";
        int count = 0;
        for (int i = 0; i < Math.pow(2, length); i++) {
            correctedString = "";
            currentString = Integer.toBinaryString(i);
            while (currentString.length() < length)
                currentString = "0" + currentString;
            correctedString = correct(currentString);
            System.out.println(currentString + " => " + correctedString);
            if (checkEqual(correctedString) == true)
                count++;
            correctedList.add(correctedString);
        }
        for (int i = 0; i < length; i++) {
            System.out.println(i + " - " + checkOccurrence(correctedList, i) + " / " + Math.pow(2, length) + " = " + (checkOccurrence(correctedList, i) + 0d) /  Math.pow(2, length));
        }
        System.out.println("Total Number = " + count + " / " + Math.pow(2, length));
    }

    public static boolean checkEqual(String input) {
        int count = 0;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '0')
                count++;
        }
        if (count == length / 2)
            return true;
        return false;
    }

    public static String correct(String input) {
        int countZero = 0;
        int countOne = 0;
        String correctedInput = "";
        for (int i = 0; i < input.length(); i++) {
            if ((countOne < length / 2) && (countZero < length / 2)) {
                if (input.charAt(i) == '0')
                    countZero++;
                if (input.charAt(i) == '1')
                    countOne++;
                correctedInput += input.charAt(i);
            }
            else {
                if (countOne == length / 2) correctedInput += "0";
                if (countZero == length / 2) correctedInput += "1";
            }
        }
        return correctedInput;
    }

    public static int checkOccurrence (ArrayList<String> inputList, int index) {
        int countZero = 0;
        for (int i = 0; i < inputList.size(); i++) {
            if (inputList.get(i).charAt(index) == '0') countZero++;
        }
        return countZero;
    }

}
 */
