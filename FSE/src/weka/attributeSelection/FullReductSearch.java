package weka.attributeSelection;

//import util.CombinationGenerator;
import weka.core.Instances;
import weka.core.OptionHandler;
import weka.core.TechnicalInformation;
import weka.core.TechnicalInformationHandler;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Enumeration;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 25/01/11
 * Time: 13:21
 * To change this template use File | Settings | File Templates.
 */
public class FullReductSearch extends ASSearch
        implements OptionHandler, TechnicalInformationHandler {

    private BitSet core = null;
    private int classIndex;
    private int numAttributes;

    @Override
    public int[] search(ASEvaluation ASEvaluator, Instances data) throws Exception {

        FuzzyRoughSubsetEval fuzzyRoughSubsetEval = (FuzzyRoughSubsetEval) ASEvaluator;
        if (core == null) core = fuzzyRoughSubsetEval.computeCore();
        classIndex = data.classIndex();
        numAttributes = data.numAttributes();
        System.out.println(fuzzyRoughSubsetEval.evaluateSubset(core));

        int currentSize = 1;
        while (currentSize < numAttributes - core.cardinality()) {
            ArrayList<int[]> adds = produce(core, currentSize);
            for (int[] currentAdd : adds) {
                System.out.println(currentAdd.toString());
                BitSet currentBitSet = new BitSet();
                for (int i : currentAdd) currentBitSet.set(currentAdd[i]);
                currentBitSet.and(core);
                System.out.println(currentBitSet.toString() + fuzzyRoughSubsetEval.evaluateSubset(currentBitSet));
            }
            currentSize = currentSize + 1;
        }

        return new int[0];  //TODO: Automatically Generated Implemented Method
    }

    private ArrayList<int[]> produce(BitSet core, int limit) {
        ArrayList<Integer> in = new ArrayList<Integer>();
        for (int i = core.nextClearBit(0); i < numAttributes; i = core.nextClearBit(i + 1)) {
            System.out.println(i);
            in.add(i);
        }
        System.out.println(in.toString());
        int[] indices;
        ArrayList<int[]> result = new ArrayList<int[]>();
        /*CombinationGenerator combinationGenerator = new CombinationGenerator(in.size(), limit);
        while (combinationGenerator.hasMore()) {
            int[] temp = new int[limit];
            indices = combinationGenerator.getNext();
            for (int i = 0; i < indices.length; i++) {
                temp[i] = in.get(indices[i]);
            }
            result.add(temp);
        }*/
        return result;
    }

    public BitSet getCore() {
        return core;
    }

    public void setCore(BitSet core) {
        this.core = core;
    }

    public Enumeration listOptions() {
        return null;  //TODO: Automatically Generated Implemented Method
    }

    public void setOptions(String[] options) throws Exception {
        //TODO: Automatically Generated Implemented Method
    }

    public String[] getOptions() {
        return new String[0];  //TODO: Automatically Generated Implemented Method
    }

    public TechnicalInformation getTechnicalInformation() {
        return null;  //TODO: Automatically Generated Implemented Method
    }
}
