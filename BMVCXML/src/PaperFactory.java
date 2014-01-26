import org.htmlparser.Node;
import org.htmlparser.tags.LinkTag;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 28/09/11
 * Time: 16:49
 * To change this template use File | Settings | File Templates.
 */
public class PaperFactory {

    private static String currentSession;
    private static Paper paper;
    private static ArrayList<Paper> papers = new ArrayList<Paper>();
    //private static ArrayList<Author> authors;

    public static void switchBoard(int option, Node node) {
        String argument = null;
        switch (option) {
            case 0: //session
                if (node.getFirstChild().getChildren() != null) {
                    argument = node.getFirstChild().getFirstChild().getText();
                } else {
                    argument = node.getFirstChild().getText();
                }
                setCurrentSession(cleanup(argument));
                break;
            case 1: //title
                argument = node.getFirstChild().getText();
                System.out.println(cleanup(argument));
                paper = new Paper(cleanup(argument));
                paper.setSession(currentSession);
                break;
            case 2: //pdf
                argument = ((LinkTag) node.getNextSibling().getNextSibling()).getLink();
                paper.setPdf(cleanup(argument));
                paper.setTailURL("papers/paper-" + stripNumbers(paper.getPdf()) + ".html");
                System.out.println(stripNumbers(paper.getPdf()));//papers/paper-288.html
                //paper.setSession(currentSession);
                break;
            case 3: //authors
                argument = node.getNextSibling().getText();
                paper.setAuthors(getAuthors(cleanup(argument)));
                papers.add(paper);
                paper = null;
                break;
            //System.out.println("args = " + cleanup(argument));
        }
    }

    public static void switchBoard(int option, String argument) {
        switch (option) {
            case 0: //session
                setCurrentSession(cleanup(argument));
                break;
            case 1: //title
                //System.out.println(cleanup(argument));
                paper = new Paper(cleanup(argument));
                paper.setSession(currentSession);
                break;
            case 2: //pdf
                paper.setPdf("papers/" + cleanup(argument) + ".pdf");
                paper.setTailURL("papers/paper-" + cleanup(argument) + ".html");
                break;
            case 3: //authors
                if (paper != null) {
                    paper.setAuthors(getAuthors(cleanup(argument)));
                    papers.add(paper);
                    paper = null;
                    break;
                }
                break;
            case 4: //tailURL
                paper.setTailURL(cleanup(argument));
            //System.out.println("args = " + cleanup(argument));
        }
    }

    private static String stripNumbers(String argument) {
        return argument.replaceAll("[^\\d]", "").substring(4);
    }

    private static String cleanup(String argument) {
        argument = argument.replaceAll("\n", " ");
        argument = argument.replaceAll("&nbsp;", " ");
        argument = argument.trim();
        return argument;
    }

    private static ArrayList<Author> getAuthors(String argument) {
        argument = argument.replaceAll(" and", ",");
        System.out.println(argument);
        ArrayList<Author> authors = new ArrayList<Author>();
        ArrayList<String> arrayList = new ArrayList();
        String currentString = "";
        for (int i = 0; i < argument.length(); i++) {
            if (argument.charAt(i) == ',') {
                if (!currentString.trim().isEmpty()) {
                    authors.add(new Author(currentString.trim()));
                    currentString = "";
                    i = i + 1;
                } else {
                    i = i + 1;
                }
            } else if (argument.charAt(i) == '(') {
                authors.add(new Author(currentString.trim()));
                currentString = "";
                for (int j = i + 1; j < argument.length(); j++) {
                    if (argument.charAt(j) == ')') {
                        for (Author author : authors) {
                            if (author.getInstitution() == null) author.setInstitution(currentString);
                        }
                        i = j + 1;
                        break;
                    }
                    currentString = currentString + argument.charAt(j);
                }
                currentString = "";
            } else {
                currentString = currentString + argument.charAt(i);
            }
            //System.out.print(argument.charAt(i));

        }
        //authors.add(new Author(currentString.trim()));
        return authors;
    }

    public static Paper getPaper() {
        return paper;
    }

    public static void setPaper(Paper paper) {
        PaperFactory.paper = paper;
    }

    /*public static ArrayList<Author> getAuthors() {
        return authors;
    }

    public static void setAuthors(ArrayList<Author> authors) {
        PaperFactory.authors = authors;
    }*/

    public static String getCurrentSession() {
        return currentSession;
    }

    public static void setCurrentSession(String currentSession) {
        //System.out.println("Current Session: " + currentSession);
        PaperFactory.currentSession = currentSession;
    }

    public static ArrayList<Paper> getPapers() {
        return papers;
    }

    public static void setPapers(ArrayList<Paper> papers) {
        PaperFactory.papers = papers;
    }
}
