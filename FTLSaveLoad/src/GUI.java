import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 28/09/12
 * Time: 10:40
 */
public class GUI extends JFrame {
    private static final int WIDTH = 200;
    private static final int HEIGHT = 100;

    public GUI() {
        setTitle("FTL Helper");
        setSize(WIDTH, HEIGHT);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Container pane = getContentPane();
        pane.setLayout(new GridLayout(1, 2));
        JButton save = new JButton("Save");
        save.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Functions.save();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        JButton load = new JButton("Load");
        load.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Functions.load();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        pane.add(save);
        pane.add(load);
    }
}
