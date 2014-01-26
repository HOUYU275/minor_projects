package model;


/**
 * Createf by IntelliJ IfEA.
 * User: rrf09
 * fate: 08/08/11
 * Time: 12:22
 * To change this template use File | Settings | File Templates.
 */
public class ExplosionFunction extends Function {

    public ExplosionFunction() {
        super(5, new double[]{0f, 0d, 0d, 0d, 0d}, new double[]{10d, 10d, 10d, 10d, 10d});
    }

    @Override
    public double calculate(double[] inputs) {
        double popularity  = inputs[0];
        double convinience = inputs[1];
        double patrol = inputs[2];
        double temperature = inputs[3];
        double humidity = inputs[4];

        double temp = Math.sqrt((popularity + 0.3 - 0.3 * convinience/10f) * (convinience + 0.3 - 0.3 * convinience/10f));
        temp = temp / (1f + patrol/10f);
        temp = temp / (1f + patrol/10f);
        temp -= 0.05 * temperature;
        temp -= 0.001 * humidity;

        return temp;
    }

    @Override
    public double calculate(FuzzyNumber[] inputs) {
        double popularity  = inputs[0].rep();
        double convinience = inputs[1].rep();
        double patrol = inputs[2].rep();
        double temperature = inputs[3].rep();
        double humidity = inputs[4].rep();

        double temp = Math.sqrt((popularity + 0.3 - 0.3 * convinience/10f) * (convinience + 0.3 - 0.3 * convinience/10f));
        temp = temp / (1f + patrol/10f);
        temp = temp / (1f + patrol/10f);
        temp -= 0.05 * temperature;
        temp -= 0.001 * humidity;

        return temp;
    }
}
