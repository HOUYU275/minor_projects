package model;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 28/07/11
 * Time: 19:36
 */
public abstract class Function {

    protected int numAntecedents;
    protected double[] minimums;
    protected double[] maximums;

    public Function(int numAntecedents, double[] minimums, double[] maximums) {
        this.numAntecedents = numAntecedents;
        this.minimums = minimums;
        this.maximums = maximums;
    }

    public abstract double calculate(double[] inputs);

    public abstract double calculate(FuzzyNumber[] fuzzyInputs);

    public int getNumAntecedents() {
        return numAntecedents;
    }

    public double[] getMinimums() {
        return minimums;
    }

    public double[] getMaximums() {
        return maximums;
    }
}
