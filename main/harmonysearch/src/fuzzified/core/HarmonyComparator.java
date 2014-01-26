package fuzzified.core;

import java.util.Comparator;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 20/12/11
 * Time: 12:33
 * To change this template use File | Settings | File Templates.
 */
public abstract class HarmonyComparator implements Comparator {

    protected int dimension;

    public HarmonyComparator() {
    }

    public abstract Object translate(Harmony harmony);

    public abstract int checkConstraint(Harmony harmony);

    public abstract void evaluate(Harmony harmony);

    public abstract int compare(Object o1, Object o2);

}