package originalharmonysearch.applications.beam;

import originalharmonysearch.core.HarmonyMemory;
import originalharmonysearch.core.ValueRange;

import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 28-Oct-2010
 * Time: 12:16:03
 * To change this template use File | Settings | File Templates.
 */
public class BeamHarmonyMemory extends HarmonyMemory {
    public BeamHarmonyMemory() {
        super();
    }

    //int musicianSize, int memorySize, int maxIteration

    public BeamHarmonyMemory(ValueRange[] parameterRanges, ValueRange[] valueRanges) throws Exception {
        super(parameterRanges, valueRanges, new BeamHarmonyComparator(), new Random());
        //0,0.1,1}, {0,0.5,1}, {8,15,1
        //super(numHarmonies, new ValueRange[]{new ValueRange(0, 0.1, true), new ValueRange(0, 0.5, true), new ValueRange(8, 15, true)}, new SpringHarmonyComparator(), HMCR, new Random());
    }
}
