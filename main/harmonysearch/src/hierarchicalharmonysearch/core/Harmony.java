package hierarchicalharmonysearch.core;

import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 26/01/11
 * Time: 12:54
 * To change this template use File | Settings | File Templates.
 */
public class Harmony {
    //private double merit;
    private double[] merits;
    //private int cardinality;
    private Note[] notes;// = new ArrayList<Double>();
    private HarmonyMemory harmonyMemory;

    public Harmony() {
    }

    public Harmony(HarmonyMemory harmonyMemory) {
        this.harmonyMemory = harmonyMemory;
        notes = new Note[harmonyMemory.getNumSections()];
        merits = new double[harmonyMemory.getDimension()];
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

    /*public Harmony(int size) {
        notes = new int[size];
    }*/

    public double[] getMerits() {
        return merits;
    }

    public void setMerits(double[] merits) {
        this.merits = merits;
    }

    /*public double getMerit() {
        return merit;
    }*/

    /*public void setMerit(double merit) {
        this.merit = merit;
    }*/

    public void setMerit(double merit) {
        this.merits[0] = merit;
        for (Note note : notes) note.setMerit(merit, 0);
    }

    public void setMerit(double merit, int index) {
        this.merits[index] = merit;
        //System.out.println(merit);
        for (Note note : notes) note.setMerit(merit, index);
    }

    public double getMerit(int index) {
        return this.merits[index];
    }

    public double getMerit() {
        return this.merits[0];
    }

    /*public int cardinality() {
        return cardinality;
    }

    public void setCardinality(int cardinality) {
        this.cardinality = cardinality;
    }*/

    /*public int[] getIntNotes() {
        //String a = "";
        int[] intNotes = new int[notes.length];
        for (int i = 0; i < notes.length; i++) {
            intNotes[i] = (int) Math.floor(notes[i]);
            //a = a + " " + intNotes[i];
        }
        //System.out.println(a);
        return intNotes;
    }*/

    public Note[] getNotes() {
        return notes;
    }

    public double[] getDoubleNotes() {
        double[] doubletNotes = new double[notes.length];
        for (int i = 0; i < notes.length; i++) {
            doubletNotes[i] = notes[i].getValue();
        }
        return doubletNotes;
    }

    public int[] getIntNotes() {
        int[] intNotes = new int[notes.length];
        for (int i = 0; i < notes.length; i++) {
            intNotes[i] = (int) Math.round(notes[i].getValue());
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
        //System.out.println(notes[index] + " for " + index);
        return notes[index];
    }

    public String toString() {
        String s = "";
        for (Note i : notes) {
            s = s + " " + i.getValue();
        }
        return s;
    }

    public String printMerits() {
        String returnString = "";
        for (double merit : merits) {
            returnString = returnString + " " + merit;
        }
        return returnString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Harmony harmony = (Harmony) o;

        if (!harmonyMemory.equals(harmony.harmonyMemory)) return false;
        if (!Arrays.equals(merits, harmony.merits)) return false;
        if (!Arrays.equals(notes, harmony.notes)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = merits != null ? Arrays.hashCode(merits) : 0;
        result = 31 * result + (notes != null ? Arrays.hashCode(notes) : 0);
        result = 31 * result + harmonyMemory.hashCode();
        return result;
    }
}
