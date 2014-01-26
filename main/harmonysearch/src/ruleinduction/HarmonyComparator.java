package ruleinduction;

import weka.classifiers.fuzzy.FuzzyRoughCalculator;
import weka.classifiers.fuzzy.FuzzyRule;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 20/12/11
 * Time: 12:33
 * To change this template use File | Settings | File Templates.
 */
public class HarmonyComparator implements Comparator {

    protected FuzzyRoughCalculator calculator;
    protected HarmonyMemory harmonyMemory;

    protected double[] globalLowerApproximation;

    protected int dimension = 1;

    public HarmonyComparator(HarmonyMemory harmonyMemory) {
        this.harmonyMemory = harmonyMemory;
        calculator = new FuzzyRoughCalculator(harmonyMemory.instances);
        globalLowerApproximation = calculator.getGlobalLowerApproximation();
        //Relation relation = calculator.generatePartition(harmonyMemory.features);
        //double[] lower = calculator.getLowerApproximations(relation);

        /*for (int i = 0; i < lower.length; i++) {
            System.out.println(i + ": " + lower[i] + " - " + globalLowerApproximation[i]);
        }*/
    }

    public ArrayList<FuzzyRule> translate(Harmony harmony) {
        ArrayList<FuzzyRule> rules = new ArrayList<FuzzyRule>();
        for (Note note : harmony.notes) {
            FuzzyRule rule = note.toFuzzyRule();
            if (rule != null) rules.add(rule);
        }
        return rules;
    }

    public int checkConstraint(Harmony harmony) {
        //double[] coverage = calculator.getCoverage(translate(harmony));
        //int i = 0;
        //for (double d : coverage) System.out.println(i++ + " - " + d);
        return 0;
    }

    public boolean checkConstraint(Note note, int instance) {
        double global = globalLowerApproximation[instance];
        /*int count = 0;
        for (int i = 0; i < note.equivalenceClasses.length; i++) {
            if (note.equivalenceClasses[i] == 1) count++;
        }
        System.out.println(instance + " - " + count);*/
        return (global == note.lowerApproximation);
    }

    public double evaluateCoverage(ArrayList<FuzzyRule> rules) {
        double[] coverage = calculator.getCoverage(rules);
        double coverageSum = 0d;
        for (int i = 0; i < coverage.length; i++) {
            coverageSum += coverage[i];
        }
        return coverageSum / coverage.length;
    }

    public void evaluate(Harmony harmony) {
        double[] weights = new double[]{0.5d, 2d, 0.5d, 0.5d};

        ArrayList<FuzzyRule> rules = translate(harmony);

        double coverageScore = evaluateCoverage(rules);

        double approximateScore = 0d;
        int approximateCounter = 0;
        for (Note note : harmony.notes) {
            if (note.value != null) {
                if (globalLowerApproximation[note.musician.instance] != 0) {
                    approximateCounter++;
                    approximateScore += note.lowerApproximation / globalLowerApproximation[note.musician.instance];
                }
            }
        }
        approximateScore = approximateScore / approximateCounter;

        double sizeScore = 1 - (rules.size() + 0d) / harmonyMemory.instances.numInstances();

        int cardinalitySum = 0;
        for (int i = 0; i < rules.size(); i++) {
            cardinalitySum += rules.get(i).getFeatures().cardinality();
        }
        double cardinalityScore = 1 - (cardinalitySum + 0d) / (rules.size() * harmonyMemory.features.length);

        calculateRedundancy(coverageScore);

        /*System.out.println("|" + "Coverage Score: " + coverageScore);
        System.out.println("|" + "Approximate Score: " + approximateScore);
        System.out.println("|" + "Size Score: " + sizeScore + " (" + (rules.size() + 0d) / harmonyMemory.instances.numInstances() + ")");
        System.out.println("|" + "Cardinality Score: " + cardinalityScore + " (" + (cardinalitySum + 0d) / (rules.size() * harmonyMemory.features.length) + ")");
        System.out.println("| Redundancy = " + harmonyMemory.redundancy + "\n");*/

        /*double merit = (coverageScore * weights[0] +
                approximateScore * weights[1] +
                sizeScore * weights[2] +
                cardinalityScore * weights[3]) /
                (weights[0] + weights[1] + weights[2] + weights[3]);*/

        double merit = (sizeScore + cardinalityScore) / 2;

        harmony.setMerit(merit);
    }

    public void evaluateVerbose(Harmony harmony) {
        double[] weights = new double[]{0.5d, 2d, 0.5d, 0.5d};

        ArrayList<FuzzyRule> rules = translate(harmony);

        double coverageScore = evaluateCoverage(rules);

        double approximateScore = 0d;
        int approximateCounter = 0;
        for (Note note : harmony.notes) {
            if (note.value != null) {
                if (globalLowerApproximation[note.musician.instance] != 0) {
                    approximateCounter++;
                    approximateScore += note.lowerApproximation / globalLowerApproximation[note.musician.instance];
                }
            }
        }
        approximateScore = approximateScore / approximateCounter;

        double sizeScore = 1 - (rules.size() + 0d) / harmonyMemory.instances.numInstances();

        int cardinalitySum = 0;
        for (int i = 0; i < rules.size(); i++) {
            cardinalitySum += rules.get(i).getFeatures().cardinality();
        }
        double cardinalityScore = 1 - (cardinalitySum + 0d) / (rules.size() * harmonyMemory.features.length);

        calculateRedundancy(coverageScore);

        System.out.println("|" + "Coverage Score: " + coverageScore);
        System.out.println("|" + "Approximate Score: " + approximateScore);
        System.out.println("|" + "Size Score: " + sizeScore + " (" + (rules.size() + 0d) / harmonyMemory.instances.numInstances() + ")");
        System.out.println("|" + "Cardinality Score: " + cardinalityScore + " (" + (cardinalitySum + 0d) / (rules.size() * harmonyMemory.features.length) + ")");
        System.out.println("| Redundancy = " + harmonyMemory.redundancy + "\n");

        /*double merit = (coverageScore * weights[0] +
                approximateScore * weights[1] +
                sizeScore * weights[2] +
                cardinalityScore * weights[3]) /
                (weights[0] + weights[1] + weights[2] + weights[3]);*/

        double merit = sizeScore;
        System.out.println("--------------------------------------------");
        for (FuzzyRule rule : rules) {
            System.out.println(rule.getFeatures().cardinality());
        }
        System.out.println("--------------------------------------------");

        harmony.setMerit(merit);
    }

    public int compare(Object o1, Object o2) {
        Harmony h1 = (Harmony) o1;
        Harmony h2 = (Harmony) o2;
        if (h1.getMerit() > h2.getMerit()) {
            return 1;
        } else if (h1.getMerit() < h2.getMerit()) {
            return -1;
        }
        return 0;
    }

    public void calculateRedundancy(double coverage) {
        if (coverage < 1) {
            if (harmonyMemory.redundancy < 1d / harmonyMemory.instances.numInstances()) harmonyMemory.redundancy = 0;
            else harmonyMemory.redundancy = harmonyMemory.redundancy / 2;
        } else harmonyMemory.redundancy = harmonyMemory.redundancy * 1.1;
        if (harmonyMemory.redundancy > 1) harmonyMemory.redundancy = 1d;
    }

    public FuzzyRoughCalculator getCalculator() {
        return calculator;
    }
}