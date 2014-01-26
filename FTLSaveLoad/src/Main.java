import java.io.IOException;
import java.util.Scanner;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 26/09/12
 * Time: 09:55
 */
public class Main {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        GUI gui = new GUI();


        System.out.println("FTL Save Load Program Started ...");
        String next = scanner.next();
        while (!next.equals("x")) {
            if (next.equals("s")) {
                try {
                    Functions.save();
                } catch (IOException e) {
                    System.out.println("No Save File Found ...s");
                }
            } else if (next.equals("l")) {
                try {
                    Functions.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (next.startsWith("l")) {
                next = next.replaceAll("l", "");
                int number = Integer.parseInt(next);
                try {
                    Functions.load(number);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("?");
            }
            next = scanner.next();
        }
        System.out.println("Exit ...");
    }

}
