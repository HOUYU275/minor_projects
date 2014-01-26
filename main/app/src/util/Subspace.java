package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 02-Nov-2010
 * Time: 12:48:38
 * To change this template use File | Settings | File Templates.
 */
public class Subspace {

    public static String randomSubSpace(int numAttributes, int classIndex, Random m_Random) {

        int subSpaceSize = numAttributes / 3 + m_Random.nextInt(numAttributes * 1 / 3);

        ArrayList<Integer> arrayList = new ArrayList<Integer>();

        for (int i = 1; i < numAttributes; i++) arrayList.add(i);

        Collections.shuffle(arrayList, m_Random);

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < subSpaceSize; i++) sb.append(arrayList.get(i) + ",");

        sb.append(classIndex + 1);

        return sb.toString();
    }

    public static String randomSubSpace(int numAttributes, int classIndex, int subSpaceSize, Random m_Random) {

        ArrayList<Integer> arrayList = new ArrayList<Integer>();

        for (int i = 1; i < numAttributes; i++) arrayList.add(i);

        Collections.shuffle(arrayList, m_Random);

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < subSpaceSize; i++) sb.append(arrayList.get(i) + ",");

        sb.append(classIndex + 1);

        return sb.toString();
    }

}
