package view;

import fuzzy.FuzzyNumber;
import model.Observation;
import model.Rule;
import model.RuleComparator;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created with IntelliJ IDEA.
 * User: Ren
 * Date: 31/07/12
 * Time: 21:29
 * To change this template use File | Settings | File Templates.
 */
public class UIProcess {

    public static FuzzyNumber convertLinguisticVariable(String option) {
        switch (option) {
            case "Very Low":
                return new FuzzyNumber(0.4, 1.4, 2.4);
            case "Low":
                return new FuzzyNumber(1.6, 2.6, 3.6);
            case "Moderate Low":
                return new FuzzyNumber(2.8, 3.8, 4.8);
            case "Moderate":
                return new FuzzyNumber(4.0, 5.0, 6.0);
            case "Moderate High":
                return new FuzzyNumber(5.2, 6.2, 7.2);
            case "High":
                return new FuzzyNumber(6.4, 7.4, 8.4);
            case "Very High":
                return new FuzzyNumber(7.6, 8.6, 9.6);
            case "Missing":
                return null;
            default:
                return null;
        }

    }

    public static String translate(double value) {
        if (value <= 1.4)
            return "Very Low";
        if (value <= 2.6)
            return "Low";
        if (value <= 3.8)
            return "Moderate Low";
        if (value <= 5.0)
            return "Moderate";
        if (value <= 6.2)
            return "Moderate High";
        if (value <= 7.4)
            return "High";
        if (value <= 8.6)
            return "Very High";
        else
            return "Very High";
    }

    public static void sortRules(ArrayList<Rule> rules, Observation observation) {
        Collections.sort(rules, new RuleComparator(observation, true));
        //TestMain.printRules(rules);
    }

}
