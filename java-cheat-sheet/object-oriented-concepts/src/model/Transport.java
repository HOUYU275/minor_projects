package model;

import controller.Tracker;
import controller.Utilities;

import java.awt.geom.Point2D;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 15/02/12
 * Time: 12:40
 */
public abstract class Transport implements Movable {
    private Point2D startPosition;
    private Point2D currentPosition;
    private Point2D goalPosition;

    private double currentSpeed;
    private double acceleration;
    private double maximumSpeed;

    private int currentDirection;

    private Tracker tracker = new Tracker();

    protected Transport() {
    }

    protected Transport(double acceleration, double maximumSpeed) {
        this.acceleration = acceleration;
        this.maximumSpeed = maximumSpeed;
        this.currentSpeed = 0;
        this.currentDirection = 0;
    }

    public Point2D getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(Point2D startPosition) {
        this.startPosition = startPosition;
    }

    public Point2D getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Point2D currentPosition) {
        this.currentPosition = currentPosition;
    }

    public Point2D getGoalPosition() {
        return goalPosition;
    }

    public void setGoalPosition(Point2D goalPosition) {
        this.goalPosition = goalPosition;
    }

    public double getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentSpeed(double currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }

    public double getMaximumSpeed() {
        return maximumSpeed;
    }

    public void setMaximumSpeed(double maximumSpeed) {
        this.maximumSpeed = maximumSpeed;
    }

    public int getCurrentDirection() {
        return currentDirection;
    }

    public void setCurrentDirection(int currentDirection) {
        this.currentDirection = currentDirection;
    }

    public Tracker getTracker() {
        return tracker;
    }

    public void setTracker(Tracker tracker) {
        this.tracker = tracker;
    }

    @Override
    public Point2D move(int totalSteps) {
        if (currentPosition == null)
            currentPosition = startPosition;
        int currentStep = 0;
        while (currentStep != totalSteps) {
            accelerate();
            step();
            currentStep++;
        }
        return currentPosition;
    }

    @Override
    public int turn(int degree) {
        currentDirection += degree;
        if (currentDirection >= 360)
            currentDirection -= 360;
        return currentDirection;
    }

    @Override
    public Point2D stop() {
        while (currentSpeed != 0) {
            decelerate();
            step();
        }
        return currentPosition;
    }

    public void accelerate() {
        if (acceleration == 0)
            currentSpeed = maximumSpeed;
        else {
            if (currentSpeed + acceleration <= maximumSpeed) {
                currentSpeed += acceleration;
            } else {
                currentSpeed = maximumSpeed;
            }
        }
    }

    public void decelerate() {
        if (acceleration == 0)
            currentSpeed = 0;
        else {
            if (currentSpeed - acceleration > 0)
                currentSpeed = currentSpeed - acceleration;
            else
                currentSpeed = 0;
        }
    }

    public void step() {
        double xDisplacement = Utilities.calculateX(currentDirection, currentSpeed);
        double yDisplacement = Utilities.calculateY(currentDirection, currentSpeed);
        currentPosition.setLocation(currentPosition.getX() + xDisplacement, currentPosition.getY() + yDisplacement);
        tracker.record(currentPosition);
    }
}
