package hierarchicalharmonysearch.core;

import java.util.Comparator;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 26/01/11
 * Time: 12:57
 * To change this template use File | Settings | File Templates.
 */
public abstract class HarmonyComparator implements Comparator {
    private int dimension;

    public HarmonyComparator() {
    }

    public abstract Object translate(Harmony harmony) throws Exception;

    public abstract int checkConstraint(Harmony harmony) throws Exception;

    public abstract void evaluate(Harmony harmony) throws Exception;

    public abstract int compare(Object o1, Object o2);

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }
}
