/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtoclient;

import Dto.dto;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 *
 * @author Anil
 */
public class synchronisation implements Runnable {

    private static Logger logger = Logger.getLogger(synchronisation.class);
    public static long lastUpdatedTime = 0;

    @Override
    //schedule the update for every 24 hours, check after 24 hours if there is an update for any file
    public void run() {
        try {
            dto dtoObjectOfChangedContentsFromServer = null;
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String currentDate = formatter.format(date);
            logger.info("current date is : " + currentDate);
            long currentTime = System.currentTimeMillis();
            logger.info("current time  is : " + currentTime);

            if (currentTime - lastUpdatedTime >= 86400000) {
                dtoObjectOfChangedContentsFromServer = readFile.checkFiles();
                 checkOperationTypeForFileUpdate.updateFileWithCorrespondingMessageType(dtoObjectOfChangedContentsFromServer);
                   lastUpdatedTime = System.currentTimeMillis();
                    logger.info("after update time : " + lastUpdatedTime);
                
            }
            else
            {
                System.out.println("wait for update");}
        } catch (Exception e) {
            logger.info(e);
        }
    }

}
