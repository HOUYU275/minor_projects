package DynamicFSEnsemble;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 22/01/13
 * Time: 12:20
 */
public class Display extends JFrame {
    private static final long serialVersionUID = 8955351752994884761L;

    public Display() {
        JFrame testFrame = new JFrame();
        testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        final JPanel comp = new JPanel();
        comp.setPreferredSize(new Dimension(320, 200));
        testFrame.getContentPane().add(comp, BorderLayout.CENTER);
        testFrame.pack();
        testFrame.setVisible(true);
    }
}
