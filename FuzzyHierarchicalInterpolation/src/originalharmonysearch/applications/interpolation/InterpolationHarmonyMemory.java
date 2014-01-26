package originalharmonysearch.applications.interpolation;

import hierarchicalinterpolation.control.BackwardProcess;
import originalharmonysearch.core.HarmonyMemory;
import originalharmonysearch.core.ValueRange;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: rrd09
 * Date: 15/08/13
 * Time: 16:31
 */
public class InterpolationHarmonyMemory  extends HarmonyMemory {

    public InterpolationHarmonyMemory() {
        super();
    }

    public InterpolationHarmonyMemory(ValueRange[] parameterRanges, ValueRange[] valueRanges, double[] totals, BackwardProcess backwardProcess)  throws Exception {
        super(parameterRanges, valueRanges, new InterpolationHarmonyComparator(backwardProcess, totals), new Random(new Random().nextLong()));
    }
}
