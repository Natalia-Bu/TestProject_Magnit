import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class XMLService {
    static void createXML(DBService DBService) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            Document doc = factory.newDocumentBuilder().newDocument();
            Element root = doc.createElement("entries");
            doc.appendChild(root);
            List<Entry> result = DBService.select();
            for (Entry rs : result) {
                Element entry = doc.createElement("entry");
                Element field = doc.createElement("field");
                field.setTextContent(rs.getField());
                entry.appendChild(field);
                root.appendChild(entry);
            }
            File file = new File("1.xml");

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(doc), new StreamResult(file));
        } catch (TransformerException e) {
            e.printStackTrace();
        }catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    static void transformXML() {
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Source xslt = new StreamSource(new File("transform.xslt"));
            Transformer transformer = factory.newTransformer(xslt);
            Source text = new StreamSource(new File("1.xml"));
            transformer.transform(text, new StreamResult(new File("2.xml")));
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    static long parseXML() {
        long sum = 0;
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse("2.xml");
            Node root = document.getDocumentElement();
            NodeList entries = root.getChildNodes();
            for (int i = 0; i < entries.getLength(); i++) {
                Node entry = entries.item(i);
                if (entry.getNodeType() != Node.TEXT_NODE) {
                    sum += Integer.parseInt(entry.getAttributes().item(0).getNodeValue());
                }
            }
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        } catch (SAXException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return sum;
    }
}
