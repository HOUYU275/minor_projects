package app;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: shj1
 * Date: 16/07/12
 * Time: 13:23
 * To change this template use File | Settings | File Templates.
 */
public class Demo {

    private static double min = -100;
    private static double max = 29;

    public static void main(String[] args) {
        int count = 100000;
        while (count > 0) {
            generate();
            count--;
        }
        System.out.println("Max = " + max + " ; Min = " + min);
    }

    public static void generate() {
        double[] inputs = new double[6];
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            inputs[i] = (double) random.nextInt(10) + 1;
        }
        double output = calculate(inputs);
        if (output > max) max = output;
        if (output < min) min = output;
    }

    public static double calculate(double[] inputs) {
        return inputs[0] + inputs[1] - inputs[2] * inputs[3] + inputs[4] / inputs[5];
    }


}
