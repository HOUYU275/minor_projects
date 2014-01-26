package fuzzified.application;

import fuzzified.core.Harmony;
import fuzzified.core.HarmonyComparator;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 20/12/11
 * Time: 15:34
 * To change this template use File | Settings | File Templates.
 */
public class SpringHarmonyComparator extends HarmonyComparator {

    public SpringHarmonyComparator() {
        super();
        this.dimension = 1;
    }

    @Override
    public double[] translate(Harmony harmony) {
        double[] notes = new double[harmony.getNotes().length];
        for (int i = 0; i < harmony.getNotes().length; i++) notes[i] = harmony.getNote(i).getValue();
        return notes;
    }

    @Override
    public int checkConstraint(Harmony harmony) {
        double[] notes = translate(harmony);
        int numViolations = 0;
        if ((notes[0] <= 0d) || (notes[1] <= 0d) || (notes[2] <= 0d)) numViolations++;
        if (!checkG1(notes[0], notes[1], notes[2])) numViolations++;
        if (!checkG2(notes[0], notes[1], notes[2])) numViolations++;
        if (!checkG3(notes[0], notes[1], notes[2])) numViolations++;
        if (!checkG4(notes[0], notes[1], notes[2])) numViolations++;
        return numViolations;
    }

    @Override
    public void evaluate(Harmony harmony) {
        double[] notes = translate(harmony);
        harmony.setMerit(0 - ((notes[2] + 2) * notes[1] * notes[0] * notes[0]));
    }

    @Override
    public int compare(Object o1, Object o2) {
        Harmony fs1 = (Harmony) o1;
        Harmony fs2 = (Harmony) o2;
        int result = 0;
        if (fs1.getMerit() > fs2.getMerit()) {
            result = result + 1;
        } else if (fs1.getMerit() < fs2.getMerit()) {
            result = result - 1;
        }
        try {
            result -= checkConstraint(fs1) - checkConstraint(fs2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean checkG1(double a, double b, double c) {
        return ((1 - (b * b * b * c) / (71785 * a * a * a * a)) <= 0);
    }

    public boolean checkG2(double a, double b, double c) {
        return (((4 * b * b - a * b) / (12566 * (b * a * a * a - a * a * a * a))) + (1 / (5108 * a * a)) - 1 <= 0);
    }

    public boolean checkG3(double a, double b, double c) {
        return (1 - (140.45 * a / (b * b * c)) <= 0);
    }

    public boolean checkG4(double a, double b, double c) {
        return ((b + a) / 1.5 - 1 <= 0);
    }
}
