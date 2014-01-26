package util;

import weka.core.Instances;

import java.io.IOException;
import java.util.Random;

/**
 * User: rrd09, Date: 22/11/11, Time: 12:46
 */
public class CrossValidation {

    private Instances[] testingFolds;
    private Instances[] trainingFolds;
    private int numFolds;
    private Instances data;

    public CrossValidation(Instances data, int numFolds) {
        this.numFolds = numFolds;
        this.data = data;
        testingFolds = new Instances[numFolds];
        trainingFolds = new Instances[numFolds];
        try {
            generateFolds();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CrossValidation(Instances data, int numFolds, Random random) {
        this.numFolds = numFolds;
        this.data = data;
        testingFolds = new Instances[numFolds];
        trainingFolds = new Instances[numFolds];
        try {
            generateFolds(random);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CrossValidation(Instances data) {
        this.numFolds = 10;
        this.data = data;
        testingFolds = new Instances[numFolds];
        trainingFolds = new Instances[numFolds];
        try {
            generateFolds();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CrossValidation(String datasetName) {
        this.numFolds = 10;
        try {
            this.data = Dataset.getDataset(datasetName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        testingFolds = new Instances[numFolds];
        trainingFolds = new Instances[numFolds];
        try {
            generateFolds();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CrossValidation(String datasetName, boolean useCache) {
        if (useCache) {
            this.numFolds = 10;
            trainingFolds = new Instances[numFolds];
            testingFolds = new Instances[numFolds];
            for (int i = 0; i < numFolds; i++) {
                try {
                    trainingFolds[i] = InstancesCache.loadCache(datasetName, true, i);
                    testingFolds[i] = InstancesCache.loadCache(datasetName, false, i);
                    trainingFolds[i].setName(datasetName);
                    testingFolds[i].setName(datasetName);
                } catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            this.numFolds = 10;
            try {
                this.data = Dataset.getDataset(datasetName);
                this.data.setName(datasetName);
            } catch (IOException e) {
                e.printStackTrace();
            }
            testingFolds = new Instances[numFolds];
            trainingFolds = new Instances[numFolds];
            try {
                generateFolds(new Random(new Random().nextLong()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public CrossValidation(String datasetName, int repeat) {
        this.numFolds = 10;
        trainingFolds = new Instances[numFolds];
        testingFolds = new Instances[numFolds];
        for (int i = 0; i < numFolds; i++) {
            try {
                trainingFolds[i] = InstancesCache.loadRepeatedCache(datasetName, true, i, repeat);
                testingFolds[i] = InstancesCache.loadRepeatedCache(datasetName, false, i, repeat);
                trainingFolds[i].setName(datasetName);
                testingFolds[i].setName(datasetName);
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public CrossValidation(String datasetName, boolean useCache, boolean noMissing) {
        if (useCache) {
            this.numFolds = 10;
            trainingFolds = new Instances[numFolds];
            testingFolds = new Instances[numFolds];
            for (int i = 0; i < numFolds; i++) {
                try {
                    trainingFolds[i] = InstancesCache.loadCache(datasetName, true, i);
                    testingFolds[i] = InstancesCache.loadCache(datasetName, false, i);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            this.numFolds = 10;
            try {
                this.data = Dataset.getDataset(datasetName);
                for (int i = 0; i < data.numAttributes() - 1; i++) {
                    this.data.deleteWithMissing(i);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            testingFolds = new Instances[numFolds];
            trainingFolds = new Instances[numFolds];
            try {
                generateFolds();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void generateFolds() throws Exception {
        data.stratify(numFolds);
        for (int i = 0; i < numFolds; i++) {
            trainingFolds[i] = data.trainCV(numFolds, i);
            testingFolds[i] = data.testCV(numFolds, i);
        }
    }

    public void generateFolds(Random random) throws Exception {
        data.stratify(numFolds);
        data.randomize(random);
        for (int i = 0; i < numFolds; i++) {
            trainingFolds[i] = data.trainCV(numFolds, i, random);
            testingFolds[i] = data.testCV(numFolds, i);
            trainingFolds[i].setName(data.getName());
            testingFolds[i].setName(data.getName());
        }
    }

    public Instances getTrainingFold(int index) {
        //trainingFolds[index].setClassIndex(data.classIndex());
        return trainingFolds[index];
    }

    public Instances getTestingFold(int index) {
        //testingFolds[index].setClassIndex(data.classIndex());
        return testingFolds[index];
    }

    public Instances[] getTestingFolds() {
        return testingFolds;
    }

    public void setTestingFolds(Instances[] testingFolds) {
        this.testingFolds = testingFolds;
    }

    public Instances[] getTrainingFolds() {
        return trainingFolds;
    }

    public void setTrainingFolds(Instances[] trainingFolds) {
        this.trainingFolds = trainingFolds;
    }

    public int getNumFolds() {
        return numFolds;
    }

    public void setNumFolds(int numFolds) {
        this.numFolds = numFolds;
    }

    public Instances getData() {
        return data;
    }

    public void setData(Instances data) {
        this.data = data;
    }

}
