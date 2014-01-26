package survey;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 07/12/12
 * Time: 13:51
 */
public class ClassificationDetail implements Serializable {

    private static final long serialVersionUID = 7170244136262040829L;
    private double percent_correct;//5
    private double root_mean_squared_error;//10
    private double true_positive_rate;//22
    private double true_negative_rate;//24
    private double precision;//30
    private double recall;//31
    private double f_measure;//32
    private double area_under_ROC;//33
    private double area_under_PRC;//34

    public ClassificationDetail(double percent_correct, double root_mean_squared_error, double true_positive_rate, double true_negative_rate, double precision, double recall, double f_measure, double area_under_ROC, double area_under_PRC) {
        this.percent_correct = percent_correct;
        this.root_mean_squared_error = root_mean_squared_error;
        this.true_positive_rate = true_positive_rate;
        this.true_negative_rate = true_negative_rate;
        this.precision = precision;
        this.recall = recall;
        this.f_measure = f_measure;
        this.area_under_ROC = area_under_ROC;
        this.area_under_PRC = area_under_PRC;
    }

    public ClassificationDetail() {
    }

    public ClassificationDetail(Object[] result) {
        this.percent_correct = (double)result[5];
        this.root_mean_squared_error = (double)result[10];
        this.true_positive_rate = (double)result[22];
        this.true_negative_rate = (double)result[24];
        this.precision = (double)result[30];
        this.recall = (double)result[31];
        this.f_measure = (double)result[32];
        this.area_under_ROC = (double)result[33];
        this.area_under_PRC = (double)result[34];
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("percent_correct" + "	" + percent_correct+ "\n");
        buffer.append("root_mean_squared_error" + "	" + root_mean_squared_error+ "\n");
        buffer.append("true_positive_rate" + "	" + true_positive_rate+ "\n");
        buffer.append("true_negative_rate" + "	" + true_negative_rate+ "\n");
        buffer.append("precision" + "	" + precision+ "\n");
        buffer.append("recall" + "	" + recall+ "\n");
        buffer.append("f_measure" + "	" + f_measure+ "\n");
        buffer.append("area_under_ROC" + "	" + area_under_ROC+ "\n");
        buffer.append("area_under_PRC" + "	" + area_under_PRC);
        return buffer.toString();
    }

    public double getPercent_correct() {
        return percent_correct;
    }

    public void setPercent_correct(double percent_correct) {
        this.percent_correct = percent_correct;
    }

    public double getRoot_mean_squared_error() {
        return root_mean_squared_error;
    }

    public void setRoot_mean_squared_error(double root_mean_squared_error) {
        this.root_mean_squared_error = root_mean_squared_error;
    }

    public double getTrue_positive_rate() {
        return true_positive_rate;
    }

    public void setTrue_positive_rate(double true_positive_rate) {
        this.true_positive_rate = true_positive_rate;
    }

    public double getTrue_negative_rate() {
        return true_negative_rate;
    }

    public void setTrue_negative_rate(double true_negative_rate) {
        this.true_negative_rate = true_negative_rate;
    }

    public double getPrecision() {
        return precision;
    }

    public void setPrecision(double precision) {
        this.precision = precision;
    }

    public double getRecall() {
        return recall;
    }

    public void setRecall(double recall) {
        this.recall = recall;
    }

    public double getF_measure() {
        return f_measure;
    }

    public void setF_measure(double f_measure) {
        this.f_measure = f_measure;
    }

    public double getArea_under_ROC() {
        return area_under_ROC;
    }

    public void setArea_under_ROC(double area_under_ROC) {
        this.area_under_ROC = area_under_ROC;
    }

    public double getArea_under_PRC() {
        return area_under_PRC;
    }

    public void setArea_under_PRC(double area_under_PRC) {
        this.area_under_PRC = area_under_PRC;
    }
}
