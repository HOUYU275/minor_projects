package model;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: rrd09
 * Date: 18/04/13
 * Time: 10:22
 * To change this template use File | Settings | File Templates.
 */
public class Temp {
    ArrayList<Double> errors = new ArrayList<Double>();

    public void push(double d) {
        errors.add(d);
    }

    public double pop() {
        double sum = 0;
        for (double d : errors) sum += d;
        int size = errors.size();
        errors.clear();
        return sum / size;
    }
}
