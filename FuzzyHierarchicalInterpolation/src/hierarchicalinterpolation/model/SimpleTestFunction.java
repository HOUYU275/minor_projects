package hierarchicalinterpolation.model;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 28/07/11
 * Time: 19:39
 * To change this template use File | Settings | File Templates.
 */
public class SimpleTestFunction extends TestFunction {

    public SimpleTestFunction(int numberOfVariables, float[] minAntecedentRanges, float[] maxAntecedentRanges, float minConsequenceRange, float maxConsequenceRange) {
        super(numberOfVariables, minAntecedentRanges, maxAntecedentRanges, minConsequenceRange, maxConsequenceRange);
    }

    @Override
    public float calculate(float[] inputs) {
        float x1 = inputs[0];
        float x2 = inputs[1];
        float x3 = inputs[2];

        double temp = 1 + Math.sqrt(x1) + 1 / x2 + Math.pow(x3, -1.5d);

        return (float)(temp * temp);
    }
}
