package fuzzy;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 12/10/11
 * Time: 11:59
 * To change this template use File | Settings | File Templates.
 */
public class IncompatibleFuzzyNumberTypeException extends Exception{
    public IncompatibleFuzzyNumberTypeException() {
        super("Fuzzy Number Must Be of Same Type");
    }
}
