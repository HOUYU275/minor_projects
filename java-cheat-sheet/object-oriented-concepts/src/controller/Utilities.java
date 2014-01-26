package controller;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 15/02/12
 * Time: 13:37
 */
public class Utilities {
    public static double convertAngle(int angle) {
        return ((angle + 0d) / 360) * 2 * Math.PI;
    }

    public static double calculateX(int angle, double speed) {
        return Math.sin(convertAngle(angle)) * speed;
    }

    public static double calculateY(int angle, double speed) {
        return Math.cos(convertAngle(angle)) * speed;
    }
}
