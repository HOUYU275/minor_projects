package old;

import java.sql.*;

public class BlockRange {
    int TotRow = 15;
    int numInterval = 5;
    double c1[] = new double[TotRow];
    double c2[] = new double[TotRow];
    double c3[] = new double[TotRow];
    double c4[] = new double[TotRow];
    double c5[] = new double[TotRow];
    double c6[] = new double[TotRow];
    double c7[] = new double[TotRow];
    double c8[] = new double[TotRow];

    double min1[] = new double[numInterval];
    double max1[] = new double[numInterval];
    double min2[] = new double[numInterval];
    double max2[] = new double[numInterval];
    double range1, range2;        /*------------------------------------------------------------------------*/

    //Finding Ranges for different Blocks
    public void findBlockRanges() {
        int i = 0;
        double mx1 = 0, mx2 = 0, mn1 = 0, mn2 = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/rdb", "root", "root");
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select max(c1),max(c2),max(c3),max(c4),max(c5),max(c6),max(c7),max(c8) from rulebase");

            while (rs.next()) {

                double c1mx = rs.getDouble(1);
                double c2mx = rs.getDouble(2);
                double c3mx = rs.getDouble(3);
                double c4mx = rs.getDouble(4);

                double c5mx = rs.getDouble(5);
                double c6mx = rs.getDouble(6);
                double c7mx = rs.getDouble(7);
                double c8mx = rs.getDouble(8);

                mx1 = maxElement(c1mx, c2mx, c3mx, c4mx);
                mx2 = maxElement(c5mx, c6mx, c7mx, c8mx);
                //System.out.println("mx1=="+mx1);
            }

            Statement st1 = con.createStatement();
            ResultSet rs1 = st1.executeQuery("select min(c1),min(c2),min(c3),min(c4),min(c5),min(c6),min(c7),min(c8) from rulebase");

            while (rs1.next()) {

                double c1mn = rs1.getDouble(1);
                double c2mn = rs1.getDouble(2);
                double c3mn = rs1.getDouble(3);
                double c4mn = rs1.getDouble(4);

                double c5mn = rs1.getDouble(5);
                double c6mn = rs1.getDouble(6);
                double c7mn = rs1.getDouble(7);
                double c8mn = rs1.getDouble(8);

                mn1 = minElement(c1mn, c2mn, c3mn, c4mn);
                mn2 = minElement(c5mn, c6mn, c7mn, c8mn);
                //System.out.println("mn1=="+mn1);
            }
            range1 = mx1 - mn1;
            double interval1 = range1 / 5;

            range2 = mx2 - mn2;
            double interval2 = range2 / 5;

            for (i = 0; i < numInterval; i++) {
                min1[i] = mn1 + (i * interval1);
                max1[i] = min1[i] + interval1;

                min2[i] = mn2 + (i * interval2);
                max2[i] = min2[i] + interval2;

                System.out.println("min1" + i + "=" + min1[i]);
                System.out.println("max1" + i + "=" + max1[i]);
                System.out.println();
                System.out.println("min2" + i + "=" + min2[i]);
                System.out.println("max2" + i + "=" + max2[i]);
                System.out.println();
            }

        } catch (ClassNotFoundException e) {
            System.out.println("excp==" + e);
        } catch (SQLException e) {
            System.out.println("excp==" + e);
        }

    }



       /*------------------------------------------------------------------------*/

    //Max Method
    double maxElement(double d1, double d2, double d3, double d4) {
        double max = 0;
        if ((d1 > d2) && (d1 > d3) && (d1 > d4)) {
            max = d1;
        }
        if ((d2 > d1) && (d2 > d3) && (d2 > d4)) {
            max = d2;
        }
        if ((d3 > d2) && (d3 > d1) && (d3 > d4)) {
            max = d3;
        }
        if ((d4 > d2) && (d4 > d1) && (d4 > d3)) {
            max = d4;
        }
        return max;
    }

       /*------------------------------------------------------------------------*/

    //Min Method
    double minElement(double d1, double d2, double d3, double d4) {
        double min = 0;
        if ((d1 < d2) && (d1 < d3) && (d1 < d4)) {
            min = d1;
        }
        if ((d2 < d1) && (d2 < d3) && (d2 < d4)) {
            min = d2;
        }
        if ((d3 < d2) && (d3 < d1) && (d3 < d4)) {
            min = d3;
        }
        if ((d4 < d2) && (d4 < d1) && (d4 < d3)) {
            min = d4;
        }
        return min;
    }

       /*------------------------------------------------------------------------*/

    public static void main(String ar[]) {
        BlockRange arl = new BlockRange();
        arl.findBlockRanges();
    }
}




