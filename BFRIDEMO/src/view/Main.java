package view;

import control.RuleBaseGenerator;
import fuzzy.FuzzyNumber;
import model.CrowdednessFunction;
import model.ExplosionFunction;
import model.Observation;
import model.Rule;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Ren
 * Date: 31/07/12
 * Time: 20:15
 * To change this template use File | Settings | File Templates.
 */
public class Main extends JFrame {

    private static RuleBaseGenerator generator = new RuleBaseGenerator();
    private static ArrayList<Rule> propertyValueRuleBase = generator.generateRules(new CrowdednessFunction(), 15);
    private static ArrayList<Rule> burglaryRuleBase = generator.generateRules(new ExplosionFunction(), 15);

    private CombinedDisplay location1, crimeRate1, security1, propertyValue1, crimeRate2, security2, burglary2;
    private PlaceHolder placeHolder1, placeHolder2, placeHolder3;
    private InterpolateButtonPanel buttonPanel1, buttonPanel2;

    private Rule rule1, rule2;

    public Main() {
        setTitle("Fuzzy Rule Interpolation Demo");
        setSize(1200, 800); // default size is 0,0
        setLocation(10, 10); // default is 0,0 (top left corner)
    }

    public static void main(String[] args) {
        Main main = new Main();
        GridLayout layout = new GridLayout(0, 6);
        main.setLayout(layout);

        CombinedDisplay[] antecedents1 = new CombinedDisplay[3];
        CombinedDisplay[] antecedents2 = new CombinedDisplay[2];

        main.location1 = new CombinedDisplay("Location", main);
        main.add(main.location1);
        antecedents1[0] = main.location1;

        main.crimeRate1 = new CombinedDisplay("Crime Rate", main);
        main.add(main.crimeRate1);
        antecedents1[2] = main.crimeRate1;
        main.crimeRate2 = new CombinedDisplay("Crime Rate", main, main.crimeRate1.getComboBox().getSelectedIndex());

        main.crimeRate1.setSister(main.crimeRate2);
        main.crimeRate2.setSister(main.crimeRate1);

        main.security1 = new CombinedDisplay("Security", main);
        main.add(main.security1);
        antecedents1[1] = main.security1;
        main.security2 = new CombinedDisplay("Security", main, main.security1.getComboBox().getSelectedIndex());
        main.security1.setSister(main.security2);
        main.security2.setSister(main.security1);

        main.propertyValue1 = new CombinedDisplay("Property Value", main);
        main.propertyValue1.getDisplay().setFuzzyNumber(null);
        main.propertyValue1.getComboBox().setSelectedIndex(7);
        main.add(main.propertyValue1);

        main.placeHolder1 = new PlaceHolder();
        main.add(main.placeHolder1);

        main.buttonPanel1 = new InterpolateButtonPanel(main, main.rule1, propertyValueRuleBase, antecedents1, main.propertyValue1);
        main.add(main.buttonPanel1);

        main.placeHolder2 = new PlaceHolder();
        main.add(main.placeHolder2);

        main.add(main.crimeRate2);
        antecedents2[0] = main.crimeRate2;
        main.add(main.security2);
        antecedents2[1] = main.security2;

        main.placeHolder3 = new PlaceHolder();
        main.add(main.placeHolder3);

        main.burglary2 = new CombinedDisplay("Burglary", main);
        main.burglary2.getDisplay().setFuzzyNumber(null);
        main.burglary2.getComboBox().setSelectedIndex(7);
        main.add(main.burglary2);

        main.buttonPanel2 = new InterpolateButtonPanel(main, main.rule2, burglaryRuleBase, antecedents2, main.burglary2);
        main.add(main.buttonPanel2);

        main.makeRules();

        main.setVisible(true);
        main.pack();
    }

    public void makeRules() {
        this.rule1 = new Rule(propertyValueRuleBase.get(0));
        this.rule1.setAntecedents(
                new FuzzyNumber[]{
                        this.location1.getDisplay().getFuzzyNumber(),
                        this.security1.getDisplay().getFuzzyNumber(),
                        this.crimeRate1.getDisplay().getFuzzyNumber(),
                }
        );
        this.rule1.setConsequence(
                this.propertyValue1.getDisplay().getFuzzyNumber()
        );
        printRule(this.rule1);
        buttonPanel1.setRule(rule1);

        this.rule2 = new Rule(burglaryRuleBase.get(0));
        this.rule2.setAntecedents(
                new FuzzyNumber[]{
                        this.crimeRate2.getDisplay().getFuzzyNumber(),
                        this.security2.getDisplay().getFuzzyNumber(),
                }
        );
        this.rule2.setConsequence(
                this.burglary2.getDisplay().getFuzzyNumber()
        );
        printRule(this.rule2);
        buttonPanel2.setRule(rule2);

        int missingIndex = this.rule1.getAntecedents().length;
        for (int i = 0; i < this.rule1.getAntecedents().length; i++) {
            if (this.rule1.getAntecedents()[i] == null) missingIndex = i;
        }
        if ((missingIndex != this.rule1.getAntecedents().length) && this.rule1.getConsequence() == null) {

        } else {
            UIProcess.sortRules(propertyValueRuleBase, new Observation(
                    this.rule1.getAntecedents(), this.rule1.getConsequence()
            ));
        }
        missingIndex = this.rule2.getAntecedents().length;
        for (int i = 0; i < this.rule2.getAntecedents().length; i++) {
            if (this.rule2.getAntecedents()[i] == null) missingIndex = i;
        }
        if ((missingIndex != this.rule2.getAntecedents().length) && this.rule2.getConsequence() == null) {

        } else {
            UIProcess.sortRules(burglaryRuleBase, new Observation(
                    this.rule2.getAntecedents(), this.rule2.getConsequence()
            ));
        }
    }

    public static void printRule(Rule rule) {
        for (FuzzyNumber fuzzyNumber : rule.getAntecedents()) {
            System.out.print((fuzzyNumber == null) ? "(null) " : fuzzyNumber.toString() + " ");
        }
        System.out.println((rule.getConsequence() == null) ? "(null)" : rule.getConsequence().toString());
    }

}
