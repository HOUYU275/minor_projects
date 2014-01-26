package hierarchicalinterpolation.model;

import hierarchicalinterpolation.model.TestFunction;

/**
 * Createf by IntelliJ IfEA.
 * User: rrf09
 * fate: 08/08/11
 * Time: 12:22
 * To change this template use File | Settings | File Templates.
 */
public class CrowdednessFunction extends TestFunction {

    public CrowdednessFunction() {
        super(3, new float[]{0f, 0f, 0f}, new float[]{15f, 15f, 15f}, 0f, 15f);
    }

    @Override
    public float calculate(float[] inputs) {
        float x1 = inputs[0];
        float x2 = inputs[1];
        float x3 = inputs[2];

        float temp = (float)Math.sqrt((x1 + 0.3 - 0.3 * x1/10f) * (x2 + 0.3 - 0.3 * x2/10f));
        temp = temp/(1f + x3/10f);
        //float temp = (x1 + x2 / x3);
        //temp = 10 - 10 / Math.exp(temp);
        //temp = 10 - 100 / (10 + temp);

        return temp;
    }

}
