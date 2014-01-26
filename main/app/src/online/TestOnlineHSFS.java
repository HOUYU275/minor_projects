package online;

import dynamic.core.DynamicHarmonyMemory;
import util.Dataset;
import util.Registry;
import weka.attributeSelection.HarmonySearch;
import weka.attributeSelection.HillClimber;
import weka.attributeSelection.NatureInspiredCommon;

import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 10/01/13
 * Time: 12:15
 */
public class TestOnlineHSFS {

    private int numMusicians = 20;
    private InstanceCreator creator;
    private HillClimber HC = new HillClimber();
    private HarmonySearch HS = new HarmonySearch();
    private String evaluationString = "CFS";

    public static void main(String[] args) throws Exception {
        TestOnlineHSFS test = new TestOnlineHSFS();
        for (String dataset : Registry.getDatasets("ijcai")) test.work(dataset, 1);
    }

    public void work(String dataset, int mode) throws Exception {
        creator = new InstanceCreator(dataset);
        creator.initialise(mode);
        DataSetEvaluator evaluator = new DataSetEvaluator(creator, evaluationString);

        DynamicHarmonyMemory memory = new DynamicHarmonyMemory(numMusicians, 20, 0.85, creator, evaluator, new Random());
        memory.fill();
        memory.iterate(5000);
        //System.out.println(NatureInspiredCommon.getString(memory.best()) + " ("  + memory.lastElement().printMerits() + ")");

        int repeat = 10;
        while (repeat-- > 0) {
            creator.change(mode);
            memory.invalidate();
            System.out.println(" -- ");
            memory.iterate(5000);
            //System.out.println(NatureInspiredCommon.getString(memory.best()) + " ("  + memory.lastElement().printMerits() + ")");
        }
    }


}
