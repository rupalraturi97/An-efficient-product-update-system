/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtoclient;

import Constants.Constants;
import Mainclass.DtoClient;
import Dto.dto;
import exception.InvalidPacketException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.transform.TransformerException;
import org.apache.log4j.Logger;

/**
 *
 * @author Anil
 */
public class checkOperationTypeForFileUpdate {

    private static Logger logger = Logger.getLogger(checkOperationTypeForFileUpdate.class);

    public static void updateFileWithCorrespondingMessageType(dto dtoObjectHavingCommaSeperatedDetails) throws IOException, FileNotFoundException, InvalidPacketException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, TransformerException {
        // split all comma seperated files and put them in an array for further use
        try {
            String filesHavingChange[] = dtoObjectHavingCommaSeperatedDetails.getFilename().split(",");
            int opearationTypesOfFilesWithAnyChange[] = new int[filesHavingChange.length];
            String md5ForFilesHavingChange[] = dtoObjectHavingCommaSeperatedDetails.getMd5().split(",");
            String locationsForFilesHavingChange[] = dtoObjectHavingCommaSeperatedDetails.getLocation().split(",");
            String operationTypes[] = dtoObjectHavingCommaSeperatedDetails.getOperationType().split(",");
            for (int i = 0; i < operationTypes.length; i++) {
                opearationTypesOfFilesWithAnyChange[i] = Integer.parseInt(operationTypes[i]);
            }

            /*check operation type of every file, and perform the necessary operation
             1- files having different md5 on server and client
             2-files to be deleted from client's location
             3-insert files at client's location,for files which are extra on server*/
            int i = 0;
            while (i < opearationTypesOfFilesWithAnyChange.length) {
                // for updating the contents of a file which exists on both client and server but with different md5
                if (opearationTypesOfFilesWithAnyChange[i] == 1) {
                    logger.info("File exists on both server and client but with different md5 : " + filesHavingChange[i]);
                    dto objectToUpdateMd5OfClientAndServer = (dto) DtoClient.map.get(filesHavingChange[i]);
                    int idToBeUpdated = objectToUpdateMd5OfClientAndServer.getId();
                    objectToUpdateMd5OfClientAndServer.setMd5(md5ForFilesHavingChange[i]);
                    String md55 = objectToUpdateMd5OfClientAndServer.getMd5();
                    DtoClient.map.put(filesHavingChange[i], objectToUpdateMd5OfClientAndServer);
                    //checking the return type of xmlupdated. if xml is updated,update file
                    int flagForXmlUpdate = xmlUpdate.updateXml(HashmapUpdateForClientSide.doc, (idToBeUpdated - 1-i), md5ForFilesHavingChange[i]);
                    if (flagForXmlUpdate == 1) {
                        updateFile.fileUpdate(filesHavingChange[i], objectToUpdateMd5OfClientAndServer.getLocation());
                    }
                    i++;
                } //for deleting a file from client's location
                else if (opearationTypesOfFilesWithAnyChange[i] == 2) {
                    dto objDelete = (dto) DtoClient.map.get(filesHavingChange[i]);
                    logger.info("File To be deleted is :" + filesHavingChange[i]);
                    int idToBeDeleted = objDelete.getId();
                    int flagForXmlDelete = xmlDelete.deleteFromXml(HashmapUpdateForClientSide.doc, idToBeDeleted - 1);
                    //check return type of xml delete. if 1 . delete file
                    if (flagForXmlDelete == 1) {
                        deleteFile.fileDelete(filesHavingChange[i], locationsForFilesHavingChange[i]);
                    }
                    logger.info("File Deleted From Server. Remove From Client");

                    i++;
                } // for adding a file on client's location from server
                else if (opearationTypesOfFilesWithAnyChange[i] == 3) {
                    logger.info("SERVER HAS EXTRA FILES . ADD FILES");
                    logger.info("file to be added is :" + filesHavingChange[i]);
                    dto objectAdd = new dto();
                    objectAdd.setFilename(filesHavingChange[i]);
                    objectAdd.setMd5(md5ForFilesHavingChange[i]);
                    objectAdd.setLocation(locationsForFilesHavingChange[i]);
                    String filenameToBeAdded = objectAdd.getFilename();
                    String md5ToBeAdded = objectAdd.getMd5();
                    String locationToBeAdded = objectAdd.getLocation();
                    DtoClient.map.put(filesHavingChange[i], objectAdd);
                    int flagForXmlAdd = xmlAdd.addToXml(HashmapUpdateForClientSide.doc, filenameToBeAdded, md5ToBeAdded, locationToBeAdded);
                    //check return type of xmlAdd . if 1 add file
                    if (flagForXmlAdd == 1) {
                        addFile.fileAdd(filesHavingChange[i], locationsForFilesHavingChange[i]);
                    }
                    i++;
                } else {
                    System.out.println("No operation specified");
                    i++;
                }

            }
               
        } catch (Exception e) {
            logger.info(e);
        }

        //return flag;
    }

}
