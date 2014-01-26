package test;

import featuresubsetensemble.*;
import util.*;
import weka.attributeSelection.ASEvaluation;
import weka.attributeSelection.ASSearch;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.core.Instances;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Random;
import java.util.concurrent.Future;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 15/10/12
 * Time: 10:14
 * To change this template use File | Settings | File Templates.
 */
public class TestFeatureSubsetEnsemble {

	private static int numFolds = 10;
	private static int numEnsembles = 25;

	private static String[] datasets;
	private static String classifier1;
	private static String classifier2;
	private static String classifier3;
	private static ASEvaluation evaluation;
	private static ASSearch search;
	private static String ensembleMode = "PARTI";

	private static String datasetsString = "CES";
	private static String classifierString1 = "J48";
	private static String classifierString2 = "NB";
	private static String classifierString3 = "VQNN";
	private static String evaluationKeyword = "FRFS";
	private static String searchKeyword = "HS";

	private static int numberOfRepeats = 20;

	private static StringBuffer stringBuffer = new StringBuffer();
	private static SimpleMatrix simpleMatrix = new SimpleMatrix(13, numberOfRepeats);
	private static int currentRow = 0;

	private static String[] header = new String[]{
			"Dataset",
			"FSE+C4.5", "FSE+NB", "FSE+VQNN", "Size",
			"FSEC+C4.5", "FSEC+NB", "FSEC+VQNN", "Size",
			//"FSE-T+C4.5", "FSE-T+NB", "FSE-T+VQNN", "Size",
			"FSE-M+C4.5", "FSE-M+NB", "FSE-M+VQNN", "Size",
			"FSE-F+C4.5", "FSE-F+NB", "FSE-F+VQNN", "Size",
			"Single+C4.5", "Single+NB", "Single+VQNN", "Size",
			"Raw+C4.5", "Raw+NB", "Raw+VQNN", "Size"
	};

	public static void main(String[] args) throws Exception {
		applySettings();
		try {
			//FileWriter fstream = new FileWriter(datasetsString + "_" + ensembleMode + "_" + evaluationKeyword + "_" + searchKeyword + "_" + (new Random().nextInt(100))+  ".txt");
			//BufferedWriter out = new BufferedWriter(fstream);
			for (int i = 0; i < header.length; i++) {
				System.out.print("\t" + header[i] + "\t");
				//out.write(header[i] + "\t");
			}
			System.out.println();
			//out.write("\n");
			//out.flush();
			FSEResult[] results = new FSEResult[datasets.length];
			ThreadPool threadPool = new ThreadPool(21, numEnsembles + 1);
			for (int i = 0; i < datasets.length; i++) {
				//out.write(datasets[i]);
				//out.flush();
				//System.out.print(datasets[i]);
				FSEResult result = new FSEResult(datasets[i], 4, numFolds);
				//crossEvaluateFSSimple(result, threadPool);
				for (int r = 0; r < numberOfRepeats; r++) {
					//compareAgainstEnsemble(result, threadPool);
					//crossEvaluateMixed(result, threadPool);
					crossEvaluateDataset(result, threadPool);
					//crossEvaluateDatasetSimple(result, threadPool);
					//crossEvaluateFSSimple(result, threadPool);
					//crossEvaluateRanking(result, threadPool);
					//result.newRow();
				}
				results[i] = result;
			}
			threadPool.shutdown();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
		System.exit(0);
	}

	public static FSEResult crossEvaluateDataset(FSEResult result, ThreadPool threadPool) throws Exception {

		CrossValidation crossValidation = new CrossValidation(result.getDataset(), false);

		FeatureSubsetEnsemble featureSubsetEnsemble;

		for (int j = 0; j < numFolds; j++) {

			Instances training = crossValidation.getTrainingFold(j);
			Instances testing = crossValidation.getTestingFold(j);

			ThreadedEvaluator threadedEvaluator = new ThreadedEvaluator(evaluationKeyword, searchKeyword, training);
			Future<int[]> singleEvaluationResult = threadPool.submitEvaluator(threadedEvaluator);

			switch (ensembleMode) {
				case "PARTI":
					featureSubsetEnsemble = DataPartitionEnsembleBuilder.build(training, evaluationKeyword, searchKeyword, numEnsembles, threadPool);
					break;
				case "STOCH":
					featureSubsetEnsemble = StochasticEnsembleBuilder.build(training, evaluationKeyword, searchKeyword, numEnsembles, threadPool);
					break;
				case "MIXED":
					featureSubsetEnsemble = MixedEnsembleBuilder.build(training, searchKeyword, numEnsembles, threadPool);
					break;
				default:
					featureSubsetEnsemble = DataPartitionEnsembleBuilder.build(training, evaluationKeyword, searchKeyword, numEnsembles, threadPool);
			}

			System.gc();
			System.out.print(j);

			//featureSubsetEnsemble.getRanks();

			result.push(ClassifierCrossValidator.validateFeatureSubsetEnsemble(classifier1, featureSubsetEnsemble, training, testing));
			result.push(ClassifierCrossValidator.validateFeatureSubsetEnsemble(classifier2, featureSubsetEnsemble, training, testing));
			result.push(ClassifierCrossValidator.validateFeatureSubsetEnsemble(classifier3, featureSubsetEnsemble, training, testing));
			result.push(featureSubsetEnsemble.getFlattenedSize());

			result.push(ClassifierCrossValidator.validateFeatureSubsetEnsembleByCombinedDistribution(classifier1, featureSubsetEnsemble, training, testing));
			result.push(ClassifierCrossValidator.validateFeatureSubsetEnsembleByCombinedDistribution(classifier2, featureSubsetEnsemble, training, testing));
			result.push(ClassifierCrossValidator.validateFeatureSubsetEnsembleByCombinedDistribution(classifier3, featureSubsetEnsemble, training, testing));
			result.push(featureSubsetEnsemble.getFlattenedSize());

			int[] midSelected = featureSubsetEnsemble.convertTo(0.5);
			result.push(ClassifierCrossValidator.validateFilteredClassifier(classifier1, midSelected, training, testing));
			result.push(ClassifierCrossValidator.validateFilteredClassifier(classifier2, midSelected, training, testing));
			result.push(ClassifierCrossValidator.validateFilteredClassifier(classifier3, midSelected, training, testing));
			result.push(midSelected.length);

			result.push(ClassifierCrossValidator.validateFilteredClassifiers(classifier1, featureSubsetEnsemble.getRawSubsets(), training, testing));
			result.push(ClassifierCrossValidator.validateFilteredClassifiers(classifier2, featureSubsetEnsemble.getRawSubsets(), training, testing));
			result.push(ClassifierCrossValidator.validateFilteredClassifiers(classifier3, featureSubsetEnsemble.getRawSubsets(), training, testing));
			result.push(featureSubsetEnsemble.getRawSubsetSize());

			//long startTime = System.currentTimeMillis();
			/*evaluation = Registry.getEvaluation(evaluationKeyword);
						   evaluation.buildEvaluator(training);
						   search = Registry.getSearch(searchKeyword);
						   ((HarmonySearch) search).setMusicianHint(Registry.getMusicianHint(result.getDataset(), evaluation.getClass().getName()));
						   ((HarmonySearch) search).setIterative(false);
						   ((HarmonySearch) search).setIteration(500);
						   if (Registry.getMusicianHint(result.getDataset(), evaluationKeyword) != 0) ((HarmonySearch) search).setMusicianHint(Registry.getMusicianHint(result.getDataset(), evaluationKeyword));
						   int[] singleSelected = search.search(evaluation, training);*/
			//System.out.println(threadPool.getThreadPoolExecutor().getCompletedTaskCount() + "" + singleEvaluationResult.isDone());

			int[] singleSelected = singleEvaluationResult.get();

			result.push(ClassifierCrossValidator.validateFilteredClassifier(classifier1, singleSelected, training, testing));
			result.push(ClassifierCrossValidator.validateFilteredClassifier(classifier2, singleSelected, training, testing));
			result.push(ClassifierCrossValidator.validateFilteredClassifier(classifier3, singleSelected, training, testing));
			result.push(singleSelected.length);

			/*long endTime = System.currentTimeMillis();
						   System.out.println("ALL "+(endTime - startTime) +  " ms");*/

			result.push(ClassifierCrossValidator.validateClassifier(classifier1, training, testing));
			result.push(ClassifierCrossValidator.validateClassifier(classifier2, training, testing));
			result.push(ClassifierCrossValidator.validateClassifier(classifier3, training, testing));
			result.push(training.numAttributes());
			System.gc();
			//System.out.println(threadPool.getThreadPoolExecutor().getCompletedTaskCount() + "" + singleEvaluationResult.isDone());
		}
		return result;
	}

	public static FSEResult crossEvaluateMixed(FSEResult result, ThreadPool threadPool) throws Exception {

		CrossValidation crossValidation = new CrossValidation(result.getDataset(), false);

		FeatureSubsetEnsemble featureSubsetEnsemble;

		for (int j = 0; j < numFolds; j++) {

			Instances training = crossValidation.getTrainingFold(j);
			Instances testing = crossValidation.getTestingFold(j);

			switch (ensembleMode) {
				case "PARTI":
					featureSubsetEnsemble = DataPartitionEnsembleBuilder.build(training, evaluationKeyword, searchKeyword, numEnsembles, threadPool);
					break;
				case "STOCH":
					featureSubsetEnsemble = StochasticEnsembleBuilder.build(training, evaluationKeyword, searchKeyword, numEnsembles, threadPool);
					break;
				case "MIXED":
					featureSubsetEnsemble = MixedEnsembleBuilder.build(training, searchKeyword, numEnsembles, threadPool);
					break;
				default:
					featureSubsetEnsemble = DataPartitionEnsembleBuilder.build(training, evaluationKeyword, searchKeyword, numEnsembles, threadPool);
			}

			System.gc();
			System.out.print(j);

			result.push(ClassifierCrossValidator.validateFeatureSubsetEnsemble(classifier1, featureSubsetEnsemble, training, testing));
			result.push(ClassifierCrossValidator.validateFeatureSubsetEnsemble(classifier2, featureSubsetEnsemble, training, testing));
			result.push(ClassifierCrossValidator.validateFeatureSubsetEnsemble(classifier3, featureSubsetEnsemble, training, testing));
			result.push(featureSubsetEnsemble.getFlattenedSize());

			result.push(ClassifierCrossValidator.validateFeatureSubsetEnsembleByCombinedDistribution(classifier1, featureSubsetEnsemble, training, testing));
			result.push(ClassifierCrossValidator.validateFeatureSubsetEnsembleByCombinedDistribution(classifier2, featureSubsetEnsemble, training, testing));
			result.push(ClassifierCrossValidator.validateFeatureSubsetEnsembleByCombinedDistribution(classifier3, featureSubsetEnsemble, training, testing));
			result.push(featureSubsetEnsemble.getFlattenedSize());

			int[] midSelected = featureSubsetEnsemble.convertTo(0.5);
			result.push(ClassifierCrossValidator.validateFilteredClassifier(classifier1, midSelected, training, testing));
			result.push(ClassifierCrossValidator.validateFilteredClassifier(classifier2, midSelected, training, testing));
			result.push(ClassifierCrossValidator.validateFilteredClassifier(classifier3, midSelected, training, testing));
			result.push(midSelected.length);

			result.push(ClassifierCrossValidator.validateFilteredClassifiers(classifier1, featureSubsetEnsemble.getRawSubsets(), training, testing));
			result.push(ClassifierCrossValidator.validateFilteredClassifiers(classifier2, featureSubsetEnsemble.getRawSubsets(), training, testing));
			result.push(ClassifierCrossValidator.validateFilteredClassifiers(classifier3, featureSubsetEnsemble.getRawSubsets(), training, testing));
			result.push(featureSubsetEnsemble.getRawSubsetSize());

			System.gc();
			//System.out.println(threadPool.getThreadPoolExecutor().getCompletedTaskCount() + "" + singleEvaluationResult.isDone());
		}
		return result;
	}

	public static FSEResult crossEvaluateRanking(FSEResult result, ThreadPool threadPool) throws Exception {

		CrossValidation crossValidation = new CrossValidation(result.getDataset(), false);

		FeatureSubsetEnsemble featureSubsetEnsemble;

		for (int j = 0; j < numFolds; j++) {

			Instances training = crossValidation.getTrainingFold(j);
			Instances testing = crossValidation.getTestingFold(j);

			InfoGainAttributeEval eval = new InfoGainAttributeEval();
			eval.buildEvaluator(training);
			FeatureSubsetEnsemble infoEnsemble = new FeatureSubsetEnsemble(training.numAttributes());
			for (int i = 0; i < training.numAttributes(); i++) {
				infoEnsemble.getFeatureMembershipPairs().add(new FeatureMembershipPair(i, eval.evaluateAttribute(i)));
			}
			infoEnsemble.getRanks();

			switch (ensembleMode) {
				case "PARTI":
					featureSubsetEnsemble = DataPartitionEnsembleBuilder.build(training, evaluationKeyword, searchKeyword, numEnsembles, threadPool);
					break;
				case "STOCH":
					featureSubsetEnsemble = StochasticEnsembleBuilder.build(training, evaluationKeyword, searchKeyword, numEnsembles, threadPool);
					break;
				case "MIXED":
					featureSubsetEnsemble = MixedEnsembleBuilder.build(training, searchKeyword, numEnsembles, threadPool);
					break;
				default:
					featureSubsetEnsemble = DataPartitionEnsembleBuilder.build(training, evaluationKeyword, searchKeyword, numEnsembles, threadPool);
			}

			System.gc();
			//System.out.print(j);

			featureSubsetEnsemble.getRanks();


			System.gc();
			//System.out.println(threadPool.getThreadPoolExecutor().getCompletedTaskCount() + "" + singleEvaluationResult.isDone());
		}
		return result;
	}

	public static FSEResult compareAgainstEnsemble(FSEResult result, ThreadPool threadPool) throws Exception {

		CrossValidation crossValidation = new CrossValidation(result.getDataset(), false);

		FeatureSubsetEnsemble featureSubsetEnsemble;

		for (int j = 0; j < numFolds; j++) {

			Instances training = crossValidation.getTrainingFold(j);
			Instances testing = crossValidation.getTestingFold(j);

			ThreadedEvaluator threadedEvaluator = new ThreadedEvaluator(evaluationKeyword, searchKeyword, training);
			Future<int[]> singleEvaluationResult = threadPool.submitEvaluator(threadedEvaluator);

			switch (ensembleMode) {
				case "PARTI":
					featureSubsetEnsemble = DataPartitionEnsembleBuilder.build(training, evaluationKeyword, searchKeyword, numEnsembles, threadPool);
					break;
				case "STOCH":
					featureSubsetEnsemble = StochasticEnsembleBuilder.build(training, evaluationKeyword, searchKeyword, numEnsembles, threadPool);
					break;
				case "MIXED":
					featureSubsetEnsemble = MixedEnsembleBuilder.build(training, searchKeyword, numEnsembles, threadPool);
					break;
				default:
					featureSubsetEnsemble = DataPartitionEnsembleBuilder.build(training, evaluationKeyword, searchKeyword, numEnsembles, threadPool);
			}

			System.gc();
			System.out.print(j);

			result.push(ClassifierCrossValidator.validateFeatureSubsetEnsemble(classifier1, featureSubsetEnsemble, training, testing));
			result.push(ClassifierCrossValidator.validateFeatureSubsetEnsemble(classifier2, featureSubsetEnsemble, training, testing));
			result.push(ClassifierCrossValidator.validateFeatureSubsetEnsemble(classifier3, featureSubsetEnsemble, training, testing));
			result.push(featureSubsetEnsemble.getFlattenedSize());

			result.push(ClassifierCrossValidator.validateFeatureSubsetEnsembleByCombinedDistribution(classifier1, featureSubsetEnsemble, training, testing));
			result.push(ClassifierCrossValidator.validateFeatureSubsetEnsembleByCombinedDistribution(classifier2, featureSubsetEnsemble, training, testing));
			result.push(ClassifierCrossValidator.validateFeatureSubsetEnsembleByCombinedDistribution(classifier3, featureSubsetEnsemble, training, testing));
			result.push(featureSubsetEnsemble.getFlattenedSize());

			System.gc();
			//System.out.println(threadPool.getThreadPoolExecutor().getCompletedTaskCount() + "" + singleEvaluationResult.isDone());
		}
		return result;
	}

	public static FSEResult crossEvaluateDatasetSimple(FSEResult result, ThreadPool threadPool) throws Exception {

		CrossValidation crossValidation = new CrossValidation(result.getDataset(), false);
		FeatureSubsetEnsemble featureSubsetEnsemble;

		System.out.print(numEnsembles + "\t");

		for (int j = 0; j < numFolds; j++) {

			Instances training = crossValidation.getTrainingFold(j);
			Instances testing = crossValidation.getTestingFold(j);

			switch (ensembleMode) {
				case "PARTI":
					featureSubsetEnsemble = DataPartitionEnsembleBuilder.build(training, evaluationKeyword, searchKeyword, numEnsembles, threadPool);
					break;
				case "STOCH":
					featureSubsetEnsemble = StochasticEnsembleBuilder.build(training, evaluationKeyword, searchKeyword, numEnsembles, threadPool);
					break;
				case "MIXED":
					featureSubsetEnsemble = MixedEnsembleBuilder.build(training, searchKeyword, numEnsembles, threadPool);
					break;
				default:
					featureSubsetEnsemble = DataPartitionEnsembleBuilder.build(training, evaluationKeyword, searchKeyword, numEnsembles, threadPool);
			}

			System.out.print(j);

			double c1 = ClassifierCrossValidator.validateFeatureSubsetEnsemble(classifier1, featureSubsetEnsemble, training, testing);
			double c2 = ClassifierCrossValidator.validateFeatureSubsetEnsemble(classifier2, featureSubsetEnsemble, training, testing);
			double c3 = ClassifierCrossValidator.validateFeatureSubsetEnsemble(classifier3, featureSubsetEnsemble, training, testing);
			double cs = featureSubsetEnsemble.getFlattenedSize();

			double d1 = ClassifierCrossValidator.validateFeatureSubsetEnsembleByCombinedDistribution(classifier1, featureSubsetEnsemble, training, testing);
			double d2 = ClassifierCrossValidator.validateFeatureSubsetEnsembleByCombinedDistribution(classifier1, featureSubsetEnsemble, training, testing);
			double d3 = ClassifierCrossValidator.validateFeatureSubsetEnsembleByCombinedDistribution(classifier1, featureSubsetEnsemble, training, testing);

			if (c1 > d1) result.push(c1);
			else result.push(d1);
			if (c2 > d2) result.push(c2);
			else result.push(d2);
			if (c3 > d3) result.push(c3);
			else result.push(d3);
			result.push(cs);

			System.gc();
		}
		return result;
	}

	public static FSEResult crossEvaluateFSSimple(FSEResult result, ThreadPool threadPool) throws Exception {

		FileWriter writer = new FileWriter(result.getDataset() + "_" + (new Random().nextInt(100)) + ".txt");
		BufferedWriter out = new BufferedWriter(writer);

		out.write("Data Set\tStoch\tParti\tMixed\tSingl\t");
		out.newLine();
		out.flush();

		for (int r = 0; r < numberOfRepeats; r++) {

			CrossValidation crossValidation = new CrossValidation(result.getDataset(), false);
			FeatureSubsetEnsemble featureSubsetEnsemble;

			for (int j = 0; j < numFolds; j++) {
				Instances training = crossValidation.getTrainingFold(j);
				Instances testing = crossValidation.getTestingFold(j);

				ThreadedEvaluator threadedEvaluator = new ThreadedEvaluator(evaluationKeyword, searchKeyword, training);
				Future<int[]> singleEvaluationResult = threadPool.submitEvaluator(threadedEvaluator);
				double c1, d1;

				out.write(result.getDataset() + "\t");

				featureSubsetEnsemble = DataPartitionEnsembleBuilder.build(training, evaluationKeyword, searchKeyword, numEnsembles, threadPool);

				c1 = ClassifierCrossValidator.validateFeatureSubsetEnsemble(classifier1, featureSubsetEnsemble, training, testing);
				d1 = ClassifierCrossValidator.validateFeatureSubsetEnsembleByCombinedDistribution(classifier1, featureSubsetEnsemble, training, testing);
				if (c1 > d1) out.write(c1 * 100 + "\t");
				else out.write(d1 * 100 + "\t");

				featureSubsetEnsemble = StochasticEnsembleBuilder.build(training, evaluationKeyword, searchKeyword, numEnsembles, threadPool);

				c1 = ClassifierCrossValidator.validateFeatureSubsetEnsemble(classifier1, featureSubsetEnsemble, training, testing);
				d1 = ClassifierCrossValidator.validateFeatureSubsetEnsembleByCombinedDistribution(classifier1, featureSubsetEnsemble, training, testing);
				if (c1 > d1) out.write(c1 * 100 + "\t");
				else out.write(d1 * 100 + "\t");

				featureSubsetEnsemble = MixedEnsembleBuilder.build(training, searchKeyword, numEnsembles, threadPool);

				c1 = ClassifierCrossValidator.validateFeatureSubsetEnsemble(classifier1, featureSubsetEnsemble, training, testing);
				d1 = ClassifierCrossValidator.validateFeatureSubsetEnsembleByCombinedDistribution(classifier1, featureSubsetEnsemble, training, testing);
				if (c1 > d1) out.write(c1 * 100 + "\t");
				else out.write(d1 * 100 + "\t");

				int[] singleSelected = singleEvaluationResult.get();
				out.write(ClassifierCrossValidator.validateFilteredClassifier(classifier1, singleSelected, training, testing) * 100 + "\t");

				System.gc();

				out.newLine();
				out.flush();
			}

		}
		out.close();
		return result;
	}

	@Deprecated
	public static void oldMain(String[] args) throws Exception {
		applySettings();
		System.out.println(pad("Dataset") + "\t           \t"
				+ pad("FSE+C4.5") + pad("\tFSE+NB") + pad("\tFSE+VQNN") + "\tSize\t"
				+ pad("FSE-T+C4.5") + pad("\tFSE-T+NB") + pad("\tFSE-T+VQNN") + "\tSize\t"
				+ pad("FSE-M+C4.5") + pad("\tFSE-M+NB") + pad("\tFSE-M+VQNN") + "\tSize\t"
				+ pad("Single+C4.5") + pad("\tSingle+NB") + pad("\tSingle+VQNN") + "\tSize\t"
				+ pad("Raw+C4.5") + pad("\tRaw+NB") + pad("\tRaw+VQNN"));
		for (int i = 0; i < datasets.length; i++) {
			simpleMatrix = new SimpleMatrix(4, 10);
			currentRow = 0;
			for (int r = 0; r < numberOfRepeats; r++) {
				evaluateDataset(datasets[i]);
				currentRow++;
			}
			System.out.println(pad("") + "\t           \t" + simpleMatrix.average(0) + "\t" + simpleMatrix.average(4) + "\t" + simpleMatrix.average(1) + "\t" + simpleMatrix.average(5) + "\t" + simpleMatrix.average(2) + "\t" + simpleMatrix.average(6) + "\t" + simpleMatrix.average(3) + "\n");
		}
	}

	@Deprecated
	public static void evaluateDataset(String dataset) throws Exception {
		System.out.print("" + pad(dataset + "\t"));
		CrossValidation crossValidation = new CrossValidation(dataset, false);

		SimpleAggregator newApproach = new SimpleAggregator();
		SimpleAggregator fsApproach = new SimpleAggregator();
		SimpleAggregator singleFSApproach = new SimpleAggregator();
		SimpleAggregator newApproachSize = new SimpleAggregator();
		SimpleAggregator fsApproachSize = new SimpleAggregator();
		SimpleAggregator singleFSApproachSize = new SimpleAggregator();
		SimpleAggregator rawApproach = new SimpleAggregator();
		FeatureSubsetEnsemble featureSubsetEnsemble;
		ThreadPool threadPool = new ThreadPool(numEnsembles / 2, numEnsembles);

		for (int j = 0; j < numFolds; j++) {
			Instances training = crossValidation.getTrainingFold(j);
			Instances testing = crossValidation.getTestingFold(j);
			int numberOfFeatures = training.numAttributes();
			evaluation.buildEvaluator(training);
			switch (ensembleMode) {
				case "PARTI":
					featureSubsetEnsemble = DataPartitionEnsembleBuilder.build(training, evaluationKeyword, searchKeyword, numEnsembles, threadPool);
					break;
				case "STOCH":
					featureSubsetEnsemble = StochasticEnsembleBuilder.build(training, evaluationKeyword, searchKeyword, numEnsembles, threadPool);
					//votes = EnsembleBuilder.buildFeatureSelectionEnsembleStochastic(training, evaluation, search, numEnsembles, false);
					break;
				case "MIXED":
					//votes = EnsembleBuilder.buildFeatureSelectionEnsembleMixed(training, search, numEnsembles, false);
					featureSubsetEnsemble = MixedEnsembleBuilder.build(training, searchKeyword, numEnsembles, threadPool);
					break;
				default:
					featureSubsetEnsemble = DataPartitionEnsembleBuilder.build(training, evaluationKeyword, searchKeyword, numEnsembles, threadPool);
					//EnsembleBuilder.buildFeatureSelectionEnsemblePartition(training, evaluation, search, numEnsembles, false);

			}
			/*FeatureSubsetEnsemble featureSubsetEnsemble = new FeatureSubsetEnsemble(numberOfFeatures);
						   featureSubsetEnsemble.convertFrom(votes);*/
			newApproachSize.push(featureSubsetEnsemble.getFlattenedSize());
			double threshold = 1;
			//System.out.println("Threshold = " + threshold + "\t{" + toStringSubset(featureSubsetEnsemble.convertTo(threshold)) + "}");
			CombinedClassifierDistribution[] distributions = ClassifierCrossValidator.getCombinedDistributions(classifier1, featureSubsetEnsemble, training, testing);
			double[] decisions = new double[distributions.length];
			int correct = 0;
			for (int k = 0; k < distributions.length; k++) {
				decisions[k] = distributions[k].classifyInstance();
				if (decisions[k] == testing.get(k).classValue()) correct++;
			}
			newApproach.push((double) correct / testing.numInstances());
			int[] selected = featureSubsetEnsemble.convertTo(1.0);
			if (selected.length == 0) selected = featureSubsetEnsemble.convertTo(0.8);
			fsApproachSize.push(selected.length);
			fsApproach.push(ClassifierCrossValidator.validateFilteredClassifier(classifier1, selected, training, testing));
			evaluation.buildEvaluator(training);
			int[] singleSeleted = search.search(evaluation, training);
			singleFSApproachSize.push(singleSeleted.length);
			singleFSApproach.push(ClassifierCrossValidator.validateFilteredClassifier(classifier1, singleSeleted, training, testing));
			rawApproach.push(ClassifierCrossValidator.validateClassifier(classifier1, training, testing));
			System.out.print(j);
		}
		//System.out.println();
		System.out.print("\t" + newApproach.average() + "\t" + newApproachSize.average());
		simpleMatrix.push(0, currentRow, newApproach.average());
		simpleMatrix.push(4, currentRow, newApproachSize.average());
		System.out.print("\t" + fsApproach.average() + "\t" + fsApproachSize.average());
		simpleMatrix.push(1, currentRow, fsApproach.average());
		simpleMatrix.push(5, currentRow, fsApproachSize.average());
		System.out.print("\t" + singleFSApproach.average() + "\t" + singleFSApproachSize.average());
		simpleMatrix.push(2, currentRow, singleFSApproach.average());
		simpleMatrix.push(6, currentRow, singleFSApproachSize.average());
		System.out.print("\t" + rawApproach.average());
		simpleMatrix.push(3, currentRow, rawApproach.average());
		System.out.println();
	}

	public static String pad(String input) {
		int total = 20;
		String pad = "";
		for (int p = 0; p < total - input.length(); p++) pad += " ";
		return input + pad;
	}

	public static void applySettings() {

		datasets = Registry.getDatasets(datasetsString);
		classifier1 = Registry.getClassifier(classifierString1);
		classifier2 = Registry.getClassifier(classifierString2);
		classifier3 = Registry.getClassifier(classifierString3);
		evaluation = Registry.getEvaluation(evaluationKeyword);
		search = Registry.getSearch(searchKeyword);

		stringBuffer.append("EnsembleMode\t" + ensembleMode + "\n");
		stringBuffer.append("Evaluation\t" + evaluationKeyword + "\n");
		stringBuffer.append("Classifier\t" + classifierString1 + " & " + classifierString2 + " & " + classifierString3 + "\n");
		stringBuffer.append("Search\t" + searchKeyword + "\n");
		stringBuffer.append("- - - - - - - - - -" + "\n");

		System.out.println(stringBuffer.toString());

	}

	public static String toStringSubset(int[] subset) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < subset.length; i++) {
			buffer.append(subset[i] + ", ");
		}
		return buffer.toString();
	}
}
