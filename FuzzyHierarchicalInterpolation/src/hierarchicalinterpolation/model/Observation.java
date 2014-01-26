package hierarchicalinterpolation.model;

import fuzzy.FuzzyNumber;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 28/07/11
 * Time: 14:53
 * To change this template use File | Settings | File Templates.
 */
public class Observation extends Rule {

    public Observation(FuzzyNumber[] antecedents) {
        super(antecedents, null);
    }

    public Observation(Rule rule) {
        super(rule);
    }
}
