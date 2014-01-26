package util;

import weka.attributeSelection.*;

/**
 * Created with IntelliJ IDEA.
 * User: rrd09
 * Date: 05/12/12
 * Time: 13:54
 */
public class ParameterConfigurator {

    public static ASSearch setMaxIteration(ASSearch search, int maxIteration) {
        if (search instanceof AntSearch) {
            ((AntSearch) search).setMaxGenerations(maxIteration);
            return search;
        }
        if (search instanceof GeneticSearch) {
            ((GeneticSearch) search).setMaxGenerations(maxIteration);
            return search;
        }
        if (search instanceof HarmonySearch) {
            ((HarmonySearch) search).setIteration(maxIteration);
            return search;
        }
        if (search instanceof MemeticSearch) {
            ((MemeticSearch) search).setMaxGenerations(maxIteration);
            return search;
        }
        if (search instanceof PSOSearch) {
            ((PSOSearch) search).setMaxGenerations(maxIteration);
            return search;
        }
        if (search instanceof SimulatedAnnealingSearch) {
            ((SimulatedAnnealingSearch) search).setMaxIteration(maxIteration);
            return search;
        }
        if (search instanceof RandomSearch) {
            ((RandomSearch) search).setMaxIteration(maxIteration);
            return search;
        }
        if (search instanceof TabuSearch) {
            ((TabuSearch) search).setM_numIterations(maxIteration);
            return search;
        }
        return search;
    }

    public static ASSearch setSeed(ASSearch search, int seed) {
        if (search instanceof AntColonySearch) {
            ((AntColonySearch) search).setSeed(seed);
            return search;
        }
        if (search instanceof ArtificialBeeColonySearch) {
            ((ArtificialBeeColonySearch) search).setSeed(seed);
            return search;
        }
        if (search instanceof FireflySearch) {
            ((FireflySearch) search).setSeed(seed);
            return search;
        }
        if (search instanceof GeneticSearch) {
            ((GeneticSearch) search).setSeed(seed);
            return search;
        }
        if (search instanceof HarmonySearch) {
            ((HarmonySearch) search).setSeed(seed);
            return search;
        }
        if (search instanceof MemeticSearch) {
            ((MemeticSearch) search).setSeed(seed);
            return search;
        }
        if (search instanceof PSOSearch) {
            ((PSOSearch) search).setSeed(seed);
            return search;
        }
        if (search instanceof SimulatedAnnealingSearch) {
            ((SimulatedAnnealingSearch) search).setSeed(seed);
            return search;
        }
        if (search instanceof ScatterSearchV1) {
            ((ScatterSearchV1) search).setSeed(seed);
            return search;
        }
        if (search instanceof RandomSearch) {
            ((RandomSearch) search).setSeed(seed);
            return search;
        }
        if (search instanceof TabuSearch) {
            ((TabuSearch) search).setSeed(seed);
            return search;
        }
        if (search instanceof ArtificialImmuneSystemSearch) {
            ((ArtificialImmuneSystemSearch) search).setSeed(seed);
            return search;
        }
        return search;
    }

}
