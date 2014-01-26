package DynamicFSEnsemble;

import dynamic.core.DynamicHarmonyMemory;
import online.DataSetEvaluator;
import online.InstanceCreator;

import java.util.BitSet;
import java.util.Random;
import java.util.concurrent.Callable;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 21/01/13
 * Time: 17:01
 */

public class DynamicFSSearcher implements Callable {

    private InstanceCreator creator;
    private int numIterations;
    private DynamicHarmonyMemory memory;

    public DynamicFSSearcher(InstanceCreator creator, DataSetEvaluator evaluator, int numMusicians, int numIterations, Random random) {
        this.creator = creator;
        this.numIterations = numIterations;
        memory = new DynamicHarmonyMemory(numMusicians, 20, 0.85, creator, evaluator, random);
        memory.invalidate();
        memory.fill();
    }

    @Override
    public int[] call() throws Exception {
        memory.invalidate();
        BitSet set = memory.iterate(numIterations);
        return creator.convertHarmony(set);
    }
}

