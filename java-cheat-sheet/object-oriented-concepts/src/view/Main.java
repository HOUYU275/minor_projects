package view;

import model.Car;
import model.Display;

import java.awt.geom.Point2D;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 15/02/12
 * Time: 11:54
 */
public class Main {
    public static void main(String args[]) {
        Car superCar = new Car(4, 20);
        Car normalCar = new Car(2, 10);
        Car crapCar = new Car(1, 5);

        normalCar.setStartPosition(new Point2D.Double(0, 0));
        normalCar.move(10);
        normalCar.turn(90);
        normalCar.move(10);
        normalCar.turn(90);
        normalCar.move(10);
        normalCar.turn(90);
        normalCar.move(10);
        normalCar.turn(90);
        normalCar.stop();

        Display.displayPath(normalCar);

    }
}
