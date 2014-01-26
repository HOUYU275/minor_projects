package util;

import originalharmonysearch.core.Harmony;

import java.util.BitSet;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 05-Nov-2010
 * Time: 11:08:59
 * To change this template use File | Settings | File Templates.
 */
public class Translate {

    public static BitSet toBitSetWrong(Harmony harmony) throws Exception {
        //return null;
        //int numAttributes = harmony.getHarmonyMemory().getNumNotes();
        int numAttributes = (int) harmony.getHarmonyMemory().getMusicians()[0].getNoteRange().getMax() + 1;
        BitSet bs = new BitSet(numAttributes);
        int[] count = new int[numAttributes];
        int[] notes = harmony.getIntNotes();
        for (int i = 0; i < notes.length; i++) {
            if ((notes[i] >= 0) && (notes[i] < numAttributes)) {
                count[notes[i]] = count[notes[i]] + 1;
            }
        }
        for (int i = 0; i < count.length; i++) {
            if (count[i] > 0) {
                bs.set(i);
            }
        }
        //System.out.println(bs.toString());
        return bs;
    }

}
