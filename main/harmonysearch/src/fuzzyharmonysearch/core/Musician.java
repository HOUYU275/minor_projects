package fuzzyharmonysearch.core;

import fuzzy.FuzzyNumber;

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

    public void addNote(Note note) {
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
        //if (!notes.remove(oldNote)) removeClosest(oldNote);
        notes.remove(oldNote);
        /*if ((bestNote == null) || (newNote.getMerits()[0] > bestNote.getMerits()[0])) {
            bestNote = newNote;
        }*/
        notes.add(newNote);
    }

    /*public void removeClosest(Note oldNote) {
        int closestIndex = 0;
        double closestDistance = Double.MAX_VALUE;
        for (int i = 0; i < notes.size(); i++) {
            if (Math.abs(closestDistance) > Math.abs(notes.get(i).getValue().getRepresentativeValue() - oldNote.getValue().getRepresentativeValue())) {
                closestDistance = Math.abs(notes.get(i).getValue().getRepresentativeValue() - oldNote.getValue().getRepresentativeValue());
                closestIndex = i;
            }
        }
        notes.remove(closestIndex);
    }*/

    public void printNotes() {
        for (int i = 0; i < notes.size(); i++) {
            System.out.print(" " + notes.get(i));
        }
        System.out.print("\n");
    }

    public void removeNote(Note note) {
        notes.remove(note);
        /*notes[index] = (notes[index] <= 0) ? 0 : (notes[index] - 1);
        cardinality = (cardinality <= 0) ? 0 : cardinality - 1;*/
    }

    public void sortNotes() {
        Collections.sort(notes);
    }

    public Note randomNote(Harmony harmony) {
        double value;
        if (noteRange.isContinuous()) {
            value = harmonyMemory.getRandom().nextDouble() * noteRange.getSpread() + noteRange.getMin();
        } else {
            value = harmonyMemory.getRandom().nextInt((int) (noteRange.getSpread()) + 1) + noteRange.getMin();
        }
        return new Note(new FuzzyNumber(value - noteRange.getSpread() * 0.01, value, value + noteRange.getSpread() * 0.01), this, harmony);
        //return new Note(new FuzzyNumber(value, value, value), this);
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

    public Note pickNote(Harmony harmony) {
        if (harmonyMemory.testHMCR()) {
            return randomNote(harmony);
        } else {
            return pitchAdjust(randomPick(harmony));
        }
    }

    public Note randomPick(Harmony harmony) {
        return this.notes.get(harmonyMemory.getRandom().nextInt(this.notes.size()));
    }

    public Note weightedPick(Harmony harmony) {
        double totalWeight = 0d;
        double currentWeight = 0d;
        double randomNumber = harmonyMemory.getRandom().nextDouble();
        for (Note note : notes) {
            if (note.getQuality() > 0.25) {
                totalWeight += note.getQuality();
            }
        }
        for (Note note : notes) {
            if (note.getQuality() > 0.25) {
                currentWeight += note.getQuality();
            }

            if ((currentWeight / totalWeight) > randomNumber) try {
                return note.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();  //TODO: Automatically Generated Catch Statement
            }
        }
        return randomNote(harmony);
    }

    private Note pitchAdjust(Note note) {
        double value;
        if (harmonyMemory.testPAR()) {
            note.getValue().setCentroid(note.getValue().getLeftShoulder() +
                    harmonyMemory.getRandom().nextDouble() *
                            (note.getValue().getRightShoulder() - note.getValue().getLeftShoulder()));
            return note;
            /*value = (harmonyMemory.getRandom().nextDouble() > 0.5) ?
                    note.getValue().getRepresentativeValue() + harmonyMemory.getBW() :
                    note.getValue().getRepresentativeValue() - harmonyMemory.getBW();
            if (!noteRange.isContinuous()) value = Math.round(value);*/
        } else {
            return note;

        }
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

    /*public void fillNoteDomain() {
        while (notes.size() < harmonyMemory.getHMS()) {
            notes.add(randomNote());
        }
    }*/
}