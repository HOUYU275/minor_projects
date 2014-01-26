package util;

/**
 * User: rrd09, Date: 15-Sep-2010, Time: 10:59:39
 */
public class Debug {

    private static boolean debug = true;

    public static void println(String message) {
        if (debug) System.out.println(message);
    }

    public static void print(String message) {
        if (debug) System.out.print(message);
    }

}
