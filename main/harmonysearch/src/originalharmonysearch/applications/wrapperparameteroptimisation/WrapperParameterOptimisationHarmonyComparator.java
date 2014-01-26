package originalharmonysearch.applications.wrapperparameteroptimisation;

import originalharmonysearch.applications.featureselection.FeatureSelectionHarmonyMemory;
import originalharmonysearch.core.Harmony;
import originalharmonysearch.core.HarmonyComparator;
import originalharmonysearch.core.HarmonyMemory;
import originalharmonysearch.core.ValueRange;
import weka.attributeSelection.SubsetEvaluator;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 18-Aug-2010
 * Time: 17:41:38
 * To change this template use File | Settings | File Templates.
 */
public class WrapperParameterOptimisationHarmonyComparator extends HarmonyComparator {

    private SubsetEvaluator subsetEvaluator;
    private Random m_random = new Random();
    //private int numHarmonies = 25;
    //private int numMusicians = 10;
    private int iteration = 100;
    private int numNotes;

    public WrapperParameterOptimisationHarmonyComparator(SubsetEvaluator subsetEvaluator, Instances data) throws Exception {
        super();
        this.setDimension(1);
        this.subsetEvaluator = subsetEvaluator;
        this.numNotes = data.numAttributes() - 1;
        //new HarmonyEvaluator[]{new WrapperParameterOptimisationEvaluator(subsetEvaluator, data)}
    }

    @Override
    public Object translate(Harmony harmony) throws Exception {
        //notice the minus one here!
        //int numAttributes = harmony.getHarmonyMemory().getNumNotes() - 1;
        //int numAttributes = (int) harmony.getHarmonyMemory().getMusicians()[0].getNoteRange().getMax();
        /*ValueRange[] noteRanges = new ValueRange[(int)harmony.getNotes()[2]];
        for (int i = 0; i < noteRanges.length; i++) {
            noteRanges[i] = new ValueRange(0, numNotes, false);
        }
        FeatureSelectionHarmonyMemory featureSelectionHarmonyMemory = new FeatureSelectionHarmonyMemory((int)harmony.getNotes()[0], noteRanges, harmony.getNotes()[1], m_random, subsetEvaluator);
        //System.out.println(bs.toString());
        return featureSelectionHarmonyMemory;*/
        return null;
    }

    @Override
    public int checkConstraint(Harmony harmony) throws Exception {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void evaluate(Harmony harmony) throws Exception {
        //long startTime = System.currentTimeMillis();

        FeatureSelectionHarmonyMemory featureSelectionHarmonyMemory = toFeatureSelectionHarmonyMemory(harmony);
        /*System.out.println("# Harmonies : " + featureSelectionHarmonyMemory.getHMS() +
                " # Musicians : " + featureSelectionHarmonyMemory.getNumMusicians());*/
        //featureSelectionHarmonyMemory.initialise();
        //long fstartTime = System.currentTimeMillis();
        //System.out.println("Start Filling Harmony Memory");
        featureSelectionHarmonyMemory.fill();
        //System.out.println("End Filling Harmony Memory");
        //long fendTime = System.currentTimeMillis();
        //featureSelectionHarmonyMemory.printHarmonies();
        //System.out.println("Start Iterating Harmony Memory");
        featureSelectionHarmonyMemory.iterate(iteration);
        //System.out.println("End Iterating Harmony Memory");
        //long endTime = System.currentTimeMillis();
        /*System.out.println("# Harmonies : " + featureSelectionHarmonyMemory.getHMS() +
                " # Musicians : " + featureSelectionHarmonyMemory.getNumMusicians() +
                " F Time Taken : " + (fendTime-fstartTime) +
                " Time Taken : " + (endTime-startTime));*/
        //System.out.println((endTime-startTime) + " seconds");
        //System.out.println("NumHarmonies = " + featureSelectionHarmonyMemory.getHMS() + " & HMCR = " + featureSelectionHarmonyMemory.getHMCR());
        //featureSelectionHarmonyMemory.printHarmonies();
        //m_bestMerit = featureSelectionHarmonyMemory.last().getMerit(0);

        //featureSelectionHarmonyMemory.printHarmonies();
        //System.out.println(" - - - \n" + featureSelectionHarmonyMemory.last().toString());
        //return attributeList((BitSet) featureSelectionHarmonyMemory.getHarmonyEvaluator(0).translate(featureSelectionHarmonyMemory.last()));
        //return ((FeatureSelectionSubsetEvaluator) featureSelectionHarmonyMemory.getHarmonyEvaluator(0)).toArray(featureSelectionHarmonyMemory.last());
        //System.out.println((endTime-startTime) + " seconds " + featureSelectionHarmonyMemory.last().getMerit(0));
        //harmony.setMerit(featureSelectionHarmonyMemory.best().getMerit(0)/* + ((numNotes - (featureSelectionHarmonyMemory.best().getMerit(1))) / numNotes)*/, super.getIndex());
        harmony.setMerit(featureSelectionHarmonyMemory.best().getMerit());

        //harmony.setCardinality(bitSet.cardinality());
    }

    @Override
    public int compare(Object o1, Object o2) {
        Harmony fs1 = (Harmony) o1;
        Harmony fs2 = (Harmony) o2;
        int result = 0;
        //for (int i = 0; i < getHarmonyEvaluators().length; i++) {
        if (fs1.getMerit() > fs2.getMerit()) {
            result = result + 1;
        } else if (fs1.getMerit() < fs2.getMerit()) {
            result = result - 1;
        }
        //}
        return result;
    }

    /*public int[] toArray(Harmony harmony) {
            //int numAttributes = harmony.getHarmonyMemory().getNumNotes() - 1;
            int numAttributes = (int) harmony.getHarmonyMemory().getMusicians()[0].getNoteRange().getMax();
            ArrayList<Integer> selected = new ArrayList<Integer>();
            int[] count = new int[numAttributes];
            int[] notes = harmony.getIntNotes();
            for (int i = 0; i < notes.length; i++) {
                if ((notes[i] >= 0) && (notes[i] < numAttributes)) {
                    count[notes[i]] = count[notes[i]] + 1;
                }
            }
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
*/

    public FeatureSelectionHarmonyMemory toFeatureSelectionHarmonyMemory(Harmony harmony) throws Exception {
        //notice the minus one here!
        //int numAttributes = harmony.getHarmonyMemory().getNumNotes() - 1;
        //int numAttributes = (int) harmony.getHarmonyMemory().getMusicians()[0].getNoteRange().getMax();
        ValueRange[] valueRanges = new ValueRange[harmony.getIntNotes()[2]];
        for (int i = 0; i < valueRanges.length; i++) {
            valueRanges[i] = new ValueRange(0, numNotes, false);
        }
        FeatureSelectionHarmonyMemory featureSelectionHarmonyMemory = new FeatureSelectionHarmonyMemory(HarmonyMemory.createParameterRanges(harmony.getIntNotes()[0], harmony.getDoubleNotes()[1]), valueRanges, m_random, subsetEvaluator);
        //System.out.println(bs.toString());
        return featureSelectionHarmonyMemory;
    }

    public int[] toArray(Harmony harmony) {
        //int numAttributes = harmony.getHarmonyMemory().getNumNotes() - 1;
        int numAttributes = (int) harmony.getHarmonyMemory().getMusicians()[0].getNoteRange().getMax();
        ArrayList<Integer> selected = new ArrayList<Integer>();
        int[] count = new int[numAttributes];
        int[] notes = harmony.getIntNotes();
        for (int i = 0; i < notes.length; i++) {
            if ((notes[i] >= 0) && (notes[i] < numAttributes)) {
                count[notes[i]] = count[notes[i]] + 1;
            }
        }
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

}
