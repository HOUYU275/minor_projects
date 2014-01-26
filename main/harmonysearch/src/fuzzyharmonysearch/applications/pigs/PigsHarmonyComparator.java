package fuzzyharmonysearch.applications.pigs;

import fuzzy.FuzzyNumber;
import fuzzyharmonysearch.core.Harmony;
import fuzzyharmonysearch.core.HarmonyComparator;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 08/02/11
 * Time: 09:36
 * To change this template use File | Settings | File Templates.
 */
public class PigsHarmonyComparator extends HarmonyComparator {

    FuzzyNumber a1 = new FuzzyNumber(2, 2.5, 5);
    FuzzyNumber a2 = new FuzzyNumber(4, 4.5, 5);
    FuzzyNumber a3 = new FuzzyNumber(4.5, 5, 5.5);
    FuzzyNumber a4 = new FuzzyNumber(50, 54, 58);

    FuzzyNumber b1 = new FuzzyNumber(4.5, 5, 5.5);
    FuzzyNumber b2 = new FuzzyNumber(2.5, 3, 3.5);
    FuzzyNumber b3 = new FuzzyNumber(9, 10, 11);
    FuzzyNumber b4 = new FuzzyNumber(56, 60, 64);

    FuzzyNumber x1 = new FuzzyNumber(7, 8, 9);
    FuzzyNumber x2 = new FuzzyNumber(8, 9, 10);
    FuzzyNumber x3 = new FuzzyNumber(9, 10, 11);

    public PigsHarmonyComparator() {
        this.setDimension(1);
    }

    @Override
    public Object translate(Harmony harmony) throws Exception {
        return null;  //TODO: Automatically Generated Implemented Method
    }

    @Override
    public int checkConstraint(Harmony harmony) throws Exception {
        FuzzyNumber[] notes = harmony.getFuzzyNotes();

        FuzzyNumber a = notes[0];
        FuzzyNumber b = notes[1];
        FuzzyNumber c = notes[2];

        //System.out.println("a: " + a + " b: " + b + " c: " + c);

        FuzzyNumber check1 = a1.times(a).adds(a2.times(b)).adds(a3.times(c));
        FuzzyNumber check2 = b1.times(a).adds(b2.times(b)).adds(b3.times(c));
        int numViolations = 0;
        if (a.getRepresentativeValue() < 0) numViolations++;
        if (b.getRepresentativeValue() < 0) numViolations++;
        if (c.getRepresentativeValue() < 0) numViolations++;
        if (check1.compareTo(a4) < 0) numViolations++;
        if (check2.compareTo(b4) < 0) numViolations++;

        return numViolations;
    }

    public int checkConstraintVerbose(Harmony harmony) throws Exception {
        FuzzyNumber[] notes = harmony.getFuzzyNotes();

        FuzzyNumber a = notes[0];
        FuzzyNumber b = notes[1];
        FuzzyNumber c = notes[2];

        FuzzyNumber check1 = a1.times(a).adds(a2.times(b)).adds(a3.times(c));
        FuzzyNumber check2 = b1.times(a).adds(b2.times(b)).adds(b3.times(c));
        int numViolations = 0;
        if (a.getRepresentativeValue() < 0) numViolations++;
        if (b.getRepresentativeValue() < 0) numViolations++;
        if (c.getRepresentativeValue() < 0) numViolations++;
        if (check1.compareTo(a4) < 0) numViolations++;
        if (check2.compareTo(b4) < 0) numViolations++;
        System.out.println(numViolations + " - " + check1 + " vs " + a4 + " & " + check2 + " vs " + b4);
        return numViolations;
    }

    @Override
    public void evaluate(Harmony harmony) throws Exception {
        FuzzyNumber[] notes = harmony.getFuzzyNotes();
        FuzzyNumber a = notes[0];
        FuzzyNumber b = notes[1];
        FuzzyNumber c = notes[2];
        harmony.setMerit(x1.times(a).adds(x2.times(b)).adds(x3.times(c)));
    }

    /*@Override
    public void calculateDistance(Harmony harmony) {
        //TODO: Automatically Generated Implemented Method
    }*/

    @Override
    public int compare(Object o1, Object o2) {
        Harmony fs1 = (Harmony) o1;
        Harmony fs2 = (Harmony) o2;

        return 0 - fs1.getMerit().compareTo(fs2.getMerit());

        /*if (fs1.getMerit().getRepresentativeValue() < fs2.getMerit().getRepresentativeValue()) return 1;
        else if (fs1.getMerit().getRepresentativeValue() == fs2.getMerit().getRepresentativeValue()) return 0;
        else return -1;*/

        /*if (fs1.getMerit().getCentroid() < fs2.getMerit().getCentroid()) return 1;
        else if (fs1.getMerit().getCentroid() == fs2.getMerit().getCentroid()) return 0;
        else return -1;*/
    }
}
