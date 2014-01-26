package original.core;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 20/12/11
 * Time: 12:31
 * To change this template use File | Settings | File Templates.
 */
public class Musician {

    protected ArrayList<Note> notes = new ArrayList();
    protected HarmonyMemory harmonyMemory;
    protected ValueRange valueRange;

    public Musician() {
    }

    public Musician(HarmonyMemory harmonyMemory, ValueRange valueRange) {
        this.harmonyMemory = harmonyMemory;
        this.valueRange = valueRange;
    }

    public ArrayList<Note> getNotes() {
        return notes;
    }

    public void add(Note note) {
        notes.add(note);
    }

    public void replace(Note oldNote, Note newNote) {
        notes.remove(oldNote);
        notes.add(newNote);
    }

    public void remove(Note index) {
        notes.remove(index);
    }

    public Note random() {
        double value;
        if (valueRange.continuous) {
            value = harmonyMemory.random.nextDouble() * (valueRange.max - valueRange.min) + valueRange.min;
        } else {
            value = harmonyMemory.random.nextInt((int) (valueRange.max - valueRange.min) + 1) + valueRange.min;
        }
        return new Note(value, this);
    }

    public Note pick() {
        return harmonyMemory.checkHMCR() ?
                random() :
                adjust(notes.get(harmonyMemory.random.nextInt(notes.size())));
    }

    private Note adjust(Note note) {
        double value;
        if (harmonyMemory.checkPAR()) {
            value = (harmonyMemory.random.nextDouble() > 0.5) ?
                    note.getValue() + harmonyMemory.BW * (valueRange.max - valueRange.min) :
                    note.getValue() - harmonyMemory.BW * (valueRange.max - valueRange.min);
            if (!valueRange.continuous) value = Math.round(value);
        } else {
            value = note.getValue();
        }
        return new Note(value, this);
    }
}
