package weka.fuzzy.measure;

import java.util.BitSet;

import weka.core.TechnicalInformation;
import weka.core.TechnicalInformationHandler;
import weka.core.TechnicalInformation.Field;
import weka.core.TechnicalInformation.Type;


public class nnWeakGamma extends FuzzyMeasure implements TechnicalInformationHandler  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1063606253458807903L;

	public nnWeakGamma() {
		super();
	}

	/*public void set(Similarity condSim, Similarity decSim, TNorm tnorm,
			TNorm compose, Implicator impl, SNorm snorm, int inst, int attrs,
			int classIndex, Instances ins) {
		super.set(condSim, decSim, tnorm, compose, impl, snorm, inst, attrs, classIndex,
				ins);
	}*/

	@Override
	public double calculate(BitSet subset) {
		double ret = 0;
		int d = -1;

		//index of an object's nearest neighbour 
		int neighbour;

		//the highest similarity value for an object with a different class to x
		double[] best=new double[m_numInstances];

		int bestN[]=new int[m_numInstances];
		//for (int i=0;i<m_numInstances;i++) bestN[i]=-1;
		
		//search through the partition to find 'y' - the nearest neighbour
		//for each 'x' with a different class value
		for (int x=0; x<m_numInstances; x++) {
			//if (bestN[x]<0) {
				best[x] = -1;

				for (int y=0;y<m_numInstances;y++) {
					if (m_trainInstances.instance(x).classValue() != m_trainInstances.instance(y).classValue()) {
						double similarity=1;

						for (int a = subset.nextSetBit(0); a >= 0; a = subset.nextSetBit(a + 1)) {
							if (a!=m_classIndex) {
								similarity = m_composition.calculate(fuzzySimilarity(a, x, y), similarity);

								if (similarity == 0 || similarity<best[x]) break;
							}
						}

						if (x!=y && similarity >= best[x]) {
							best[x] = similarity;
							bestN[x] = y;

						}
						if (best[x]==1) break;
					}
				}
			/*}
			else {
				best[x] = best[bestN[x]];
			}*/
			neighbour = bestN[x];
			bestN[neighbour] = x;
			//best[neighbour] = best[x];
			
			d = x;// weak version, so only consider x's decision

			ret += m_Implicator.calculate(best[x], fuzzySimilarity(m_classIndex, d, neighbour));
		}


		return ret / n_objects_d;

	}

	/**
	 * Returns an instance of a TechnicalInformation object, containing 
	 * detailed information about the technical background of this class,
	 * e.g., paper reference or book this class is based on.
	 * 
	 * @return the technical information about this class
	 */
	public TechnicalInformation getTechnicalInformation() {
		TechnicalInformation        result;
		TechnicalInformation        additional;

		result = new TechnicalInformation(Type.ARTICLE);
		result.setValue(Field.AUTHOR, "R. Jensen, Q. Shen");
		result.setValue(Field.YEAR, "2009");
		result.setValue(Field.TITLE, "New Approaches to Fuzzy-rough Feature Selection. IEEE Transactions on Fuzzy Systems");
		result.setValue(Field.SCHOOL, "Aberystwyth University");

		additional = result.add(Type.INPROCEEDINGS);
		additional.setValue(Field.AUTHOR, "C. Cornelis, G. Hurtado Martin, R. Jensen and D. Slezak");
		additional.setValue(Field.TITLE, "Feature Selection with Fuzzy Decision Reducts");
		additional.setValue(Field.BOOKTITLE, "Third International Conference on Rough Sets and Knowledge Technology (RSKT'08)");
		additional.setValue(Field.YEAR, "2008");
		additional.setValue(Field.PAGES, "284-291");
		additional.setValue(Field.PUBLISHER, "Springer");

		return result;
	}

	@Override
	public String globalInfo() {
		return "Nearest neighbour weak gamma evaluator, using the fuzzy rough lower approximations.\n\n"
		+  getTechnicalInformation().toString();
	}

	public String toString() {
		return "nnWeak gamma";
	}
}
