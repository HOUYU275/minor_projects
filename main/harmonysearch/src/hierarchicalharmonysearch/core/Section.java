package hierarchicalharmonysearch.core;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 26/01/11
 * Time: 11:20
 * To change this template use File | Settings | File Templates.
 */
public class Section {

    private Musician[] musicians;
    private double[] merits;
    private HarmonyMemory harmonyMemory;
    private ValueRange valueRange;

    public Section(HarmonyMemory harmonyMemory, ValueRange valueRange) {
        this.harmonyMemory = harmonyMemory;
        this.valueRange = valueRange;
        this.musicians = new Musician[1];
        for (int i = 0; i < musicians.length; i++) {
            musicians[i] = new Musician(
                    this, valueRange
                    /*new ValueRange(
                            (valueRange.getMin() * (musicians.length - i) + valueRange.getMax() * i) / musicians.length,
                            (valueRange.getMin() * (musicians.length - i - 1) + valueRange.getMax() * (i + 1)) / musicians.length,
                            valueRange.isContinuous()
                    )*/);
        }
        this.merits = new double[harmonyMemory.getHMS()];
    }

    public Note pickNote() {
        /*Note[] pickedNotes = new Note[musicians.length];
        for (int i = 0; i < musicians.length; i ++) {
            pickedNotes[i] = musicians[i].pickNote();
            //System.out.println(pickedNotes[i]);
        }
        return pickedNotes[harmonyMemory.getRandom().nextInt(musicians.length)];*/
        return musicians[harmonyMemory.getRandom().nextInt(musicians.length)].pickNote();

        /*double[] bestMerit = new double[harmonyMemory.getDimension()];
        Note bestNote = null;

        for (int i = 0; i < bestMerit.length; i ++) {
            bestMerit[i] = pickedNotes[0].getMerits()[i];
            bestNote = pickedNotes[0];
            for (int j = 1; j < pickedNotes.length; j ++) {
                if (pickedNotes[j].getMerits()[i] > bestMerit[i]) {
                    bestMerit[i] = pickedNotes[j].getMerits()[i];
                    bestNote = pickedNotes[j];
                }
            }
        }
        //System.out.println(bestNote);

        return bestNote;*/

        /*for (int i = 0; i < musicians.length; i ++) {
            value = value + musicians[i].pickValue()*//**//* * musicians[i].calculateWeight()*//**//*;
        }
        return valueRange.isContinuous() ? new Note(value / musicians.length, this) :
                new Note(Math.round(value / musicians.length), this);*/
    }

    public Note randomNote() {
        return musicians[harmonyMemory.getRandom().nextInt(musicians.length)].randomNote();
        //return new Note(value, this);
        /*for (int i = 0; i < musicians.length; i ++) {
            value = value + musicians[i].randomValue()*//* * musicians[i].calculateWeight()*//*;
        }*/
        /*return valueRange.isContinuous() ? new Note(value / musicians.length, this) :
                new Note(Math.round(value / musicians.length), this);*/
    }

    //TODO: Additions required
    public void addNote(Note note) {
        /*for (int i = 0; i < musicians.length; i ++) {
            musicians[harmonyMemory.getRandom().nextInt(musicians.length)].addNote(note);
        }*/
        note.getMusician().addNote(note);
        /*for (Musician musician : musicians) {
            musician.addNote(note);
        }*/
    }

    public void removeNote(Note note) {
        /*for (int i = 0; i < musicians.length; i ++) {
            musicians[harmonyMemory.getRandom().nextInt(musicians.length)].removeNote(note);
        }*/
        note.getMusician().removeNote(note);
        /*for (Musician musician : musicians) {
            musician.removeNote(note);
        }*/
    }

    public Musician[] getMusicians() {
        return musicians;
    }

    public void setMusicians(Musician[] musicians) {
        this.musicians = musicians;
    }

    public HarmonyMemory getHarmonyMemory() {
        return harmonyMemory;
    }

    public void setHarmonyMemory(HarmonyMemory harmonyMemory) {
        this.harmonyMemory = harmonyMemory;
    }

    public ValueRange getValueRange() {
        return valueRange;
    }

    public void setValueRange(ValueRange valueRange) {
        this.valueRange = valueRange;
    }

    public double[] getMerits() {
        return merits;
    }

    public void setMerits(double[] merits) {
        this.merits = merits;
    }

    public void initialise() {
        for (Musician musician : musicians) {
            musician.fill();
        }
    }

    public void clearNotes() {
        for (Musician musician : musicians) musician.clearNotes();
    }

    public void remove(Note note) {
        for (Musician musician : musicians) musician.removeNote(note);
    }

    public void printNotes() {
        for (Musician musician : musicians) {
            System.out.println("---->");
            musician.printNotes();
        }
    }
}
