package test;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 04/08/11
 * Time: 16:58
 * To change this template use File | Settings | File Templates.
 */
public class FeatureSelectionResult extends ExperimentalResult {

    private int[] selectedSubset;
    private int subsetSize;
    private double merit;
    private long time;

    public FeatureSelectionResult() {
    }

    public FeatureSelectionResult(String datasetName, String method, int[] selectedSubset, double merit) {
        super.setDatasetName(datasetName);
        super.setMethod(method);
        this.selectedSubset = selectedSubset;
        this.subsetSize = selectedSubset.length;
        this.merit = merit;
    }

    public FeatureSelectionResult(String method, int[] selectedSubset, double merit, long time) {
        super.setMethod(method);
        if (selectedSubset == null) selectedSubset = new int[0];
        this.selectedSubset = selectedSubset;
        this.subsetSize = selectedSubset.length;
        this.merit = merit;
        this.time = time;
    }

    /*public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }*/

    public int[] getSelectedSubset() {
        return selectedSubset;
    }

    public void setSelectedSubset(int[] selectedSubset) {
        this.selectedSubset = selectedSubset;
    }

    public int getSubsetSize() {
        return subsetSize;
    }

    public void setSubsetSize(int subsetSize) {
        this.subsetSize = subsetSize;
    }

    public double getMerit() {
        return merit;
    }

    public void setMerit(double merit) {
        this.merit = merit;
    }

    /*public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }*/

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getFeatures() {
        StringBuffer stringBuffer = new StringBuffer();
        //stringBuffer.append(selectedSubset[0]);
        for (int i = 0; i < selectedSubset.length; i++) {
            stringBuffer.append(" " + selectedSubset[i]);
        }
        return stringBuffer.toString().trim();
    }

    public String outputFormat() {
        return subsetSize + "\t" + merit + "\t" + time + "\t" + getFeatures();
    }

    @Override
    public String toString() {
        //System.out.println(subsetSize + "\t" + merit);
        return subsetSize + "\t" + merit + "\t" + time;    //TODO: Automatically Generated Override Method
    }
}
