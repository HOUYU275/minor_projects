package weka.fuzzy.measure;

import java.io.Serializable;
//import java.util.HashMap;

public class Clause implements Comparable<Clause>, Serializable {
	public boolean removed=false;
	public int hashCode;
	public double max=0;
	public double cardinality=0;
	public int setValues=0;
	static final long serialVersionUID = -6312951970168525411L;
	public int object1;
	public int object2;
	
	//private HashMap<Integer,Double> valuesMap;
	private double[] values;
	
	public Clause() {}
	
	public Clause(double[] v) {
		values = v;
		//setValuesMap(v);
	}
	
	public Clause(double[] v, int i, double s, double c,int sv) {
		values = v;
		//setValuesMap(v);
		
		hashCode = i;
		max = s;
		cardinality=c;
		setValues=sv;
	}
	
	/*public Clause(HashMap<Integer,Double> v, int i, double s, double c,int sv) {
		valuesMap = v;
		
		hashCode = i;
		max = s;
		cardinality=c;
		setValues=sv;
	}

	private void setValuesMap(double[] v) {
		valuesMap = new HashMap<Integer,Double>();
		
		for (int a=0;a<v.length;a++) {
			if (v[a]>0) valuesMap.put(a, v[a]);
			
		}
	}*/
	
	public void setObjects(int i, int j) {
		this.object1 = i;
		this.object2 = j;
	}
	
	public int getObject1() {
		return this.object1;
	}
	
	public int getObject2() {
		return this.object2;
	}
	
	public int hashCode() {
		return hashCode;
	}
	
	public String toString() {
		String ret="";
		
		for (int a=0;a<values.length;a++) ret+="  "+(float)values[a];
		
		return ret;
	}

	public int compareTo(Clause c) {
		//return Double.compare(cardinality-c.cardinality));
		return setValues-c.setValues;
	}

	public double getVariableValue(int attr) {
		return values[attr];
		
		//if (!valuesMap.containsKey(attr)) return 0;
		//else return valuesMap.get(attr);
	}
	
	public void setVariable(int attr, double value) {
		values[attr]=value;
		//valuesMap.put(attr, value);
	}
	
	public Clause clone() {
		return new Clause(values.clone(),hashCode,max,cardinality,setValues);
		//return new Clause((HashMap<Integer,Double>)valuesMap.clone(),hashCode,max,cardinality,setValues);
	}
	
}
