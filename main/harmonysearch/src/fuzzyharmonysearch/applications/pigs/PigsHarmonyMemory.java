package fuzzyharmonysearch.applications.pigs;

import fuzzyharmonysearch.core.HarmonyMemory;
import fuzzyharmonysearch.core.ValueRange;

import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 08/02/11
 * Time: 09:36
 * To change this template use File | Settings | File Templates.
 */
public class PigsHarmonyMemory extends HarmonyMemory {

    public PigsHarmonyMemory() {
        super();
    }

    public PigsHarmonyMemory(ValueRange[] parameterRanges, ValueRange[] valueRanges) throws Exception {
        super(parameterRanges, valueRanges, new PigsHarmonyComparator(), new Random());
    }

}
