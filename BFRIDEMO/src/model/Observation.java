package model;

import fuzzy.FuzzyNumber;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 28/07/11
 * Time: 14:53
 * To change this template use File | Settings | File Templates.
 */
public class Observation extends Rule{

    public Observation(FuzzyNumber[] antecedents) {
        super(antecedents, new FuzzyNumber());
    }

    public Observation(FuzzyNumber[] antecedents, FuzzyNumber consequence) {
        super(antecedents, consequence);
    }

    public Observation(Rule rule) {
        super(rule);
    }

    public Observation(RuleBase ruleBase) {
        super(ruleBase);
    }
}
