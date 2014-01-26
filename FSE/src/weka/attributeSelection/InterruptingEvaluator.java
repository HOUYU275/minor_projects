package weka.attributeSelection;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 12/12/12
 * Time: 09:21
 */
public interface InterruptingEvaluator {

    public int maximumEvaluation = 20000;

    public void interrupt() throws MaximumEvaluationReachedException;

    public void reset();

    public void turnOff();

    public void pause();

    public void unpause();

}
