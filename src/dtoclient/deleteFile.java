/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dtoclient;

import java.io.File;
import org.apache.log4j.Logger;

/**
 *
 * @author Anil
 */
public class deleteFile {
    private static Logger logger = Logger.getLogger(deleteFile.class);
//delete file from client's location
    public static void fileDelete(String filename, String fileLocation) {
        try{
        String fileName = filename + ".txt";
        File fileToBeDeleted = new File("C:\\Users\\Anil\\" + fileLocation + "\\" + fileName);
        logger.info(" text file to be deleted is " + filename );
        if (fileToBeDeleted.delete()) {
            logger.info("File deleted which was extra on client");
        }
    }catch(Exception e)
    {
    logger.info(e);
    }}
    
}
