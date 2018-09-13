/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtoclient;

import Constants.Constants;
import exception.InvalidPacketException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import org.apache.log4j.Logger;
import packet.DataPacket;
import sun.misc.BASE64Decoder;
import tcp.TCPProcessor;

/**
 *
 * @author Anil
 */
public class addFile {

    private static final String algorithm = "AES";
    private static byte[] keyvalue;
    private static Logger logger = Logger.getLogger(addFile.class);

//add file to client's location , which is extra on server's side
    public static void fileAdd(String fileOnServer, String locationOnClientSide) throws IOException, InvalidPacketException {
        Socket socketForAddingFile = null;
        InputStream inputstreamForAddingFile = null;
        OutputStream outputstreamForAddingFile = null;
        try {
            String fileServer = fileOnServer + ".txt";
            String key = "Thats my Kung Fu";
            String decryptedText = null;
            String loc = locationOnClientSide;
            socketForAddingFile = socketCreation.createSocket();
            inputstreamForAddingFile = socketForAddingFile.getInputStream();
            outputstreamForAddingFile = socketForAddingFile.getOutputStream();
            //Reading data, from server's file location
            File fileToBeReadFromServer = new File("C:\\Users\\Anil\\Documents\\NetBeansProjects\\UpdateServer\\" + fileServer);
            if (fileToBeReadFromServer.exists()) {
                FileReader filereader = new FileReader(fileToBeReadFromServer);
                BufferedReader bufferedreader = new BufferedReader(filereader);
                logger.info("Sending to server: fetch contents of extra file on server");
                DataPacket dataSentForFileUpdate = new DataPacket();
                dataSentForFileUpdate.setMessageType(Constants.FILE_UPDATE);
                dataSentForFileUpdate.setString(Constants.FILE_NAME, fileOnServer);
                TCPProcessor.send(outputstreamForAddingFile, dataSentForFileUpdate);
                
                DataPacket dataRecievedFromServer = TCPProcessor.receive(inputstreamForAddingFile);
                if (dataRecievedFromServer.getMessageType() == Constants.FILE_UPDATE) {
                    String encryptedValue = dataRecievedFromServer.getString(Constants.TEXT_CONTENT);
                    decryptedText = fileDecrypt(key, encryptedValue);
                    logger.info("DecryptedText at file addition :" + decryptedText);
                }

                // Writing decrypted data to file's location at client side
                File fileTobeUpdatedInClient = new File("C:\\Users\\Anil\\" + loc + "\\" + fileServer);
                fileTobeUpdatedInClient.delete();
                logger.info("File deleted");
                File clientFileOfUpdate = new File("C:\\Users\\Anil\\" + loc + "\\" + fileServer);
                if (clientFileOfUpdate.exists()) {
                    logger.info("ALREADY EXISTS, DELETE UNSUCCESFULL");
                } else {
                    clientFileOfUpdate.createNewFile();
                    logger.info("New file created successfully : file Added");
                }
                FileWriter filewriter = new FileWriter(clientFileOfUpdate);
                BufferedWriter bufferedwriter = new BufferedWriter(filewriter);
                String line = null;
                while ((line = bufferedreader.readLine()) != null) {

                    bufferedwriter.write(line);
                    bufferedwriter.newLine();
                }
                bufferedwriter.flush();
                bufferedwriter.close();
                
            }
        } catch (Exception e) {
            logger.info(e + " at file addition");
        } finally {
            socketForAddingFile.close();
            inputstreamForAddingFile.close();
            outputstreamForAddingFile.close();
        }

    }
// decrypt the encrypted contents of file
    public static String fileDecrypt(String keyGiven, String encryptedText) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {
        logger.info("in decrypt block");
        String decryptedValue = null;
        keyvalue = keyGiven.getBytes();
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedString = new BASE64Decoder().decodeBuffer(encryptedText);
        byte[] decodedValue = cipher.doFinal(decodedString);
        decryptedValue = new String(decodedValue);
        return decryptedValue;
    }

    public static Key generateKey() {

        Key key = new SecretKeySpec(keyvalue, algorithm);
        return key;
    }
}
