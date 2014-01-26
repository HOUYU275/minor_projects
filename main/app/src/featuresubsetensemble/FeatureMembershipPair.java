package featuresubsetensemble;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 15/10/12
 * Time: 09:46
 * To change this template use File | Settings | File Templates.
 */
public class FeatureMembershipPair implements Comparable{
    private int feature;
    private double membership;

    public FeatureMembershipPair(int feature) {
        this.feature = feature;
        this.membership = 0d;
    }

    public FeatureMembershipPair(int feature, double membership) {
        this.feature = feature;
        this.membership = membership;
    }

    public int getFeature() {
        return feature;
    }

    public void setFeature(int feature) {
        this.feature = feature;
    }

    public double getMembership() {
        return membership;
    }

    public void setMembership(double membership) {
        this.membership = membership;
    }

    @Override
    public int compareTo(Object o) {
        FeatureMembershipPair pair = (FeatureMembershipPair)o;
        if (this.membership > pair.getMembership()) return 1;
        if (this.membership < pair.getMembership()) return -1;
        return 0;
    }
}
