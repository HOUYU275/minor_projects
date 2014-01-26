package model;

import control.RuleBaseGenerator;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 15/01/13
 * Time: 15:42
 */
public class RuleBase extends ArrayList<Rule> {
    protected double[] maximums, minimums;
    protected RuleBaseGenerator ruleBaseGenerator;

    public RuleBase(int numAntecedents) {
        super();
        maximums = new double[numAntecedents];
        minimums = new double[numAntecedents];
    }

    public RuleBase(RuleBaseGenerator ruleBaseGenerator) {
        super();
        this.setRuleBaseGenerator(ruleBaseGenerator);
        maximums = new double[ruleBaseGenerator.getFunction().getNumAntecedents()];
        minimums = new double[ruleBaseGenerator.getFunction().getNumAntecedents()];
    }

    public double getRange(int antecedent) {
        return getMaximums()[antecedent] - getMinimums()[antecedent];
    }

    @Override
    public boolean add(Rule rule) {
        for (int a = 0; a < getMaximums().length; a++) {
            if (super.isEmpty()) getMaximums()[a] = rule.getAntecedents()[a].getA3();
            getMaximums()[a] = rule.getAntecedents()[a].getA3() > getMaximums()[a] ? rule.getAntecedents()[a].getA3() : getMaximums()[a];
        }
        for (int a = 0; a < getMinimums().length; a++) {
            if (super.isEmpty()) getMinimums()[a] = rule.getAntecedents()[a].getA1();
            getMinimums()[a] = rule.getAntecedents()[a].getA1() < getMinimums()[a] ? rule.getAntecedents()[a].getA1() : getMinimums()[a];
        }
        return super.add(rule);
    }

    public double[] getMaximums() {
        return maximums;
    }

    public double[] getMinimums() {
        return minimums;
    }

    public RuleBaseGenerator getRuleBaseGenerator() {
        return ruleBaseGenerator;
    }

    public void setRuleBaseGenerator(RuleBaseGenerator ruleBaseGenerator) {
        this.ruleBaseGenerator = ruleBaseGenerator;
    }

    public Rule check(Rule observation) {
        double dMin = Double.MAX_VALUE;
        Rule closestRule = null;
        for (Rule r : this) {
            double d = r.distanceTo(observation);
            if (d < dMin) {
                dMin = d;
                closestRule = r;
            }
        }
        if (dMin < 0.05) return closestRule;
        return null;
    }
}
