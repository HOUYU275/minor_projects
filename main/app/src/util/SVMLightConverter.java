package util;

import java.io.*;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: rrd09
 * Date: 29/04/13
 * Time: 12:06
 */
public class SVMLightConverter {

    private static String fileName = "ecoli";
    private static String directory = Registry.datasetPath + File.separator + "svm" + File.separator;

    private static DataSet dataSet;

    public static void main(String[] args) throws IOException {
        File file = new File(directory + fileName + ".svmlight");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String currentLine = null;
        dataSet = new DataSet();
        while ((currentLine = reader.readLine()) != null) {
            System.out.println(currentLine);
            String[] splits = currentLine.split(" ");
            double label = Double.parseDouble(splits[0]);
            ArrayList<Double> values = new ArrayList<>();
            for (int i = 1; i < splits.length; i++) values.add(Double.parseDouble(splits[i].split(":")[1]));
            Data data = new Data(values, label);
            dataSet.add(data);
            System.out.println(data);
        }
        File output = new File(directory + fileName + ".arff");
        BufferedWriter writer = new BufferedWriter(new FileWriter(output));
        writer.write("@RELATION " + fileName.toUpperCase());
        writer.newLine();
        writer.newLine();
        for (int i = 0; i < dataSet.get(0).values.length; i++) {
            writer.write("@ATTRIBUTE " + i + " REAL");
            writer.newLine();
        }
        writer.write("@ATTRIBUTE class {" + dataSet.labels.get(0));
        for (int i = 1; i < dataSet.labels.size(); i++)           writer.write("," + dataSet.labels.get(i));
        writer.write("}");
        writer.newLine();
        writer.newLine();
        writer.write("@DATA");
        writer.newLine();
        for (Data data : dataSet) {
            writer.write(data.toString());
            writer.newLine();
        }
        reader.close();
        writer.close();
    }

    private static class DataSet extends ArrayList<Data> {
        ArrayList<Double> labels;

        public DataSet() {
            this.labels = new ArrayList<>();
        }

        public boolean add(Data data) {
            if (!labels.contains(data.label)) labels.add(data.label);
            return super.add(data);
        }
    }

    private static class Data {
        double[] values;
        double label;

        public Data(ArrayList<Double> values, double label) {
            this.values = new double[values.size()];
            for (int i = 0; i < values.size(); i++) this.values[i] = values.get(i);
            this.label = label;
        }

        public String toString() {
            StringBuffer buffer = new StringBuffer();
            for (double value : values) buffer.append(value).append(" ");
            buffer.append(label);
            return buffer.toString();
        }
    }
}
