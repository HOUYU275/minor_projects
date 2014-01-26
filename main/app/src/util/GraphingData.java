package util;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 09/02/11
 * Time: 14:44
 * To change this template use File | Settings | File Templates.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

public class GraphingData extends JPanel {
    /*int[] data = {
        21, 14, 18, 03, 86, 88, 74, 87, 54, 77,
        61, 55, 48, 60, 49, 36, 38, 27, 20, 18
    };*/

    double[] data;

    final int PAD = 20;

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();
        // Draw ordinate.
        g2.draw(new Line2D.Double(PAD, PAD, PAD, h - PAD));
        // Draw abcissa.
        g2.draw(new Line2D.Double(PAD, h - PAD, w - PAD, h - PAD));
        double xInc = (double) (w - 2 * PAD) / (data.length - 1);
        double scale = (double) (h - 2 * PAD) / getMax();
        // Mark data points.
        g2.setPaint(Color.red);
        for (int i = 0; i < data.length; i++) {
            double x = PAD + i * xInc;
            double y = h - PAD - scale * data[i];
            g2.fill(new Ellipse2D.Double(x - 1, y - 1, 2, 2));
            //g2.fill(new Ellipse2D.Double(x, y, 1, 1));
        }
    }

    public void setData(double[] input) {
        data = input.clone();
    }

    private double getMax() {
        double max = -Integer.MAX_VALUE;
        for (int i = 0; i < data.length; i++) {
            if (data[i] > max)
                max = data[i];
        }
        return max;
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new GraphingData());
        f.setSize(400, 400);
        f.setLocation(200, 200);
        f.setVisible(true);
    }
}
