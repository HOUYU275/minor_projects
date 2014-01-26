package test;

import util.Dataset;
import util.ObjectSerialisation;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.core.Instances;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 27/01/12
 * Time: 12:29
 * To change this template use File | Settings | File Templates.
 */
public class Serialisation {

    public static void main(String args[]) throws Exception {
        Classifier classifier = AbstractClassifier.forName("weka.classifiers.trees.J48", null);
        Instances data = Dataset.getDataset("heart");
        classifier.buildClassifier(data);
        for (int i = 0; i < data.numInstances(); i++) {
            System.out.println("o\t" + classifier.classifyInstance(data.instance(i)) + " - " + data.instance(i).classValue());
        }

        System.out.println(" ---------------------------- ");

        ObjectSerialisation.exportClassifier(classifier, "testJ48");
        classifier = null;
        classifier = ObjectSerialisation.importClassifier("testJ48");
        for (int i = 0; i < data.numInstances(); i++) {
            System.out.println("n\t" + classifier.classifyInstance(data.instance(i)) + " - " + data.instance(i).classValue());
        }

    }

}
