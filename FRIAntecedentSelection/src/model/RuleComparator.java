package model;

import java.util.Comparator;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 16/01/13
 * Time: 00:27
 */
public class RuleComparator implements Comparator<Rule> {
    private Rule observation;
    private int missingIndex;

    public RuleComparator(Rule observation) {
        this.observation = observation;
        this.missingIndex = missingIndex;
    }

    @Override
    public int compare(Rule o1, Rule o2) {
        //observation.setConsequent(null);
        double d1 = observation.distanceTo(o1);
        double d2 = observation.distanceTo(o2);
        if (d1 < d2) return -1;
        if (d1 > d2) return 1;
        return 0;
    }
}
