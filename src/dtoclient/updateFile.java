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
import java.io.FileNotFoundException;
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
public class updateFile {

    private static Logger logger = Logger.getLogger(updateFile.class);
    private static final String algorithm = "AES";
    private static byte[] keyvalue;
//update the contents of a file which has different md5 on server and client
    public static void fileUpdate(String fileOnServer, String locationOnClientSide) throws FileNotFoundException, IOException, InvalidPacketException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Socket socketForUpdatingFile = null;
        InputStream inputstreamForUpdatingFile = null;
        OutputStream outputstreamForUpdatingFile = null;
        try {
            String fileServer = fileOnServer + ".txt";
            String key = "Thats my Kung Fu"; //key for encryption and decryption
            String decryptedText = null;
            socketForUpdatingFile = socketCreation.createSocket();
            inputstreamForUpdatingFile = socketForUpdatingFile.getInputStream();
            outputstreamForUpdatingFile = socketForUpdatingFile.getOutputStream();
            //Reading data from server's file
            File fileToBeReadFromServer = new File("C:\\Users\\Anil\\Documents\\NetBeansProjects\\UpdateServer\\" + fileServer);
            if (fileToBeReadFromServer.exists()) {
                FileReader filereader = new FileReader(fileToBeReadFromServer);
                BufferedReader bufferedreader = new BufferedReader(filereader);
                logger.info("Sending to server for file update---there is a change in md5");
                DataPacket dataSentForFileUpdate = new DataPacket();
                dataSentForFileUpdate.setMessageType(Constants.FILE_UPDATE);
                dataSentForFileUpdate.setString(Constants.FILE_NAME, fileOnServer);
                TCPProcessor.send(outputstreamForUpdatingFile, dataSentForFileUpdate);
                 //receive encypted text of file contents
                DataPacket dataRecievedFromServer = TCPProcessor.receive(inputstreamForUpdatingFile);
                if (dataRecievedFromServer.getMessageType() == Constants.FILE_UPDATE) {
                    String encryptedValue = dataRecievedFromServer.getString(Constants.TEXT_CONTENT);
                    decryptedText = fileDecrypt(key, encryptedValue);
                    logger.info("DecryptedText :" + decryptedText);
                }
                // Writing decrypted data to client's location
                File fileTobeUpdatedInClient = new File("C:\\Users\\Anil\\" + locationOnClientSide + "\\" + fileServer);
                fileTobeUpdatedInClient.delete();
                logger.info("File deleted");
                File clientFileOfUpdate = new File("C:\\Users\\Anil\\" + locationOnClientSide + "\\" + fileServer);
                if (clientFileOfUpdate.exists()) {
                    logger.info("FILE ALREADY EXISTS, DELETE UNSUCCESFULL");
                } else {
                    clientFileOfUpdate.createNewFile();
                    logger.info("New file created successfully");
                }
                FileWriter filewriter = new FileWriter(clientFileOfUpdate);
                BufferedWriter bufferedwriter = new BufferedWriter(filewriter);
                String line ;
                while ((line = bufferedreader.readLine()) != null) {

                    bufferedwriter.write(line);
                    bufferedwriter.newLine();
                }

                bufferedwriter.flush();
                bufferedwriter.close();
            }
        } catch (Exception e) {
            logger.info(e + "  at file update");
        } finally {
            //socketForUpdatingFile.close();
            inputstreamForUpdatingFile.close();
            outputstreamForUpdatingFile.close();
        }
    }
//decrypt encrypted text
    public static String fileDecrypt(String keyGiven, String encryptedText) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {
        String decryptedValue = null;
        keyvalue = keyGiven.getBytes();
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedString=new BASE64Decoder().decodeBuffer(encryptedText);
        byte[] decodedValue=cipher.doFinal(decodedString);
        decryptedValue=new String(decodedValue);
        return decryptedValue;
    }
     public static Key generateKey() {

        Key key = new SecretKeySpec(keyvalue, algorithm);
        return key;
    }
}
