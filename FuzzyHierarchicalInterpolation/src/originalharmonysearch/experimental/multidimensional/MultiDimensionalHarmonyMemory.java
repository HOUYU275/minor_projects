package originalharmonysearch.experimental.multidimensional;

import originalharmonysearch.core.Harmony;
import originalharmonysearch.core.HarmonyMemory;
import originalharmonysearch.core.ValueRange;

import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 18-Aug-2010
 * Time: 13:37:49
 * To change this template use File | Settings | File Templates.
 */
public class MultiDimensionalHarmonyMemory extends HarmonyMemory {

    public MultiDimensionalHarmonyMemory() {
    }

    public MultiDimensionalHarmonyMemory(int dimensions, int numHarmonies, ValueRange[] valueRanges, double HMCR, Random random) throws Exception {
        //super(dimensions, numHarmonies, valueRanges, HMCR, random);
    }

    @Override
    public Harmony best() {
        return super.best();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public boolean add(Harmony e) {
        /*if (contains(e)) {
            //System.out.println("Contained");
            return false;
        }
        else if (super.size() < this.getHMS()) {
            //System.out.println("Straight Add");
            for (int i = 0; i < e.getSize(); i++) {
                //System.out.println("Adding " + e.get(i) + " to " + i);
                //musicians.get(i).addNote(e.getNote(i));
                this.getMusicians()[i].addNote(e.getNote(i));
            }
            super.add(e);
            sort();
            return true;
        }
        //else if (super.comparator().compare(e, super.first()) >= 0) {
        else if (this.harmonyComparator.compare(e, this.get(0)) >= 0) {
            //System.out.println("Compare Add");
            for (int i = 0; i < e.getSize(); i++) {
                //musicians.get(i).replaceNote(this.first().getNote(i), e.getNote(i));
                //musicians[i].replaceNote(this.first().getNote(i), e.getNote(i));
                this.getMusicians()[i].replaceNote(this.get(0).getNote(i), e.getNote(i));
            }
            //super.remove(this.first());
            super.remove(0);
            super.add(e);
            sort();
            return true;
        }*/
        return false;

    }
}
