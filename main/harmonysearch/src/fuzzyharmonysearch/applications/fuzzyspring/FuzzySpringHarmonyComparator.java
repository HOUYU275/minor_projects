package fuzzyharmonysearch.applications.fuzzyspring;

import fuzzy.FuzzyNumber;
import fuzzyharmonysearch.core.Harmony;
import fuzzyharmonysearch.core.HarmonyComparator;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 18-Aug-2010
 * Time: 17:39:22
 * To change this template use File | Settings | File Templates.
 */
public class FuzzySpringHarmonyComparator extends HarmonyComparator {

    public FuzzySpringHarmonyComparator() {
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
        /*if ((harmony[0] <= 0d) || (harmony[1] <= 0d) || (harmony[2] <= 0d)) {
            return false;
        }
        return true;*/
        int numViolations = 0;

        if ((notes[0] <= 0d) || (notes[1] <= 0d) || (notes[2] <= 0d)) {
            //return false;
            numViolations++;
        }

        /*if (!checkG1(notes[0], notes[1], notes[2]) || !checkG2(notes[0], notes[1], notes[2]) || !checkG3(notes[0], notes[1], notes[2]) || !checkG4(notes[0], notes[1], notes[2])) {
            return false;
        }*/

        if (!checkG1(notes[0], notes[1], notes[2])) {
            //System.out.println("G1 Violation");
            numViolations++;
        }

        if (!checkG2(notes[0], notes[1], notes[2])) {
            //System.out.println("G2 Violation");
            numViolations++;
        }

        if (!checkG3(notes[0], notes[1], notes[2])) {
            //System.out.println("G3 Violation");
            numViolations++;
        }

        if (!checkG4(notes[0], notes[1], notes[2])) {
            //System.out.println("G4 Violation");
            numViolations++;
        }

        /*if (!checkG1(notes[0], notes[1], notes[2]) || !checkG2(notes[0], notes[1], notes[2]) || !checkG3(notes[0], notes[1], notes[2]) || !checkG4(notes[0], notes[1], notes[2])) {
            return false;
        }*/

        /*if (!checkG1(harmony[0], harmony[1], harmony[2]) || !checkG4(harmony[0], harmony[1], harmony[2])) {
            return false;
        }*/

        return numViolations;
    }

    @Override
    public void evaluate(Harmony harmony) throws Exception {

        double[] notes = harmony.getDoubleNotes();
        double merit = 0 - ((notes[2] + 2) * notes[1] * notes[0] * notes[0]);
        harmony.setMerit(new FuzzyNumber(merit, merit, merit));

        /*FuzzyNumber[] notes = harmony.getFuzzyNotes();
        FuzzyNumber merit = notes[2].adds(2).times(notes[1]).times(notes[0]).times(notes[0]).inverse();
        harmony.setMerit(merit);*/
    }

    @Override
    public int compare(Object o1, Object o2) {
        Harmony fs1 = (Harmony) o1;
        Harmony fs2 = (Harmony) o2;

        if (fs1.getMerit().getRepresentativeValue() > fs2.getMerit().getRepresentativeValue()) return 1;
        else if (fs1.getMerit().getRepresentativeValue() == fs2.getMerit().getRepresentativeValue()) return 0;
        else return -1;
        /*Harmony fs1 = (Harmony) o1;
        Harmony fs2 = (Harmony) o2;
        int result = 0;
        if (fs1.getMerit() > fs2.getMerit()) {
            result = result + 1;
        } else if (fs1.getMerit() < fs2.getMerit()) {
            result = result - 1;
        }
        //System.out.println("CC" + (checkConstraint(fs1) - checkConstraint(fs2)));

        try {
            result -= checkConstraint(fs1) - checkConstraint(fs2);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return result;*/
    }

    public boolean checkG1(double a, double b, double c) {
        return ((1 - (b * b * b * c) / (71785 * a * a * a * a)) <= 0);
    }

    public boolean checkG2(double a, double b, double c) {
        //System.out.println(((4 * b * b - a * b) / (12566 * (b * a * a * a - a * a * a * a))) + (1 / (5108 * a * a)) - 1);
        return (((4 * b * b - a * b) / (12566 * (b * a * a * a - a * a * a * a))) + (1 / (5108 * a * a)) - 1 <= 0);
    }

    public boolean checkG3(double a, double b, double c) {
        return (1 - (140.45 * a / (b * b * c)) <= 0);
    }

    public boolean checkG4(double a, double b, double c) {
        return ((b + a) / 1.5 - 1 <= 0);
    }
}