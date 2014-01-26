package weka.fuzzy.similarity;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 09-Nov-2010
 * Time: 12:58:38
 * To change this template use File | Settings | File Templates.
 */
public class Similarities/* extends HashMap<String, Double>*/ {

    private double[][][] similarities;
    private boolean ready = false;

    public Similarities(int numAttributes, int numInstances) {
        similarities = new double[numAttributes][numInstances][numInstances];
    }

    public void setSimilarity(int attribute, int x, int y, double value) {
        if (x > y) {
            similarities[attribute][y][x] = value;
        } else {
            similarities[attribute][x][y] = value;
        }
    }

    public double getSimilarity(int attribute, int x, int y) {
        return (x > y) ? similarities[attribute][y][x] : similarities[attribute][x][y];
    }

    public boolean containsSimilarity(int attribute, int x, int y) {
        return (x > y) ? (similarities[attribute][y][x] != 0) : (similarities[attribute][x][y] != 0);
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
/*public Similarities() {
        super();
        System.out.println("Created");
    }

    public void setSimilarity (int attribute, int x, int y, double value) {
        if (x > y) super.put(attribute + "_" + y + "_" + x, value);
        super.put(attribute + "_" + x + "_" + y, value);        
    }

    public double getSimilarity (int attribute, int x, int y) {
        return (x > y) ? super.get(attribute + "_" + y + "_" + x) : super.get(attribute + "_" + x + "_" + y);
    }

    public boolean containsSimilarity(int attribute, int x, int y) {
        return (x > y) ? super.containsKey(attribute + "_" + y + "_" + x) : super.containsKey(attribute + "_" + x + "_" + y);
    }*/
}
