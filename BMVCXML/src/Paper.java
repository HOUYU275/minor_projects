import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 28/09/11
 * Time: 16:37
 * To change this template use File | Settings | File Templates.
 */
public class Paper {
    private String title;
    private String pdf;
    private String tailURL;
    private String session;
    private ArrayList<Author> authors;

    public Paper() {
    }

    public Paper(String title) {
        this.title = title;
    }

    public Paper(String title, String pdf, String tailURL, String session, ArrayList<Author> authors) {
        this.title = title;
        this.pdf = pdf;
        this.tailURL = tailURL;
        this.session = session;
        this.authors = authors;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public String getTailURL() {
        return tailURL;
    }

    public void setTailURL(String tailURL) {
        this.tailURL = tailURL;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public ArrayList<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(ArrayList<Author> authors) {
        this.authors = authors;
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.title + "\n");
        stringBuffer.append(this.pdf + "\n");
        stringBuffer.append(this.tailURL + "\n");
        stringBuffer.append(this.session + "\n");
        for (Author author : this.authors) stringBuffer.append(author.getName() + " " + author.getInstitution() + "\n");
        return stringBuffer.toString();
    }
}
