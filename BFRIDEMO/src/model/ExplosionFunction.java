package model;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 08/08/11
 * Time: 15:25
 * To change this template use File | Settings | File Templates.
 */
public class ExplosionFunction extends TestFunction {

    public ExplosionFunction() {
        super(2, new double[]{0d, 0d}, new double[]{10d, 10d}, 0d, 10d);
    }

    @Override
    public double calculate(double[] inputs) {
        double x1 = inputs[0];
        double x2 = inputs[1];

        double temp = x1/(1d + 1d* x2/10d);
        //temp = temp/(1d + x3/10d);
        //double temp = (x1 + x2 / x3);
        //temp = 10 - 10 / Math.exp(temp);
        //temp = 10 - 100 / (10 + temp);

        return temp;
    }
}
