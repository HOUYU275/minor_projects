package survey;

import weka.attributeSelection.SubsetEvaluator;

import java.util.BitSet;
import java.util.concurrent.Callable;

/**
 * Created by IntelliJ IDEA.
 * User: Ren
 * Date: 01/11/12
 * Time: 12:44
 */
public class ThreadedSubsetEvaluator implements Callable<Double> {
	private SubsetEvaluator evaluator;
	private BitSet bitSet;

    public ThreadedSubsetEvaluator(SubsetEvaluator evaluator, BitSet bitSet) {
        this.evaluator = evaluator;
        this.bitSet = bitSet;
    }

    @Override
	public Double call() throws Exception {
        return evaluator.evaluateSubset(bitSet);
	}
}
