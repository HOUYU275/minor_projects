package originalharmonysearch.applications.classifierensemble;

import originalharmonysearch.core.HarmonyMemory;
import originalharmonysearch.core.ValueRange;
import weka.classifiers.Classifier;
import weka.core.Instances;

import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 12-Apr-2010
 * Time: 15:12:41
 * To change this template use File | Settings | File Templates.
 */
public class ClassifierEnsembleHarmonyMemory extends HarmonyMemory {

    public ClassifierEnsembleHarmonyMemory() {
        super();
    }

    public ClassifierEnsembleHarmonyMemory(ValueRange[] parameterRanges, ValueRange[] valueRanges, Random random, Classifier[] classifiers, Instances data) throws Exception {
        super(parameterRanges, valueRanges, new ClassifierEnsembleHarmonyComparator(classifiers, data), random);
    }

    /*public void initialise(*//*Classifier[] classifiers, Instances data*//*) throws Exception {
        //super.setHarmonyEvaluator(new ClassifierEnsembleEntropyEvaluator(classifiers, data), 0);
        //super.setHarmonyEvaluator(new ClassifierEnsembleCardinalityEvaluator(), 1);
        super.initialise();
    }*/
}
