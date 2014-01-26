package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 17/11/11
 * Time: 12:34
 * To change this template use File | Settings | File Templates.
 */
public class FormatConverter {

    public static String getTimeStamp() {
        GregorianCalendar calendar = new GregorianCalendar();
        Date date = calendar.getTime();
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        String s = dateFormat.format(date);
        s = s.replaceAll("[:]", "-");
        return s;
    }

    public static void main(String[] args) throws IOException {
        //String rootDirectory = "C:\\Documents and Settings\\rrd09\\Desktop\\";
        String fileName = "CNAE-9.data";
        BufferedReader dataReader = new FileIOHandler().getReader(fileName);
        BufferedWriter writer = new FileIOHandler().getWriter("cnae.arff");
        String dataLine;
        String labelLine;

        String[] split;

        writer.write("@RELATION CNAE9\n" +
                "\n");
        for (int i = 0; i < 856; i++) {
            writer.write("@ATTRIBUTE " + i + " NUMERIC\n");
        }

        writer.write("@ATTRIBUTE class {1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0}\n");
        writer.write("\n" + "@DATA\n");

        while ((dataLine = dataReader.readLine()) != null) {
            StringBuffer buffer = new StringBuffer();
            System.out.println(dataLine);
            split = dataLine.split(",");
            for (int i = 1; i < split.length; i++) buffer.append(split[i]).append(",");
            buffer.append(split[0]).append(".0\n");
            writer.write(buffer.toString());
        }
        dataReader.close();
        writer.close();
    }

    /*public static void main(String[] args) throws IOException {
        String rootDirectory = "C:\\Documents and Settings\\rrd09\\Desktop\\";
        String datasetName = "eighthr";
        BufferedReader dataReader = new BufferedReader(new FileReader(rootDirectory + datasetName + ".data"));
        BufferedReader labelReader = new BufferedReader(new FileReader(rootDirectory + datasetName + "_labels.data"));
        BufferedWriter writer = new BufferedWriter(new FileWriter(rootDirectory + "secom.arff"));
        String dataLine;
        String labelLine;
        String[] split;
        writer.write("@RELATION Secom");
        writer.newLine();
        writer.newLine();
        for (int i = 0; i < 590; i++) {
            writer.write("@ATTRIBUTE " + i + " REAL");
            writer.newLine();
        }
        writer.write("@ATTRIBUTE class {-1,1}");
        writer.newLine();
        writer.newLine();

        writer.write("@DATA");
        writer.newLine();
        while ((dataLine = dataReader.readLine()) != null) {
            labelLine = labelReader.readLine();
            //System.out.println(dataLine);
            split = dataLine.split(" ");
            for (int i = 0; i < split.length; i++) {
                if (split[i].equals("NaN")) {
                    writer.write("?\t");
                } else {
                    writer.write(split[i].trim() + "\t");
                }
            }
            writer.write(labelLine.split(" ")[0].trim());
            writer.newLine();
        }
        dataReader.close();
        labelReader.close();
        writer.close();
    }*/

}
