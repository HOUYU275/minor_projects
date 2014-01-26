package originalharmonysearch.applications.interpolation;

import hierarchicalinterpolation.control.BackwardProcess;
import hierarchicalinterpolation.model.Rule;
import originalharmonysearch.core.Harmony;
import originalharmonysearch.core.HarmonyComparator;

/**
 * Created with IntelliJ IDEA.
 * User: rrd09
 * Date: 15/08/13
 * Time: 16:31
 */
public class InterpolationHarmonyComparator extends HarmonyComparator {

    BackwardProcess backwardProcess;
    double[] totals;

    public InterpolationHarmonyComparator(BackwardProcess backwardProcess, double[] totals) throws Exception {
        super();
        this.setDimension(1);
        this.backwardProcess = backwardProcess;
        this.totals = totals;
    }

    @Override
    public double[] translate(Harmony harmony) throws Exception {
        double[] parameters = new double[10];
        parameters[0] = harmony.getDoubleNotes()[0];
        parameters[1] = 1 - parameters[0];
        parameters[2] = totals[0] - parameters[0];
        parameters[3] = 1 - parameters[2];
        parameters[4] = harmony.getDoubleNotes()[1];
        parameters[5] = totals[1] - parameters[4];
        parameters[6] = harmony.getDoubleNotes()[2];
        parameters[7] = totals[2] - parameters[6];
        parameters[8] = harmony.getDoubleNotes()[3];
        parameters[9] = totals[3] - parameters[8];
        return parameters;
    }

    @Override
    public int checkConstraint(Harmony harmony) throws Exception {
        return 0;
    }

    @Override
    public void evaluate(Harmony harmony) throws Exception {
        Rule rule = backwardProcess.backwardInterpolate(translate(harmony));
        double range = (backwardProcess.getRuleBase().getMaxConsequenceRange() - backwardProcess.getRuleBase().getMinConsequenceRange());
        harmony.setMerit(backwardProcess.evaluateForwardConsequence(rule) / range);
    }

    @Override
    public int compare(Object o1, Object o2) {
        Harmony fs1 = (Harmony) o1;
        Harmony fs2 = (Harmony) o2;

        int result = 0;
        if (fs1.getMerit() < fs2.getMerit()) {
            result = result + 1;
        } else if (fs1.getMerit() > fs2.getMerit()) {
            result = result - 1;
        }
        return result;
    }
}
