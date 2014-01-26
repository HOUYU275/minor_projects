package dynamic.core;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 10/01/13
 * Time: 10:52
 */
public class DynamicComparator implements Comparator<DynamicHarmony> {
    protected DynamicHarmonyMemory memory;

    public DynamicComparator(DynamicHarmonyMemory memory) {
        super();
        this.memory = memory;
    }

    public BitSet toBitSet(DynamicHarmony harmony) {
        BitSet bs = new BitSet(memory.range.numAttributes);
        for (int i = 0; i < harmony.notes.length; i++)
            if (harmony.notes[i].value != -1 && memory.range.contains(harmony.notes[i].value))
                bs.set(harmony.notes[i].value);
        return bs;
    }

    public void evaluate(DynamicHarmony harmony) throws Exception {
        harmony.merits[0] = memory.evaluator.evaluate(memory.creator.convertEvaluateHarmony(toBitSet(harmony)));
        harmony.merits[1] = 0 - toBitSet(harmony).cardinality();
    }

    public int[] toIntArray(DynamicHarmony harmony) {
        ArrayList<Integer> selected = new ArrayList<>();
        int[] count = new int[memory.range.numAttributes];
        for (int i = 0; i < harmony.notes.length; i++)
            if (harmony.notes[i].value != -1 & memory.range.contains(harmony.notes[i].value))
                count[harmony.notes[i].value]++;
        for (int i = 0; i < memory.range.numAttributes; i++) if (count[i] > 0) selected.add(i);
        Collections.sort(selected);
        int[] returnArray = new int[selected.size()];
        for (int i = 0; i < selected.size(); i++) returnArray[i] = selected.get(i);
        return returnArray;
    }

    @Override
    public int compare(DynamicHarmony fs1, DynamicHarmony fs2) {
        if (fs1.merits[0] > fs2.merits[0]) return 1;
        else if (fs1.merits[0] == fs2.merits[0] && fs1.merits[1] > fs2.merits[1]) return 1;
        else if (fs1.merits[0] == fs2.merits[0] && fs1.merits[1] == fs2.merits[1]) return 0;
        else return -1;
    }
}
