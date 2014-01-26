package model;

import control.RuleBaseGenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 15/01/13
 * Time: 15:42
 */
public class RuleBase extends ArrayList<Rule> implements Cloneable {
    protected double[] maximums, minimums;
    protected RuleBaseGenerator ruleBaseGenerator;
    protected Mask mask;

    public RuleBase(int numAntecedents) {
        super();
        maximums = new double[numAntecedents + 1];
        minimums = new double[numAntecedents + 1];
        for (int m = 0; m < maximums.length; m++) {
            maximums[m] = -Double.MAX_VALUE;
            minimums[m] = Double.MAX_VALUE;
        }
    }

    public RuleBase(RuleBaseGenerator ruleBaseGenerator) {
        super();
        this.setRuleBaseGenerator(ruleBaseGenerator);
        maximums = new double[ruleBaseGenerator.getFunction().getNumAntecedents() + 1];
        minimums = new double[ruleBaseGenerator.getFunction().getNumAntecedents() + 1];
        for (int m = 0; m < maximums.length; m++) {
            maximums[m] = -Double.MAX_VALUE;
            minimums[m] = Double.MAX_VALUE;
        }
    }

    public double getRange(int antecedent) {
        return getMaximums()[antecedent] - getMinimums()[antecedent];
    }

    public Mask getMask() {
        return mask;
    }

    public void setMask(Mask mask) {
        this.mask = mask;
    }

    @Override
    public Object clone() {
        RuleBase clonedRuleBase = new RuleBase(this.ruleBaseGenerator);
        for (int r = 0; r < this.size(); r++)
            try {
                clonedRuleBase.add(this.get(r).clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        clonedRuleBase.maximums = this.maximums;
        clonedRuleBase.minimums = this.minimums;
        return clonedRuleBase;
    }

    @Override
    public boolean add(Rule rule) {
        for (int a = 0; a < getMaximums().length - 1; a++) {
            if (super.isEmpty()) maximums[a] = rule.getAntecedents()[a].getA3();
            getMaximums()[a] = rule.getAntecedents()[a].getA3() > maximums[a] ? rule.getAntecedents()[a].getA3() : getMaximums()[a];
        }
        maximums[maximums.length - 1] = rule.getConsequent().getA3() > maximums[maximums.length - 1] ? rule.getConsequent().getA3() : maximums[maximums.length - 1];
        for (int a = 0; a < getMinimums().length - 1; a++) {
            if (super.isEmpty()) getMinimums()[a] = rule.getAntecedents()[a].getA1();
            getMinimums()[a] = rule.getAntecedents()[a].getA1() < getMinimums()[a] ? rule.getAntecedents()[a].getA1() : getMinimums()[a];
        }
        getMinimums()[getMinimums().length - 1] = rule.getConsequent().getA1() < getMinimums()[getMinimums().length - 1] ? rule.getConsequent().getA1() : getMinimums()[getMinimums().length - 1];
        rule.setRuleBase(this);
        return super.add(rule);
    }

    public double[] getMaximums() {
        return maximums;
    }

    public double[] getMinimums() {
        return minimums;
    }

    public RuleBaseGenerator getRuleBaseGenerator() {
        return ruleBaseGenerator;
    }

    public void setRuleBaseGenerator(RuleBaseGenerator ruleBaseGenerator) {
        this.ruleBaseGenerator = ruleBaseGenerator;
    }

    public Rule check(Rule observation) {
        double dMin = Double.MAX_VALUE;
        Rule closestRule = null;
        for (Rule r : this) {
            double d = r.distanceTo(observation);
            if (d < dMin) {
                dMin = d;
                closestRule = r;
            }
        }
        if (dMin < 0.05) return closestRule;
        return null;
    }

    public double verify(Rule observation) {
        if (mask != null) {
            FuzzyNumber[] fuzzyNumbers = new FuzzyNumber[ruleBaseGenerator.getFunction().getNumAntecedents()];

            for (int m = 0; m < mask.mask.length; m++) {
                fuzzyNumbers[mask.mask[m]] = observation.getAntecedents()[m];
            }
            for (int f = 0; f < fuzzyNumbers.length; f++) {
                if (fuzzyNumbers[f] == null) fuzzyNumbers[f] = new FuzzyNumber(0,0,0);
            }

            return Math.abs(observation.getConsequent().rep() - ruleBaseGenerator.getFunction().calculate(fuzzyNumbers)) /getRange(maximums.length-1);
        }
        //System.out.println(ruleBaseGenerator.getFunction().calculate(observation.getAntecedents()));
        return Math.abs(observation.getConsequent().rep() - ruleBaseGenerator.getFunction().calculate(observation.getAntecedents())) /getRange(maximums.length-1);
    }


    public void export(String fileName) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(fileName + ".arff")));
        writer.write("@RELATION Test");
        writer.newLine();
        writer.newLine();
        for (int i = 0; i < this.get(0).getAntecedents().length + 1; i++)  {
            writer.write("@ATTRIBUTE " + i +" NUMERIC");
            writer.newLine();
        }
        writer.newLine();
        writer.write("@DATA");
        writer.newLine();
        String line = "";
        for (int r = 0; r < this.size(); r++){
            for (int a = 0; a < this.get(r).getAntecedents().length; a++){
                BigDecimal bd = new BigDecimal(this.get(r).getAntecedents()[a].rep());
                BigDecimal rounded = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                line += rounded.doubleValue() + " ";
            }
            line += (int)Math.round(this.get(r).getConsequent().rep()) + ".0";
            writer.write(line);
            writer.newLine();
            line = "";
        }
        writer.close();
    }

    public void load(String fileName) throws IOException {
        this.clear();
        Scanner scanner = new Scanner(new File(fileName + ".arff"));
        while (scanner.hasNext()) {
            if (scanner.nextLine().contains("@DATA")) break;
        }
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] splits = line.split(" ");
            FuzzyNumber [] fuzzyNumbers = new FuzzyNumber[splits.length-1];
            for (int f = 0; f < fuzzyNumbers.length; f++) fuzzyNumbers[f] = ruleBaseGenerator.fuzzify(Double.parseDouble(splits[f]));
            FuzzyNumber consequent = ruleBaseGenerator.fuzzify(Double.parseDouble(splits[splits.length - 1]));
            this.add(new Rule(fuzzyNumbers, consequent));
        }
        scanner.close();
        System.out.println("Loaded Rule Base [" + this.size() + "]");
    }

    public void trim(Mask mask) {
        this.setMask(mask);
        for (int r = 0; r < this.size(); r++) {
            FuzzyNumber[] originals = this.get(r).getAntecedents();
            FuzzyNumber[] trimed = new FuzzyNumber[mask.mask.length];
            int index = 0;
            for (int m = 0; m < mask.mask.length; m++) {
                trimed[index] = originals[mask.mask[m]];
                index++;
            }
            this.get(r).antecedents = trimed;
        }
        double[] trimedMaximums = new double[mask.mask.length];
        double[] trimedMinimums = new double[mask.mask.length];
        int index = 0;
        for (int m = 0; m < mask.mask.length; m++) {
            trimedMaximums[index] = this.maximums[mask.mask[m]];
            trimedMinimums[index] = this.minimums[mask.mask[m]];
            index++;
        }
    }

    public double verify(Rule observation, Rule rule, int missingIndex) {
        return Math.abs(observation.getAntecedents()[missingIndex].rep() - rule.getAntecedents()[missingIndex].rep())/rule.getRuleBase().getRange(missingIndex);
    }
}
