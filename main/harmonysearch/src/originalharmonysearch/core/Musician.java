package originalharmonysearch.core;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 19-Mar-2010
 * Time: 13:24:15
 * To change this template use File | Settings | File Templates.
 */
public class Musician {

    //private int[] notes;
    //private int cardinality;
    private ArrayList<Note> notes = new ArrayList<Note>();
    //private ArrayList<Double> notes = new ArrayList<Double>();
    private HarmonyMemory harmonyMemory;
    private ValueRange noteRange;
    private Note bestNote;

    public Musician() {
    }

    /*public Musician(int numAttributes) {
        notes = new int[numAttributes];
        cardinality = 0;
        //System.out.println("New Musician Size = " + size);
    }*/

    public Musician(HarmonyMemory harmonyMemory, ValueRange noteRange) {
        this.harmonyMemory = harmonyMemory;
        this.noteRange = noteRange;
        //notes = new int[harmonyMemory.getNumNotes() + 1];
        //cardinality = 0;
    }

    public void clearNotes() {
        this.notes.clear();
    }

    public HarmonyMemory getHarmonyMemory() {
        return harmonyMemory;
    }

    public void setHarmonyMemory(HarmonyMemory harmonyMemory) {
        this.harmonyMemory = harmonyMemory;
    }

    public ValueRange getNoteRange() {
        return noteRange;
    }

    public void setNoteRange(ValueRange valueRange) {
        this.noteRange = valueRange;
    }

    /*public void addNote(Double index) {
        //System.out.println("Size = " + notes.length + " - " + notes[index]);
        notes.add(index);
    }*/

    public void addNote(Note note) {
        if ((bestNote == null) || (note.getMerits()[0] > bestNote.getMerits()[0])) {
            bestNote = note;
        }
        notes.add(note);
    }

    /*public void replaceNote(Double oldValue, Double newValue) {
        *//*System.out.println("B");
        printNotes();*//*
        notes.remove(oldValue);
        notes.add(newValue);
        //spread(newValue);
        *//*System.out.println("A");
        printNotes();*//*
        *//*notes[oldValue] = (notes[oldValue] <= 0) ? 0 : (notes[oldValue] - 1);
        notes[newValue] = notes[newValue] + 1;*//*
    }*/

    public void replaceNote(Note oldNote, Note newNote) {
        if (!notes.remove(oldNote)) removeClosest(oldNote);
        if ((bestNote == null) || (newNote.getMerits()[0] > bestNote.getMerits()[0])) {
            bestNote = newNote;
        }
        notes.add(newNote);
        if (this.harmonyMemory.isSpread()) spread(newNote);
        if (this.harmonyMemory.isInfluence()) influence();
    }

    public void removeClosest(Note oldNote) {
        int closestIndex = 0;
        double closestDistance = Double.MAX_VALUE;
        for (int i = 0; i < notes.size(); i++) {
            if (Math.abs(closestDistance) > Math.abs(notes.get(i).getValue() - oldNote.getValue())) {
                closestDistance = Math.abs(notes.get(i).getValue() - oldNote.getValue());
                closestIndex = i;
            }
        }
        notes.remove(closestIndex);
    }

    /*public void spread(double value) {
        int cap = harmonyMemory.getRandom().nextInt(notes.size());
        for (int i = 0; i < cap; i++) {
            if (noteRange.isContinuous()) {
                int index = harmonyMemory.getRandom().nextInt(notes.size());
                double originalValue = notes.get(index);
                double newValue = harmonyMemory.getRandom().nextDouble() * (value - originalValue) + originalValue;
                notes.set(index, newValue);
            } else {
                int index = harmonyMemory.getRandom().nextInt(notes.size());
                double originalValue = notes.get(index);
                double newValue = harmonyMemory.getRandom().nextDouble() * (value - originalValue) + originalValue;
                notes.set(index, newValue);
            }
        }
    }*/

    public void influence() {
        for (int i = 0; i < notes.size(); i++) {
            double noteValue = notes.get(i).getValue();
            double newValue = harmonyMemory.getRandom().nextDouble() * 0.01 * (bestNote.getValue() - noteValue) + noteValue;
            notes.get(i).setValue(newValue);
            if (!noteRange.isContinuous()) notes.get(i).setValue(Math.round(notes.get(i).getValue()));
        }
    }

    public void spread(Note note) {
        for (int i = 0; i < notes.size(); i++) {
            if (note.getMerits()[0] > notes.get(i).getMerits()[0]) {
                double newValue = notes.get(i).getValue() + harmonyMemory.getRandom().nextDouble() * 0.1 * (note.getValue() - notes.get(i).getValue());
                notes.get(i).setValue(newValue);
                if (!noteRange.isContinuous()) notes.get(i).setValue(Math.round(notes.get(i).getValue()));
            }
        }
    }

    public void printNotes() {
        for (int i = 0; i < notes.size(); i++) {
            System.out.print(" " + notes.get(i));
        }
        System.out.print("\n");
    }

    public void removeNote(Note index) {
        notes.remove(index);
        /*notes[index] = (notes[index] <= 0) ? 0 : (notes[index] - 1);
        cardinality = (cardinality <= 0) ? 0 : cardinality - 1;*/
    }

    public void sortNotes() {
        Collections.sort(notes);
    }

    public Note randomNote() {
        double value;
        if (noteRange.isContinuous()) {
            value = harmonyMemory.getRandom().nextDouble() * (noteRange.getMax() - noteRange.getMin()) + noteRange.getMin();
        } else {
            value = harmonyMemory.getRandom().nextInt((int) (noteRange.getMax() - noteRange.getMin()) + 1) + noteRange.getMin();
        }
        return new Note(value, this);
    }

    public Note getBestNote() {
        return bestNote;
    }

    public void setBestNote(Note bestNote) {
        this.bestNote = bestNote;
    }
/*private double continuousPick() {
        Collections.sort(notes);

        double randomDouble = harmonyMemory.getRandom().nextDouble();
        //double randomScaledDouble = randomDouble * (notes.size() - 1);
        double randomScaledDouble = randomDouble * (notes.size() + 1);
        double randomFlooredDouble = Math.floor(randomScaledDouble);
        int randomFlooredInt = (int) Math.round(randomFlooredDouble);

        double floorValue, ceilValue, returnValue;

        if (randomFlooredInt == notes.size() - 1) {
            floorValue = noteRange.getMin();
            ceilValue = notes.get(0);
        } else if (randomFlooredInt == notes.size()) {
            floorValue = notes.get(notes.size() - 1);
            ceilValue = noteRange.getMax();
        } else {
            floorValue = notes.get(randomFlooredInt);
            ceilValue = notes.get(randomFlooredInt + 1);
        }

        returnValue = (randomScaledDouble - randomFlooredDouble) * (ceilValue - floorValue) + floorValue;
        //System.out.println("randomDouble = " + randomDouble + "randomFlooredDouble = " + randomFlooredDouble + "returnValue " + returnValue);
        return returnValue;
    }*/

    /*private void discretePick() {
        *//*double randomDouble = harmonyMemory.getRandom().nextDouble();
                //double randomScaledDouble = randomDouble * (notes.size() - 1);
                double randomScaledDouble = randomDouble * (notes.size() + 1);
                double randomFlooredDouble = Math.floor(randomScaledDouble);
                int randomFlooredInt = (int) Math.round(randomFlooredDouble);

                double floorValue, ceilValue, returnValue;

                if (randomFlooredInt == notes.size() - 1) {
                    floorValue = noteRange.getMin();
                    ceilValue = notes.get(0);
                } else if (randomFlooredInt == notes.size()) {
                    floorValue = notes.get(notes.size() - 1);
                    ceilValue = noteRange.getMax();
                } else {
                    floorValue = notes.get(randomFlooredInt);
                    ceilValue = notes.get(randomFlooredInt + 1);
                }

                returnValue = (randomScaledDouble - randomFlooredDouble) * (ceilValue - floorValue) + floorValue;
                return Math.round(returnValue) + 0d;*//*
    }*/

    public Note pickNote() {
        return (harmonyMemory.getRandom().nextDouble() > harmonyMemory.getHMCR()) ?
                randomNote() :
                pitchAdjust(notes.get(harmonyMemory.getRandom().nextInt(notes.size())));
    }

    private Note pitchAdjust(Note note) {
        double value;
        if (harmonyMemory.getRandom().nextDouble() < harmonyMemory.getPAR()) {
            value = (harmonyMemory.getRandom().nextDouble() > 0.5) ? note.getValue() + harmonyMemory.getBW() : note.getValue() - harmonyMemory.getBW();
            if (!noteRange.isContinuous()) value = Math.round(value);
        } else {
            value = note.getValue();
        }
        return new Note(value, this);
    }

    public ArrayList getNotes() {
        return notes;
    }

    public void setNotes(ArrayList notes) {
        this.notes = notes;
    }

    public int getCardinality() {
        return notes.size();
    }

}