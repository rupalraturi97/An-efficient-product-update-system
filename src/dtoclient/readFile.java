/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtoclient;

import Dto.dto;
import Constants.Constants;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import packet.DataPacket;
import tcp.TCPProcessor;
import org.apache.log4j.Logger;

public class readFile {

    private static Logger logger = Logger.getLogger(readFile.class);
    private static byte[] keyvalue;
    private static final String algorithm = "AES";
    public static int indexToupdate = 0;
//send comma seperated files of client and receive comma seperated files which needs to be changed from server

    public static dto checkFiles() throws IOException {
        dto objectHavingChangedContentsOfFilesFromServer = new dto();
        Socket socketToSendClientFilesToServer = null;
        OutputStream outputstreamForClientFilesToServer = null;
        InputStream inputstreamForClientFilesToServer = null;
        try {
            String allFilesOnClient = "";
            String allMd5sOnClient = "";
            for (int j = 0; j < HashmapUpdateForClientSide.files.length; j++) {
                if (j == 0) {
                    allFilesOnClient += HashmapUpdateForClientSide.files[j];
                } else {
                    allFilesOnClient += "," + HashmapUpdateForClientSide.files[j];

                }
            }
            String filesOnClient[] = allFilesOnClient.split(",");
            for (int j = 0; j < HashmapUpdateForClientSide.allMd5OnClient.length; j++) {
                if (j == 0) {
                    allMd5sOnClient += HashmapUpdateForClientSide.allMd5OnClient[j];
                } else {
                    allMd5sOnClient += "," + HashmapUpdateForClientSide.allMd5OnClient[j];
                }
            }
            String md5sOnClient[] = allFilesOnClient.split(",");
            logger.info(allFilesOnClient);
                  //sending client files to server to check for an update
            DataPacket requestPacket = new DataPacket();
            requestPacket.setMessageType(Constants.UPDATE);
            requestPacket.setString(Constants.FILE_NAME, allFilesOnClient);
            requestPacket.setString(Constants.MD5, allMd5sOnClient);
            socketToSendClientFilesToServer = socketCreation.createSocket();
            outputstreamForClientFilesToServer = socketToSendClientFilesToServer.getOutputStream();
            TCPProcessor.send(outputstreamForClientFilesToServer, requestPacket);
                  //receiving files which have a change from server
            inputstreamForClientFilesToServer = socketToSendClientFilesToServer.getInputStream();
            DataPacket fromServer = TCPProcessor.receive(inputstreamForClientFilesToServer);
            if (fromServer.getMessageType() == Constants.UPDATE) {
                logger.info("Files recieved from server : " + fromServer.getString(Constants.FILES_TO_BE_CHANGED));
                logger.info("MD5s recieved  from server : " + fromServer.getString(Constants.MD5_TO_BE_CHANGED));
                logger.info("Operation Types recieved from server : " + fromServer.getString(Constants.OPERATION_TYPE_TO_BE_CHANGED));
                logger.info("Locations recieved from server : " + fromServer.getString(Constants.LOCATIONS_FOR_FILES_TO_BE_CHANGED));
                objectHavingChangedContentsOfFilesFromServer.setFilename(fromServer.getString(Constants.FILES_TO_BE_CHANGED));
                objectHavingChangedContentsOfFilesFromServer.setMd5(fromServer.getString(Constants.MD5_TO_BE_CHANGED));
                objectHavingChangedContentsOfFilesFromServer.setLocation(fromServer.getString(Constants.LOCATIONS_FOR_FILES_TO_BE_CHANGED));
                objectHavingChangedContentsOfFilesFromServer.setOperationType(fromServer.getString(Constants.OPERATION_TYPE_TO_BE_CHANGED));
            }

            return objectHavingChangedContentsOfFilesFromServer;

        } catch (Exception e) {
            logger.info(e);
            return objectHavingChangedContentsOfFilesFromServer;

        } finally {
            outputstreamForClientFilesToServer.close();
            inputstreamForClientFilesToServer.close();
        }

    }

}
