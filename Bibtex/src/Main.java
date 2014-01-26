import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 15/05/12
 * Time: 11:28
 */
public class Main {
    /*
    \bibitem{MICROARRAYFS}
    E.P. Xing, M.I. Jordan, and R.M. Karp,
    Feature Selection for High-Dimensional Genomic Microarray Data,
    In Proceedings of the Eighteenth International Conference on Machine Learning,
    pp. 601--608,
    2001.
     */
    private static String input = "Feature Selection for Clustering - a Filter Solution";
    public static void main(String[] args) throws FileNotFoundException {
        //System.out.println(generateLabel("Deterministic Parameter Control in Harmony Search"));
     System.out.println(sortBib("bib.tex"));
    }

    public static String generateLabel(String input) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < input.length(); i++) {
            if (Character.isLetter(input.charAt(i))) {
                stringBuffer.append(input.substring(i, i + 1).toLowerCase());
            }
            else {
                stringBuffer.append("_");
            }
        }
        String label = stringBuffer.toString();
        while (label.contains("__")) label = label.replace("__", "_");
        return label;
    }

    public static String sortBib(String fileName) throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileInputStream(fileName));
        StringBuffer stringBuffer = new StringBuffer();
        ArrayList<BibliographyItem> items = new ArrayList<BibliographyItem>();
        String line;
        BibliographyItem item = null;
        while (scanner.hasNext()) {
            line = scanner.nextLine();
            if (line.contains("\\bibitem")) {
                if (item != null) items.add(item);
                String label = line.replace("\\bibitem{", "");
                label = label.substring(0, label.length() - 1);
                item = new BibliographyItem(label);
            }
            else {
                item.append(line);
            }
        }
        if (item != null) items.add(item);

        for (BibliographyItem it : items) it.setAuthor(it.findAuthor());
        Collections.sort(items);
        for (BibliographyItem it : items) System.out.println(it.toString());
        return stringBuffer.toString();
    }
}
