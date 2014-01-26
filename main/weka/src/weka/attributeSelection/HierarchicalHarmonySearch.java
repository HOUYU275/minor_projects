package weka.attributeSelection;

import hierarchicalharmonysearch.applications.featureselection.FeatureSelectionHarmonyComparator;
import hierarchicalharmonysearch.applications.featureselection.FeatureSelectionHarmonyMemory;
import hierarchicalharmonysearch.core.HarmonyMemory;
import hierarchicalharmonysearch.core.ValueRange;
import weka.core.*;

import java.util.BitSet;
import java.util.Enumeration;
import java.util.Random;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 06-Jul-2010
 * Time: 11:15:21
 * To change this template use File | Settings | File Templates.
 */
public class HierarchicalHarmonySearch extends ASSearch
        implements /*StartSetHandler, */OptionHandler, TechnicalInformationHandler

{

    private int numHarmonies = 20;
    private int numMusicians = 15;
    private double HMCR = 0.5;
    private int iteration = 1000;

    private int[] searchResult = null;

    //private double discardThreshold = 0.5;

    /**
     * for serialization
     */
    static final long serialVersionUID = 7479392617377425484L;

    /**
     * only accept a feature set as being "better" than the best if its
     * merit is better or equal to the best, and it contains fewer
     * features than the best (this allows LVF to be implimented).
     */
    //private boolean m_onlyConsiderBetterAndSmaller;

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
    private int m_seed;

    /**
     * random number object
     */
    private Random m_random;

    /**
     * output new best subsets as the search progresses
     */
    private boolean m_verbose;

    /**
     * the best feature set found during the search
     */
    private BitSet m_bestGroup;

    /**
     * the merit of the best subset found
     */
    private double m_bestMerit;

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

    /**
     * Constructor
     */
    public HierarchicalHarmonySearch() {
        resetOptions();
    }

    public int getNumMusicians() {
        return numMusicians;
    }

    public void setNumMusicians(int numMusicians) {
        this.numMusicians = numMusicians;
    }

    public double getHMCR() {
        return HMCR;
    }

    public void setHMCR(double HMCR) {
        this.HMCR = HMCR;
    }

    /*public double getDiscardThreshold() {
        return discardThreshold;
    }

    public void setDiscardThreshold(double discardThreshold) {
        this.discardThreshold = discardThreshold;
    }*/

    public int getNumHarmonies() {
        return numHarmonies;
    }

    public void setNumHarmonies(int numHarmonies) {
        this.numHarmonies = numHarmonies;
    }

    public int getIteration() {
        return iteration;
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }

    public int getM_seed() {
        return m_seed;
    }

    public void setM_seed(int m_seed) {
        this.m_seed = m_seed;
    }

    /**
     * Returns an enumeration describing the available options.
     *
     * @return an enumeration of all the available options.
     */
    public Enumeration listOptions() {
        Vector newVector = new Vector(6);
        newVector.addElement(new Option("\tOutput subsets as the search progresses."
                + "\n\t(default = false)."
                , "V", 0
                , "-V"));
        newVector.addElement(new Option("\tTotal Iterations."
                + "\n\t(default = false)."
                , "I", 250
                , "-I"));
        newVector.addElement(new Option("\tMemory Size."
                + "\n\t(default = false)."
                , "M", 20
                , "-M"));
        /*newVector.addElement(new Option("\tDiscard Threshold."
                + "\n\t(default = false)."
                , "D", 50
                , "-D"));*/
        newVector.addElement(new Option("\tRandom Threshold."
                + "\n\t(default = false)."
                , "R", 80
                , "-R"));
        newVector.addElement(new Option("\tEnsemble Size."
                + "\n\t(default = false)."
                , "S", 10
                , "-S"));
        newVector.addElement(new Option("\tRandom Seed."
                + "\n\t(default = false)."
                , "Z", 0
                , "-Z"));
        /*newVector.addElement(new Option("\tSecond Evaluator."
     + "\t(default: weka.attributeSelection.FuzzyRoughSubsetEval)"
     , "B", 1
     , "-B <second evaluator>"));*/

        /*if ((secondEvaluator != null) &&
                (secondEvaluator instanceof OptionHandler)) {
            newVector.addElement(new Option("", "", 0, "\nOptions specific to scheme "
                    + secondEvaluator.getClass().getName()
                    + ":"));
            Enumeration enu = ((OptionHandler) secondEvaluator).listOptions();

            while (enu.hasMoreElements()) {
                newVector.addElement(enu.nextElement());
            }
        }*/
        return newVector.elements();
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

        /*optionString = Utils.getOption('B', options);

        if (optionString.length() == 0)
            optionString = FuzzyRoughSubsetEval.class.getName();
            setSecondEvaluator(ASEvaluation.forName(optionString,
                Utils.partitionOptions(options)));*/

        optionString = Utils.getOption('I', options);
        if (optionString.length() != 0) {
            setIteration((new Integer(optionString)));
        }
        optionString = Utils.getOption('M', options);
        if (optionString.length() != 0) {
            setNumHarmonies((new Integer(optionString)));
        }
        optionString = Utils.getOption('S', options);
        if (optionString.length() != 0) {
            setNumMusicians((new Integer(optionString)));
        }
        /*optionString = Utils.getOption('D', options);
        if (optionString.length() != 0) {
            setDiscardThreshold((new Double(optionString)).doubleValue());
        }*/
        optionString = Utils.getOption('R', options);
        if (optionString.length() != 0) {
            setHMCR((new Double(optionString)).doubleValue());
        }
        setVerbose(Utils.getFlag('V', options));
    }

    /**
     * Gets the current settings of RandomSearch.
     *
     * @return an array of strings suitable for passing to setOptions()
     */
    public String[] getOptions() {
        String[] options = new String[11];
        int current = 0;

        if (m_verbose) {
            options[current++] = "-V";
        }

        options[current++] = "-I";
        options[current++] = "" + getIteration();

        options[current++] = "-M";
        options[current++] = "" + getNumHarmonies();

        options[current++] = "-S";
        options[current++] = "" + getNumMusicians();

        /*options[current++] = "-D";
        options[current++] = "" + getDiscardThreshold();*/

        options[current++] = "-R";
        options[current++] = "" + getHMCR();

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
     * prints a description of the search
     *
     * @return a description of the search as a string
     */
    public String toString() {
        StringBuffer text = new StringBuffer();

        text.append("\tHarmony Search\n");
        text.append("\tNumber of Iterations: " + iteration + "\n");
        text.append("\tNumber of Harmonies: " + numHarmonies + "\n");
        text.append("\tNumber of Musicians: " + numMusicians + "\n");
        //text.append("\tNumber of Discard Threshold: " + discardThreshold + "\n");
        text.append("\tNumber of Random Threshold: " + HMCR + "\n");
        text.append("\tMerit of best subset found: "
                + Utils.doubleToString(Math.abs(m_bestMerit), 8, 3) + "\n");

        return text.toString();
    }

    /*public void wrapper(int innerMemorySize,
                        int memorySize,
                        double HMCR,
                        int maxIteration,
                        SubsetEvaluator subsetEvaluator,
                        Instances data) throws Exception {
        //int wrapperMemorySize = memorySize;
        ValueRange[] wrapperValueRanges = new ValueRange[3];
        //HMS
        wrapperValueRanges[0] = new ValueRange(1, innerMemorySize, false);
        //HMCR
        wrapperValueRanges[1] = new ValueRange(0d, 1d, true);
        //Musician Size
        wrapperValueRanges[2] = new ValueRange(5, m_numAttribs, false);

        WrapperParameterOptimisationHarmonyMemory wrapperParameterOptimisationHarmonyMemory =
                new WrapperParameterOptimisationHarmonyMemory(HarmonyMemory.createParameterRanges(memorySize, HMCR), wrapperValueRanges, m_random, subsetEvaluator, data);
        //wrapperParameterOptimisationHarmonyMemory.initialise();
        wrapperParameterOptimisationHarmonyMemory.fill();
        System.out.println("Initial Harmonies");
        wrapperParameterOptimisationHarmonyMemory.printHarmonies();
        wrapperParameterOptimisationHarmonyMemory.iterate(maxIteration);

        wrapperParameterOptimisationHarmonyMemory.printHarmonies();
    }
*/

    /**
     * Searches the attribute subset space randomly.
     *
     * @param ASEval the attribute evaluator to guide the search
     * @param data   the training instances.
     * @return an array (not necessarily ordered) of selected attribute indexes
     * @throws Exception if the search can't be completed
     */
    public int[] search(ASEvaluation ASEval, Instances data) throws Exception {

        if (!(ASEval instanceof SubsetEvaluator))
            throw new Exception(ASEval.getClass().getName() + " is not a Subset evaluator!");

        //m_random = new Random((long) m_seed);
        m_random = new Random(new Random().nextInt());

        if (ASEval instanceof UnsupervisedSubsetEvaluator) {
            m_hasClass = false;
        } else {
            m_hasClass = true;
            m_classIndex = data.classIndex();
        }

        SubsetEvaluator subsetEvaluator = (SubsetEvaluator) ASEval;
        m_numAttribs = data.numAttributes();
        //System.out.println("Num Attributes = " + m_numAttribs);
        int numNotes = (m_hasClass) ? m_numAttribs - 1 : m_numAttribs;

        ValueRange[] valueRanges = new ValueRange[numMusicians];
        for (int i = 0; i < valueRanges.length; i++) {
            valueRanges[i] = new ValueRange(0, numNotes, false);
        }

        long startTime = System.currentTimeMillis();

        FeatureSelectionHarmonyMemory featureSelectionHarmonyMemory =
                new FeatureSelectionHarmonyMemory(
                        HarmonyMemory.createParameterRanges(numHarmonies, HMCR),
                        valueRanges, m_random, subsetEvaluator);
        //System.out.println("Filling Started");
        featureSelectionHarmonyMemory.fill();
        //System.out.println("Iteration Started");
        featureSelectionHarmonyMemory.iterate(iteration);

        long endTime = System.currentTimeMillis();

        m_bestMerit = featureSelectionHarmonyMemory.best().getMerit();
        searchResult = ((FeatureSelectionHarmonyComparator) featureSelectionHarmonyMemory.getHarmonyComparator()).toArray(featureSelectionHarmonyMemory.best());

        /*for (int attribute : selectedAttributes) System.out.println(attribute + " ");
        System.out.println("\t" + (endTime - startTime) + "\t" + selectedAttributes.length + "\t" + m_bestMerit);*/

        //featureSelectionHarmonyMemory.printHarmonies();

        return searchResult;
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
     * resets to defaults
     */
    private void resetOptions() {
        m_seed = 1;
        m_verbose = false;
    }

    public double getM_bestMerit() {
        return m_bestMerit;
    }

    public int[] getSearchResult() {
        return searchResult;
    }
}