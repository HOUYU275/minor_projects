package weka.attributeSelection;

import original.core.ValueRange;
import weka.core.Instances;
import weka.core.OptionHandler;
import weka.core.TechnicalInformation;
import weka.core.TechnicalInformationHandler;

import java.util.Enumeration;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 23/01/12
 * Time: 16:07
 * To change this template use File | Settings | File Templates.
 */
public class DistributedHSFS
        extends ASSearch
        implements OptionHandler, TechnicalInformationHandler {

    private int numHarmonies = 15;
    private int numMusicians = 10;
    private double HMCR = 0.8;
    private int K = 2000;

    private boolean hasClass;
    private int classIndex;
    private int numAttributes;

    private SubsetEvaluator[] evaluators;
    private Instances[] dataSplits;

    @Override
    public int[] search(ASEvaluation ASEvaluator, Instances data) throws Exception {
        initialiseParameters(ASEvaluator, data);

        ValueRange[] valueRanges = new ValueRange[data.numAttributes() - 1];
        for (int i = 0; i < valueRanges.length; i++) {
            valueRanges[i] = new ValueRange(0, numAttributes - 1, false);
        }

        return new int[0];
    }

    @Override
    public Enumeration listOptions() {
        return null;  //TODO: Automatically Generated Implemented Method
    }

    @Override
    public void setOptions(String[] options) throws Exception {
        //TODO: Automatically Generated Implemented Method
    }

    @Override
    public String[] getOptions() {
        return new String[0];  //TODO: Automatically Generated Implemented Method
    }

    @Override
    public TechnicalInformation getTechnicalInformation() {
        return null;  //TODO: Automatically Generated Implemented Method
    }

    private void initialiseParameters(ASEvaluation ASEvaluator, Instances data) throws Exception {
        if (!(ASEvaluator instanceof SubsetEvaluator))
            throw new Exception(ASEvaluator.getClass().getName() + " is not a Subset evaluator!");
        if (ASEvaluator instanceof UnsupervisedSubsetEvaluator) hasClass = false;
        else {
            hasClass = true;
            classIndex = data.classIndex();
        }
        SubsetEvaluator subsetEvaluator = (SubsetEvaluator) ASEvaluator;

        numAttributes = data.numAttributes();

        data.stratify(numMusicians);
        for (int i = 0; i < numMusicians; i++) {
            dataSplits[i] = data.testCV(numMusicians, i);
        }

        for (int i = 0; i < numMusicians; i++) {
            ASEvaluation asEvaluation = ASEvaluation.forName(ASEvaluator.getClass().getName(), null);
            asEvaluation.buildEvaluator(dataSplits[i]);
            evaluators[i] = (SubsetEvaluator) asEvaluation;
        }
    }
}
