package model;

import fuzzy.FuzzyNumber;

import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 28/07/11
 * Time: 14:49
 * To change this template use File | Settings | File Templates.
 */
public class Rule {

    private FuzzyNumber[] antecedents;
    private FuzzyNumber consequence;
    private double[] maxAntecedentRanges;
    private double[] minAntecedentRanges;
    private double maxConsequenceRange;
    private double minConsequenceRange;

    public Rule(FuzzyNumber[] antecedents, FuzzyNumber consequence,
                double[] minAntecedentRanges, double[] maxAntecedentRanges,
                double minConsequenceRange, double maxConsequenceRange) {
        this.antecedents = antecedents;
        this.consequence = consequence;
        this.maxAntecedentRanges = maxAntecedentRanges;
        this.minAntecedentRanges = minAntecedentRanges;
        this.maxConsequenceRange = maxConsequenceRange;
        this.minConsequenceRange = minConsequenceRange;
    }

    public Rule(Rule rule) {
        this.antecedents = new FuzzyNumber[rule.getAntecedents().length];
        this.consequence = new FuzzyNumber();
        this.maxAntecedentRanges = rule.getMaxAntecedentRanges();
        this.minAntecedentRanges = rule.getMinAntecedentRanges();
        this.maxConsequenceRange = rule.getMaxConsequenceRange();
        this.minConsequenceRange = rule.getMinConsequenceRange();
    }

    public Rule(RuleBase ruleBase) {
        this.antecedents = new FuzzyNumber[ruleBase.getMinAntecedentRanges().length];
        this.consequence = new FuzzyNumber();
        this.maxAntecedentRanges = ruleBase.getMaxAntecedentRanges();
        this.minAntecedentRanges = ruleBase.getMinAntecedentRanges();
        this.maxConsequenceRange = ruleBase.getMaxConsequenceRange();
        this.minConsequenceRange = ruleBase.getMinConsequenceRange();
    }

    public Rule(int dimension,
                double[] minAntecedentRanges, double[] maxAntecedentRanges,
                double minConsequenceRange, double maxConsequenceRange) {
        this.antecedents = new FuzzyNumber[dimension];
        this.consequence = new FuzzyNumber();
        this.maxAntecedentRanges = maxAntecedentRanges;
        this.minAntecedentRanges = minAntecedentRanges;
        this.maxConsequenceRange = maxConsequenceRange;
        this.minConsequenceRange = minConsequenceRange;
    }

    public Rule(FuzzyNumber[] antecedents, FuzzyNumber consequence) {
        this.antecedents = antecedents;
        this.consequence = consequence;
    }

    /*public Rule(FuzzyNumber[] antecedents, FuzzyNumber consequence) {
        this.antecedents = antecedents;
        this.consequence = consequence;
    }*/

    public double getWeight(FuzzyNumber observation, int index) {
        if (observation.absDistanceTo(antecedents[index]) == 0) {
            return 1d / 0.000000000000000000000000000000000001d;
        }
        return 1d / (observation.absDistanceTo(antecedents[index]));
    }

    public double calculateDistance(Observation observation, boolean useWeightedConsequenceDistance) {
        double distance = 0d;
        double temp;

        int dimensionToBeInterpolated = -1;
        int numberOfAntecedents = observation.getAntecedents().length;

        for (int i = 0; i < numberOfAntecedents; i++) {
            if (observation.getAntecedents()[i] == null) dimensionToBeInterpolated = i;
        }

        if (dimensionToBeInterpolated >= 0) {
            //backward
            for (int i = 0; i < numberOfAntecedents; i++) {
                if (i != dimensionToBeInterpolated) {
                    temp = Math.abs(antecedents[i].getRep() - observation.getAntecedents()[i].getRep());
                    temp = temp / (maxAntecedentRanges[i] - minAntecedentRanges[i]);
                    distance = distance + temp * temp;
                }
                temp = Math.abs(consequence.getRep() - observation.getConsequence().getRep());
                temp = temp / (maxConsequenceRange - minConsequenceRange);
                distance = distance + (useWeightedConsequenceDistance ? numberOfAntecedents * temp * temp : temp * temp);
                return Math.sqrt(distance);
            }

        } else {
            //forward
            for (int i = 0; i < antecedents.length; i++) {
                temp = Math.abs(antecedents[i].getRep() - observation.getAntecedents()[i].getRep());
                temp = temp / (maxAntecedentRanges[i] - minAntecedentRanges[i]);

                //System.out.println(distance + " + " + temp + "^2");
                distance = distance + temp * temp;
            }
            return Math.sqrt(distance);
        }

        return distance;
    }

    public FuzzyNumber[] getAntecedents() {
        return antecedents;
    }

    public void setAntecedents(FuzzyNumber[] antecedents) {
        this.antecedents = antecedents;
    }

    public FuzzyNumber getConsequence() {
        return consequence;
    }

    public void setConsequence(FuzzyNumber consequence) {
        this.consequence = consequence;
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

    public Rule clone() {
        return new Rule(antecedents, consequence, maxAntecedentRanges, minAntecedentRanges,
                maxConsequenceRange, minConsequenceRange);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rule rule = (Rule) o;

        if (!Arrays.equals(antecedents, rule.antecedents)) return false;
        if (consequence != null ? !consequence.equals(rule.consequence) : rule.consequence != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = antecedents != null ? Arrays.hashCode(antecedents) : 0;
        result = 31 * result + (consequence != null ? consequence.hashCode() : 0);
        return result;
    }
}
