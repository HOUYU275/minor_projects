package util;

import test.ExperimentalResult;
import test.FeatureSelectionResult;
import weka.attributeSelection.*;
import weka.core.Instances;

/**
 * User: rrd09, Date: 24/11/11, Time: 11:23
 */
public class FeatureSelectionCrossValidator extends CrossValidator {

    private ASEvaluation asEvaluation;
    private String asSearch;

    public void report(ExperimentalResult result, int repeat) {
        getMother().getResultCollector().put(result.getMethod(), getNumFold(), repeat, result);
    }

    @Override
    public void run() {
        super.run();
    }

    @Override
    public FeatureSelectionResult validate() {
        FeatureSelectionResult result = null;
        if (asSearch.equals("HS")) {
            try {
                result = harmonySearchFeatureSelection(
                        getInstances(),
                        asEvaluation,
                        1500,
                        11,
                        true);
            } catch (Exception e) {
                e.printStackTrace();  //TODO: Automatically Generated Catch Statement
            }
            //Debug.println(result.toString());
            System.out.println("HS\t" + getNumFold() + "\t" + result.outputFormat());
            //writer.newLine();

        } else if (asSearch.equals("PSO")) {
            try {
                result = particleSwarmFeatureSelection(getInstances(), asEvaluation);
            } catch (Exception e) {
                e.printStackTrace();  //TODO: Automatically Generated Catch Statement
            }
            //Debug.println(result.toString());
            System.out.println("PSO\t" + getNumFold() + "\t" + result.outputFormat());
            //writer.append("HC\t" + j + "\t" + k + "\t" + featureSelectionResult.outputFormat());
            //writer.newLine();
            //report(result);
        } else if (asSearch.equals("HC")) {
            try {
                result = hillClimbingFeatureSelection(getInstances(), asEvaluation);
            } catch (Exception e) {
                e.printStackTrace();  //TODO: Automatically Generated Catch Statement
            }
            //Debug.println(result.toString());
            System.out.println("HC\t" + getNumFold() + "\t" + result.outputFormat());
            //writer.append("HC\t" + j + "\t" + k + "\t" + featureSelectionResult.outputFormat());
            //writer.newLine();
            //report(result);
        } else if (asSearch.equals("GA")) {
            try {
                result = geneticAlgorithmFeatureSelection(getInstances(), asEvaluation);
            } catch (Exception e) {
                e.printStackTrace();  //TODO: Automatically Generated Catch Statement
            }
            //Debug.println(result.toString());
            System.out.println("GA\t" + getNumFold() + "\t" + result.outputFormat());
            //writer.append("HC\t" + j + "\t" + k + "\t" + featureSelectionResult.outputFormat());
            //writer.newLine();
            //report(result);
        }
        return result;
    }

    public ASEvaluation getAsEvaluation() {
        return asEvaluation;
    }

    public void setAsEvaluation(ASEvaluation asEvaluation) {
        this.asEvaluation = asEvaluation;
    }

    public String getAsSearch() {
        return asSearch;
    }

    public void setAsSearch(String asSearch) {
        this.asSearch = asSearch;
    }

    public FeatureSelectionResult harmonySearchFeatureSelection(
            Instances instances,
            ASEvaluation asEvaluation,
            int maxIteration,
            int parameterMode,
            boolean iterative) throws Exception {
        long start = System.currentTimeMillis();
        int size = instances.numAttributes() / 3;

        double merit = 0d;
        int[] searchResult = null;

        HarmonySearch harmonySearch = new HarmonySearch();

        //Debug.println(size + "");
        if (iterative) {

            harmonySearch.setIteration(maxIteration);
            harmonySearch.setNumMusicians(size);
            harmonySearch.setParameterMode(parameterMode);
            harmonySearch.search(asEvaluation, instances);

            while ((harmonySearch.getM_bestMerit() >= merit)) {
                //System.out.print("->" + harmonySearch.getSearchResult().length);
                if ((harmonySearch.getM_bestMerit() == merit) & (harmonySearch.getSearchResult().length == size)) {
                    break;
                }

                size = harmonySearch.getSearchResult().length;
                merit = harmonySearch.getM_bestMerit();
                searchResult = harmonySearch.getSearchResult();
                harmonySearch.setNumMusicians(size);
                harmonySearch.search(asEvaluation, instances);
            }
        } else {
            harmonySearch.setIteration(maxIteration);
            harmonySearch.setNumMusicians(size);
            harmonySearch.setParameterMode(parameterMode);
            harmonySearch.search(asEvaluation, instances);
            merit = harmonySearch.getM_bestMerit();
            searchResult = harmonySearch.getSearchResult();
        }

        return new FeatureSelectionResult("HarmonySearch", searchResult, merit, System.currentTimeMillis() - start);
    }

    public FeatureSelectionResult geneticAlgorithmFeatureSelection(
            Instances instances, ASEvaluation asEvaluation) throws Exception {
        long start = System.currentTimeMillis();
        GeneticSearch geneticSearch = new GeneticSearch();
        int[] searchResult = geneticSearch.search(asEvaluation, instances);
        return new FeatureSelectionResult("GeneticSearch", searchResult, geneticSearch.getM_bestMerit(), System.currentTimeMillis() - start);
    }

    public FeatureSelectionResult particleSwarmFeatureSelection(
            Instances instances, ASEvaluation asEvaluation) throws Exception {
        long start = System.currentTimeMillis();
        PSOSearch psoSearch = new PSOSearch();
        int[] searchResult = psoSearch.search(asEvaluation, instances);
        return new FeatureSelectionResult("PSOSearch", searchResult, psoSearch.getM_bestMerit(), System.currentTimeMillis() - start);
    }

    public FeatureSelectionResult hillClimbingFeatureSelection(
            Instances instances, ASEvaluation asEvaluation) throws Exception {
        long start = System.currentTimeMillis();
        GreedyStepwise greedyStepwise = new GreedyStepwise();
        int[] searchResult = greedyStepwise.search(asEvaluation, instances);
        return new FeatureSelectionResult("GreedyStepwise", searchResult, greedyStepwise.getM_bestMerit(), System.currentTimeMillis() - start);
    }

}
