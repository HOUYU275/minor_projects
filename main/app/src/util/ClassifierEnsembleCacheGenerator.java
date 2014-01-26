package util;

import originalharmonysearch.applications.classifierensemble.DecisionMatrix;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.meta.RandomSubSpace;
import weka.core.Instances;
import weka.core.Randomizable;
import weka.core.Utils;
import weka.filters.unsupervised.attribute.Remove;

import java.util.Random;

/**
 * User: rrd09, Date: 01/02/12, Time: 13:30
 */
public class ClassifierEnsembleCacheGenerator {

    private String datasetName;
    private Classifier classifier;
    private String method;
    private int size;

    private CrossValidation crossValidation;

    public ClassifierEnsembleCacheGenerator() {
    }

    public ClassifierEnsembleCacheGenerator(String datasetName) {
        this.datasetName = datasetName;
        this.crossValidation = new CrossValidation(datasetName, true);
    }

    public ClassifierEnsembleCacheGenerator(String datasetName, Classifier classifier, String method, int size) {
        this.datasetName = datasetName;
        this.classifier = classifier;
        this.method = method;
        this.size = size;
    }

    public void setClassifier(Classifier classifier) {
        this.classifier = classifier;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void generateCache() {
        Instances instances;
        String path = Registry.ensembleCachePath +
                datasetName + "//" +
                classifier.getClass().getName() + "//" +
                method + "//" +
                size + "//";
        for (int foldIndex = 0; foldIndex < crossValidation.getNumFolds(); foldIndex++) {
            instances = crossValidation.getTrainingFold(foldIndex);

            try {
                Classifier[] classifiers = generateBaseClassifiers(classifier, size, method, instances);
                for (int classifierIndex = 0; classifierIndex < classifiers.length; classifierIndex++) {
                    ObjectSerialisation.exportClassifier(classifiers[classifierIndex], path + foldIndex + "//" + classifierIndex + ".classifier");
                }
                DecisionMatrix decisionMatrix = new DecisionMatrix(classifiers, instances);
                decisionMatrix.build();
                decisionMatrix.exportToWEKAFile(path + foldIndex + "//" + "decision_matrix.arff");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Classifier[] generateBaseClassifiers(Classifier baseClassifier, int size, String method, Instances data) throws Exception {
        Classifier[] generatedBaseClassifiers = new Classifier[size];
        switch (method) {
            case "bagging":
                generatedBaseClassifiers = generateBagging(baseClassifier, generatedBaseClassifiers, data);
                break;
            case "subspace":
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
                int subSpaceSize = randomSubSpace.numberOfAttributes(indices.length, 0.5);
                long m_Seed = 1;
                Random random = data.getRandomNumberGenerator(m_Seed);

                for (int j = 0; j < generatedBaseClassifiers.length; j++) {
                    if (baseClassifier instanceof Randomizable) {
                        ((Randomizable) generatedBaseClassifiers[j]).setSeed(random.nextInt());
                    }
                    FilteredClassifier fc = new FilteredClassifier();
                    fc.setClassifier(baseClassifier);
                    generatedBaseClassifiers[j] = fc;
                    Remove rm = new Remove();
                    rm.setOptions(new String[]{"-V", "-R", randomSubSpace.randomSubSpace(indices, subSpaceSize, classIndex + 1, random)});
                    fc.setFilter(rm);
                    generatedBaseClassifiers[j].buildClassifier(data);
                }
                break;
            default:
                generatedBaseClassifiers = generateBagging(baseClassifier, generatedBaseClassifiers, data);
                break;
        }
        return generatedBaseClassifiers;
    }

    public static Classifier[] generateBagging(Classifier classifier, Classifier[] classifiers, Instances data) throws Exception {
        boolean m_CalcOutOfBag = false;
        int m_BagSizePercent = 100;

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

    public static final Instances resampleWithWeights(Instances data, Random random, boolean[] sampled) {

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

}
