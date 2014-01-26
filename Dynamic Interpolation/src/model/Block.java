package model;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 15/01/13
 * Time: 15:57
 */
public class Block extends ArrayList<Rule> {
    protected double[] maximums, minimums;

    public Block(int numAntecedents) {
        super();
        setMaximums(new double[numAntecedents]);
        setMinimums(new double[numAntecedents]);
    }

    @Override
    public String toString() {
        DecimalFormat format = new DecimalFormat("###.##");
        StringBuffer buffer = new StringBuffer();
        buffer.append("Block [");
        for (int a = 0; a < getMaximums().length; a++)
            buffer.append(format.format(getMinimums()[a])).append("-").append(format.format(getMaximums()[a])).append(", ");
        buffer.delete(buffer.length() - 2, buffer.length());
        buffer.append("] ").append(super.size());
        return buffer.toString();
    }

    public double[] getMaximums() {
        return maximums;
    }

    public void setMaximums(double[] maximums) {
        this.maximums = maximums;
    }

    public double[] getMinimums() {
        return minimums;
    }

    public void setMinimums(double[] minimums) {
        this.minimums = minimums;
    }
}
