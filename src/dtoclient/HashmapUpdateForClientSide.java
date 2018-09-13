/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtoclient;

import Dto.dto;
import Mainclass.DtoClient;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Anil
 */
public class HashmapUpdateForClientSide {

    private static Logger logger = Logger.getLogger(HashmapUpdateForClientSide.class);
    public static String files[] = new String[3];
    public static String allMd5OnClient[] = new String[3];
    public static Document doc = null;
// parsing xml file to update client's map
    public static void updateHashMap() {
        try {
            int id = 0;
            String md5 = null;
            String location = null;
            String filename = null;
            String systemConfig = null;
            Element element = null;
            File file = new File("/C:/Users/Anil/Desktop/reve/client.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(file);
            doc.getDocumentElement().normalize();
            logger.info("Root Element  " + doc.getDocumentElement().getNodeName());
            NodeList list = doc.getElementsByTagName("file_info");
            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                element = (Element) node;
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    filename = element.getElementsByTagName("filename").item(0).getTextContent();
                    id = Integer.parseInt(element.getAttribute("id"));
                    location = element.getElementsByTagName("location").item(0).getTextContent();
                    systemConfig = element.getElementsByTagName("systemConfig").item(0).getTextContent();
                    md5 = element.getElementsByTagName("md5").item(0).getTextContent();
                    logger.info(md5);
                }
                // create a dto object for every file and store in hashmap<String,dto>
                dto obj = new dto();
                obj.setId(id);
                obj.setLocation(location);
                obj.setMd5(md5);
                allMd5OnClient[i] = obj.getMd5();
                obj.setSystemConfig(systemConfig);
                DtoClient.map.put(filename, obj);
                files[i] = filename;
            }
        } catch (Exception e) {
           logger.info(e);
        }
    }

}
