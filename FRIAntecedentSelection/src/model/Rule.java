package model;

import test.Main;

import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 15/01/13
 * Time: 15:39
 */
public class Rule implements Cloneable {
    protected FuzzyNumber[] antecedents;
    protected FuzzyNumber consequent;

    protected RuleBase ruleBase = Main.ruleBase;

    public Rule(FuzzyNumber[] antecedents, FuzzyNumber consequent) {
        this.antecedents = antecedents;
        this.setConsequent(consequent);
    }

    public Rule(int numAntecedent, String s) {
        String[] split = s.split(" ");
        antecedents = new FuzzyNumber[numAntecedent];
        for (int a = 0; a < numAntecedent; a++) getAntecedents()[a] = new FuzzyNumber(split[a]);
        setConsequent(new FuzzyNumber(split[numAntecedent]));
    }

    public Rule(FuzzyNumber[] antecedents, RuleBase ruleBase) {
        this.ruleBase = ruleBase;
        this.antecedents = antecedents.clone();
    }

    public Rule() {
    }

    public Rule(Rule rule) {
        antecedents = new FuzzyNumber[rule.getAntecedents().length];
    }

    public Rule(int numAntecedents) {
        antecedents = new FuzzyNumber[numAntecedents];
    }

    public RuleBase getRuleBase() {
        return ruleBase;
    }

    public void setRuleBase(RuleBase ruleBase) {
        this.ruleBase = ruleBase;
    }

    public double[] reps() {
        double[] reps = new double[getAntecedents().length + 1];
        for (int a = 0; a < getAntecedents().length; a++) reps[a] = getAntecedents()[a].rep();
        reps[getAntecedents().length] = getConsequent().rep();
        return reps;
    }

    @Override
    public String toString() {
        String s = "";
        for (FuzzyNumber f : getAntecedents()) s += f + "\t";
        return s + getConsequent() + "\t" + getConsequent().rep();
    }

/*    @Override
    public String toString() {
        String s = "";
        for (FuzzyNumber f : getAntecedents()) s += f.rep() + " ";
        int classLabel = (getConsequent() == null) ? Integer.MAX_VALUE : (int) Math.round(getConsequent().rep());
        return s + classLabel + ".0";
    }*/

    public double distanceTo(Rule rule, int missingIndex) {
        //System.out.println(rule);
        double sum = 0;
        if ((missingIndex >= 0) & (missingIndex < rule.getAntecedents().length)) {
            for (int a = 0; a < getAntecedents().length; a++) {
                if (a != missingIndex)
                    sum += Math.pow((getAntecedents()[a].rep() - rule.getAntecedents()[a].rep()) / (ruleBase.getRange(a)), 2);
            }
            sum += Math.pow((getConsequent().rep() - rule.getConsequent().rep()) / ruleBase.getRange(ruleBase.getMaximums().length - 1) * rule.getAntecedents().length, 2);
        } else {
            return distanceTo(rule);
        }
//        if (rule.getConsequent() != null && getConsequent() != null)
//            sum += Math.pow((getConsequent().rep() - rule.getConsequent().rep()) / ruleBase.getRange(ruleBase.getMaximums().length - 1), 2);
        return Math.sqrt(sum);
    }

    public double distanceTo(Rule rule, int missingIndex, double[] weights) {
        double sum = 0;
        if ((missingIndex >= 0) & (missingIndex < rule.getAntecedents().length)) {
        for (int a = 0; a < getAntecedents().length; a++) {
            if (getAntecedents()[a] != null || getAntecedents()[a] != new FuzzyNumber())
                sum += Math.pow((getAntecedents()[a].rep() - rule.getAntecedents()[a].rep()) * weights[a] / (ruleBase.getRange(a)), 2);
        }
            sum += Math.pow((getConsequent().rep() - rule.getConsequent().rep()) / ruleBase.getRange(ruleBase.getMaximums().length - 1) * rule.getAntecedents().length, 2) ;
        }
        else {
            return distanceTo(rule, weights);
        }
//        if (rule.getConsequent() != null && getConsequent() != null)
//            sum += Math.pow((getConsequent().rep() - rule.getConsequent().rep()) / ruleBase.getRange(ruleBase.getMaximums().length - 1), 2);
        return Math.sqrt(sum);
    }

    public double distanceTo(Rule rule) {
        //System.out.println(rule);
        double sum = 0;
        for (int a = 0; a < getAntecedents().length; a++) {
            if (getAntecedents()[a] != null || getAntecedents()[a] != new FuzzyNumber())
                sum += Math.pow((getAntecedents()[a].rep() - rule.getAntecedents()[a].rep()) / (ruleBase.getRange(a)), 2);
        }
//        if (rule.getConsequent() != null && getConsequent() != null)
//            sum += Math.pow((getConsequent().rep() - rule.getConsequent().rep()) / ruleBase.getRange(ruleBase.getMaximums().length - 1), 2);
        return Math.sqrt(sum);
    }

    public double distanceTo(Rule rule, double[] weights) {
        //System.out.println(rule);
        double sum = 0;
        for (int a = 0; a < getAntecedents().length; a++) {
            if (getAntecedents()[a] != null || getAntecedents()[a] != new FuzzyNumber())
                sum += Math.pow((getAntecedents()[a].rep() - rule.getAntecedents()[a].rep()) * weights[a] / (ruleBase.getRange(a)), 2);
        }
//        if (rule.getConsequent() != null && getConsequent() != null)
//            sum += Math.pow((getConsequent().rep() - rule.getConsequent().rep()) / ruleBase.getRange(ruleBase.getMaximums().length - 1), 2);
        return Math.sqrt(sum);
    }

    public double distanceTo(Centroid centroid) {
        double sum = 0;
        for (int a = 0; a < getAntecedents().length; a++)
            sum += Math.pow(getAntecedents()[a].rep() - centroid.values[a], 2);
        sum += Math.pow(getConsequent().rep() - centroid.values[getAntecedents().length], 2);
        return Math.sqrt(sum);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule rule = (Rule) o;
        return Arrays.equals(getAntecedents(), rule.getAntecedents()) && getConsequent().equals(rule.getConsequent());
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(getAntecedents());
        result = 31 * result + getConsequent().hashCode();
        return result;
    }

    @Override
    public Rule clone() throws CloneNotSupportedException {
        return new Rule(getAntecedents().clone(), getConsequent().clone());
    }

    public FuzzyNumber[] getAntecedents() {
        return antecedents;
    }

    public void setAntecedent(int index, FuzzyNumber number) {
        this.antecedents[index] = number;
    }

    public FuzzyNumber getConsequent() {
        return consequent;
    }

    public void setConsequent(FuzzyNumber consequent) {
        this.consequent = consequent;
    }

    public void trim(Mask mask) {
        FuzzyNumber[] trimed = new FuzzyNumber[mask.mask.length];
        int index = 0;
        for (int m = 0; m < mask.mask.length; m++) {
            trimed[index] = antecedents[mask.mask[m]];
            index++;
        }
        this.antecedents = trimed;
    }
}
