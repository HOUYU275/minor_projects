package originalharmonysearch.applications.vessel;

import originalharmonysearch.core.HarmonyMemory;
import originalharmonysearch.core.ValueRange;

import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 28-Oct-2010
 * Time: 14:02:32
 * To change this template use File | Settings | File Templates.
 */
public class VesselHarmonyMemory extends HarmonyMemory {
    public VesselHarmonyMemory() {
        super();
    }

    public VesselHarmonyMemory(ValueRange[] parameterRanges, ValueRange[] valueRanges) throws Exception {
        super(parameterRanges, valueRanges, new VesselHarmonyComparator(), new Random());
    }
}
