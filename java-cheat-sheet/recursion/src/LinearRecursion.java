/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 21/02/12
 * Time: 12:08
 */
public class LinearRecursion {

    public double linearSum(int[] input, int i) {
        if (i == 1) return input[0];
        return linearSum(input, i-1) + input[i-1];
    }

    public int[] reverseArray(int[] input, int i) {
        if (i < input.length / 2) return input;
        int temp = input[i - 1];
        input[i - 1] = input[input.length - i];
        input[input.length - i] = temp;
        return reverseArray(input, i-1);
    }
    
    
}
