package old;

import java.sql.*;

public class AvgRuleLength {
    //RuleBase interpolated = new RuleBase(numAntecedents);
    //for (String stringRule : originalRules) interpolated.add(new Rule(numAntecedents, stringRule));
    private static String[] originalRules = new String[]{
            "4.50,6.00,7.00,8.50 6.50,7.50,9.00,11.00 3.00,4.00,5.00,6.00 5.00,6.50,8.00,10.00",
            "14.50,16.00,18.00,19.00 13.00,14.50,16.50,18.00 16.00,17.00,18.50,21.00 13.00,15.50,17.00,18.50",
            "25.00,26.00,27.00,30.00 28.00,30.00,31.00,32.00 24.00,26.00,27.00,28.00 25.00,26.00,28.00,29.00",
            "5.00,7.00,8.00,9.00 7.00,8.00,9.50,11.50 3.50,5.00,6.50,7.00 6.00,7.50,9.00,10.50",
            "14.00,15.50,17.50,19.50 12.00,14.00,15.50,17.50 15.50,17.50,19.00,22.00 14.00,16.00,17.50,19.00",
            "4.50,6.50,8.00,9.50 6.50,8.00,9.00,11.00 3.00,4.50,5.50,6.50 5.50,7.50,9.50,11.00",
            "4.00,5.50,7.50,8.50 7.00,8.50,10.00,12.00 4.00,5.00,6.00,7.50 6.50,8.00,9.50,11.00",
            "15.00,16.00,17.00,18.00 13.50,15.00,17.00,18.50 16.50,17.50,19.50,21.50 13.00,15.00,17.00,19.00",
            "20.00,21.00,22.00,23.00 27.00,29.00,30.00,32.00 29.00,30.00,32.00,34.00 26.00,27.00,29.00,31.00",
            "5.50,7.50,8.50,9.50 6.50,8.00,9.50,11.50 2.00,3.50,4.50,6.00 5.00,7.00,9.00,10.50",
            "16.00,17.00,18.00,19.00 12.50,13.50,16.00,17.50 15.00,17.00,19.00,21.50 14.00,16.00,17.50,19.50",
            "25.00,26.00,28.00,32.00 22.00,23.00,24.00,25.00 27.00,28.00,30.00,32.00 20.00,21.00,22.00,24.00",
            "5.00,6.50,7.50,9.00 7.00,9.00,11.00,12.00 2.50,3.50,5.00,6.50 5.50,8.00,10.00,11.00",
    };
    int TotRow = 15;
    double Rep1[] = new double[TotRow];
    double Rep2[] = new double[TotRow];
    double Rep3[] = new double[TotRow];
    double Rep4[] = new double[TotRow];
    double AV1[] = new double[TotRow];
    double AV2[] = new double[TotRow];
    double AV3[] = new double[TotRow];
    double AV4[] = new double[TotRow];
    double c1[] = new double[TotRow];
    double c2[] = new double[TotRow];
    double c3[] = new double[TotRow];
    double c4[] = new double[TotRow];
    double c5[] = new double[TotRow];
    double c6[] = new double[TotRow];
    double c7[] = new double[TotRow];
    double c8[] = new double[TotRow];
    double c9[] = new double[TotRow];
    double c10[] = new double[TotRow];
    double c11[] = new double[TotRow];
    double c12[] = new double[TotRow];
    double c13[] = new double[TotRow];
    double c14[] = new double[TotRow];
    double c15[] = new double[TotRow];
    double c16[] = new double[TotRow];
    double Range1[] = new double[TotRow];
    double Range2[] = new double[TotRow];
    double Range3[] = new double[TotRow];
    double Range4[] = new double[TotRow];
    double av1 = 0;
    double av2 = 0;
    double av3 = 0;
    double av4 = 0;
    double totavg = 0;       /*------------------------------------------------------------------------*/



       /*------------------------------------------------------------------------*/

    public static void main(String ar[]) {
        AvgRuleLength arl = new AvgRuleLength();
        arl.readData();

    }

    public double readData() {
        int i = 0;
        int rc = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/rdb", "root", "root");
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from rulebase");

     /*Fetching database data & finding RV(Average) for four coordinates/columns (c1-c4,c5-c8,c9-c12,c13-c16)
      into array RV1[],RV2[],RV3[] & RV4[] */

            while (rs.next()) {

                rc = rc + 1;

                c1[i] = rs.getDouble(2);
                c2[i] = rs.getDouble(3);
                c3[i] = rs.getDouble(4);
                c4[i] = rs.getDouble(5);
                Range1[i] = maxElement(c1[i], c2[i], c3[i], c4[i]) - minElement(c1[i], c2[i], c3[i], c4[i]);
                c5[i] = rs.getDouble(6);
                c6[i] = rs.getDouble(7);
                c7[i] = rs.getDouble(8);
                c8[i] = rs.getDouble(9);
                Range2[i] = maxElement(c5[i], c6[i], c7[i], c8[i]) - minElement(c5[i], c6[i], c7[i], c8[i]);
                c9[i] = rs.getDouble(10);
                c10[i] = rs.getDouble(11);
                c11[i] = rs.getDouble(12);
                c12[i] = rs.getDouble(13);
                Range3[i] = maxElement(c9[i], c10[i], c11[i], c12[i]) - minElement(c9[i], c10[i], c11[i], c12[i]);
                c13[i] = rs.getDouble(14);
                c14[i] = rs.getDouble(15);
                c15[i] = rs.getDouble(16);
                c16[i] = rs.getDouble(17);
                Range4[i] = maxElement(c13[i], c14[i], c15[i], c16[i]) - minElement(c13[i], c14[i], c15[i], c16[i]);

                i = i + 1;

            }
            int c = 0;
            for (int j = 0; j < rc; j++) {
                av1 = av1 + Range1[j];
                av2 = av2 + Range2[j];
                av3 = av3 + Range3[j];
                av4 = av4 + Range4[j];
                c = c + 1;
                //System.out.println("range1 element["+j+"]="+Range1[j]);
                //System.out.println("range2 element["+j+"]="+Range2[j]);
                //System.out.println("range3 element["+j+"]="+Range3[j]);
                //System.out.println("range4 element["+j+"]="+Range4[j]);
            }
            av1 = av1 / c;
            av2 = av2 / c;
            av3 = av3 / c;
            av4 = av4 / c;
            totavg = (av1 + av2 + av3 + av4) / 4;
            System.out.println("total average=" + totavg);
            System.out.println();
        } catch (ClassNotFoundException e) {
            System.out.println("excp==" + e);
        } catch (SQLException e) {
            System.out.println("excp==" + e);
        }
        return totavg;
    }

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
}




