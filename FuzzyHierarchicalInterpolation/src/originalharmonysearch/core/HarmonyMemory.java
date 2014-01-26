package originalharmonysearch.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 19-Mar-2010
 * Time: 15:44:38
 * To change this template use File | Settings | File Templates.
 */
public abstract class HarmonyMemory extends ArrayList<Harmony> {
    //private HarmonyEvaluator[] harmonyEvaluators;
    //private HarmonyComparator harmonyComparator;
    private HarmonyComparator harmonyComparator;
    private Random random;

    private ValueRange[] parameterRanges;

    private int parameterMode;
    //parameterRanges 0: HMS, 1: HMCR, 2: PAR, 3: BW
    private int HMS;
    private double HMCR;
    private double PAR;
    private double BW;
    private int dimension;
    private boolean spread = false;
    private boolean influence = false;
    //private ValueRange[] noteRanges;
    //private int numMusicians;
    //private int numNotes;
    private Musician[] musicians;

    public HarmonyMemory() {
        super();
    }

    public HarmonyMemory(ValueRange[] parameterRanges, ValueRange[] noteRanges, HarmonyComparator harmonyComparator, Random random) throws Exception {
        super();
        this.harmonyComparator = harmonyComparator;
        this.dimension = harmonyComparator.getDimension();
        //this.harmonyComparator.setDimension(dimension);
        this.random = random;
        this.parameterRanges = parameterRanges;
        this.HMS = (int) parameterRanges[0].getMin();
        this.HMCR = parameterRanges[1].getMin();
        this.PAR = parameterRanges[2].getMin();
        this.BW = parameterRanges[3].getMax();
        this.musicians = new Musician[noteRanges.length];
        for (int i = 0; i < musicians.length; i++) {
            musicians[i] = new Musician(this, noteRanges[i]);
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

    public static ValueRange[] createParameterRangesFixed(int HMS,
                                                          double HMCR) {
        ValueRange[] parameterRanges = new ValueRange[4];
        parameterRanges[0] = new ValueRange(HMS, HMS, false);
        parameterRanges[1] = new ValueRange(HMCR, HMCR, true);
        parameterRanges[2] = new ValueRange(0, 0, true);
        parameterRanges[3] = new ValueRange(Double.MIN_VALUE, Double.MIN_VALUE, true);
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
        for (int i = 0; i < musicians.length; i++) {
            musicians[i].clearNotes();
        }
        super.clear();    //To change body of overridden methods use File | Settings | File Templates.
    }

    //public HarmonyMemory(int dimensions, int HMS, /*int numMusicians, *//*int numNotes, */ValueRange[] noteRanges, double HMCR, Random random) throws Exception {
    /*public HarmonyMemory(int HMS, ValueRange[] noteRanges, HarmonyComparator harmonyComparator, double HMCR, Random random) throws Exception {
        super();
        //this.harmonyComparator = new HarmonyComparator();
        //this.harmonyEvaluators = new HarmonyEvaluator[dimensions];
        //this.harmonyEvaluators = new HarmonyEvaluator[dimensions];
        this.harmonyComparator = harmonyComparator;
        this.HMS = HMS;
        //this.numMusicians = numMusicians;
        this.HMCR = HMCR;
        //this.numNotes = numNotes;
        this.random = random;
        musicians = new Musician[noteRanges.length];
        for (int i = 0; i < musicians.length; i++) {
            musicians[i] = new Musician(this, noteRanges[i]);
        }
        //this.noteRanges = noteRanges;
    }*/

    public void resetParameters() {
        this.HMS = (int) parameterRanges[0].getMin();
        this.HMCR = parameterRanges[1].getMin();
        this.PAR = parameterRanges[2].getMin();
        this.BW = parameterRanges[3].getMax();
    }

    private void calculateParameters(int currentIteration, int maxIteration) {

        switch (parameterMode) {
            case 00:
                //flat
                this.HMS = (int) ((parameterRanges[0].getMax() + parameterRanges[0].getMin()) / 2);
                this.HMCR = (parameterRanges[1].getMax() + parameterRanges[1].getMin()) / 2;
                this.PAR = (parameterRanges[2].getMax() + parameterRanges[2].getMin()) / 2;
                this.BW = (parameterRanges[3].getMax() + parameterRanges[3].getMin()) / 2;
                break;
            case 01:
                //flat
                this.HMS = (int) ((parameterRanges[0].getMax() + parameterRanges[0].getMin()) / 2);
                this.HMCR = parameterRanges[1].getMin() +
                        (parameterRanges[1].getMax() - parameterRanges[1].getMin()) / maxIteration * currentIteration;
                this.PAR = (parameterRanges[2].getMax() - parameterRanges[2].getMin()) / 2;
                this.BW = (parameterRanges[3].getMax() - parameterRanges[3].getMin()) / 2;
                break;
            case 02:
                this.HMS = (int) ((parameterRanges[0].getMax() + parameterRanges[0].getMin()) / 2);
                this.HMCR = parameterRanges[1].getMax() -
                        (parameterRanges[1].getMax() - parameterRanges[1].getMin()) / maxIteration * currentIteration;
                this.PAR = (parameterRanges[2].getMax() - parameterRanges[2].getMin()) / 2;
                this.BW = (parameterRanges[3].getMax() - parameterRanges[3].getMin()) / 2;
                break;
            case 10:
                this.HMS = (int) (parameterRanges[0].getMin() +
                        (parameterRanges[0].getMax() - parameterRanges[0].getMin()) / maxIteration * currentIteration);
                this.HMCR = (parameterRanges[1].getMax() + parameterRanges[1].getMin()) / 2;
                this.PAR = (parameterRanges[2].getMax() + parameterRanges[2].getMin()) / 2;
                this.BW = (parameterRanges[3].getMax() + parameterRanges[3].getMin()) / 2;
                break;
            case 11:
                //original
                this.HMS = (int) (parameterRanges[0].getMin() +
                        (parameterRanges[0].getMax() - parameterRanges[0].getMin()) / maxIteration * currentIteration);
                this.HMCR = parameterRanges[1].getMin() +
                        (parameterRanges[1].getMax() - parameterRanges[1].getMin()) / maxIteration * currentIteration;
                this.PAR = parameterRanges[2].getMin() +
                        (parameterRanges[2].getMax() - parameterRanges[2].getMin()) / maxIteration * currentIteration;
                this.BW = parameterRanges[3].getMax() * Math.exp(Math.log(parameterRanges[3].getMin() /
                        parameterRanges[3].getMax()) / maxIteration * currentIteration);
                break;
            case 12:
                this.HMS = (int) (parameterRanges[0].getMin() +
                        (parameterRanges[0].getMax() - parameterRanges[0].getMin()) / maxIteration * currentIteration);
                this.HMCR = parameterRanges[1].getMax() -
                        (parameterRanges[1].getMax() - parameterRanges[1].getMin()) / maxIteration * currentIteration;
                break;
            case 20:
                this.HMS = (int) (parameterRanges[0].getMax() -
                        (parameterRanges[0].getMax() - parameterRanges[0].getMin()) / maxIteration * currentIteration);
                this.HMCR = (parameterRanges[1].getMax() + parameterRanges[1].getMin()) / 2;
                break;
            case 21:
                this.HMS = (int) (parameterRanges[0].getMax() -
                        (parameterRanges[0].getMax() - parameterRanges[0].getMin()) / maxIteration * currentIteration);
                this.HMCR = parameterRanges[1].getMin() +
                        (parameterRanges[1].getMax() - parameterRanges[1].getMin()) / maxIteration * currentIteration;
                break;
            case 22:
                this.HMS = (int) (parameterRanges[0].getMax() -
                        (parameterRanges[0].getMax() - parameterRanges[0].getMin()) / maxIteration * currentIteration);
                this.HMCR = parameterRanges[1].getMax() -
                        (parameterRanges[1].getMax() - parameterRanges[1].getMin()) / maxIteration * currentIteration;
                break;
            case 33:
                this.HMS = (int) (parameterRanges[0].getMax() -
                        (parameterRanges[0].getMax() - parameterRanges[0].getMin()) / maxIteration * currentIteration);
                this.HMCR = parameterRanges[1].getMax() -
                        (parameterRanges[1].getMax() - parameterRanges[1].getMin()) / maxIteration * currentIteration;
                break;
            default:
                break;
        }

        //System.out.println("( " + this.HMS + " " + this.HMCR + " " + this.PAR + " " + this.BW + " ) - ");

        //System.out.println("( " + this.HMS + " " + this.HMCR + " " + this.PAR + " " + this.BW + " )");
        //System.out.println(this.BW);
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

    /*public HarmonyMemory(int dimensions, int HMS, *//*int numMusicians, */

    /**//*int numNotes, *//*ValueRange[] noteRanges, double HMCR, Random random) throws Exception {
        super(new HarmonyComparator());
        this.harmonyEvaluators = new HarmonyEvaluator[dimensions];
        this.HMS = HMS;
        //this.numMusicians = numMusicians;
        this.HMCR = HMCR;
        //this.numNotes = numNotes;
        this.random = random;
        musicians = new Musician[noteRanges.length];
        for (int i = 0; i < musicians.length; i++) {
            musicians[i] = new Musician(this, noteRanges[i]);
        }
        //this.noteRanges = noteRanges;
    }*/
    /*public void initialise() throws Exception {
        if ((harmonyComparator.getHarmonyEvaluators().length == 0) || (HMCR < 0) || (HMCR > 1) || (HMS <= 0)*//* || (numMusicians <= 0)*//* *//*|| (numNotes <= 0)*//*) {
            //if ((harmonyEvaluators.size() == 0) || (HMS <= 0) || (numMusicians <= 0) || (numNotes <= 0)) {
            System.out.println("Empty Harmony");
            throw new Exception("Empty Harmony");
        }

        *//*for (int i = 0; i < numMusicians; i++) {
            musicians.add(new Musician(this));
        }*//*
    }*/
    public void fill() throws Exception {
        //System.out.println("Fill ");
        while (this.size() < HMS) {
            //System.out.println(" " + this.size());
            //harmonyMemory.evaluateAndAdd(harmonyMemory.randomHarmony(discardThreshold));
            Harmony randomHarmony = randomHarmony();
            //System.out.println(randomHarmony.toString());
            while (harmonyComparator.checkConstraint(randomHarmony) != 0) {
                randomHarmony = randomHarmony();
            }
            //System.out.println(randomHarmony.toString());
            this.evaluateAndAdd(randomHarmony);
        }
        //printHarmonies();
        //System.out.println();
    }

    public void iterate(int maxIteration) throws Exception {
        //System.out.println("Iteration ");
        int currentIteration = 0;
        while (currentIteration <= maxIteration) {

            calculateParameters(currentIteration, maxIteration);
            resize();
            //System.out.println(" " + currentIteration + " - " + HMS);
            Harmony newHarmony = newHarmony();
            while (harmonyComparator.checkConstraint(newHarmony) != 0) {
                newHarmony = newHarmony();
            }
            this.evaluateAndAdd(newHarmony);
            currentIteration = currentIteration + 1;
        }
        //printHarmonies();
        //System.out.println();
    }

    public void sort() {
        //Collections.sort(this, this.harmonyComparator);
        Collections.sort(this, harmonyComparator);
    }

    public void resize() throws Exception {

        if (this.size() > HMS) {
            //System.out.println("Resize " + this.size() + " to " + HMS);
            //subtraction case
            //HMS = newNumHarmonies;
            while (this.size() > HMS) {
                //this.remove(this.first());
                this.remove(0);
            }
        }
        /*} else if (newNumHarmonies > HMS) {
            System.out.println("Resize " + HMS + " to " + newNumHarmonies);
            //addition case
            //HMS = newNumHarmonies;
            *//*while (this.size() < HMS) {
                //harmonyMemory.evaluateAndAdd(harmonyMemory.randomHarmony(discardThreshold));
                Harmony randomHarmony = randomHarmony();
                while (!harmonyEvaluators[0].checkConstraint(randomHarmony)) {
                    randomHarmony = randomHarmony();
                }
                //System.out.println(randomHarmony.toString());
                this.evaluateAndAdd(randomHarmony);
            }*//*
        }*/

    }

    public void resize(int newNumHarmonies) throws Exception {

        if (newNumHarmonies < HMS) {
            System.out.println("Resize " + HMS + " to " + newNumHarmonies);
            //subtraction case
            HMS = newNumHarmonies;
            while (this.size() > HMS) {
                //this.remove(this.first());
                this.remove(0);
            }
        } else if (newNumHarmonies > HMS) {
            System.out.println("Resize " + HMS + " to " + newNumHarmonies);
            //addition case
            //HMS = newNumHarmonies;
            /*while (this.size() < HMS) {
                //harmonyMemory.evaluateAndAdd(harmonyMemory.randomHarmony(discardThreshold));
                Harmony randomHarmony = randomHarmony();
                while (!harmonyEvaluators[0].checkConstraint(randomHarmony)) {
                    randomHarmony = randomHarmony();
                }
                //System.out.println(randomHarmony.toString());
                this.evaluateAndAdd(randomHarmony);
            }*/
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

    public int getNumMusicians() {
        //return numMusicians;
        return musicians.length;
    }

    /*public void setNumMusicians(int numMusicians) {
        this.numMusicians = numMusicians;
    }*/

    /*public int getHint() {
        return hint;
    }

    public void setHint(int hint) {
        this.hint = hint;
    }*/

    /*public HarmonyEvaluator[] getHarmonyEvaluators() {
        return harmonyEvaluators;
    }

    public void setHarmonyEvaluators(HarmonyEvaluator[] harmonyEvaluators) {
        this.harmonyEvaluators = harmonyEvaluators;
    }*/

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    /*public int getNumNotes() {
        return numNotes;
    }

    public void setNumNotes(int numNotes) {
        this.numNotes = numNotes;
    }*/

    /*public ArrayList<Musician> getExperts() {
        return musicians;
    }

    public void setExperts(ArrayList<Musician> musicians) {
        this.musicians = musicians;
    }*/

    /*public HarmonyEvaluator getHarmonyEvaluator() {
        return harmonyEvaluator;
    }

    public void setHarmonyEvaluator(HarmonyEvaluator harmonyEvaluator) {
        this.harmonyEvaluator = harmonyEvaluator;
    }*/

    /*public HarmonyEvaluator getHarmonyEvaluator(int index) {
        return harmonyEvaluators[index];
    }

    public void setHarmonyEvaluator(HarmonyEvaluator harmonyEvaluator, int index) {
        this.harmonyEvaluators[index] = harmonyEvaluator;
        harmonyEvaluator.setIndex(index);
    }*/

    /*public ArrayList<Musician> getMusicians() {
        return musicians;
    }

    public void setMusicians(ArrayList<Musician> musicians) {
        this.musicians = musicians;
    }*/

    public Musician[] getMusicians() {
        return musicians;
    }

    public void setMusicians(Musician[] musicians) {
        this.musicians = musicians;
    }

    public int getHMS() {
        return HMS;
    }//true if added and false otherwise

    public void setHMS(int HMS) {
        this.HMS = HMS;
    }

    //TODO: this

    public boolean evaluateAndAdd(Harmony harmony) throws Exception {
        //System.out.println(harmony.toString());
        /*for (int i = 0; i < harmonyEvaluators.length; i++) {
            harmonyEvaluators[i].evaluate(harmony);
        }*/
        harmonyComparator.evaluate(harmony);
        //System.out.println("Evaluation Complete with Merit : " + harmony.printMerits());
        return add(harmony);
    }

    //TODO: need the multi-D version now!

    public boolean add(Harmony e) {
        //System.out.println("Adding Harmony");

        if (contains(e)) {
            //System.out.println("Contained");
            return false;
        } else if (super.size() < HMS) {
            //System.out.println("Straight Add");
            for (int i = 0; i < e.getSize(); i++) {
                //System.out.println("Adding " + e.get(i) + " to " + i);
                //musicians.get(i).addNote(e.getNote(i));
                musicians[i].addNote(e.getNote(i));
            }
            super.add(e);
            sort();
            return true;
        }
        //else if (super.comparator().compare(e, super.first()) >= 0) {
        else if (/*this.harmonyComparator*/harmonyComparator.compare(e, this.get(0)) > 0) {
            //System.out.println("Compare Add");
            for (int i = 0; i < e.getSize(); i++) {
                //musicians.get(i).replaceNote(this.first().getNote(i), e.getNote(i));
                //musicians[i].replaceNote(this.first().getNote(i), e.getNote(i));
                musicians[i].replaceNote(this.get(0).getNote(i), e.getNote(i));
            }
            //super.remove(this.first());
            super.remove(0);
            super.add(e);
            sort();
            return true;
        }
        return false;
    }

    public boolean remove(Harmony e) {
        if (!super.contains(e)) return false;
        for (int i = 0; i < e.getSize(); i++) {
            //musicians.get(i).replaceNote(this.first().getNote(i), e.getNote(i));
            musicians[i].removeNote(e.getNote(i));

        }
        super.remove(e);
        sort();
        return true;
    }

    public Harmony newHarmony() {
        Harmony harmony = new Harmony(this);

        for (int i = 0; i < musicians.length; i++) {
            harmony.setNote(i, musicians[i].pickNote());
            //newFS.set(i, musicians.get(i).makeChoice(m_random.nextInt(6)));
            /*if (hint == 99) {
                harmony.set(i, musicians.get(i).makeChoice(random.nextInt(6)));
            } else {
                harmony.set(i, musicians.get(i).makeChoice(hint));
            }*/
            //musicians.get(i).sortNotes();
            //musicians[i].sortNotes();
            //musicians.get(i).printNotes();
            //harmony.setNote(i, musicians.get(i).pickNote());

            /*if (m_random.nextDouble() < randomThreshold) {
                newFS.set(i, musicians.get(i).makeChoice(0));
            } else {
                newFS.set(i, m_random.nextInt(m_numAttribs));
            }*/
        }
        return harmony;
    }

    /*public Harmony randomHarmony(double discardThreshold) {
        Harmony fs = new Harmony(this);
        for (int i = 0; i < musicians.length; i++) {
            if (random.nextDouble() <= discardThreshold) {
                fs.setNote(i, random.nextInt(numNotes));
            } else {
                fs.setNote(i, numNotes);
            }
        }
        return fs;
    }*/

    public Harmony randomHarmony() {
        Harmony harmony = new Harmony(this);
        for (int i = 0; i < musicians.length; i++) {
            //harmony.setNote(i, random.nextInt(numNotes));
            harmony.setNote(i, musicians[i].randomNote());
        }
        return harmony;
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public boolean isSpread() {
        return spread;
    }

    public void setSpread(boolean spread) {
        this.spread = spread;
    }

    public void reset() {
        for (int i = 0; i < musicians.length; i++) {
            musicians[i].clearNotes();
            musicians[i].setBestNote(null);
        }
        this.HMS = (int) parameterRanges[0].getMin();
        this.HMCR = parameterRanges[1].getMin();
        this.PAR = parameterRanges[2].getMin();
        this.BW = parameterRanges[3].getMax();
        super.clear();
    }

    public boolean isInfluence() {
        return influence;
    }

    public void setInfluence(boolean influence) {
        this.influence = influence;
    }

    public int getParameterMode() {
        return parameterMode;
    }

    public void setParameterMode(int parameterMode) {
        this.parameterMode = parameterMode;
        calculateParameters(0, 10);

    }
}
