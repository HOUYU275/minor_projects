package hierarchicalharmonysearch.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 26/01/11
 * Time: 12:51
 * To change this template use File | Settings | File Templates.
 */
public abstract class HarmonyMemory extends ArrayList<Harmony> {

    private HarmonyComparator harmonyComparator;
    private Random random;

    private ValueRange[] parameterRanges;
    private int HMS;
    private double HMCR;
    private double PAR;
    private double BW;
    private int dimension;
    private Section[] sections;

    public HarmonyMemory() {
        super();
    }

    public HarmonyMemory(ValueRange[] parameterRanges, ValueRange[] noteRanges, HarmonyComparator harmonyComparator, Random random) throws Exception {
        super();
        this.harmonyComparator = harmonyComparator;
        this.dimension = harmonyComparator.getDimension();
        this.random = random;
        this.parameterRanges = parameterRanges;
        this.HMS = (int) parameterRanges[0].getMin();
        this.HMCR = parameterRanges[1].getMin();
        this.PAR = parameterRanges[2].getMin();
        this.BW = parameterRanges[3].getMax();
        this.sections = new Section[noteRanges.length];
        //this.musicians = new Musician[noteRanges.length];
        for (int i = 0; i < sections.length; i++) {
            sections[i] = new Section(this, noteRanges[i]);
        }
    }

    public static ValueRange[] createParameterRanges(int minHMS, int maxHMS,
                                                     double minHMCR, double maxHMCR,
                                                     double minPAR, double maxPAR,
                                                     double minBW, double maxBW) {
        ValueRange[] parameterRanges = new ValueRange[4];
        parameterRanges[0] = new ValueRange(minHMS, maxHMS, false);
        parameterRanges[1] = new ValueRange(minHMCR, maxHMCR, true);
        parameterRanges[2] = new ValueRange(minPAR, maxPAR, true);
        parameterRanges[3] = new ValueRange(minBW, maxBW, true);
        return parameterRanges;
    }

    public static ValueRange[] createParameterRanges(int maxHMS,
                                                     double minHMCR,
                                                     double minPAR,
                                                     double maxBW) {
        ValueRange[] parameterRanges = new ValueRange[4];
        parameterRanges[0] = new ValueRange(maxHMS, maxHMS, false);
        parameterRanges[1] = new ValueRange(minHMCR, 1, true);
        parameterRanges[2] = new ValueRange(minPAR, 1, true);
        parameterRanges[3] = new ValueRange(0.0001, maxBW, true);
        return parameterRanges;
    }

    public static ValueRange[] createParameterRanges(int maxHMS,
                                                     double minHMCR) {
        ValueRange[] parameterRanges = new ValueRange[4];
        parameterRanges[0] = new ValueRange(maxHMS / 3, maxHMS, false);
        parameterRanges[1] = new ValueRange(minHMCR, 1, true);
        parameterRanges[2] = new ValueRange(0, 0, true);
        parameterRanges[3] = new ValueRange(Double.MIN_VALUE, Double.MIN_VALUE, true);
        return parameterRanges;
    }

    @Override
    public void clear() {
        for (int i = 0; i < sections.length; i++) {
            sections[i].clearNotes();
        }
        super.clear();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void resetParameters() {
        this.HMS = (int) parameterRanges[0].getMin();
        this.HMCR = parameterRanges[1].getMin();
        this.PAR = parameterRanges[2].getMin();
        this.BW = parameterRanges[3].getMax();
    }

    private void calculateParameters(int currentIteration, int maxIteration) {
        this.HMS = (int) (parameterRanges[0].getMin() + (parameterRanges[0].getMax() - parameterRanges[0].getMin()) / maxIteration * currentIteration);
        this.HMCR = parameterRanges[1].getMin() + (parameterRanges[1].getMax() - parameterRanges[1].getMin()) / maxIteration * currentIteration;
        this.PAR = parameterRanges[2].getMin() + (parameterRanges[2].getMax() - parameterRanges[2].getMin()) / maxIteration * currentIteration;
        this.BW = parameterRanges[3].getMax() * Math.exp(Math.log(parameterRanges[3].getMin() / parameterRanges[3].getMax()) / maxIteration * currentIteration);
    }

    public HarmonyComparator getHarmonyComparator() {
        return harmonyComparator;
    }

    public ValueRange[] getParameterRanges() {
        return parameterRanges;
    }

    public void setParameterRanges(ValueRange[] parameterRanges) {
        this.parameterRanges = parameterRanges;
    }

    public void setHarmonyComparator(HarmonyComparator harmonyComparator) {
        this.harmonyComparator = harmonyComparator;
    }

    public Harmony best() {
        return this.get(this.size() - 1);
    }

    public void fill() throws Exception {
        for (Section section : sections) {
            section.initialise();
            //section.printNotes();
        }

        while (this.size() < HMS) {
            //System.out.println(this.size());
            /*Harmony randomHarmony = randomHarmony();
            while (harmonyComparator.checkConstraint(randomHarmony) != 0) {
                randomHarmony = randomHarmony();
            }
            this.evaluateAndAdd(randomHarmony);*/
            Harmony newHarmony = newHarmony();
            while (harmonyComparator.checkConstraint(newHarmony) != 0) {
                newHarmony = newHarmony();
            }
            this.evaluateAndAdd(newHarmony);
        }
        printHarmonies();
        /*System.out.println("-----------------------");
        for (Section section : sections) {
            //section.initialise();
            section.printNotes();
        }*/
    }

    public void iterate(int maxIteration) throws Exception {
        int currentIteration = 0;
        while (currentIteration <= maxIteration) {
            calculateParameters(currentIteration, maxIteration);
            Harmony newHarmony = newHarmony();
            while (harmonyComparator.checkConstraint(newHarmony) != 0) {
                newHarmony = newHarmony();
            }
            this.evaluateAndAdd(newHarmony);
            currentIteration = currentIteration + 1;
        }
        printHarmonies();
        //System.out.println("-----------------------");
        /*for (Section section : sections) {
            //section.initialise();
            section.printNotes();
        }*/
    }

    public void sort() {
        Collections.sort(this, harmonyComparator);
    }

    public void resize(int newNumHarmonies) throws Exception {

        if (newNumHarmonies < HMS) {
            System.out.println("Resize " + HMS + " to " + newNumHarmonies);
            HMS = newNumHarmonies;
            while (this.size() > HMS) {
                this.remove(0);
            }
        } else if (newNumHarmonies > HMS) {
            System.out.println("Resize " + HMS + " to " + newNumHarmonies);
            HMS = newNumHarmonies;
        }

    }

    public double getPAR() {
        return PAR;
    }

    public void setPAR(double PAR) {
        this.PAR = PAR;
    }

    public double getHMCR() {
        return HMCR;
    }

    public void setHMCR(double HMCR) {
        this.HMCR = HMCR;
    }

    public double getBW() {
        return BW;
    }

    public void setBW(double BW) {
        this.BW = BW;
    }

    public void printHarmonies() {
        for (Harmony harmony : this) {
            System.out.print("(");
            for (double merit : harmony.getMerits()) {
                System.out.print(" " + merit);
            }
            try {
                System.out.print(" ) + [ " + this.harmonyComparator.checkConstraint(harmony) + " ]" + harmony.toString());
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            System.out.println();
        }
    }

    public int getNumSections() {
        return sections.length;
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public Section[] getSections() {
        return sections;
    }

    public void setSections(Section[] sections) {
        this.sections = sections;
    }

    /*public Musician[] getMusicians() {
        return musicians;
    }

    public void setMusicians(Musician[] musicians) {
        this.musicians = musicians;
    }*/

    public int getHMS() {
        return HMS;
    }

    public void setHMS(int HMS) {
        this.HMS = HMS;
    }

    //TODO: this

    public boolean evaluateAndAdd(Harmony harmony) throws Exception {
        harmonyComparator.evaluate(harmony);
        /*System.out.println(harmony.toString());
        System.out.println(harmony.printMerits());*/
        //addHarmony(harmony);
        return add(harmony);
    }

    //TODO: need the multi-D version now!

    public boolean add(Harmony e) {
        if (contains(e)) {
            return false;
        } else if (super.size() < HMS) {
            addHarmony(e);
            super.add(e);
            sort();
            return true;
        } else if (harmonyComparator.compare(e, this.get(0)) > 0) {
            removeHarmony(this.get(0));
            addHarmony(e);
            super.remove(0);
            super.add(e);
            sort();
            return true;
        }
        return false;
    }

    public void addHarmony(Harmony harmony) {
        for (Note note : harmony.getNotes()) {
            /*for (int i = 0; i < harmony.getMerits().length; i++) {
                note.setMerit(harmony.getMerit(i), i);
            }*/
            note.getSection().addNote(note);
        }
    }

    private void removeHarmony(Harmony harmony) {
        for (Note note : harmony.getNotes()) {
            note.getSection().removeNote(note);
        }
    }

    public boolean remove(Harmony e) {
        if (!super.contains(e)) return false;
        for (int i = 0; i < e.getSize(); i++) {
            sections[i].remove(e.getNote(i));
            super.remove(e);
            sort();
            return true;
        }
        return false;
    }

    public Harmony newHarmony() {
        Harmony harmony = new Harmony(this);
        for (int i = 0; i < sections.length; i++) {
            harmony.setNote(i, sections[i].pickNote());
        }
        return harmony;
    }

    public Harmony randomHarmony() {
        Harmony harmony = new Harmony(this);
        for (int i = 0; i < sections.length; i++) {
            harmony.setNote(i, sections[i].randomNote());
        }
        return harmony;
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public void reset() {
        for (int i = 0; i < sections.length; i++) {
            sections[i].clearNotes();
        }
        this.HMS = (int) parameterRanges[0].getMin();
        this.HMCR = parameterRanges[1].getMin();
        this.PAR = parameterRanges[2].getMin();
        this.BW = parameterRanges[3].getMax();
        super.clear();
    }

}
