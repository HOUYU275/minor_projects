package control;

import model.Function;
import model.FuzzyNumber;
import model.Rule;
import model.RuleBase;

import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 28/07/11
 * Time: 20:11
 */
public class RuleBaseGenerator {

    private Function function;
    private double spread;

    public RuleBaseGenerator(Function function, double spread) {
        this.function = function;
        this.spread = spread;
    }

    public Function getFunction() {
        return function;
    }

    public Rule newObservation(int numberOfVariables, double[] ranges) {
        FuzzyNumber[] observationAntecedents = new FuzzyNumber[numberOfVariables];
        Random random = new Random();
        double a0 = 1 + random.nextInt((int) ranges[0]);
        double a1 = 1 + random.nextInt((int) ranges[1]);
        double a2 = 1 + random.nextInt((int) ranges[2]);
        observationAntecedents[0] = new FuzzyNumber(a0 - 1, a0, a0 + 1);
        observationAntecedents[1] = new FuzzyNumber(a1 - 1, a1, a1 + 1);
        observationAntecedents[2] = new FuzzyNumber(a2 - 1, a2, a2 + 1);
        return new Rule(observationAntecedents);
    }

    public double[] getCrispInputs(int numberOfVariables, Rule rule) {
        double[] crispInput = new double[numberOfVariables];
        for (int i = 0; i < crispInput.length; i++) {
            crispInput[i] = rule.getAntecedents()[i].rep();
        }
        return crispInput;
    }

    public RuleBase generateRuleBase(int size) {
        RuleBase base = new RuleBase(function.getNumAntecedents());
        base.setRuleBaseGenerator(this);
        while (size-- > 0) base.add(generateRule());
        return base;
    }

    public RuleBase add(RuleBase base, int size) {
        while (size-- > 0) base.add(generateRule());
        return base;
    }

    public Rule generateObservation() {
        Random random = new Random();
        double[] inputs = new double[function.getNumAntecedents()];
        for (int a = 0; a < function.getNumAntecedents(); a++)
            inputs[a] = random.nextDouble() * (function.getMaximums()[a] - function.getMinimums()[a]) + function.getMinimums()[a];
        return new Rule(fuzzify(inputs));
    }

    public Rule generateRule() {
        Random random = new Random();
        double[] inputs = new double[function.getNumAntecedents()];
        for (int a = 0; a < function.getNumAntecedents(); a++) {
            inputs[a] = random.nextDouble() * (function.getMaximums()[a] - function.getMinimums()[a]) + function.getMinimums()[a];
        }
        double result = function.calculate(inputs);
        return new Rule(fuzzify(inputs), fuzzify(result));
    }

    public FuzzyNumber fuzzify(double number) {
        return new FuzzyNumber(number - spread / 2, number, number + spread / 2);
    }

    public FuzzyNumber[] fuzzify(double[] numbers) {
        FuzzyNumber[] fuzzyNumbers = new FuzzyNumber[numbers.length];
        for (int n = 0; n < numbers.length; n++) fuzzyNumbers[n] = fuzzify(numbers[n]);
        return fuzzyNumbers;
    }
}
