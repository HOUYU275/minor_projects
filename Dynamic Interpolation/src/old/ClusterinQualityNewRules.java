package old;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class ClusterinQualityNewRules {

    //Defining Global Variables

    FindBlocks fb = new FindBlocks();
    int rc = fb.countRow();

    int kmax = 3;
    int TotRow = rc;
    int clustcounter;

    int BestK[] = new int[kmax];

    double Rep1[] = new double[TotRow];
    double Rep2[] = new double[TotRow];

    double RV1[] = new double[TotRow];
    double RV2[] = new double[TotRow];

    double c1[] = new double[TotRow];
    double c2[] = new double[TotRow];
    double c3[] = new double[TotRow];
    double c4[] = new double[TotRow];
    double c5[] = new double[TotRow];
    double c6[] = new double[TotRow];
    double c7[] = new double[TotRow];
    double c8[] = new double[TotRow];

    int ClustNumber;

    double ClustgroupW[] = new double[TotRow];
    double ClustgroupX[] = new double[TotRow];
    int[] grouping = new int[TotRow];

    double ClustRep1[];
    double ClustRep2[];

    double ClustRep11[];
    double ClustRep21[];

    double DistValue[] = new double[TotRow];
    double InterClustDist[] = new double[TotRow];
    double AvgIntraClustDist[] = new double[TotRow];
    int ClustSize[];
    int TotCloseRule[];

    double Weight1[] = new double[TotRow];
    double Weight2[] = new double[TotRow];

    double NormalWeight1[] = new double[TotRow];
    double NormalWeight2[] = new double[TotRow];

    DecimalFormat dec = new DecimalFormat("0.00");

    ArrayList b1;

    AvgRuleLength obj1 = new AvgRuleLength();
    double AvgAttriLen = obj1.readData();

          /*------------------------------------------------------------------------*/
       /*Fetching A1 and A2 of corresponding Blocks & finding RV(Average) for four coordinates/columns (c1-c4,c5-c8,c9-c12,c13-c16)
            into array RV1[],RV2[] */

    public void readData() throws IOException {
        int rno = 0;
        boolean f = false;
        int h;
        int i = 0;

        int q = 0;

        ArrayList blocks[] = fb.readData();
        String rules = "";
        for (int p = 0; p < blocks.length; p++) {
            b1 = blocks[p];
            int minrule = rc / 10;

            if (b1.size() > minrule) {

                for (h = 0; h <= b1.size() - 1; h++) {

                    rno = ((Integer) b1.get(h)).intValue();                    /*
                    System.out.println("rno===="+rno);
            		if(h==b1.size()-1)
            		rules=rules+rno;
            		else
            		rules=rules+rno+",";
            		System.out.println("rules==="+rules);
            		*/
                    try {
                        Class.forName("com.mysql.jdbc.Driver");
                        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/rdb", "root", "root");
                        Statement st = con.createStatement();
                        ResultSet rs = st.executeQuery("select * from intrulebase where ruleno=" + rno);

                        while (rs.next()) {
                            c1[i] = rs.getDouble(2);
                            //System.out.println("column1=" +c1[i]);
                            c2[i] = rs.getDouble(3);
                            c3[i] = rs.getDouble(4);
                            c4[i] = rs.getDouble(5);
                            c5[i] = rs.getDouble(6);
                            c6[i] = rs.getDouble(7);
                            c7[i] = rs.getDouble(8);
                            c8[i] = rs.getDouble(9);

                            double a = (rs.getDouble(2) + rs.getDouble(3) + rs.getDouble(4) + rs.getDouble(5)) / 4.0;
                            double b = (rs.getDouble(6) + rs.getDouble(7) + rs.getDouble(8) + rs.getDouble(9)) / 4.0;

                            RV1[i] = a;
                            RV2[i] = b;

                            i = i + 1;

                        }

                    } catch (ClassNotFoundException e) {
                        System.out.println("excp==" + e);
                    } catch (SQLException e) {
                        System.out.println("excp==" + e);
                    }

                }
                int w = 0;
                for (clustcounter = 1; clustcounter <= kmax; clustcounter++) {
                    System.out.println("-----------for list no = " + p + "--------------");
                    System.out.println("*********** for cluster size k =" + clustcounter + "  **********");

                    inputValue(clustcounter);
                    objectsinCluster(); //Quality Measure-1
                    clusterCloseness(); //Quality Measure-2
                    intraClustDist();   //Quality Measure-3
                    objectsPosition();  //Quality Measure-4
                    f = clusterEvaluate(clustcounter);

                    if (f) {
                        BestK[w] = clustcounter;
                        //System.out.println("bestK=="+BestK[w]);
                        w++;
                    }
                }

                System.out.println("Cluster Size k =" + BestK[0] + "  is best k to generate rule(s)");
                for (int j = 0; j < BestK[0]; j++) {
                    System.out.println("----New Rule =" + j + "-----");
                    newRuleGenerate(BestK[j]);
                }
            }

        }
    }





            /*------------------------------------------------------------------------*/

    public void inputValue(int k) throws IOException {

        //Input the value of number of Clusters

        // System.out.println("Enter the number of clusters:");

        // Scanner input = new Scanner(System.in);
        ClustNumber = clustcounter;// input.nextInt();
        // System.out.println(ClustNumber);

        ClustRep1 = new double[ClustNumber];
        ClustRep2 = new double[ClustNumber];

        ClustRep11 = new double[ClustNumber];
        ClustRep21 = new double[ClustNumber];

        Arrays.fill(ClustRep11, 0);
        Arrays.fill(ClustRep21, 0);

        int row = 0;

        //Assign RV1 and RV2 to Parameters Rep1 and Rep2
        for (row = 0; row < rc; row++) {

            Rep1[row] = RV1[row];
            Rep2[row] = RV2[row];

            //System.out.print(" " + Rep1[row] + " , ");
            //System.out.print(" " + Rep2[row]  + " ,");

        }

        assignCentroids(Rep1, Rep2);
        groupingCluster(Rep1, Rep2, ClustRep1, ClustRep2);
        groupingCentroid();
        displayCentroid();
    }

        /*------------------------------------------------------------------------*/

    //Setting K (ClustNumber) Centroids

    void assignCentroids(double[] Rep1, double[] Rep2) {
        for (int i = 0; i < ClustNumber; i++) {

            ClustRep1[i] = Rep1[i];
            ClustRep2[i] = Rep2[i];

            //System.out.print(" " + ClustRep1[i] + " , ");
            //System.out.print(" " + ClustRep2[i]  + ",");

        }

    }

            /*------------------------------------------------------------------------*/

    //Grouping the Datasets to respective Clusters by comparing the Distance

    void groupingCluster(double[] Rep1, double[] Rep2, double[] ClustRep1, double[] ClustRep2) {
        double distance = Double.MAX_VALUE;
        //double DistValue[]  = new double [1000];
        int k = 0;
        int r = 1;
        for (int i = 0; i < rc; i++) {

            for (int j = 0; j < ClustNumber; j++) {
                DistValue[j] = Math.sqrt(Math.pow((Rep1[i] - ClustRep1[j]), 2) + Math.pow((Rep2[i] - ClustRep2[j]), 2));

                //System.out.println("Distance "+r+"="+ + DistValue[j]);
                //r=r+1;

                if (distance > DistValue[j]) {

                    distance = DistValue[j];
                    grouping[k] = j;

                }

            }

            ClustgroupW[k] = Rep1[i];
            ClustgroupX[k] = Rep2[i];

            k++;
            distance = Double.MAX_VALUE;
        }

    }
        /*---------------------------------------------------------------------------------*/

    //Finding the New Centroids for All Clusters

    void groupingCentroid() {

        double CentroidRep1[] = new double[rc];
        double CentroidRep2[] = new double[rc];

        Arrays.fill(CentroidRep1, 0);
        Arrays.fill(CentroidRep2, 0);

        for (int j = 0; j < ClustNumber; j++) {
            int counter = 0;

            for (int i = 0; i < b1.size(); i++) {

                if (grouping[i] == j)

                {

                    CentroidRep1[j] = CentroidRep1[j] + ClustgroupW[i];
                    CentroidRep2[j] = CentroidRep2[j] + ClustgroupX[i];

                    counter++;

                }
                //System.out.println("Centroid:" + CentroidRep1[j] + " , " + CentroidRep2[j] + CentroidRep3[j] + " , " + CentroidRep4[j]);

            }
            //System.out.println("Counter:" + counter);

            CentroidRep1[j] = CentroidRep1[j] / counter;
            CentroidRep2[j] = CentroidRep2[j] / counter;

            ClustRep1[j] = CentroidRep1[j];
            ClustRep2[j] = CentroidRep2[j];

        }

        while (!(ClustRep11[0] == ClustRep1[0] && ClustRep21[0] == ClustRep2[0])) {

            for (int p = 0; p < ClustNumber; p++) {
                ClustRep11[p] = ClustRep1[p];
                ClustRep21[p] = ClustRep2[p];

            }
            //System.out.println("New Centroid:" + ClustRep1[0] + " , " + ClustRep4[0]);
            groupingCluster(Rep1, Rep2, ClustRep1, ClustRep2);
            //System.out.println("New Centroid:" + ClustRep1[0] + " , " + ClustRep4[0]);
            groupingCentroid();

        }

    }

        /*---------------------------------------------------------------------------------*/

    //Printing the Final Centroids and their Respective Coordinates for All Clusters

    void displayCentroid() throws IOException {

        BufferedWriter wr = new BufferedWriter(new FileWriter("resultmulti.txt"));

        for (int j = 0; j < ClustNumber; j++) {
            wr.write("------------------------");
            wr.newLine();
            wr.write("Centroid for group-- " + j);
            wr.newLine();
            wr.write("Final Centroids:" + ClustRep1[j] + " , " + ClustRep2[j]);
            wr.newLine();
            wr.write("------------------------");
            wr.newLine();

            System.out.println("------------------------");
            System.out.println("Centroid for group-- " + j);
            System.out.println("Final Centroids:" + ClustRep1[j] + " , " + ClustRep2[j]);
            System.out.println("------------------------");

            for (int i = 0; i < b1.size(); i++) {
                //System.out.println("Distance "+"="+  DistValue[j]);
                if (grouping[i] == j) {
                    wr.write("Centroid Coordinates:" + ClustgroupW[i] + " , " + ClustgroupX[i]);
                    wr.newLine();
                    System.out.println("Centroid Coordinates:" + ClustgroupW[i] + " , " + ClustgroupX[i]);

                }

            }

            wr.newLine();
            System.out.println("\n");
        }

        wr.close();
    }

         /*---------------------------------------------------------------------------------*/

    //1st Quality Measure-Number of Objects in a Cluster
    void objectsinCluster() {

        System.out.println("----1st Quality Measure-Number of Rules/Objects in a Cluster----");

        ClustSize = new int[ClustNumber];

        for (int j = 0; j < ClustNumber; j++) {
            int counter = 0;
            //System.out.println("b1 size==="+b1.size());
            for (int i = 0; i < b1.size(); i++) {

                if (grouping[i] == j)

                {
                    counter++;
                }

            }
            ClustSize[j] = counter;
            System.out.println("Total Rules/Objects in a Cluster--" + j + "==" + counter);

        }

        System.out.println("\n");
    }

           /*---------------------------------------------------------------------------------*/

    //2nd Quality Measure-Inter Cluster Distance (Clusters Closeness/ Isolation)
    void clusterCloseness() {

        System.out.println("----2nd Quality Measure-Inter Cluster Distance (Clusters Closeness/ Isolation)----");

        Arrays.fill(InterClustDist, 0);
        if (ClustNumber == 1) {
            InterClustDist[0] = fb.numIntervals * AvgAttriLen;
            System.out.println("only one cluster is there so distance is Max");
        } else {
            for (int j = 0; j < ClustNumber; j++) {

                for (int i = j + 1; i < ClustNumber; i++) {

                    InterClustDist[j] = Math.sqrt(Math.pow((ClustRep1[j] - ClustRep1[i]), 2) + Math.pow((ClustRep2[j] - ClustRep2[i]), 2));

                    System.out.println("Distance Between Two Centroids--" + j + " and " + i + "--" + InterClustDist[j]);
                }
            }
        }
        System.out.println("\n");

    }

    /*---------------------------------------------------------------------------------*/
    //3rd Quality Measure-Average Intra Cluster Distance
    void intraClustDist() {

        System.out.println("----3rd Quality Measure-Average Intra Cluster Distance----");

        Arrays.fill(AvgIntraClustDist, 0);

        for (int j = 0; j < ClustNumber; j++) {

            int counter = 0;
            for (int i = 0; i < b1.size(); i++) {
                DistValue[i] = Math.sqrt(Math.pow((Rep1[i] - ClustRep1[j]), 2) + Math.pow((Rep2[i] - ClustRep2[j]), 2));
                if (grouping[i] == j)

                {
                    AvgIntraClustDist[j] = AvgIntraClustDist[j] + DistValue[i];
                    //System.out.println("distvalue==="+ DistValue[i]);
                    counter++;
                }

            }

            AvgIntraClustDist[j] = AvgIntraClustDist[j] / counter;
            System.out.println("Average Intra Cluster Distance for Cluster--" + j + "--" + AvgIntraClustDist[j]);

        }

        System.out.println("\n");
    }

         /*---------------------------------------------------------------------------------*/

    //4th Quality Measure-Objects Positioning in Clusters (Compactness)
    void objectsPosition() {

        TotCloseRule = new int[ClustNumber];
        System.out.println("----4th Quality Measure-Objects Positioning in Clusters (Compactness)----");

        for (int j = 0; j < ClustNumber; j++) {
            int hcounter = 0;
            int lcounter = 0;

            for (int i = 0; i < b1.size(); i++) {
                DistValue[i] = Math.sqrt(Math.pow((Rep1[i] - ClustRep1[j]), 2) + Math.pow((Rep2[i] - ClustRep2[j]), 2));
                if (grouping[i] == j) {
                    if (DistValue[i] > AvgIntraClustDist[j])
                        hcounter++;
                    else
                        lcounter++;
                }
            }
            System.out.println("Total Objects with Greater Average Intra Cluster Distance for Cluster--" + j + "--" + hcounter);
            System.out.println("Total Objects with Smaller/Equal Average Intra Cluster Distance for Cluster--" + j + "--" + lcounter);
            TotCloseRule[j] = lcounter;
        }
        System.out.println("\n");
    }


         /*---------------------------------------------------------------------------------*/

    //Evaluating Clusters on the basis all Quality Measures
    boolean clusterEvaluate(int clustcounter) {

        ClustNumber = clustcounter;

        int q = 0;
        boolean flag = false;
        int j = 0;

        //Comparative Selection of k by Measuring Quality of Clusters
        for (j = 0; j < ClustNumber; j++) {
            if (ClustSize[j] > rc / 10) {
                if (InterClustDist[j] > AvgAttriLen) {
                    if (AvgIntraClustDist[j] <= AvgAttriLen) {
                        if (TotCloseRule[j] > ClustSize[j] / 4.0) { //System.out.println("CloseRule="+TotCloseRule [j]+","+"ClustSize="+ClustSize [j]/4.0);
                            q = q + 1;

                        }
                    }
                }
            }

        }
        if (q == ClustNumber)
            flag = true;
        return flag;

    }




         /*---------------------------------------------------------------------------------*/

    //Generating New Rule from the selected Clusters
    void newRuleGenerate(int groupNo) {
        double CentDist1[] = new double[TotRow];
        double CentDist2[] = new double[TotRow];

        double a1[] = new double[TotRow];
        double a2[] = new double[TotRow];
        double a3[] = new double[TotRow];
        double a4[] = new double[TotRow];
        double a5[] = new double[TotRow];
        double a6[] = new double[TotRow];
        double a7[] = new double[TotRow];
        double a8[] = new double[TotRow];

        double SumC1 = 0;
        double SumC2 = 0;
        double SumC3 = 0;
        double SumC4 = 0;
        double SumC5 = 0;
        double SumC6 = 0;
        double SumC7 = 0;
        double SumC8 = 0;

        Arrays.fill(Weight1, 0);
        Arrays.fill(Weight2, 0);

        Arrays.fill(NormalWeight1, 0);
        Arrays.fill(NormalWeight2, 0);

        double AllWeight1 = 0;
        double AllWeight2 = 0;

        int counter = 0;

        //Finding All Weights
        for (int i = 0; i < rc; i++) {
            //System.out.println("RV1 and ClustRep1==="+ RV1[i]+","+ClustRep1[groupNo]);
            CentDist1[i] = Math.abs(RV1[i] - ClustRep1[groupNo]);
            //System.out.println("CentDist==="+ CentDist1[i]);
            CentDist2[i] = Math.abs(RV2[i] - ClustRep2[groupNo]);

            if (grouping[i] == groupNo)

            {
                //System.out.println("CentDist==="+ CentDist1[i]);
                if (!((CentDist1[i] >= 0) && (CentDist1[i] <= 0.1))) {
                    Weight1[counter] = 1.0 / CentDist1[i];
                } else {
                    Weight1[counter] = 10;
                }

                if (!((CentDist2[i] >= 0) && (CentDist2[i] <= 0.1))) {
                    Weight2[counter] = 1.0 / CentDist2[i];
                } else {
                    Weight2[counter] = 10;
                }

                //Weight2[counter] = 1.0/ CentDist2[i];
                //Weight3[counter] = 1.0/ CentDist3[i];
                //Weight4[counter] = 1.0/ CentDist4[i];

                //System.out.println("Weight1==="+ Weight1[counter]);
                //System.out.println("Weight2==="+ Weight2[counter]);
                //System.out.println("Weight3==="+ Weight3[counter]);
                //System.out.println("Weight4==="+ Weight4[counter]);
                counter++;
            }
        }
        //Adding All Weights
        for (int k = 0; k < counter; k++) {
            AllWeight1 = AllWeight1 + Weight1[k];
            AllWeight2 = AllWeight2 + Weight2[k];

        }
        //System.out.println("All Weight1===" + AllWeight1);
        //System.out.println("All Weight2===" + AllWeight2);
        //System.out.println("All Weight3===" + AllWeight3);
        //System.out.println("All Weight4===" + AllWeight4);

        //Finding Normal Weights
        for (int k = 0; k < counter; k++) {
            NormalWeight1[k] = Weight1[k] / AllWeight1;
            NormalWeight2[k] = Weight2[k] / AllWeight2;

            //System.out.println("Normal Weight1==="+ NormalWeight1[k]);
            //System.out.println("Normal Weight2==="+ NormalWeight2[k]);
            //System.out.println("Normal Weight3==="+ NormalWeight3[k]);
            //System.out.println("Normal Weight4==="+ NormalWeight4[k]);
        }

        //Finding Relative Coordinates (A'') by Multiplying their Weights
        int k = 0;
        for (int i = 0; i < b1.size(); i++) {

            if (grouping[i] == groupNo) {
                //System.out.print("cc1= " + c1[i] + " , ");
                a1[k] = c1[i] * NormalWeight1[k];
                //System.out.print("a1= " + a1[k] + " , "+ "c1= " + c1[i] + " , " + "NormalWeight1= " + NormalWeight1[k] + "\n");
                a2[k] = c2[i] * NormalWeight1[k];
                a3[k] = c3[i] * NormalWeight1[k];
                a4[k] = c4[i] * NormalWeight1[k];
                a5[k] = c5[i] * NormalWeight2[k];
                a6[k] = c6[i] * NormalWeight2[k];
                a7[k] = c7[i] * NormalWeight2[k];
                a8[k] = c8[i] * NormalWeight2[k];

                //System.out.print("a1= " + a1[k] + " , ");
                //System.out.print(" " + a2[k]  + " ,");
                //System.out.print(" " + a3[k] + " , ");
                //System.out.print(" " + a4[k]  + "\n");
                //System.out.print(" " + a5[k] + " , ");
                //System.out.print(" " + a6[k]  + " ,");
                //System.out.print(" " + a7[k] + " , ");
                //System.out.print(" " + a8[k]  + "\n");

                k = k + 1;
            }
        }

        for (int l = 0; l < counter; l++) {
            SumC1 = SumC1 + a1[l];
            //System.out.print("C1= " + SumC1 + " , ");
            SumC2 = SumC2 + a2[l];
            SumC3 = SumC3 + a3[l];
            SumC4 = SumC4 + a4[l];
            SumC5 = SumC5 + a5[l];
            SumC6 = SumC6 + a6[l];
            SumC7 = SumC7 + a7[l];
            SumC8 = SumC8 + a8[l];

            //System.out.print("a1= " + a1[k] + " , ");
        }
        System.out.print("C1= " + SumC1 + " , ");
        System.out.print("C2= " + SumC2 + " ,");
        System.out.print("C3= " + SumC3 + " , ");
        System.out.print("C4= " + SumC4 + "\n");
        System.out.print("C5= " + SumC5 + " , ");
        System.out.print("C6= " + SumC6 + " ,");
        System.out.print("C7= " + SumC7 + " , ");
        System.out.print("C8= " + SumC8 + "\n");

        System.out.println();
    }

         /*---------------------------------------------------------------------------------*/

    public static void main(String[] args) throws IOException {

        ClusterinQualityNewRules f1 = new ClusterinQualityNewRules();
        f1.readData();

    }

}
