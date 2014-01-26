package test;

import util.Dataset;
import weka.classifiers.fuzzy.QuickRules;
import weka.core.Instances;
import weka.fuzzy.implicator.ImplicatorKD;
import weka.fuzzy.similarity.Similarity1;
import weka.fuzzy.tnorm.TNormKD;
import weka.fuzzy.tnorm.TNormLukasiewicz;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 09/11/11
 * Time: 15:07
 * To change this template use File | Settings | File Templates.
 */
public class TestRuleInduction {

    public static void main(String[] args) throws Exception {
        Instances instances = Dataset.getDataset("heart");
        //weka.classifiers.fuzzy.QuickRules -I weka.fuzzy.implicator.ImplicatorKD -T weka.fuzzy.tnorm.TNormKD -R "weka.fuzzy.similarity.Similarity1 -R first-last -T weka.fuzzy.tnorm.TNormLukasiewicz -C 0.0" -W -P 0
        QuickRules quickRules = constructQuickRules();
        quickRules.buildClassifier(instances);

    }

    public static QuickRules constructQuickRules() throws Exception {
        QuickRules quickRules = new QuickRules();
        quickRules.setImplicator(new ImplicatorKD());
        quickRules.setTNorm(new TNormKD());
        quickRules.setSimilarity(new Similarity1());
        quickRules.getSimilarity().setAttributeIndices("first-last");
        quickRules.getSimilarity().setTNorm(new TNormLukasiewicz());
        quickRules.getSimilarity().setCutoff(0.0);
        quickRules.setPruning(0);
        return quickRules;
    }

}
