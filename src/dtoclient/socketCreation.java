/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dtoclient;

import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Anil
 */
public class socketCreation {
    //create a new socket
    public static Socket createSocket() throws IOException
    {
        Socket socket=new Socket("localhost",1234);
    return socket;
    }
}
