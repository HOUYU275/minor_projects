import org.htmlparser.*;
import org.htmlparser.Node;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.SimpleNodeIterator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.*;
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
 * User: rrd09
 * Date: 28/09/11
 * Time: 15:15
 * To change this template use File | Settings | File Templates.
 */
public class Main2004 {
    private static DocumentBuilder builder;

    public static void main(String args[]) throws SAXException, ParserException, TransformerException, ParserConfigurationException, FileNotFoundException {
        Node htmlNode = getHTMLNode("http://www.bmva.org/bmvc/2004/BMVC_2004a.html");
        Node bodyNode = getBodyNode(htmlNode);
        NodeList nodeList = bodyNode.getChildren();
        SimpleNodeIterator simpleNodeIterator = nodeList.elements();
        while (simpleNodeIterator.hasMoreNodes()) {
            Node node = simpleNodeIterator.nextNode();
            if (node.getText().startsWith("h3")) {
                PaperFactory.switchBoard(0, node);
            } else {
                if (node.getText().startsWith(getExpectedTag())) {
                    PaperFactory.switchBoard(getOption(), node);
                }
            }
        }
        createXML();
    }

    private static void createXML() throws ParserConfigurationException, TransformerException, FileNotFoundException {
        //String root = bf.readLine();
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
        fullTitleElement.appendChild(document.createTextNode("British Machine Vision Conference 2004"));
        editionElement.appendChild(fullTitleElement);

        Element ShortTitle = document.createElement("ShortTitle");
        ShortTitle.appendChild(document.createTextNode("BMVC 2004"));
        editionElement.appendChild(ShortTitle);

        Element BibString = document.createElement("ShortTitle");
        BibString.appendChild(document.createTextNode("BMVC 2004"));
        editionElement.appendChild(BibString);

        Element Location = document.createElement("Location");
        Location.appendChild(document.createTextNode("Kingston"));
        editionElement.appendChild(Location);
        
        Element Publisher = document.createElement("Publisher");
        Publisher.appendChild(document.createTextNode("British Machine Vision Association"));
        editionElement.appendChild(Publisher);

        Element Sponsor = document.createElement("Sponsor");
        Sponsor.appendChild(document.createTextNode("British Machine Vision Association"));
        editionElement.appendChild(Sponsor);

        Element Year = document.createElement("Year");
        Year.appendChild(document.createTextNode("2004"));
        editionElement.appendChild(Year);

        Element Chair = document.createElement("Chair");
        Chair.appendChild(document.createTextNode(""));
        editionElement.appendChild(Chair);

        Element Chair1 = document.createElement("Chair");
        Chair1.appendChild(document.createTextNode(""));
        editionElement.appendChild(Chair1);
        
        Element Ed = document.createElement("Ed");
        Ed.appendChild(document.createTextNode("Andreas Hoppe"));
        editionElement.appendChild(Ed);

        Element Ed1 = document.createElement("Ed");
        Ed1.appendChild(document.createTextNode("Sarah Barman"));
        editionElement.appendChild(Ed1);

        Element Ed2 = document.createElement("Ed");
        Ed2.appendChild(document.createTextNode("Tim Ellis"));
        editionElement.appendChild(Ed2);

        Element ISBN = document.createElement("ISBN");
        ISBN.appendChild(document.createTextNode("1-901725-25-1"));
        editionElement.appendChild(ISBN);

        Element Doi = document.createElement("Doi");
        Doi.appendChild(document.createTextNode(""));
        editionElement.appendChild(Doi);

        Element URL = document.createElement("URL");
        URL.appendChild(document.createTextNode("http://www.bmva.org/bmvc/2004/index.html"));
        editionElement.appendChild(URL);

        Element RootURL = document.createElement("RootURL");
        RootURL.appendChild(document.createTextNode("http://www.bmva.org/bmvc/2004"));
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
        FileOutputStream fileOutputStream = new FileOutputStream("2004XML.xml");
        StreamResult result = new StreamResult(fileOutputStream);
        transformer.transform(source, result);
    }

    private static String getExpectedTag() {
        if (PaperFactory.getPaper() == null) return "span";
        if (PaperFactory.getPaper().getPdf() == null) return "a";
        if (PaperFactory.getPaper().getAuthors() == null) return "br";
        return null;
    }

    private static int getOption() {
        if (PaperFactory.getPaper() == null) return 1;
        if (PaperFactory.getPaper().getPdf() == null) return 2;
        if (PaperFactory.getPaper().getAuthors() == null) return 3;
        return -1;
    }

    private static Node getBodyNode(Node htmlNode) {
        NodeList nodeList = htmlNode.getChildren();
        SimpleNodeIterator simpleNodeIterator = nodeList.elements();
        while (simpleNodeIterator.hasMoreNodes()) {
            Node node = simpleNodeIterator.nextNode();
            if (node.getText().startsWith("body")) return node;
        }
        return null;
    }

    private static Node getHTMLNode(String webPageURL) throws SAXException, ParserException {
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
                if (node.getText().equals("html")) return node;
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

    /*public void print(String fileName, PrintStream out)
            throws SAXException, IOException {
        Document document = builder.parse(fileName);

        NodeList nodes_i
                = document.getDocumentElement().getChildNodes();
        for (int i = 0; i < nodes_i.getLength(); i++) {
            Node node_i = nodes_i.item(i);
            if (node_i.getNodeType() == Node.ELEMENT_NODE
                    && ((Element) node_i).getTagName()
                    .equals("CHESSBOARD")) {
                Element chessboard = (Element) node_i;
                NodeList nodes_j = chessboard.getChildNodes();
                for (int j = 0; j < nodes_j.getLength(); j++) {
                    Node node_j = nodes_j.item(j);
                    if (node_j.getNodeType() == Node.ELEMENT_NODE) {
                        Element pieces = (Element) node_j;
                        NodeList nodes_k = pieces.getChildNodes();
                        for (int k = 0; k < nodes_k.getLength(); k++) {
                            Node node_k = nodes_k.item(k);
                            if (node_k.getNodeType() == Node.ELEMENT_NODE) {
                                Element piece = (Element) node_k;
                                Element position
                                        = (Element) piece.getChildNodes().item(0);
                                out.println((pieces.getTagName()
                                        .equals("WHITEPIECES")
                                        ? "White " : "Black ")
                                        + piece.getTagName().toLowerCase()
                                        + ": "
                                        + position.getAttribute("COLUMN")
                                        + position.getAttribute("ROW"));
                            }
                        }
                    }
                }
            }
        }
        return;
    }*/
}
