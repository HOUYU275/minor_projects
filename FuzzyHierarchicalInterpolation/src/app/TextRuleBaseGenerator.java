package app;

import java.io.*;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: shj1
 * Date: 18/07/12
 * Time: 13:19
 * To change this template use File | Settings | File Templates.
 */
public class TextRuleBaseGenerator {

    private static int numVariables = 5;
    private static int numFuzzySets = 6;
    private static float valueRange = 9f;

    public static void main(String[] args) throws IOException {
        Random random = new Random();
        float[][] representativeValues = new float[numVariables][numFuzzySets];
        for (int i = 0; i < numVariables; i++) {
            float interval = valueRange / numFuzzySets;
            float startingValue = 0.5f;
            for (int j = 0; j < numFuzzySets; j++) {
                float value = random.nextFloat() * interval + startingValue;
                representativeValues[i][j] = value;
                startingValue += interval;
            }
        }
        print(representativeValues);

        FileOutputStream ostream = new FileOutputStream("RuleBaseRaw.txt");
        DataOutputStream out = new DataOutputStream(ostream);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));

        double[] ranges = new double[numVariables];
        for (int i = 0; i < numVariables; i++) {
            ranges[i] = numFuzzySets - 1;
        }
        int[] currentIndices = null;
        float[] currentInputs = new float[numVariables];
        for (int k = 0; k < 7776; k++) {
            currentIndices = getNextIndices(currentIndices, ranges);
            for (int i = 0; i < representativeValues.length; i++) {
                currentInputs[i] = representativeValues[i][currentIndices[i]] + random.nextFloat() * 1f - 0.5f;
                bw.write(currentInputs[i] + " ");
            }
            float currentOutput = calculate(currentInputs);
            bw.write(currentOutput + "");
            bw.newLine();
        }
        bw.close();
        out.close();
        ostream.close();
    }

    public static int[] getNextIndices(int[] currentIndices, double[] ranges) {
        int[] nextIndices = new int[ranges.length];

        if (currentIndices == null) {
            for (int i = 0; i < nextIndices.length; i++) {
                nextIndices[i] = 0;
            }
            return nextIndices;
        }

        boolean increment = false;
        for (int i = nextIndices.length - 1; i >= 0; i--) {
            if (i == nextIndices.length - 1) {
                if (currentIndices[i] == ranges[i]) {
                    increment = true;
                    nextIndices[i] = 0;
                } else {
                    nextIndices[i] = currentIndices[i] + 1;
                }
            } else {
                if (increment == true) {
                    if (currentIndices[i] == ranges[i]) {
                        if (i == 0) return null;
                        increment = true;
                        nextIndices[i] = 0;
                    } else {
                        increment = false;
                        nextIndices[i] = currentIndices[i] + 1;
                    }
                } else {
                    nextIndices[i] = currentIndices[i];
                }
            }

        }
        return nextIndices;
    }

    private static void print(float[][] representativeValues) {
        for (int i = 0; i < numVariables; i++) {
            for (int j = 0; j < numFuzzySets; j++) {
                System.out.print(representativeValues[i][j] + ", ");
            }
            System.out.println();
        }
    }

    private static float calculate(float[] inputs) {
        return (float) (Math.sqrt(inputs[0]) - 5 * inputs[1] + 1 / (inputs[2]+1) + Math.pow(inputs[3],2) + inputs[4]);
    }
}
