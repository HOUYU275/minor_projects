package hierarchicalinterpolation.model;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 08/08/11
 * Time: 15:25
 * To change this template use File | Settings | File Templates.
 */
public class ExplosionFunction extends TestFunction {

    public ExplosionFunction() {
        super(2, new float[]{0f, 0f}, new float[]{10f, 10f}, 0f, 10f);
    }

    @Override
    public float calculate(float[] inputs) {
        float x1 = inputs[0];
        float x2 = inputs[1];

        float temp = x1/(1f + 1f* x2/10f);
        //temp = temp/(1d + x3/10d);
        //float temp = (x1 + x2 / x3);
        //temp = 10 - 10 / Math.exp(temp);
        //temp = 10 - 100 / (10 + temp);

        return temp;
    }
}
