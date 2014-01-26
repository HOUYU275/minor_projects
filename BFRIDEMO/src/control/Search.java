package control;

import model.Observation;
import model.Rule;
import model.RuleComparator;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created with IntelliJ IDEA.
 * User: Ren
 * Date: 01/08/12
 * Time: 00:13
 * To change this template use File | Settings | File Templates.
 */
public class Search {

    public static Rule findClosest(Observation observation, ArrayList<Rule> rules) {
        sortRules(rules, observation);
        return rules.get(0);
    }

    private static void sortRules(ArrayList<Rule> rules, Observation observation) {
        Collections.sort(rules, new RuleComparator(observation, true));
        //TestMain.printRules(rules);
    }

}
