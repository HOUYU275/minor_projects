/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 15/05/12
 * Time: 12:24
 */
public class BibliographyItem implements Comparable {
    private String label;
    private String blob;
    private String author;

    public BibliographyItem(String label) {
        this.label = label;
    }

    public BibliographyItem(String label, String blob) {
        this.label = label;
        this.blob = blob;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getBlob() {
        return blob;
    }

    public void setBlob(String blob) {
        this.blob = blob;
    }

    public void append(String line) {
        if (blob == null) blob = line;
        else blob += "\n" + line;
    }

    public String findAuthor() {
        System.out.println(label);
        int begin = blob.indexOf(" ");
        int endAnd = blob.indexOf(" and", begin + 1);
        int endComma = blob.indexOf(",", begin + 1);
        String author = blob.substring(begin + 1, endComma > endAnd ? endComma : endAnd);
        return author;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public int compareTo(Object o) {
        BibliographyItem item = (BibliographyItem) o;
        Character a = this.author.charAt(0);
        Character b = item.getAuthor().charAt(0);
        int aInt = (int) a;
        int bInt = (int) b;
        if (aInt == bInt) {
            a = this.author.charAt(1);
            b = item.getAuthor().charAt(1);
            aInt = (int) a;
            bInt = (int) b;
        }
        if (aInt == bInt) {
            a = this.author.charAt(2);
            b = item.getAuthor().charAt(2);
            aInt = (int) a;
            bInt = (int) b;
        }
        if (aInt == bInt) {
            a = this.author.charAt(3);
            b = item.getAuthor().charAt(3);
            aInt = (int) a;
            bInt = (int) b;
        }
        return aInt - bInt;
    }

    @Override
    public String toString() {
        return "\\bibitem{" + label + "}\n" + blob;
    }
}
