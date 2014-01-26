package model;

import java.text.DecimalFormat;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 15/01/13
 * Time: 15:39
 */
public class FuzzyNumber {
    protected double a1, a2, a3;

    public FuzzyNumber(double a1, double a2, double a3) {
        this.a1 = a1;
        this.a2 = a2;
        this.a3 = a3;
    }

    public FuzzyNumber(String s) {
        String[] split = s.split(",");
        a1 = Double.valueOf(split[0]);
        a2 = Double.valueOf(split[1]);
        a3 = Double.valueOf(split[2]);
    }

    public FuzzyNumber() {
    }

    public double rep() {
        return (a1 + a2 + a3) / 3;
    }

    @Override
    public String toString() {
        DecimalFormat format = new DecimalFormat("###.#");
        return "(" + format.format(getA1()) + "," + format.format(getA2()) + "," + format.format(getA3()) + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FuzzyNumber that = (FuzzyNumber) o;
        return Double.compare(that.getA1(), getA1()) == 0 && Double.compare(that.getA2(), getA2()) == 0 && Double.compare(that.getA3(), getA3()) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = getA1() != +0.0d ? Double.doubleToLongBits(getA1()) : 0L;
        result = (int) (temp ^ (temp >>> 32));
        temp = getA2() != +0.0d ? Double.doubleToLongBits(getA2()) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = getA3() != +0.0d ? Double.doubleToLongBits(getA3()) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public FuzzyNumber clone() throws CloneNotSupportedException {
        if (this == null) return null;
        return new FuzzyNumber(getA1(), getA2(), getA3());
    }

    public double getA1() {
        return a1;
    }

    public double getA2() {
        return a2;
    }

    public double getA3() {
        return a3;
    }
}
