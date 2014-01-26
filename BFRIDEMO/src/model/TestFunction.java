package model;

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
    double[] inputs;
    FuzzyNumber[] fuzzyInputs;
    double[] minAntecedentRanges;
    double[] maxAntecedentRanges;
    double minConsequenceRange;
    double maxConsequenceRange;

    public TestFunction(int numberOfVariables,
                        double[] minAntecedentRanges, double[] maxAntecedentRanges,
                        double minConsequenceRange, double maxConsequenceRange) {
        this.numberOfVariables = numberOfVariables;
        this.minAntecedentRanges = minAntecedentRanges;
        this.maxAntecedentRanges = maxAntecedentRanges;
        this.minConsequenceRange = minConsequenceRange;
        this.maxConsequenceRange = maxConsequenceRange;
    }

    public abstract double calculate(double[] inputs);

    public int getNumberOfVariables() {
        return numberOfVariables;
    }

    public void setNumberOfVariables(int numberOfVariables) {
        this.numberOfVariables = numberOfVariables;
    }

    public double[] getInputs() {
        return inputs;
    }

    public void setInputs(double[] inputs) {
        this.inputs = inputs;
    }

    public FuzzyNumber[] getFuzzyInputs() {
        return fuzzyInputs;
    }

    public void setFuzzyInputs(FuzzyNumber[] fuzzyInputs) {
        this.fuzzyInputs = fuzzyInputs;
    }

    public double[] getMinAntecedentRanges() {
        return minAntecedentRanges;
    }

    public void setMinAntecedentRanges(double[] minAntecedentRanges) {
        this.minAntecedentRanges = minAntecedentRanges;
    }

    public double[] getMaxAntecedentRanges() {
        return maxAntecedentRanges;
    }

    public void setMaxAntecedentRanges(double[] maxAntecedentRanges) {
        this.maxAntecedentRanges = maxAntecedentRanges;
    }

    public double getMinConsequenceRange() {
        return minConsequenceRange;
    }

    public void setMinConsequenceRange(double minConsequenceRange) {
        this.minConsequenceRange = minConsequenceRange;
    }

    public double getMaxConsequenceRange() {
        return maxConsequenceRange;
    }

    public void setMaxConsequenceRange(double maxConsequenceRange) {
        this.maxConsequenceRange = maxConsequenceRange;
    }
}
