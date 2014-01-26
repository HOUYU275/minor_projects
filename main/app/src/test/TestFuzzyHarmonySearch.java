package test;

import fuzzyharmonysearch.applications.pigs.PigsHarmonyMemory;
import fuzzyharmonysearch.core.ValueRange;
import util.GraphingData;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 30/08/11
 * Time: 16:14
 * To change this template use File | Settings | File Templates.
 */
public class TestFuzzyHarmonySearch {

    public static void main(String args[]) throws Exception {

        ValueRange[] pigsValueRanges = new ValueRange[3];
        pigsValueRanges[0] = new ValueRange(0, 20, true);
        for (int i = 1; i < pigsValueRanges.length; i++) {
            pigsValueRanges[i] = new ValueRange(0, 20, true);
        }

        PigsHarmonyMemory harmonyMemory =
                new PigsHarmonyMemory(
                        ValueRange.createParameterRanges(20, 0.8, 0.8, 0.001),
                        pigsValueRanges);

        harmonyMemory.fill();
        harmonyMemory.iterate(20000);
        System.out.println(" - - - - \n" +
                "Intermediate Result = \n" +
                harmonyMemory.best().toString() +
                " - - - - " +
                harmonyMemory.best().getMerit().getRepresentativeValue());

        //new Random().nextLong()

        /*ValueRan  ge[] springValueRanges = new ValueRange[3];
        springValueRanges[0] = new ValueRange(0, 0.1, true);
        springValueRanges[1] = new ValueRange(0, 0.5, true);
        springValueRanges[2] = new ValueRange(8, 15, true);

        FuzzySpringHarmonyMemory fuzzySpringHarmonyMemory =
                new FuzzySpringHarmonyMemory(
                        ValueRange.createParameterRanges(20, 0.5, 0.6, 0.001),
                        springValueRanges);*/
        //new Random().nextLong()

        /*for (int i = 0; i < 1000; i ++) {
            harmonyMemory.clear();
            harmonyMemory.fill();
            harmonyMemory.iterate(2000);
            System.out.println(harmonyMemory.best().getMerit().getRepresentativeValue());
        }*/

        /*for (int i = 0; i < 1; i++) {
            fuzzySpringHarmonyMemory.clear();
            fuzzySpringHarmonyMemory.setFuzzify(false);
            fuzzySpringHarmonyMemory.fill();
            fuzzySpringHarmonyMemory.iterate(10000);
            System.out.println(fuzzySpringHarmonyMemory.best().getMerit().getRepresentativeValue());
        }


        SpringHarmonyMemory harmonyMemory =
                new SpringHarmonyMemory(
                        originalharmonysearch.core.HarmonyMemory.createParameterRanges(20, 0.5),
                        new originalharmonysearch.core.ValueRange[]{
                                new originalharmonysearch.core.ValueRange(0, 0.1, true),
                                new originalharmonysearch.core.ValueRange(0, 0.5, true),
                                new originalharmonysearch.core.ValueRange(8, 15, true)});

        harmonyMemory.fill();
        harmonyMemory.iterate(10000);
        System.out.println(" - - - - \n" +
                "Intermediate Result = \n" +
                harmonyMemory.best().toString() +
                " - - - - " +
                harmonyMemory.best().getMerit());

        harmonyMemory.clear();
        harmonyMemory.fill();
        harmonyMemory.iterate(10000);
        System.out.println(" - - - - \n" +
                "Intermediate Result = \n" +
                harmonyMemory.best().toString() +
                " - - - - " +
                harmonyMemory.best().getMerit());*/
        /*harmonyMemory.fill();
        harmonyMemory.iterate(2000);

        System.out.println(" - - - - \n" +
                "Intermediate Result = \n" +
                harmonyMemory.getIntermediateResult() +
                " - - - - ");

        System.out.println("Harmony: " + harmonyMemory.best().toString());
        System.out.println("Merit: " + harmonyMemory.best().getMerit());
        System.out.println("Merit: " + harmonyMemory.best().getMerit().getRepresentativeValue());
        System.out.println("Additions: " + harmonyMemory.getAdditionCounter());

        //plotGraph(harmonyMemory.getHistoricalAcceptedValues()[2]);
        harmonyMemory.setAdditionCounter(0);
        harmonyMemory.setFuzzify(false);
        harmonyMemory.clear();
        harmonyMemory.fill();
        harmonyMemory.iterate(2000);

        System.out.println("Harmony: " + harmonyMemory.best().toString());
        System.out.println("Merit: " + harmonyMemory.best().getMerit());
        System.out.println("Merit: " + harmonyMemory.best().getMerit().getRepresentativeValue());
        System.out.println("Record: " + harmonyMemory.getBestIteration());
        System.out.println("Additions: " + harmonyMemory.getAdditionCounter());*/

        //plotGraph(harmonyMemory.getHistoricalAcceptedValues()[2]);
    }

    public static void plotGraph(double[] input) {
        JFrame f3 = new JFrame();
        f3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GraphingData graphingData3 = new GraphingData();
        graphingData3.setData(input);
        f3.add(graphingData3);
        f3.setSize(1200, 1000);
        f3.setLocation(200, 200);
        f3.setVisible(true);
    }
}
