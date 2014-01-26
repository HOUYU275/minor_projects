package test;

/**
 * Created by IntelliJ IDEA.
 * User: Ren
 * Date: 24/10/12
 * Time: 10:06
 */
public class SimpleMatrix {
	private double[][] storage;
	public SimpleMatrix (int numColumns, int numRows) {
		this.storage = new double[numRows][numColumns];
	}
	public void push(int column, int row, double value) {
		this.storage[row][column] = value;
	}
	public double pop(int column, int row) {
		return this.storage[row][column];
	}

	public double average(int column) {
			double sum = 0d;
			for (int i = 0; i < storage.length; i++) {
				sum += storage[i][column];
			}
			return sum / storage.length;
	}
}
