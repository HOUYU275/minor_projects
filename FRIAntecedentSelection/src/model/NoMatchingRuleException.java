package model;

/**
 * Created with IntelliJ IDEA.
 * User: rrd09
 * Date: 17/08/13
 * Time: 22:17
 */
public class NoMatchingRuleException extends Exception {
    public NoMatchingRuleException(){
        super("No Matching Rules");
    }
}
