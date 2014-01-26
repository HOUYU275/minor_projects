package weka.fuzzy;

import weka.core.Instances;
import weka.fuzzy.implicator.Implicator;
import weka.fuzzy.similarity.Relation;
import weka.fuzzy.similarity.Similarity;

/**
 * Created by IntelliJ IDEA.
 * User: Ren
 * Date: 30/12/11
 * Time: 15:45
 * To change this template use File | Settings | File Templates.
 */
public class CommonCalculations {


    /*//int x: the instance index 0->numInstances-1
    public static void lowerApproximation(int x, Instances instances, int numInstances, Relation relation, Implicator implicator) {
        int decision = x;
        double lower = 1;
        double currentLower = 0;
        double[] equivalenceClasses = new double[numInstances];

        for (int i = 0; i < numInstances; i++) {
            double value = relation.getCell(x, i);
            equivalenceClasses[i] = value;

            currentLower = implicator.calculate(value,
                    fuzzySimilarity(
                            instances.classIndex(),
                            instances.instance(decision).value(instances.classIndex()),
                            instances.instance(i).value(instances.classIndex())));
        }
    }

    public static double fuzzySimilarity(int attributeIndex, int classIndex,
                                         double x, double y,
                                         boolean isNominal,
                                         Similarity similarityEqual, Similarity similarity) {
        double ret = 0;
        //no decision feature, so each object is distinct
        if (attributeIndex < 0 && attributeIndex == classIndex) {
            ret = 0;
        } else {
            //if it's a nominal attribute, then use crisp equivalence
            //otherwise use the general similarity measure
            if (Double.isNaN(x) || Double.isNaN(y)) ret = 1;
            else if (isNominal) ret = similarityEqual.similarity(attributeIndex, x, y);
            else ret = similarity.similarity(attributeIndex, x, y);
        }
        return ret;
    }*/

}
