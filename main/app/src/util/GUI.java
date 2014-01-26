package util;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 07/12/12
 * Time: 16:34
 */
public class GUI extends JFrame {
    private static final long serialVersionUID = 8298894194981936633L;

    public static void main(String[] args) {
        GUI gui = new GUI();
        gui.setBounds(100, 100, 300, 100);
        gui.setVisible(true);
        gui.pack();
    }
}
