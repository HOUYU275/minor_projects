package control;

import model.FuzzyNumber;
import model.Rule;
import model.RuleBase;
import model.RuleComparator;

import java.util.Collections;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 28/07/11
 * Time: 14:17
 */
public class TransformationBasedInterpolation {

    private boolean verbose = true;

    public static Rule interpolate(RuleBase base, Rule observation) throws Exception {
        Rule temp = new Rule(observation.getAntecedents());
        Rule[] closestRules = TransformationBasedInterpolation.getClosestRules(base, temp);
        Rule intermediateRule = TransformationBasedInterpolation.getIntermediateRule(closestRules[0], closestRules[1], temp, temp.getAntecedents().length);
        return TransformationBasedInterpolation.transform(intermediateRule, temp, temp.getAntecedents().length);
    }

    public static double getDelta(FuzzyNumber aStar, FuzzyNumber a1, FuzzyNumber a2) {
        return Math.abs((a1.rep() - aStar.rep()) / (a1.rep() - a2.rep()));
    }

    public static FuzzyNumber getIntermediateFuzzyTerm(FuzzyNumber f1, FuzzyNumber f2, double delta) {
        double newLeftShoulder = (1 - delta) * f1.getA1() + delta * f2.getA1();
        double newCentroid = (1 - delta) * f1.getA2() + delta * f2.getA2();
        double newRightShoulder = (1 - delta) * f1.getA3() + delta * f2.getA3();
        return new FuzzyNumber(newLeftShoulder, newCentroid, newRightShoulder);
    }

    public static double getScaleRate(FuzzyNumber aDash, FuzzyNumber aStar) {
        return (aStar.getA3() - aStar.getA1()) /
                (aDash.getA3() - aDash.getA1());
    }

    public static FuzzyNumber scale(FuzzyNumber fuzzyNumber, double scaleRate) throws Exception {
        if (scaleRate < 0) throw new Exception("Scale Rate (" + scaleRate + ") cannot be less than 0");
        double newLeftShoulder = (fuzzyNumber.getA1() * (1 + 2 * scaleRate) +
                fuzzyNumber.getA2() * (1 - scaleRate) +
                fuzzyNumber.getA3() * (1 - scaleRate)) / 3;
        double newCentroid = (fuzzyNumber.getA1() * (1 - scaleRate) +
                fuzzyNumber.getA2() * (1 + 2 * scaleRate) +
                fuzzyNumber.getA3() * (1 - scaleRate)) / 3;
        double newRightShoulder = (fuzzyNumber.getA1() * (1 - scaleRate) +
                fuzzyNumber.getA2() * (1 - scaleRate) +
                fuzzyNumber.getA3() * (1 + 2 * scaleRate)) / 3;
        return new FuzzyNumber(newLeftShoulder, newCentroid, newRightShoulder);
    }

    public static double getMoveRatio(FuzzyNumber aDashScaled, FuzzyNumber aStar) throws Exception {
        double moveRatio;
        if (aStar.getA1() >= aDashScaled.getA1()) {
            moveRatio = (aStar.getA1() - aDashScaled.getA1()) * 3 /
                    (aDashScaled.getA2() - aDashScaled.getA1());
            if ((moveRatio < 0) || (moveRatio > 1))
                throw new Exception("Move Ratio (" + moveRatio + ") cannot exceed [0,1]");
        } else {
            moveRatio = (aStar.getA1() - aDashScaled.getA1()) * 3 /
                    (aDashScaled.getA3() - aDashScaled.getA2());
            if ((moveRatio > 0) || (moveRatio < -1))
                throw new Exception("Move Ratio (" + moveRatio + ") cannot exceed [-1,0]");
        }
        return moveRatio;
    }

    public static FuzzyNumber move(FuzzyNumber fuzzyNumber, double moveRatio) {
        double moveRate = (moveRatio >= 0) ? moveRatio * (fuzzyNumber.getA2() - fuzzyNumber.getA1()) / 3
                : moveRatio * (fuzzyNumber.getA3() - fuzzyNumber.getA2()) / 3;
        return new FuzzyNumber(fuzzyNumber.getA1() + moveRate,
                fuzzyNumber.getA2() - 2 * moveRate,
                fuzzyNumber.getA3() + moveRate);
    }

    public static Rule getIntermediateRule(Rule rule1, Rule rule2, Rule observation, int dimensionToBeInterpolated) {
        double deltaSum = 0;
        double delta;
        Rule intermediateRule = new Rule(rule1);

        if ((dimensionToBeInterpolated >= 0) & (dimensionToBeInterpolated < observation.getAntecedents().length)) {
            //backward
            for (int i = 0; i < observation.getAntecedents().length; i++) {
                if (i != dimensionToBeInterpolated) {
                    delta = getDelta(observation.getAntecedents()[i],
                            rule1.getAntecedents()[i],
                            rule2.getAntecedents()[i]);
                    deltaSum = deltaSum + delta;
                    intermediateRule.getAntecedents()[i] =
                            getIntermediateFuzzyTerm(rule1.getAntecedents()[i], rule2.getAntecedents()[i], delta);
                }
            }
            delta = getDelta(observation.getConsequent(),
                    rule1.getConsequent(),
                    rule2.getConsequent());

            intermediateRule.setConsequent(getIntermediateFuzzyTerm(rule1.getConsequent(), rule2.getConsequent(), delta));

            delta = delta * observation.getAntecedents().length - deltaSum;
            intermediateRule.getAntecedents()[dimensionToBeInterpolated] =
                    getIntermediateFuzzyTerm(
                            rule1.getAntecedents()[dimensionToBeInterpolated],
                            rule2.getAntecedents()[dimensionToBeInterpolated], delta);

        } else {
            //forward
            for (int i = 0; i < observation.getAntecedents().length; i++) {
                delta = getDelta(observation.getAntecedents()[i],
                        rule1.getAntecedents()[i],
                        rule2.getAntecedents()[i]);
                deltaSum = deltaSum + delta;
                intermediateRule.getAntecedents()[i] =
                        getIntermediateFuzzyTerm(rule1.getAntecedents()[i], rule2.getAntecedents()[i], delta);
            }
            delta = deltaSum / observation.getAntecedents().length;
            intermediateRule.setConsequent(getIntermediateFuzzyTerm(rule1.getConsequent(), rule2.getConsequent(), delta));
        }
        //   System.out.println("delta=" + delta);
      /*  for (int j = 0; j < intermediateRule.antecedents.length; j++) {
            System.out.println(j + " antecedent = " + intermediateRule.antecedents[j].toString());
        }
*/
        // System.out.println("Consequence = " + intermediateRule.consequent.toString());
        return intermediateRule;
    }

    public static Rule transform(Rule intermediateRule, Rule observation, int index) throws Exception {
        Rule transformedRule = new Rule(intermediateRule);
        double sSum = 0;
        double s;
        double mSum = 0;
        double m;
        if ((index >= 0) & (index < observation.getAntecedents().length)) {
            //backward
            for (int i = 0; i < observation.getAntecedents().length; i++) {
                if (i != index) {
                    s = getScaleRate(intermediateRule.getAntecedents()[i], observation.getAntecedents()[i]);
                    System.out.println(i + "'th dimension: " + "s=" + s);
                    intermediateRule.getAntecedents()[i] = scale(intermediateRule.getAntecedents()[i], s);
                    sSum = sSum + s;

                    m = getMoveRatio(intermediateRule.getAntecedents()[i], observation.getAntecedents()[i]);
                    System.out.println(i + "'th dimension: " + "m=" + m);
                    transformedRule.getAntecedents()[i] = move(intermediateRule.getAntecedents()[i], m);
                    mSum = mSum + m;
                }
            }

            s = getScaleRate(intermediateRule.getConsequent(), observation.getConsequent());
            intermediateRule.setConsequent(scale(intermediateRule.getConsequent(), s));
            System.out.println("Consequence dimension: " + "s=" + s);
            m = getMoveRatio(intermediateRule.getConsequent(), observation.getConsequent());
            transformedRule.setConsequent(move(intermediateRule.getConsequent(), m));
            System.out.println("Consequence dimension: " + "m=" + m);

            s = s * observation.getAntecedents().length - sSum;
            System.out.println(index + " dimension: " + "s=" + s);
            m = m * observation.getAntecedents().length - mSum;
            System.out.println(index + " dimension: " + "m=" + m);
            transformedRule.getAntecedents()[index] = move(scale(intermediateRule.getAntecedents()[index], s), m);
        } else {
            //forward
            for (int i = 0; i < observation.getAntecedents().length; i++) {
                s = getScaleRate(intermediateRule.getAntecedents()[i], observation.getAntecedents()[i]);
                intermediateRule.getAntecedents()[i] = scale(intermediateRule.getAntecedents()[i], s);
                sSum = sSum + s;

                m = getMoveRatio(intermediateRule.getAntecedents()[i], observation.getAntecedents()[i]);
                transformedRule.getAntecedents()[i] = move(intermediateRule.getAntecedents()[i], m);
                mSum = mSum + m;
            }
            s = sSum / observation.getAntecedents().length;
            m = mSum / observation.getAntecedents().length;
            transformedRule.setConsequent(move(scale(intermediateRule.getConsequent(), s), m));
        }
        return transformedRule;
    }

    public static void sortRules(RuleBase base, Rule observation) {
        Collections.sort(base, new RuleComparator(observation));
    }

    public static void printRules(Rule[] rules) {
        for (Rule rule : rules) {
            for (FuzzyNumber fuzzyNumber : rule.getAntecedents()) System.out.print(fuzzyNumber.toString() + " ");
            System.out.println(rule.getConsequent().toString());
        }
    }

    public static Rule[] getClosestRules(RuleBase base, Rule observation) throws Exception {
        Rule[] closestRules = new Rule[2];
        sortRules(base, observation);
        double distance = base.get(0).distanceTo(observation) + base.get(1).distanceTo(observation);
        //System.out.println(base);
        int min1 = 0, min2 = 1;
        while (min2 < base.size() && !isFlanking(observation, base.get(min1), base.get(min2))) min2++;
        while (min2 == base.size()) {
            min1++;
            min2 = min1 + 1;
            while (min2 < base.size() && !isFlanking(observation, base.get(min1), base.get(min2))) min2++;
        }
        if (min1 == base.size() || base.get(min1).distanceTo(observation) > distance)
            throw new Exception("Interpolation Failed, No Matching Rules");
        try {
            closestRules[0] = (Rule) base.get(min1).clone();
            //System.out.println(closestRules[0]);
            closestRules[1] = (Rule) base.get(min2).clone();
            //System.out.println(closestRules[1]);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return closestRules;
    }

    public static boolean isFlanking(Rule observation, Rule r1, Rule r2) {
        boolean flanking = true;
        for (int a = 0; a < observation.getAntecedents().length; a++) {
            flanking &= (observation.getAntecedents()[a].rep() - r1.getAntecedents()[a].rep())
                    * (observation.getAntecedents()[a].rep() - r2.getAntecedents()[a].rep()) < 0;
        }
        return flanking;
    }

    /*private Rule[] getClosestRules(FuzzyNumber subConsequence, ArrayList<Rule> rules) {
        Rule[] closestRules = new Rule[2];
        double[] distances = new double[rules.size()];
        for (int i = 0; i < rules.size(); i++) {
            distances[i] = rules.get(i).consequent.rep() - subConsequence.rep();
        }
        double minDistance1 = Double.MAX_VALUE;
        int minIndex1 = 0;
        double minDistance2 = Double.MAX_VALUE;
        int minIndex2 = 0;

        for (int i = 0; i < rules.size(); i++) {
            if ((Math.abs(distances[i]) < minDistance1) & (distances[i] * distances[minIndex1] < 0)) {
                minDistance2 = minDistance1;
                minIndex2 = minIndex1;
                minDistance1 = Math.abs(distances[i]);
                minIndex1 = i;
            } else if ((Math.abs(distances[i]) < minDistance2) & (distances[i] * distances[minIndex1] < 0)) {
                minDistance2 = Math.abs(distances[i]);
                minIndex2 = i;
            }
        }
        try {
            closestRules[0] = (Rule) rules.get(minIndex1).clone();
            closestRules[1] = (Rule) rules.get(minIndex2).clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return closestRules;
    }*/

}
