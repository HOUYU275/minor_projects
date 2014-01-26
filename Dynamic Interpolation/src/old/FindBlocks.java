package old;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class FindBlocks {

    //Defining the Global Variables

    int rc;
    int ClustNumber;
    int numIntervals = 5;
    int numBlocks = numIntervals * numIntervals;

    ArrayList RV1 = new ArrayList();
    ArrayList RV2 = new ArrayList();

    ArrayList c1 = new ArrayList();
    ArrayList c2 = new ArrayList();
    ArrayList c3 = new ArrayList();
    ArrayList c4 = new ArrayList();
    ArrayList c5 = new ArrayList();
    ArrayList c6 = new ArrayList();
    ArrayList c7 = new ArrayList();
    ArrayList c8 = new ArrayList();

    double min1[] = new double[numIntervals];
    double max1[] = new double[numIntervals];
    double min2[] = new double[numIntervals];
    double max2[] = new double[numIntervals];
    double range1, range2;

    	   /*------------------------------------------------------------------------*/
    //Counting total number of rows in a Table

    public int countRow() //throws IOException
    {
        int rowc = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/rdb", "root", "root");
            Statement st = con.createStatement();
            ResultSet rsrow = st.executeQuery("select count(*) from intrulebase");

            rsrow.next();
            rc = rsrow.getInt(1);
            rowc = rc;
            System.out.println("Total Interpolated Rules ==" + rc);
        } catch (ClassNotFoundException e) {
            System.out.println("excp==" + e);
        } catch (SQLException e) {
            System.out.println("excp==" + e);
        }

        return rowc;
    }




     	 /*------------------------------------------------------------------------*/

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
            double interval1 = range1 / numIntervals;

            range2 = mx2 - mn2;
            double interval2 = range2 / numIntervals;

            for (i = 0; i < numIntervals; i++) {
                min1[i] = mn1 + (i * interval1);
                max1[i] = min1[i] + interval1;

                min2[i] = mn2 + (i * interval2);
                max2[i] = min2[i] + interval2;

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

       /*Fetching database data & finding RV1 and RV2 for two Attributes A1 & A2 -- coordinates/columns (c1-c4,c5-c8)into array RV1[],RV2[]*/

    public ArrayList[] readData() {
        int rno[] = new int[rc];
        int i = 0;
        ArrayList[] blocks = new ArrayList[numBlocks];
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/rdb", "root", "root");
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from intrulebase");

            while (rs.next()) {
                rno[i] = rs.getInt(1);

                c1.add(rs.getDouble(2));
                c2.add(rs.getDouble(3));
                c3.add(rs.getDouble(4));
                c4.add(rs.getDouble(5));
                c5.add(rs.getDouble(6));
                c6.add(rs.getDouble(7));
                c7.add(rs.getDouble(8));
                c8.add(rs.getDouble(9));

                double a = (rs.getDouble(2) + rs.getDouble(3) + rs.getDouble(4) + rs.getDouble(5)) / 4.0;
                double b = (rs.getDouble(6) + rs.getDouble(7) + rs.getDouble(8) + rs.getDouble(9)) / 4.0;

                RV1.add(a);
                RV2.add(b);
                i = i + 1;

            }

            //Calling Method for defining Blocks
            findBlockRanges();

/*Assigning Rules to corresponding Blocks*/

//blocks holds the indices of rules for each block
//the count of rules is retrieved by calling blocks[i].size();

            for (int r = 0; r < numBlocks; r++) blocks[r] = new ArrayList<Integer>();

            for (int k = 0; k < numIntervals; k++) {
                for (int j = 0; j < numIntervals; j++) {
                    for (int ruleno = 0; ruleno < rc; ruleno++) {
                        if ((((Double) RV1.get(ruleno)).doubleValue() >= min1[k]) && (((Double) RV1.get(ruleno)).doubleValue() < max1[k])) {
                            if ((((Double) RV2.get(ruleno)).doubleValue() >= min2[j]) && (((Double) RV2.get(ruleno)).doubleValue() < max2[j])) {
                                blocks[k * numIntervals + j].add(ruleno);
                            }
                        }
                    }
                }
            }

            for (i = 0; i < numBlocks; i++) {
                System.out.println("Block (" + (i / numIntervals) + "," + (i %
                        numIntervals) + ") has " + blocks[i].size() + " rules.");
                ArrayList arr = blocks[i];
                System.out.println("list " + i + " ==" + arr);
            }

        } catch (ClassNotFoundException e) {
            System.out.println("excp==" + e);
        } catch (SQLException e) {
            System.out.println("excp==" + e);
        }
        return blocks;
    }





         /*---------------------------------------------------------------------------------*/

    public static void main(String[] args) throws IOException {

        FindBlocks f1 = new FindBlocks();
        f1.countRow();
        f1.readData();

    }
}
    /*---------------------------------------------------------------------------------*/


