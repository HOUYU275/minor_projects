package weka.classifiers.fuzzy;

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
 *    FuzzyRoughNN.java
 *
 */


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
public class QuickRules
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

    public QuickRules() {
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
        //m_DecisionSimilarity.setInstances(m_Train);
        //m_SimilarityEq.setInstances(m_Train);

        //m_composition = m_Similarity.getTNorm();
        m_SNorm = m_TNorm.getAssociatedSNorm();
        m_classIndex = m_Train.classIndex();
        m_numInstances = m_Train.numInstances();

        globallowers = new double[m_numInstances];
        global = new double[m_numInstances];

        arity = 0;
        current = new Relation(m_numInstances);
        temprel = new Relation(m_numInstances);
        initCurrent();

        fullData = true;
        //work out the consistency
        BitSet full = new BitSet(m_Train.numAttributes());
        //for (int a=0;a<m_Train.numAttributes();a++) if (a!=m_classIndex) full.set(a);
        full.set(0, m_Train.numAttributes());
        full.clear(m_classIndex);
        //generatePartition(full);
        for (int a = 0; a < m_Train.numAttributes(); a++) {
            if (a != m_classIndex) {
                generatePartition(current, a);
                setCurrent(temprel);
            }
        }
        //current.printCells();
        indexes = new int[m_numInstances];
        //prepare optimization information
        decIndexes = new int[m_numInstances];

        if (!m_weak) {
            for (int i = 0; i < m_numInstances; i++) {
                boolean same = false;

                for (int j = i + 1; j < m_numInstances; j++) {

                    for (int d = 0; d < m_numInstances; d++) {
                        double decSim1 = fuzzySimilarity(m_classIndex, m_Train.instance(i).value(m_classIndex), m_Train.instance(d).value(m_classIndex));
                        double decSim2 = fuzzySimilarity(m_classIndex, m_Train.instance(j).value(m_classIndex), m_Train.instance(d).value(m_classIndex));

                        if (decSim1 != decSim2) {
                            same = false;
                            break;
                        } else same = true;

                    }
                    if (same) {
                        decIndexes[j] = i + 1;
                        break;
                    } // i+1 as default is 0
                }
            }
        }

        double fullGamma;

        if (m_ClassType != Attribute.NOMINAL && !m_weak) fullGamma = calculateGamma2(current, full);
        else fullGamma = calculateGamma(current, full);

        fullData = false;

        if (m_Pruning < 0 || m_Pruning > 10) m_Pruning = 0;
        induce(fullGamma);
    }

    /**
     * Induce (and store) rules via QuickRules
     */
    public void induce(double full) {
        initCurrent();
        cands = new ArrayList<FuzzyRule>(m_numInstances);
        BitSet reduct = new BitSet(m_Train.numAttributes());
        double gamma = -1;
        int bestAttr = -1;

        while ((float) gamma != (float) full) {
            bestAttr = -1;
            for (int a = 0; a < m_Train.numAttributes(); a++) {
                if (a != m_classIndex && !reduct.get(a)) {
                    reduct.set(a);
                    generatePartition(current, a);
                    double gam;
                    if (m_ClassType != Attribute.NOMINAL && !m_weak) gam = calculateGamma2(temprel, reduct);
                    else gam = calculateGamma(temprel, reduct);

                    //System.err.println(reduct+" => "+gam);
                    reduct.clear(a);

                    if (gam >= gamma) {
                        gamma = gam;
                        bestAttr = a;
                    }
                }
            }

            if (bestAttr == -1) break;

            reduct.set(bestAttr);

            if (m_Debug) System.err.println(reduct + " => " + ((float) gamma / (float) full));
            if (allCovered()) break;

            generatePartition(current, bestAttr);
            setCurrent(temprel);
        }

        for (int i = 0; i < m_Pruning; i++) prune();

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

        FuzzyRoughCalculator calculator = new FuzzyRoughCalculator(m_Train);
        double[] myLower = calculator.getLowerApproximations(current);
        double[] globalLower = calculator.getGlobalLowerApproximation();
        int approximateCounter = 0;
        double approximateScore = 0d;
        for (int i = 0; i < myLower.length; i++) {
            if (globalLower[i] != 0) {
                approximateCounter++;
                approximateScore += myLower[i] / globalLower[i];
            }
        }
        double[] coverage = calculator.getCoverage(cands);
        double coverageSum = 0d;
        for (int i = 0; i < coverage.length; i++) {
            coverageSum += coverage[i];
        }
        coverageSum = coverageSum / coverage.length;
        int cardinalitySum = 0;
        for (int i = 0; i < cands.size(); i++) {
            cardinalitySum += cands.get(i).getFeatures().cardinality();
        }

        System.out.println("|" + "Coverage Score: " +  coverageSum);
        System.out.println("|" + "Approximate Score: " + approximateScore / approximateCounter);
        System.out.println("|" + "Size Score: (" + (cands.size() + 0d) / m_Train.numInstances() + ")");
        System.out.println("|" + "Cardinality Score: (" + (cardinalitySum + 0d) / (cands.size() * (m_Train.numAttributes()-1)) + ")");

        int five = 0;
        int ten = 0;
        int twenty = 0;
        int thirty = 0;
        int fifty = 0;
        int over = 0;
        System.out.println("--------------------------------------------");
        for (FuzzyRule rule : cands) {
            int cardinality = rule.getFeatures().cardinality();
            if (cardinality < 5) five++;
            else if (cardinality < 10) ten++;
            else if (cardinality < 20) twenty++;
            else if (cardinality < 30) thirty++;
            else if (cardinality < 50) fifty++;
            else over++;
            System.out.println();
        }
        System.out.println("--------------------------------------------");
        System.out.println("five\t" + five);
        System.out.println("ten\t" + ten);
        System.out.println("twenty\t" + twenty);
        System.out.println("thirty\t" + thirty);
        System.out.println("fifty\t" + fifty);
        System.out.println("over\t" + over);
        System.out.println("--------------------------------------------");
    }

    public static Integer[] toArray(BitSet bitSet, int classIndex) {
        ArrayList<Integer> intArrayList = new ArrayList<Integer>();
        for (int i = bitSet.nextSetBit(0); i >= 0; i = bitSet.nextSetBit(i + 1)) {
            if (i != classIndex) intArrayList.add(i);
        }
        return intArrayList.toArray(new Integer[intArrayList.size()]);
    }


    public Integer[] randomDraft(Integer[] features) {
        Random random = new Random();
        Integer[] draft = new Integer[random.nextInt(features.length) + 1];
        draft[0] = features[random.nextInt(features.length)];
        for (int i = 1; i < draft.length; i++) {
            Integer feature = features[random.nextInt(features.length)];
            boolean exist = true;
            while (exist) {
                for (int j = 0; j < i; j++) {
                    if (draft[j] != feature) exist = false;
                }
            }
            draft[i] = feature;
        }
        return draft;
    }

    //weak version

    private final double calculateGamma(Relation rel, BitSet reduct) {
        double lower;
        double currLower = 0;
        double ret = 0;
        int d = -1;
        double[] eqClass;

        for (int x = 0; x < m_numInstances; x++) {
            d = x;//weak version, so only consider x's decision
            lower = 1;

            eqClass = new double[m_numInstances];

            //lower  approximations of object x
            for (int y = 0; y < m_numInstances; y++) {
                double val = rel.getCell(x, y);
                eqClass[y] = val;

                currLower = m_Implicator.calculate(val,
                        fuzzySimilarity(m_classIndex, m_Train.instance(d).value(m_classIndex), m_Train.instance(y).value(m_classIndex)));

                //inf               =-
                lower = Math.min(currLower, lower);

                if (lower == 0) break;
            }
            if ((float) lower == (float) globallowers[x] && global[x] < globallowers[x]) {
                determineAdd(new FuzzyRule(eqClass, (BitSet) reduct.clone(), x));
            }
            if (fullData) globallowers[x] = lower;
            ret += lower;

        }
        return ret / Double.valueOf(m_numInstances);
    }


    //full version
    private final double calculateGamma2(Relation rel, BitSet reduct) {
        double lower;
        double currLower = 0;
        double ret = 0;
        double[] eqClass;
        double val;
        vals = new double[m_numInstances];
        indexes = new int[m_numInstances];

        setIndexes(rel);

        for (int x = 0; x < m_numInstances; x++) {
            if (indexes[x] != 0) {
                vals[x] = vals[indexes[x] - 1];
            } else {
                val = 0;
                decVals = new double[m_numInstances];


                for (int d = 0; d < m_numInstances; d++) {
                    if (decIndexes[d] != 0) {
                        decVals[d] = decVals[decIndexes[d] - 1];
                    } else {
                        lower = 1;
                        eqClass = new double[m_numInstances];

                        // lower approximations of object x
                        for (int y = 0; y < m_numInstances; y++) {
                            double condSim = rel.getCell(x, y);
                            eqClass[y] = condSim;
                            currLower = m_Implicator.calculate(condSim, fuzzySimilarity(m_classIndex, m_Train.instance(d).value(m_classIndex), m_Train.instance(y).value(m_classIndex)));

                            lower = Math.min(currLower, lower);
                            if (lower == 0) break;
                        }
                        decVals[d] = lower;

                        //if (decVals[d]==globallowers[x][d]&&global[x]/globalpositives[x]<1) {
                        if (!fullData && (float) decVals[d] == (float) globallowers[x] && global[x] < globallowers[x]) {
                            determineAdd(new FuzzyRule(eqClass, (BitSet) reduct.clone(), x));
                        }
                    }
                    //if (fullData) globallowers[x][d]=decVals[d];
                    val = Math.max(decVals[d], val);


                    if (val == 1) break;
                }

                vals[x] = val;

            }
            if (fullData) globallowers[x] = vals[x];
            ret += vals[x];
        }


        return ret / Double.valueOf(m_numInstances);
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
