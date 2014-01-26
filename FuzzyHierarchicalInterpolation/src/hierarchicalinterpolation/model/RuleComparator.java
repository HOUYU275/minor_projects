package hierarchicalinterpolation.model;

import java.util.Comparator;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 28/07/11
 * Time: 14:52
 * To change this template use File | Settings | File Templates.
 */
public class RuleComparator implements Comparator{

    private Rule observation;
    private Boolean useWeightedConsequenceDistance;

    public RuleComparator(Rule observation, boolean useWeightedConsequenceDistance) {
        this.observation = observation;
        this.useWeightedConsequenceDistance = useWeightedConsequenceDistance;
    }

    public int compare(Object o1, Object o2) {
        Rule rule1 = (Rule)o1;
        Rule rule2 = (Rule)o2;

        double distance1 = rule1.calculateDistance(observation, useWeightedConsequenceDistance);
        double distance2 = rule2.calculateDistance(observation, useWeightedConsequenceDistance);

        /*for (int i = 0; i < observation.getAntecedents().length; i++) {
            System.out.print(observation.getAntecedents()[i] + " ");
        }
        System.out.println("");
        for (int i = 0; i < rule1.getAntecedents().length; i++) {
            System.out.print(rule1.getAntecedents()[i] + " ");
        }
        System.out.println("\nvs");
        for (int i = 0; i < rule2.getAntecedents().length; i++) {
            System.out.print(rule2.getAntecedents()[i] + " ");
        }
        System.out.println();
        System.out.println(distance1 + " vs " + distance2);
        System.out.println(" ------------------------- ");*/

        if (distance1 > distance2) return 1;
        if (distance1 < distance2) return -1;
        return 0;
    }

}
