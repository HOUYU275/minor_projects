package originalharmonysearch.experimental.adaptive;

import originalharmonysearch.core.HarmonyMemory;
import originalharmonysearch.core.ValueRange;

import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 11-Aug-2010
 * Time: 10:55:10
 * To change this template use File | Settings | File Templates.
 */
public class AdaptiveHarmonyMemory extends HarmonyMemory {
    public AdaptiveHarmonyMemory() {
        super();
    }

    public AdaptiveHarmonyMemory(int dimensions, ValueRange[] valueRanges, Random random) throws Exception {
        //The last two parameters are: memorySize and HMCR ranges
        /*int numHarmoniesLowerBound = (int)valueRanges[valueRanges.length - 2].getMin();
        int numHarmoniesUpperBound = (int)valueRanges[valueRanges.length - 2].getMax();
        double HMCRLowerBound = valueRanges[valueRanges.length - 1].getMin();
        double HMCRUpperBound = valueRanges[valueRanges.length - 1].getMax();
        int numHarmonies = numHarmoniesLowerBound + random.nextInt(numHarmoniesUpperBound - numHarmoniesLowerBound + 1);
        double HMCR = HMCRLowerBound + random.nextDouble() * (HMCRUpperBound - HMCRLowerBound);*/
        /*super(
                dimensions,
                (int)valueRanges[valueRanges.length - 2].getMin() +
                        random.nextInt((int)(valueRanges[valueRanges.length - 2].getMax() -
                                valueRanges[valueRanges.length - 2].getMin())),
                valueRanges,
                valueRanges[valueRanges.length - 1].getMin() +
                        random.nextDouble() * (valueRanges[valueRanges.length - 1].getMax() -
                                valueRanges[valueRanges.length - 1].getMin()),
                random
        );*/
    }

    @Override
    public void iterate(int maxIteration) throws Exception {
        /*int currentIteration = 0;
        while (currentIteration <= maxIteration) {
            Harmony newHarmony = newHarmony();                       
            while (!super.getHarmonyEvaluators()[0].checkConstraint(newHarmony)) {
                newHarmony = randomHarmony();
            }
            if (this.evaluateAndAdd(newHarmony)) {
                resize((int)newHarmony.getNote(super.getMusicians().length - 2));
                super.setHMCR(newHarmony.getNote(super.getMusicians().length - 1)); 
            }
            currentIteration = currentIteration + 1;
        }*/
    }
}
