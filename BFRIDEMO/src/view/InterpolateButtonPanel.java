package view;

import fuzzy.FuzzyNumber;
import model.Rule;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Ren
 * Date: 01/08/12
 * Time: 00:25
 * To change this template use File | Settings | File Templates.
 */
public class InterpolateButtonPanel extends JPanel implements ActionListener {
    Main main;
    Rule rule;
    ArrayList<Rule> rules;
    CombinedDisplay[] antecedents;
    CombinedDisplay consequence;

    public InterpolateButtonPanel(Main main, Rule rule, ArrayList<Rule> rules, CombinedDisplay[] antecedents, CombinedDisplay consequence) {

        super(new GridLayout(0, 1));

        this.main = main;
        this.rule = rule;
        this.rules = rules;
        this.antecedents = antecedents;
        this.consequence = consequence;

        this.setPreferredSize(new Dimension(200, 400));
        JButton button = new JButton("Interpolate");
        button.addActionListener(this);
        this.add(button);
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public void actionPerformed(ActionEvent e) {

        main.makeRules();

        int missingIndex = antecedents.length;
        for (int i = 0; i < rule.getAntecedents().length; i++) {
            if (rule.getAntecedents()[i] == null) missingIndex = i;
        }

        if (missingIndex < antecedents.length) {
            String option = UIProcess.translate(rules.get(0).getAntecedents()[missingIndex].getRep());
            this.antecedents[missingIndex].comboBox.setSelectedItem(option);
        } else {
            String option = UIProcess.translate(rules.get(0).getConsequence().getRep());
            this.consequence.comboBox.setSelectedItem(option);
        }
    }


}
