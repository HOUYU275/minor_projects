package model;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 28/07/11
 * Time: 19:39
 * To change this template use File | Settings | File Templates.
 */
public class SimpleTestFunctionTwo extends TestFunction {

    public SimpleTestFunctionTwo(int numberOfVariables, double[] minAntecedentRanges, double[] maxAntecedentRanges, double minConsequenceRange, double maxConsequenceRange) {
        super(numberOfVariables, minAntecedentRanges, maxAntecedentRanges, minConsequenceRange, maxConsequenceRange);
    }

    @Override
    public double calculate(double[] inputs) {
        double x1 = inputs[0];
        double x2 = inputs[1];
        double x3 = inputs[2];

        double temp = 1 + Math.sqrt(x3) + 1 / x2 + Math.pow(x1, -1.5d);

        return temp * temp;
    }
}
