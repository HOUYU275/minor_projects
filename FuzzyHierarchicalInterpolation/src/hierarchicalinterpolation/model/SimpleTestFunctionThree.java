package hierarchicalinterpolation.model;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 28/07/11
 * Time: 19:39
 * To change this template use File | Settings | File Templates.
 */
public class SimpleTestFunctionThree extends TestFunction {

    public SimpleTestFunctionThree(int numberOfVariables, float[] minAntecedentRanges, float[] maxAntecedentRanges, float minConsequenceRange, float maxConsequenceRange) {
        super(numberOfVariables, minAntecedentRanges, maxAntecedentRanges, minConsequenceRange, maxConsequenceRange);
    }

    @Override
    public float calculate(float[] inputs) {
        float x1 = inputs[0];
        float x2 = inputs[1];
        float x3 = inputs[2];
        float x4 = inputs[3];
        float x5 = inputs[4];
        float x6 = inputs[5];

        double temp = 1 + Math.sqrt(x3) + 1 / x2 + Math.pow(x1, -1.5d) + Math.sqrt(x4) + 1 / x5 + Math.pow(x6, -1.5d);

        return (float)temp;
    }
}
