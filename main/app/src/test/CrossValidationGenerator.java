package test;

import weka.core.Instances;

import java.io.FileReader;
import java.io.IOException;
import java.util.Random;


public class CrossValidationGenerator {

	Instances randInss;
	Random rand;
	int randSeed = 0;
	int numfolds = 10;
	RandomInstance ri;

	public CrossValidationGenerator(Instances inss, int numfolds)
	{
		this.rand = new Random(System.currentTimeMillis());
		this.randInss = new Instances(inss);
		this.numfolds = numfolds;
		randInss.randomize(rand);
		ri = new RandomInstance();

		/*for numberic class label only*/
		//randInss.stratify(numfolds);
	}

	public Instances getTrainFold(int foldIndex)
	{
		Instances train = randInss.trainCV(numfolds, foldIndex);
		return train;
	}

	public Instances getTestFold(int foldIndex)
	{
		Instances test = randInss.testCV(numfolds, foldIndex);
		return test;
	}

	public Instances getDisturbTrain(int foldIndex)
	{
		Instances train = ri.getRandInstances(randInss.trainCV(numfolds, foldIndex), 0.1);
		return train;
	}

	public Instances getDisturbTest(int foldIndex)
	{
		Instances test = ri.getRandInstances(randInss.testCV(numfolds, foldIndex), 0.1);
		return test;
	}

	public static void main(String[] args) throws IOException
	{
		FileReader file;
		file = new FileReader("");
		Instances inss = new Instances(file);
		CrossValidationGenerator cvg = new CrossValidationGenerator(inss,10);
		Instances rand1 = cvg.getTrainFold(0);
		System.out.println(rand1.toString());
		System.out.println("*****************************line********************************");
		System.out.println(cvg.getTestFold(0));
	}
}