package hierarchicalinterpolation.model;

import fuzzy.FuzzyNumber;

import java.util.ArrayList;
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
    private float[] maxAntecedentRanges;
    private float[] minAntecedentRanges;
    private float maxConsequenceRange;
    private float minConsequenceRange;

    public Rule(FuzzyNumber[] antecedents, FuzzyNumber consequence,
                float[] minAntecedentRanges, float[] maxAntecedentRanges,
                float minConsequenceRange, float maxConsequenceRange) {
        this.antecedents = antecedents;
        this.consequence = consequence;
        this.maxAntecedentRanges = maxAntecedentRanges;
        this.minAntecedentRanges = minAntecedentRanges;
        this.maxConsequenceRange = maxConsequenceRange;
        this.minConsequenceRange = minConsequenceRange;
    }

    public Rule(Rule rule) {
        this.antecedents = new FuzzyNumber[rule.getAntecedents().length];
        this.consequence = null;//new TriangularFuzzyNumber();
        this.maxAntecedentRanges = rule.getMaxAntecedentRanges();
        this.minAntecedentRanges = rule.getMinAntecedentRanges();
        this.maxConsequenceRange = rule.getMaxConsequenceRange();
        this.minConsequenceRange = rule.getMinConsequenceRange();
    }

    public Rule(/*int dimension,*/
                float[] minAntecedentRanges, float[] maxAntecedentRanges,
                float minConsequenceRange, float maxConsequenceRange) {
        this.antecedents = new FuzzyNumber[maxAntecedentRanges.length];
        this.consequence = null;//new TriangularFuzzyNumber();
        this.maxAntecedentRanges = maxAntecedentRanges;
        this.minAntecedentRanges = minAntecedentRanges;
        this.maxConsequenceRange = maxConsequenceRange;
        this.minConsequenceRange = minConsequenceRange;
    }

    public Rule(FuzzyNumber[] antecedents, FuzzyNumber consequence) {
        this.antecedents = antecedents;
        this.consequence = consequence;
    }

    /*public Rule(float[] maxAntecedentRanges, float[] minAntecedentRanges, float maxConsequenceRange, float minConsequenceRange) {
        this.maxAntecedentRanges = maxAntecedentRanges;
        this.minAntecedentRanges = minAntecedentRanges;
        this.maxConsequenceRange = maxConsequenceRange;
        this.minConsequenceRange = minConsequenceRange;
    }*/

    /*public Rule(int numberOfPoints, int numberOfVariables, float[] minAntecedentRanges, float[] maxAntecedentRanges, float minConsequenceRange, float maxConsequenceRange) {
        if (numberOfPoints == 4) {
            this.antecedents = new TrapezoidalFuzzyNumber[numberOfVariables];
        }
        else {
            this.antecedents = new TriangularFuzzyNumber[numberOfVariables];
        }
        this.maxAntecedentRanges = maxAntecedentRanges;
        this.minAntecedentRanges = minAntecedentRanges;
        this.maxConsequenceRange = maxConsequenceRange;
        this.minConsequenceRange = minConsequenceRange;
    }*/

    /*public Rule(float[] minAntecedentRanges, float[] maxAntecedentRanges, float minConsequenceRange, float maxConsequenceRange) {
        //To change body of created methods use File | Settings | File Templates.
    }*/

    /*public Rule(FuzzyNumber[] antecedents, FuzzyNumber consequence) {
        this.antecedents = antecedents;
        this.consequence = consequence;
    }*/

    /*public float getWeight(FuzzyNumber observation, int index) {
        if (observation.absDistanceTo(antecedents[index]) == 0) {
            return 1d / 0.000000000000000000000000000000000001d;
        }
        return 1d / (observation.absDistanceTo(antecedents[index]));
    }*/

    public float calculateDistance(Observation observation) {
        float distance = 0f;
        float temp = 0f;
        for (int i = 0; i < antecedents.length; i++) {
            temp = (float) Math.abs(antecedents[i].getRepresentativeValue() - observation.getAntecedents()[i].getRepresentativeValue());
            temp = temp / (maxAntecedentRanges[i] - minAntecedentRanges[i]);

            //System.out.println(distance + " + " + temp + "^2");
            distance = distance + temp * temp;

        }
        return (float)Math.sqrt(distance);
    }
    
public float calculateDistance(Rule observation, boolean useWeightedConsequenceDistance) {
        float distance = 0f;
        float temp;

        ArrayList<Integer> dimensionsToBeInterpolated = new ArrayList<Integer>();
        int numberOfAntecedents = observation.getAntecedents().length;

        for (int i = 0; i < numberOfAntecedents; i++) {
            if (observation.getAntecedents()[i] == null) dimensionsToBeInterpolated.add(i);
        }

        if (dimensionsToBeInterpolated.size() >= 0) {
            //backward
            for (int i = 0; i < numberOfAntecedents; i++) {
                if (!dimensionsToBeInterpolated.contains(i)) {
                    temp = (float) Math.abs(antecedents[i].getRepresentativeValue() - observation.getAntecedents()[i].getRepresentativeValue());
                    temp = temp / (maxAntecedentRanges[i] - minAntecedentRanges[i]);
                    distance = distance + temp * temp;
                }
                temp = (float) Math.abs(consequence.getRepresentativeValue() - observation.getConsequence().getRepresentativeValue());
                temp = temp / (maxConsequenceRange - minConsequenceRange);
                distance = distance + (useWeightedConsequenceDistance ? numberOfAntecedents * temp * temp : temp * temp);
                return (float)Math.sqrt(distance);
            }

        } else {
            //forward
            for (int i = 0; i < antecedents.length; i++) {
                temp = (float) Math.abs(antecedents[i].getRepresentativeValue() - observation.getAntecedents()[i].getRepresentativeValue());
                temp = temp / (maxAntecedentRanges[i] - minAntecedentRanges[i]);

                //System.out.println(distance + " + " + temp + "^2");
                distance = distance + temp * temp;
            }
            return (float)Math.sqrt(distance);
        }

        return distance;
    }
    

    public int getNumberOfAntecedents() {
        return this.antecedents.length;
    }

    public FuzzyNumber getAntecedent(int index) {
        return this.antecedents[index];
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

    public void setAntecedent(int i, FuzzyNumber intermediateFuzzyTerm) {
        this.antecedents[i] = intermediateFuzzyTerm;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < antecedents.length; i ++) {
            stringBuffer.append("[" + i + "] " + antecedents[i] + "\n");
        }
        stringBuffer.append("[-] " + consequence);
        return stringBuffer.toString();
    }
}
