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
 * User: rrd09
 * Date: 28/09/11
 * Time: 15:15
 * To change this template use File | Settings | File Templates.
 */
public class Main2006 {
    private static DocumentBuilder builder;

    public static void main(String args[]) throws SAXException, ParserException, TransformerException, ParserConfigurationException, IOException, MalformedURLException {
        //Common.fetchURL("http://www.macs.hw.ac.uk/bmvc2006/volume1.html");
        //Common.fetchURL2("http://www.macs.hw.ac.uk/bmvc2006/volume2.html");
        Common.fetchURL3("http://www.macs.hw.ac.uk/bmvc2006/volume3.html");
        Common.createXML("2006");
        /*Node htmlNode = getHTMLNode("http://www.macs.hw.ac.uk/bmvc2006/volume1.html");
        Node bodyNode = getBodyNode(htmlNode);
        Node tableNode = getTableNode(bodyNode);
        Node trNode = getTRNode(tableNode);
        NodeList nodeList = trNode.getChildren();
        SimpleNodeIterator simpleNodeIterator = nodeList.elements();
        while (simpleNodeIterator.hasMoreNodes())
        {
            Node node = simpleNodeIterator.nextNode();
            System.out.println(node.getText());
            if (node.getText().startsWith("h3")) {
                PaperFactory.switchBoard(0, node);
            } else {
                if (node.getText().startsWith(getExpectedTag())) {
                    PaperFactory.switchBoard(getOption(), node);
                }
            }
        }
        createXML();*/
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
