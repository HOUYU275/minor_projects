package hierarchicalinterpolation.model;

import fuzzy.FuzzyNumber;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 28/07/11
 * Time: 19:36
 * To change this template use File | Settings | File Templates.
 */
public abstract class TestFunction {

    int numberOfVariables;
    float[] inputs;
    FuzzyNumber[] fuzzyInputs;
    float[] minAntecedentRanges;
    float[] maxAntecedentRanges;
    float minConsequenceRange;
    float maxConsequenceRange;

    public TestFunction(int numberOfVariables,
                        float[] minAntecedentRanges, float[] maxAntecedentRanges,
                        float minConsequenceRange, float maxConsequenceRange) {
        this.numberOfVariables = numberOfVariables;
        this.minAntecedentRanges = minAntecedentRanges;
        this.maxAntecedentRanges = maxAntecedentRanges;
        this.minConsequenceRange = minConsequenceRange;
        this.maxConsequenceRange = maxConsequenceRange;
    }

    public abstract float calculate(float[] inputs);

    public int getNumberOfVariables() {
        return numberOfVariables;
    }

    public void setNumberOfVariables(int numberOfVariables) {
        this.numberOfVariables = numberOfVariables;
    }

    public float[] getInputs() {
        return inputs;
    }

    public void setInputs(float[] inputs) {
        this.inputs = inputs;
    }

    public FuzzyNumber[] getFuzzyInputs() {
        return fuzzyInputs;
    }

    public void setFuzzyInputs(FuzzyNumber[] fuzzyInputs) {
        this.fuzzyInputs = fuzzyInputs;
    }

    public float[] getMinAntecedentRanges() {
        return minAntecedentRanges;
    }

    public void setMinAntecedentRanges(float[] minAntecedentRanges) {
        this.minAntecedentRanges = minAntecedentRanges;
    }

    public float[] getMaxAntecedentRanges() {
        return maxAntecedentRanges;
    }

    public void setMaxAntecedentRanges(float[] maxAntecedentRanges) {
        this.maxAntecedentRanges = maxAntecedentRanges;
    }

    public float getMinConsequenceRange() {
        return minConsequenceRange;
    }

    public void setMinConsequenceRange(float minConsequenceRange) {
        this.minConsequenceRange = minConsequenceRange;
    }

    public float getMaxConsequenceRange() {
        return maxConsequenceRange;
    }

    public void setMaxConsequenceRange(float maxConsequenceRange) {
        this.maxConsequenceRange = maxConsequenceRange;
    }
}
