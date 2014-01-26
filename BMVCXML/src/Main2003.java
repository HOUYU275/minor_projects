import org.htmlparser.Node;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.SimpleNodeIterator;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Ren
 * Date: 28/09/11
 * Time: 22:50
 * To change this template use File | Settings | File Templates.
 */
public class Main2003 {

    //

    public static void main(String args[]) throws SAXException, ParserException, TransformerException, ParserConfigurationException, IOException {
        Common.fetchPage("http://www.bmva.org/bmvc/2003/table_of_contents.html");
        Common.createXML("2003");
    }
}
