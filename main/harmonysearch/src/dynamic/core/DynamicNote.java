package dynamic.core;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 10/01/13
 * Time: 11:00
 */
public class DynamicNote {
    protected int value;

    public DynamicNote(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DynamicNote that = (DynamicNote) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return value;
    }
}
