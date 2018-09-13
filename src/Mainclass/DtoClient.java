/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mainclass;

import Dto.dto;
import dtoclient.HashmapUpdateForClientSide;
import dtoclient.synchronisation;
import java.net.Socket;
import java.util.HashMap;
import org.apache.log4j.Logger;

public class DtoClient {

    private static Logger logger = Logger.getLogger(DtoClient.class);
    public static Socket socket = null;
    public static HashMap<String, Object> map;
    public String filename = null;
    public String location = null;
    public String md5 = null;
    public int id = 0;
    public String systemConfig = null;
    public dto obj = null;
// main class of client side

    public static void main(String[] args) {
        try {
            map = new HashMap<String, Object>();
            // client parses xml file for updating data on client's Hashmap
            HashmapUpdateForClientSide.updateHashMap();
            logger.info("CLIENT READY");
            Thread synchronisation = new Thread(new synchronisation());
            synchronisation.start();
            synchronisation.join();
        } catch (Exception e) {
            logger.info(e);
        }

    }

}
