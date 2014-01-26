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

//import DecisionMatrix;

import util.ObjectSerialisation;
import util.Registry;
import weka.attributeSelection.ASEvaluation;
import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.GreedyStepwise;
import weka.attributeSelection.HarmonySearch;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.RandomizableMultipleClassifiersCombiner;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.fuzzy.FuzzyRoughNN;
import weka.classifiers.fuzzy.VQNN;
import weka.classifiers.lazy.IBk;
import weka.classifiers.rules.JRip;
import weka.classifiers.rules.PART;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.REPTree;
import weka.core.*;
import weka.core.Capabilities.Capability;
import weka.core.TechnicalInformation.Field;
import weka.core.TechnicalInformation.Type;
import weka.filters.unsupervised.attribute.Remove;

import java.io.*;
import java.util.*;

//import weka.classifiers.fuzzy.FuzzyRoughNN;

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
public class FeatureSelectionEnsemble extends RandomizableMultipleClassifiersCombiner implements TechnicalInformationHandler {

    private Classifier baseClassifier = new NaiveBayes();

    //private int ensembleSize = 50;
    //private int maxIteration = 500;
    private int poolSize = 100;
    //private int selectionMethod = 111;
    private int poolMethod = 0;
    private Classifier[] pool;
    private DecisionMatrix decisionMatrix;
    private boolean useFull = false;
    private boolean useRandom = false;
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
    public static final Tag[] TAGS_RULES = {new Tag(AVERAGE_RULE, "AVG", "Average of Probabilities"), new Tag(PRODUCT_RULE, "PROD", "Product of Probabilities"), new Tag(MAJORITY_VOTING_RULE, "MAJ", "Majority Voting"), new Tag(MIN_RULE, "MIN", "Minimum Probability"), new Tag(MAX_RULE, "MAX", "Maximum Probability"), new Tag(MEDIAN_RULE, "MED", "Median")};

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
    private boolean useCache = false;
    private String datasetName;
    private String ensembleMethod;
    private int fold;

    public ASEvaluation getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(ASEvaluation evaluation) {
        this.evaluation = evaluation;
    }

    private ASEvaluation evaluation;

    /**
     * Returns a string describing classifier
     *
     * @return a description suitable for
     *         displaying in the explorer/experimenter gui
     */
    public String globalInfo() {
        return "Class for combining classifiers. Different combinations of " + "probability estimates for classification are available.\n\n" + "For more information see:\n\n" + getTechnicalInformation().toString();
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

        result.addElement(new Option("\tThe combination rule to use\n" + "\t(default: AVG)", "R", 1, "-R " + Tag.toOptionList(TAGS_RULES)));

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

    public boolean isUseRandom() {
        return useRandom;
    }

    public void setUseRandom(boolean useRandom) {
        this.useRandom = useRandom;
    }

    /**
     * Returns default capabilities of the classifier.
     *
     * @return the capabilities of this classifier
     */
    public Capabilities getCapabilities() {
        Capabilities result = super.getCapabilities();

        // class
        if ((m_CombinationRule == PRODUCT_RULE) || (m_CombinationRule == MAJORITY_VOTING_RULE)) {
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

    public void setCacheProperties(String datasetName, Classifier classifier, String ensembleMethod, int poolSize, int fold) {
        useCache = true;
        this.datasetName = datasetName;
        this.baseClassifier = classifier;
        this.ensembleMethod = ensembleMethod;
        this.poolSize = poolSize;
        this.fold = fold;
    }

    /**
     * Buildclassifier selects a classifier from the set of classifiers
     * by minimising error on the training data.
     *
     * @param data the training data to be used for generating the
     *             boosted classifier.
     * @throws Exception if the classifier could not be built successfully
     */
    public void buildClassifier(Instances data) throws Exception {
        getCapabilities().testWithFail(data);
        Instances newData = new Instances(data);
        newData.deleteWithMissingClass();

        m_Random = new Random(new Random().nextLong());
        Classifier[] selectedClassifiers = null;

        if (useCache) {
            String cachePath = Registry.ensembleCachePath + datasetName + "\\" + baseClassifier.getClass().getName() + "\\" + ensembleMethod + "\\" + poolSize + "\\" + fold + "\\";
            if (useFull) {
                selectedClassifiers = new Classifier[poolSize];
                for (int i = 0; i < poolSize; i++) {
                    selectedClassifiers[i] = ObjectSerialisation.importClassifier(cachePath + i + ".classifier");
                }
            } else {
                int[] selected = featureSelection(cachePath + "decision_matrix");
                selectedClassifiers = new Classifier[selected.length];
                for (int i = 0; i < selected.length; i++) {
                    selectedClassifiers[i] = ObjectSerialisation.importClassifier(cachePath + selected[i] + ".classifier");
                }
                System.out.print("ensemble size = " + selectedClassifiers.length);
            }
        } else

        {
            pool = generateBaseClassifiers(baseClassifier, poolSize, poolMethod, newData);
            buildDecisionMatrix(newData);

            if (useFull) {
                selectedClassifiers = new Classifier[pool.length];
                for (int i = 0; i < pool.length; i++) {
                    selectedClassifiers[i] = pool[i];
                }
                System.out.print("full ensemble = " + selectedClassifiers.length + " / " + pool.length);
            }
            else if (useRandom) {
                int[] selected = randomPick();

                selectedClassifiers = new Classifier[selected.length];
                for (int i = 0; i < selected.length; i++) {
                    selectedClassifiers[i] = pool[selected[i]];
                }
                System.out.print("random ensemble = " + selectedClassifiers.length + " / " + pool.length);
            }
            else {
                int[] selected = featureSelection(decisionMatrix.printWekaDataToFile());

                selectedClassifiers = new Classifier[selected.length];
                for (int i = 0; i < selected.length; i++) {
                    selectedClassifiers[i] = pool[selected[i]];
                }
                System.out.print("ensemble size = " + selectedClassifiers.length + " / " + pool.length);
            }
        }

        setClassifiers(selectedClassifiers);
        //setClassifiers(pool);
        System.gc();
    }

    /*public void buildClassifier(Instances data) throws Exception {
        // can classifier handle the data?
        getCapabilities().testWithFail(data);
        // remove instances with missing class
        Instances newData = new Instances(data);
        newData.deleteWithMissingClass();

        //m_Random = new Random(new Random().nextLong());
        m_Random = new Random(new Random().nextLong());
        //baseClassifier = m_Classifiers[0];
        //System.out.println("Building Classifiers");
        //System.out.println("Received data with " + data.numInstances() + " instances");

        //baseClassifiers = new Classifier[totalClassifiers];

        //if (pool == null)
        pool = generateBaseClassifiers(baseClassifier, poolSize, poolMethod, newData);
        //if (decisionMatrix == null)
        buildDecisionMatrix(newData);

        int[] selected = featureSelection(decisionMatrix.printWekaDataToFile());

        Classifier[] selectedClassifiers = new Classifier[selected.length];
        for (int i = 0; i < selected.length; i++) {
            selectedClassifiers[i] = pool[selected[i]];
            //System.out.println(selectedClassifiers[i].getClass().getName());
        }
        setClassifiers(selectedClassifiers);
        System.gc();
    }*/
    public int[] randomPick() {
        int limit = pool.length / 5;
        int[] selected = new int[limit];
        for (int i = 0; i < selected.length; i++) {
            selected[i] = m_Random.nextInt(pool.length);
        }
        return selected;
    }

    /*public int[] featureSelectionOld(String fileName) throws Exception {
        File file = new File(fileName + ".arff");
        BufferedReader reader = new BufferedReader(
                new FileReader(file));
        Instances tempData = new Instances(reader);
        reader.close();
        file.delete();
        tempData.setClassIndex(tempData.numAttributes() - 1);
        int[] selected;
        ASEvaluation ASEvaluation;
        HarmonySearch harmonySearch = new HarmonySearch();
        harmonySearch.setIteration(maxIteration);
        harmonySearch.setNumMusicians(ensembleSize);
        switch (selectionMethod) {
            case 111:
                ASEvaluation = new FuzzyRoughSubsetEval();
                ((FuzzyRoughSubsetEval) ASEvaluation).setSimilarity(new Similarity1());
                ((FuzzyRoughSubsetEval) ASEvaluation).setImplicator(new ImplicatorKDL());
                ASEvaluation.buildEvaluator(tempData);
                selected = harmonySearch.search(ASEvaluation, tempData);
                break;
            case 121:
                ASEvaluation = new FuzzyRoughSubsetEval();
                ((FuzzyRoughSubsetEval) ASEvaluation).setSimilarity(new Similarity2());
                ASEvaluation.buildEvaluator(tempData);
                selected = harmonySearch.search(ASEvaluation, tempData);
                break;
            case 131:
                ASEvaluation = new FuzzyRoughSubsetEval();
                ((FuzzyRoughSubsetEval) ASEvaluation).setSimilarity(new Similarity3());
                ASEvaluation.buildEvaluator(tempData);
                selected = harmonySearch.search(ASEvaluation, tempData);
                break;
            case 132:
                ASEvaluation = new FuzzyRoughSubsetEval();
                ((FuzzyRoughSubsetEval) ASEvaluation).setUnsupervised(true);
                ASEvaluation.buildEvaluator(tempData);
                selected = harmonySearch.search(ASEvaluation, tempData);
                break;
            case 3:
                ASEvaluation = new CfsSubsetEval();
                ASEvaluation.buildEvaluator(tempData);
                selected = harmonySearch.search(ASEvaluation, tempData);
                break;
            case 4:
                ASEvaluation = new ConsistencySubsetEval();
                ASEvaluation.buildEvaluator(tempData);
                selected = harmonySearch.search(ASEvaluation, tempData);
                break;
            case 5:
                ASEvaluation = new UnsupervisedFRFS();
                ASEvaluation.buildEvaluator(tempData);
                selected = harmonySearch.search(ASEvaluation, tempData);
                break;
            case 999:
                selected = new int[baseClassifiers.length];
                for (int i = 0; i < baseClassifiers.length; i++) {
                    selected[i] = i;
                }
                break;
            case 12345:
                selected = randomPick();
                break;
            default:
                selected = randomPick();
                break;
        }
        return selected;
    }*/

    public int[] featureSelection(String fileName) throws Exception {
        File file = new File(fileName + ".arff");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        Instances data = new Instances(reader);
        reader.close();
        //file.delete();
        data.setClassIndex(data.numAttributes() - 1);
        int[] selected;
        //ASEvaluation ASEvaluation = new CfsSubsetEval();
        if (evaluation == null) evaluation = new CfsSubsetEval();
        evaluation.buildEvaluator(data);
        HarmonySearch search = new HarmonySearch();

        //GreedyStepwise search = new GreedyStepwise();
        selected = search.search(evaluation, data);
        return selected;
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
        runClassifier(new FeatureSelectionEnsemble(), argv);
    }

    /*public int getSelectionMethod() {
        return selectionMethod;
    }

    public void setSelectionMethod(int selectionMethod) {
        this.selectionMethod = selectionMethod;
    }*/

    public int getPoolMethod() {
        return poolMethod;
    }

    public void setPoolMethod(int poolMethod) {
        this.poolMethod = poolMethod;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    public Classifier getBaseC() {
        return baseClassifier;
    }

    public void setBaseClassifier(Classifier baseC) {
        this.baseClassifier = baseC;
    }

    public Classifier[] getPool() {
        return pool;
    }

    public void setPool(Classifier[] pool) {
        this.pool = pool;
    }

    public Random getM_Random() {
        return m_Random;
    }

    public void setM_Random(Random m_Random) {
        this.m_Random = m_Random;
    }

    public Classifier[] generateBaseClassifiers(Classifier baseClassifier, int size, int poolMethod, Instances data) throws Exception {
        Classifier[] generatedBaseClassifiers = new Classifier[size];
        Classifier[] mixedBaseClassifiers;
        switch (poolMethod) {
            case 0:  //(Classifier) Class.forName(baseClassifier.getClass().getName()).newInstance()
                generatedBaseClassifiers = generateBagging(baseClassifier, generatedBaseClassifiers, data);
                break;
            case 1:
                RandomSubSpace randomSubSpace = new RandomSubSpace();
                randomSubSpace.setClassifier(baseClassifier);
                Integer[] indices = new Integer[data.numAttributes() - 1];
                int classIndex = data.classIndex();
                int offset = 0;
                for (int i = 0; i < indices.length + 1; i++) {
                    if (i != classIndex) {
                        indices[offset++] = i + 1;
                    }
                }
                int subSpaceSize = randomSubSpace.numberOfAttributes(indices.length, 0.75);
                Random random = data.getRandomNumberGenerator(m_Seed);

                for (int j = 0; j < generatedBaseClassifiers.length; j++) {
                    if (baseClassifier instanceof Randomizable) {
                        ((Randomizable) generatedBaseClassifiers[j]).setSeed(random.nextInt());
                    }
                    FilteredClassifier fc = new FilteredClassifier();
                    fc.setClassifier(baseClassifier);
                    Remove rm = new Remove();
                    rm.setOptions(new String[]{"-V", "-R", randomSubSpace.randomSubSpace(indices, subSpaceSize, classIndex + 1, random)});
                    fc.setFilter(rm);
                    generatedBaseClassifiers[j] = fc;
                    generatedBaseClassifiers[j].buildClassifier(data);

                }
                break;
            /*case 1:
            for (int i = 0; i < size; i++) {
                //Classifier baseClassifier = new FuzzyNN();
                //Classifier baseClassifier = new JRip();
                //Classifier baseClassifier = new REPTree();
                Classifier base = (Classifier) Class.forName(baseClassifier.getClass().getName()).newInstance();
                FilteredClassifier fc = new FilteredClassifier();
                fc.setClassifier(base);
                Remove rm = new Remove();
                rm.setOptions(new String[]{"-V", "-R", Subspace.randomSubSpace(data.numAttributes(), data.classIndex(), new Random(new Random().nextInt()))});
                fc.setFilter(rm);
                generatedBaseClassifiers[i] = fc;
                generatedBaseClassifiers[i].buildClassifier(data);
            }
            break;*/
            /*case 3:
                mixedBaseClassifiers = new Classifier[]{
                        new MultilayerPerceptron(),
                        //new LibSVM(),
                        new FuzzyRoughNN(),
                        new VQNN(),
                        new IBk(),
                        new J48(),
                        new REPTree(),
                        new JRip(),
                        new PART(),
                        new NaiveBayes(),
                };
                for (int i = 0; i < size; i++) {
                    Classifier base =
                            (Classifier) Class.forName(
                                    mixedBaseClassifiers[i % mixedBaseClassifiers.length]
                                            .getClass().getName()).newInstance();
                    FilteredClassifier fc = new FilteredClassifier();
                    fc.setClassifier(base);
                    Remove rm = new Remove();
                    rm.setOptions(
                            new String[]{
                                    "-V",
                                    "-R",
                                    Subspace.randomSubSpace(
                                            data.numAttributes(),
                                            data.classIndex(),
                                            new Random(new Random().nextInt()))});
                    fc.setFilter(rm);
                    generatedBaseClassifiers[i] = fc;
                    generatedBaseClassifiers[i].buildClassifier(data);
                }
                break;*/
            case 4:
                mixedBaseClassifiers = new Classifier[]{
                        new MultilayerPerceptron(),
                        //new LibSVM(),
                        new FuzzyRoughNN(),
                        new VQNN(),
                        new IBk(),
                        new J48(),
                        new REPTree(),
                        new JRip(),
                        new PART(),
                        new NaiveBayes(),
                };
                for (int i = 0; i < size; i++) {
                    generatedBaseClassifiers[i] = (Classifier) Class.forName(
                            mixedBaseClassifiers[i % mixedBaseClassifiers.length]
                                    .getClass().getName()).newInstance();
                }
                generatedBaseClassifiers = generateMixedBagging(mixedBaseClassifiers, generatedBaseClassifiers, data);
                break;
            default:
                generatedBaseClassifiers = generateBagging(baseClassifier, generatedBaseClassifiers, data);
                break;
        }
        return generatedBaseClassifiers;
    }

    public void buildDecisionMatrix(Instances data) throws Exception {
        decisionMatrix = new DecisionMatrix(pool, data);
        decisionMatrix.build();
    }

    public Classifier[] generateBagging(Classifier classifier, Classifier[] classifiers, Instances data) throws Exception {
        boolean m_CalcOutOfBag = false;
        int m_BagSizePercent = 100;

        // can classifier handle the data?
        getCapabilities().testWithFail(data);

        // remove instances with missing class
        data = new Instances(data);
        data.deleteWithMissingClass();

        if (classifiers == null) {
            throw new Exception("A base classifier has not been specified!");
        } else {
            classifiers = AbstractClassifier.makeCopies(classifier, classifiers.length);
        }

        if (m_CalcOutOfBag && (m_BagSizePercent != 100)) {
            throw new IllegalArgumentException("Bag size needs to be 100% if " + "out-of-bag error is to be calculated!");
        }

        int bagSize = data.numInstances() * m_BagSizePercent / 100;
        Random random = new Random(new Random().nextInt());

        boolean[][] inBag = null;
        if (m_CalcOutOfBag)
            inBag = new boolean[classifiers.length][];

        for (int j = 0; j < classifiers.length; j++) {
            Instances bagData = null;

            // create the in-bag dataset
            if (m_CalcOutOfBag) {
                inBag[j] = new boolean[data.numInstances()];
                bagData = resampleWithWeights(data, random, inBag[j]);
            } else {
                bagData = data.resampleWithWeights(random);
                if (bagSize < data.numInstances()) {
                    bagData.randomize(random);
                    Instances newBagData = new Instances(bagData, 0, bagSize);
                    bagData = newBagData;
                }
            }

            if (classifier instanceof Randomizable) {
                ((Randomizable) classifiers[j]).setSeed(random.nextInt());
            }

            // build the classifier
            classifiers[j].buildClassifier(bagData);
        }
        return classifiers;
    }

    public Classifier[] generateMixedBagging(Classifier[] baseClassifiers, Classifier[] classifiers, Instances data) throws Exception {
        boolean m_CalcOutOfBag = false;
        int m_BagSizePercent = 100;

        // can classifier handle the data?
        getCapabilities().testWithFail(data);

        // remove instances with missing class
        data = new Instances(data);
        data.deleteWithMissingClass();



        if (classifiers == null) {
            throw new Exception("A base classifier has not been specified!");
        } else {
            for (int i = 0; i < classifiers.length; i++) {
                classifiers[i] = AbstractClassifier.makeCopy(baseClassifiers[m_Random.nextInt(baseClassifiers.length)]);
            }
        }

        if (m_CalcOutOfBag && (m_BagSizePercent != 100)) {
            throw new IllegalArgumentException("Bag size needs to be 100% if " + "out-of-bag error is to be calculated!");
        }

        int bagSize = data.numInstances() * m_BagSizePercent / 100;
        Random random = new Random(new Random().nextInt());

        boolean[][] inBag = null;
        if (m_CalcOutOfBag)
            inBag = new boolean[classifiers.length][];

        for (int j = 0; j < classifiers.length; j++) {
            Instances bagData = null;

            // create the in-bag dataset
            if (m_CalcOutOfBag) {
                inBag[j] = new boolean[data.numInstances()];
                bagData = resampleWithWeights(data, random, inBag[j]);
            } else {
                bagData = data.resampleWithWeights(random);
                if (bagSize < data.numInstances()) {
                    bagData.randomize(random);
                    Instances newBagData = new Instances(bagData, 0, bagSize);
                    bagData = newBagData;
                }
            }

            if (classifiers[j] instanceof Randomizable) {
                ((Randomizable) classifiers[j]).setSeed(random.nextInt());
            }

            // build the classifier
            classifiers[j].buildClassifier(bagData);
        }
        return classifiers;
    }

    public final Instances resampleWithWeights(Instances data, Random random, boolean[] sampled) {

        double[] weights = new double[data.numInstances()];
        for (int i = 0; i < weights.length; i++) {
            weights[i] = data.instance(i).weight();
        }
        Instances newData = new Instances(data, data.numInstances());
        if (data.numInstances() == 0) {
            return newData;
        }
        double[] probabilities = new double[data.numInstances()];
        double sumProbs = 0, sumOfWeights = Utils.sum(weights);
        for (int i = 0; i < data.numInstances(); i++) {
            sumProbs += random.nextDouble();
            probabilities[i] = sumProbs;
        }
        Utils.normalize(probabilities, sumProbs / sumOfWeights);

        // Make sure that rounding errors don't mess things up
        probabilities[data.numInstances() - 1] = sumOfWeights;
        int k = 0;
        int l = 0;
        sumProbs = 0;
        while ((k < data.numInstances() && (l < data.numInstances()))) {
            if (weights[l] < 0) {
                throw new IllegalArgumentException("Weights have to be positive.");
            }
            sumProbs += weights[l];
            while ((k < data.numInstances()) && (probabilities[k] <= sumProbs)) {
                newData.add(data.instance(l));
                sampled[l] = true;
                newData.instance(k).setWeight(1);
                k++;
            }
            l++;
        }
        return newData;
    }

    public void setUseFull(boolean useFull) {
        this.useFull = useFull;
    }

    class DecisionMatrix implements Serializable {

        private int numClassifiers;
        private int numInstances;
        private int numClasses;
        private int[][] decisions;
        private ArrayList<Integer>[] classLabels;
        private Classifier[] classifiers;
        private int[][] decisionCount;
        private int[][] agreementCount;
        private int[][] labelCount;
        private float[][] kappas;
        private Instances data;

        public DecisionMatrix() {

        }

        //public DecisionMatrix(int numClassifiers, int numInstances, int numClasses) {

        public DecisionMatrix(Classifier[] classifiers, Instances data) {
            this.classifiers = classifiers;
            this.numClassifiers = classifiers.length;
            this.data = data;
            this.numInstances = data.numInstances();
            this.numClasses = data.numClasses();
            decisions = new int[numClassifiers][numInstances];
            classLabels = new ArrayList[numClasses];
            for (int i = 0; i < numClasses; i++) {
                classLabels[i] = new ArrayList<Integer>();
            }
            //classifiers = new Classifier[numClassifiers];
            decisionCount = new int[numClassifiers][numClasses];
            agreementCount = new int[numClassifiers][numClassifiers];
            labelCount = new int[numInstances][numClasses];
            kappas = new float[numClassifiers][numClassifiers];
        }

        public void setDecisions(int index, int[] singleClassifierDecisions) throws Exception {
            if (decisions[index].length != singleClassifierDecisions.length)
                throw new Exception("Unmatched Instance Count");
            decisions[index] = singleClassifierDecisions;
        }

        public void printDecisions() {
            System.out.println("Decision Matrix numClassifiers = " + numClassifiers + " numInstances = " + numInstances);
            for (int i = 0; i < numClassifiers; i++) {
                for (int j = 0; j < numInstances; j++) {
                    System.out.print(" " + decisions[i][j]);
                }
                System.out.print("\n");
            }
        }

        public void printWekaData() {
            System.out.println("@RELATION Algae\n");
            for (int j = 0; j < numClassifiers; j++) {
                System.out.println("@ATTRIBUTE " + j + " REAL");
            }
            System.out.println("@ATTRIBUTE class {1,2}\n\n@DATA");
            for (int i = 0; i < numInstances; i++) {

                for (int j = 0; j < numClassifiers; j++) {
                    System.out.print(" " + decisions[j][i]);
                }

                System.out.print(" " + data.instance(i).classValue());
                System.out.print("\n");
            }
        }

        public void build() throws Exception {
            int[] singleClassifierDecisions;
            for (int i = 0; i < classifiers.length; i++) {
                singleClassifierDecisions = new int[data.numInstances()];
                for (int j = 0; j < data.numInstances(); j++) {
                    double[] prob = classifiers[i].distributionForInstance(data.instance(j));
                    int maxIndex = 0;
                    for (int k = 0; k < prob.length; k++) {
                        if (prob[k] > prob[maxIndex]) {
                            maxIndex = k;
                        }
                    }
                    singleClassifierDecisions[j] = maxIndex;
                }
                setDecisions(i, singleClassifierDecisions);
            }
        }

        public String printWekaDataToFile() {
            FileOutputStream out; // declare a file output object
            PrintStream p; // declare a println stream object

            try {
                String fileName = "tempWekaFile" + new Random().nextDouble();
                // Create a new file output stream
                // connected to "myfile.txt"
                out = new FileOutputStream(fileName + ".arff");

                // Connect println stream to the output stream
                p = new PrintStream(out);

                p.println("@RELATION Algae\n");
                for (int j = 0; j < numClassifiers; j++) {
                    p.println("@ATTRIBUTE " + j + " REAL");
                }
                p.print("@ATTRIBUTE class {");
                for (int i = 0; i < data.classAttribute().numValues(); i++) {
                    if (i == 0)
                        p.print(data.classAttribute().value(i));
                    else
                        p.print("," + data.classAttribute().value(i));
                }
                p.print("}\n\n@DATA\n");
                for (int i = 0; i < numInstances; i++) {

                    for (int j = 0; j < numClassifiers; j++) {
                        p.print(" " + decisions[j][i] + ".0");
                    }
                    //System.out.println();
                    p.print(" " + data.classAttribute().value((int) data.instance(i).classValue()));
                    p.print("\n");
                }

                //p.println ("This is written to a file");

                p.close();
                //System.out.println("File Output to \"" + fileName + "\"");
                return fileName;
            } catch (Exception e) {
                System.err.println("Error writing to file");
            }
            return null;
        }

        public void printInstanceLabels() {
            System.out.println("Instance Labels numClassifiers = " + numClassifiers + " numInstances = " + numInstances);
            for (int i = 0; i < numInstances; i++) {

                for (int j = 0; j < numClassifiers; j++) {
                    System.out.print(" " + decisions[j][i]);
                }

                System.out.print(" " + data.instance(i).classValue());
                System.out.print("\n");
            }
        }

        public void printDecisionCount() {
            System.out.println("Decision Count");
            for (int i = 0; i < numClassifiers; i++) {
                for (int j = 0; j < numClasses; j++) {
                    System.out.print(" " + decisionCount[i][j]);
                }
                System.out.print("\n");
            }
        }

        public void printAgreementCount() {
            System.out.println("Agreement Count");
            for (int i = 0; i < numClassifiers; i++) {
                for (int j = 0; j < numClassifiers; j++) {
                    System.out.print("\t" + agreementCount[i][j]);
                }
                System.out.print("\n");
            }
        }

        public void printKappas() {
            System.out.println("Kappas");
            for (int i = 0; i < numClassifiers; i++) {
                for (int j = 0; j < numClassifiers; j++) {
                    System.out.print("\t" + kappas[i][j]);
                }
                System.out.print("\n");
            }
        }

        public void countDecisions() {
            for (int i = 0; i < numClassifiers; i++) {
                for (int j = 0; j < numInstances; j++) {
                    decisionCount[i][decisions[i][j]] = decisionCount[i][decisions[i][j]] + 1;
                }
            }
        }

        private void countLabels() {
            labelCount = new int[numInstances][numClasses];
            for (int i = 0; i < numInstances; i++) {
                for (int j = 0; j < numClassifiers; j++) {
                    labelCount[i][decisions[j][i]] = labelCount[i][decisions[j][i]] + 1;
                }
            }
        }

        private void countLabels(int[] selected) {
            labelCount = new int[numInstances][numClasses];
            for (int i = 0; i < numInstances; i++) {
                for (int j = 0; j < selected.length; j++) {
                    labelCount[i][decisions[selected[j]][i]] = labelCount[i][decisions[selected[j]][i]] + 1;
                }
            }
        }

        public void printLabels() {
            for (int i = 0; i < numInstances; i++) {
                for (int j = 0; j < numClasses; j++) {
                    System.out.print(" " + labelCount[i][j]);
                }
                System.out.println();
            }
        }

        public double calculateEntropy() {
            countLabels();
            double entropy = 0d;
            double p = 0d;
            for (int i = 0; i < numClasses; i++) {
                for (int j = 0; j < numInstances; j++) {
                    p = (double) labelCount[j][i] / (double) numClassifiers;
                    //System.out.println(p + " " + p * Math.log(p));
                    entropy = (p == 0d) ? entropy : entropy - p * Math.log(p);
                }
            }
            //System.out.println("Entropy = " + entropy);
            return entropy;
        }

        public double calculateEntropy(int[] selected) {
            countLabels(selected);
            double entropy = 0d;
            double p = 0d;
            for (int i = 0; i < numClasses; i++) {
                for (int j = 0; j < numInstances; j++) {
                    p = (double) labelCount[j][i] / (double) selected.length;
                    //System.out.println(p + " " + p * Math.log(p));
                    entropy = (p == 0d) ? entropy : entropy - p * Math.log(p);
                }
            }
            //System.out.println("Entropy = " + entropy);
            return entropy;
        }

        public void countAgreements() {
            for (int j = 0; j < numInstances; j++) {
                for (int k = 0; k < numClasses; k++) {
                    classLabels[k].clear();
                }
                for (int i = 0; i < numClassifiers; i++) {
                    classLabels[decisions[i][j]].add(i);
                }
                for (int l = 0; l < numClasses; l++) {
                    for (int m = 0; m < classLabels[l].size(); m++) {
                        for (int n = 0; n < classLabels[l].size(); n++) {
                            agreementCount[classLabels[l].get(m)][classLabels[l].get(n)] = agreementCount[classLabels[l].get(m)][classLabels[l].get(n)] + 1;
                        }
                    }
                }
            }
        }

        //redundant runs atm

        public void calculateKappas() {
            float sigma1 = 0f;
            float sigma2 = 0f;
            float kappa = 0f;
            for (int i = 0; i < numClassifiers; i++) {
                for (int j = 0; j < numClassifiers; j++) {
                    sigma1 = agreementCount[i][j] / numInstances;
                    //sigma2 =
                    for (int k = 0; k < numClasses; k++) {
                        sigma2 = sigma2 + decisionCount[i][k] * decisionCount[j][k];
                    }
                    sigma2 = sigma2 / (numInstances * numInstances);
                    kappas[i][j] = (sigma1 - sigma2) / (1f - sigma2);
                }
            }
        }

        public void randomFill() {
            Random random = new Random();
            float roll;
            float interval = 1f / numClasses;
            float floored = 0f;
            int decision;
            for (int i = 0; i < numClassifiers; i++) {
                for (int j = 0; j < numInstances; j++) {
                    for (int k = 0; k < numClasses; k++) {
                        roll = random.nextFloat();
                        floored = (float) Math.floor(roll / interval);
                    }
                    decision = (int) floored;
                    if ((decision > 0) && (decision < numClasses - 1)) {
                        decision = (random.nextBoolean()) ? decision + random.nextInt(2) : decision - random.nextInt(2);
                    }
                    decisions[i][j] = decision;
                }
            }
        }

        public int[][] getDecisionCount() {
            return decisionCount;
        }

        public void setDecisionCount(int[][] decisionCount) {
            this.decisionCount = decisionCount;
        }

        public int[][] getAgreementCount() {
            return agreementCount;
        }

        public void setAgreementCount(int[][] agreementCount) {
            this.agreementCount = agreementCount;
        }

        public float[][] getKappas() {
            return kappas;
        }

        public void setKappas(float[][] kappas) {
            this.kappas = kappas;
        }

        public int getNumClassifiers() {
            return numClassifiers;
        }

        public void setNumClassifiers(int numClassifiers) {
            this.numClassifiers = numClassifiers;
        }

        public int getNumInstances() {
            return numInstances;
        }

        public void setNumInstances(int numInstances) {
            this.numInstances = numInstances;
        }

        public int getNumClasses() {
            return numClasses;
        }

        public void setNumClasses(int numClasses) {
            this.numClasses = numClasses;
        }

        public int[][] getDecisions() {
            return decisions;
        }

        public void setDecisions(int[][] decisions) {
            this.decisions = decisions;
        }

        /*public ClassLabel[] getClassLabels() {
            return classLabels;
        }

        public void setClassLabels(ClassLabel[] classLabels) {
            this.classLabels = classLabels;
        }*/

        public Classifier[] getClassifiers() {
            return classifiers;
        }

        public void setClassifiers(Classifier[] classifiers) {
            this.classifiers = classifiers;
        }

    }
}