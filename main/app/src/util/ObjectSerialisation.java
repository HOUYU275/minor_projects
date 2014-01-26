package util;

import weka.classifiers.Classifier;
import weka.core.Instances;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 27/01/12
 * Time: 11:49
 * To change this template use File | Settings | File Templates.
 */
public class ObjectSerialisation {

    public static void exportClassifiers(Classifier[] classifiers, String fileName) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(Registry.serialisationPath + fileName);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(classifiers);
        objectOutputStream.close();
    }

    public static Classifier[] importClassifiers(String fileName) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(Registry.serialisationPath + fileName);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Classifier[] classifiers = (Classifier[]) objectInputStream.readObject();
        objectInputStream.close();
        return classifiers;
    }

    public static void exportClassifier(Classifier classifier, String pathWithName) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(pathWithName);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(classifier);
        objectOutputStream.close();
    }

    public static Classifier importClassifier(String fileName) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(fileName);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Classifier classifier = (Classifier) objectInputStream.readObject();
        objectInputStream.close();
        return classifier;
    }

    public static void exportInstances(Instances instances, String datasetName, String foldName) throws IOException {
        FileOutputStream fileOutputStream =
                new FileOutputStream(Registry.crossValidationCachePath + datasetName + "\\" + foldName);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(instances);
        objectOutputStream.close();
    }

    public static void exportRepeatedInstances(Instances instances, String path, String foldName) throws IOException {
        FileOutputStream fileOutputStream =
                new FileOutputStream(path + "\\" + foldName);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(instances);
        objectOutputStream.close();
    }

    public static Instances importRepeatedInstances(String fileName) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(Registry.crossValidationCachePath + fileName);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Instances instances = (Instances) objectInputStream.readObject();
        objectInputStream.close();
        return instances;
    }

    public static Instances importInstances(String fileName) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(Registry.crossValidationCachePath + fileName);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Instances instances = (Instances) objectInputStream.readObject();
        objectInputStream.close();
        return instances;
    }

}
