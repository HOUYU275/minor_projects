package model;

/**
 * Created with IntelliJ IDEA.
 * User: rrd09
 * Date: 14/08/13
 * Time: 20:29
 */
public class ComplexFunction extends Function {

    public ComplexFunction() {
        super(15, new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, new double[]{20, 20, 20,20, 20, 20,20, 20, 20,20, 20, 20,20, 20, 20});
    }

    @Override
    public double calculate(double[] inputs) {
        double x0 = inputs[0];
        double x1 = inputs[1];
        double x2 = inputs[2];
        double x3 = inputs[3];
        double x4 = inputs[4];

        double x5 = inputs[5];
        double x6 = inputs[6];
        double x7 = inputs[7];
        double x8 = inputs[8];
        double x9 = inputs[9];

        double x10 = inputs[10];
        double x11 = inputs[11];
        double x12 = inputs[12];
        double x13 = inputs[13];
        double x14 = inputs[14];

        //[0 4 6 7 13]

        double temp = 1 + Math.sqrt(x0 * 10) + 0.0001 * x1 + 0.0001 * x2 + 0.0001 * x3 - 5 * x4;
        temp += 3 + 0.0001 * x5 - x6 + 100 / (x7+1) - 0.0001 * x8 + 0.0001 * x9;
        temp += -5 + 0.0001 * x10 - 0.0001 * x11 + 0.0001 * x12 - Math.pow(x13, 2) + 0.0001 * x14;
        return temp;
    }

    @Override
    public double calculate(FuzzyNumber[] inputs) {
        double x0 = inputs[0].rep();
        double x1 = inputs[1].rep();
        double x2 = inputs[2].rep();
        double x3 = inputs[3].rep();
        double x4 = inputs[4].rep();

        double x5 = inputs[5].rep();
        double x6 = inputs[6].rep();
        double x7 = inputs[7].rep();
        double x8 = inputs[8].rep();
        double x9 = inputs[9].rep();

        double x10 = inputs[10].rep();
        double x11 = inputs[11].rep();
        double x12 = inputs[12].rep();
        double x13 = inputs[13].rep();
        double x14 = inputs[14].rep();


        double temp = 1 + Math.sqrt(x0 * 10) + 0.0001 * x1 + 0.0001 * x2 + 0.0001 * x3 - 5 * x4;
        temp += 3 + 0.0001 * x5 - x6 + 100 / (x7+1) - 0.0001 * x8 + 0.0001 * x9;
        temp += -5 + 0.0001 * x10 - 0.0001 * x11 + 0.0001 * x12 - Math.pow(x13, 2) + 0.0001 * x14;
        return temp;
    }


}
