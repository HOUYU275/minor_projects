package model;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 15/01/13
 * Time: 19:30
 */
public class Clusters {
    Cluster[] clusters;
    Centroid[] previousCentroids;

    public Clusters(int k) {
        clusters = new Cluster[k];
        //previousCentroids = new Centroid[k];
    }

    public boolean isConverged() {
        if (previousCentroids == null) {
            previousCentroids = new Centroid[getClusters().length];
            for (int c = 0; c < getClusters().length; c++) previousCentroids[c] = getClusters()[c].getCentroid();
            return false;
        }
        boolean converged = true;
        for (int c = 0; c < getClusters().length; c++) {
            converged &= previousCentroids[c].equals(getClusters()[c].getCentroid());
            previousCentroids[c] = getClusters()[c].getCentroid();
        }
        return converged;
    }

    public void updateCentroids() {
        int numAntecedents = getClusters()[0].getCentroid().values.length - 1;
        for (Cluster cluster : getClusters()) {
            Centroid centroid = new Centroid(numAntecedents);
            for (Rule rule : cluster) {
                for (int a = 0; a < numAntecedents; a++) centroid.values[a] += rule.getAntecedents()[a].rep();
                centroid.values[numAntecedents] += rule.getConsequent().rep();
            }
            for (int v = 0; v < centroid.values.length; v++) centroid.values[v] /= cluster.size();
            cluster.centroid = centroid;
        }
    }

    public double getDunnIndex() {
        double maxIntraDistance = getMaxIntraDistance();
        double min = Double.MAX_VALUE;
        for (int i = 0; i < getClusters().length; i++) {
            for (int j = 0; j < getClusters().length; j++) {
                if (i != j) min = Math.min(min, getInterDistance(i, j));
            }
        }
        return min / maxIntraDistance;
    }

    public double getMaxIntraDistance() {
        double max = -Double.MAX_VALUE;
        double[] distances = getIntraDistances();
        for (double d : distances) max = Math.max(max, d);
        //System.out.println(max);
        return max;
    }

    public double[] getIntraDistances() {
        double[] distances = new double[getClusters().length];
        for (int c = 0; c < getClusters().length; c++) {
            double sum = 0;
            for (Rule rule : getClusters()[c]) sum += Math.pow(rule.distanceTo(getClusters()[c].getCentroid()), 2);
            distances[c] = Math.sqrt(sum / getClusters()[c].size());
            //System.out.println(distances[c]);
        }
        return distances;
    }

    public double getInterDistance(int i, int j) {
        double sum = 0;
        for (int v = 0; v < getClusters()[i].getCentroid().values.length; v++)
            sum += Math.pow(getClusters()[i].getCentroid().values[v] - getClusters()[j].getCentroid().values[v], 2);
        return Math.sqrt(sum / getClusters()[i].getCentroid().values.length);
    }

    public Cluster getLargest() {
        double[] distances = getIntraDistances();
        int maxIndex = 0;
        int maxSize = getClusters()[0].size();
        for (int c = 1; c < getClusters().length; c++) {
            if (getClusters()[c].size() > maxSize || (getClusters()[c].size() == maxSize && distances[c] < distances[maxIndex])) {
                maxSize = getClusters()[c].size();
                maxIndex = c;
            }
        }
        return getClusters()[maxIndex];
    }

    public int getSize() {
        return getClusters().length;
    }

    public String report() {
        String s = "";
        s += "Best K = " + getClusters().length + "\n";
        s += "Largest Cluster: " + getLargest();
        return s;
    }

    public Cluster[] getClusters() {
        return clusters;
    }
}
