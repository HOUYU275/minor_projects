package view;

import fuzzy.FuzzyNumber;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Ren
 * Date: 31/07/12
 * Time: 20:20
 * To change this template use File | Settings | File Templates.
 */
public class FuzzyNumberDisplay extends JPanel {

    private FuzzyNumber fuzzyNumber;
    private Polygon polygon;
    private int horizontalScaling = 20;
    private int verticalScaling = 230;

    public FuzzyNumberDisplay(FuzzyNumber fuzzyNumber) {
        super();
        this.fuzzyNumber = fuzzyNumber;
        this.setPreferredSize(new Dimension(200, 260));
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        if (fuzzyNumber != null) {
            convertToPolygon();
            g2.drawPolygon(polygon);
        }
        g2.drawLine(0, verticalScaling, verticalScaling, verticalScaling);
        g2.drawLine(0, verticalScaling - 1, verticalScaling, verticalScaling - 1);
    }

    public void convertToPolygon() {
        int left = (int) fuzzyNumber.getLeftShoulder() * horizontalScaling;
        int centroid = (int) fuzzyNumber.getCentroid() * horizontalScaling;
        int right = (int) fuzzyNumber.getRightShoulder() * horizontalScaling;
        polygon = new Polygon(new int[]{left, centroid, right}, new int[]{verticalScaling, 0, verticalScaling}, 3);
    }

    public FuzzyNumber getFuzzyNumber() {
        return fuzzyNumber;
    }

    public void setFuzzyNumber(FuzzyNumber fuzzyNumber) {
        this.fuzzyNumber = fuzzyNumber;
    }
}
