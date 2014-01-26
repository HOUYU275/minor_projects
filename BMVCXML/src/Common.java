import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.SimpleNodeIterator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Ren
 * Date: 28/09/11
 * Time: 22:53
 * To change this template use File | Settings | File Templates.
 */
public class Common {

    public static void fetchPage(String urlString) throws IOException {
        URL url = new URL(urlString);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

        String inputLine;
        int left, right;
        while ((inputLine = in.readLine()) != null) {
            System.out.println(inputLine);
            if (inputLine.contains("toc_session")) {
                left = inputLine.indexOf(">") + 1;
                right = inputLine.indexOf("<", left);
                System.out.println(inputLine.substring(left, right));
                PaperFactory.switchBoard(0, inputLine.substring(left, right));
            } else if (inputLine.contains("paper_title")) {
                left = inputLine.indexOf("html\">") + 6;
                right = inputLine.indexOf("</a></h3>", left);
                PaperFactory.switchBoard(1, inputLine.substring(left, right));
                left = inputLine.indexOf("href=\"") + 6;
                right = inputLine.indexOf("\">", left);
                //fetchPDFLink(inputLine.substring(left, right));
                String tailURL = inputLine.substring(left, right);
                PaperFactory.switchBoard(2, fetchPDFLink(tailURL));
                PaperFactory.switchBoard(4, tailURL);
            } else if (inputLine.startsWith("<span class=\"paper_author\">")) {
                String newLine;
                StringBuffer stringBuffer = new StringBuffer();
                while ((newLine = in.readLine()) != null) {
                    if (newLine.equals("</div>")) break;
                    left = newLine.indexOf("<a href=\"");
                    if (left < 0) left = newLine.indexOf(".</span><br />");
                    stringBuffer.append(newLine.substring(0, left));
                }
                String newAuthors = stringBuffer.toString();
                newAuthors = newAuthors.replaceAll("</a>", "");
                PaperFactory.switchBoard(3, newAuthors);
            }
        }
        in.close();
    }

    public static String fetchPDFLink(String currentURL) throws IOException {
        URL url = null;
        url = new URL("http://www.bmva.org/bmvc/2003/" + currentURL);

        BufferedReader in;
        try {
             in = new BufferedReader(new InputStreamReader(url.openStream()));
        }
        catch (IOException e) {
            System.out.println(url);
            return "";
        }

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            if (inputLine.contains(".pdf")) {
                int left = inputLine.indexOf("href=\"") + 6;
                int right = inputLine.indexOf(".pdf");
                System.out.println(inputLine.substring(left, right));
                return inputLine.substring(left, right);
            }
        }
        return null;
    }

    public static void fetchURL3(String urlString) throws IOException {
        URL url = new URL(urlString);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            //System.out.println(inputLine);
            if (inputLine.contains("name=")) {
                int startName = inputLine.indexOf("name=");
                int leftIndex = inputLine.indexOf(">", startName) + 1;
                int rightIndex = inputLine.indexOf("<", startName);
                PaperFactory.switchBoard(0, inputLine.substring(leftIndex, rightIndex));
            } else if (inputLine.matches("<tr><td align=\"left\"><a target=\"_blank\" href=\"papers/.*.pdf\">.*</a></td><td align=\"left\">&nbsp;</td></tr>")) {
                inputLine = inputLine.replaceAll("<tr><td align=\"left\"><a target=\"_blank\" href=\"papers/", "");
                inputLine = inputLine.replaceAll("</a></td><td align=\"left\">&nbsp;</td></tr>", "");
                inputLine = inputLine.replaceAll(".pdf\">", "&&&&&");
                String[] splitString = inputLine.split("&&&&&");
                PaperFactory.switchBoard(1, splitString[1]);
                PaperFactory.switchBoard(2, splitString[0]);
            } else if (inputLine.matches("<tr><td align=\"left\"><i>.*&nbsp; ")) {
                inputLine = inputLine.replaceAll("<tr><td align=\"left\"><i>", "");
                inputLine = inputLine.replaceAll("&nbsp; ", "");
                System.out.println(inputLine);
                PaperFactory.switchBoard(3, inputLine);
            }
        }
        in.close();
    }


    public static void fetchURL(String urlString) throws IOException {
        URL url = new URL(urlString);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            //System.out.println(inputLine);
            if (inputLine.contains("NAME")) {
                int startName = inputLine.indexOf("NAME");
                int leftIndex = inputLine.indexOf(">", startName) + 1;
                int rightIndex = inputLine.indexOf("<", startName);
                PaperFactory.switchBoard(0, inputLine.substring(leftIndex, rightIndex));
            } else if (inputLine.matches("<tr><td align=\"left\"><a target=\"_blank\" href=\"papers/.*.pdf\">.*</a></td><td align=\"left\">&nbsp;</td></tr>")) {
                inputLine = inputLine.replaceAll("<tr><td align=\"left\"><a target=\"_blank\" href=\"papers/", "");
                inputLine = inputLine.replaceAll("</a></td><td align=\"left\">&nbsp;</td></tr>", "");
                inputLine = inputLine.replaceAll(".pdf\">", "&&&&&");
                String[] splitString = inputLine.split("&&&&&");
                PaperFactory.switchBoard(1, splitString[1]);
                PaperFactory.switchBoard(2, splitString[0]);
            } else if (inputLine.matches("<tr><td align=\"left\"><i>.*&nbsp; ")) {
                inputLine = inputLine.replaceAll("<tr><td align=\"left\"><i>", "");
                inputLine = inputLine.replaceAll("&nbsp; ", "");
                System.out.println(inputLine);
                PaperFactory.switchBoard(3, inputLine);
            }
        }
        in.close();
    }

    public static void fetchURL2(String urlString) throws IOException {
        URL url = new URL(urlString);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            //System.out.println(inputLine);
            if (inputLine.contains("name")) {
                int startName = inputLine.indexOf("name");
                int leftIndex = inputLine.indexOf(">", startName) + 1;
                int rightIndex = inputLine.indexOf("<", startName);
                PaperFactory.switchBoard(0, inputLine.substring(leftIndex, rightIndex));
            } else if (inputLine.matches("<tr><td align=\"left\" width=\"723\"><a target=\"_blank\" href=\"papers/.*.pdf\">.*</a></td><td align=\"left\">&nbsp;</td></tr>")) {
                inputLine = inputLine.replaceAll("<tr><td align=\"left\" width=\"723\"><a target=\"_blank\" href=\"papers/", "");
                inputLine = inputLine.replaceAll("</a></td><td align=\"left\">&nbsp;</td></tr>", "");
                inputLine = inputLine.replaceAll(".pdf\">", "&&&&&");
                String[] splitString = inputLine.split("&&&&&");
                PaperFactory.switchBoard(1, splitString[1]);
                PaperFactory.switchBoard(2, splitString[0]);
            } else if (inputLine.contains("<tr><td align=\"left\" width=\"723\"><i>")) {
                inputLine = inputLine.replaceAll("<tr><td align=\"left\" width=\"723\"><i>", "");
                String[] splitted = inputLine.split("&nbsp;");
                //inputLine = inputLine.replaceAll("&nbsp; ", "");
                PaperFactory.switchBoard(3, splitted[0]);
            }
        }
        in.close();
    }

    public static void createXML(String year) throws ParserConfigurationException, TransformerException, FileNotFoundException {
        //String root = bf.readLine();

        System.out.println(PaperFactory.getPapers().size());
        DocumentBuilderFactory documentBuilderFactory =
                DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder =
                documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        Element rootElement = document.createElement("BMVC");
        document.appendChild(rootElement);

        Element editionElement = document.createElement("Edition");
        rootElement.appendChild(editionElement);

        Element fullTitleElement = document.createElement("FullTitle");
        fullTitleElement.appendChild(document.createTextNode("British Machine Vision Conference " + year));
        editionElement.appendChild(fullTitleElement);

        Element ShortTitle = document.createElement("ShortTitle");
        ShortTitle.appendChild(document.createTextNode("BMVC " + year));
        editionElement.appendChild(ShortTitle);

        Element BibString = document.createElement("ShortTitle");
        BibString.appendChild(document.createTextNode("BMVC " + year));
        editionElement.appendChild(BibString);

        Element Location = document.createElement("Location");
        Location.appendChild(document.createTextNode("Norwich"));
        editionElement.appendChild(Location);

        Element Publisher = document.createElement("Publisher");
        Publisher.appendChild(document.createTextNode("British Machine Vision Association"));
        editionElement.appendChild(Publisher);

        Element Sponsor = document.createElement("Sponsor");
        Sponsor.appendChild(document.createTextNode("British Machine Vision Association"));
        editionElement.appendChild(Sponsor);

        Element Year = document.createElement("Year");
        Year.appendChild(document.createTextNode(year));
        editionElement.appendChild(Year);

        Element Chair = document.createElement("Chair");
        Chair.appendChild(document.createTextNode(""));
        editionElement.appendChild(Chair);

        Element Chair1 = document.createElement("Chair");
        Chair1.appendChild(document.createTextNode(""));
        editionElement.appendChild(Chair1);

        /*Element Chair2 = document.createElement("Chair");
        Chair2.appendChild(document.createTextNode("Manuel Trucco"));
        editionElement.appendChild(Chair2);*/

        Element Ed = document.createElement("Ed");
        Ed.appendChild(document.createTextNode("Richard Harvey"));
        editionElement.appendChild(Ed);

        Element Ed1 = document.createElement("Ed");
        Ed1.appendChild(document.createTextNode("Andrew Bangham"));
        editionElement.appendChild(Ed1);

        Element ISBN = document.createElement("ISBN");
        ISBN.appendChild(document.createTextNode(""));
        editionElement.appendChild(ISBN);

        Element Doi = document.createElement("Doi");
        Doi.appendChild(document.createTextNode(""));
        editionElement.appendChild(Doi);

        Element URL = document.createElement("URL");
        URL.appendChild(document.createTextNode("http://www.bmva.org/bmvc/" + year + "/index.html"));
        editionElement.appendChild(URL);

        Element RootURL = document.createElement("RootURL");
        RootURL.appendChild(document.createTextNode("http://www.bmva.org/bmvc/" + year));
        editionElement.appendChild(RootURL);

        Element paperListElement = document.createElement("PaperList");
        rootElement.appendChild(paperListElement);


        ArrayList<Paper> papers = PaperFactory.getPapers();

        for (int i = 0; i < papers.size(); i++) {

            Paper paper = papers.get(i);

            Element paperElement = document.createElement("Paper");
            paperListElement.appendChild(paperElement);

            Element titleElement = document.createElement("Title");
            titleElement.appendChild(document.createTextNode(paper.getTitle()));
            paperElement.appendChild(titleElement);

            Element pdfElement = document.createElement("PDF");
            pdfElement.appendChild(document.createTextNode(paper.getPdf()));
            paperElement.appendChild(pdfElement);

            Element tailURLElement = document.createElement("TailURL");
            tailURLElement.appendChild(document.createTextNode(paper.getTailURL()));
            paperElement.appendChild(tailURLElement);

            Element sessionElement = document.createElement("Session");
            sessionElement.appendChild(document.createTextNode(paper.getSession()));
            paperElement.appendChild(sessionElement);

            for (Author author : paper.getAuthors()) {
                Element authorElement = document.createElement("Author");
                authorElement.setAttribute("institution", author.getInstitution());
                authorElement.appendChild(document.createTextNode(author.getName()));
                paperElement.appendChild(authorElement);
            }
        }
        TransformerFactory transformerFactory =
                TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        FileOutputStream fileOutputStream = new FileOutputStream(year + "XML.xml");
        StreamResult result = new StreamResult(fileOutputStream);
        transformer.transform(source, result);
    }

    public static String getExpectedTag() {
        if (PaperFactory.getPaper() == null) return "span";
        if (PaperFactory.getPaper().getPdf() == null) return "a";
        if (PaperFactory.getPaper().getAuthors() == null) return "br";
        return null;
    }

    public static int getOption() {
        if (PaperFactory.getPaper() == null) return 1;
        if (PaperFactory.getPaper().getPdf() == null) return 2;
        if (PaperFactory.getPaper().getAuthors() == null) return 3;
        return -1;
    }

    public static Node getBodyNode(Node htmlNode) {
        NodeList nodeList = htmlNode.getChildren();
        SimpleNodeIterator simpleNodeIterator = nodeList.elements();
        while (simpleNodeIterator.hasMoreNodes()) {
            Node node = simpleNodeIterator.nextNode();
            if (node.getText().startsWith("body")) return node;
        }
        return null;
    }

    public static Node getHTMLNode(String webPageURL) throws SAXException, ParserException {
        URL url;
        URLConnection urlConnection;
        try {
            url = new URL(webPageURL);
            urlConnection = url.openConnection();
            urlConnection.connect();
            //urlConnection.setRequestProperty("Authorization", "Basic " + getAuthenticationString());
            InputStream is = urlConnection.getInputStream();
            Parser parser = new Parser();
            //parser.setConnection(urlConnection);
            parser.setURL(webPageURL);
            NodeList nodeList = parser.parse(null);
            SimpleNodeIterator simpleNodeIterator = nodeList.elements();
            while (simpleNodeIterator.hasMoreNodes()) {
                Node node = simpleNodeIterator.nextNode();
                System.out.println(node.getText());
                if (node.getText().contains("html")) return node;
            }
            //InputStreamReader isr = new InputStreamReader(is);
            //Document document = builder.parse(is);
            //Document document = tidy.parseDOM(is, null);

            /*int numCharsRead;
            char[] charArray = new char[1024];
            StringBuffer sb = new StringBuffer();
            while ((numCharsRead = isr.read(charArray)) > 0) {
                sb.append(charArray, 0, numCharsRead);
            }
            return sb.toString();*/
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
