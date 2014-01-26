package dynamic.core;

import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 10/01/13
 * Time: 10:55
 */
public class DynamicHarmony {
    protected double[] merits;
    protected DynamicNote[] notes;
    protected DynamicHarmonyMemory memory;

    public DynamicHarmony(DynamicHarmonyMemory memory) {
        this.memory = memory;
        notes = new DynamicNote[memory.musicians.length];
        merits = new double[2];
    }

    @Override
    public String toString() {
        String s = "(";
        for (DynamicNote n : notes) s = s + n.value + ",";
        s = s.substring(0, s.length() - 2) + ")" + " [" + merits[0]+ "," + merits[1] + "]";
        return s;
    }

    public String printMerits() {
        String returnString = "";
        for (double merit : merits) returnString = returnString + " " + merit;
        return returnString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DynamicHarmony that = (DynamicHarmony) o;
        return Arrays.equals(notes, that.notes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(notes);
    }
}
