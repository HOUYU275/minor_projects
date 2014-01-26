package test;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 31/12/12
 * Time: 08:50
 */
public class SimpleMain {

    public static void main(String[] args) {
        double initial = 0.6;
        double radius = 125;
        double centreX = 250;
        double centreY = 250;
        int count = 5;
        for (int i = 0; i < count; i++) {
            System.out.println((centreX - Math.cos(initial) * radius) + ", " + (centreY - Math.sin(initial) * radius));
            initial += 2 * Math.PI / count;
        }
    }

}
