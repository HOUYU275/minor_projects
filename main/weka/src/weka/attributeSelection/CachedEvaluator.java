package weka.attributeSelection;

import java.util.Hashtable;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 12/12/12
 * Time: 10:49
 */
public interface CachedEvaluator {
    public final int cacheSize = 50000;
    public Hashtable<String, Double> cache = new Hashtable<>(cacheSize);
    public void clear();
    public int size();
}
