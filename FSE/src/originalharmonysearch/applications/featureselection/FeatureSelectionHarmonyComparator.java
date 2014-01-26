package originalharmonysearch.applications.featureselection;

import originalharmonysearch.core.Harmony;
import originalharmonysearch.core.HarmonyComparator;
import weka.attributeSelection.SubsetEvaluator;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 18-Aug-2010
 * Time: 17:27:38
 * To change this template use File | Settings | File Templates.
 */
public class FeatureSelectionHarmonyComparator extends HarmonyComparator {

    private SubsetEvaluator subsetEvaluator;

    public FeatureSelectionHarmonyComparator(SubsetEvaluator subsetEvaluator) throws Exception {
        super();
        this.setDimension(2);
        this.subsetEvaluator = subsetEvaluator;
        //new HarmonyEvaluator[]{new FeatureSelectionSubsetEvaluator(subsetEvaluator), new FeatureSelectionCardinalityEvaluator()}        
    }

    @Override
    public Object translate(Harmony harmony) throws Exception {
        return toBitSet(harmony);
        /*int numAttributes = (int) harmony.getHarmonyMemory().getMusicians()[0].getNoteRange().getMax();
        BitSet bs = new BitSet(numAttributes);
        int[] count = new int[numAttributes];
        int[] notes = harmony.getIntNotes();
        for (int i = 0; i < notes.length; i++) {
            if ((notes[i] >= 0) & (notes[i] < numAttributes)) {
                count[notes[i]] = count[notes[i]] + 1;
            }
        }
        for (int i = 0; i < numAttributes; i++) {
            if (count[i] > 0) {
                bs.set(i);
            }
        }
        //System.out.println(bs.toString());
        return bs;*/
    }

    @Override
    public int checkConstraint(Harmony harmony) throws Exception {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void evaluate(Harmony harmony) throws Exception {

        harmony.setMerit(this.subsetEvaluator.evaluateSubset(toBitSet(harmony)), 0);
        harmony.setMerit(0 - toBitSet(harmony).cardinality(), 1);
    }

    @Override
    public int compare(Object o1, Object o2) {
        Harmony fs1 = (Harmony) o1;
        Harmony fs2 = (Harmony) o2;
        int result;

        if (fs1.getMerits()[0] > fs2.getMerits()[0]) {
            result = 1;
        } else if (fs1.getMerits()[0] == fs2.getMerits()[0]) {
            if (fs1.getMerits()[1] > fs2.getMerits()[1]) {
                result = 1;
            } else if (fs1.getMerits()[1] == fs2.getMerits()[1]) {
                result = 0;
            } else {
                result = -1;
            }
        } else {
            result = -1;
        }
        /*for (int i = 0; i < getHarmonyEvaluators().length; i++) {
            if (fs1.getMerits()[i] > fs2.getMerits()[i]) {
                result = result + 1;
            } else if (fs1.getMerits()[i] < fs2.getMerits()[i]) {
                result = result - 1;
            }
        }*/
        return result;
    }

    public int[] toArray(Harmony harmony) {
        //int numAttributes = harmony.getHarmonyMemory().getNumNotes() - 1;
        int numAttributes = (int) harmony.getHarmonyMemory().getMusicians()[0].getNoteRange().getMax();
        ArrayList<Integer> selected = new ArrayList<Integer>();
        int[] count = new int[numAttributes];
        int[] notes = harmony.getIntNotes();
        for (int i = 0; i < notes.length; i++) {
            //System.out.println(notes[i] + " ");
            if ((notes[i] >= 0) & (notes[i] < numAttributes)) {
                count[notes[i]] = count[notes[i]] + 1;
            }
        }
        //System.out.println();
        for (int i = 0; i < numAttributes; i++) {
            if (count[i] > 0) {
                selected.add(i);
            }
        }
        Collections.sort(selected);
        int[] returnArray = new int[selected.size()];
        for (int i = 0; i < selected.size(); i++) {
            returnArray[i] = selected.get(i);
            //System.out.println(returnArray[i] + " ");
        }
        //System.out.println();
        return returnArray;
    }

    private BitSet toBitSet(Harmony harmony) throws Exception {
        //return null;
        //int numAttributes = harmony.getHarmonyMemory().getNumNotes();
        int numAttributes = (int) harmony.getHarmonyMemory().getMusicians()[0].getNoteRange().getMax();
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
