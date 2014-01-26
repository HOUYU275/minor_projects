package test;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 21/11/11
 * Time: 12:26
 * To change this template use File | Settings | File Templates.
 */
public class PatientSeparator {

    public static void generateBatchCommand() {
        String rootDirectory = "";
        for (int i = 1; i <= 26; i++) {
            System.out.println(rootDirectory + "\\P\\P" + i + "\\test.arff");
        }
    }

    public static void main(String[] args) throws IOException {

        //generateBatchCommand();
        String fileName = "NewDataSubjs.txt";
        String rootDirectory = "C:\\Documents and Settings\\rrd09\\Desktop\\";
        BufferedReader dataReader = new BufferedReader(new FileReader(rootDirectory + fileName));
        ArrayList<ArrayList<String>> train = new ArrayList<ArrayList<String>>(26);
        ArrayList<ArrayList<String>> test = new ArrayList<ArrayList<String>>(26);
        for (int i = 0; i < 26; i++) {
            train.add(new ArrayList<String>());
            test.add(new ArrayList<String>());
        }
        String dataLine;
        while ((dataLine = dataReader.readLine()) != null) {
            //System.out.println(dataLine);
            String[] split = dataLine.split("\t");
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(split[1]);
            for (int i = 2; i < split.length; i++) {
                stringBuffer.append("\t");
                stringBuffer.append(split[i]);
            }
            int index = Integer.parseInt(split[0]) - 1;
            test.get(index).add(stringBuffer.toString());
            for (int i = 0; i < 26; i++) {
                if (i != index) {
                    train.get(i).add(stringBuffer.toString());
                }
            }
        }
        dataReader.close();

        (new File(rootDirectory + "\\P")).mkdir();

        for (int i = 0; i < 26; i++) {
            String directory = rootDirectory + "\\P\\P" + (i + 1);
            (new File(directory)).mkdir();
            BufferedWriter trainWriter = new BufferedWriter(new FileWriter(directory + "\\train.arff"));
            BufferedWriter testWriter = new BufferedWriter(new FileWriter(directory + "\\test.arff"));

            writeHeader(trainWriter);
            for (int j = 0; j < train.get(i).size(); j++) {
                trainWriter.write(train.get(i).get(j));
                trainWriter.newLine();
            }
            trainWriter.close();

            writeHeader(testWriter);
            for (int j = 0; j < test.get(i).size(); j++) {
                testWriter.write(test.get(i).get(j));
                testWriter.newLine();
            }
            testWriter.close();
        }
    }

    public static void writeHeader(BufferedWriter bufferedWriter) throws IOException {
        bufferedWriter.write("@RELATION Patient");
        bufferedWriter.newLine();
        bufferedWriter.newLine();
        for (int i = 0; i < 102; i++) {
            bufferedWriter.write("@ATTRIBUTE " + i + " REAL");
            bufferedWriter.newLine();
        }
        bufferedWriter.write("@ATTRIBUTE class {1.00,2.00}");
        bufferedWriter.newLine();
        bufferedWriter.newLine();
        bufferedWriter.write("@DATA");
        bufferedWriter.newLine();
    }

}


