package DynamicFSEnsemble;

import featuresubsetensemble.FeatureSubsetEnsemble;
import online.DataSetEvaluator;
import online.InstanceCreator;
import util.ClassifierCrossValidator;
import util.CrossValidation;
import util.Registry;
import util.Table;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 21/01/13
 * Time: 16:48
 */
public class TestDynamicFSE {

    private static int numEnsembles = 20;
    //private static int numIterations = 2500;
    private static String[] classifiers = new String[]{Registry.getClassifier("J48"), Registry.getClassifier("NN"), Registry.getClassifier("NB")};
    private int numMusicians = 20;
    private InstanceCreator creator;
    private DataSetEvaluator cfs;
    private DataSetEvaluator pcfs;
    private Table cfsResults = new Table();
    private Table pcfsResults = new Table();

    public static void main(String[] args) throws Exception {
        //Display display = new Display();
        TestDynamicFSE test = new TestDynamicFSE();
        for (String dataset : Registry.getDatasets("ijcai")) test.work(dataset, Registry.IR, 20);
    }

    public void crossValidation(String dataset) throws Exception {
        double d = 0;
        for (int i = 0; i < 10; i++) {
            CrossValidation crossValidation = new CrossValidation(dataset, 1);
            d += ClassifierCrossValidator.validateClassifier(Registry.getClassifier("PART"), crossValidation.getTrainingFold(i), crossValidation.getTestingFold(i));
        }
        System.out.println(d / 10);
    }

    public void work(String dataset, int mode, int repeat) throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter("" + dataset + "_" + mode + ".txt"));
        cfsResults.clear();
        cfsResults.add("Data Set\t" + dataset);
        cfsResults.newRow();
        pcfsResults.clear();
        pcfsResults.add("Data Set\t" + dataset);
        pcfsResults.newRow();
        creator = new InstanceCreator(dataset);
        creator.initialise(mode);

        cfs = new DataSetEvaluator(creator, "CFS");
        cfsResults.add("CFS\tFeature\tInstance\tCFS Merit\tCFS Size\tC4.5\tNN\tNB\tMerit\tSize\tC4.5\tNN\tNB\tC4.5\tNN\tNB");
        DynamicFSEnsembleBuilder cfsBuilder = new DynamicFSEnsembleBuilder();
        FeatureSubsetEnsemble cfsEnsemble = cfsBuilder.build(creator, cfs, 10000, new Random(), numEnsembles);
        getResults(cfsEnsemble, cfsResults, cfs);

        pcfs = new DataSetEvaluator(creator, "PCFS");
        pcfsResults.add("PCFS\tFeature\tInstance\tPCFS Merit\tPCFS Size\tC4.5\tNN\tNB\tMerit\tSize\tC4.5\tNN\tNB\tC4.5\tNN\tNB");
        DynamicFSEnsembleBuilder pcfsBuilder = new DynamicFSEnsembleBuilder();
        FeatureSubsetEnsemble pcfsEnsemble = pcfsBuilder.build(creator, pcfs, 5000, new Random(), numEnsembles);
        getResults(pcfsEnsemble, pcfsResults, pcfs);

        while (--repeat > 0) {
            System.out.println(" -- " + repeat + " -- ");
            cfsResults.newRow();
            pcfsResults.newRow();
            creator.setInterval(repeat);
            String changeString = creator.change(mode) + "\t" + creator.getCreated().numAttributes() + "\t" + creator.getCreated().numInstances();
            cfsResults.add(changeString);
            pcfsResults.add(changeString);
            cfsEnsemble = changeAndAdapt(cfsBuilder, cfsEnsemble, cfsResults, cfs);
            pcfsEnsemble = changeAndAdapt(pcfsBuilder, pcfsEnsemble, pcfsResults, pcfs);
        }
        System.out.println(cfsResults.toString());
        System.out.println(pcfsResults.toString());
        writer.write(cfsResults.toString());
        writer.newLine();
        writer.write(pcfsResults.toString());
        writer.close();
    }

    public FeatureSubsetEnsemble changeAndAdapt(DynamicFSEnsembleBuilder builder, FeatureSubsetEnsemble ensemble, Table table, DataSetEvaluator evaluator) throws Exception {
        evaluator.build();
        //System.out.println(ensemble.getAveragedMerit() + " " + ensemble.getRawSubsetSize());

        ensemble = creator.convertEvaluate(ensemble, evaluator);

        //System.out.println(ensemble.getAveragedMerit() + " " + ensemble.getRawSubsetSize());

        table.add(ensemble.getAveragedMerit());
        table.add(ensemble.getRawSubsetSize());
        table.add(ClassifierCrossValidator.validateFilteredClassifiers(classifiers, ensemble.getRawSubsets(), creator.getCreated(), creator.createTest()));

        ensemble = builder.adapt(creator, evaluator);

        //System.out.println(ensemble.getAveragedMerit() + " " + ensemble.getRawSubsetSize());

        getResults(ensemble, table, evaluator);

        return ensemble;
    }

    private void getResults(FeatureSubsetEnsemble ensemble, Table table, DataSetEvaluator evaluator) throws Exception {
        table.newRow();
        table.add("\t" + creator.getCreated().numAttributes() + "\t" + creator.getCreated().numInstances());
        table.add(ensemble.getAveragedMerit());
        table.add(ensemble.getRawSubsetSize());
        table.add(ClassifierCrossValidator.validateFilteredClassifiers(classifiers, ensemble.getRawSubsets(), creator.getCreated(), creator.createTest()));

        int[] singleSubset = creator.search(evaluator);
        table.add(evaluator.evaluate(singleSubset));
        table.add(singleSubset.length);
        table.add(ClassifierCrossValidator.validateFilteredClassifier(classifiers, singleSubset, creator.getCreated(), creator.createTest()));

        table.add(ClassifierCrossValidator.validateClassifier(classifiers, creator.getCreated(), creator.createTest()));
    }

}
