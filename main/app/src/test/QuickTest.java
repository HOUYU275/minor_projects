package test;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created with IntelliJ IDEA.
 * User: rrd09
 * Date: 23/05/13
 * Time: 12:28
 * To change this template use File | Settings | File Templates.
 */
public class QuickTest {

    public static ArrayList<int[]> generateRestrictedDomain(int numMusicians, int numFeatures, double restrictedRatio) {
        ArrayList<int[]> noteDomains = new ArrayList<>();
        int targetSize = (int) (numFeatures * restrictedRatio);
        ArrayList<Integer> seeds = new ArrayList<>();
        for (int i = 0; i < numFeatures; i++) seeds.add(i);
        for (int i = 0; i < numMusicians; i++) {
            Collections.shuffle(seeds);
            int[] noteDomain = new int[targetSize];
            for (int j = 0; j < targetSize; j++) noteDomain[j] = seeds.get(j);
            noteDomains.add(noteDomain);
        }
        return noteDomains;
    }

    public static void main(String[] args) {

        ArrayList<int[]> noteDomains = generateRestrictedDomain(10, 15, 0.75);
        for (int[] noteDomain : noteDomains) {
            for (int i : noteDomain) System.out.print(i + " ");
            System.out.println();
        }

        /*public void CreateGd(int musicianNum) {

            Random rand = new Random(System.currentTimeMillis() * 2000);
            BitSet bs1 = new BitSet();
            BitSet bs2 = new BitSet();

            for (int note : FullFeatures) {
                bs2.set(note, true);
            }
            while (true) {
                for (int i = 0; i < musicianNum; i++) {

                    int cur;
                    int[] full=Arrays.copyOf(FullFeatures,FullFeatures.length);
                    int[] gd = new int[(int) (RR * featureNum)];
                    for (int j = 0; j < (int) (RR * featureNum); j++) {
                        cur = rand.nextInt(full.length);
                        if (full[cur] != HarmonyMemory.Empty) {
                            gd[j] = full[cur];
                            bs1.set(gd[j], true);
                            full[cur] = HarmonyMemory.Empty;
                        }
                    }
                    musicians[i].setGlobalDomain(gd);
                }
                if (bs1.equals(bs2)) {
                    // bs1.clear();
                    // bs2.clear();
                    break;
                }
            }
        }*/


        for (float a = 0; a <= 10f; a += 2f) {
            for (float b = 0; b <= 10f; b += 2f) {
                for (float c = 0; c <= 10f; c += 2f) {
                    System.out.println("a =\t" + a + "\tb =\t" + b + "\tc =\t" + c + "\tz =\t" + popularity(a, b, c));
                }
            }
        }

    }

    public static float explosion(float a, float b, float c) {
        float temp = 0.9f * a / (1f + 0.6f * b / 10f);
        temp = 0.4f * c / (1f - temp / 10f);
        return temp;
    }

    public static float popularity(float a, float b, float c) {
        float temp = a / (0.54f + b / 43f);
        temp = c / (0.88f + temp / 68f);
        return temp;
    }


}
