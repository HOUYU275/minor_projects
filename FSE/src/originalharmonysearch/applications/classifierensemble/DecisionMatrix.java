package originalharmonysearch.applications.classifierensemble;

import weka.classifiers.Classifier;
import weka.core.Instances;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 23-Mar-2010
 * Time: 14:45:19
 * To change this template use File | Settings | File Templates.
 */
public class DecisionMatrix implements Serializable {

    private int numClassifiers;
    private int numInstances;
    private int numClasses;
    private int[][] decisions;
    private ClassLabel[] classLabels;
    private Classifier[] classifiers;
    private int[][] decisionCount;
    private int[][] agreementCount;
    private int[][] labelCount;
    private float[][] kappas;
    private Instances data;

    public DecisionMatrix() {

    }

    //public DecisionMatrix(int numClassifiers, int numInstances, int numClasses) {

    public DecisionMatrix(Classifier[] classifiers, Instances data) {
        this.classifiers = classifiers;
        this.numClassifiers = classifiers.length;
        this.data = data;
        this.numInstances = data.numInstances();
        this.numClasses = data.numClasses();
        decisions = new int[numClassifiers][numInstances];
        classLabels = new ClassLabel[numClasses];
        for (int i = 0; i < numClasses; i++) {
            classLabels[i] = new ClassLabel();
        }
        //classifiers = new Classifier[numClassifiers];
        decisionCount = new int[numClassifiers][numClasses];
        agreementCount = new int[numClassifiers][numClassifiers];
        labelCount = new int[numInstances][numClasses];
        kappas = new float[numClassifiers][numClassifiers];
    }

    public void setDecisions(int index, int[] singleClassifierDecisions) throws Exception {
        if (decisions[index].length != singleClassifierDecisions.length)
            throw new Exception("Unmatched Instance Count");
        decisions[index] = singleClassifierDecisions;
    }

    public void printDecisions() {
        System.out.println("Decision Matrix numClassifiers = " + numClassifiers + " numInstances = " + numInstances);
        for (int i = 0; i < numClassifiers; i++) {
            for (int j = 0; j < numInstances; j++) {
                System.out.print(" " + decisions[i][j]);
            }
            System.out.print("\n");
        }
    }

    public void printWekaData() {
        System.out.println("@RELATION Algae\n");
        for (int j = 0; j < numClassifiers; j++) {
            System.out.println("@ATTRIBUTE " + j + " REAL");
        }
        System.out.println("@ATTRIBUTE class {1,2}\n\n@DATA");
        for (int i = 0; i < numInstances; i++) {

            for (int j = 0; j < numClassifiers; j++) {
                System.out.print(" " + decisions[j][i]);
            }

            System.out.print(" " + data.instance(i).classValue());
            System.out.print("\n");
        }
    }

    public void build() throws Exception {
        int[] singleClassifierDecisions;
        for (int i = 0; i < classifiers.length; i++) {
            singleClassifierDecisions = new int[data.numInstances()];
            for (int j = 0; j < data.numInstances(); j++) {
                double[] prob = classifiers[i].distributionForInstance(data.instance(j));
                int maxIndex = 0;
                for (int k = 0; k < prob.length; k++) {
                    if (prob[k] > prob[maxIndex]) {
                        maxIndex = k;
                    }
                }
                singleClassifierDecisions[j] = maxIndex;
            }
            setDecisions(i, singleClassifierDecisions);
        }
    }

    public String printWekaDataToFile() {
        FileOutputStream out; // declare a file output object
        PrintStream p; // declare a println stream object

        try {
            String fileName = "tempWekaFile" + new Random().nextDouble();
            // Create a new file output stream
            // connected to "myfile.txt"
            out = new FileOutputStream(fileName + ".arff");

            // Connect println stream to the output stream
            p = new PrintStream(out);

            p.println("@RELATION Algae\n");
            for (int j = 0; j < numClassifiers; j++) {
                p.println("@ATTRIBUTE " + j + " REAL");
            }
            p.print("@ATTRIBUTE class {");
            for (int i = 0; i < data.classAttribute().numValues(); i++) {
                if (i == 0)
                    p.print(data.classAttribute().value(i));
                else
                    p.print("," + data.classAttribute().value(i));
            }
            p.print("}\n\n@DATA\n");
            for (int i = 0; i < numInstances; i++) {
                for (int j = 0; j < numClassifiers; j++) {
                    p.print(" " + decisions[j][i] + ".0");
                }
                //System.out.println();
                p.print(" " + data.classAttribute().value((int) data.instance(i).classValue()));
                p.print("\n");
            }

            //p.println ("This is written to a file");

            p.close();
            //System.out.println("File Output to \"" + fileName + "\"");
            return fileName;
        } catch (Exception e) {
            System.err.println("Error writing to file");
        }
        return null;
    }

    public String exportToWEKAFile(String fileName) {
        FileOutputStream out; // declare a file output object
        PrintStream p; // declare a println stream object

        try {
            // Create a new file output stream
            // connected to "myfile.txt"
            out = new FileOutputStream(fileName);

            // Connect println stream to the output stream
            p = new PrintStream(out);

            p.println("@RELATION DecisionMatrix\n");
            for (int j = 0; j < numClassifiers; j++) {
                p.println("@ATTRIBUTE " + j + " REAL");
            }
            p.print("@ATTRIBUTE class {");
            for (int i = 0; i < data.classAttribute().numValues(); i++) {
                if (i == 0)
                    p.print(data.classAttribute().value(i));
                else
                    p.print("," + data.classAttribute().value(i));
            }
            p.print("}\n\n@DATA\n");
            for (int i = 0; i < numInstances; i++) {

                for (int j = 0; j < numClassifiers; j++) {
                    p.print(" " + decisions[j][i] + ".0");
                }
                //System.out.println();
                p.print(" " + data.classAttribute().value((int) data.instance(i).classValue()));
                p.print("\n");
            }

            //p.println ("This is written to a file");

            p.close();
            //System.out.println("File Output to \"" + fileName + "\"");
            return fileName;
        } catch (Exception e) {
            System.err.println("Error writing to file");
        }
        return null;
    }

    public void printInstanceLabels() {
        System.out.println("Instance Labels numClassifiers = " + numClassifiers + " numInstances = " + numInstances);
        for (int i = 0; i < numInstances; i++) {

            for (int j = 0; j < numClassifiers; j++) {
                System.out.print(" " + decisions[j][i]);
            }

            System.out.print(" " + data.instance(i).classValue());
            System.out.print("\n");
        }
    }

    public void printDecisionCount() {
        System.out.println("Decision Count");
        for (int i = 0; i < numClassifiers; i++) {
            for (int j = 0; j < numClasses; j++) {
                System.out.print(" " + decisionCount[i][j]);
            }
            System.out.print("\n");
        }
    }

    public void printAgreementCount() {
        System.out.println("Agreement Count");
        for (int i = 0; i < numClassifiers; i++) {
            for (int j = 0; j < numClassifiers; j++) {
                System.out.print("\t" + agreementCount[i][j]);
            }
            System.out.print("\n");
        }
    }

    public void printKappas() {
        System.out.println("Kappas");
        for (int i = 0; i < numClassifiers; i++) {
            for (int j = 0; j < numClassifiers; j++) {
                System.out.print("\t" + kappas[i][j]);
            }
            System.out.print("\n");
        }
    }

    public void countDecisions() {
        for (int i = 0; i < numClassifiers; i++) {
            for (int j = 0; j < numInstances; j++) {
                decisionCount[i][decisions[i][j]] = decisionCount[i][decisions[i][j]] + 1;
            }
        }
    }

    private void countLabels() {
        labelCount = new int[numInstances][numClasses];
        for (int i = 0; i < numInstances; i++) {
            for (int j = 0; j < numClassifiers; j++) {
                labelCount[i][decisions[j][i]] = labelCount[i][decisions[j][i]] + 1;
            }
        }
    }

    private void countLabels(int[] selected) {
        labelCount = new int[numInstances][numClasses];
        for (int i = 0; i < numInstances; i++) {
            for (int j = 0; j < selected.length; j++) {
                labelCount[i][decisions[selected[j]][i]] = labelCount[i][decisions[selected[j]][i]] + 1;
            }
        }
    }

    public void printLabels() {
        for (int i = 0; i < numInstances; i++) {
            for (int j = 0; j < numClasses; j++) {
                System.out.print(" " + labelCount[i][j]);
            }
            System.out.println();
        }
    }

    public double calculateEntropy() {
        countLabels();
        double entropy = 0d;
        double p = 0d;
        for (int i = 0; i < numClasses; i++) {
            for (int j = 0; j < numInstances; j++) {
                p = (double) labelCount[j][i] / (double) numClassifiers;
                //System.out.println(p + " " + p * Math.log(p));
                entropy = (p == 0d) ? entropy : entropy - p * Math.log(p);
            }
        }
        //System.out.println("Entropy = " + entropy);
        return entropy;
    }

    public double calculateEntropy(int[] selected) {
        countLabels(selected);
        double entropy = 0d;
        double p = 0d;
        for (int i = 0; i < numClasses; i++) {
            for (int j = 0; j < numInstances; j++) {
                p = (double) labelCount[j][i] / (double) selected.length;
                //System.out.println(p + " " + p * Math.log(p));
                entropy = (p == 0d) ? entropy : entropy - p * Math.log(p);
            }
        }
        //System.out.println("Entropy = " + entropy);
        return entropy;
    }

    public void countAgreements() {
        for (int j = 0; j < numInstances; j++) {
            for (int k = 0; k < numClasses; k++) {
                classLabels[k].clear();
            }
            for (int i = 0; i < numClassifiers; i++) {
                classLabels[decisions[i][j]].add(i);
            }
            for (int l = 0; l < numClasses; l++) {
                for (int m = 0; m < classLabels[l].size(); m++) {
                    for (int n = 0; n < classLabels[l].size(); n++) {
                        agreementCount[classLabels[l].get(m)][classLabels[l].get(n)] = agreementCount[classLabels[l].get(m)][classLabels[l].get(n)] + 1;
                    }
                }
            }
        }
    }

    //redundant runs atm

    public void calculateKappas() {
        float sigma1 = 0f;
        float sigma2 = 0f;
        float kappa = 0f;
        for (int i = 0; i < numClassifiers; i++) {
            for (int j = 0; j < numClassifiers; j++) {
                sigma1 = agreementCount[i][j] / numInstances;
                //sigma2 =
                for (int k = 0; k < numClasses; k++) {
                    sigma2 = sigma2 + decisionCount[i][k] * decisionCount[j][k];
                }
                sigma2 = sigma2 / (numInstances * numInstances);
                kappas[i][j] = (sigma1 - sigma2) / (1f - sigma2);
            }
        }
    }

    public void randomFill() {
        Random random = new Random();
        float roll;
        float interval = 1f / numClasses;
        float floored = 0f;
        int decision;
        for (int i = 0; i < numClassifiers; i++) {
            for (int j = 0; j < numInstances; j++) {
                for (int k = 0; k < numClasses; k++) {
                    roll = random.nextFloat();
                    floored = (float) Math.floor(roll / interval);
                }
                decision = (int) floored;
                if ((decision > 0) && (decision < numClasses - 1)) {
                    decision = (random.nextBoolean()) ? decision + random.nextInt(2) : decision - random.nextInt(2);
                }
                decisions[i][j] = decision;
            }
        }
    }

    public int[][] getDecisionCount() {
        return decisionCount;
    }

    public void setDecisionCount(int[][] decisionCount) {
        this.decisionCount = decisionCount;
    }

    public int[][] getAgreementCount() {
        return agreementCount;
    }

    public void setAgreementCount(int[][] agreementCount) {
        this.agreementCount = agreementCount;
    }

    public float[][] getKappas() {
        return kappas;
    }

    public void setKappas(float[][] kappas) {
        this.kappas = kappas;
    }

    public int getNumClassifiers() {
        return numClassifiers;
    }

    public void setNumClassifiers(int numClassifiers) {
        this.numClassifiers = numClassifiers;
    }

    public int getNumInstances() {
        return numInstances;
    }

    public void setNumInstances(int numInstances) {
        this.numInstances = numInstances;
    }

    public int getNumClasses() {
        return numClasses;
    }

    public void setNumClasses(int numClasses) {
        this.numClasses = numClasses;
    }

    public int[][] getDecisions() {
        return decisions;
    }

    public void setDecisions(int[][] decisions) {
        this.decisions = decisions;
    }

    public ClassLabel[] getClassLabels() {
        return classLabels;
    }

    public void setClassLabels(ClassLabel[] classLabels) {
        this.classLabels = classLabels;
    }

    public Classifier[] getClassifiers() {
        return classifiers;
    }

    public void setClassifiers(Classifier[] classifiers) {
        this.classifiers = classifiers;
    }

}
