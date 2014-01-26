package model;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 15/02/12
 * Time: 12:55
 */
public class Car extends Vehicle {
    public Car(double acceleration, double maximumSpeed) {
        super(acceleration, maximumSpeed, 4, 4, 4);
    }

    public Car(double maximumSpeed) {
        super(0, maximumSpeed, 4, 4, 4);
    }
}
