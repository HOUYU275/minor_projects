package originalharmonysearch.applications.vessel;

import originalharmonysearch.core.Harmony;
import originalharmonysearch.core.HarmonyComparator;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 28-Oct-2010
 * Time: 14:02:45
 * To change this template use File | Settings | File Templates.
 */
public class VesselHarmonyComparator extends HarmonyComparator {
    public VesselHarmonyComparator() {
        super();
        this.setDimension(1);
    }

    @Override
    public Object translate(Harmony harmony) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int checkConstraint(Harmony harmony) throws Exception {
        double[] notes = harmony.getDoubleNotes();
        int numViolations = 0;
        double a = notes[0];
        double b = notes[1];
        double c = notes[2];
        double d = notes[3];
        if ((a <= 0d) || (b <= 0d) || (c <= 0d) || (d <= 0d)) numViolations++;
        if (!checkG1(a, b, c, d)) numViolations++;
        if (!checkG2(a, b, c, d)) numViolations++;
        if (!checkG3(a, b, c, d)) numViolations++;
        if (!checkG4(a, b, c, d)) numViolations++;
        if (!checkG5(a, b, c, d)) numViolations++;
        if (!checkG6(a, b, c, d)) numViolations++;
        return numViolations;
    }

    @Override
    public void evaluate(Harmony harmony) throws Exception {
        double[] notes = harmony.getDoubleNotes();
        double a = notes[0];
        double b = notes[1];
        double c = notes[2];
        double d = notes[3];
        harmony.setMerit(0 - (0.6224 * a * 0.0625 * c * d + 1.7781 * b * 0.0625 * Math.pow(c, 2) + 3.1611 * Math.pow(a * 0.0625, 2) * d + 19.84 * Math.pow(a * 0.0625, 2) * c));
    }

    @Override
    public int compare(Object o1, Object o2) {
        Harmony fs1 = (Harmony) o1;
        Harmony fs2 = (Harmony) o2;
        int result = 0;
        //for (int i = 0; i < getHarmonyEvaluators().length; i++) {
        if (fs1.getMerit() > fs2.getMerit()) {
            result = result + 1;
        } else if (fs1.getMerit() < fs2.getMerit()) {
            result = result - 1;
        }
        //}

        try {
            result -= checkConstraint(fs1) - checkConstraint(fs2);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return result;
    }

    public boolean checkG1(double a, double b, double c, double d) {
        return (-1 * a * 0.0625 + 0.0193 * c <= 0);
    }

    public boolean checkG2(double a, double b, double c, double d) {
        return (-1 * b * 0.0625 + 0.00954 * c <= 0);
    }

    public boolean checkG3(double a, double b, double c, double d) {
        //System.out.println(Math.PI * Math.pow(58.29015, 2) * 43.69268 + 4 * Math.PI * Math.pow(58.29015, 3) / 3 - 1296000);
        return (750 * 1728 - (d + c * 4 / 3) * Math.PI * Math.pow(c, 2) <= 0);
    }

    public boolean checkG4(double a, double b, double c, double d) {
        return (d - 240 <= 0);
    }

    public boolean checkG5(double a, double b, double c, double d) {
        return (1.1 - a * 0.0625 <= 0);
    }

    public boolean checkG6(double a, double b, double c, double d) {
        return (0.6 - b * 0.0625 <= 0);
    }

}
