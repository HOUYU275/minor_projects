package test;

import weka.core.Instance;

/**
 * Created with IntelliJ IDEA.
 * User: rrd09
 * Date: 23/11/12
 * Time: 11:23
 */
public class Person {
    private Instance[] input = new Instance[3];
    private Instance output;

    public Person(Instance i1, Instance i2, Instance i3) {
        this.input[0] = i1;
        this.input[1] = i2;
        this.input[2] = i3;
        this.output = (Instance) i1.copy();
    }

    public Instance aggregate() {
        for (int i = 0; i < output.numAttributes() - 1; i++) {
            output.setValue(i, TripleAggregator.aggregate(input[0].value(i), input[1].value(i), input[2].value(i)));
        }
        return output;
    }


}
