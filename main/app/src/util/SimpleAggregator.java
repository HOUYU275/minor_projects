package util;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Ren
 * Date: 19/10/12
 * Time: 17:35
 */
public class SimpleAggregator {
    ArrayList<Double> storage = new ArrayList<>();

    public void push(double d) {
        storage.add(d);
    }

    public double average() {
        double sum = 0d;
        for (int i = 0; i < storage.size(); i++) {
            sum += storage.get(i);
        }
        return sum / storage.size();
    }
}
