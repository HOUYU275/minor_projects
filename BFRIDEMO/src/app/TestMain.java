package app;

import control.Search;
import fuzzy.FuzzyNumber;
import control.RuleBaseGenerator;
import model.*;


import java.util.ArrayList;

import static java.util.Collections.sort;

/**
 * Created by IntelliJ IDEA.
 * User: rxd846
 * Date: Feb 1, 2010
 * Time: 2:24:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestMain {
    private static int numberClosestRules = 2;

    private static RuleBaseGenerator generator = new RuleBaseGenerator();
    private static ArrayList<Rule> crowdednessRuleBase = generator.generateRules(new CrowdednessFunction(), 7);

    public static void main(String args[]) throws Exception {

        Observation crowdednessObservation = new Observation(
                crowdednessRuleBase.get(13).getAntecedents()
        );

        //printRules(crowdednessRuleBase);

        for (FuzzyNumber fuzzyNumber : crowdednessRuleBase.get(13).getAntecedents()) {
            System.out.print(fuzzyNumber.toString() + " ");
        }
        System.out.println(crowdednessRuleBase.get(13).getConsequence().toString());

        Rule rule = Search.findClosest(crowdednessObservation, crowdednessRuleBase);


    }

    public static void printRules(ArrayList<Rule> rules) {
        for (int i = 0; i < rules.size(); i++) {
            for (FuzzyNumber fuzzyNumber : rules.get(i).getAntecedents()) {
                System.out.print(fuzzyNumber.toString() + " ");
            }
            System.out.println(rules.get(i).getConsequence().toString());
        }
    }




}
