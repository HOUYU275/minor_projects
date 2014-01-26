package model;

import controller.Tracker;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 15/02/12
 * Time: 14:43
 */
public class Display extends JPanel {

    private Tracker tracker;

    public Display(Tracker tracker) {
        this.tracker = tracker;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int width = getWidth();
        int height = getHeight();

        int centreX = width / 2;
        int centreY = height / 2;

        ArrayList<Point2D> path = tracker.getPath();

        for (int i = 1; i < path.size(); i++) {
            g2.draw(new Line2D.Double(centreX + path.get(i - 1).getX(), centreY + path.get(i - 1).getY(), centreX + path.get(i).getX(), centreY + path.get(i).getY()));
        }
    }

    public void setTracker(Tracker tracker) {
        this.tracker = tracker;
    }

    /*private double getMax() {
        double max = -Integer.MAX_VALUE;
        for (int i = 0; i < data.length; i++) {
            if (data[i] > max)
                max = data[i];
        }
        return max;
    }*/

    public static void displayPath(Transport transport) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Display display = new Display(transport.getTracker());
        f.add(display);
        f.setSize(400, 400);
        f.setLocation(200, 200);
        f.setVisible(true);
    }
}
