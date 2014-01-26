package view;

import fuzzy.FuzzyNumber;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Ren
 * Date: 31/07/12
 * Time: 20:54
 * To change this template use File | Settings | File Templates.
 */
public class CombinedDisplay extends JPanel implements ActionListener {

    FuzzyNumberDisplay display;
    Main main;
    CombinedDisplay sister;
    JComboBox comboBox;

    public CombinedDisplay(String title, Main main) {
        super(new FlowLayout());
        this.main = main;
        this.setPreferredSize(new Dimension(200, 400));

        DefaultListCellRenderer cellRenderer = new DefaultListCellRenderer();
        cellRenderer.setHorizontalAlignment(DefaultListCellRenderer.CENTER);

        JLabel label = new JLabel(title);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        Font f = new Font("Dialog", Font.PLAIN, 24);
        label.setFont(f);
        label.setPreferredSize(new Dimension(200, 70));
        this.add(label);

        String[] options = { "Very High", "High", "Moderate High", "Moderate", "Moderate Low", "Low", "Very Low", "Missing"};
        comboBox = new JComboBox(options);

        comboBox.setRenderer(cellRenderer);
        comboBox.setSelectedIndex(new Random().nextInt(7));
        comboBox.addActionListener(this);
        comboBox.setFont(f);
        comboBox.setPreferredSize(new Dimension(200, 70));

        display = new FuzzyNumberDisplay(UIProcess.convertLinguisticVariable(options[comboBox.getSelectedIndex()]));

        this.add(display);
        this.add(comboBox);
    }

    public CombinedDisplay(String title, Main main, int selectedIndex) {
        super(new FlowLayout());
        this.main = main;
        this.setPreferredSize(new Dimension(200, 400));

        DefaultListCellRenderer cellRenderer = new DefaultListCellRenderer();
        cellRenderer.setHorizontalAlignment(DefaultListCellRenderer.CENTER);

        JLabel label = new JLabel(title);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(200, 70));
        Font f = new Font("Dialog", Font.PLAIN, 24);
        label.setFont(f);
        this.add(label);

        String[] options = { "Very High", "High", "Moderate High", "Moderate", "Moderate Low", "Low", "Very Low", "Missing"};
        comboBox = new JComboBox(options);

        comboBox.setRenderer(cellRenderer);
        comboBox.setSelectedIndex(selectedIndex);
        comboBox.addActionListener(this);
        comboBox.setFont(f);
        comboBox.setPreferredSize(new Dimension(200, 70));

        display = new FuzzyNumberDisplay(UIProcess.convertLinguisticVariable(options[comboBox.getSelectedIndex()]));

        this.add(display);
        this.add(comboBox);
    }

    public JComboBox getComboBox() {
        return comboBox;
    }

    public void setComboBox(JComboBox comboBox) {
        this.comboBox = comboBox;
    }

    public CombinedDisplay getSister() {
        return sister;
    }

    public void setSister(CombinedDisplay sister) {
        this.sister = sister;
    }

    public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox)e.getSource();
        String option = (String)cb.getSelectedItem();
        updateLabel(option);
    }

    private void updateLabel(String option) {
        System.out.println("Option Changed to " + option);
        display.setFuzzyNumber(UIProcess.convertLinguisticVariable(option));

        if (sister != null) sister.update(option);
        //else main.makeRules();
        repaint();
    }

    private void update(String option) {
        System.out.println("Option Changed to " + option);
        this.comboBox.setSelectedItem(option);
        display.setFuzzyNumber(UIProcess.convertLinguisticVariable(option));
        //main.makeRules();
        repaint();
    }

    private void updateLabel(FuzzyNumber number) {
        System.out.println("FuzzyNumber Changed to " + number);
        display.setFuzzyNumber(number);

        repaint();
    }

    public FuzzyNumberDisplay getDisplay() {
        return display;
    }

    public void setDisplay(FuzzyNumberDisplay display) {
        this.display = display;
    }
}
