package util;

import test.ExperimentalResult;
import weka.core.Instances;

/**
 * User: rrd09, Date: 24/11/11, Time: 11:04
 */
public abstract class CrossValidator implements Runnable {

    private CrossValidation mother;
    private int numFold;

    public void run() {
        //System.out.println(numFold + " " + getMother().getResultCollector().getRepeat());
        for (int i = 0; i < getMother().getResultCollector().getRepeat(); i++) {
            report(validate(), i);
        }
    }

    public Instances getInstances() {
        return this.mother.getTrainingFold(numFold);
    }

    public CrossValidation getMother() {
        return mother;
    }

    public void setMother(CrossValidation mother) {
        this.mother = mother;
    }

    public int getNumFold() {
        return numFold;
    }

    public void setNumFold(int numFold) {
        this.numFold = numFold;
    }

    public abstract void report(ExperimentalResult result, int repeat);

    public abstract ExperimentalResult validate();
}
