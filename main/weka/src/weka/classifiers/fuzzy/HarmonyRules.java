package weka.classifiers.fuzzy;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 10/01/12
 * Time: 10:42
 * To change this template use File | Settings | File Templates.
 */

import ruleinduction.Harmony;
import ruleinduction.HarmonyMemory;
import ruleinduction.ValueRange;
import weka.attributeSelection.FuzzyRoughSubsetEval;
import weka.attributeSelection.HarmonySearch;
import weka.core.Attribute;
import weka.core.Capabilities;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Option;
import weka.core.OptionHandler;
import weka.core.TechnicalInformation;
import weka.core.TechnicalInformationHandler;
import weka.core.Utils;
import weka.core.Capabilities.Capability;
import weka.core.TechnicalInformation.Field;
import weka.core.TechnicalInformation.Type;
import weka.fuzzy.implicator.*;
import weka.fuzzy.tnorm.*;
import weka.fuzzy.similarity.*;

import java.util.*;


/**
 * <!-- globalinfo-start -->
 * Fuzzy-rough nearest-neighbour classifier. <br/>
 * <br/>
 * <p/>
 * <p/>
 * <!-- globalinfo-end -->
 * <p/>
 * <p/>
 * <!-- options-start -->
 * Valid options are: <p/>
 * <p/>
 * <pre> -D
 *  If set, classifier is run in debug mode and
 *  may output additional info to the console</pre>
 * <p/>
 * <!-- options-end -->
 *
 * @author Richard Jensen
 * @version $Revision: 1.19 $
 */
public class HarmonyRules
        extends FuzzyRoughRuleInducer
        implements OptionHandler,
        TechnicalInformationHandler {

    /**
     * for serialization.
     */
    static final long serialVersionUID = -3080186098777067172L;
    public Implicator m_Implicator = new ImplicatorKD();

    //whether to use the weak positive region or not
    public boolean m_weak = true;

    public HarmonyRules() {
        init();
    }

    /**
     * Returns a string describing classifier.
     *
     * @return a description suitable for
     *         displaying in the explorer/experimenter gui
     */
    public String globalInfo() {

        return "Fuzzy-rough rule induction.\n\n"
                + "For more information, see\n\n"
                + getTechnicalInformation().toString();
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

        result = new TechnicalInformation(Type.CONFERENCE);
        result.setValue(Field.AUTHOR, "R. Jensen and C. Cornelis");
        result.setValue(Field.YEAR, "2008");
        result.setValue(Field.TITLE, "A New Approach to Fuzzy-Rough Nearest Neighbour Classification");
        result.setValue(Field.BOOKTITLE, "6th International Conference on Rough Sets and Current Trends in Computing");
        result.setValue(Field.PAGES, "310-319");

        return result;
    }


    /**
     * Get the number of training instances the classifier is currently using.
     *
     * @return the number of training instances the classifier is currently using
     */
    public int getNumTraining() {

        return m_numInstances;
    }

    /**
     * Returns default capabilities of the classifier.
     *
     * @return the capabilities of this classifier
     */
    public Capabilities getCapabilities() {
        Capabilities result = super.getCapabilities();

        // attributes
        result.enable(Capability.NOMINAL_ATTRIBUTES);
        result.enable(Capability.NUMERIC_ATTRIBUTES);
        result.enable(Capability.MISSING_VALUES);

        // class
        result.enable(Capability.NOMINAL_CLASS);
        result.enable(Capability.NUMERIC_CLASS);
        result.enable(Capability.MISSING_CLASS_VALUES);

        // instances
        result.setMinimumNumberInstances(0);

        return result;
    }


    /**
     * Returns an enumeration describing the available options.
     *
     * @return an enumeration of all the available options.
     */
    public Enumeration<Option> listOptions() {

        Vector<Option> newVector = new Vector<Option>(8);

        newVector.addElement(new Option(
                "\tNumber of nearest neighbours (k) used in classification.\n" +
                        "\t(Default = 1)",
                "K", 1, "-K <number of neighbors>"));
        newVector.addElement(new Option(
                "\tSelect the number of nearest neighbours between 1\n" +
                        "\tand the k value specified using hold-one-out evaluation\n" +
                        "\ton the training data (use when k > 1)",
                "X", 0, "-X"));
        newVector.addElement(new Option(
                "\tThe nearest neighbour search algorithm to use " +
                        "(default: weka.core.neighboursearch.LinearNNSearch).\n",
                "A", 0, "-A"));

        return newVector.elements();
    }

    /**
     * Parses a given list of options. <p/>
     * <p/>
     * <!-- options-start -->
     * Valid options are: <p/>
     * <p/>
     * <!-- options-end -->
     *
     * @param options the list of options as an array of strings
     * @throws Exception if an option is not supported
     */
    public void setOptions(String[] options) throws Exception {

        String optionString = Utils.getOption('I', options);
        if (optionString.length() != 0) {
            String nnSearchClassSpec[] = Utils.splitOptions(optionString);
            if (nnSearchClassSpec.length == 0) {
                throw new Exception("Invalid Implicator specification string.");
            }
            String className = nnSearchClassSpec[0];
            nnSearchClassSpec[0] = "";

            setImplicator((Implicator) Utils.forName(Implicator.class, className, nnSearchClassSpec));
        } else {
            setImplicator(new ImplicatorLukasiewicz());
        }


        optionString = Utils.getOption('T', options);
        if (optionString.length() != 0) {
            String nnSearchClassSpec[] = Utils.splitOptions(optionString);
            if (nnSearchClassSpec.length == 0) {
                throw new Exception("Invalid TNorm specification string.");
            }
            String className = nnSearchClassSpec[0];
            nnSearchClassSpec[0] = "";

            setTNorm((TNorm) Utils.forName(TNorm.class, className, nnSearchClassSpec));
        } else {
            setTNorm(new TNormLukasiewicz());
        }

        optionString = Utils.getOption('R', options);
        if (optionString.length() != 0) {
            String nnSearchClassSpec[] = Utils.splitOptions(optionString);
            if (nnSearchClassSpec.length == 0) {
                throw new Exception("Invalid Similarity specification string.");
            }
            String className = nnSearchClassSpec[0];
            nnSearchClassSpec[0] = "";

            setSimilarity((Similarity) Utils.forName(Similarity.class, className, nnSearchClassSpec));
        } else {
            setSimilarity(new Similarity3());
        }

        setWeak(Utils.getFlag('W', options));

        optionString = Utils.getOption('P', options);
        if (optionString.length() != 0) {
            setPruning(Integer.parseInt(optionString));
        } else {
            setPruning(0);
        }

        Utils.checkForRemainingOptions(options);
    }

    public void setPruning(int w) {
        m_Pruning = w;
    }

    public int getPruning() {
        return m_Pruning;
    }


    public void setWeak(boolean w) {
        m_weak = w;
    }

    public boolean getWeak() {
        return m_weak;
    }

    public void setImplicator(Implicator impl) {
        m_Implicator = impl;
    }

    public Implicator getImplicator() {
        return m_Implicator;
    }

    //set the relation composition operator = tnorm
    public void setTNorm(TNorm tnorm) {
        m_TNorm = tnorm;
    }

    public TNorm getTNorm() {
        return m_TNorm;
    }

    public Similarity getSimilarity() {
        return m_Similarity;
    }

    public void setSimilarity(Similarity s) {
        m_Similarity = s;
    }

    /**
     * Gets the current settings of IBk.
     *
     * @return an array of strings suitable for passing to setOptions()
     */
    public String[] getOptions() {

        String[] options = new String[15];
        int current = 0;

        options[current++] = "-I";
        options[current++] = (m_Implicator.getClass().getName() + " " +
                Utils.joinOptions(m_Implicator.getOptions())).trim();

        options[current++] = "-T";
        options[current++] = (m_TNorm.getClass().getName() + " " +
                Utils.joinOptions(m_TNorm.getOptions())).trim();

        options[current++] = "-R";
        options[current++] = (m_Similarity.getClass().getName() + " " + Utils.joinOptions(m_Similarity.getOptions())).trim();

        if (getWeak()) {
            options[current++] = "-W";
        }

        options[current++] = "-P";
        options[current++] = "" + getPruning();

        while (current < options.length) {
            options[current++] = "";
        }

        return options;
    }

    /**
     * Returns a description of this classifier.
     *
     * @return a description of this classifier as a string.
     */
    public String toString() {

        if (m_Train == null) {
            return "QuickRules: No model built yet.";
        }

        String result = "QuickRules\n" +
                "Fuzzy-rough rule induction\n";
        StringBuffer text = new StringBuffer();
        text.append("\nSimilarity measure: " + m_Similarity);
        text.append("\nImplicator: " + m_Implicator);
        text.append("\nT-Norm: " + m_TNorm + "\nRelation composition: " + m_Similarity.getTNorm());
        text.append("\n\nNumber of rules: " + cands.size());
        text.append("\nAverage rule arity: " + arity);

        result += text.toString();

        return result;
    }

    /**
     * Initialise scheme variables.
     */
    protected void init() {
        m_TNorm = new TNormKD();
        m_Implicator = new ImplicatorKD();
        m_Similarity = new Similarity1();

    }

    /**
     * Generates the classifier.
     *
     * @param instances set of instances serving as training data
     * @throws Exception if the classifier has not been generated successfully
     */
    public void buildClassifier(Instances instances) throws Exception {

        // can classifier handle the data?
        getCapabilities().testWithFail(instances);

        // remove instances with missing class
        instances = new Instances(instances);
        instances.deleteWithMissingClass();

        m_numClasses = instances.numClasses();
        m_ClassType = instances.classAttribute().type();
        m_Train = new Instances(instances, 0, instances.numInstances());

        m_Similarity.setInstances(m_Train);
        m_SimilarityEq.setInstances(m_Train);

        //m_composition = m_Similarity.getTNorm();
        m_SNorm = m_TNorm.getAssociatedSNorm();
        m_classIndex = m_Train.classIndex();
        m_numInstances = m_Train.numInstances();

        arity = 0;

        induce(0);
    }

    /**
     * Induce (and store) rules via QuickRules
     */
    public void induce(double full) {

        cands = new ArrayList<>(m_numInstances);

        Integer[] features = new Integer[0];
        try {
            features = harmonyFeatureSelection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        features = fullFeatures();

        HarmonyMemory memory = new HarmonyMemory(ValueRange.createStaticParameters(10, 0.8, 0.5, 0.1), m_Train, features);
        memory.fill();
        memory.iterate(100);

        Harmony best = memory.best();
        cands = memory.getComparator().translate(best);

        memory.getComparator().evaluateVerbose(best);

        for (int i = 0; i < m_Pruning; i++) prune();

        System.gc();
    }
    
    public Integer[] fullFeatures() {
        Integer[] features = new Integer[m_Train.numAttributes() - 1];
        for (int i = 0; i < m_Train.numAttributes() - 1; i++) {
            features[i] = i;
        }
        return features;
    }

    public Integer[] harmonyFeatureSelection() throws Exception {
        FuzzyRoughSubsetEval evaluator = new FuzzyRoughSubsetEval();
        evaluator.buildEvaluator(m_Train);

        HarmonySearch featureSelection = new HarmonySearch();
        int[] intFeatures = featureSelection.search(evaluator, m_Train);
        Integer[] features = new Integer[intFeatures.length];
        for (int i = 0; i < intFeatures.length; i++) {
            features[i] = intFeatures[i];
        }
        return features;
    }

    public void debug() {
        if (m_Debug) {
            Iterator<FuzzyRule> it = cands.iterator();
            FuzzyRule rule;

            while (it.hasNext()) {
                rule = it.next();
                arity += rule.features.cardinality();
            }
            arity /= cands.size();
            System.err.println("Finished induction: " + cands.size() + " rule(s)");
            System.err.println("Average rule arity = " + arity + "\n");
        }
    }

    /**
     * Calculates the class membership probabilities for the given test instance.
     *
     * @param instance the instance to be classified
     * @return predicted class probability distribution
     * @throws Exception if an error occurred during the prediction
     */
    public double[] distributionForInstance(Instance instance) throws Exception {

        if (m_numInstances == 0) {
            throw new Exception("No training instances!");
        }


        double[] distribution = null;
        if (m_ClassType == Attribute.NOMINAL) distribution = new double[m_numClasses];
        else distribution = new double[m_numInstances];

        double total = 0;
        double bestValue = -1;
        int bestClass = -1;

        //System.err.println(neighbours.numInstances());

        if (m_ClassType == Attribute.NOMINAL) {
            distribution = new double[m_numClasses];

            Iterator<FuzzyRule> it = cands.iterator();
            FuzzyRule rule;

            while (it.hasNext()) {
                rule = it.next();
                double value = 1;

                for (int a = rule.features.nextSetBit(0); a >= 0; a = rule.features.nextSetBit(a + 1)) {
                    if (a != m_classIndex) {
                        //value = Math.min(fuzzySimilarity(a,m_Train.instance(rule.object).value(a),instance.value(a)), value);
                        value = m_composition.calculate(fuzzySimilarity(a, m_Train.instance(rule.instance).value(a), instance.value(a)), value);
                        if (value == 0) break;
                    }
                }

                distribution[(int) m_Train.instance(rule.instance).classValue()] += value;
                total += value;


                /*if (value>bestValue) {
                        bestValue=value;
                        bestClass = (int)m_Train.instance(rule.object).value(m_classIndex);
                    }
                    if (bestValue==1) break;*/
            }
            //if (bestClass>-1) distribution[bestClass]=1;

            if (total > 0) Utils.normalize(distribution, total);
        } else {//if (m_ClassType == Attribute.NUMERIC) {
            double denom = 0;
            double num = 0;

            Iterator<FuzzyRule> it = cands.iterator();
            FuzzyRule rule;

            while (it.hasNext()) {
                rule = it.next();
                double value = 1;

                for (int a = rule.features.nextSetBit(0); a >= 0; a = rule.features.nextSetBit(a + 1)) {
                    if (a != m_classIndex) {
                        value = m_composition.calculate(fuzzySimilarity(a, m_Train.instance(rule.instance).value(a), instance.value(a)), value);
                        if (value == 0) break;
                    }
                }

                num += value * m_Train.instance(rule.instance).value(m_classIndex);
                denom += value;
            }
            distribution[0] = num / denom;
        }

        return distribution;
    }


    /**
     * Main method for testing this class.
     *
     * @param argv should contain command line options (see setOptions)
     */
    public static void main(String[] argv) {
        runClassifier(new QuickRules(), argv);
    }
}
