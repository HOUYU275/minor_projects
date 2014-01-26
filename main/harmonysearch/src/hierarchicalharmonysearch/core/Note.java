package hierarchicalharmonysearch.core;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 26/01/11
 * Time: 11:20
 * To change this template use File | Settings | File Templates.
 */
public class Note implements Comparable {

    //private Section section;
    private Musician musician;
    private double value;
    private double[] merits;

    public Note(double value, Musician musician) {
        this.value = value;
        this.musician = musician;
        //this.section = section;
        this.merits = new double[musician.getSection().getHarmonyMemory().getDimension()];
        for (int i = 0; i < merits.length; i++) merits[i] = Double.NEGATIVE_INFINITY;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Section getSection() {
        return musician.getSection();
    }

    /*public void setSection(Section section) {
        this.section = section;
    }*/

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

        return true;
    }

    @Override
    public int hashCode() {
        long temp = value != +0.0d ? Double.doubleToLongBits(value) : 0L;
        return (int) (temp ^ (temp >>> 32));
    }

    public int compareTo(Object o) {
        Note note = (Note) o;
        int result = 0;

        for (int i = 0; i < merits.length; i++) {
            if (note.getMerits()[i] > merits[i]) {
                result = 1;
                break;
            } else if (note.getMerits()[i] == merits[i]) {
                result = 0;
            } else {
                result = -1;
                break;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "Note{" +
                "value=" + value +
                ", merits=" + merits[0] + "" + ((merits.length > 1) ? merits[1] : "") +
                '}';
        /*return "Note{" +
                "" + value +
                '}';*/
    }

    public Musician getMusician() {
        return musician;
    }

    public void setMusician(Musician musician) {
        this.musician = musician;
    }
}