package weka.classifiers.meta;/*
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
 *//*


*/
/*
 *    RandomSubSpace.java
 *    Copyright (C) 2006 University of Waikato, Hamilton, New Zealand
 *
 *//*


package weka.classifiers.meta;

import weka.attributeSelection.*;
import weka.classifiers.Classifier;
import weka.classifiers.RandomizableIteratedSingleClassifierEnhancer;
import weka.core.*;
import weka.core.TechnicalInformation.Field;
import weka.core.TechnicalInformation.Type;
import weka.filters.unsupervised.attribute.Remove;
import Harmony;
import FeatureSelectionHarmonyMemory;

import java.security.SecureRandom;
import java.util.*;

*/
/**
 <!-- globalinfo-start -->
 * This method constructs a decision tree based classifier that maintains highest accuracy on training data and improves on generalization accuracy as it grows in complexity. The classifier consists of multiple trees constructed systematically by pseudorandomly selecting subsets of components of the feature vector, that is, trees constructed in randomly chosen subspaces.<br/>
 * <br/>
 * For more information, see<br/>
 * <br/>
 * Tin Kam Ho (1998). The Random Subspace Method for Constructing Decision Forests. IEEE Transactions on Pattern Analysis and Machine Intelligence. 20(8):832-844. URL http://citeseer.ist.psu.edu/ho98random.html.
 * <p/>
 <!-- globalinfo-end -->
 *
 <!-- technical-bibtex-start -->
 * BibTeX:
 * <pre>
 * &#64;article{Ho1998,
 *    author = {Tin Kam Ho},
 *    journal = {IEEE Transactions on Pattern Analysis and Machine Intelligence},
 *    number = {8},
 *    pages = {832-844},
 *    title = {The Random Subspace Method for Constructing Decision Forests},
 *    volume = {20},
 *    year = {1998},
 *    ISSN = {0162-8828},
 *    URL = {http://citeseer.ist.psu.edu/ho98random.html}
 * }
 * </pre>
 * <p/>
 <!-- technical-bibtex-end -->
 *
 <!-- options-start -->
 * Valid options are: <p/>
 *
 * <pre> -P
 *  Size of each subspace:
 *   &lt; 1: percentage of the number of attributes
 *   &gt;=1: absolute number of attributes
 * </pre>
 *
 * <pre> -S &lt;num&gt;
 *  Random number seed.
 *  (default 1)</pre>
 *
 * <pre> -I &lt;num&gt;
 *  Number of iterations.
 *  (default 10)</pre>
 *
 * <pre> -D
 *  If set, classifier is run in debug mode and
 *  may output additional info to the console</pre>
 *
 * <pre> -W
 *  Full name of base classifier.
 *  (default: weka.classifiers.trees.REPTree)</pre>
 *
 * <pre>
 * Options specific to classifier weka.classifiers.trees.REPTree:
 * </pre>
 *
 * <pre> -M &lt;minimum number of instances&gt;
 *  Set minimum number of instances per leaf (default 2).</pre>
 *
 * <pre> -V &lt;minimum variance for split&gt;
 *  Set minimum numeric class variance proportion
 *  of train variance for split (default 1e-3).</pre>
 *
 * <pre> -N &lt;number of folds&gt;
 *  Number of folds for reduced error pruning (default 3).</pre>
 *
 * <pre> -S &lt;seed&gt;
 *  Seed for random data shuffling (default 1).</pre>
 *
 * <pre> -P
 *  No pruning.</pre>
 *
 * <pre> -L
 *  Maximum tree depth (default -1, no maximum)</pre>
 *
 <!-- options-end -->
 *
 * Options after -- are passed to the designated classifier.<p>
 *
 * @author Bernhard Pfahringer (bernhard@cs.waikato.ac.nz)
 * @author Peter Reutemann (fracpete@cs.waikato.ac.nz)
 * @version $Revision: 1.4 $
 *//*

public class HarmonySubSpace
  extends RandomizableIteratedSingleClassifierEnhancer
  implements WeightedInstancesHandler, TechnicalInformationHandler {

  */
/** for serialization *//*

  private static final long serialVersionUID = 1278172513912424947L;

  */
/** The size of each bag sample, as a percentage of the training size *//*

  protected double m_SubSpaceSize = 0.5;

  */
/** a ZeroR model in case no model can be built from the data *//*

  protected Classifier m_ZeroR;

  private ASEvaluation ASEval;  
    */
/**
 * number of attributes in the data
 *//*

    private int m_numAttribs;
          */
/**
 * random number object
 *//*

    private Random m_random;
  */
/**
 * Constructor.
 *//*

   private int memorySize;
    private double discardThreshold = 0.8;
    private double randomThreshold = 0.8;
    private int ensembleSize = 15;
    private int updateFrequency = 50;
    private int optimalFrequency = 100;
     private int iteration = 250;
      private int hint = 0;
    */
/**
 * does the data have a class
 *//*

    private boolean m_hasClass;

    */
/**
 * holds the class index
 *//*

    private int m_classIndex;
  public HarmonySubSpace() {
    super();

    m_Classifier = new weka.classifiers.trees.REPTree();
  }

  */
/**
 * Returns a string describing classifier
 *
 * @return a description suitable for
 * 			displaying in the explorer/experimenter gui
 *//*

  public String globalInfo() {
    return
        "This method constructs a decision tree based classifier that "
      + "maintains highest accuracy on training data and improves on "
      + "generalization accuracy as it grows in complexity. The classifier "
      + "consists of multiple trees constructed systematically by "
      + "pseudorandomly selecting subsets of components of the feature vector, "
      + "that is, trees constructed in randomly chosen subspaces.\n\n"
      + "For more information, see\n\n"
      + getTechnicalInformation().toString();
  }

    public ASEvaluation getASEval() {
        return ASEval;
    }

    public void setASEval(ASEvaluation ASEval) {
        this.ASEval = ASEval;
    }

    */
/**
 * Returns an instance of a TechnicalInformation object, containing
 * detailed information about the technical background of this class,
 * e.g., paper reference or book this class is based on.
 *
 * @return the technical information about this class
 *//*

  public TechnicalInformation getTechnicalInformation() {
    TechnicalInformation 	result;

    result = new TechnicalInformation(Type.ARTICLE);
    result.setValue(Field.AUTHOR, "Tin Kam Ho");
    result.setValue(Field.YEAR, "1998");
    result.setValue(Field.TITLE, "The Random Subspace Method for Constructing Decision Forests");
    result.setValue(Field.JOURNAL, "IEEE Transactions on Pattern Analysis and Machine Intelligence");
    result.setValue(Field.VOLUME, "20");
    result.setValue(Field.NUMBER, "8");
    result.setValue(Field.PAGES, "832-844");
    result.setValue(Field.URL, "http://citeseer.ist.psu.edu/ho98random.html");
    result.setValue(Field.ISSN, "0162-8828");

    return result;
  }

  */
/**
 * String describing default classifier.
 *
 * @return the default classifier classname
 *//*

  protected String defaultClassifierString() {
    return "weka.classifiers.trees.REPTree";
  }

  */
/**
 * Returns an enumeration describing the available options.
 *
 * @return an enumeration of all the available options.
 *//*

  public Enumeration listOptions() {
    Vector result = new Vector();

    result.addElement(new Option(
	"\tSize of each subspace:\n"
	+ "\t\t< 1: percentage of the number of attributes\n"
	+ "\t\t>=1: absolute number of attributes\n",
	"P", 1, "-P"));

    Enumeration enu = super.listOptions();
    while (enu.hasMoreElements()) {
      result.addElement(enu.nextElement());
    }

    return result.elements();
  }

  */
/**
 * Parses a given list of options. <p/>
 *
 <!-- options-start -->
 * Valid options are: <p/>
 *
 * <pre> -P
 *  Size of each subspace:
 *   &lt; 1: percentage of the number of attributes
 *   &gt;=1: absolute number of attributes
 * </pre>
 *
 * <pre> -S &lt;num&gt;
 *  Random number seed.
 *  (default 1)</pre>
 *
 * <pre> -I &lt;num&gt;
 *  Number of iterations.
 *  (default 10)</pre>
 *
 * <pre> -D
 *  If set, classifier is run in debug mode and
 *  may output additional info to the console</pre>
 *
 * <pre> -W
 *  Full name of base classifier.
 *  (default: weka.classifiers.trees.REPTree)</pre>
 *
 * <pre>
 * Options specific to classifier weka.classifiers.trees.REPTree:
 * </pre>
 *
 * <pre> -M &lt;minimum number of instances&gt;
 *  Set minimum number of instances per leaf (default 2).</pre>
 *
 * <pre> -V &lt;minimum variance for split&gt;
 *  Set minimum numeric class variance proportion
 *  of train variance for split (default 1e-3).</pre>
 *
 * <pre> -N &lt;number of folds&gt;
 *  Number of folds for reduced error pruning (default 3).</pre>
 *
 * <pre> -S &lt;seed&gt;
 *  Seed for random data shuffling (default 1).</pre>
 *
 * <pre> -P
 *  No pruning.</pre>
 *
 * <pre> -L
 *  Maximum tree depth (default -1, no maximum)</pre>
 *
 <!-- options-end -->
 *
 * Options after -- are passed to the designated classifier.<p>
 *
 * @param options     the list of options as an array of strings
 * @throws Exception     if an option is not supported
 *//*

  public void setOptions(String[] options) throws Exception {
    String tmpStr;

    tmpStr = Utils.getOption('P', options);
    if (tmpStr.length() != 0)
      setSubSpaceSize(Double.parseDouble(tmpStr));
    else
      setSubSpaceSize(0.5);

    super.setOptions(options);
  }

  */
/**
 * Gets the current settings of the Classifier.
 *
 * @return an array of strings suitable for passing to setOptions
 *//*

  public String [] getOptions() {
    Vector        result;
    String[]      options;
    int           i;

    result  = new Vector();

    result.add("-P");
    result.add("" + getSubSpaceSize());

    options = super.getOptions();
    for (i = 0; i < options.length; i++)
      result.add(options[i]);

    return (String[]) result.toArray(new String[result.size()]);
  }

  */
/**
 * Returns the tip text for this property
 *
 * @return tip text for this property suitable for
 * 			displaying in the explorer/experimenter gui
 *//*

  public String subSpaceSizeTipText() {
    return
        "Size of each subSpace: if less than 1 as a percentage of the "
      + "number of attributes, otherwise the absolute number of attributes.";
  }

  */
/**
 * Gets the size of each subSpace, as a percentage of the training set size.
 *
 * @return the subSpace size, as a percentage.
 *//*

  public double getSubSpaceSize() {
    return m_SubSpaceSize;
  }

  */
/**
 * Sets the size of each subSpace, as a percentage of the training set size.
 *
 * @param value     the subSpace size, as a percentage.
 *//*

  public void setSubSpaceSize(double value) {
    m_SubSpaceSize = value;
  }

  */
/**
 * calculates the number of attributes
 *
 * @param total    the available number of attributes
 * @param fraction    the fraction - if less than 1 it represents the
 * 			percentage, otherwise the absolute number of attributes
 * @return the number of attributes to use
 *//*

  protected int numberOfAttributes(int total, double fraction) {
    int k = (int) Math.round((fraction < 1.0) ? total*fraction : fraction);

    if (k > total)
      k = total;
    if (k < 1)
      k = 1;

    return k;
  }

  */
/**
 * generates an index string describing a random subspace, suitable for
 * the Remove filter.
 *
 * @param indices        the attribute indices
 * @param subSpaceSize    the size of the subspace
 * @param classIndex        the class index
 * @param random        the random number generator
 * @return the generated string describing the subspace
 *//*

  protected String randomSubSpace(Integer[] indices, int subSpaceSize, int classIndex, Random random) {
    Collections.shuffle(Arrays.asList(indices), random);
    StringBuffer sb = new StringBuffer("");
    for(int i = 0; i < subSpaceSize; i++) {
      sb.append(indices[i]+",");
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
        b.append(i+1);
        i = i + 1;
	while (bitSet.nextClearBit(i) <= classIndex) {
        //System.out.println(i);
	    i = bitSet.nextClearBit(i);
        b.append(",").append(i+1);
        i = i + 1;
    }
        return b.toString();
    }

*/
/*
    protected String getRemove(BitSet bitSet, int classIndex) {
        StringBuffer b = new StringBuffer();
	//b.append('{');

	int i = bitSet.nextSetBit(0);
        //System.out.println(i);
        b.append(i+1);
        i = i + 1;
	while ((bitSet.nextSetBit(i) != -1) && (bitSet.nextSetBit(i) <= classIndex)) {
        //System.out.println(i);
	    i = bitSet.nextSetBit(i);
        b.append(",").append(i+1);
        i = i + 1;
    }
        return b.toString();
    }
*//*


  */
/**
 * builds the classifier.
 *
 * @param data     the training data to be used for generating the
 * 			classifier.
 * @throws Exception     if the classifier could not be built successfully
 *//*

  public void buildClassifier(Instances data) throws Exception {

    // can classifier handle the data?
    getCapabilities().testWithFail(data);

    // remove instances with missing class
    data = new Instances(data);
    data.deleteWithMissingClass();

    // only class? -> build ZeroR model
    if (data.numAttributes() == 1) {
      System.err.println(
	  "Cannot build model (only class attribute present in data!), "
	  + "using ZeroR model instead!");
      m_ZeroR = new weka.classifiers.rules.ZeroR();
      m_ZeroR.buildClassifier(data);
      return;
    }
    else {
      m_ZeroR = null;
    }

    super.buildClassifier(data);

    Integer[] indices = new Integer[data.numAttributes()-1];
    int classIndex = data.classIndex();
    int offset = 0;
    for(int i = 0; i < indices.length+1; i++) {
      if (i != classIndex) {
	indices[offset++] = i+1;
      }
    }
    int subSpaceSize = numberOfAttributes(indices.length, getSubSpaceSize());
    Random random = data.getRandomNumberGenerator(m_Seed);
    m_random = new SecureRandom((m_Seed + "").getBytes());
    m_numAttribs = data.numAttributes();
    BitSet[] bitSets = searchAll(data);

    */
/*for (Vote fv : harmonyMemory) {
            BitSet bss = fv.toBitSet();
            System.out.println(getRemove(bss, m_classIndex));
            System.out.println(rank + bss.toString() + " - " + fv.getMerit());
            rank++;
        }*//*

    for (int j = 0; j < m_Classifiers.length; j++) {
      if (m_Classifier instanceof Randomizable) {
	((Randomizable) m_Classifiers[j]).setSeed(random.nextInt());
      }
      FilteredClassifier fc = new FilteredClassifier();
      fc.setClassifier(m_Classifiers[j]);
      m_Classifiers[j] = fc;
      Remove rm = new Remove();
        //System.out.println(randomSubSpace(indices,subSpaceSize,classIndex+1,random));
        System.out.println(getRemove(bitSets[j], m_classIndex));
      //rm.setOptions(new String[]{"-V", "-R", randomSubSpace(indices,subSpaceSize,classIndex+1,random)});
        rm.setOptions(new String[]{"-V", "-R", getRemove(bitSets[j], m_classIndex)});
      fc.setFilter(rm);

      // build the classifier
      m_Classifiers[j].buildClassifier(data);
    }

  }

    public BitSet[] searchAll(Instances data) throws Exception {
        BitSet[] result = new BitSet[m_Classifiers.length];
        for (int i = 0; i < m_Classifiers.length; i++) {
            result[i] = search(data)[m_Classifiers.length - 1];
        }
        return result;
    }

    public BitSet[] search(Instances data) throws Exception {

        double best_merit;
        int sizeOfBest = m_numAttribs;
        //FeatureSubset temp;
        ASEval = new FuzzyRoughSubsetEval();
        ASEval.buildEvaluator(data);
        if (!(ASEval instanceof SubsetEvaluator)) {
            System.out.println(ASEval.getClass().getName()
                    + " is not a "
                    + "Subset evaluator!");
        }
        SubsetEvaluator ASEvaluator = (SubsetEvaluator) ASEval;
        m_numAttribs = data.numAttributes();
             if (ASEval instanceof UnsupervisedSubsetEvaluator) {
            m_hasClass = false;
        } else {
            m_hasClass = true;
            m_classIndex = data.classIndex();
        }
        int i;
        if (m_hasClass) {
            i = m_numAttribs - 1;
        } else {
            i = m_numAttribs;
        }

        memorySize = m_Classifiers.length;
        */
/*HarmonyMemory harmonyMemory = new HarmonyMemory(memorySize, ensembleSize, i, m_random, data);
        harmonyMemory.initialise();*//*

        FeatureSelectionHarmonyMemory harmonyMemory = new FeatureSelectionHarmonyMemory(memorySize, ensembleSize, data.numAttributes() - 1, m_random);
        harmonyMemory.initialise(data);
        //int tempSize;
        //double tempMerit;

        //populate memory
        */
/*for (int k = 0; k < memorySize; k++) {
            FeatureSubset fs = generateRandomSubset();
            fs.setMerit(ASEvaluator.evaluateSubset(fs));
            System.out.println("Adding New FeatureSubset " + fs.toString() + " - " + fs.getMerit() + " - " + featureSubsetHarmonyMemory.add(fs));
        }*//*

        //int ensembleSize = 15;
        //HarmonyMemory harmonyMemory =

        */
/*for (int kk = 0; kk < ensembleSize; kk++) {
            //System.out.println("New NoteDomain " + kk);
            harmonyMemory.getExperts().add(new Musician(m_numAttribs));
            //System.out.println("New NoteDomain " + kk + " done");
        }*//*


        int lastAdded = 0;
        int optimalAdded = 0;
        int addedCount = 0;
        int rejectedCount = 0;
        int initialSeedCount = 0;

        int noUpdate = 0;
        int noOptimal = 0;
        int currentIteration = 0;

        while (harmonyMemory.size() < memorySize) {
            Harmony harmony = harmonyMemory.randomHarmony(discardThreshold);
            //System.out.println(vote.toString());
            BitSet bs = (BitSet) harmonyMemory.getHarmonyEvaluator(0).translate(harmony);
            harmony.setMerit(ASEvaluator.evaluateSubset(bs));
            harmony.setCardinality(bs.cardinality());
            harmonyMemory.add(harmony);
            initialSeedCount = initialSeedCount + 1;
            //System.out.println("Adding New Vote " + bs.toString() + " - " + vote.getMerit() + " - " + vote.cardinality() + " - " + harmonyMemory.add(vote));
        }

        // main loop
        while (!((updateFrequency - noUpdate <= 0) || (optimalFrequency - noOptimal <= 0) || (currentIteration >= iteration))) {

            Harmony harmony = harmonyMemory.newHarmony();
            //System.out.println("Added Vote " + vote.toString());
            BitSet bitSet = (BitSet) harmonyMemory.getHarmonyEvaluator().translate(harmony);
            harmony.setMerit(ASEvaluator.evaluateSubset(bitSet));
            harmony.setCardinality(bitSet.cardinality());
            Boolean added = harmonyMemory.add(harmony);
            if (added) {
                //System.out.println("Added Vote " + bitSet.toString() + " - " + vote.getMerit());
                addedCount = addedCount + 1;
                lastAdded = currentIteration;
                noUpdate = 0;
                if (harmonyMemory.comparator().compare(harmonyMemory.last(), harmony) == 0) {
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
            }

            currentIteration = currentIteration + 1;

        }
        int rank = 0;
        BitSet[] bitSets = new BitSet[harmonyMemory.size()];

        for (Harmony harmony : harmonyMemory) {
            BitSet bitSet = (BitSet) harmonyMemory.getHarmonyEvaluator().translate(harmony);
            bitSets[rank] = bitSet;
            //System.out.println(getRemove(bitSet, m_classIndex));
            //System.out.println(rank + bitSet.toString() + " - " + vote.getMerit());
            rank++;
        }

        //System.out.println("Current Iteration = " + currentIteration);
        //System.out.println("Last Addition on the " + lastAdded + "-th Iteration");
        //System.out.println("Optimal Found on the " + optimalAdded + "-th Iteration");
        //System.out.println("Initial Seed " + initialSeedCount + " - Added " + addedCount + " - Rejected " + rejectedCount);
        return bitSets;
    }

    */
/**
 * generates a random subset
 *
 * @return a random subset as a BitSet
 *//*

        */
/*private FeatureSubset generateRandomSubset() {
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
        }*//*


        */
/*private Vote generateRandomFeatureVote() {
            Vote fs = new Vote(ensembleSize);
            for (int i = 0; i < ensembleSize; i++) {
                if (m_random.nextDouble() <= discardThreshold) {
                    fs.set(i, m_random.nextInt(m_numAttribs - 1));
                } else {
                    fs.set(i, m_numAttribs - 1);
                }
            }
            return fs;
        }*//*


        */
/**
 * counts the number of features in a subset
 *
 * @param featureSet the feature set for which to count the features
 * @return the number of features in the subset
 *//*

        private int countFeatures(BitSet featureSet) {
            return featureSet.cardinality();
            */
/*int count = 0;
           for (int i=0;i<m_numAttribs;i++) {
             if (featureSet.get(i)) {
           count++;
             }
           }
           return count;*//*

        }

  */
/**
 * Calculates the class membership probabilities for the given test
 * instance.
 *
 * @param instance     the instance to be classified
 * @return preedicted class probability distribution
 * @throws Exception     if distribution can't be computed successfully
 *//*

  public double[] distributionForInstance(Instance instance) throws Exception {

    // default model?
    if (m_ZeroR != null) {
      return m_ZeroR.distributionForInstance(instance);
    }

    double[] sums = new double [instance.numClasses()], newProbs;

    for (int i = 0; i < m_NumIterations; i++) {
      if (instance.classAttribute().isNumeric() == true) {
	sums[0] += m_Classifiers[i].classifyInstance(instance);
      } else {
	newProbs = m_Classifiers[i].distributionForInstance(instance);
	for (int j = 0; j < newProbs.length; j++)
	  sums[j] += newProbs[j];
      }
    }
    if (instance.classAttribute().isNumeric() == true) {
      sums[0] /= (double)m_NumIterations;
      return sums;
    } else if (Utils.eq(Utils.sum(sums), 0)) {
      return sums;
    } else {
      Utils.normalize(sums);
      return sums;
    }
  }

  */
/**
 * Returns description of the bagged classifier.
 *
 * @return description of the bagged classifier as a string
 *//*

  public String toString() {

    // only ZeroR model?
    if (m_ZeroR != null) {
      StringBuffer buf = new StringBuffer();
      buf.append(this.getClass().getName().replaceAll(".*\\.", "") + "\n");
      buf.append(this.getClass().getName().replaceAll(".*\\.", "").replaceAll(".", "=") + "\n\n");
      buf.append("Warning: No model could be built, hence ZeroR model is used:\n\n");
      buf.append(m_ZeroR.toString());
      return buf.toString();
    }

    if (m_Classifiers == null) {
      return "RandomSubSpace: No model built yet.";
    }
    StringBuffer text = new StringBuffer();
    text.append("All the base classifiers: \n\n");
    for (int i = 0; i < m_Classifiers.length; i++)
      text.append(m_Classifiers[i].toString() + "\n\n");

    return text.toString();
  }

  */
/**
 * Returns the revision string.
 *
 * @return the revision
 *//*

  public String getRevision() {
    return RevisionUtils.extract("$Revision: 1.4 $");
  }

  */
/**
 * Main method for testing this class.
 *
 * @param args     the options
 *//*

  public static void main(String[] args) {
    runClassifier(new HarmonySubSpace(), args);
  }

}
*/
