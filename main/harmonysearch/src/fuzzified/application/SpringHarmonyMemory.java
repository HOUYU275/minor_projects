package fuzzified.application;

import fuzzified.core.HarmonyMemory;
import fuzzified.core.ValueRange;

import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 20/12/11
 * Time: 15:33
 * To change this template use File | Settings | File Templates.
 */
public class SpringHarmonyMemory extends HarmonyMemory {

    public SpringHarmonyMemory(ValueRange[] parameterRanges) {
        super(
                parameterRanges,
                new ValueRange[]{
                        new ValueRange(0, 0.1, true),
                        new ValueRange(0, 0.5, true),
                        new ValueRange(8, 15, true)},
                new SpringHarmonyComparator(),
                new Random());
    }

}
