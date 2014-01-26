/**
 *
 */
package weka.fuzzy.measure;

import util.Dataset;
import weka.core.Capabilities;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Option;
import weka.core.OptionHandler;
import weka.core.Range;
import weka.core.RevisionUtils;
import weka.core.UnsupportedAttributeTypeException;
import weka.core.Utils;
import weka.core.Capabilities.Capability;
import weka.filters.Filter;
import weka.filters.SupervisedFilter;
import weka.attributeSelection.JohnsonReducer;
import weka.fuzzy.implicator.Implicator;
import weka.fuzzy.implicator.ImplicatorLukasiewicz;
import weka.fuzzy.measure.*;
import weka.fuzzy.similarity.Similarity;
import weka.fuzzy.similarity.Similarity2;
import weka.fuzzy.similarity.Similarity4;
import weka.fuzzy.similarity.SimilarityEq;
import weka.fuzzy.snorm.SNorm;
import weka.fuzzy.snorm.SNormLukasiewicz;
import weka.fuzzy.tnorm.TNorm;
import weka.fuzzy.tnorm.TNormLukasiewicz;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;



/**
<!-- globalinfo-start -->
* Filters instances according to the value of an attribute.
* <p/>
<!-- globalinfo-end -->
*
<!-- options-start -->
* Valid options are: <p/>
*
<!-- options-end -->
*
* @author ncm
* @version $Revision: 1.0 $
*/
public class BiReductFilter extends Filter
implements SupervisedFilter, OptionHandler, Serializable {

	/** for serialization */
	static final long serialVersionUID = 4752870393679263361L;

	/** Stores which values of nominal attribute are to be used for filtering.*/
	protected Range m_Values;

	/** Stores which value of a numeric attribute is to be used for filtering.*/
	protected double m_Value = 0;

	/** True if missing values should count as a match */
	protected boolean m_MatchMissingValues = false;

	/** Modify header for nominal attributes? */
	protected boolean m_ModifyHeader = false;

	/** If m_ModifyHeader, stores a mapping from old to new indexes */
	protected int [] m_NominalMapping;

	public Similarity m_Similarity = new Similarity4();
	public Similarity m_SimilarityEq = new SimilarityEq();
	public Similarity m_DecisionSimilarity = new SimilarityEq();
	public FuzzyMeasure m_Measure= new FuzzyDiscernibilityBiReduct();
	public JohnsonReducer as = new JohnsonReducer();


	public TNorm m_TNorm = new TNormLukasiewicz();
	public TNorm m_composition = new TNormLukasiewicz();
	public Implicator m_Implicator = new ImplicatorLukasiewicz();
	public SNorm m_SNorm = new SNormLukasiewicz();

	public ArrayList<Clause> clauseList=null;

	public boolean iterative=false;

	/**
	 *
	 */
	public BiReductFilter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Enumeration listOptions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setOptions(String[] options) throws Exception {
		String optionString;

		optionString = Utils.getOption('Z', options);
		if(optionString.length() != 0) {
			String nnSearchClassSpec[] = Utils.splitOptions(optionString);
			if(nnSearchClassSpec.length == 0) {
				throw new Exception("Invalid FuzzyMeasure specification string.");
			}
			String className = nnSearchClassSpec[0];
			nnSearchClassSpec[0] = "";

			setMeasure( (FuzzyMeasure) Utils.forName( FuzzyMeasure.class, className, nnSearchClassSpec) );
		}
		else {
			setMeasure(new WeakGamma());
		}


		optionString = Utils.getOption('I', options);
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
			setSimilarity(new Similarity2());
		}


		}




	public void setMeasure(FuzzyMeasure fe) {
		m_Measure = fe;
	}

	public FuzzyMeasure getMeasure() {
		return m_Measure;
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
		//m_composition = tnorm;
		m_SNorm = tnorm.getAssociatedSNorm();
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



	public boolean getIterative() {
		return iterative;
	}

	public void setIterative(boolean b) {
		iterative=b;
	}

	/**
	 * Gets the current settings of FuzzyRoughSubsetEval
	 *
	 * @return an array of strings suitable for passing to setOptions()
	 */
	public String[] getOptions ()  {
		Vector<String>	result;

		result = new Vector<String>();

		result.add("-Z");
		result.add((m_Measure.getClass().getName() + " " +
				Utils.joinOptions(m_Measure.getOptions())).trim());

		result.add("-I");
		result.add((m_Implicator.getClass().getName() + " " +
				Utils.joinOptions(m_Implicator.getOptions())).trim());

		result.add("-T");
		result.add((m_TNorm.getClass().getName() + " " +
				Utils.joinOptions(m_TNorm.getOptions())).trim());

		result.add("-R");
		result.add((m_Similarity.getClass().getName() + " " + Utils.joinOptions(m_Similarity.getOptions())).trim());

		if (getIterative()) {
			result.add("-B");
		}

		return result.toArray(new String[result.size()]);
		}

	public Capabilities getCapabilities() {
		Capabilities result = super.getCapabilities();

		// attributes
		result.enableAllAttributes();

		// class
		result.enableAllClasses();
		result.enable(Capability.NO_CLASS);

		return result;
	}


	public boolean batchFinished() throws Exception {
		/*System.out.println("Here");
		if (getInputFormat() == null) {
			throw new IllegalStateException("No input instance format defined");
		}*/

		if (!isFirstBatchDone()) {
			int count=0;

			Instances m_trainInstances = getInputFormat();
            m_trainInstances = Dataset.getDataset("iris");
            m_trainInstances.deleteWithMissingClass();

			double gamma=0;
			int m_numAttribs = m_trainInstances.numAttributes();
			int m_numObjects = m_trainInstances.numInstances();

			BitSet full = new BitSet(m_numAttribs);
			full.set(0, m_numAttribs);

			m_Similarity.setInstances(m_trainInstances);
			m_DecisionSimilarity.setInstances(m_trainInstances);
			m_SimilarityEq.setInstances(m_trainInstances);
			m_composition = m_Similarity.getTNorm();

			m_Measure.set(m_Similarity,m_DecisionSimilarity,m_TNorm,m_composition,m_Implicator,m_SNorm,m_numObjects-1,m_numAttribs,m_trainInstances.classIndex(),m_trainInstances);


			 //Get the full clause list
			clauseList = ((FuzzyDiscernibilityBiReduct)m_Measure).calculateMatrix();
			System.out.println("Size of clause list: " + clauseList.size());
			for (int i = 0; i < clauseList.size(); i++) {
				System.out.println("Clause " + i + " : " + clauseList.get(i) + " (" + clauseList.get(i).getObject1() + ", " + clauseList.get(i).getObject2() + ")");
			}
			System.out.println("line 310");

			int featFreq = as.heuristicPick(clauseList);
            //clauseList.get(featFreq);
			System.out.println("FeatFreq: " + featFreq);
			int[] objectFreq = as.countOccurence(clauseList);
			int highest = as.findHighestFrequency(objectFreq);
            for (int i = 0; i < clauseList.size(); i++) {
                if (clauseList.get(i).getObject1() == highest || clauseList.get(i).getObject1() == highest) clauseList.remove(i);
            }
			System.out.println("FeatFreq: " + featFreq);
			System.out.println("Highest obj: " + highest);




		}
		flushInput();
		m_NewBatch = true;
		m_FirstBatchDone = true;
		return (numPendingOutput() != 0);
	}

    /**
     * @param args
     */
    public static void main(String[] args) {
        BiReductFilter filter = new BiReductFilter();
        try {
            filter.batchFinished();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }


}
