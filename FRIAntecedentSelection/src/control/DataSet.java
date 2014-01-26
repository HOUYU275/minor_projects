package control;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: rrd09
 * Date: 14/08/13
 * Time: 20:48
 */
public class DataSet {

    static final String directory = "C:\\Users\\rrd09\\Documents\\academic-work\\data\\wine.arff";
    static int numFeatures = 0;
    static ArrayList<double[]> data = new ArrayList<double[]>();

    public static ArrayList<double[]> read() throws FileNotFoundException {

        Scanner scanner = new Scanner(new File(directory));
        String currentLine = "";
        while (scanner.hasNext()) if (scanner.nextLine().contains("@DATA")) break;
        while (scanner.hasNext()) {
            currentLine = scanner.nextLine();
            String[] splits = currentLine.split(" ");
            double[] entry = new double[splits.length];
            for (int s = 0; s < splits.length; s++) entry[s] = Double.parseDouble(splits[s]);
            data.add(entry);
        }
        return data;
    }

}
