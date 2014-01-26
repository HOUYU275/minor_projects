package fuzzified.core;

import java.util.Iterator;
import java.util.Random;
import java.util.TreeSet;

/**
 * User: rrd09, Date: 19-Mar-2010, Time: 15:44:38
 */
public class HarmonyMemory extends TreeSet<Harmony> {

    protected Musician[] musicians;
    protected HarmonyComparator comparator;

    private ValueRange[] parameterRanges;
    private ValueRange[] valueRanges;

    protected int HMS;
    protected double HMCR;
    protected double PAR;
    protected double BW;

    protected Random random;

    protected boolean fuzzified = true;
    protected double spread = 1d;

    public HarmonyMemory() {
        super();
    }

    public HarmonyMemory(ValueRange[] parameterRanges, ValueRange[] valueRanges,
                         HarmonyComparator comparator, Random random) {
        super();
        this.parameterRanges = parameterRanges;
        this.valueRanges = valueRanges;
        this.comparator = comparator;
        this.random = random;
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
        this.musicians = new Musician[valueRanges.length];
        for (int i = 0; i < musicians.length; i++) {
            musicians[i] = new Musician(this, valueRanges[i]);
        }
    }

    public void initialise() {
        initialiseParameters();
        initialiseMusicians();
        super.clear();
    }

    public Harmony newHarmony() {
        Harmony harmony = new Harmony(this);
        for (int i = 0; i < musicians.length; i++) {
            harmony.setNote(i, musicians[i].pick());
        }
        while (comparator.checkConstraint(harmony) != 0) {
            for (int i = 0; i < musicians.length; i++) {
                harmony.setNote(i, musicians[i].pick());
            }
        }
        comparator.evaluate(harmony);
        return harmony;
    }

    public Harmony randomHarmony() {
        Harmony harmony = new Harmony(this);
        for (int i = 0; i < musicians.length; i++) {
            harmony.setNote(i, musicians[i].random());
        }
        while (comparator.checkConstraint(harmony) != 0) {
            for (int i = 0; i < musicians.length; i++) {
                harmony.setNote(i, musicians[i].random());
            }
        }
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
        } else {
            if (fuzzified) {
                for (int i = 0; i < e.getSize(); i++) musicians[i].add(e.getNote(i));
                super.add(e);
                calculateMemberships();
                return true;
            } else {
                if (comparator.compare(e, worst()) > 0) {
                    for (int i = 0; i < e.getSize(); i++) musicians[i].replace(worst().getNote(i), e.getNote(i));
                    super.remove(worst());
                    super.add(e);
                    calculateMemberships();
                    return true;
                }
            }
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
        if (fuzzified) calculateMemberships();
    }

    public void iterate(int maxIteration) {
        int currentIteration = 0;
        while (currentIteration < maxIteration) {
            add(newHarmony());
            /*for (Musician musician : musicians) System.out.print(musician.notes.size() + " ");
            System.out.println("| " + size());*/
            currentIteration++;
        }
    }

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

    public void calculateMemberships() {
        double bestMerit, worstMerit, bottomMerit, membership;
        int counter = 0;
        bestMerit = best().getMerit();
        worstMerit = Double.NEGATIVE_INFINITY;
        bottomMerit = Double.NEGATIVE_INFINITY;
        Iterator iterator = descendingIterator();
        while (iterator.hasNext()) {
            Harmony harmony = (Harmony) iterator.next();
            if (counter == HMS - 1) {
                worstMerit = harmony.getMerit();
                bottomMerit = worstMerit - (bestMerit - worstMerit) * spread;
                harmony.membership = 1;
                counter++;
            } else if (counter >= HMS) {
                if (harmony.getMerit() <= bottomMerit) {
                    for (int i = 0; i < harmony.getSize(); i++) {
                        musicians[i].remove(harmony.getNote(i));
                    }
                    iterator.remove();
                } else {
                    membership = (harmony.getMerit() - bottomMerit) / (worstMerit - bottomMerit);
                    harmony.membership = membership;
                }
            } else {
                harmony.membership = 1;
                counter++;
            }
        }
    }

    public void setFuzzified(boolean fuzzified) {
        this.fuzzified = fuzzified;
    }

    public Musician[] getMusicians() {
        return musicians;
    }

    public void setSpread(double spread) {
        this.spread = spread;
    }
}
