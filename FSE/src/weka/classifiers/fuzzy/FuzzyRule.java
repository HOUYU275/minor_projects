package weka.classifiers.fuzzy;

import java.io.Serializable;
import java.util.Arrays;
import java.util.BitSet;

/**
 * Created by IntelliJ IDEA.
 * User: Ren
 * Date: 31/12/11
 * Time: 16:54
 * To change this template use File | Settings | File Templates.
 */
public class FuzzyRule implements Serializable {
    static final long serialVersionUID = -3080186098777067142L;

    double[] equivalenceClasses;
    BitSet features;
    int instance;

    public FuzzyRule(double[] v, BitSet a, int obj) {
        equivalenceClasses = v;
        features = a;
        instance = obj;
    }

    public int hashCode() {
        return Arrays.hashCode(equivalenceClasses);
    }

    public String toString() {
        String ret = "Rule - \n " + features.toString() + ":\n Object: " + instance + "\n Eq class: [ ";
        for (int i = 0; i < equivalenceClasses.length; i++) ret += equivalenceClasses[i] + " ";
        ret += "]";
        return ret;
    }

    public double[] getEquivalenceClasses() {
        return equivalenceClasses;
    }

    public void setEquivalenceClasses(double[] equivalenceClasses) {
        this.equivalenceClasses = equivalenceClasses;
    }

    public BitSet getFeatures() {
        return features;
    }

    public void setFeatures(BitSet features) {
        this.features = features;
    }

    public int getInstance() {
        return instance;
    }

    public void setInstance(int instance) {
        this.instance = instance;
    }
}