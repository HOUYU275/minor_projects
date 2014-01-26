package dynamic.core;

import online.InstanceCreator;
import weka.attributeSelection.NatureInspiredCommon;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 10/01/13
 * Time: 11:17
 */
public class DynamicRange extends Vector<Integer> {

    private static final long serialVersionUID = 4684215073429537336L;
    protected DynamicHarmonyMemory memory;
    private int currentIteration;
    protected int numAttributes;

    public DynamicRange(DynamicHarmonyMemory memory) {
        this.memory = memory;
        numAttributes = memory.creator.getOriginal().numAttributes() - 1;
        currentIteration = -1;
    }

    public void convert() {
        int[] features = NatureInspiredCommon.toIntArray(memory.creator.getCurrentFeatures(), numAttributes);
        super.clear();
        for (int feature : features) super.add(feature);
        int numDiscards = super.size() / 10;
        while (numDiscards-- > 0) super.add(-1);
        //currentIteration = memory.currentIteration;
    }

    public int random() {
        //if (currentIteration != memory.currentIteration) convert();
        return super.get(memory.random.nextInt(super.size()));
    }

}
