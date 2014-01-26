package originalharmonysearch.applications.classifierensemble;

import originalharmonysearch.core.Harmony;
import originalharmonysearch.core.HarmonyComparator;
import weka.classifiers.Classifier;
import weka.core.Instances;

import java.util.BitSet;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 18-Aug-2010
 * Time: 16:49:57
 * To change this template use File | Settings | File Templates.
 */
public class ClassifierEnsembleHarmonyComparator extends HarmonyComparator {

    private DecisionMatrix decisionMatrix;
    //private Classifier[] classifiers;
    //private Instances data;

    public ClassifierEnsembleHarmonyComparator(Classifier[] classifiers, Instances data) throws Exception {
        super();
        this.setDimension(2);
        //this.classifiers = classifiers;
        //this.data = data;
        //decisionMatrix = new DecisionMatrix(classifiers.length, data.numInstances(), data.numClasses());
        decisionMatrix = new DecisionMatrix(classifiers, data);
        decisionMatrix.build();
        decisionMatrix.countDecisions();
        //decisionMatrix.printDecisionCount();
        decisionMatrix.countAgreements();
        //decisionMatrix.printAgreementCount();
        //decisionMatrix.countLabels();
        //decisionMatrix.printLabels();
        decisionMatrix.calculateEntropy();
        //new HarmonyEvaluator[]{new ClassifierEnsembleEntropyEvaluator(classifiers, data), new ClassifierEnsembleCardinalityEvaluator()}
    }

    @Override
    public Object translate(Harmony harmony) throws Exception {
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

    @Override
    public int checkConstraint(Harmony harmony) throws Exception {
        return 0;
    }

    @Override
    public void evaluate(Harmony harmony) throws Exception {
        harmony.setMerit(decisionMatrix.calculateEntropy(harmony.getIntNotes()), 0);
        harmony.setMerit(0 - toBitSet(harmony).cardinality(), 1);
    }

    @Override
    public int compare(Object o1, Object o2) {
        Harmony fs1 = (Harmony) o1;
        Harmony fs2 = (Harmony) o2;
        int result = 0;
        for (int i = 0; i < this.getDimension(); i++) {
            if (fs1.getMerits()[i] > fs2.getMerits()[i]) {
                result = result + 1;
            } else if (fs1.getMerits()[i] < fs2.getMerits()[i]) {
                result = result - 1;
            }
        }
        return result;
    }

    public void printWekaDataToFile() {
        this.decisionMatrix.printWekaDataToFile();
    }

    public DecisionMatrix getDecisionMatrix() {
        return decisionMatrix;
    }

    public void setDecisionMatrix(DecisionMatrix decisionMatrix) {
        this.decisionMatrix = decisionMatrix;
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
