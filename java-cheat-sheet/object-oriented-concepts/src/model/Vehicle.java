package model;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 15/02/12
 * Time: 12:49
 */
public abstract class Vehicle extends Transport {
    private int numDoors;
    private int numSeats;
    private int numWheels;

    protected Vehicle() {
    }

    protected Vehicle(double acceleration, double maximumSpeed, int numDoors, int numSeats, int numWheels) {
        super(acceleration, maximumSpeed);
        this.numDoors = numDoors;
        this.numSeats = numSeats;
        this.numWheels = numWheels;
    }

    protected Vehicle(int numDoors, int numSeats, int numWheels) {
        super();
        this.numDoors = numDoors;
        this.numSeats = numSeats;
        this.numWheels = numWheels;
    }

    public int getNumDoors() {
        return numDoors;
    }

    public void setNumDoors(int numDoors) {
        this.numDoors = numDoors;
    }

    public int getNumSeats() {
        return numSeats;
    }

    public void setNumSeats(int numSeats) {
        this.numSeats = numSeats;
    }

    public int getNumWheels() {
        return numWheels;
    }

    public void setNumWheels(int numWheels) {
        this.numWheels = numWheels;
    }
}
