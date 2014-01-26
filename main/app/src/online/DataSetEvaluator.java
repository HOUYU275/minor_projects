package online;

import util.Registry;
import weka.attributeSelection.ASEvaluation;
import weka.attributeSelection.FuzzyRoughSubsetEval;
import weka.attributeSelection.NatureInspiredCommon;
import weka.attributeSelection.SubsetEvaluator;
import weka.fuzzy.measure.Gamma;

import java.util.BitSet;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 27/01/13
 * Time: 10:38
 */
public class DataSetEvaluator {
    private InstanceCreator creator;
    private ASEvaluation evaluation;

    public DataSetEvaluator(InstanceCreator creator, String evaluationString) {
        this.creator = creator;
        try {
            initialise(evaluationString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SubsetEvaluator getEvaluation() {
        return (SubsetEvaluator) evaluation;
    }


    public ASEvaluation getASEvaluation() {
        return evaluation;
    }

    public double evaluate(BitSet bitSet) throws Exception {
        return ((SubsetEvaluator) evaluation).evaluateSubset(bitSet);
    }

    public double evaluate(int[] set) throws Exception {
        BitSet bitSet = NatureInspiredCommon.toBitSet(set, creator.getCreated().classIndex());
        return ((SubsetEvaluator) evaluation).evaluateSubset(bitSet);
    }

    public void build() throws Exception {
        evaluation.buildEvaluator(creator.getCreated());
    }

    public void initialise(String evaluationString) throws Exception {
        this.evaluation = Registry.getEvaluation(evaluationString);
        if (evaluationString.equals("FuzzyRoughSubsetEval"))
            ((FuzzyRoughSubsetEval) evaluation).setMeasure(new Gamma());
        build();
        full();
    }

    public double full() throws Exception {
        int[] fullSubset = NatureInspiredCommon.fullSubset(creator.getCreated().numAttributes() - 1);
        double originalFullMerit = getEvaluation().evaluateSubset(NatureInspiredCommon.toBitSet(fullSubset, creator.getCreated().classIndex()));
        //System.out.println("Full Merit = " + originalFullMerit + " (" + fullSubset.length + ")");
        return originalFullMerit;
    }
}
