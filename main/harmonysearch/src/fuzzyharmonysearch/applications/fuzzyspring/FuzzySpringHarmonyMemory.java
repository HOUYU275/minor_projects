package fuzzyharmonysearch.applications.fuzzyspring;

import fuzzyharmonysearch.core.HarmonyMemory;
import fuzzyharmonysearch.core.ValueRange;

import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 05-Jul-2010
 * Time: 16:05:48
 * To change this template use File | Settings | File Templates.
 */
public class FuzzySpringHarmonyMemory extends HarmonyMemory {
    public FuzzySpringHarmonyMemory() {
        super();
    }

    //int musicianSize, int memorySize, int maxIteration

    public FuzzySpringHarmonyMemory(ValueRange[] parameterRanges, ValueRange[] valueRanges) throws Exception {
        super(parameterRanges, valueRanges, new FuzzySpringHarmonyComparator(), new Random());
        //0,0.1,1}, {0,0.5,1}, {8,15,1
        //super(numHarmonies, new ValueRange[]{new ValueRange(0, 0.1, true), new ValueRange(0, 0.5, true), new ValueRange(8, 15, true)}, new FuzzySpringHarmonyComparator(), HMCR, new Random());
    }

    /*public void initialise() throws Exception {
        //super.setHarmonyEvaluator(new SpringEvaluator(), 0);
        super.initialise();
    }*/
}
