package hierarchicalinterpolation.control;

import hierarchicalinterpolation.model.Observation;
import hierarchicalinterpolation.model.Rule;
import hierarchicalinterpolation.model.RuleBase;

/**
 * Created with IntelliJ IDEA.
 * User: shj1
 * Date: 27/07/12
 * Time: 11:00
 * To change this template use File | Settings | File Templates.
 */
public class BackwardProcess {

    private int missingIndex1;
    private int missingIndex2;
    private RuleBase ruleBase;
    private Rule[] closestRules;
    private Observation observation;

    private Rule intermediateRule;

    public RuleBase getRuleBase() {
        return ruleBase;
    }

    public BackwardProcess(RuleBase base, Observation obs, Rule[] rules, int index1, int index2) {
        closestRules = rules;
        ruleBase = base;
        observation = obs;
        missingIndex1 = index1;
        missingIndex2 = index2;
    }

    /*public Rule getIntermediateRule() throws IllegalAccessException, InstantiationException {
        float[] totalNormalisedWeights = Process.getTotalNormalisedWeights(closestRules, observation, missingIndex1, missingIndex2);
    }*/

    public Rule backwardInterpolate(double[] parameters) throws Exception {
        float[][] normalisedWeights = Process.getNormalisedTermWeights(observation, closestRules, missingIndex1, missingIndex2);
        Rule rule = Process.getIntermediateFuzzyRule(
                closestRules, normalisedWeights, missingIndex1, missingIndex2,
                parameters[0], parameters[1], parameters[2], parameters[3]);
        rule.setAntecedent(missingIndex1, rule.getAntecedent(missingIndex1).adds(
                parameters[4] * (rule.getMaxAntecedentRanges()[missingIndex1] -
                        rule.getMinAntecedentRanges()[missingIndex1])));
        rule.setAntecedent(missingIndex2, rule.getAntecedent(missingIndex2).adds(
                parameters[5] * (rule.getMaxAntecedentRanges()[missingIndex2] -
                        rule.getMinAntecedentRanges()[missingIndex2])));
        rule = Process.multiBackwardInterpolation(
                rule, observation, missingIndex1, missingIndex2, parameters[6], parameters[7], parameters[8], parameters[9]);
        return rule;
    }

    public double evaluateForwardConsequence(Rule rule) throws Exception, InstantiationException {
        Rule intermediateRule = Process.getIntermediateFuzzyRule(Process.getClosestRules(ruleBase, rule, 2, false), rule, observation.getAntecedents().length);
        Rule transformedRule = Process.transform(intermediateRule, rule, observation.getAntecedents().length);
        double ratio = Math.abs(observation.getConsequence().getRepresentativeValue() - transformedRule.getConsequence().getRepresentativeValue());
        return ratio;
    }
}
