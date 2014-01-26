package fuzzyharmonysearch.applications.productmix;

import fuzzy.FuzzyNumber;
import fuzzyharmonysearch.core.Harmony;
import fuzzyharmonysearch.core.HarmonyComparator;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 19/12/11
 * Time: 14:34
 * To change this template use File | Settings | File Templates.
 */
public class ProductMixHarmonyComparator extends HarmonyComparator {

    private FuzzyNumber a = new FuzzyNumber(5.8, 6, 6.2);
    private FuzzyNumber b = new FuzzyNumber(7.5, 8, 8.5);
    private FuzzyNumber c = new FuzzyNumber(5.6, 6, 6.4);

    private FuzzyNumber a1 = new FuzzyNumber(5.6, 6, 6.4);
    private FuzzyNumber b1 = new FuzzyNumber(7.5, 8, 8.5);
    private FuzzyNumber c1 = new FuzzyNumber(2.8, 3, 3.2);
    private FuzzyNumber d1 = new FuzzyNumber(283, 288, 293);

    private FuzzyNumber a2 = new FuzzyNumber(11.4, 12, 12.6);
    private FuzzyNumber b2 = new FuzzyNumber(7.6, 8, 8.4);
    private FuzzyNumber c2 = new FuzzyNumber(5.7, 6, 6.3);
    private FuzzyNumber d2 = new FuzzyNumber(306, 312, 318);

    private FuzzyNumber a3 = new FuzzyNumber(1.8, 2, 2.2);
    private FuzzyNumber b3 = new FuzzyNumber(3.8, 4, 4.2);
    private FuzzyNumber c3 = new FuzzyNumber(0.9, 1, 1.1);
    private FuzzyNumber d3 = new FuzzyNumber(121, 124, 127);

    @Override
    public Object translate(Harmony harmony) throws Exception {
        return null;
    }

    @Override
    public int checkConstraint(Harmony harmony) throws Exception {
        FuzzyNumber[] notes = harmony.getFuzzyNotes();

        FuzzyNumber x1 = notes[0];
        FuzzyNumber x2 = notes[1];
        FuzzyNumber x3 = notes[2];

        int violations = 0;

        if (a.getRepresentativeValue() < 0) violations++;
        if (b.getRepresentativeValue() < 0) violations++;
        if (c.getRepresentativeValue() < 0) violations++;

        if (a1.times(x1).adds(b1.times(x2)).adds(c1.times(x3)).compareTo(d1) > 0) violations++;
        if (a2.times(x1).adds(b2.times(x2)).adds(c2.times(x3)).compareTo(d2) > 0) violations++;
        if (a3.times(x1).adds(b3.times(x2)).adds(c3.times(x3)).compareTo(d3) > 0) violations++;

        return violations;
    }

    @Override
    public void evaluate(Harmony harmony) throws Exception {
        FuzzyNumber[] notes = harmony.getFuzzyNotes();
        FuzzyNumber x1 = notes[0];
        FuzzyNumber x2 = notes[1];
        FuzzyNumber x3 = notes[2];
        harmony.setMerit(x1.times(a).adds(x2.times(b)).adds(x3.times(c)));
    }

    @Override
    public int compare(Object o1, Object o2) {
        return 0;  //TODO: Automatically Generated Implemented Method
    }
}
