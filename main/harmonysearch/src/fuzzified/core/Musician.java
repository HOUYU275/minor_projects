package fuzzified.core;

import java.util.ArrayList;

/**
 * User: rrd09, Date: 20/12/11, Time: 12:31
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
        if (harmonyMemory.fuzzified) {
            if (harmonyMemory.checkHMCR()) return random();
            else {
                Note note = fuzzifiedPick();
                return adjust(note);
            }
        } else {
            return harmonyMemory.checkHMCR() ?
                    random() :
                    adjust(notes.get(harmonyMemory.random.nextInt(notes.size())));
        }
    }

    public Note fuzzifiedPick() {
        double sum = 0;
        for (int i = 0; i < notes.size(); i++) {
            sum += notes.get(i).harmony.membership;
        }
        double randomPointer = harmonyMemory.random.nextDouble() * sum;
        sum = 0d;
        for (int i = 0; i < notes.size(); i++) {
            if ((sum + notes.get(i).harmony.membership) < randomPointer) sum += notes.get(i).harmony.membership;
            else {
                return notes.get(i);
            }
        }
        return null;
    }

    public Note fuzzifiedPickVerbose() {
        double sum = 0;
        for (int i = 0; i < notes.size(); i++) {
            sum += notes.get(i).harmony.membership;
        }
        double randomPointer = harmonyMemory.random.nextDouble() * sum;
        sum = 0d;
        for (int i = 0; i < notes.size(); i++) {
            if ((sum + notes.get(i).harmony.membership) < randomPointer) sum += notes.get(i).harmony.membership;
            else {
                System.out.println(i + "/" + notes.size());
                return notes.get(i);
            }
        }
        return null;
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
