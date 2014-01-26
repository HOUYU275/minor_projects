package dynamic.core;

import online.DataSetEvaluator;
import online.InstanceCreator;

import java.util.BitSet;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 10/01/13
 * Time: 10:37
 */
public class DynamicHarmonyMemory extends Vector<DynamicHarmony> {
    private static final long serialVersionUID = 4559196891411946508L;
    protected InstanceCreator creator;
    protected DataSetEvaluator evaluator;
    protected DynamicComparator comparator;
    protected Random random;
    protected int HMS;
    protected double HMCR;
    protected DynamicMusician[] musicians;
    protected DynamicRange range;
    protected int currentIteration;

    public DynamicHarmonyMemory(int numMusicians, int HMS, double HMCR, InstanceCreator creator, DataSetEvaluator evaluator, Random random) {
        super();
        this.HMS = HMS;
        this.HMCR = HMCR;
        this.creator = creator;
        this.evaluator = evaluator;
        this.range = new DynamicRange(this);
        this.comparator = new DynamicComparator(this);
        this.random = random;
        this.musicians = new DynamicMusician[numMusicians];
        for (int i = 0; i < musicians.length; i++) musicians[i] = new DynamicMusician(this);
    }

    @Override
    public void clear() {
        currentIteration = 0;
        for (DynamicMusician musician : musicians) musician.clear();
        super.clear();
    }

    public int[] best() {
        return comparator.toIntArray(this.lastElement());
    }

    public void fill() {
        while (this.size() < HMS) add(randomHarmony());
    }

    public BitSet iterate(int maxIteration) throws Exception {
        currentIteration = 0;
        while (currentIteration++ <= maxIteration) this.add(newHarmony());
        //print();
        return comparator.toBitSet(this.lastElement());
    }

    private void sort() {
        Collections.sort(this, comparator);
    }

    public void print() {
        for (DynamicHarmony harmony : this) {
            System.out.println(harmony);
        }
    }

    @Override
    public boolean add(DynamicHarmony harmony) {
        try {
            comparator.evaluate(harmony);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (contains(harmony)) return false;
        if (super.size() < HMS) {
            for (int i = 0; i < harmony.notes.length; i++) musicians[i].add(harmony.notes[i]);
            super.add(harmony);
            sort();
            return true;
        }
        if (comparator.compare(harmony, this.firstElement()) > 0) {
            for (int i = 0; i < harmony.notes.length; i++)
                musicians[i].replace(this.firstElement().notes[i], harmony.notes[i]);
            super.remove(0);
            super.add(harmony);
            sort();
            return true;
        }
        return false;
    }

    private boolean remove(DynamicHarmony e) {
        if (!super.contains(e)) return false;
        for (int i = 0; i < e.notes.length; i++) musicians[i].remove(e.notes[i]);
        super.remove(e);
        sort();
        return true;
    }

    private DynamicHarmony newHarmony() {
        DynamicHarmony harmony = new DynamicHarmony(this);
        for (int i = 0; i < musicians.length; i++) harmony.notes[i] = musicians[i].pick();
        return harmony;
    }

    private DynamicHarmony randomHarmony() {
        DynamicHarmony harmony = new DynamicHarmony(this);
        for (int i = 0; i < musicians.length; i++) harmony.notes[i] = musicians[i].random();
        return harmony;
    }

    public void invalidate() {
        range.convert();
        for (int i = 0; i < super.size(); i++) {
            for (int j = 0; j < super.get(i).notes.length; j++)
                if (!range.contains(super.get(i).notes[j].value)) super.get(i).notes[j].value = -1;
            try {
                comparator.evaluate(super.get(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        sort();
        for (int i = 0; i < musicians.length; i++) {
            for (int j = 0; j < musicians[i].notes.size(); j++) {
                if (!range.contains(musicians[i].notes.get(j).value)) musicians[i].notes.remove(j);
            }
        }
        //print();
    }
}