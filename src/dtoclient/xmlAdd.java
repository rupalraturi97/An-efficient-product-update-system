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

/**
 *
 * @author Anil
 */
public class xmlAdd {
    
    private static Logger logger = Logger.getLogger(xmlAdd.class);
// add new file to xml
    public static int addToXml(Document doc, String filename, String md5, String location) throws TransformerException {
        Element rootOfXml = doc.getDocumentElement();
        Element newfile_info = doc.createElement("file_info");
        Element newfile_name = doc.createElement("filename");
        newfile_name.setTextContent(filename);
        Element new_md5 = doc.createElement("md5");
        new_md5.setTextContent(md5);
        Element new_location = doc.createElement("location");
        new_location.setTextContent(location);
        Element new_systemConfig = doc.createElement("systemConfig");
        newfile_info.appendChild(newfile_name);
        newfile_info.appendChild(new_md5);
        newfile_info.appendChild(new_location);
        rootOfXml.appendChild(newfile_info);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File("/C:/Users/Anil/Desktop/reve/client.xml"));
        transformer.transform(source, result);
        int flag=1;
        logger.info("File added to xml");
        return flag;
    }

    
}
