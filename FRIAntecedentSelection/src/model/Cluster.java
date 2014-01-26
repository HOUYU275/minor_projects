package model;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 15/01/13
 * Time: 17:39
 */
public class Cluster extends ArrayList<Rule> {
    protected Centroid centroid;
    protected Rule previousCentroid;

    public Cluster() {
        super();
    }

    public Cluster(Rule rule) {
        super();
        super.add(rule);
        centroid = new Centroid(rule);
    }

    public Rule merge() {
        double[][] weights = new double[getCentroid().values.length][size()];
        double[] sumWeight = new double[getCentroid().values.length];
        for (int a = 0; a < weights.length; a++) {
            for (int r = 0; r < weights[a].length; r++) {
                if (a == weights.length - 1)
                    weights[a][r] = 1 / Math.abs(get(r).getConsequent().rep() - getCentroid().values[a]);
                else weights[a][r] = 1 / Math.abs(get(r).getAntecedents()[a].rep() - getCentroid().values[a]);
                sumWeight[a] += weights[a][r];
            }
        }
        for (int a = 0; a < weights.length; a++)
            for (int r = 0; r < weights[a].length; r++) weights[a][r] /= sumWeight[a];
        for (int r = 0; r < weights[0].length; r++) {
            //System.out.print("$w_{" + r + ",j}$\t");
            //for (int a = 0; a < weights.length; a++) System.out.print(weights[a][r] + "\t");
            //System.out.println();
        }
        FuzzyNumber[] antecedents = new FuzzyNumber[weights.length - 1];
        FuzzyNumber consequent = new FuzzyNumber();
        for (int a = 0; a < antecedents.length; a++) {
            antecedents[a] = new FuzzyNumber();
            for (int r = 0; r < size(); r++) {
                antecedents[a].a1 = antecedents[a].getA1() + weights[a][r] * get(r).getAntecedents()[a].getA1();
                antecedents[a].a2 = antecedents[a].getA2() + weights[a][r] * get(r).getAntecedents()[a].getA2();
                antecedents[a].a3 = antecedents[a].getA3() + weights[a][r] * get(r).getAntecedents()[a].getA3();
            }
        }
        for (int r = 0; r < size(); r++) {
            consequent.a1 = consequent.getA1() + weights[weights.length - 1][r] * get(r).getConsequent().getA1();
            consequent.a2 = consequent.getA2() + weights[weights.length - 1][r] * get(r).getConsequent().getA2();
            consequent.a3 = consequent.getA3() + weights[weights.length - 1][r] * get(r).getConsequent().getA3();
        }
        return new Rule(antecedents, consequent);
    }

    public Centroid getCentroid() {
        return centroid;
    }
}
