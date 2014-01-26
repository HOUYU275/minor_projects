package weka.fuzzy.similarity;

/**
 * Created by IntelliJ IDEA.
 * User: endario
 * Date: 28/10/2012
 * Time: 15:10
 */
public class FloatRelation {
	float[][] vals;

	public FloatRelation() {}

	public FloatRelation(int s) {
		vals = new float[s][];
		for (int i = 0; i < s; i++) {
			vals[i] = new float [i+1];
		}
	}

	public void setCell(int i, int j, float value) {
		if (i>=j) vals[i][j]=value;
		else vals[j][i]=value;
	}

	public float getCell(int i, int j) {
		if (i>=j) return vals[i][j];
		else return vals[j][i];
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
