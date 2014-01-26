package fuzzified.core;

import java.util.Arrays;

/**
 * User: rrd09, Date: 20/12/11, Time: 12:32
 */
public class Harmony implements Comparable {
    protected double[] merits;
    protected Note[] notes;
    protected HarmonyMemory harmonyMemory;
    protected double membership;

    public Harmony() {
    }

    public Harmony(HarmonyMemory harmonyMemory) {
        this.harmonyMemory = harmonyMemory;
        notes = new Note[harmonyMemory.musicians.length];
        merits = new double[harmonyMemory.comparator.dimension];
    }

    public HarmonyMemory getHarmonyMemory() {
        return harmonyMemory;
    }

    public int getSize() {
        return notes.length;
    }

    public double[] getMerits() {
        return merits;
    }

    public void setMerit(double merit) {
        this.merits[0] = merit;
        for (Note note : notes) note.setMerit(merit, 0);
    }

    public void setMerit(double merit, int index) {
        this.merits[index] = merit;
        for (Note note : notes) note.setMerit(merit, index);
    }

    public double getMerit(int index) {
        return this.merits[index];
    }

    public double getMerit() {
        return this.merits[0];
    }

    public Note[] getNotes() {
        return notes;
    }

    public void setNotes(Note[] notes) {
        this.notes = notes;
    }

    public void setNote(int index, Note note) {
        notes[index] = note;
        note.harmony = this;
    }

    public Note getNote(int index) {
        return notes[index];
    }

    public String toString() {
        String s = "";
        for (Note note : notes) s += " " + note.toString();
        s += " |";
        for (double merit : merits) s += " " + String.valueOf(merit).substring(0, 10);
        return s.trim();
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

    @Override
    public int compareTo(Object o) {
        return harmonyMemory.comparator.compare(this, o);
    }
}