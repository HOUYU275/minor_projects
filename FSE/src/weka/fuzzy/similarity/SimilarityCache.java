package weka.fuzzy.similarity;

import weka.fuzzy.measure.FuzzyMeasure;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 25/11/11
 * Time: 16:07
 * To change this template use File | Settings | File Templates.
 */
public class SimilarityCache implements Serializable {
    double[][] values;

    public SimilarityCache() {
    }

    public SimilarityCache(int size) {
        values = new double[size][];
        for (int i = 0; i < size; i++) {
            values[i] = new double[i + 1];
        }
    }

    public SimilarityCache(int size, FuzzyMeasure measure, int classIndex) {
        values = new double[size][];
        for (int i = 0; i < size; i++) {
            values[i] = new double[i + 1];
            for (int j = 0; j < i + 1; j ++) {
                values[i][j] = measure.fuzzySimilarity(classIndex, i, j);
            }
        }
    }

    public void set(int i, int j, double value) {
        if (i >= j) values[i][j] = value;
        else values[j][i] = value;
    }

    public double get(int i, int j) {
        return (i >= j) ? values[i][j] : values[j][i];
    }
}
