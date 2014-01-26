package fuzzified.core;

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
    protected double value;
    protected double[] merits;

    public Note(double value, Musician musician) {
        this.value = value;
        this.musician = musician;
        this.merits = new double[musician.harmonyMemory.comparator.dimension];
        for (int i = 0; i < merits.length; i++) merits[i] = Double.NEGATIVE_INFINITY;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
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

        if (Double.compare(note.value, value) != 0) return false;
        if (musician != null ? !musician.equals(note.musician) : note.musician != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = musician != null ? musician.hashCode() : 0;
        temp = value != +0.0d ? Double.doubleToLongBits(value) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public int compareTo(Object o) {
        if (this.value > ((Note) o).value) return 1;
        if (this.value < ((Note) o).value) return -1;
        return 0;
    }

    @Override
    public String toString() {
        return String.valueOf(value).substring(0, 8);
    }
}
