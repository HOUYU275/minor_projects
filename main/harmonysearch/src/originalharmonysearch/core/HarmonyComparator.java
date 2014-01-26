package originalharmonysearch.core;

import java.util.Comparator;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 18-Aug-2010
 * Time: 16:20:07
 * To change this template use File | Settings | File Templates.
 */
public abstract class HarmonyComparator implements Comparator {
    private int dimension;
    //private HarmonyEvaluator[] harmonyEvaluators;

    public HarmonyComparator() {
    }

    /*public HarmonyComparator(HarmonyEvaluator[] harmonyEvaluators) {
        this.harmonyEvaluators = harmonyEvaluators;
    }

    public HarmonyEvaluator[] getHarmonyEvaluators() {
        return harmonyEvaluators;
    }

    public void setHarmonyEvaluators(HarmonyEvaluator[] harmonyEvaluators) {
        this.harmonyEvaluators = harmonyEvaluators;
    }*/

    public abstract Object translate(Harmony harmony) throws Exception;

    public abstract int checkConstraint(Harmony harmony) throws Exception;

    public abstract void evaluate(Harmony harmony) throws Exception; /*{
        for (int i = 0; i < harmonyEvaluators.length; i++) {
            harmony.setMerit(harmonyEvaluators[i].evaluate(harmony), i);
        }
    }*/

    public abstract int compare(Object o1, Object o2);

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }
}
