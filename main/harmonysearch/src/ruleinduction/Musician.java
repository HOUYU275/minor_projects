package ruleinduction;

import java.util.*;

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
    //protected ValueRange valueRange;

    protected int instance;

    public Musician() {
    }

    public Musician(HarmonyMemory harmonyMemory, int instance) {
        this.harmonyMemory = harmonyMemory;
        //this.valueRange = valueRange;
        this.instance = instance;
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

    public Note quickRandom() {
        if (harmonyMemory.random.nextDouble() < harmonyMemory.redundancy) return new Note(null, this);
        //int size = harmonyMemory.features.length;// * 2 / 3 + harmonyMemory.random.nextInt(harmonyMemory.features.length / 3);
        ArrayList<Integer> featuresArrayList = new ArrayList(Arrays.asList(harmonyMemory.features));
        Collections.shuffle(featuresArrayList);
        BitSet value = new BitSet(harmonyMemory.instances.numAttributes());
        Note note = new Note(value, this);
        while ((!featuresArrayList.isEmpty()) && (note.lowerApproximation != harmonyMemory.comparator.globalLowerApproximation[instance])) {
            value.set(featuresArrayList.get(0));
            featuresArrayList.remove(0);
            note = new Note(value, this);
        }
        /*List<Integer> draft = featuresArrayList.subList(0, size);
        BitSet value = new BitSet(harmonyMemory.instances.numAttributes());
        for (int i = 0; i < draft.size(); i++) {
            value.set(draft.get(i));
        }
        Note note = new Note(value, this);*/
        return note;
    }

    public Note random() {
        if (harmonyMemory.random.nextDouble() < harmonyMemory.redundancy) return new Note(null, this);
        int size = harmonyMemory.random.nextInt(harmonyMemory.features.length) + 1;
        ArrayList<Integer> featuresArrayList = new ArrayList(Arrays.asList(harmonyMemory.features));
        Collections.shuffle(featuresArrayList);
        List<Integer> draft = featuresArrayList.subList(0, size);
        BitSet value = new BitSet(harmonyMemory.instances.numAttributes());
        for (int i = 0; i < draft.size(); i++) {
            value.set(draft.get(i));
        }
        Note note = new Note(value, this);
        int counter = 0;
        while (!harmonyMemory.comparator.checkConstraint(note, instance)) {
            if (counter > 10) {
                value = new BitSet(harmonyMemory.instances.numAttributes());
                for (int feature : harmonyMemory.features) value.set(feature);
                note = new Note(value, this);
            } else {
                size = harmonyMemory.random.nextInt(harmonyMemory.features.length) + 1;
                featuresArrayList = new ArrayList(Arrays.asList(harmonyMemory.features));
                Collections.shuffle(featuresArrayList);
                draft = featuresArrayList.subList(0, size);
                value = new BitSet(harmonyMemory.instances.numAttributes());
                for (int i = 0; i < draft.size(); i++) {
                    value.set(draft.get(i));
                }
                note = new Note(value, this);
                counter++;
            }
        }
        return note;
    }

    public Note pick() {
        return harmonyMemory.checkHMCR() ?
                quickRandom() :
                adjust(notes.get(harmonyMemory.random.nextInt(notes.size())));
    }

    private Note adjust(Note note) {
        BitSet value = note.value;
        if (value == null) return new Note(value, this);
        if (harmonyMemory.checkPAR()) {
            Integer[] integerFeatures = note.toIntegerArray();
            //int size = (int)harmonyMemory.BW * harmonyMemory.features.length;
            //int count = 0;
            //ArrayList<Integer> featuresArrayList = new ArrayList(Arrays.asList(integerFeatures));
            //Collections.shuffle(featuresArrayList);
            //for (int i = 0; i < featuresArrayList.size(); i++) {
            for (int i = 0; i < integerFeatures.length; i++) {
                //if (size == count) return new Note(value, this);
                value.clear(integerFeatures[i]);
                note = new Note(value, this);
                if (note.lowerApproximation != harmonyMemory.comparator.globalLowerApproximation[instance]) {
                    value.set(integerFeatures[i]);
                }
            }
            note = new Note(value, this);
            //System.out.println(instance + " : " + note.lowerApproximation + " vs " + harmonyMemory.comparator.globalLowerApproximation[instance]);
            /*int flipBit = harmonyMemory.random.nextInt(harmonyMemory.features.length);
            value.flip(flipBit);
            if (value.cardinality() == 0) value = null;*/
            /*int distance = (int) Math.round(harmonyMemory.BW * harmonyMemory.features.length);
            if (distance == 0) distance = 1;
            //Integer[] integerFeatures = note.toIntegerArray();
            int flipBit;
            for (int i = 0; i < distance; i++) {
                flipBit = harmonyMemory.random.nextInt(harmonyMemory.features.length);
                value.flip(flipBit);
            }
            if (value.cardinality() == 0) value = null;*/
        } else {
            value = note.value;
        }
        return new Note(value, this);
    }
}
