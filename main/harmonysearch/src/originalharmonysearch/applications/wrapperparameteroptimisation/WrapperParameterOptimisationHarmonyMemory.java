package originalharmonysearch.applications.wrapperparameteroptimisation;

import originalharmonysearch.core.HarmonyMemory;
import originalharmonysearch.core.ValueRange;
import weka.attributeSelection.SubsetEvaluator;
import weka.core.Instances;

import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 24-Mar-2010
 * Time: 16:42:48
 * To change this template use File | Settings | File Templates.
 */
public class WrapperParameterOptimisationHarmonyMemory extends HarmonyMemory {

    public WrapperParameterOptimisationHarmonyMemory() {
        super();
    }

    public WrapperParameterOptimisationHarmonyMemory(ValueRange[] parameterRanges, ValueRange[] valueRanges, Random random, SubsetEvaluator subsetEvaluator, Instances data) throws Exception {
        //notice the +1 here as well, it takes the real attribute number from client
        //TODO: new note range added the maximum discard option
        /*ValueRange[] valueRanges = new ValueRange[numMusicians];
        for (int i = 0; i < valueRanges.length; i ++) {
            valueRanges[i] = new ValueRange(0, numNotes, false);
        }*/
        super(parameterRanges, valueRanges, new WrapperParameterOptimisationHarmonyComparator(subsetEvaluator, data), random);
        //super(2, /*numHarmonies, *//*numMusicians, */valueRanges, /*HMCR, */random);
        //System.out.println("\tNumber of Harmonies: " + numHarmonies + "\n");
        //System.out.println("\tNumber of Musicians: " + numMusicians + "\n");
    }

    /*ASEvaluation ASEval = new FuzzyRoughSubsetEval();
        ASEval.buildEvaluator(data);
        if (!(ASEval instanceof SubsetEvaluator)) {
            System.out.println(ASEval.getClass().getName()
                    + " is not a "
                    + "Subset evaluator!");
        }
        SubsetEvaluator subsetEvaluator = (SubsetEvaluator) ASEval;*/

    /*public void initialise(*//*SubsetEvaluator ASEval, Instances data*//*) throws Exception {
        //super.setHarmonyEvaluator(new WrapperParameterOptimisationEvaluator(ASEval, data), 0);
        //super.setHarmonyEvaluator(new FeatureSelectionCardinalityEvaluator(), 1);
        super.initialise();
    }*/

}