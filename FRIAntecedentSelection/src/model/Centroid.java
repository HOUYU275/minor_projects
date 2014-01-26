package model;

import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 15/01/13
 * Time: 19:45
 */
public class Centroid {
    protected double[] values;

    public Centroid(int numAntecedents) {
        this.values = new double[numAntecedents + 1];
    }

    public Centroid(Rule rule) {
        this.values = new double[rule.getAntecedents().length + 1];
        for (int a = 0; a < rule.getAntecedents().length; a++) values[a] = rule.getAntecedents()[a].rep();
        values[values.length - 1] = rule.getConsequent().rep();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Centroid centroid = (Centroid) o;
        return Arrays.equals(values, centroid.values);

    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }
}
