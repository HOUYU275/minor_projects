package weka.fuzzy.measure;

import java.util.BitSet;
import java.util.Vector;

import weka.core.TechnicalInformation;
import weka.core.TechnicalInformationHandler;
import weka.core.Utils;
import weka.core.TechnicalInformation.Field;
import weka.core.TechnicalInformation.Type;
import weka.fuzzy.similarity.Relation;


public class WeakVQRSNoVQ extends FuzzyMeasure implements TechnicalInformationHandler {
	public double alpha=0.2;
	public double beta=1;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1063606253458807903L;

	public WeakVQRSNoVQ() {
		super();
	}
	
	/*public void set(Similarity condSim, Similarity decSim, TNorm tnorm,
			TNorm compose, Implicator impl, SNorm snorm, int inst, int attrs,
			int classIndex, Instances ins) {
		super.set(condSim, decSim, tnorm, compose, impl, snorm, inst, attrs, classIndex,
				ins);
	}*/
	
	/**
	 * Gets the current settings. Returns empty array.
	 *
	 * @return 		an array of strings suitable for passing to setOptions()
	 */
	public String[] getOptions(){
		Vector<String>	result;

		result = new Vector<String>();


		return result.toArray(new String[result.size()]);
	}
	
	
	/**
	 * Parses a given list of options.
	 *
	 * @param options 	the list of options as an array of strings
	 * @throws Exception 	if an option is not supported
	 */
	public void setOptions(String[] options) throws Exception {
		String	tmpStr;

		

	}
	
	@Override
	public double calculate(BitSet subset) {
		generatePartition(subset);
	
		double ret = 0;

		for (int x = 0; x < m_numInstances; x++) {
			double val = 0;

			val = lowerVQRS(current, x, x); // use object x's decision class only
			ret += val;

		}
		return Math.min(1, ret / n_objects_d);
	}

	private final double lowerVQRS(Relation rel, int x, int d) {
		double val = 0;
		double denom = 0;

		// for each fuzzy equivalence class in the relation
		for (int f = 0; f < m_numInstances; f++) {
			double condSim = rel.getCell(x, f);
			val += m_TNorm.calculate(condSim, fuzzySimilarity(m_classIndex, d, f));

			denom += condSim;

		}
		return val / denom; // return quantified value
	}

	
	/**
	 * Returns an instance of a TechnicalInformation object, containing 
	 * detailed information about the technical background of this class,
	 * e.g., paper reference or book this class is based on.
	 * 
	 * @return the technical information about this class
	 */
	public TechnicalInformation getTechnicalInformation() {
		TechnicalInformation result;

		result = new TechnicalInformation(Type.INPROCEEDINGS);
		result.setValue(Field.AUTHOR, "C. Cornelis and R. Jensen");
		result.setValue(Field.TITLE, "A Noise-tolerant Approach to Fuzzy-Rough Feature Selection");
		result.setValue(Field.BOOKTITLE, "17th International Conference on Fuzzy Systems (FUZZ-IEEE'08)");
		result.setValue(Field.YEAR, "2008");
		result.setValue(Field.PAGES, "1598-1605");
		result.setValue(Field.PUBLISHER, "IEEE");

		return result;
	}

	@Override
	public String globalInfo() {
		// TODO Auto-generated method stub
		return "Weak VQRS evaluator, using the vaguely quantified fuzzy rough lower approximations.\n\n"
		+ toString() + "\n\n" + getTechnicalInformation().toString();
	}

	public String toString() {
		return "Weak VQRS: alpha="+alpha+" beta="+beta;
	}
}
