/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dtoclient;

import java.io.File;
import java.io.IOException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Anil
 */
public class xmlUpdate {
     private static Logger logger = Logger.getLogger(xmlUpdate.class);
//update filecontents to xml
     public static int updateXml(Document doc, int indexToupdate, String newmd5) throws TransformerConfigurationException, TransformerException, IOException {
        Node root = doc.getFirstChild();
         Node fileinfo = doc.getElementsByTagName("file_info").item(indexToupdate);
        NodeList list = fileinfo.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);

            if ("md5".equals(node.getNodeName())) {
                node.setTextContent(newmd5);
            }
        }
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File("/C:/Users/Anil/Desktop/reve/client.xml"));
        transformer.transform(source, result);
        int flag=1;
       logger.info("xml file updated");
       return flag;

    }

    
}
