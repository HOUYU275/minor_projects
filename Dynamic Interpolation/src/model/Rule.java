package model;

import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 15/01/13
 * Time: 15:39
 */
public class Rule {
    protected FuzzyNumber[] antecedents;
    protected FuzzyNumber consequent;

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

    public Rule(FuzzyNumber[] antecedents) {
        this.antecedents = antecedents;
    }

    public Rule() {
    }

    public Rule(Rule rule) {
        antecedents = new FuzzyNumber[rule.getAntecedents().length];
    }

    public Rule(int numAntecedents) {
        antecedents = new FuzzyNumber[numAntecedents];
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
        return  s + getConsequent() + "\t" + getConsequent().rep();
    }

    public double distanceTo(Rule rule) {
        double sum = 0;
        for (int a = 0; a < getAntecedents().length; a++)
            sum += Math.pow(getAntecedents()[a].rep() - rule.getAntecedents()[a].rep(), 2);
        if (rule.getConsequent() != null && getConsequent() != null)
            sum += Math.pow(getConsequent().rep() - rule.getConsequent().rep(), 2);
        return Math.sqrt(sum);
    }

    public double distanceTo(Centroid centroid) {
        double sum = 0;
        for (int a = 0; a < getAntecedents().length; a++) sum += Math.pow(getAntecedents()[a].rep() - centroid.values[a], 2);
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

    public FuzzyNumber getConsequent() {
        return consequent;
    }

    public void setConsequent(FuzzyNumber consequent) {
        this.consequent = consequent;
    }
}
