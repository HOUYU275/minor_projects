package originalharmonysearch.applications.spring;

import originalharmonysearch.core.HarmonyMemory;
import originalharmonysearch.core.ValueRange;

import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 05-Jul-2010
 * Time: 16:05:48
 * To change this template use File | Settings | File Templates.
 */
public class SpringHarmonyMemory extends HarmonyMemory {
    public SpringHarmonyMemory() {
        super();
    }

    //int musicianSize, int memorySize, int maxIteration

    public SpringHarmonyMemory(ValueRange[] parameterRanges, ValueRange[] valueRanges) throws Exception {
        super(parameterRanges, valueRanges, new SpringHarmonyComparator(), new Random());
        //0,0.1,1}, {0,0.5,1}, {8,15,1
        //super(numHarmonies, new ValueRange[]{new ValueRange(0, 0.1, true), new ValueRange(0, 0.5, true), new ValueRange(8, 15, true)}, new SpringHarmonyComparator(), HMCR, new Random());
    }

    /*public void initialise() throws Exception {
        //super.setHarmonyEvaluator(new SpringEvaluator(), 0);
        super.initialise();
    }*/
}
