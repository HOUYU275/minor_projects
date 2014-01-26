package test;

import java.io.*;
import java.util.Scanner;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 04/08/11
 * Time: 15:18
 * To change this template use File | Settings | File Templates.
 */
public class CellExpander {

    public static void main(String args[]) throws IOException {
        Scanner scanner = new Scanner(new FileInputStream(args[0] + ".txt"));
        Writer output = new BufferedWriter(new FileWriter(args[0] + "_output.txt"));
        try {
            String line;
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                output.write(line + "\n" + line + "\n" + line + "\n");
            }
        } finally {
            scanner.close();
            output.close();
        }
    }

}
