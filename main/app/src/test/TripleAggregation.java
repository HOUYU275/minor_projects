package test;

import weka.core.Instances;
import weka.core.converters.ArffSaver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: rrd09
 * Date: 23/11/12
 * Time: 11:20
 */
public class TripleAggregation {

    public static void main(String[] args) throws IOException {
        String fileName = "heart";
        Instances input = openDataset(fileName);
        Instances output = new Instances(input, input.size() / 3);
        ArrayList<Person> persons = new ArrayList<>();
        for (int i = 0; i < input.size() - 3; i += 3) {
            persons.add(new Person(input.instance(i), input.instance(i + 1), input.instance(i + 2)));
        }
        for (Person person : persons) output.add(person.aggregate());
        saveDataset(output, fileName + "_out.arff");
    }

    public static Instances openDataset(String fileName) throws IOException {
        File file = new File(fileName + ".arff");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        Instances data = new Instances(reader);
        reader.close();
        data.setClassIndex(data.numAttributes() - 1);
        return data;
    }

    public static void saveDataset(Instances output, String fileName) throws IOException {
        ArffSaver saver = new ArffSaver();
        saver.setInstances(output);
        saver.setFile(new File(fileName));
        saver.writeBatch();
    }

}
