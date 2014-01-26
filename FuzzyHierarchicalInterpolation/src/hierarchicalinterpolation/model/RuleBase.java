package hierarchicalinterpolation.model;

import fuzzy.FuzzyNumber;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 05/08/11
 * Time: 15:07
 * To change this template use File | Settings | File Templates.
 */
public class RuleBase extends ArrayList<Rule> {

    private float[] maxAntecedentRanges;
    private float[] minAntecedentRanges;
    private float maxConsequenceRange;
    private float minConsequenceRange;

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

    public RuleBase(float[] minAntecedentRanges, float[] maxAntecedentRanges,
                    float minConsequenceRange, float maxConsequenceRange) {
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

    public float[] getMaxAntecedentRanges() {
        return maxAntecedentRanges;
    }

    public void setMaxAntecedentRanges(float[] maxAntecedentRanges) {
        this.maxAntecedentRanges = maxAntecedentRanges;
    }

    public float[] getMinAntecedentRanges() {
        return minAntecedentRanges;
    }

    public void setMinAntecedentRanges(float[] minAntecedentRanges) {
        this.minAntecedentRanges = minAntecedentRanges;
    }

    public float getMaxConsequenceRange() {
        return maxConsequenceRange;
    }

    public void setMaxConsequenceRange(float maxConsequenceRange) {
        this.maxConsequenceRange = maxConsequenceRange;
    }

    public float getMinConsequenceRange() {
        return minConsequenceRange;
    }

    public void setMinConsequenceRange(float minConsequenceRange) {
        this.minConsequenceRange = minConsequenceRange;
    }

    @Override
    public List<Rule> subList(int fromIndex, int toIndex) {
        return super.subList(fromIndex, toIndex);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public Rule newRule() {
        return new Rule(this.minAntecedentRanges,
                this.maxAntecedentRanges,
                this.minConsequenceRange,
                this.maxConsequenceRange);
    }
}
