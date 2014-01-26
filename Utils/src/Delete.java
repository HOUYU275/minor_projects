import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: rrd09
 * Date: 21/08/13
 * Time: 09:28
 */
public class Delete {

    public static void main (String[] args) {

        double angle = 2 * Math.PI / 10;
        double arm = 250;

        for (double d = 0; d < 2 * Math.PI; d += angle) {
            System.out.print(arm - Math.sin(d) * arm - 50);
            System.out.println("\t" + (arm - Math.cos(d) * arm - 25));
        }
















        /*Scanner scanner = new Scanner(Strings.ir);

        String line;
        int count = 0;
        int record = 0;
        while (scanner.hasNext()) {
            line = scanner.nextLine();
            if (count % 2 == 1) System.out.println(line);
            count++;
            *//*String[] splits = line.split("\t");

            if (splits.length > 1) {
                try {
                    if (Integer.parseInt(splits[1]) != record) {

                        record = Integer.parseInt(splits[1]);
                    }
                }
                catch (java.lang.NumberFormatException e) {
                    //e.printStackTrace();
                }



            }*//*
        }*/



    }

}
