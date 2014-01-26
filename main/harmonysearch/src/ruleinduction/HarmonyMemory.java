package ruleinduction;

import weka.core.Instances;

import java.util.Random;
import java.util.TreeSet;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 19-Mar-2010
 * Time: 15:44:38
 * To change this template use File | Settings | File Templates.
 */
public class HarmonyMemory extends TreeSet<Harmony> {

    protected Musician[] musicians;
    protected HarmonyComparator comparator;

    private ValueRange[] parameterRanges;
    //private ValueRange[] valueRanges;

    protected int HMS;
    protected double HMCR;
    protected double PAR;
    protected double BW;

    protected Random random;

    protected Instances instances;
    protected Integer[] features;

    protected double redundancy = 0;

    public HarmonyMemory() {
        super();
    }

    public HarmonyMemory(ValueRange[] parameterRanges, Instances instances, Integer[] features) {
        super();
        this.parameterRanges = parameterRanges;
        //this.valueRanges = valueRanges;
        this.instances = instances;
        this.features = features;
        this.comparator = new HarmonyComparator(this);
        this.random = new Random();
        initialise();
    }

    public Harmony best() {
        return this.last();
    }

    public Harmony worst() {
        return this.first();
    }

    private void initialiseParameters() {
        this.HMS = (int) parameterRanges[0].min;
        this.HMCR = parameterRanges[1].min;
        this.PAR = parameterRanges[2].min;
        this.BW = parameterRanges[3].max;
    }

    private void initialiseMusicians() {
        this.musicians = new Musician[instances.numInstances()];
        for (int i = 0; i < musicians.length; i++) {
            musicians[i] = new Musician(this, i);
        }
    }

    public void initialise() {
        initialiseParameters();
        initialiseMusicians();
        super.clear();
    }

    public Harmony newHarmony() {
        Harmony harmony = new Harmony(this);
        /*for (int i = 0; i < musicians.length; i++) {
            Note note = musicians[i].pick();
            harmony.setNote(i, note);
        }*/
        boolean[] pickEnabled = new boolean[musicians.length];
        for (int i = 0; i < pickEnabled.length; i++) {
            pickEnabled[i] = true;
        }
        for (int i = 0; i < musicians.length; i++) {
            if (pickEnabled[i]) {
                Note note = musicians[i].pick();
                //if ((randomNote.value != null) && (randomNote.lowerApproximation == comparator.globalLowerApproximation[i])) {
                if (note.value != null) {
                    /*for (int j = 0; j < i; j++) {
                        if (randomNote.equivalenceClasses[j] == 1) harmony.notes[j].value = null;
                    }*/
                    for (int j = i + 1; j < note.equivalenceClasses.length; j++) {
                        if (note.equivalenceClasses[j] == comparator.globalLowerApproximation[j])
                            pickEnabled[j] = false;
                    }
                }
                harmony.setNote(i, note);
            } else harmony.setNote(i, new Note(null, musicians[i]));
        }
        /*int start = random.nextInt(musicians.length);
        boolean forward = random.nextDouble() > 0.5;

        if (forward) {
            for (int i = start; i < musicians.length; i++) {
                Note note = musicians[i].pick();
                if (note.value != null) {
                    for (int j = 0; j < start; j++) {
                        if (note.equivalenceClasses[j] == 1) pickEnabled[j] = false;
                    }
                }
                harmony.setNote(i, note);
            }
            for (int i = 0; i < start; i++) {
                if (pickEnabled[i]) {
                    Note note = musicians[i].pick();
                    harmony.setNote(i, note);
                } else {
                    harmony.setNote(i, new Note(null, musicians[i]));
                }
            }
        } else {
            for (int i = 0; i < start; i++) {
                Note note = musicians[i].pick();
                if (note.value != null) {
                    for (int j = start; j < musicians.length; j++) {
                        if (note.equivalenceClasses[j] == 1) pickEnabled[j] = false;
                    }
                }
                harmony.setNote(i, note);
            }
            for (int i = start; i < musicians.length; i++) {
                if (pickEnabled[i]) {
                    Note note = musicians[i].pick();
                    harmony.setNote(i, note);
                } else {
                    harmony.setNote(i, new Note(null, musicians[i]));
                }
            }
        }*/
        /*for (int i = start; i < musicians.length; i++) {
if (pickEnabled[i]) {
    Note note = musicians[i].pick();
    //if ((randomNote.value != null) && (randomNote.lowerApproximation == comparator.globalLowerApproximation[i])) {
    if (note.value != null) {
        *//*for (int j = 0; j < i; j++) {
                        if (randomNote.equivalenceClasses[j] == 1) harmony.notes[j].value = null;
                    }*//*
                    for (int j = i + 1; j < note.equivalenceClasses.length; j++) {
                        if (note.equivalenceClasses[j] == 1) pickEnabled[j] = false;
                    }
                }
                harmony.setNote(i, note);
            } else harmony.setNote(i, new Note(null, musicians[i]));
        }*/
        comparator.evaluate(harmony);
        return harmony;
    }

    public Harmony randomHarmony() {
        Harmony harmony = new Harmony(this);
        boolean[] pickEnabled = new boolean[musicians.length];
        for (int i = 0; i < pickEnabled.length; i++) {
            pickEnabled[i] = true;
        }
        for (int i = 0; i < musicians.length; i++) {
            if (pickEnabled[i]) {
                Note randomNote = musicians[i].quickRandom();
                //if ((randomNote.value != null) && (randomNote.lowerApproximation == comparator.globalLowerApproximation[i])) {
                if (randomNote.value != null) {
                    /*for (int j = 0; j < i; j++) {
                        if (randomNote.equivalenceClasses[j] == 1) harmony.notes[j].value = null;
                    }*/
                    for (int j = i + 1; j < randomNote.equivalenceClasses.length; j++) {
                        if (randomNote.equivalenceClasses[j] == comparator.globalLowerApproximation[j])
                            pickEnabled[j] = false;
                    }
                }
                harmony.setNote(i, randomNote);
            } else harmony.setNote(i, new Note(null, musicians[i]));
        }
        /*while (comparator.checkConstraint(harmony) != 0) {
            for (int i = 0; i < musicians.length; i++) {
                harmony.setNote(i, musicians[i].random());
            }
        }*/
        comparator.evaluate(harmony);
        return harmony;
    }

    public boolean add(Harmony e) {
        if (contains(e)) {
            return false;
        } else if (size() < HMS) {
            for (int i = 0; i < e.getSize(); i++) musicians[i].add(e.getNote(i));
            super.add(e);
            return true;
        } else if (comparator.compare(e, worst()) > 0) {
            for (int i = 0; i < e.getSize(); i++) musicians[i].replace(worst().getNote(i), e.getNote(i));
            super.remove(worst());
            super.add(e);
            return true;
        }
        return false;
    }

    public boolean remove(Harmony e) {
        for (int i = 0; i < e.getSize(); i++) {
            musicians[i].remove(e.getNote(i));
        }
        super.remove(e);
        return true;
    }

    public void fill() {
        while (size() < HMS) add(randomHarmony());
    }

    public void iterate(int maxIteration) {
        int currentIteration = 0;
        while (currentIteration < maxIteration) {
            //System.out.println(currentIteration + " ...");
            add(newHarmony());
            currentIteration++;
            //statusDisplay(currentIteration, maxIteration);
        }
    }

    /*public void statusDisplay(int current, int max) {
        int step = max / 100;
        if (current%step == 0) System.out.print("|");
    }*/

    public void run(int maxIteration) {
        initialise();
        fill();
        iterate(maxIteration);
        System.gc();
    }

    public boolean checkHMCR() {
        return random.nextDouble() > HMCR;
    }

    public boolean checkPAR() {
        return random.nextDouble() < PAR;
    }

    public void report() {
        System.out.println("Best Harmony: " + best().toString());
    }

    public void reportFull() {
        for (Harmony harmony : this) System.out.println(harmony.toString() + " " + harmony.membership);
        System.out.println("Best Harmony: " + best().toString());
    }

    public Musician[] getMusicians() {
        return musicians;
    }

    public HarmonyComparator getComparator() {
        return comparator;
    }
}
