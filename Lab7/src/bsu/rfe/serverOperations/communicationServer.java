/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bsu.rfe.serverOperations;

import bsu.rfe.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.Timer;

/**
 *
 * @author hleb
 */
public class communicationServer {

    String destinationAddress;
    int PORT;
    String userName;
    Authentication authentication = new Authentication();
    public MessageManager messageServer;
    private MainPanel mainPanel;

    public communicationServer(String destinationAddress, int PORT) {
        this.destinationAddress = destinationAddress;
        this.PORT = PORT;
    }

    public communicationServer(String userName, ArrayList<ChatPanel> openDialogs,
            MainPanel mainPanel, String destinationAddress, int PORT) {
        this.destinationAddress = destinationAddress;
        this.PORT = PORT;
        this.userName = userName;
        this.mainPanel = mainPanel;
        startMessageServer(openDialogs);

    }
    private Timer messageTimer = new Timer(3000, new ActionListener() {
        public void actionPerformed(ActionEvent event) {
            try {
                while (messageServer.receiveMessage(userName)) {
                }
            } catch (IOException ex) {
            }
        }
    });

    public boolean sendMessage(Message message) throws IOException {
        return messageServer.sendMessage(message);
    }

    public int logIn(String senderName, String senderPassword) {
        return authentication.logIn(senderName, senderPassword, destinationAddress, PORT);
    }

    public void startServer() {
        messageTimer.start();
    }

    public void startMessageServer(ArrayList<ChatPanel> openDialogs) {
        messageServer = new MessageManager(openDialogs, destinationAddress, PORT, mainPanel);
    }

    public int findUser(String user) throws IOException {
        Socket socket = new Socket(destinationAddress, PORT);
        final DataOutputStream out =
                new DataOutputStream(socket.getOutputStream());
        final DataInputStream in = new DataInputStream(
                socket.getInputStream());
        out.writeUTF("FindUser");
        out.writeUTF(user);
        boolean status1 = in.readBoolean();
        if (status1) {
            if (in.readBoolean() == true) {
                socket.close();
                return 2;
            }

            socket.close();
            return 1;
        }

        socket.close();
        return 0;
    }
}
