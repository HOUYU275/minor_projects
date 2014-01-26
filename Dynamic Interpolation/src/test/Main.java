package test;

import control.RuleBaseGenerator;
import model.Block;
import control.DynamicInterpolation;
import model.RuleBase;
import model.SimpleFunction;
import model.Temp;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 15/01/13
 * Time: 17:40
 */
public class Main {

    private static int numAntecedents = 3;
    private static int numIntervals = 3;
    private static double spread = 1;
    private static int targetBlockSize = 15;

    public static Temp t = new Temp();

    public static void main(String[] args) throws CloneNotSupportedException {
        DynamicInterpolation interpolation = new DynamicInterpolation();
        RuleBaseGenerator ruleBaseGenerator = new RuleBaseGenerator(new SimpleFunction(), spread);

        RuleBase original = ruleBaseGenerator.generateRuleBase(100);
        RuleBase interpolated = new RuleBase(numAntecedents);

        int count = 0;
        int repeat = 0;
        int numBlocks = (int) Math.pow(numIntervals, numAntecedents);
        System.out.println("Iteration\tOriginal\tInterpolated\tCount\tTotal");
        //while (count != numBlocks) {
        while (repeat != 100) {
            if (interpolated.size() == targetBlockSize * numBlocks) {
                //while (interpolated.size() > targetBlockSize * blocks.length / 2) interpolated.remove((new Random()).nextInt(interpolated.size()));
                interpolated.clear();
            }
            interpolation.fill(original, interpolated, targetBlockSize * numBlocks);
            count = interpolation.dynamicInterpolation(original, interpolated, numIntervals, targetBlockSize);
            //System.out.println(repeat + "\t" + original.size() + "\t" + interpolated.size() + "\t" + count + "\t" + numBlocks);
            repeat++;
            System.out.println(t.pop());
        }
        System.out.println(repeat);
    }
}
