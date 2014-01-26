package test;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.SparseInstance;

import java.io.FileReader;
import java.io.Reader;
import java.util.Random;



public class RandomInstance {

	double randomNum;
	Random rand;
	double[] maxs;
	double[] mins;


	public RandomInstance()
	{
		rand = new Random();

	}

	public void changeSeed()
	{
		rand.setSeed(System.currentTimeMillis());
	}

	public void changeSeed(long seed)
	{
		rand.setSeed(seed);
	}

	public Instance getRandInstance (Instance model, double radius, double center)
	{
		Instance ins = new SparseInstance(model);
		randomNum = rand.nextDouble();
		randomNum = (randomNum - 0.5) * radius / 0.5 + center;

		for(int i = 0; i<ins.numAttributes(); i++)
		{
			ins.setValue(i, ins.value(i)+randomNum);
		}
		return ins;
	}

	public Instances getRandInstances (Instances inss, double radius, double center)
	{
		Instances result = new Instances(inss);

		for(int i = 0; i<result.numInstances(); i++)
		{
			for(int j = 0; j<result.numAttributes(); j++)
			{
				randomNum = rand.nextDouble();
				randomNum = (randomNum - 0.5) * radius / 0.5 + center;
				result.instance(i).setValue(j, result.instance(i).value(j)+randomNum);
			}
		}
		return result;
	}

	public Instances getRandInstances(Instances inss, double radius)
	{
		Instances result = new Instances(inss);
		maxs = new double [result.numAttributes()];
		mins = new double [result.numAttributes()];

		for(int j = 0 ; j<maxs.length; j++)
		{
			maxs[j] = 0;
			mins[j] = 2000000000;

			for(int i = 0 ; i< result.numInstances(); i++)
			{
				if(maxs[j]<result.instance(i).value(j))
				{
					maxs[j] = result.instance(i).value(j);
				}
				if(mins[j]>result.instance(i).value(j))
				{
					mins[j] = result.instance(i).value(j);
				}
			}
		}

		for(int i = 0; i<result.numInstances(); i++)
		{
			for(int j = 0; j<result.numAttributes(); j++)
			{
				randomNum = rand.nextDouble();
				randomNum = (randomNum - 0.5) / 0.5 * (maxs[j]-mins[j])/2 * radius;

				if(result.instance(i).value(j)+randomNum>0)
				{
					result.instance(i).setValue(j, result.instance(i).value(j)+randomNum);
				}
				else
				{
					result.instance(i).setValue(j, result.instance(i).value(j)-randomNum);
				}

			}
		}
		return result;
	}

	public static void main(String arg[])
	{
		Instances inss;
		Instances randinss;
		Reader file;

		RandomInstance ri = new RandomInstance();

		try {
			file = new FileReader("C:\\Program Files\\Weka-3-6\\data\\iris.arff");
			inss = new Instances(file);
			randinss = ri.getRandInstances(inss, 0.1);

			for(int i = 0 ; i<randinss.numInstances(); i++)
			{
				for(int j = 0 ; j<randinss.numAttributes(); j++)
				{
					System.out.print(randinss.instance(i).value(j)+",");
				}
				System.out.println();

				for(int j = 0 ; j<randinss.numAttributes(); j++)
				{
					System.out.print(inss.instance(i).value(j)+",");
					inss.toString();
				}
				System.out.println();
			}
		}catch(Exception e)
		{

		}

	}

}
