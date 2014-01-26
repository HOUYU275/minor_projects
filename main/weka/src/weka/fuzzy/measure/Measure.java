package weka.fuzzy.measure;

import java.io.Serializable;
import java.util.BitSet;
import java.util.Vector;

import weka.core.Instances;
import weka.fuzzy.implicator.Implicator;
import weka.fuzzy.similarity.Similarity;
import weka.fuzzy.snorm.SNorm;
import weka.fuzzy.tnorm.TNorm;

public abstract class Measure implements Serializable {

	static final long serialVersionUID = 1063626753411807303L;
	
	public abstract double calculate(BitSet subset);
	
	public abstract void set(Similarity condSim, Similarity decSim, TNorm tnorm, TNorm compose, Implicator impl, SNorm snorm, int inst, int attrs, int classIndex, Instances ins);
	
	public abstract String globalInfo();

	public abstract String toString();
	
	public String[] getOptions() {
		Vector<String>	result;
	    
	    result = new Vector<String>();
	    return result.toArray(new String[result.size()]);
	}

	

	public void setOptions(String[] options) throws Exception {
		// no options
		
	}
}
