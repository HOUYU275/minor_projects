package weka.classifiers.fuzzy;

/*
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

/*
 *    FuzzyRoughNN.java
 *
 */


import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.Capabilities;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Option;
import weka.core.OptionHandler;
import weka.core.Utils;
import weka.core.Capabilities.Capability;
import weka.fuzzy.implicator.*;
import weka.fuzzy.tnorm.*;
import weka.fuzzy.similarity.*;

import java.io.Serializable;
import java.util.*;


/**
 <!-- globalinfo-start -->
 * Fuzzy-rough LEM. <br/>
 * <br/>
 * 
 * <p/>
 <!-- globalinfo-end -->
 * 
 *
 <!-- options-start -->
 * Valid options are: <p/>
 * 
 * <pre> -D
 *  If set, classifier is run in debug mode and
 *  may output additional info to the console</pre>
 * 
 <!-- options-end -->
 *
 * @author Richard Jensen
 * @version $Revision: 1.19 $
 */
public class FLEM 
extends FuzzyRoughRuleInducer 
implements OptionHandler {

	/** for serialization. */
	static final long serialVersionUID = -3080186098777067172L;
	public Implicator m_Implicator = new ImplicatorKD();

	//whether to use the weak positive region or not
	public boolean m_weak=true;
	public ArrayList<ArrayList<AVPair>> [] rules;
	public static String name = "FLEM";

	public FLEM() {
		init();
	}

	public class AVPair {
		int attr;
		int obj;

		public AVPair(int a, int o) {
			attr=a;
			obj=o;
		}
	}

	/**
	 * Returns a string describing classifier.
	 * @return a description suitable for
	 * displaying in the explorer/experimenter gui
	 */
	public String globalInfo() {

		return  name+" - fuzzy-rough LEM.\n\n"
		+ "For more information, see\n\n";
	}





	/**
	 * Get the number of training instances the classifier is currently using.
	 * 
	 * @return the number of training instances the classifier is currently using
	 */
	public int getNumTraining() {

		return m_numInstances;
	}

	/**
	 * Returns default capabilities of the classifier.
	 *
	 * @return      the capabilities of this classifier
	 */
	public Capabilities getCapabilities() {
		Capabilities result = super.getCapabilities();

		// attributes
		result.enable(Capability.NOMINAL_ATTRIBUTES);
		result.enable(Capability.NUMERIC_ATTRIBUTES);
		result.enable(Capability.MISSING_VALUES);

		// class
		result.enable(Capability.NOMINAL_CLASS);
		result.enable(Capability.NUMERIC_CLASS);
		//result.enable(Capability.MISSING_CLASS_VALUES);

		// instances
		result.setMinimumNumberInstances(0);

		return result;
	}



	/**
	 * Returns an enumeration describing the available options.
	 *
	 * @return an enumeration of all the available options.
	 */
	public Enumeration<Option> listOptions() {

		Vector<Option> newVector = new Vector<Option>(8);

		newVector.addElement(new Option(
				"\tNumber of nearest neighbours (k) used in classification.\n"+
				"\t(Default = 1)",
				"K", 1,"-K <number of neighbors>"));
		newVector.addElement(new Option(
				"\tSelect the number of nearest neighbours between 1\n"+
				"\tand the k value specified using hold-one-out evaluation\n"+
				"\ton the training data (use when k > 1)",
				"X", 0,"-X"));
		newVector.addElement(new Option(
				"\tThe nearest neighbour search algorithm to use "+
				"(default: weka.core.neighboursearch.LinearNNSearch).\n",
				"A", 0, "-A"));

		return newVector.elements();
	}

	/**
	 * Parses a given list of options. <p/>
	 *
 <!-- options-start -->
	 * Valid options are: <p/>
	 * 
 <!-- options-end -->
	 *
	 * @param options the list of options as an array of strings
	 * @throws Exception if an option is not supported
	 */
	public void setOptions(String[] options) throws Exception {

		String optionString = Utils.getOption('I', options);
		if(optionString.length() != 0) {
			String nnSearchClassSpec[] = Utils.splitOptions(optionString);
			if(nnSearchClassSpec.length == 0) { 
				throw new Exception("Invalid Implicator specification string."); 
			}
			String className = nnSearchClassSpec[0];
			nnSearchClassSpec[0] = "";

			setImplicator( (Implicator) Utils.forName( Implicator.class, className, nnSearchClassSpec) );
		}
		else {
			setImplicator(new ImplicatorLukasiewicz());
		}


		optionString = Utils.getOption('T', options);
		if(optionString.length() != 0) {
			String nnSearchClassSpec[] = Utils.splitOptions(optionString);
			if(nnSearchClassSpec.length == 0) { 
				throw new Exception("Invalid TNorm specification string."); 
			}
			String className = nnSearchClassSpec[0];
			nnSearchClassSpec[0] = "";

			setTNorm( (TNorm) Utils.forName( TNorm.class, className, nnSearchClassSpec) );
		}
		else {
			setTNorm(new TNormLukasiewicz());
		}

		optionString = Utils.getOption('R', options);
		if(optionString.length() != 0) {
			String nnSearchClassSpec[] = Utils.splitOptions(optionString);
			if(nnSearchClassSpec.length == 0) { 
				throw new Exception("Invalid Similarity specification string."); 
			}
			String className = nnSearchClassSpec[0];
			nnSearchClassSpec[0] = "";

			setSimilarity( (Similarity) Utils.forName( Similarity.class, className, nnSearchClassSpec) );
		}
		else {
			setSimilarity(new Similarity3());
		}

		setWeak(Utils.getFlag('W', options));

		optionString = Utils.getOption('P', options);
		if (optionString.length() != 0) {
			setPruning(Integer.parseInt(optionString));
		} else {
			setPruning(0);
		}

		Utils.checkForRemainingOptions(options);
	}

	public void setPruning(int w) {
		m_Pruning = w;
	}

	public int getPruning() {
		return m_Pruning;
	}


	public void setWeak(boolean w) {
		m_weak = w;
	}

	public boolean getWeak() {
		return m_weak;
	}

	public void setImplicator(Implicator impl) {
		m_Implicator = impl;
	}

	public Implicator getImplicator() {
		return m_Implicator;
	}

	//set the relation composition operator = tnorm
	public void setTNorm(TNorm tnorm) {
		m_TNorm = tnorm;
	}

	public TNorm getTNorm() {
		return m_TNorm;
	}

	public Similarity getSimilarity() {
		return m_Similarity;
	}

	public void setSimilarity(Similarity s) {
		m_Similarity = s;
	}

	/**
	 * Gets the current settings of IBk.
	 *
	 * @return an array of strings suitable for passing to setOptions()
	 */
	public String [] getOptions() {

		String [] options = new String [15];
		int current = 0;

		options[current++] = "-I";
		options[current++] = (m_Implicator.getClass().getName() + " " +
				Utils.joinOptions(m_Implicator.getOptions())).trim();

		options[current++] = "-T";
		options[current++] = (m_TNorm.getClass().getName() + " " +
				Utils.joinOptions(m_TNorm.getOptions())).trim();

		options[current++] = "-R";
		options[current++] = (m_Similarity.getClass().getName() + " " + Utils.joinOptions(m_Similarity.getOptions())).trim(); 

		if (getWeak()) {
			options[current++] = "-W";
		}

		options[current++] = "-P"; options[current++] = "" + getPruning();

		while (current < options.length) {
			options[current++] = "";
		}

		return options;
	}




	/**
	 * Returns a description of this classifier.
	 *
	 * @return a description of this classifier as a string.
	 */
	public String toString() {

		if (m_Train == null) {
			return name+": No model built yet.";
		}

		String result = name+"\n" +
		"Fuzzy-rough rule induction\n";
		StringBuffer text = new StringBuffer();
		text.append("\nSimilarity measure: "+m_Similarity);
		text.append("\nImplicator: "+m_Implicator);
		text.append("\nT-Norm: "+m_TNorm+"\nRelation composition: "+m_Similarity.getTNorm());
		text.append("\n\nNumber of rules: "+cands.size());
		text.append("\nAverage rule arity: "+arity);

		result+=text.toString();

		return result;
	}

	/**
	 * Initialise scheme variables.
	 */
	protected void init() {
		m_TNorm = new TNormKD();
		m_Implicator = new ImplicatorKD();
		m_Similarity = new Similarity1();

	}

	/**
	 * Generates the classifier.
	 *
	 * @param instances set of instances serving as training data 
	 * @throws Exception if the classifier has not been generated successfully
	 */
	public void buildClassifier(Instances instances) throws Exception {

		// can classifier handle the data?
		getCapabilities().testWithFail(instances);

		// remove instances with missing class
		instances = new Instances(instances);
		instances.deleteWithMissingClass();

		m_numClasses = instances.numClasses();
		m_ClassType = instances.classAttribute().type();
		m_Train = new Instances(instances, 0, instances.numInstances());

		m_Similarity.setInstances(m_Train);
		//m_DecisionSimilarity.setInstances(m_Train);
		//m_SimilarityEq.setInstances(m_Train);

		m_composition = m_Similarity.getTNorm();
		m_SNorm = m_TNorm.getAssociatedSNorm();
		m_classIndex = m_Train.classIndex();
		m_numInstances = m_Train.numInstances();

		global = new double[m_numInstances];

		arity=0;
		induce(-1);
	}


	/**
	 * Induce (and store) rules via fuzzy-rough LEM
	 */
	public void induce(double full) {

		//rules = new ArrayList<ArrayList<AVPair>> [m_numClasses];

		for (int d=0;d<m_numClasses;d++) {
			rules[d] = LEM(d);
		}

		if (m_Debug) {
			Iterator<FuzzyRule> it = cands.iterator();
			FuzzyRule rule;

			while (it.hasNext()) {
				rule = it.next();
				arity+=rule.features.cardinality();
			}
			arity/=cands.size();
			System.err.println("Finished induction: "+cands.size()+" rule(s)");
			System.err.println("Average rule arity = "+arity+"\n");
		}

	}

	//d is the decision concept index
	public ArrayList<ArrayList<AVPair>> LEM(int d) {
		double[] concept = new double[m_numInstances];
		double[] covered = new double[m_numInstances];

		for (int o=0;o<m_numInstances;o++) {
			if (m_Train.instance(o).classValue()==d) concept[o]=1;
		}

		ArrayList<ArrayList<AVPair>> ruleset = new ArrayList<ArrayList<AVPair>>();

		int bestAttr=-1;
		int bestObj=-1;

		boolean notCovered=true;

		while (notCovered) {

			boolean removed[][] = new boolean[m_Train.numAttributes()][m_numInstances];
			boolean go=true;
			double[] values = new double[m_numInstances];
			double[] t = new double[m_numInstances];
			double[] G = concept;
			double[] currentEqClass = new double[m_numInstances];
			for (int o=0;o<m_numInstances;o++) currentEqClass[o]=1;
			ArrayList<AVPair> T = new ArrayList<AVPair>();

			while (go) {
				double bestCardinality=-1;
				double bestCard=-1;
				//work out which attribute-value pair to consider
				for (int a=0;a<m_Train.numAttributes();a++) {
					if (a!=m_classIndex) {
						for (int o=0;o<m_numInstances;o++) {
							if (!removed[a][o]) {
								double card=0;
								double card2=0;
								double val = m_Train.instance(o).value(a);

								//how much this eq class overlaps with the concept
								for (int o2=0;o2<m_numInstances;o2++) {
									double sim=fuzzySimilarity(a,val,m_Train.instance(o2).value(a));
									t[o2] = sim;

									card+= m_TNorm.calculate(t[o2], G[o2]);
									card2+=sim;
								}

								//card/=card2;

								if (card>bestCardinality||(card==bestCardinality&&card2<bestCard)) {
									values = t;
									bestAttr=a;
									bestObj=o;
									bestCardinality=card;
									bestCard=card2;
								}
							}
						}
					}
				}

				//bestAttr and bestObj determine the chosen eq class
				removed[bestAttr][bestObj]=true;
				T.add(new AVPair(bestAttr,bestObj));


				go=false;
				for (int o=0;o<m_numInstances;o++) {
					currentEqClass[o] = m_composition.calculate(currentEqClass[o], values[o]);
					G[o]= m_TNorm.calculate(G[o], currentEqClass[o]);
					covered[o] = m_SNorm.calculate(covered[o], currentEqClass[o]);
					
					if (currentEqClass[o]<concept[o]) go=true;
				}
				
			}
			ruleset.add(T);
			
			notCovered=false;

			// update fuzzy coverage
			for (int o2=0;o2<m_numInstances;o2++) {
				
				G[o2] = Math.max(0, concept[o2]-covered[o2]);

				//if all objects aren't covered, then loop
				if (covered[o2]<concept[o2]) notCovered=true;
			}

		}
		return ruleset;
	}

	public double m_alpha=1;
	public double m_beta=1;

	/**
	 * Calculates the class membership probabilities for the given test instance.
	 *
	 * @param instance the instance to be classified
	 * @return predicted class probability distribution
	 * @throws Exception if an error occurred during the prediction
	 */
	public double [] distributionForInstance(Instance instance) throws Exception {

		if (m_numInstances == 0) {
			throw new Exception("No training instances!");
		}


		double [] distribution=null;
		if (m_ClassType == Attribute.NOMINAL) distribution = new double[m_numClasses];
		else distribution = new double[m_numInstances];

		double total=0;
		double bestValue=-1;
		int bestClass=-1;

		//System.err.println(neighbours.numInstances());

		if (m_ClassType == Attribute.NOMINAL) {
			distribution = new double [m_numClasses];

			Iterator<FuzzyRule> it = cands.iterator();
			FuzzyRule rule;

			while (it.hasNext()) {
				rule = it.next();
				double value=1;

				for (int a = rule.features.nextSetBit(0); a >= 0; a = rule.features.nextSetBit(a + 1)) {
					if (a != m_classIndex) {
						//value = Math.min(fuzzySimilarity(a,m_Train.instance(rule.object).value(a),instance.value(a)), value);
						value = m_composition.calculate(fuzzySimilarity(a,m_Train.instance(rule.instance).value(a),instance.value(a)), value);
						if (value==0) break;
					}
				}

				distribution[(int)m_Train.instance(rule.instance).classValue()]+=value;
				total+=value;


				/*if (value>bestValue) {
					bestValue=value;
					bestClass = (int)m_Train.instance(rule.object).value(m_classIndex);
				}
				if (bestValue==1) break;*/
			}
			//if (bestClass>-1) distribution[bestClass]=1;

			if (total>0) Utils.normalize(distribution, total);
		}
		else {//if (m_ClassType == Attribute.NUMERIC) {
			double denom=0;double num=0;

			Iterator<FuzzyRule> it = cands.iterator();
			FuzzyRule rule;

			while (it.hasNext()) {
				rule = it.next();
				double value=1;

				for (int a = rule.features.nextSetBit(0); a >= 0; a = rule.features.nextSetBit(a + 1)) {
					if (a != m_classIndex) {
						value = m_composition.calculate(fuzzySimilarity(a,m_Train.instance(rule.instance).value(a),instance.value(a)), value);
						if (value==0) break;
					}
				}

				num+=value*m_Train.instance(rule.instance).value(m_classIndex);
				denom+=value;
			}
			distribution[0] = num/denom;
		}

		return distribution;
	}



	/**
	 * Main method for testing this class.
	 *
	 * @param argv should contain command line options (see setOptions)
	 */
	public static void main(String [] argv) {
		runClassifier(new FLEM(), argv);
	}
}
