package originalharmonysearch.applications.beam;

import originalharmonysearch.core.Harmony;
import originalharmonysearch.core.HarmonyComparator;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 28-Oct-2010
 * Time: 12:00:24
 * To change this template use File | Settings | File Templates.
 */
public class BeamHarmonyComparator extends HarmonyComparator {

    private double P = 6000;
    private double L = 14;
    private double deltaMax = 0.25;
    private double E = 30000000;
    private double G = 12000000;
    private double tauMax = 13600;
    private double sigmaMax = 30600;

    public BeamHarmonyComparator() {
        super();
        this.setDimension(1);
    }

    @Override
    public Object translate(Harmony harmony) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int checkConstraint(Harmony harmony) throws Exception {
        double[] notes = harmony.getDoubleNotes();
        int numViolations = 0;
        double a = notes[0];
        double b = notes[1];
        double c = notes[2];
        double d = notes[3];
        if ((a < 0d) || (c < 0d)) numViolations++;
        if (!checkG1(a, b, c, d)) numViolations++;
        if (!checkG2(a, b, c, d)) numViolations++;
        if (!checkG3(a, b, c, d)) numViolations++;
        if (!checkG4(a, b, c, d)) numViolations++;
        if (!checkG5(a, b, c, d)) numViolations++;
        if (!checkG6(a, b, c, d)) numViolations++;
        if (!checkG7(a, b, c, d)) numViolations++;
        if (!checkG8(a, b, c, d)) numViolations++;
        if (!checkG9(a, b, c, d)) numViolations++;
        return numViolations;
    }

    @Override
    public void evaluate(Harmony harmony) throws Exception {
        double[] notes = harmony.getDoubleNotes();
        double a = notes[0];
        double b = notes[1];
        double c = notes[2];
        double d = notes[3];
        harmony.setMerit(0 - (1.10471 * a * a * b + 0.04811 * c * d * (14.0 + b)));
    }

    @Override
    public int compare(Object o1, Object o2) {
        Harmony fs1 = (Harmony) o1;
        Harmony fs2 = (Harmony) o2;
        int result = 0;
        //for (int i = 0; i < getHarmonyEvaluators().length; i++) {
        if (fs1.getMerit() > fs2.getMerit()) {
            result = result + 1;
        } else if (fs1.getMerit() < fs2.getMerit()) {
            result = result - 1;
        }
        //}
        //System.out.println("CC" + (checkConstraint(fs1) - checkConstraint(fs2)));

        try {
            int fs1cc = checkConstraint(fs1);
            int fs2cc = checkConstraint(fs2);

            result -= fs1cc - fs2cc;

            //System.out.println(fs1cc + " " + fs2cc + " " + result);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return result;
    }

    private boolean checkG1(double a, double b, double c, double d) {
        //System.out.println(tau(a,b,c,d)-tauMax);
        return (tau(a, b, c, d) - tauMax <= 0);
    }

    private boolean checkG2(double a, double b, double c, double d) {
        return (sigma(a, b, c, d) - sigmaMax <= 0);
    }

    private boolean checkG3(double a, double b, double c, double d) {
        //System.out.println(Math.PI * Math.pow(58.29015, 2) * 43.69268 + 4 * Math.PI * Math.pow(58.29015, 3) / 3 - 129P);
        return a - d <= 0;
    }

    private boolean checkG4(double a, double b, double c, double d) {
        return delta(a, b, c, d) - deltaMax <= 0;
    }

    private boolean checkG5(double a, double b, double c, double d) {
        return P - Pc(a, b, c, d) <= 0;
    }

    private boolean checkG6(double a, double b, double c, double d) {
        return (a <= 5) && (a >= 0.125);
    }

    private boolean checkG7(double a, double b, double c, double d) {
        return (b >= 0.1) && (b <= 10);
    }

    private boolean checkG8(double a, double b, double c, double d) {
        return (c >= 0.1) && (c <= 10);
    }

    private boolean checkG9(double a, double b, double c, double d) {
        return (d >= 0.1) && (d <= 5);
    }

    private double tau(double a, double b, double c, double d) {
        //return Math.sqrt(Math.pow(tau1(a,b,c,d),2)+2*tau1(a,b,c,d)*tau2(a,b,c,d)*b/(2*R(a,b,c,d))+Math.pow(tau2(a,b,c,d),2));
        return Math.sqrt(Math.pow(tau1(a, b, c, d), 2) + Math.pow(tau2(a, b, c, d), 2) + b * tau1(a, b, c, d) * tau2(a, b, c, d) / R(a, b, c, d));
    }

    private double tau1(double a, double b, double c, double d) {
        return P / (Math.sqrt(2) * a * b);
    }

    private double tau2(double a, double b, double c, double d) {
        return M(a, b, c, d) * R(a, b, c, d) / J(a, b, c, d);
    }

    private double M(double a, double b, double c, double d) {
        return P * (L + 0.5 * b);
    }

    private double R(double a, double b, double c, double d) {
        //return Math.sqrt(0.25*(Math.pow(b,2)/4)+(Math.pow((a+c)/2,2)));
        return Math.sqrt(0.25 * (b * b + (a + c) * (a + c)));
    }

    private double J(double a, double b, double c, double d) {
        //return 2*(Math.sqrt(2)*a*b*(Math.pow(b,2)/4+Math.pow((a+c)/2,2)));
        return 2 * (0.707 * a * b * (b * b / 12 + 0.25 * (a + c) * (a + c)));
    }

    private double sigma(double a, double b, double c, double d) {
        //return 6*P*L/(d*Math.pow(c,2));
        return 50400 / (c * c * d);
    }

    private double delta(double a, double b, double c, double d) {
        return 6 * P * Math.pow(L, 3) / (E * Math.pow(c, 2) * d);
    }

    private double Pc(double a, double b, double c, double d) {
        return 4.013 * E * Math.sqrt(Math.pow(c, 2) * Math.pow(d, 6) / 36) * (1 - c * Math.sqrt(E / (4 * G)) / (2 * L)) / (Math.pow(L, 2));
        //return 64746(1-0.028)
    }
}
