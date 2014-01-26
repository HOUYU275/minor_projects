package hierarchicalinterpolation.model;

import fuzzy.FuzzyNumber;
import fuzzy.TrapezoidalFuzzyNumber;
import fuzzy.TriangularFuzzyNumber;

import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: shj1
 * Date: 08/11/11
 * Time: 12:17
 * To change this template use File | Settings | File Templates.
 */
public class CrispRule {
    private float[] antecedents;
    private float consequence;

    public CrispRule() {
    }

    public CrispRule(float[] antecedents) {
        this.antecedents = antecedents;
    }

    public CrispRule(float[] antecedents, float consequence) {
        this.antecedents = antecedents;
        this.consequence = consequence;
    }

    public CrispRule(int numberOfVariables) {
        this.antecedents = new float[numberOfVariables];
    }

    public float[] getAntecedents() {
        return antecedents;
    }

    public void setAntecedents(float[] antecedents) {
        this.antecedents = antecedents;
    }

    public float getConsequence() {
        return consequence;
    }

    public void setConsequence(float consequence) {
        this.consequence = consequence;
    }

    public void setAntecedent(int i, float value) {
        this.antecedents[i] = value;
    }

    public Rule fuzzify(int numberOfPoints, Random random, int numberOfFuzzySetsPerVariable, Rule rule) {
        float supportLength;
        for (int i = 0; i < this.antecedents.length; i++) {
            supportLength = (float) 0.5 * (rule.getMaxAntecedentRanges()[i] - rule.getMinAntecedentRanges()[i]) /
                    numberOfFuzzySetsPerVariable;
            rule.setAntecedent(i, fuzzify(antecedents[i], numberOfPoints, supportLength, random));
        }
        supportLength = (float) 0.5 * (rule.getMaxConsequenceRange() - rule.getMinConsequenceRange()) /
                numberOfFuzzySetsPerVariable;
        rule.setConsequence(fuzzify(this.consequence, numberOfPoints, supportLength, random));
        return rule;
    }

    private FuzzyNumber fuzzify(float crispValue, int numberOfPoints, float supportLength, Random random) {
        if (numberOfPoints == 4) {
            //Trapezoidal
            supportLength = supportLength / numberOfPoints;
            TrapezoidalFuzzyNumber t = new TrapezoidalFuzzyNumber(
                    crispValue - 2 * supportLength,
                    crispValue - supportLength,
                    crispValue + supportLength,
                    crispValue + 2 * supportLength);
            float a = t.getRepresentativeValue();
            return t;
            /*float leftRatio = random.nextFloat();
    float topBottomRatio = random.nextFloat();
    float bottomLeftShift = leftRatio * supportLength;
    float a = crispValue - bottomLeftShift;
    float d = a + supportLength;
    float b = crispValue - (supportLength - bottomLeftShift) * topBottomRatio;
    float c = crispValue + (supportLength - bottomLeftShift) * (1 - topBottomRatio);
    float rep = (a + d + (b + c) / 2) / 3;
    return new TrapezoidalFuzzyNumber(
            a,
            b,
            c,
            d);*/
        } else if (numberOfPoints == 3) {
            float leftShift = random.nextFloat() * supportLength;
            return new TriangularFuzzyNumber(
                    crispValue - leftShift,
                    crispValue + 2 * leftShift - supportLength,
                    crispValue + supportLength - leftShift);
        }
        return null;
    }
}
