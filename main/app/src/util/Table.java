package util;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 22/01/13
 * Time: 12:57
 */
public class Table {
    private Vector<Vector<String>> table;

    public Table() {
        table = new Vector<>();
        table.add(new Vector<String>());
    }

    public void add(String s) {
        String[] split = s.split("\t");
        for (String aSplit : split) table.lastElement().add(aSplit);
    }

    public void add(double[] ds) {
        for (double d : ds) table.lastElement().add(String.valueOf(d));
    }

    public void add(Object o) {
        table.lastElement().add(o.toString());
    }

    public void newRow() {
        table.add(new Vector<String>());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Vector<String> aTable : table) {
            for (String anATable : aTable) builder.append(anATable).append("\t");
            builder.append("\n");
        }
        return builder.toString();
    }

    public void clear() {
        table = new Vector<>();
        table.add(new Vector<String>());
    }
}
