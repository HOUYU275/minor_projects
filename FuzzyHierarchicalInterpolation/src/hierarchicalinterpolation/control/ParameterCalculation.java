package hierarchicalinterpolation.control;

import java.io.PipedInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: shj1
 * Date: 18/10/11
 * Time: 15:17
 * To change this template use File | Settings | File Templates.
 */
public class ParameterCalculation {

    private static float sum = 0;

    public static void reset() {
        sum = 0;
    }

    public static void push(float value, boolean isConsequence, boolean isForward) {
        //sum = (isForward) ? sum + value :
    }

}
