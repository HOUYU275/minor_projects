package dynamic.core;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 10/01/13
 * Time: 10:59
 */
public class DynamicMusician {
    protected Vector<DynamicNote> notes = new Vector<>();
    protected DynamicHarmonyMemory memory;

    public DynamicMusician(DynamicHarmonyMemory memory) {
        this.memory = memory;
    }

    public void clear() {
        this.notes.clear();
    }

    public void add(DynamicNote note) {
        notes.add(note);
    }

    public void replace(DynamicNote old, DynamicNote newNote) {
        notes.remove(old);
        notes.add(newNote);
    }

    public void remove(DynamicNote note) {
        notes.remove(note);
    }

    public void removeAll(DynamicNote note) {
        while (notes.contains(note)) notes.remove(note);
    }

    public DynamicNote random() {
        return new DynamicNote(memory.range.random());
    }

    public DynamicNote pick() {
        return (memory.random.nextDouble() > memory.HMCR) ? random() : notes.get(memory.random.nextInt(notes.size()));
    }

    @Override
    public String toString() {
        String returnString = "";
        for (DynamicNote note : notes) returnString += (" " + note);
        return returnString;
    }
}
