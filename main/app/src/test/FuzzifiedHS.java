package test;

import fuzzified.application.SpringHarmonyMemory;
import fuzzified.core.ValueRange;
import org.apache.commons.math.stat.descriptive.SummaryStatistics;
import org.apache.commons.math.stat.inference.TTestImpl;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 20/12/11
 * Time: 15:43
 * To change this template use File | Settings | File Templates.
 */
public class FuzzifiedHS {
    public static void main(String args[]) throws Exception {

        Collections.sort(new ArrayList<Comparable>());

        TTestImpl tTest = new TTestImpl();
        int total = 1000;

        double[] crispResult = new double[total];
        SummaryStatistics crispSummaryStatistics = new SummaryStatistics();
        for (int i = 0; i < total; i++) {
            crispResult[i] = spring(false, 1);
            crispSummaryStatistics.addValue(crispResult[i]);
        }

        System.out.println("Crisp AVG = " + crispSummaryStatistics.getMean());
        System.out.println("Crisp BEST = " + crispSummaryStatistics.getMax());

        double[] fuzzifiedResult = new double[total];
        double spread = 0d;
        SummaryStatistics fuzzifiedSummaryStatistics;

        spread = 0d;

        fuzzifiedSummaryStatistics = new SummaryStatistics();
        for (int i = 0; i < total; i++) {
            fuzzifiedResult[i] = spring(true, spread);
            fuzzifiedSummaryStatistics.addValue(fuzzifiedResult[i]);
        }

        System.out.println("[" + spread + "] Fuzzified AVG = " + fuzzifiedSummaryStatistics.getMean());
        System.out.println("[" + spread + "] Fuzzified BEST = " + fuzzifiedSummaryStatistics.getMax());

        System.out.println("Crisp vs Fuzzified = " + tTest.pairedTTest(crispResult, fuzzifiedResult));

        spread = 1d;

        fuzzifiedSummaryStatistics = new SummaryStatistics();
        for (int i = 0; i < total; i++) {
            fuzzifiedResult[i] = spring(true, spread);
            fuzzifiedSummaryStatistics.addValue(fuzzifiedResult[i]);
        }

        System.out.println("[" + spread + "] Fuzzified AVG = " + fuzzifiedSummaryStatistics.getMean());
        System.out.println("[" + spread + "] Fuzzified BEST = " + fuzzifiedSummaryStatistics.getMax());

        System.out.println("Crisp vs Fuzzified = " + tTest.pairedTTest(crispResult, fuzzifiedResult));

        spread = 0.5d;

        fuzzifiedSummaryStatistics = new SummaryStatistics();
        for (int i = 0; i < total; i++) {
            fuzzifiedResult[i] = spring(true, spread);
            fuzzifiedSummaryStatistics.addValue(fuzzifiedResult[i]);
        }

        System.out.println("[" + spread + "] Fuzzified AVG = " + fuzzifiedSummaryStatistics.getMean());
        System.out.println("[" + spread + "] Fuzzified BEST = " + fuzzifiedSummaryStatistics.getMax());

        System.out.println("Crisp vs Fuzzified = " + tTest.pairedTTest(crispResult, fuzzifiedResult));

        spread = 0.25d;

        fuzzifiedSummaryStatistics = new SummaryStatistics();
        for (int i = 0; i < total; i++) {
            fuzzifiedResult[i] = spring(true, spread);
            fuzzifiedSummaryStatistics.addValue(fuzzifiedResult[i]);
        }

        System.out.println("[" + spread + "] Fuzzified AVG = " + fuzzifiedSummaryStatistics.getMean());
        System.out.println("[" + spread + "] Fuzzified BEST = " + fuzzifiedSummaryStatistics.getMax());

        System.out.println("Crisp vs Fuzzified = " + tTest.pairedTTest(crispResult, fuzzifiedResult));

    }

    private static double spring(boolean fuzzified, double spread) {
        SpringHarmonyMemory springHarmonyMemory = new SpringHarmonyMemory(ValueRange.createStaticParameters(10, 0.8, 0.9, 0.0001));
        springHarmonyMemory.setFuzzified(fuzzified);
        springHarmonyMemory.setSpread(spread);
        springHarmonyMemory.run(1000);
        //springHarmonyMemory.report();
        /*for (int i = 0; i < springHarmonyMemory.getMusicians()[0].getNotes().size(); i++) {
            System.out.println(springHarmonyMemory.getMusicians()[0].getNotes().get(i).getValue());
        }
        
        System.out.println(" - - - - ");

        springHarmonyMemory.reportFull();

        for (int i = 0; i < 1000; i ++) {
            //System.out.println(springHarmonyMemory.getMusicians()[0].fuzzifiedPickVerbose().getValue());
            springHarmonyMemory.getMusicians()[0].fuzzifiedPickVerbose();
        }

        System.out.println(" - - - - ");*/

        return springHarmonyMemory.best().getMerit();
    }
}
