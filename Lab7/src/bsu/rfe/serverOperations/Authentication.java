/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bsu.rfe.serverOperations;

import bsu.rfe.AuthenticationPanel;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *
 * @author hlebA
 */
public class Authentication {

    public int logIn(String senderName, String senderPassword,
            String destinationAddress, int PORT) {
        boolean status = false;
        try {
            Socket socket = new Socket(destinationAddress, PORT);
            final DataOutputStream out =
                    new DataOutputStream(socket.getOutputStream());
            final DataInputStream in = new DataInputStream(
                    socket.getInputStream());
            out.writeUTF("Authentication");
            out.writeUTF(senderName);
            out.writeUTF(senderPassword);
            status = in.readBoolean();
            socket.close();

        } catch (IOException ex) {
             return 1;
        }
        if(status) {
                    return 0;
        }
        return 2;
    }
}
