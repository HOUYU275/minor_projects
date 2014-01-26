package originalharmonysearch.applications.constraintfour;

import originalharmonysearch.core.Harmony;
import originalharmonysearch.core.HarmonyComparator;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 18-Aug-2010
 * Time: 17:22:55
 * To change this template use File | Settings | File Templates.
 */
public class ConstraintFourHarmonyComparator extends HarmonyComparator {

    public ConstraintFourHarmonyComparator() {
        super();
        this.setDimension(1);
    }

    @Override
    public Object translate(Harmony harmony) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int checkConstraint(Harmony harmony) {
        double[] notes = harmony.getDoubleNotes();
        double a = notes[0];
        double b = notes[1];
        double c = notes[2];
        int numViolations = 0;
        /*if ((a < 0d) || (b < 0d)) {
            //return false;
            numViolations++;
        }*/

        if (!checkA(a, b, c)) {
            numViolations++;
        }

        if (!checkB(a, b, c)) {
            numViolations++;
        }

        if (!checkC(a, b, c)) {
            numViolations++;
        }

        if (!checkG1(a, b, c)) {
            numViolations++;
        }

        /*if (!checkA(a, b, c) || !checkB(a, b, c) || !checkC(a, b, c) || !checkG1(a, b, c)) {
            //return false;
        }*/
        return numViolations;
    }

    @Override
    public void evaluate(Harmony harmony) throws Exception {
        double[] notes = harmony.getDoubleNotes();
        double a = notes[0];
        double b = notes[1];
        double c = notes[2];
        harmony.setMerit((100 - (a - 5) * (a - 5) - (b - 5) * (b - 5) - (c - 5) * (c - 5)) / 100);
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
        return result;
    }

    public boolean checkG1(double a, double b, double c) {

        for (int i = 1; i < 10; i++) {
            for (int j = 1; j < 10; j++) {
                for (int k = 1; k < 10; k++) {
                    if (checkG2(a, b, c, i, j, k)) {
                        //System.out.println(a+" "+b+" "+c+" "+i+" "+j+" "+k);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean checkG2(double a, double b, double c, int p, int q, int r) {
        return (a - p) * (a - p) + (b - q) * (b - q) + (c - r) * (c - r) - 0.0625 <= 0;
    }

    public boolean checkG3(double a, double b, double c) {
        return (a >= 0) && (a <= 6);
    }

    public boolean checkG4(double a, double b, double c) {
        return (b >= 0) && (b <= 6);
    }

    public boolean checkA(double a, double b, double c) {
        return (a >= 0) && (a <= 10);
    }

    public boolean checkB(double a, double b, double c) {
        return (b >= 0) && (b <= 10);
    }

    public boolean checkC(double a, double b, double c) {
        return (c >= 0) && (c <= 10);
    }
}
