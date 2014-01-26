package weka.attributeSelection;

import weka.core.*;

import java.security.SecureRandom;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 21-Oct-2009
 * Time: 12:44:47
 * To change this template use File | Settings | File Templates.
 */
public class EnsembleHarmonySearch extends ASSearch
        implements StartSetHandler, OptionHandler, TechnicalInformationHandler {

    //private FeatureSubsetHarmonyMemory featureSubsetHarmonyMemory;// = new FeatureSubsetHarmonyMemory(new FeatureSubsetComparator(), 15);
    private HarmonyMemory harmonyMemory;
    private int memorySize = 25;
    private double discardThreshold = 0.5;
    private double randomThreshold = 0.8;
    private int ensembleSize = 10;
    private int updateFrequency = 50;
    private int optimalFrequency = 100;
    private int hint = 0;

    private int iteration = 250;
    /**
     * for serialization
     */
    static final long serialVersionUID = 7479392617377425484L;

    /**
     * holds a starting set as an array of attributes.
     */
    private int[] m_starting;

    /**
     * holds the start set as a range
     */
    private Range m_startRange;

    /**
     * the best feature set found during the search
     */
    private BitSet m_bestGroup;

    /**
     * the merit of the best subset found
     */
    private double m_bestMerit;

    /**
     * only accept a feature set as being "better" than the best if its
     * merit is better or equal to the best, and it contains fewer
     * features than the best (this allows LVF to be implimented).
     */
    private boolean m_onlyConsiderBetterAndSmaller;

    /**
     * does the data have a class
     */
    private boolean m_hasClass;

    /**
     * holds the class index
     */
    private int m_classIndex;

    /**
     * number of attributes in the data
     */
    private int m_numAttribs;

    /**
     * seed for random number generation
     */
    private String m_seed;

    /**
     * percentage of the search space to consider
     */
    private double m_searchSize;

    /**
     * the number of iterations performed
     */
    private int m_iterations;

    /**
     * random number object
     */
    private SecureRandom m_random;

    /**
     * output new best subsets as the search progresses
     */
    private boolean m_verbose;

    /**
     * Returns a string describing this search method
     *
     * @return a description of the search suitable for
     *         displaying in the explorer/experimenter gui
     */
    public String globalInfo() {
        return "RandomSearch : \n\nPerforms a Random search in "
                + "the space of attribute subsets. If no start set is supplied, Random "
                + "search starts from a random point and reports the best subset found. "
                + "If a start set is supplied, Random searches randomly for subsets "
                + "that are as good or better than the start point with the same or "
                + "or fewer attributes. Using RandomSearch in conjunction with a start "
                + "set containing all attributes equates to the LVF algorithm of Liu "
                + "and Setiono (ICML-96).\n\n"
                + "For more information see:\n\n"
                + getTechnicalInformation().toString();
    }

    public int getOptimalFrequency() {
        return optimalFrequency;
    }

    public void setOptimalFrequency(int optimalFrequency) {
        this.optimalFrequency = optimalFrequency;
    }

    public int getUpdateFrequency() {
        return updateFrequency;
    }

    public void setUpdateFrequency(int updateFrequency) {
        this.updateFrequency = updateFrequency;
    }

    public int getHint() {
        return hint;
    }

    public void setHint(int hint) {
        this.hint = hint;
    }

    public String getM_seed() {
        return m_seed;
    }

    public void setM_seed(String m_seed) {
        this.m_seed = m_seed;
    }

    public int getEnsembleSize() {
        return ensembleSize;
    }

    public void setEnsembleSize(int ensembleSize) {
        this.ensembleSize = ensembleSize;
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

        result = new TechnicalInformation(TechnicalInformation.Type.INPROCEEDINGS);
        result.setValue(TechnicalInformation.Field.AUTHOR, "H. Liu and R. Setiono");
        result.setValue(TechnicalInformation.Field.TITLE, "A probabilistic approach to feature selection - A filter solution");
        result.setValue(TechnicalInformation.Field.BOOKTITLE, "13th International Conference on Machine Learning");
        result.setValue(TechnicalInformation.Field.YEAR, "1996");
        result.setValue(TechnicalInformation.Field.PAGES, "319-327");

        return result;
    }

    public double getRandomThreshold() {
        return randomThreshold;
    }

    public void setRandomThreshold(double randomThreshold) {
        this.randomThreshold = randomThreshold;
    }

    public double getDiscardThreshold() {
        return discardThreshold;
    }

    public void setDiscardThreshold(double discardThreshold) {
        this.discardThreshold = discardThreshold;
    }

    /**
     * Constructor
     */
    public EnsembleHarmonySearch() {
        resetOptions();
    }

    /**
     * Returns an enumeration describing the available options.
     *
     * @return an enumeration of all the available options.
     */
    public Enumeration listOptions() {
        Vector newVector = new Vector(12);

        newVector.addElement(new Option("\tSpecify a starting set of attributes."
                + "\n\tEg. 1,3,5-7."
                + "\n\tIf a start point is supplied,"
                + "\n\trandom search evaluates the start"
                + "\n\tpoint and then randomly looks for"
                + "\n\tsubsets that are as good as or better"
                + "\n\tthan the start point with the same"
                + "\n\tor lower cardinality."
                , "P", 1
                , "-P <start set>"));

        newVector.addElement(new Option("\tPercent of search space to consider."
                + "\n\t(default = 25%)."
                , "F", 1
                , "-F <percent> "));
        newVector.addElement(new Option("\tOutput subsets as the search progresses."
                + "\n\t(default = false)."
                , "V", 0
                , "-V"));
        newVector.addElement(new Option("\tTotal Iterations."
                + "\n\t(default = false)."
                , "I", 200
                , "-I"));

        newVector.addElement(new Option("\tMemory Size."
                + "\n\t(default = false)."
                , "M", 25
                , "-M"));
        newVector.addElement(new Option("\tDiscard Threshold."
                + "\n\t(default = false)."
                , "D", 50
                , "-D"));
        newVector.addElement(new Option("\tHint."
                + "\n\t(default = false)."
                , "H", 0
                , "-H"));
        newVector.addElement(new Option("\tRandom Threshold."
                + "\n\t(default = false)."
                , "R", 80
                , "-R"));
        newVector.addElement(new Option("\tEnsemble Size."
                + "\n\t(default = false)."
                , "S", 10
                , "-S"));
        newVector.addElement(new Option("\tUpdate Frequency."
                + "\n\t(default = false)."
                , "U", 50
                , "-U"));
        newVector.addElement(new Option("\tOptimal Frequency."
                + "\n\t(default = false)."
                , "O", 100
                , "-O"));
        newVector.addElement(new Option("\tSeed."
                + "\n\t(default = false)."
                , "Z", 0
                , "-Z"));
        //ensembleSize

        return newVector.elements();
    }

    public int getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(int memorySize) {
        this.memorySize = memorySize;
    }

    public int getIteration() {
        return iteration;
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }

    /**
     * Parses a given list of options. <p/>
     * <p/>
     * <!-- options-start -->
     * Valid options are: <p/>
     * <p/>
     * <pre> -P &lt;start set&gt;
     *  Specify a starting set of attributes.
     *  Eg. 1,3,5-7.
     *  If a start point is supplied,
     *  random search evaluates the start
     *  point and then randomly looks for
     *  subsets that are as good as or better
     *  than the start point with the same
     *  or lower cardinality.</pre>
     * <p/>
     * <pre> -F &lt;percent&gt;
     *  Percent of search space to consider.
     *  (default = 25%).</pre>
     * <p/>
     * <pre> -V
     *  Output subsets as the search progresses.
     *  (default = false).</pre>
     * <p/>
     * <!-- options-end -->
     *
     * @param options the list of options as an array of strings
     * @throws Exception if an option is not supported
     */
    public void setOptions(String[] options)
            throws Exception {
        String optionString;
        resetOptions();

        optionString = Utils.getOption('P', options);
        if (optionString.length() != 0) {
            setStartSet(optionString);
        }

        optionString = Utils.getOption('F', options);
        if (optionString.length() != 0) {
            setSearchPercent((new Double(optionString)).doubleValue());
        }

        optionString = Utils.getOption('I', options);
        if (optionString.length() != 0) {
            setIteration((new Integer(optionString)));
        }
        optionString = Utils.getOption('H', options);
        if (optionString.length() != 0) {
            setHint((new Integer(optionString)));
        }
        optionString = Utils.getOption('M', options);
        if (optionString.length() != 0) {
            setMemorySize((new Integer(optionString)));
        }
        optionString = Utils.getOption('S', options);
        if (optionString.length() != 0) {
            setEnsembleSize((new Integer(optionString)));
        }
        optionString = Utils.getOption('U', options);
        if (optionString.length() != 0) {
            setUpdateFrequency((new Integer(optionString)));
        }
        optionString = Utils.getOption('O', options);
        if (optionString.length() != 0) {
            setOptimalFrequency((new Integer(optionString)));
        }

        optionString = Utils.getOption('Z', options);
        if (optionString.length() != 0) {
            setM_seed((new String(optionString)));
        }

        optionString = Utils.getOption('D', options);
        if (optionString.length() != 0) {
            setDiscardThreshold((new Double(optionString)).doubleValue());
        }
        optionString = Utils.getOption('R', options);
        if (optionString.length() != 0) {
            setRandomThreshold((new Double(optionString)).doubleValue());
        }
        setVerbose(Utils.getFlag('V', options));
    }

    /**
     * Gets the current settings of RandomSearch.
     *
     * @return an array of strings suitable for passing to setOptions()
     */
    public String[] getOptions() {
        String[] options = new String[5];
        int current = 0;

        if (m_verbose) {
            options[current++] = "-V";
        }

        if (!(getStartSet().equals(""))) {
            options[current++] = "-P";
            options[current++] = "" + startSetToString();
        }

        options[current++] = "-F";
        options[current++] = "" + getSearchPercent();

        while (current < options.length) {
            options[current++] = "";
        }

        return options;
    }

    /**
     * Returns the tip text for this property
     *
     * @return tip text for this property suitable for
     *         displaying in the explorer/experimenter gui
     */
    public String startSetTipText() {
        return "Set the start point for the search. This is specified as a comma "
                + "seperated list off attribute indexes starting at 1. It can include "
                + "ranges. Eg. 1,2,5-9,17. If specified, Random searches for subsets "
                + "of attributes that are as good as or better than the start set with "
                + "the same or lower cardinality.";
    }

    /**
     * Sets a starting set of attributes for the search. It is the
     * search method's responsibility to report this start set (if any)
     * in its toString() method.
     *
     * @param startSet a string containing a list of attributes (and or ranges),
     *                 eg. 1,2,6,10-15. "" indicates no start point.
     *                 If a start point is supplied, random search evaluates the
     *                 start point and then looks for subsets that are as good as or better
     *                 than the start point with the same or lower cardinality.
     * @throws Exception if start set can't be set.
     */
    public void setStartSet(String startSet) throws Exception {
        m_startRange.setRanges(startSet);
    }

    /**
     * Returns a list of attributes (and or attribute ranges) as a String
     *
     * @return a list of attributes (and or attribute ranges)
     */
    public String getStartSet() {
        return m_startRange.getRanges();
    }

    /**
     * Returns the tip text for this property
     *
     * @return tip text for this property suitable for
     *         displaying in the explorer/experimenter gui
     */
    public String verboseTipText() {
        return "Print progress information. Sends progress info to the terminal "
                + "as the search progresses.";
    }

    /**
     * set whether or not to output new best subsets as the search proceeds
     *
     * @param v true if output is to be verbose
     */
    public void setVerbose(boolean v) {
        m_verbose = v;
    }

    /**
     * get whether or not output is verbose
     *
     * @return true if output is set to verbose
     */
    public boolean getVerbose() {
        return m_verbose;
    }

    /**
     * Returns the tip text for this property
     *
     * @return tip text for this property suitable for
     *         displaying in the explorer/experimenter gui
     */
    public String searchPercentTipText() {
        return "Percentage of the search space to explore.";
    }

    /**
     * set the percentage of the search space to consider
     *
     * @param p percent of the search space ( 0 < p <= 100)
     */
    public void setSearchPercent(double p) {
        p = Math.abs(p);
        if (p == 0) {
            p = 25;
        }

        if (p > 100.0) {
            p = 100;
        }

        m_searchSize = (p / 100.0);
    }

    /**
     * get the percentage of the search space to consider
     *
     * @return the percent of the search space explored
     */
    public double getSearchPercent() {
        return m_searchSize * 100;
    }

    /**
     * converts the array of starting attributes to a string. This is
     * used by getOptions to return the actual attributes specified
     * as the starting set. This is better than using m_startRanges.getRanges()
     * as the same start set can be specified in different ways from the
     * command line---eg 1,2,3 == 1-3. This is to ensure that stuff that
     * is stored in a database is comparable.
     *
     * @return a comma seperated list of individual attribute numbers as a String
     */
    private String startSetToString() {
        StringBuffer FString = new StringBuffer();
        boolean didPrint;

        if (m_starting == null) {
            return getStartSet();
        }

        for (int i = 0; i < m_starting.length; i++) {
            didPrint = false;

            if ((m_hasClass == false) ||
                    (m_hasClass == true && i != m_classIndex)) {
                FString.append((m_starting[i] + 1));
                didPrint = true;
            }

            if (i == (m_starting.length - 1)) {
                FString.append("");
            } else {
                if (didPrint) {
                    FString.append(",");
                }
            }
        }

        return FString.toString();
    }

    /**
     * prints a description of the search
     *
     * @return a description of the search as a string
     */
    public String toString() {
        StringBuffer text = new StringBuffer();

        text.append("\tRandom search.\n\tStart set: ");
        if (m_starting == null) {
            text.append("no attributes\n");
        } else {
            text.append(startSetToString() + "\n");
        }
        text.append("\tNumber of iterations: " + m_iterations + " ("
                + (m_searchSize * 100.0) + "% of the search space)\n");
        text.append("\tMerit of best subset found: "
                + Utils.doubleToString(Math.abs(m_bestMerit), 8, 3) + "\n");

        return text.toString();
    }

    public void initialiseHarmonyMemory(int size) {

    }

    /**
     * Searches the attribute subset space randomly.
     *
     * @param ASEval the attribute evaluator to guide the search
     * @param data   the training instances.
     * @return an array (not necessarily ordered) of selected attribute indexes
     * @throws Exception if the search can't be completed
     */
    public int[] search(ASEvaluation ASEval, Instances data)
            throws Exception {

        //System.out.println("hi");
        //featureSubsetHarmonyMemory = new FeatureSubsetHarmonyMemory(memorySize);
        harmonyMemory = new HarmonyMemory(memorySize);

        double best_merit;
        int sizeOfBest = m_numAttribs;
        FeatureSubset temp;
        m_bestGroup = new FeatureSubset(m_numAttribs);

        m_onlyConsiderBetterAndSmaller = false;
        if (!(ASEval instanceof SubsetEvaluator)) {
            throw new Exception(ASEval.getClass().getName()
                    + " is not a "
                    + "Subset evaluator!");
        }

        m_random = new SecureRandom(m_seed.getBytes());

        if (ASEval instanceof UnsupervisedSubsetEvaluator) {
            m_hasClass = false;
        } else {
            m_hasClass = true;
            m_classIndex = data.classIndex();
        }

        SubsetEvaluator ASEvaluator = (SubsetEvaluator) ASEval;
        m_numAttribs = data.numAttributes();

        m_startRange.setUpper(m_numAttribs - 1);
        if (!(getStartSet().equals(""))) {
            m_starting = m_startRange.getSelection();
        }

        // If a starting subset has been supplied, then initialise the bitset
        if (m_starting != null) {
            for (int i = 0; i < m_starting.length; i++) {
                if ((m_starting[i]) != m_classIndex) {
                    m_bestGroup.set(m_starting[i]);
                }
            }
            m_onlyConsiderBetterAndSmaller = true;
            best_merit = ASEvaluator.evaluateSubset(m_bestGroup);
            sizeOfBest = countFeatures(m_bestGroup);
        } else {
            // do initial random subset
            m_bestGroup = generateRandomSubset();
            best_merit = ASEvaluator.evaluateSubset(m_bestGroup);
        }

        if (m_verbose) {
            System.out.println("Initial subset ("
                    + Utils.doubleToString(Math.
                    abs(best_merit), 8, 5)
                    + "): " + printSubset(m_bestGroup));
        }

        int i;
        if (m_hasClass) {
            i = m_numAttribs - 1;
        } else {
            i = m_numAttribs;
        }
        m_iterations = (int) ((m_searchSize * Math.pow(2, i)));

        int tempSize;
        double tempMerit;

        //populate memory
        /*for (int k = 0; k < memorySize; k++) {
            FeatureSubset fs = generateRandomSubset();
            fs.setMerit(ASEvaluator.evaluateSubset(fs));
            System.out.println("Adding New FeatureSubset " + fs.toString() + " - " + fs.getMerit() + " - " + featureSubsetHarmonyMemory.add(fs));
        }*/

        for (int kk = 0; kk < ensembleSize; kk++) {
            //System.out.println("New NoteDomain " + kk);
            harmonyMemory.getExperts().add(new Expert(m_numAttribs));
            //System.out.println("New NoteDomain " + kk + " done");
        }

        int lastAdded = 0;
        int optimalAdded = 0;
        int addedCount = 0;
        int rejectedCount = 0;
        int initialSeedCount = 0;

        int noUpdate = 0;
        int noOptimal = 0;
        int currentIteration = 0;

        while (harmonyMemory.size() < memorySize) {
            Vote fs = generateRandomFeatureVote();
            //System.out.println(fs.toString());
            BitSet bs = fs.toBitSet();
            fs.setMerit(ASEvaluator.evaluateSubset(bs));
            fs.setCardinality(bs.cardinality());
            harmonyMemory.add(fs);
            initialSeedCount = initialSeedCount + 1;
            //System.out.println("Adding New Vote " + bs.toString() + " - " + fs.getMerit() + " - " + fs.cardinality() + " - " + harmonyMemory.add(fs));
        }

        // main loop
        while (!((updateFrequency - noUpdate <= 0) || (optimalFrequency - noOptimal <= 0) || (currentIteration >= iteration))) {

            Vote fs = harmonyMemory.newHarmony();
            //System.out.println("Added Vote " + fs.toString());
            BitSet bs = fs.toBitSet();
            fs.setMerit(ASEvaluator.evaluateSubset(bs));
            fs.setCardinality(bs.cardinality());
            Boolean added = harmonyMemory.add(fs);
            if (added) {
                //System.out.println("Added Vote " + bs.toString() + " - " + fs.getMerit());
                addedCount = addedCount + 1;
                lastAdded = currentIteration;
                noUpdate = 0;
                if (harmonyMemory.comparator().compare(harmonyMemory.last(), fs) == 0) {
                    optimalAdded = currentIteration;
                    noOptimal = 0;
                }
                else {
                    noOptimal = noOptimal + 1;
                }
            } else {
                rejectedCount = rejectedCount + 1;
                noUpdate = noUpdate + 1;
                //System.out.println("Rejected Vote " + bs.toString() + " - " + fs.getMerit());
            }

            currentIteration = currentIteration + 1;

        }
        int rank = 1;
        for (Vote fv : harmonyMemory) {
            BitSet bss = fv.toBitSet();
            System.out.println(rank + bss.toString() + " - " + fv.getMerit());
            rank++;
        }

        System.out.println("Current Iteration = " + currentIteration);
        System.out.println("Last Addition on the " + lastAdded + "-th Iteration");
        System.out.println("Optimal Found on the " + optimalAdded + "-th Iteration");
        System.out.println("Initial Seed " + initialSeedCount + " - Added " + addedCount + " - Rejected " + rejectedCount);
        m_bestMerit = ASEvaluator.evaluateSubset(harmonyMemory.last().toBitSet());
        return attributeList(harmonyMemory.last().toBitSet());


        //m_bestMerit = best_merit;
        //return attributeList(m_bestGroup);
    }

    /**
     * prints a subset as a series of attribute numbers
     *
     * @param temp the subset to println
     * @return a subset as a String of attribute numbers
     */
    private String printSubset(BitSet temp) {
        StringBuffer text = new StringBuffer();

        for (int j = 0; j < m_numAttribs; j++) {
            if (temp.get(j)) {
                text.append((j + 1) + " ");
            }
        }
        return text.toString();
    }

    /**
     * converts a BitSet into a list of attribute indexes
     *
     * @param group the BitSet to convert
     * @return an array of attribute indexes
     */
    private int[] attributeList(BitSet group) {
        int count = 0;

        // count how many were selected
        for (int i = 0; i < m_numAttribs; i++) {
            if (group.get(i)) {
                count++;
            }
        }

        int[] list = new int[count];
        count = 0;

        for (int i = 0; i < m_numAttribs; i++) {
            if (group.get(i)) {
                list[count++] = i;
            }
        }

        return list;
    }

    /**
     * generates a random subset
     *
     * @return a random subset as a BitSet
     */
    private FeatureSubset generateRandomSubset() {
        FeatureSubset temp = new FeatureSubset(m_numAttribs - 1);
        double r;

        for (int i = 0; i < m_numAttribs - 1; i++) {
            r = m_random.nextDouble();
            if (r <= 0.5) {
                if (m_hasClass && i == m_classIndex) {
                } else {
                    temp.set(i);
                }
            }
        }
        return temp;
    }

    private Vote generateRandomFeatureVote() {
        Vote fs = new Vote(ensembleSize);
        for (int i = 0; i < ensembleSize; i++) {
            if (m_random.nextDouble() <= discardThreshold) {
                fs.set(i, m_random.nextInt(m_numAttribs - 1));
            } else {
                fs.set(i, m_numAttribs - 1);
            }
        }
        return fs;
    }

    /**
     * counts the number of features in a subset
     *
     * @param featureSet the feature set for which to count the features
     * @return the number of features in the subset
     */
    private int countFeatures(BitSet featureSet) {
        return featureSet.cardinality();
        /*int count = 0;
       for (int i=0;i<m_numAttribs;i++) {
         if (featureSet.get(i)) {
       count++;
         }
       }
       return count;*/
    }

    /**
     * resets to defaults
     */
    private void resetOptions() {
        m_starting = null;
        m_startRange = new Range();
        m_searchSize = 0.25;
        m_seed = "";
        m_onlyConsiderBetterAndSmaller = false;
        m_verbose = false;
    }

    protected class FeatureSubset extends BitSet {
        private double merit;

        public FeatureSubset(double merit) {
            this.merit = merit;
        }

        public FeatureSubset(int nbits, double merit) {
            super(nbits);
            this.merit = merit;
        }

        public FeatureSubset() {
            super();
        }

        public FeatureSubset(int nbits) {
            super(nbits);
        }

        public double getMerit() {
            return merit;
        }

        public void setMerit(double merit) {
            this.merit = merit;
        }
    }

    protected class Vote {
        private double merit;
        private int cardinality;
        private int[] featureVotes;

        public int getSize() {
            return featureVotes.length;
        }

        public Vote(int size) {
            featureVotes = new int[size];
        }

        public BitSet toBitSet() {
            //System.out.println("blah");
            BitSet bs = new BitSet(m_numAttribs - 1);
            int[] count = new int[m_numAttribs -1];
            for (int i = 0; i < featureVotes.length; i++) {
                if ((featureVotes[i] >= 0) && (featureVotes[i] < m_numAttribs - 1)) {
                    //bs.set(m_numAttribs-1 - featureVotes[i]);
                    if (count[featureVotes[i]] > 0)
                    {
                        bs.set(featureVotes[i]);
                    }
                    count[featureVotes[i]] = count[featureVotes[i]] + 1;
                }
            }
            //System.out.println(bs.toString());
            return bs;
        }

        public double getMerit() {
            return merit;
        }

        public void setMerit(double merit) {
            this.merit = merit;
        }

        public int cardinality() {
            return cardinality;
        }

        public void setCardinality(int cardinality) {
            this.cardinality = cardinality;
        }

        public int[] getFeatureVotes() {
            return featureVotes;
        }

        public void setFeatureVotes(int[] featureVotes) {
            this.featureVotes = featureVotes;
        }

        public void set(int index, int value) {
            featureVotes[index] = value;
        }

        public int get(int index) {
            //System.out.println(featureVotes[index] + " for " + index);
            return featureVotes[index];
        }

        public String toString() {
            String s = "";
            for (int i : featureVotes) {
                s = s + " " + i;
            }
            return s;
        }
    }

    protected class HarmonyMemory extends TreeSet<Vote> {
        private int memorySize;
        private ArrayList<Expert> noteDomains;

        public HarmonyMemory() {
        }

        public ArrayList<Expert> getExperts() {
            return noteDomains;
        }

        public void setExperts(ArrayList<Expert> noteDomains) {
            this.noteDomains = noteDomains;
        }

        public HarmonyMemory(int memorySize) {
            super(new FeatureVoteComparator());
            //System.out.println("New HarmonyMemory");
            this.memorySize = memorySize;
            noteDomains = new ArrayList<Expert>();
            //System.out.println("New Experts");

        }

        public int getMemorySize() {
            return memorySize;
        }//true if added and false otherwise

        public void setMemorySize(int memorySize) {
            this.memorySize = memorySize;
        }

        public boolean add(Vote e) {

            if (super.contains(e)) return false;
            if (super.size() < memorySize) {
                for (int i = 0; i < e.getSize(); i++) {
                    //System.out.println("Adding " + e.get(i) + " to " + i);
                    noteDomains.get(i).addChoice(e.get(i));
                }
                super.add(e);
                return true;
            }
            if (super.comparator().compare(e, super.first()) >= 0) {
                for (int i = 0; i < e.getSize(); i++) {
                    noteDomains.get(i).replaceChoice(this.first().get(i), e.get(i));
                }
                super.remove(this.first());
                super.add(e);
                return true;
            }
            return false;
        }

        public Vote newHarmony() {
            Vote newFS = new Vote(ensembleSize);
            for (int i = 0; i < ensembleSize; i++) {
                //newFS.set(i, noteDomains.get(i).makeChoice(m_random.nextInt(6)));
                if (hint == 99) {
                    newFS.set(i, noteDomains.get(i).makeChoice(m_random.nextInt(6)));
                }
                else {
                    newFS.set(i, noteDomains.get(i).makeChoice(hint));
                }

                /*if (m_random.nextDouble() < randomThreshold) {
                    newFS.set(i, noteDomains.get(i).makeChoice(0));
                } else {
                    newFS.set(i, m_random.nextInt(m_numAttribs));
                }*/
            }
            return newFS;
        }

    }

    public class Expert {

        private int[] choices;
        private int cardinality;

        public Expert() {
            choices = new int[m_numAttribs];
            cardinality = 0;
        }

        public Expert(int size) {
            choices = new int[size];
            cardinality = 0;
            //System.out.println("New Musician Size = " + size);
        }

        public int getSize() {
            return choices.length;
        }

        public void addChoice(int index) {
            //System.out.println("Size = " + choices.length + " - " + choices[index]);
            choices[index] = choices[index] + 1;
            cardinality = cardinality + 1;
        }

        public void replaceChoice(int oldIndex, int newIndex) {
            choices[oldIndex] = (choices[oldIndex] <= 0) ? 0 : (choices[oldIndex] - 1);
            choices[newIndex] = choices[newIndex] + 1;
        }

        public void removeChoice(int index) {
            choices[index] = (choices[index] <= 0) ? 0 : (choices[index] - 1);
            cardinality = (cardinality <= 0) ? 0 : cardinality - 1;
        }

        public int makeChoice(int hint) {
            int choice = m_numAttribs - 1;
            int index = 0;
            switch (hint) {
                case 0:
                    //default & 0 -> random picking from existing choices
                    index = m_random.nextInt(cardinality) + 1;
                    for (int i = 0; i < choices.length; i++) {
                        if (choices[i] >= index) {
                            choice = i;
                            break;
                        }
                        index = index - choices[i];
                    }
                    break;
                case 1:
                    //random picking from non-existing choices
                    int emptyCount = 0;
                    for (int i = 0; i < choices.length; i++) {
                        if (choices[i] > 0) {
                            emptyCount = emptyCount + 1;
                        }
                    }
                    if (emptyCount == 0) {
                        choice = m_numAttribs - 1;
                    }
                    else {
                        index = m_random.nextInt(emptyCount) + 1;
                        for (int i = 0; i < choices.length; i++) {
                            if ((index > 0) && (choices[i] == 0)) {
                                choice = i;
                                index = index - 1;
                            }
                        }
                    }
                    break;
                case 2:
                    //random
                    choice = m_random.nextInt(m_numAttribs);
                    break;
                case 3:
                    //majority
                    /*int[] voteCount = new int[m_numAttribs];
                    for (int i = 0; i < choices.length; i++) {
                        voteCount[choices[i]] = voteCount[choices[i]] + 1;
                    }*/
                    int largest = 0;
                    for (int i = 0; i < choices.length; i++) {
                        if (choices[i] > largest) {
                            choice = i;
                            largest = choices[i];
                        }
                    }
                    break;
                case 4:
                    //minority
                    /*int[] voteCountS = new int[m_numAttribs];
                    for (int i = 0; i < choices.length; i++) {
                        voteCountS[choices[i]] = voteCountS[choices[i]] + 1;
                    }*/
                    int smallest = ensembleSize;
                    for (int i = 0; i < choices.length; i++) {
                        if ((0 < choices[i]) && (choices[i] < smallest)) {
                            choice = i;
                            smallest = choices[i];
                        }
                    }
                    break;
                case 5:
                    //discard
                    choice = (m_numAttribs - 1);
                    break;
                default:
                    index = m_random.nextInt(cardinality) + 1;
                    for (int i = 0; i < choices.length; i++) {
                        if (choices[i] >= index) {
                            choice = i;
                            break;
                        }
                        index = index - choices[i];
                    }
                    break;
            }

            return choice;
        }

        public int[] getChoices() {
            return choices;
        }

        public void setChoices(int[] choices) {
            this.choices = choices;
        }

        public int getCardinality() {
            return cardinality;
        }

        public void setCardinality(int cardinality) {
            this.cardinality = cardinality;
        }
    }

    public class FeatureVoteComparator implements Comparator {

        public int compare(Object o1, Object o2) {
            Vote fs1 = (Vote) o1;
            Vote fs2 = (Vote) o2;

            if (fs1.getMerit() >= fs2.getMerit()) {
                if ((fs1.getMerit() > fs2.getMerit()) || (fs1.cardinality() < fs2.cardinality())) return 1;
                if ((fs1.getMerit() == fs2.getMerit()) && (fs1.cardinality() == fs2.cardinality())) return 0;
                return -1;
            } else {
                return -1;
            }
        }
    }

}