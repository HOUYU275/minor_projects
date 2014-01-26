package weka.fuzzy.measure;

import weka.core.*;
import weka.fuzzy.implicator.Implicator;
import weka.fuzzy.similarity.Relation;
import weka.fuzzy.similarity.Similarity;
import weka.fuzzy.similarity.SimilarityEq;
import weka.fuzzy.snorm.SNorm;
import weka.fuzzy.tnorm.TNorm;

import java.io.Serializable;
import java.util.BitSet;
import java.util.Enumeration;
import java.util.Vector;


public abstract class ClusterFuzzyMeasure implements OptionHandler, Serializable  {
	// normalising factor
	double c_divisor = -1; // may need to change this

	public Similarity m_Similarity;
	public Similarity m_SimilarityEq = new SimilarityEq();
	public Similarity m_DecisionSimilarity;

	public TNorm m_TNorm;
	public TNorm m_composition;
	public Implicator m_Implicator;
	public SNorm m_SNorm;
	Relation current;
	
	int[] indexes;
	double[] vals; 
	int[] decIndexes;
	double[] decVals;
	int m_numInstances;
	int m_numAttribs;
	int m_classIndex;
	Instances m_trainInstances;
	double n_objects_d;
	
	public ClusterFuzzyMeasure() {
		
	}
	
	public void set(Similarity condSim, Similarity decSim, TNorm tnorm, TNorm compose, Implicator impl, SNorm snorm, int inst, int attrs, int classIndex, Instances ins) {
		m_Similarity = condSim;
		m_DecisionSimilarity = decSim;
		m_TNorm = tnorm;
		m_composition = compose;
		m_Implicator = impl;
		m_SNorm = snorm;
		m_numInstances = inst;
		n_objects_d = (double)inst;
		m_numAttribs = attrs;
		m_classIndex = classIndex;
		m_trainInstances = ins;
		current = new Relation(m_numInstances);
		
		//prepare optimization information
		decIndexes = new int[m_numInstances];

		for (int i = 0; i < m_numInstances; i++) {
			boolean same = false;

			for (int j = i + 1; j < m_numInstances; j++) {

				for (int d = 0; d < m_numInstances; d++) {
					double decSim1 = fuzzySimilarity(m_classIndex, i, d);
					double decSim2 = fuzzySimilarity(m_classIndex, j, d);

					if (decSim1!=decSim2) {same=false; break;}
					else same=true;

				}
				if (same) {
					decIndexes[j] = i + 1;
					break;
				} // i+1 as default is 0
			}

		}
	}
	
	/**
	 * Returns a string describing this object.
	 * 
	 * @return 		a description of the evaluator suitable for
	 * 			displaying in the explorer/experimenter gui
	 */
	public abstract String globalInfo();

	public abstract String toString();
	
	public String[] getOptions() {
		Vector<String>	result;
	    
	    result = new Vector<String>();
	    return result.toArray(new String[result.size()]);
	}

	public Enumeration listOptions() {
		Vector result = new Vector();
		return result.elements();
	}

	public void setOptions(String[] options) throws Exception {
		// no options
		
	}


	public final void setIndexes(Relation rel) {
		int m_numInstances = rel.size;
		
		for (int i = 0; i < m_numInstances; i++) {
			boolean same = false;

			for (int j = i + 1; j < m_numInstances; j++) {

				for (int d = 0; d < m_numInstances; d++) {
					if (rel.getCell(i, d) != rel.getCell(j, d)) {
						same = false;
						break;
					} else
						same = true;
				}
				if (same) {
					indexes[j] = i + 1;
					break;
				} // i+1 as default is 0
			}

		}
	}
	
	public abstract double calculate(BitSet subset);
	
	public final double getConsistency() {
		BitSet full = new BitSet(m_numAttribs);
		for (int a = 0; a < m_numAttribs - 1; a++)
			full.set(a);


		double c_div = calculate(full);

		if (c_div == 0 || c_div == Double.NaN) {
			System.err.println("\n*** Inconsistent data (full dataset value = "
					+ c_div + " for this measure) ***\n");
			c_div=1;
			//System.exit(1);
		}

		return c_div;
	}
	
	public final void generatePartition(BitSet reduct) {
	       int nextBit = reduct.nextSetBit(0);
	       if (nextBit==m_classIndex) nextBit=reduct.nextSetBit(nextBit+1);

	       for (int i = 0; i < m_numInstances; i++) {

	           current.setCell(i, i, 1);

	           for (int j = i + 1; j < m_numInstances; j++) {
	               double rel = fuzzySimilarity(nextBit, i, j);

	               current.setCell(i, j, rel*rel);
	           }
	       }


	       if (reduct.cardinality() > 1) {
	           for (int o = 0; o < m_numInstances; o++) {
	               for (int o1 = o + 1; o1 < m_numInstances; o1++) {
	                   for (int a = reduct.nextSetBit(nextBit + 1); a >= 0; a = reduct.nextSetBit(a + 1)) {
	                       if (a!=m_classIndex) {
	                    	   double rel = fuzzySimilarity(a, o, o1);
	                            rel*=current.getCell(o, o1);
	                            current.setCell(o, o1, rel);

	                        }
	                    }

	                   double sim = current.getCell(o,o1);
	                   sim = Math.sqrt(sim);
	                   current.setCell(o,o1,sim);
	               }
	           }
	       }
	   }


	public final double fuzzySimilarity(int attr, int x, int y) {
		double ret = 0;			

		//no decision feature, so each object is distinct
		if (attr<0 && attr==m_classIndex) {
			ret=0;
		}
		else {
			double mainVal=m_trainInstances.instance(x) .value(attr);
			double otherVal=m_trainInstances.instance(y) .value(attr);

			//if it's the class attribute, use the class similarity measure
			//if it's a nominal attribute, then use crisp equivalence
			//otherwise use the general similarity measure
			if (Double.isNaN(mainVal)||Double.isNaN(otherVal)) ret=1;	
			else if (attr==m_classIndex) ret = m_DecisionSimilarity.similarity(attr, mainVal, otherVal);
			else if (m_trainInstances.attribute(attr).isNominal()) ret = m_SimilarityEq.similarity(attr, mainVal, otherVal);
			else ret = m_Similarity.similarity(attr, mainVal, otherVal);

		}
		return ret;
	}
}

