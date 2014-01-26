package test;

import weka.fuzzy.measure.WeakGamma;
import weka.fuzzy.tnorm.TNormLukasiewicz;

import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 02/01/13
 * Time: 14:20
 */
public class FuzzyRoughTest{

    public static void main(String[] args) {
        WeakGamma gamma = new WeakGamma();
        gamma.m_composition = new TNormLukasiewicz();
        Random random = new Random();
        double[] randoms = new double[20];
        for (int i = 0; i < randoms.length; i++) {
            randoms[i] = random.nextDouble();
            System.out.print(randoms[i] + " ");
        }
        System.out.println();
        //randoms[random.nextInt(randoms.length)] = 0;
        double rel = randoms[0];
        System.out.println(rel);
        for (int i = 1; i < randoms.length; i++) {
            rel = gamma.m_composition.calculate(randoms[i], rel);
            System.out.println(rel);
        }

    }

}
