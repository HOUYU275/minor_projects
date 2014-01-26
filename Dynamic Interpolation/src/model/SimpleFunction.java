package model;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 28/07/11
 * Time: 19:39
 */
public class SimpleFunction extends Function {

    public SimpleFunction() {
        super(3, new double[]{1, 1, 1}, new double[]{20, 20, 20});
    }

    @Override
    public double calculate(double[] inputs) {
        double x0 = inputs[0];
        double x1 = inputs[1];
        double x2 = inputs[2];
        double temp = 1 + Math.sqrt(x0) + 1 / x1 + Math.pow(x2, -1.5d);
        return temp * temp;
    }

    @Override
    public double calculate(FuzzyNumber[] inputs) {
        double x0 = inputs[0].rep();
        double x1 = inputs[1].rep();
        double x2 = inputs[2].rep();
        double temp = 1 + Math.sqrt(x0) + 1 / x1 + Math.pow(x2, -1.5d);
        return temp * temp;
    }
}
