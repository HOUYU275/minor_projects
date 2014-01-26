package fuzzyharmonysearch.core;

import fuzzy.FuzzyNumber;

import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 19-Mar-2010
 * Time: 15:45:03
 * To change this template use File | Settings | File Templates.
 */
public class Harmony implements Comparable {
    private FuzzyNumber[] merits;
    private Note[] notes;
    private HarmonyMemory harmonyMemory;
    //private double distance;
    private double distance = 1d;

    public Harmony() {
    }

    public Harmony(HarmonyMemory harmonyMemory) {
        this.harmonyMemory = harmonyMemory;
        notes = new Note[harmonyMemory.getNumMusicians()];
        merits = new FuzzyNumber[harmonyMemory.getDimension()];
        for (int i = 0; i < merits.length; i++) {
            merits[i] = new FuzzyNumber(0, 0, 0);
        }
    }

    public HarmonyMemory getHarmonyMemory() {
        return harmonyMemory;
    }

    public void setHarmonyMemory(HarmonyMemory harmonyMemory) {
        this.harmonyMemory = harmonyMemory;
    }

    public int getSize() {
        return notes.length;
    }

    public FuzzyNumber[] getMerits() {
        return merits;
    }

    public void setMerits(FuzzyNumber[] merits) {
        this.merits = merits;
    }

    public void setMerit(FuzzyNumber merit) {
        this.merits[0] = merit;
    }

    public void setMerit(FuzzyNumber merit, int index) {
        this.merits[index] = merit;
    }

    public FuzzyNumber getMerit(int index) {
        return this.merits[index];
    }

    public FuzzyNumber getMerit() {
        return this.merits[0];
    }

    public Note[] getNotes() {
        return notes;
    }

    public FuzzyNumber[] getFuzzyNotes() {
        FuzzyNumber[] fuzzyNumbers = new FuzzyNumber[notes.length];
        for (int i = 0; i < notes.length; i++) {
            fuzzyNumbers[i] = notes[i].getValue();
        }
        return fuzzyNumbers;
    }

    public double[] getDoubleNotes() {
        double[] doubletNotes = new double[notes.length];
        for (int i = 0; i < notes.length; i++) {
            doubletNotes[i] = notes[i].getValue().getRepresentativeValue();
        }
        return doubletNotes;
    }

    public int[] getIntNotes() {
        int[] intNotes = new int[notes.length];
        for (int i = 0; i < notes.length; i++) {
            intNotes[i] = (int) Math.round(notes[i].getValue().getRepresentativeValue());
        }
        return intNotes;
    }

    public void setNotes(Note[] notes) {
        this.notes = notes;
    }

    public void setNote(int index, Note note) {
        notes[index] = note;
    }

    public Note getNote(int index) {
        return notes[index];
    }

    /*public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }*/

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String toString() {
        String s = "[";
        for (Note i : notes) {
            s = s + i.getValue().getRepresentativeValue() + " ";
        }
        s = s.substring(0, s.length() - 1);
        s += "]";
        return s;
    }

    public String printMerits() {
        String returnString = "";
        for (FuzzyNumber merit : merits) {
            returnString = returnString + " " + merit;
        }
        return returnString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Harmony harmony = (Harmony) o;

        if (harmonyMemory != null ? !harmonyMemory.equals(harmony.harmonyMemory) : harmony.harmonyMemory != null)
            return false;
        if (!Arrays.equals(merits, harmony.merits)) return false;
        if (!Arrays.equals(notes, harmony.notes)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = merits != null ? Arrays.hashCode(merits) : 0;
        result = 31 * result + (notes != null ? Arrays.hashCode(notes) : 0);
        result = 31 * result + (harmonyMemory != null ? harmonyMemory.hashCode() : 0);
        return result;
    }

    public int compareTo(Object o) {
        return harmonyMemory.getHarmonyComparator().compare(this, o);
    }
}
