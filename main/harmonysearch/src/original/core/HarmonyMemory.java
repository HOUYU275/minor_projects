package original.core;

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
    private ValueRange[] valueRanges;

    protected int HMS;
    protected double HMCR;
    protected double PAR;
    protected double BW;

    protected Random random;

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
            add(newHarmony());
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

    public Musician[] getMusicians() {
        return musicians;
    }

    public static ValueRange[] createParameterRanges(int numHarmonies, double hmcr) {
        ValueRange[] parameterRanges = new ValueRange[4];
        parameterRanges[0] = new ValueRange(numHarmonies, numHarmonies, false);
        parameterRanges[1] = new ValueRange(hmcr, hmcr, true);
        parameterRanges[2] = new ValueRange(0, 0, true);
        parameterRanges[3] = new ValueRange(Double.MIN_VALUE, Double.MIN_VALUE, true);
        return parameterRanges;
    }

    public Random getRandom() {
        return random;
    }

    public HarmonyComparator getComparator() {
        return comparator;
    }
}
