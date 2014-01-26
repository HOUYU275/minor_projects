package originalharmonysearch.applications.constraintfour;

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
public class ConstraintFourHarmonyMemory extends HarmonyMemory {
    public ConstraintFourHarmonyMemory() {
        super();
    }

    //int musicianSize, int memorySize, int maxIteration

    public ConstraintFourHarmonyMemory(ValueRange[] parameterRanges, ValueRange[] valueRanges) throws Exception {
        super(parameterRanges, valueRanges, new ConstraintFourHarmonyComparator(), new Random());
        //0,0.1,1}, {0,0.5,1}, {8,15,1
        //super(numHarmonies, new ValueRange[]{new ValueRange(0, 0.1, true), new ValueRange(0, 0.5, true), new ValueRange(8, 15, true)}, new SpringHarmonyComparator(), HMCR, new Random());
    }

    public ConstraintFourHarmonyMemory(int numHarmonies, double HMCR) throws Exception {
        //0,0.1,1}, {0,0.5,1}, {8,15,1                                0,10,1}, {0,10,1}, {0,10,1}
        //super(numHarmonies, new ValueRange[]{new ValueRange(0, 10, true), new ValueRange(0, 10, true), new ValueRange(0, 10, true)}, new ConstraintFourHarmonyComparator(), HMCR, new Random());
    }

    /*public void initialise() throws Exception {
        //super.setHarmonyEvaluator(new ConstraintFourEvaluator(), 0);
        super.initialise();
    }*/
}