package util;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 19/12/11
 * Time: 11:07
 * To change this template use File | Settings | File Templates.
 */
public class Formatter {

    public static String toReferenceLabel(String input) {
        input = input.toLowerCase();
        input = input.replaceAll("-", "_");
        input = input.replaceAll(" ", "_");
        return input;
    }

}
