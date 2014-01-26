package util;

import weka.core.Instances;

/**
 * User: rrd09, Date: 24/11/11, Time: 11:04
 */
public abstract class CrossValidator implements Runnable {

    private CrossValidation mother;
    private int numFold;

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
}
