package originalharmonysearch.core;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 27-Oct-2010
 * Time: 11:01:17
 * To change this template use File | Settings | File Templates.
 */
public class Note implements Comparable {

    private Musician musician;
    private double value;
    //private boolean isContinuous;
    private double[] merits;

    public Note(double value, /*boolean continuous, */Musician musician) {
        this.value = value;
        //isContinuous = continuous;
        this.musician = musician;
        this.merits = new double[musician.getHarmonyMemory().getDimension()];
        for (int i = 0; i < merits.length; i++) merits[i] = Double.NEGATIVE_INFINITY;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    /*public boolean isContinuous() {
        return isContinuous;
    }

    public void setContinuous(boolean continuous) {
        isContinuous = continuous;
    }*/

    public Musician getMusician() {
        return musician;
    }

    public void setMusician(Musician musician) {
        this.musician = musician;
    }

    public double[] getMerits() {
        return merits;
    }

    public void setMerits(double[] merits) {
        for (int i = 0; i < merits.length; i++) {
            if (merits[i] > this.merits[i]) this.merits[i] = merits[i];
        }
    }

    public void setMerit(double merit, int index) {
        //System.out.println(this.merits[index] + " & " + merit);
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
/*@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Note note = (Note) o;

        //if (isContinuous != note.isContinuous) return false;
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
        //result = 31 * result + (isContinuous ? 1 : 0);
        return result;
    }*/

    public int compareTo(Object o) {
        if (this.value > ((Note) o).value) return 1;
        if (this.value < ((Note) o).value) return -1;
        return 0;
    }
}
