package fuzzyharmonysearch.applications.productmix;

import fuzzyharmonysearch.core.HarmonyMemory;
import fuzzyharmonysearch.core.ValueRange;

import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 19/12/11
 * Time: 13:53
 * To change this template use File | Settings | File Templates.
 */
public class ProductMixHarmonyMemory extends HarmonyMemory {

    public ProductMixHarmonyMemory(ValueRange[] parameterRanges) throws Exception {
        super(
                parameterRanges,
                new ValueRange[]{
                        new ValueRange(25.238, 67.222, true),
                        new ValueRange(30.238, 40.263, true),
                        new ValueRange(50.476, 134.444, true),
                },
                new ProductMixHarmonyComparator(),
                new Random()
        );
    }

}
