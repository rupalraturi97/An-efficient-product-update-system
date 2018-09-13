/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dtoclient;

import java.io.File;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author Anil
 */
public class xmlDelete {
    private static Logger logger = Logger.getLogger(xmlDelete.class);
// delete file from xml
     public static int deleteFromXml(Document doc, int indexOfNode) throws TransformerException {
        Element rootNode = doc.getDocumentElement();
        Node fileinfo = doc.getElementsByTagName("file_info").item(indexOfNode);
        rootNode.removeChild(fileinfo);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File("/C:/Users/Anil/Desktop/reve/client.xml"));
        transformer.transform(source, result);
        int flag=1;
        logger.info("file deleted from xml");
        return flag;
    }
    
}
