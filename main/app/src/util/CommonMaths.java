package util;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 30/04/12
 * Time: 12:00
 */
public class CommonMaths {
    public static double average(double[] inputs) {
        double sum = 0d;
        for (double d : inputs) sum += d;
        return sum / inputs.length;
    }
}
