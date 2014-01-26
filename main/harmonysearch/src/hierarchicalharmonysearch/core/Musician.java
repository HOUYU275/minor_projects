package hierarchicalharmonysearch.core;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 26/01/11
 * Time: 11:10
 * To change this template use File | Settings | File Templates.
 */
public class Musician {

    private ArrayList<Note> notes = new ArrayList<Note>();
    //private HarmonyMemory harmonyMemory;
    private Section section;
    private ValueRange valueRange;

    public Musician() {
    }

    public Musician(/*HarmonyMemory harmonyMemory, */Section section, ValueRange valueRange) {
        //this.harmonyMemory = harmonyMemory;
        this.section = section;
        this.valueRange = valueRange;
    }

    public void clearNotes() {
        this.notes.clear();
    }

    /*public HarmonyMemory getHarmonyMemory() {
        return harmonyMemory;
    }

    public void setHarmonyMemory(HarmonyMemory harmonyMemory) {
        this.harmonyMemory = harmonyMemory;
    }*/

    /*public ValueRange getNoteRange() {
        return noteRange;
    }

    public void setNoteRange(ValueRange valueRange) {
        this.noteRange = valueRange;
    }*/

    public boolean addNote(Note note) {
        return notes.add(note);
        /*if (notes.contains(note)) {
            System.out.println("contains");
            notes.remove(note);
            notes.add(note);
            return false;
        } else */
        /*if (notes.size() < section.getHarmonyMemory().getHMS()) {
            //System.out.println("Less than HMS");
            note.setMusician(this);
            notes.add(note);
            sortNotes();
            return true;
        } else if (note.compareTo(notes.get(0)) > 0) {
            //System.out.println("Else");
            notes.remove(0);
            note.setMusician(this);
            notes.add(note);
            sortNotes();
            return true;
        }
        return false;*/
    }

    /*public boolean add(Note note) {
        if (notes.contains(note)) {
            return false;
        } else {
            Collections.sort(notes);
            if (notes.get(0).compareTo(note) < 0) {
                notes.set(0, note);
            }
            return true;
        }
    }*/

    /*public void replaceNote(Note oldNote, Note newNote) {
        notes.add(newNote);
    }*/

    public void printNotes() {
        for (int i = 0; i < notes.size(); i++) {
            System.out.print(" " + notes.get(i));
        }
        System.out.print("\n");
    }

    public void removeNote(Note note) {
        notes.remove(note);
    }

    public void sortNotes() {
        Collections.sort(notes);
    }

    public double randomValue() {
        double value;
        if (valueRange.isContinuous()) {
            value = section.getHarmonyMemory().getRandom().nextDouble() *
                    (valueRange.getMax() - valueRange.getMin()) +
                    valueRange.getMin();
        } else {
            value = section.getHarmonyMemory().getRandom().nextInt((int) (valueRange.getMax() -
                    valueRange.getMin()) + 1) +
                    valueRange.getMin();
        }
        return value;
    }

    /*public double pickValue() {
        return (section.getHarmonyMemory().getRandom().nextDouble() > section.getHarmonyMemory().getHMCR()) ?
                randomValue() :
                pitchAdjust(notes.get(section.getHarmonyMemory().getRandom().nextInt(notes.size())));
    }*/

    public double calculateWeight() {
        double weight = 0d;
        for (Note note : notes) weight = weight + note.getMerits()[0];
        return weight / notes.size();
    }

    private Note pitchAdjust(Note note) {
        double value = 0;
        if (section.getHarmonyMemory().getRandom().nextDouble() < section.getHarmonyMemory().getPAR()) {
            value = (section.getHarmonyMemory().getRandom().nextDouble() > 0.5) ? note.getValue() + section.getHarmonyMemory().getBW() : note.getValue() - section.getHarmonyMemory().getBW();
            if (!valueRange.isContinuous()) value = Math.round(value);
            note.setValue(value);
        }/* else {
            value = note.getValue();
        }*/
        return note;
    }

    public void fill() {
        for (int i = 0; i < section.getHarmonyMemory().getHMS(); i++) {
            notes.add(randomNote());
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

    public Note pickNote() {
        return ((section.getHarmonyMemory().getRandom().nextDouble() > section.getHarmonyMemory().getHMCR()) || (notes.size() <= 0)) ?
                randomNote() :
                pitchAdjust(notes.get(section.getHarmonyMemory().getRandom().nextInt(notes.size())));
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public Note randomNote() {
        return new Note(randomValue(), this);
    }

    public ValueRange getValueRange() {
        return valueRange;
    }

    public void setValueRange(ValueRange valueRange) {
        this.valueRange = valueRange;
    }
}