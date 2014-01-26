package util;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Ren
 * Date: 25/10/12
 * Time: 15:40
 */
public class FSEResult {
    private String dataset;
    private int numberOfColumns;
    private int numberOfFolds;
    private int nextColumn = 0;
    private ArrayList<double[]> storage;
    private boolean verbose = true;

    public FSEResult(String dataset, int numberOfColumns, int numberOfFolds) {
        this.dataset = dataset;
        this.numberOfColumns = numberOfColumns;
        this.numberOfFolds = numberOfFolds;
        this.storage = new ArrayList<>();
        this.storage.add(new double[numberOfColumns]);
        //if (verbose) System.out.print(dataset);
    }

    public void push(double value) {
        this.storage.get(storage.size() - 1)[nextColumn] += value;
        if (nextColumn < numberOfColumns - 1) nextColumn++;
        else nextColumn = 0;
        //if (verbose) System.out.print("\t" + value);
    }

    public String newRow() {
        String report = this.average();
        this.storage.add(new double[numberOfColumns]);
        this.nextColumn = 0;
        if (verbose) report = "\t" + dataset + report;
        if (verbose) System.out.print(report);
        return report;
    }

    public String average() {
        String report = "";
        double[] currentRow = this.storage.get(storage.size() - 1);
        for (int i = 0; i < currentRow.length; i++) currentRow[i] = currentRow[i] / numberOfFolds;
        if (verbose) {
            for (int i = 0; i < currentRow.length; i++) report += ("\t" + currentRow[i]);
            report += "\n";
        }
        //if (verbose) System.out.print(report);
        return report;
    }

    public String getDataset() {
        return dataset;
    }
}
