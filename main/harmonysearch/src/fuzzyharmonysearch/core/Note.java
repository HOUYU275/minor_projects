package fuzzyharmonysearch.core;

import fuzzy.FuzzyNumber;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 27-Oct-2010
 * Time: 11:01:17
 * To change this template use File | Settings | File Templates.
 */
public class Note implements Comparable {

    private double control = 0.5d;

    private Musician musician;
    private Harmony harmony;
    private FuzzyNumber value;
    //private double quality;

    public Note(FuzzyNumber value, Musician musician, Harmony harmony) {
        this.value = value;
        this.musician = musician;
        this.harmony = harmony;
        //this.quality = 1d;
    }

    public FuzzyNumber getValue() {
        return value;
    }

    public void setValue(FuzzyNumber value) {
        this.value = value;
    }

    public Musician getMusician() {
        return musician;
    }

    public void setMusician(Musician musician) {
        this.musician = musician;
    }

    /*public double getDistance() {
        //return this.
        return quality;
    }

    public void setDistance(double quality) {
        this.quality = quality;
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Note note = (Note) o;

        if (!musician.equals(note.musician)) return false;
        if (!value.equals(note.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = musician.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    public int compareTo(Object o) {
        return this.compareTo(o);
    }

    @Override
    protected Note clone() throws CloneNotSupportedException {
        return new Note(new FuzzyNumber(value.getLeftShoulder(), value.getCentroid(), value.getRightShoulder()), musician, harmony);
    }
/*@Override
    protected Note clone() {
        return new Note(new FuzzyNumber(value.getLeftShoulder(), value.getCentroid(), value.getRightShoulder()), musician);
    }*/

    /*public boolean qualityDecrease() {
        //quality = (quality - 0.1 <= 0) ? 0 : quality - 0.1;
        quality = quality * control;
        return (quality < 0.5);
    }

    public boolean qualityIncrease() {
        quality = (quality / control >= 1) ? 1 : quality / control;
        return (quality == 1);
    }*/

    public double getQuality() {
        //return harmony.getMerit().getRepresentativeValue();
        return harmony.getDistance();
    }
}
