package weka.classifiers.fuzzy;

import weka.core.Instances;
import weka.fuzzy.implicator.Implicator;
import weka.fuzzy.implicator.ImplicatorKD;
import weka.fuzzy.implicator.ImplicatorLukasiewicz;
import weka.fuzzy.similarity.*;
import weka.fuzzy.snorm.SNorm;
import weka.fuzzy.tnorm.TNorm;
import weka.fuzzy.tnorm.TNormKD;
import weka.fuzzy.tnorm.TNormLukasiewicz;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Ren
 * Date: 30/12/11
 * Time: 21:00
 * To change this template use File | Settings | File Templates.
 */
public class FuzzyRoughCalculator {

    protected Implicator implicator = new ImplicatorKD();
    protected TNorm tNorm = new TNormKD();
    protected SNorm sNorm = tNorm.getAssociatedSNorm();
    protected Instances instances;

    protected Similarity similarityGeneral;
    protected Similarity similarityEqual;
    protected Similarity similarityDecision;

    protected Relation[] fullFeaturePartitions;

    public FuzzyRoughCalculator(Instances instances) {
        this.instances = instances;
        similarityGeneral = new Similarity3(instances);
        similarityEqual = new SimilarityEq(instances);
        similarityDecision = similarityEqual;
        generationIndividualFeaturePartition();
    }

    public double[] getEquivalenceClasses(Relation relation, int instance) {
        double[] equivalenceClasses = new double[instances.numInstances()];
        for (int i = 0; i < instances.numInstances(); i++) {
            equivalenceClasses[i] = relation.getCell(instance, i);
        }
        return equivalenceClasses;
    }

    public double getLowerApproximation(Relation relation, int instance) {
        double lowerApproximation = 1;
        double currentLowerApproximation = 1;
        for (int i = 0; i < instances.numInstances(); i++) {
            currentLowerApproximation = implicator.calculate(
                    relation.getCell(instance, i),
                    getSimilarity(instances.classIndex(), instance, i));
            lowerApproximation = Math.min(currentLowerApproximation, lowerApproximation);
            if (lowerApproximation == 0) break;
        }
        return lowerApproximation;
    }

    public double getLowerApproximation(Integer[] features, int instance) {
        Relation relation = generatePartition(features);
        return getLowerApproximation(relation, instance);
    }

    public double[] getEquivalenceClasses(Integer[] features, int instance) {
        Relation relation = generatePartition(features);
        return getEquivalenceClasses(relation, instance);
    }

    public double[] getGlobalLowerApproximation() {
        Integer[] fullFeatures = new Integer[instances.numAttributes() - 1];
        for (int i = 0; i < fullFeatures.length; i++) fullFeatures[i] = i;
        Relation fullRelation = generatePartition(fullFeatures);
        return getLowerApproximations(fullRelation);
    }

    public double[] getLowerApproximations(Relation relation) {
        double[] lowerApproximation = new double[instances.numInstances()];
        for (int i = 0; i < lowerApproximation.length; i++)
            lowerApproximation[i] = getLowerApproximation(relation, i);
        return lowerApproximation;
    }

    public Relation generatePartition(Integer[] features) {
        return generatePartitionQuick(features);
        /*Relation relation = new Relation(instances.numInstances());
        double currentRelation;
        for (int i = 0; i < instances.numInstances(); i++) {
            relation.setCell(i, i, 1);
            for (int j = i + 1; j < instances.numInstances(); j++) {
                relation.setCell(i, j, getAttributeSimilarity(features[0], i, j));
            }
        }
        if (features.length > 1) {
            for (int k = 1; k < features.length; k++) {
                for (int i = 0; i < instances.numInstances(); i++) {
                    for (int j = i + 1; j < instances.numInstances(); j++) {
                        double sim = getAttributeSimilarity(features[k], i, j);
                        currentRelation = tNorm.calculate(sim, relation.getCell(i, j));
                        relation.setCell(i, j, currentRelation);
                    }
                }
            }
        }
        return relation;*/
    }

    public void generationIndividualFeaturePartition() {
        fullFeaturePartitions = new Relation[instances.numAttributes() - 1];
        for (int feature = 0; feature < fullFeaturePartitions.length; feature++) {
            fullFeaturePartitions[feature] = new Relation(instances.numInstances());
            for (int i = 0; i < instances.numInstances(); i++) {
                fullFeaturePartitions[feature].setCell(i, i, 1);
                for (int j = i + 1; j < instances.numInstances(); j++) {
                    fullFeaturePartitions[feature].setCell(i, j, getAttributeSimilarity(feature, i, j));
                }
            }
        }
    }

    public Relation generatePartitionQuick(Integer[] features) {
        Relation relation = new Relation(instances.numInstances());
        for (int i = 0; i < instances.numInstances(); i++) {
            relation.setCell(i, i, 1);
            for (int j = i + 1; j < instances.numInstances(); j++) {
                relation.setCell(i, j, fullFeaturePartitions[features[0]].getCell(i, j));
            }
        }
        if (features.length > 1) {
            for (int k = 1; k < features.length; k++) {
                for (int i = 0; i < instances.numInstances(); i++) {
                    for (int j = i + 1; j < instances.numInstances(); j++) {
                        relation.setCell(i, j,
                                tNorm.calculate(
                                        fullFeaturePartitions[features[k]].getCell(i, j), relation.getCell(i, j)));
                    }
                }
            }
        }
        return relation;
    }

    public double[] getCoverage(ArrayList<FuzzyRule> rules) {
        double[] coverage = new double[instances.numInstances()];
        for (FuzzyRule rule : rules) {
            for (int i = 0; i < instances.numInstances(); i++) {
                coverage[i] = sNorm.calculate(rule.equivalenceClasses[i], coverage[i]);
            }
        }
        return coverage;
    }

    public double getSimilarity(int feature, int i, int j) {
        double similarity;
        //no decision feature, so each object is distinct
        if (feature < 0 && feature == instances.classIndex()) {
            System.out.println("UNSUPERVISED?!");
            if (i == j) similarity = 1;
            else similarity = 0;
        } else {
            double mainVal = instances.instance(i).value(feature);
            double otherVal = instances.instance(j).value(feature);
            //if it's the class attribute, use the class similarity measure
            //if it's a nominal attribute, then use crisp equiva
            //
            // lence
            //otherwise use the general similarity measure
            if (Double.isNaN(mainVal) || Double.isNaN(otherVal)) {
                similarity = 1;
            } else if (feature == instances.classIndex()) {
                //System.out.println(attr + "class");
                similarity = similarityDecision.similarity(feature, mainVal, otherVal);
            } else if (instances.attribute(feature).isNumeric()) {
                //System.out.println(attr + "num");
                similarity = similarityGeneral.similarity(feature, mainVal, otherVal);
            } else {
                //System.out.println(attr + "else");
                similarity = similarityEqual.similarity(feature, mainVal, otherVal);
            }
        }
        return similarity;
    }

    public double getAttributeSimilarity(int feature, int i, int j) {
        double iValue = instances.instance(i).value(feature);
        double jValue = instances.instance(j).value(feature);
        if (Double.isNaN(iValue) || Double.isNaN(jValue)) {
            return 1;
        } else if (instances.attribute(feature).isNominal()) {
            return similarityEqual.similarity(feature, iValue, jValue);
        } else {
            return similarityGeneral.similarity(feature, iValue, jValue);
        }
    }


    public boolean validate(Integer[] features, int instance, double globalLowerApproximation) {
        double lowerApproximation = getLowerApproximation(features, instance);
        return (lowerApproximation == globalLowerApproximation);
    }

}
