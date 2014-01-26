package model;

import fuzzy.FuzzyNumber;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 05/08/11
 * Time: 15:07
 * To change this template use File | Settings | File Templates.
 */
public class RuleBase extends ArrayList<Rule> {

    private double[] maxAntecedentRanges;
    private double[] minAntecedentRanges;
    private double maxConsequenceRange;
    private double minConsequenceRange;

    public RuleBase(int initialCapacity) {
        super(initialCapacity);
    }

    public RuleBase() {
    }

    public RuleBase(Collection<? extends Rule> c) {
        super(c);
    }

    public RuleBase(TestFunction testFunction) {
        this.maxAntecedentRanges = testFunction.getMaxAntecedentRanges();
        this.minAntecedentRanges = testFunction.getMinAntecedentRanges();
        this.maxConsequenceRange = testFunction.getMaxConsequenceRange();
        this.minConsequenceRange = testFunction.getMinConsequenceRange();
    }

    public RuleBase(double[] minAntecedentRanges, double[] maxAntecedentRanges,
                    double minConsequenceRange, double maxConsequenceRange) {
        this.maxAntecedentRanges = maxAntecedentRanges;
        this.minAntecedentRanges = minAntecedentRanges;
        this.maxConsequenceRange = maxConsequenceRange;
        this.minConsequenceRange = minConsequenceRange;
    }

    public void add(FuzzyNumber[] antecedents, FuzzyNumber consequence) {
        super.add(
                new Rule(antecedents, consequence,
                        this.minAntecedentRanges,
                        this.maxAntecedentRanges,
                        this.minConsequenceRange,
                        this.maxConsequenceRange));
    }

    public double[] getMaxAntecedentRanges() {
        return maxAntecedentRanges;
    }

    public void setMaxAntecedentRanges(double[] maxAntecedentRanges) {
        this.maxAntecedentRanges = maxAntecedentRanges;
    }

    public double[] getMinAntecedentRanges() {
        return minAntecedentRanges;
    }

    public void setMinAntecedentRanges(double[] minAntecedentRanges) {
        this.minAntecedentRanges = minAntecedentRanges;
    }

    public double getMaxConsequenceRange() {
        return maxConsequenceRange;
    }

    public void setMaxConsequenceRange(double maxConsequenceRange) {
        this.maxConsequenceRange = maxConsequenceRange;
    }

    public double getMinConsequenceRange() {
        return minConsequenceRange;
    }

    public void setMinConsequenceRange(double minConsequenceRange) {
        this.minConsequenceRange = minConsequenceRange;
    }
}
