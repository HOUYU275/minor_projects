/*
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

/*
 *    Vote.java
 *    Copyright (C) 2000 University of Waikato
 *    Copyright (C) 2006 Roberto Perdisci
 *
 */

package weka.classifiers.meta;

import originalharmonysearch.applications.classifierensemble.ClassifierEnsembleHarmonyComparator;
import originalharmonysearch.core.Harmony;
import originalharmonysearch.core.HarmonyMemory;
import originalharmonysearch.core.ValueRange;
import originalharmonysearch.applications.classifierensemble.ClassifierEnsembleHarmonyMemory;
import weka.attributeSelection.ASEvaluation;
import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.GreedyStepwise;
import weka.attributeSelection.SubsetEvaluator;
import weka.classifiers.Classifier;
import weka.classifiers.RandomizableMultipleClassifiersCombiner;
import weka.classifiers.trees.J48;
import weka.core.*;
import weka.core.Capabilities.Capability;
import weka.core.TechnicalInformation.Field;
import weka.core.TechnicalInformation.Type;
import weka.filters.unsupervised.attribute.Remove;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/**
 * <!-- globalinfo-start -->
 * Class for combining classifiers. Different combinations of probability estimates for classification are available.<br/>
 * <br/>
 * For more information see:<br/>
 * <br/>
 * Ludmila I. Kuncheva (2004). Combining Pattern Classifiers: Methods and Algorithms. John Wiley and Sons, Inc..<br/>
 * <br/>
 * J. Kittler, M. Hatef, Robert P.W. Duin, J. Matas (1998). On combining classifiers. IEEE Transactions on Pattern Analysis and Machine Intelligence. 20(3):226-239.
 * <p/>
 * <!-- globalinfo-end -->
 * <p/>
 * <!-- options-start -->
 * Valid options are: <p/>
 * <p/>
 * <pre> -S &lt;num&gt;
 *  Random number seed.
 *  (default 1)</pre>
 * <p/>
 * <pre> -B &lt;classifier specification&gt;
 *  Full class name of classifier to include, followed
 *  by scheme options. May be specified multiple times.
 *  (default: "weka.classifiers.rules.ZeroR")</pre>
 * <p/>
 * <pre> -D
 *  If set, classifier is run in debug mode and
 *  may output additional info to the console</pre>
 * <p/>
 * <pre> -R &lt;AVG|PROD|MAJ|MIN|MAX|MED&gt;
 *  The combination rule to use
 *  (default: AVG)</pre>
 * <p/>
 * <!-- options-end -->
 * <p/>
 * <!-- technical-bibtex-start -->
 * BibTeX:
 * <pre>
 * &#64;book{Kuncheva2004,
 *    author = {Ludmila I. Kuncheva},
 *    publisher = {John Wiley and Sons, Inc.},
 *    title = {Combining Pattern Classifiers: Methods and Algorithms},
 *    year = {2004}
 * }
 *
 * &#64;article{Kittler1998,
 *    author = {J. Kittler and M. Hatef and Robert P.W. Duin and J. Matas},
 *    journal = {IEEE Transactions on Pattern Analysis and Machine Intelligence},
 *    number = {3},
 *    pages = {226-239},
 *    title = {On combining classifiers},
 *    volume = {20},
 *    year = {1998}
 * }
 * </pre>
 * <p/>
 * <!-- technical-bibtex-end -->
 *
 * @author Alexander K. Seewald (alex@seewald.at)
 * @author Eibe Frank (eibe@cs.waikato.ac.nz)
 * @author Roberto Perdisci (roberto.perdisci@gmail.com)
 * @version $Revision: 1.19 $
 */
public class HarmonyEnsemble
        extends RandomizableMultipleClassifiersCombiner
        implements TechnicalInformationHandler {

    /**
     * for serialization
     */
    static final long serialVersionUID = -637891196294399624L;

    /**
     * combination rule: Average of Probabilities
     */
    public static final int AVERAGE_RULE = 1;
    /**
     * combination rule: Product of Probabilities (only nominal classes)
     */
    public static final int PRODUCT_RULE = 2;
    /**
     * combination rule: Majority Voting (only nominal classes)
     */
    public static final int MAJORITY_VOTING_RULE = 3;
    /**
     * combination rule: Minimum Probability
     */
    public static final int MIN_RULE = 4;
    /**
     * combination rule: Maximum Probability
     */
    public static final int MAX_RULE = 5;
    /**
     * combination rule: Median Probability (only numeric class)
     */
    public static final int MEDIAN_RULE = 6;
    /**
     * combination rules
     */
    public static final Tag[] TAGS_RULES = {
            new Tag(AVERAGE_RULE, "AVG", "Average of Probabilities"),
            new Tag(PRODUCT_RULE, "PROD", "Product of Probabilities"),
            new Tag(MAJORITY_VOTING_RULE, "MAJ", "Majority Voting"),
            new Tag(MIN_RULE, "MIN", "Minimum Probability"),
            new Tag(MAX_RULE, "MAX", "Maximum Probability"),
            new Tag(MEDIAN_RULE, "MED", "Median")
    };

    /**
     * Combination Rule variable
     */
    protected int m_CombinationRule = AVERAGE_RULE;

    /**
     * the random number generator used for breaking ties in majority voting
     *
     * @see #distributionForInstanceMajorityVoting(weka.core.Instance)
     */
    protected Random m_Random;

    /**
     * Returns a string describing classifier
     *
     * @return a description suitable for
     *         displaying in the explorer/experimenter gui
     */
    public String globalInfo() {
        return
                "Class for combining classifiers. Different combinations of "
                        + "probability estimates for classification are available.\n\n"
                        + "For more information see:\n\n"
                        + getTechnicalInformation().toString();
    }

    /**
     * Returns an enumeration describing the available options.
     *
     * @return an enumeration of all the available options.
     */
    public Enumeration listOptions() {
        Enumeration enm;
        Vector result;

        result = new Vector();

        enm = super.listOptions();
        while (enm.hasMoreElements())
            result.addElement(enm.nextElement());

        result.addElement(new Option(
                "\tThe combination rule to use\n"
                        + "\t(default: AVG)",
                "R", 1, "-R " + Tag.toOptionList(TAGS_RULES)));

        return result.elements();
    }

    /**
     * Gets the current settings of Vote.
     *
     * @return an array of strings suitable for passing to setOptions()
     */
    public String[] getOptions() {
        int i;
        Vector result;
        String[] options;

        result = new Vector();

        options = super.getOptions();
        for (i = 0; i < options.length; i++)
            result.add(options[i]);

        result.add("-R");
        result.add("" + getCombinationRule());

        return (String[]) result.toArray(new String[result.size()]);
    }

    /**
     * Parses a given list of options. <p/>
     * <p/>
     * <!-- options-start -->
     * Valid options are: <p/>
     * <p/>
     * <pre> -S &lt;num&gt;
     *  Random number seed.
     *  (default 1)</pre>
     * <p/>
     * <pre> -B &lt;classifier specification&gt;
     *  Full class name of classifier to include, followed
     *  by scheme options. May be specified multiple times.
     *  (default: "weka.classifiers.rules.ZeroR")</pre>
     * <p/>
     * <pre> -D
     *  If set, classifier is run in debug mode and
     *  may output additional info to the console</pre>
     * <p/>
     * <pre> -R &lt;AVG|PROD|MAJ|MIN|MAX|MED&gt;
     *  The combination rule to use
     *  (default: AVG)</pre>
     * <p/>
     * <!-- options-end -->
     *
     * @param options the list of options as an array of strings
     * @throws Exception if an option is not supported
     */
    public void setOptions(String[] options) throws Exception {
        String tmpStr;

        tmpStr = Utils.getOption('R', options);
        if (tmpStr.length() != 0)
            setCombinationRule(new SelectedTag(tmpStr, TAGS_RULES));
        else
            setCombinationRule(new SelectedTag(AVERAGE_RULE, TAGS_RULES));

        super.setOptions(options);
    }

    /**
     * Returns an instance of a TechnicalInformation object, containing
     * detailed information about the technical background of this class,
     * e.g., paper reference or book this class is based on.
     *
     * @return the technical information about this class
     */
    public TechnicalInformation getTechnicalInformation() {
        TechnicalInformation result;
        TechnicalInformation additional;

        result = new TechnicalInformation(Type.BOOK);
        result.setValue(Field.AUTHOR, "Ludmila I. Kuncheva");
        result.setValue(Field.TITLE, "Combining Pattern Classifiers: Methods and Algorithms");
        result.setValue(Field.YEAR, "2004");
        result.setValue(Field.PUBLISHER, "John Wiley and Sons, Inc.");

        additional = result.add(Type.ARTICLE);
        additional.setValue(Field.AUTHOR, "J. Kittler and M. Hatef and Robert P.W. Duin and J. Matas");
        additional.setValue(Field.YEAR, "1998");
        additional.setValue(Field.TITLE, "On combining classifiers");
        additional.setValue(Field.JOURNAL, "IEEE Transactions on Pattern Analysis and Machine Intelligence");
        additional.setValue(Field.VOLUME, "20");
        additional.setValue(Field.NUMBER, "3");
        additional.setValue(Field.PAGES, "226-239");

        return result;
    }

    /**
     * Returns default capabilities of the classifier.
     *
     * @return the capabilities of this classifier
     */
    public Capabilities getCapabilities() {
        Capabilities result = super.getCapabilities();

        // class
        if ((m_CombinationRule == PRODUCT_RULE)
                || (m_CombinationRule == MAJORITY_VOTING_RULE)) {
            result.disableAllClasses();
            result.disableAllClassDependencies();
            result.enable(Capability.NOMINAL_CLASS);
            result.enableDependency(Capability.NOMINAL_CLASS);
        } else if (m_CombinationRule == MEDIAN_RULE) {
            result.disableAllClasses();
            result.disableAllClassDependencies();
            result.enable(Capability.NUMERIC_CLASS);
            result.enableDependency(Capability.NUMERIC_CLASS);
        }

        return result;
    }

    /*private ArrayList<BitSet> fillSubsetPool(Instances newData) throws Exception {
        FuzzyRoughSubsetEval ASEval = new FuzzyRoughSubsetEval();
        ASEval.buildEvaluator(newData);
        System.out.println("CORE: " + ASEval.computeCore().toString());
        ArrayList<BitSet> subsetPool = new ArrayList<BitSet>();
        for (int i = 0; i < 150; i ++) {
            BitSet newSubset = searchBest(newData);
            if (subsetPool.contains(newSubset)) {
                //System.out.println("(" + i + ") Found Duplicate");
            }
            else {
                subsetPool.add(newSubset);
                //System.out.println("(" + i + ") Added " + newSubset);
            }
        }
        for (int i = 0; i < subsetPool.size(); i ++) {
            System.out.println(subsetPool.get(i));
        }
        return subsetPool;
    }*/

    /**
     * Buildclassifier selects a classifier from the set of classifiers
     * by minimising error on the training data.
     *
     * @param data the training data to be used for generating the
     *             boosted classifier.
     * @throws Exception if the classifier could not be built successfully
     */
    public void buildClassifier(Instances data) throws Exception {

        // can classifier handle the data?
        getCapabilities().testWithFail(data);

        // remove instances with missing class
        Instances newData = new Instances(data);
        newData.deleteWithMissingClass();

        m_Random = new Random(getSeed());


        //DecisionMatrix decisionMatrix = new DecisionMatrix(totalClassifiers, numInstances, newData.numClasses());
        //int[] singleClassifierDecisions;      3

        Integer[] indices = new Integer[data.numAttributes() - 1];
        int classIndex = data.classIndex();
        int offset = 0;
        for (int i = 0; i < indices.length + 1; i++) {
            if (i != classIndex) {
                indices[offset++] = i + 1;
            }
        }

        boolean useReducts = true;


        //ArrayList<BitSet> subsetPool = fillSubsetPool(newData);
        //int totalClassifiers = subsetPool.size();
        int totalClassifiers = 200;
        Classifier[] classifiers = new Classifier[totalClassifiers];
        int numInstances = newData.numInstances();

        for (int i = 0; i < totalClassifiers; i++) {
            Classifier classifier = new J48();
            FilteredClassifier fc = new FilteredClassifier();
            fc.setClassifier(classifier);
            Remove rm = new Remove();
            //rm.setOptions(new String[]{"-V", "-R", getRemove(bitSets[i], data.classIndex())});
            //rm.setOptions(new String[]{"-V", "-R", getRemove(searchBest(newData), data.classIndex())});
            //rm.setOptions(new String[]{"-V", "-R", getRemove(subsetPool.get(i), data.classIndex())});
            rm.setOptions(new String[]{"-V", "-R", randomSubSpace(indices, (data.numAttributes() / 3), classIndex + 1, m_Random)});

            //rm.setOptions(new String[]{"-V", "-R", randomSubSpace(indices,m_Random.nextInt(data.numAttributes() - 1) + 1,classIndex+1,m_Random)});
            fc.setFilter(rm);
            classifiers[i] = fc;
            classifiers[i].buildClassifier(newData);

            /*singleClassifierDecisions = new int[numInstances];
            for (int j = 0; j < newData.numInstances(); j ++) {
                *//*for (double d : getClassifier(i).distributionForInstance(newData.instance(j))) {
                    System.out.println(d + " ");
                }
                System.out.println("\n");*//*
                double[] prob = classifiers[i].distributionForInstance(newData.instance(j));
                int maxIndex = 0;
                for (int k = 0; k < prob.length; k++) {
                    if (prob[k] > prob[maxIndex]){
                        maxIndex = k;
                    }
                }
                singleClassifierDecisions[j] = maxIndex;
                //System.out.println(maxIndex);
            }
            decisionMatrix.setDecisions(i, singleClassifierDecisions);*/
        }
        //decisionMatrix.printDecisions();

        //decisionMatrix.countDecisions();
        //decisionMatrix.printDecisionCount();

        //decisionMatrix.countAgreements();
        //decisionMatrix.printAgreementCount();
        //                                      
        //decisionMatrix.countLabels();
        //decisionMatrix.printLabels();
        //decisionMatrix.calculateEntropy();

        int memorySize = 10;//m_Classifiers.length;
        int ensembleSize = 10;
        double HMCR = 0.8;
        //double discardThreshold = 0.5;
        //int maxIteration = 50;
        ValueRange[] valueRanges = new ValueRange[ensembleSize];
        for (int i = 0; i < valueRanges.length; i++) {
            valueRanges[i] = new ValueRange(0, classifiers.length, false);
        }
        //ValueRange[] parameterRanges = new ValueRange[ensembleSize];

        ClassifierEnsembleHarmonyMemory harmonyMemory = new ClassifierEnsembleHarmonyMemory(HarmonyMemory.createParameterRanges(memorySize, HMCR), /*ensembleSize, */valueRanges, m_Random, classifiers, data);
        //harmonyMemory.initialise();
        //new revision
        //String fileName = "tempWekaFile";
        //((ClassifierEnsembleEntropyEvaluator) harmonyMemory.getHarmonyEvaluator(0)).getDecisionMatrix().printWekaDataToFile(fileName);
        //((ClassifierEnsembleHarmonyComparator) harmonyMemory.getHarmonyComparator()).printWekaDataToFile(fileName);
        ((ClassifierEnsembleHarmonyComparator) harmonyMemory.getHarmonyComparator()).printWekaDataToFile();
        //int[] selected = featureSelection(fileName);
        int[] selectedHarmony = harmonySearch(harmonyMemory);

        //System.out.println("Entropy for FS Selection = " + ((ClassifierEnsembleEntropyEvaluator)harmonyMemory.getHarmonyEvaluator()).getDecisionMatrix().calculateEntropy(selected));
        System.out.println("Entropy for HS Selection = " + ((ClassifierEnsembleHarmonyComparator) harmonyMemory.getHarmonyComparator()).getDecisionMatrix().calculateEntropy(selectedHarmony));

        Classifier[] newClassifiers = new Classifier[selectedHarmony.length];
        for (int i = 0; i < selectedHarmony.length; i++) {
            newClassifiers[i] = classifiers[(int) Math.round(selectedHarmony[i])];
        }
        /*System.out.println("Values for Class!");
        for (int i = 0; i < data.classAttribute().numValues(); i ++) {
            System.out.println(data.classAttribute().value(i));
        }*/

        setClassifiers(newClassifiers);
    }

    public int[] harmonySearch(ClassifierEnsembleHarmonyMemory harmonyMemory) throws Exception {
        //int memorySize = 15;//m_Classifiers.length;
        //int ensembleSize = 10;
        //double discardThreshold = 0.5;
        int maxIteration = 100;

        //ClassifierEnsembleHarmonyMemory harmonyMemory = new ClassifierEnsembleHarmonyMemory(memorySize, ensembleSize, classifiers.length, m_Random);
        //harmonyMemory.initialise(classifiers, data);

        while (harmonyMemory.size() < harmonyMemory.getHMS()) {
            //harmonyMemory.evaluateAndAdd(harmonyMemory.randomHarmony(discardThreshold));
            harmonyMemory.evaluateAndAdd(harmonyMemory.randomHarmony());
        }

        int currentIteration = 0;
        while (currentIteration <= maxIteration) {
            harmonyMemory.evaluateAndAdd(harmonyMemory.newHarmony());
            currentIteration = currentIteration + 1;

        }

        int rank = 0;
        //BitSet returnBitSet = null;
        Harmony bestHarmony = null;
        for (Harmony harmony : harmonyMemory) {
            if (rank == harmonyMemory.getHMS() - 1) {
                bestHarmony = harmony;
            }
            System.out.println(rank + " " + harmony.toString() + " " + harmony.printMerits());
            rank++;
        }

        return bestHarmony.getIntNotes();
        /*for (int i = 0; i < getClassifiers().length; i ++) {
            setClassifier(i, classifiers[bestHarmony.getNotes()[i]]);
        }*/

        //return returnBitSet;

        //int[] selected = new int[m_Classifiers.length];
        //int[] selectedBest = new int[m_Classifiers.length];
        //double bestScore = Double.MIN_VALUE;
        //double currentScore;
        /*for (int k = 0; k < m_Classifiers.length * 10; k ++) {
            selected = EnsemblePicker.randomSelect(m_Classifiers.length, totalClassifiers);
            for (int i = 0; i < selected.length; i ++) System.out.println(" " + selected[i]);
            System.out.println();
            currentScore = decisionMatrix.calculateEntropy(selected);
            //decisionMatrix.printLabels();
            if (currentScore > bestScore) {
                bestScore = currentScore;
                selectedBest = selected;
            }
            //System.out.println(EnsemblePicker.getAvgKappa(decisionMatrix, selected));
        }*/

        /*for (int i = 0; i < selectedBest.length; i ++) {
            setClassifier(i, classifiers[selectedBest[i]]);
        }*/
        //setClassifiers
        //BitSet[] bitSets = search(newData);

        /*for (int i = 0; i < m_Classifiers.length; i++) {
            FilteredClassifier fc = new FilteredClassifier();
            fc.setClassifier(getClassifier(i));
            Remove rm = new Remove();
            //rm.setOptions(new String[]{"-V", "-R", getRemove(bitSets[i], data.classIndex())});
            rm.setOptions(new String[]{"-V", "-R", getRemove(searchBest(newData), data.classIndex())});
            fc.setFilter(rm);
            setClassifier(i, fc);
            getClassifier(i).buildClassifier(newData);
            singleClassifierDecisions = new int[numInstances];
            for (int j = 0; j < newData.numInstances(); j ++) {
                *//*for (double d : getClassifier(i).distributionForInstance(newData.instance(j))) {
                    System.out.println(d + " ");
                }
                System.out.println("\n");*//*
                double[] prob = getClassifier(i).distributionForInstance(newData.instance(j));
                int maxIndex = 0;
                for (int k = 0; k < prob.length; k++) {
                    if (prob[k] > prob[maxIndex]){
                        maxIndex = k;
                    }
                }
                singleClassifierDecisions[j] = maxIndex;
                //System.out.println(maxIndex);
            }
            decisionMatrix.setDecisions(i, singleClassifierDecisions);
        }
        decisionMatrix.printDecisions();

        decisionMatrix.countDecisions();
        //decisionMatrix.printDecisionCount();

        decisionMatrix.countAgreements();
        //decisionMatrix.printAgreementCount();

        decisionMatrix.countLabels();
        //decisionMatrix.printLabels();
        decisionMatrix.calculateEntropy();*/

        /*decisionMatrix.calculateKappas();
        decisionMatrix.printKappas();*/
    }

    public int[] featureSelection(String fileName) throws Exception {
        BufferedReader reader = new BufferedReader(
                new FileReader(fileName + ".arff"));
        Instances tempData = new Instances(reader);
        reader.close();
        // setting class attribute
        tempData.setClassIndex(tempData.numAttributes() - 1);
        //ASEvaluation ASEval = new FuzzyRoughSubsetEval();
        ASEvaluation ASEval = new CfsSubsetEval();
        ASEval.buildEvaluator(tempData);
        if (!(ASEval instanceof SubsetEvaluator)) {
            System.out.println(ASEval.getClass().getName()
                    + " is not a "
                    + "Subset evaluator!");
        }
        //SubsetEvaluator ASEvaluator = (SubsetEvaluator) ASEval;
        GreedyStepwise greedyStepwise = new GreedyStepwise();
        int[] selected = greedyStepwise.search(ASEval, tempData);
        System.out.println("Selected Subset:");
        for (int i : selected) System.out.print(" " + i);
        System.out.println();
        return selected;
    }

    /*public BitSet[] search(Instances data) throws Exception {

        int numAttributes;
        int memorySize = m_Classifiers.length;
        int ensembleSize = 15;
        double discardThreshold = 0.5;
        int maxIteration = 500;
        //double best_merit;
        //int sizeOfBest = m_numAttribs;
        //FeatureSubset temp;
        ASEvaluation ASEval = new FuzzyRoughSubsetEval();
        ASEval.buildEvaluator(data);
        if (!(ASEval instanceof SubsetEvaluator)) {
            System.out.println(ASEval.getClass().getName()
                    + " is not a "
                    + "Subset evaluator!");
        }
        SubsetEvaluator ASEvaluator = (SubsetEvaluator) ASEval;

         if (ASEval instanceof UnsupervisedSubsetEvaluator) {
            //m_hasClass = false;
             numAttributes = data.numAttributes();
        } else {
             numAttributes = data.numAttributes() - 1;
            //m_hasClass = true;
            //m_classIndex = data.classIndex();
        }
        
        *//*if (m_hasClass) {
            i = m_numAttribs - 1;
        } else {
            i = m_numAttribs;
        }*//*


        HarmonyMemory harmonyMemory = new HarmonyMemory(memorySize, ensembleSize, numAttributes, m_Random);
        harmonyMemory.initialise();
        //int tempSize;
        //double tempMerit;

        //populate memory
        *//*for (int k = 0; k < memorySize; k++) {
            FeatureSubset fs = generateRandomSubset();
            fs.setMerit(ASEvaluator.evaluateSubset(fs));
            System.out.println("Adding New FeatureSubset " + fs.toString() + " - " + fs.getMerit() + " - " + featureSubsetHarmonyMemory.add(fs));
        }*//*
        //int ensembleSize = 15;
        //HarmonyMemory harmonyMemory =

        *//*for (int kk = 0; kk < ensembleSize; kk++) {
            //System.out.println("New NoteDomain " + kk);
            harmonyMemory.getExperts().add(new Musician(m_numAttribs));
            //System.out.println("New NoteDomain " + kk + " done");
        }*//*

        *//*int lastAdded = 0;
        int optimalAdded = 0;
        int addedCount = 0;
        int rejectedCount = 0;
        int initialSeedCount = 0;

        int noUpdate = 0;
        int noOptimal = 0;*//*
        int currentIteration = 0;

        while (harmonyMemory.size() < memorySize) {
            Harmony harmony = harmonyMemory.randomHarmony(discardThreshold);
            System.out.println(harmony.toString());
            BitSet bs = harmony.toBitSet();
            harmony.setMerit(ASEvaluator.evaluateSubset(bs));
            harmony.setCardinality(bs.cardinality());
            harmonyMemory.add(harmony);
            //initialSeedCount = initialSeedCount + 1;
            //System.out.println("Adding New Vote " + bs.toString() + " - " + vote.getMerit() + " - " + vote.cardinality() + " - " + harmonyMemory.add(vote));
        }

        // main loop
        while (currentIteration <= maxIteration) {

            Harmony harmony = harmonyMemory.newHarmony();
            //System.out.println("Added Vote " + vote.toString());
            BitSet bitSet = harmony.toBitSet();
            harmony.setMerit(ASEvaluator.evaluateSubset(bitSet));
            harmony.setCardinality(bitSet.cardinality());
            harmonyMemory.add(harmony);

            //Boolean added = harmonyMemory.add(vote);
            *//*if (added) {
                //System.out.println("Added Vote " + bitSet.toString() + " - " + vote.getMerit());
                addedCount = addedCount + 1;
                lastAdded = currentIteration;
                noUpdate = 0;
                if (harmonyMemory.comparator().compare(harmonyMemory.last(), vote) == 0) {
                    optimalAdded = currentIteration;
                    noOptimal = 0;
                }
                else {
                    noOptimal = noOptimal + 1;
                }
            } else {
                rejectedCount = rejectedCount + 1;
                noUpdate = noUpdate + 1;
                //System.out.println("Rejected Vote " + bitSet.toString() + " - " + vote.getMerit());
            }*//*

            currentIteration = currentIteration + 1;
            
        }
        int rank = 0;
        BitSet[] bitSets = new BitSet[harmonyMemory.size()];

        for (Harmony harmony : harmonyMemory) {
            BitSet bitSet = harmony.toBitSet();
            bitSets[rank] = bitSet;
            //System.out.println(getRemove(bitSet, m_classIndex));
            if (rank == memorySize - 1) System.out.println(rank + bitSet.toString() + " - " + harmony.getMerit());
            rank++;
        }        

        //System.out.println("Current Iteration = " + currentIteration);
        //System.out.println("Last Addition on the " + lastAdded + "-th Iteration");
        //System.out.println("Optimal Found on the " + optimalAdded + "-th Iteration");
        //System.out.println("Initial Seed " + initialSeedCount + " - Added " + addedCount + " - Rejected " + rejectedCount);
        return bitSets;
    }
*/

    //public 

    /*public BitSet searchBest(Instances data) throws Exception {

        int memorySize = 10;//m_Classifiers.length;
        int ensembleSize = 10;
        //double discardThreshold = 0.5;
        int maxIteration = 100;
        double HMCR = 0.8;
        FeatureSelectionHarmonyMemory harmonyMemory = new FeatureSelectionHarmonyMemory(memorySize, ensembleSize, data.numAttributes() - 1, HMCR, m_Random);

        ASEvaluation ASEval = new FuzzyRoughSubsetEval();
        ASEval.buildEvaluator(data);
        if (!(ASEval instanceof SubsetEvaluator)) {
            System.out.println(ASEval.getClass().getName()
                    + " is not a "
                    + "Subset evaluator!");
        }
        SubsetEvaluator subsetEvaluator = (SubsetEvaluator) ASEval;
        harmonyMemory.initialise(data, subsetEvaluator);

        while (harmonyMemory.size() < memorySize) {
            //harmonyMemory.evaluateAndAdd(harmonyMemory.randomHarmony(discardThreshold));
            harmonyMemory.evaluateAndAdd(harmonyMemory.randomHarmony());
        }

        int currentIteration = 0;
        while (currentIteration <= maxIteration) {
            harmonyMemory.evaluateAndAdd(harmonyMemory.newHarmony());
            currentIteration = currentIteration + 1;

        }

        int rank = 0;
        BitSet returnBitSet = null;
        for (Harmony harmony : harmonyMemory) {
            if (rank == memorySize - 1) {
                returnBitSet = (BitSet)harmonyMemory.getHarmonyEvaluator(0).translate(harmony);
            }
            rank++;
        }

        return returnBitSet;
    }
*/

    protected String randomSubSpace(Integer[] indices, int subSpaceSize, int classIndex, Random random) {
        Collections.shuffle(Arrays.asList(indices), random);
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < subSpaceSize; i++) {
            sb.append(indices[i] + ",");
        }
        sb.append(classIndex);

        if (getDebug())
            System.out.println("subSPACE = " + sb);

        return sb.toString();
    }

    protected String getRemove(BitSet bitSet, int classIndex) {
        StringBuffer b = new StringBuffer();
        //b.append('{');

        int i = bitSet.nextClearBit(0);
        //System.out.println(i);
        b.append(i + 1);
        i = i + 1;
        while (bitSet.nextClearBit(i) <= classIndex) {
            //System.out.println(i);
            i = bitSet.nextClearBit(i);
            b.append(",").append(i + 1);
            i = i + 1;
        }
        return b.toString();
    }

    /*private String getSubset(BitSet bitSet, int classIndex) {
        StringBuffer b = new StringBuffer();
        int i = bitSet.nextSetBit(0);
        b.append(i + 1);
        i = i + 1;
        while (bitSet.nextSetBit(i) <= classIndex) {
            i = bitSet.nextClearBit(i);
            b.append(",").append(i + 1);
            i = i + 1;
        }
        return b.toString();
        bitSet.toString();
    }*/

    public void setClassifier(int index, Classifier classifier) {
        m_Classifiers[index] = classifier;
    }

    /**
     * Classifies the given test instance.
     *
     * @param instance the instance to be classified
     * @return the predicted most likely class for the instance or
     *         Instance.missingValue() if no prediction is made
     * @throws Exception if an error occurred during the prediction
     */
    public double classifyInstance(Instance instance) throws Exception {
        double result;
        double[] dist;
        int index;

        switch (m_CombinationRule) {
            case AVERAGE_RULE:
            case PRODUCT_RULE:
            case MAJORITY_VOTING_RULE:
            case MIN_RULE:
            case MAX_RULE:
                dist = distributionForInstance(instance);
                if (instance.classAttribute().isNominal()) {
                    index = Utils.maxIndex(dist);
                    if (dist[index] == 0)
                        result = Utils.missingValue();
                    else
                        result = index;
                } else if (instance.classAttribute().isNumeric()) {
                    result = dist[0];
                } else {
                    result = Utils.missingValue();
                }
                break;
            case MEDIAN_RULE:
                result = classifyInstanceMedian(instance);
                break;
            default:
                throw new IllegalStateException("Unknown combination rule '" + m_CombinationRule + "'!");
        }

        return result;
    }

    /**
     * Classifies the given test instance, returning the median from all
     * classifiers.
     *
     * @param instance the instance to be classified
     * @return the predicted most likely class for the instance or
     *         Instance.missingValue() if no prediction is made
     * @throws Exception if an error occurred during the prediction
     */
    protected double classifyInstanceMedian(Instance instance) throws Exception {
        double[] results = new double[m_Classifiers.length];
        double result;

        for (int i = 0; i < results.length; i++)
            results[i] = m_Classifiers[i].classifyInstance(instance);

        if (results.length == 0)
            result = 0;
        else if (results.length == 1)
            result = results[0];
        else
            result = Utils.kthSmallestValue(results, results.length / 2);

        return result;
    }

    /**
     * Classifies a given instance using the selected combination rule.
     *
     * @param instance the instance to be classified
     * @return the distribution
     * @throws Exception if instance could not be classified
     *                   successfully
     */
    public double[] distributionForInstance(Instance instance) throws Exception {
        double[] result = new double[instance.numClasses()];

        switch (m_CombinationRule) {
            case AVERAGE_RULE:
                result = distributionForInstanceAverage(instance);
                break;
            case PRODUCT_RULE:
                result = distributionForInstanceProduct(instance);
                break;
            case MAJORITY_VOTING_RULE:
                result = distributionForInstanceMajorityVoting(instance);
                break;
            case MIN_RULE:
                result = distributionForInstanceMin(instance);
                break;
            case MAX_RULE:
                result = distributionForInstanceMax(instance);
                break;
            case MEDIAN_RULE:
                result[0] = classifyInstance(instance);
                break;
            default:
                throw new IllegalStateException("Unknown combination rule '" + m_CombinationRule + "'!");
        }

        if (!instance.classAttribute().isNumeric() && (Utils.sum(result) > 0))
            Utils.normalize(result);

        return result;
    }

    /**
     * Classifies a given instance using the Average of Probabilities
     * combination rule.
     *
     * @param instance the instance to be classified
     * @return the distribution
     * @throws Exception if instance could not be classified
     *                   successfully
     */
    protected double[] distributionForInstanceAverage(Instance instance) throws Exception {

        double[] probs = getClassifier(0).distributionForInstance(instance);
        for (int i = 1; i < m_Classifiers.length; i++) {
            double[] dist = getClassifier(i).distributionForInstance(instance);
            for (int j = 0; j < dist.length; j++) {
                probs[j] += dist[j];
            }
        }
        for (int j = 0; j < probs.length; j++) {
            probs[j] /= (double) m_Classifiers.length;
        }
        return probs;
    }

    /**
     * Classifies a given instance using the Product of Probabilities
     * combination rule.
     *
     * @param instance the instance to be classified
     * @return the distribution
     * @throws Exception if instance could not be classified
     *                   successfully
     */
    protected double[] distributionForInstanceProduct(Instance instance) throws Exception {

        double[] probs = getClassifier(0).distributionForInstance(instance);
        for (int i = 1; i < m_Classifiers.length; i++) {
            double[] dist = getClassifier(i).distributionForInstance(instance);
            for (int j = 0; j < dist.length; j++) {
                probs[j] *= dist[j];
            }
        }

        return probs;
    }

    /**
     * Classifies a given instance using the Majority Voting combination rule.
     *
     * @param instance the instance to be classified
     * @return the distribution
     * @throws Exception if instance could not be classified
     *                   successfully
     */
    protected double[] distributionForInstanceMajorityVoting(Instance instance) throws Exception {

        double[] probs = new double[instance.classAttribute().numValues()];
        double[] votes = new double[probs.length];

        for (int i = 0; i < m_Classifiers.length; i++) {
            probs = getClassifier(i).distributionForInstance(instance);
            int maxIndex = 0;
            for (int j = 0; j < probs.length; j++) {
                if (probs[j] > probs[maxIndex])
                    maxIndex = j;
            }

            // Consider the cases when multiple classes happen to have the same probability
            for (int j = 0; j < probs.length; j++) {
                if (probs[j] == probs[maxIndex])
                    votes[j]++;
            }
        }

        int tmpMajorityIndex = 0;
        for (int k = 1; k < votes.length; k++) {
            if (votes[k] > votes[tmpMajorityIndex])
                tmpMajorityIndex = k;
        }

        // Consider the cases when multiple classes receive the same amount of votes
        Vector<Integer> majorityIndexes = new Vector<Integer>();
        for (int k = 0; k < votes.length; k++) {
            if (votes[k] == votes[tmpMajorityIndex])
                majorityIndexes.add(k);
        }
        // Resolve the ties according to a uniform random distribution
        int majorityIndex = majorityIndexes.get(m_Random.nextInt(majorityIndexes.size()));

        //set probs to 0
        for (int k = 0; k < probs.length; k++)
            probs[k] = 0;
        probs[majorityIndex] = 1; //the class that have been voted the most receives 1

        return probs;
    }

    /**
     * Classifies a given instance using the Maximum Probability combination rule.
     *
     * @param instance the instance to be classified
     * @return the distribution
     * @throws Exception if instance could not be classified
     *                   successfully
     */
    protected double[] distributionForInstanceMax(Instance instance) throws Exception {

        double[] max = getClassifier(0).distributionForInstance(instance);
        for (int i = 1; i < m_Classifiers.length; i++) {
            double[] dist = getClassifier(i).distributionForInstance(instance);
            for (int j = 0; j < dist.length; j++) {
                if (max[j] < dist[j])
                    max[j] = dist[j];
            }
        }

        return max;
    }

    /**
     * Classifies a given instance using the Minimum Probability combination rule.
     *
     * @param instance the instance to be classified
     * @return the distribution
     * @throws Exception if instance could not be classified
     *                   successfully
     */
    protected double[] distributionForInstanceMin(Instance instance) throws Exception {

        double[] min = getClassifier(0).distributionForInstance(instance);
        for (int i = 1; i < m_Classifiers.length; i++) {
            double[] dist = getClassifier(i).distributionForInstance(instance);
            for (int j = 0; j < dist.length; j++) {
                if (dist[j] < min[j])
                    min[j] = dist[j];
            }
        }

        return min;
    }

    /**
     * Returns the tip text for this property
     *
     * @return tip text for this property suitable for
     *         displaying in the explorer/experimenter gui
     */
    public String combinationRuleTipText() {
        return "The combination rule used.";
    }

    /**
     * Gets the combination rule used
     *
     * @return the combination rule used
     */
    public SelectedTag getCombinationRule() {
        return new SelectedTag(m_CombinationRule, TAGS_RULES);
    }

    /**
     * Sets the combination rule to use. Values other than
     *
     * @param newRule the combination rule method to use
     */
    public void setCombinationRule(SelectedTag newRule) {
        if (newRule.getTags() == TAGS_RULES)
            m_CombinationRule = newRule.getSelectedTag().getID();
    }

    /**
     * Output a representation of this classifier
     *
     * @return a string representation of the classifier
     */
    public String toString() {

        if (m_Classifiers == null) {
            return "Vote: No model built yet.";
        }

        String result = "Vote combines";
        result += " the probability distributions of these base learners:\n";
        for (int i = 0; i < m_Classifiers.length; i++) {
            result += '\t' + getClassifierSpec(i) + '\n';
        }
        result += "using the '";

        switch (m_CombinationRule) {
            case AVERAGE_RULE:
                result += "Average of Probabilities";
                break;

            case PRODUCT_RULE:
                result += "Product of Probabilities";
                break;

            case MAJORITY_VOTING_RULE:
                result += "Majority Voting";
                break;

            case MIN_RULE:
                result += "Minimum Probability";
                break;

            case MAX_RULE:
                result += "Maximum Probability";
                break;

            case MEDIAN_RULE:
                result += "Median Probability";
                break;

            default:
                throw new IllegalStateException("Unknown combination rule '" + m_CombinationRule + "'!");
        }

        result += "' combination rule \n";

        return result;
    }

    /**
     * Returns the revision string.
     *
     * @return the revision
     */
    public String getRevision() {
        return RevisionUtils.extract("$Revision: 1.19 $");
    }

    /**
     * Main method for testing this class.
     *
     * @param argv should contain the following arguments:
     *             -t training file [-T test file] [-c class index]
     */
    public static void main(String[] argv) {
        runClassifier(new HarmonyEnsemble(), argv);
    }
}