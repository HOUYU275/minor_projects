package ruleinduction;

import weka.classifiers.fuzzy.FuzzyRule;
import weka.fuzzy.similarity.Relation;

import java.util.ArrayList;
import java.util.BitSet;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 20/12/11
 * Time: 12:32
 * To change this template use File | Settings | File Templates.
 */
public class Note implements Comparable {

    protected Musician musician;
    protected Harmony harmony;
    protected BitSet value;
    protected double[] merits;

    protected double[] equivalenceClasses;
    protected double lowerApproximation = 999999;

    public Note(BitSet value, Musician musician) {
        this.value = value;
        this.musician = musician;
        if (this.value != null) {
            Integer[] features = toIntegerArray();
            if (features.length == 0) {
                this.value = null;
            } else {
                Relation relation = musician.harmonyMemory.comparator.calculator.generatePartition(features);
                lowerApproximation = musician.harmonyMemory.comparator.calculator.getLowerApproximation(relation, musician.instance);
                equivalenceClasses = musician.harmonyMemory.comparator.calculator.getEquivalenceClasses(relation, musician.instance);
            }
        }
        this.merits = new double[musician.harmonyMemory.comparator.dimension];
        for (int i = 0; i < merits.length; i++) merits[i] = Double.NEGATIVE_INFINITY;
    }

    public BitSet getValue() {
        return value;
    }

    public void setValue(BitSet value) {
        this.value = value;
    }

    public double[] getMerits() {
        return merits;
    }

    public void setMerit(double merit, int index) {
        if (merit > this.merits[index]) this.merits[index] = merit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Note note = (Note) o;

        if (musician != null ? !musician.equals(note.musician) : note.musician != null) return false;
        if (value != null ? !value.equals(note.value) : note.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = musician != null ? musician.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    public int compareTo(Object o) {
        if (this.value.cardinality() > ((Note) o).value.cardinality()) return 1;
        if (this.value.cardinality() < ((Note) o).value.cardinality()) return -1;
        return 0;
    }

    @Override
    public String toString() {
        return String.valueOf(value).substring(0, 8);
    }

    public Integer[] toIntegerArray() {
        ArrayList<Integer> intArrayList = new ArrayList<Integer>();
        for (int i = value.nextSetBit(0); i >= 0; i = value.nextSetBit(i + 1)) {
            if (i != musician.harmonyMemory.instances.classIndex()) intArrayList.add(i);
        }
        return intArrayList.toArray(new Integer[intArrayList.size()]);
    }

    public FuzzyRule toFuzzyRule() {
        if (value == null) return null;
        return new FuzzyRule(equivalenceClasses, value, musician.instance);
        /*musician.harmonyMemory.comparator.calculator.getEquivalenceClasses(
     toIntegerArray(), musician.instance), value, musician.instance);*/
    }
}
