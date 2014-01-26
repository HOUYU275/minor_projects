import org.htmlparser.*;
import org.htmlparser.Node;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.SimpleNodeIterator;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.soap.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 28/09/11
 * Time: 15:15
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    private static DocumentBuilder builder;

    public static void main(String args[]) throws SAXException, ParserException {
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
                    //System.out.println(node.getText());
                    //System.out.println("expecting " + getExpectedTag());
                    PaperFactory.switchBoard(getOption(), node);
                    //System.out.println(node.getFirstChild().getText());
                }
            }
            /*if ((node.getText() != null) && (!node.getText().equals("\n")) && (!node.getText().equals(" "))) {
                System.out.println(counter + " " + node.getText());
                counter++;
            }*/


        }


        System.out.println(PaperFactory.getPapers());

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
