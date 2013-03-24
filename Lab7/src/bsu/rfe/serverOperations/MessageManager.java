/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bsu.rfe.serverOperations;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import bsu.rfe.*;


public class MessageManager {
   private String destinationAddress ;
   private int PORT;
   ArrayList<ChatPanel> openDialogs;
   private MainPanel mainPanel;
   
   public MessageManager(ArrayList<ChatPanel> openDialogs,
           String destinationAddress,int PORT,MainPanel mainPanel) {
      this.openDialogs = openDialogs;
      this.destinationAddress = destinationAddress;
      this.PORT = PORT;
      this.mainPanel = mainPanel;
   }
    public boolean sendMessage(Message message) throws IOException {

        Socket socket = new Socket(destinationAddress, PORT);
        final DataOutputStream out =
                new DataOutputStream(socket.getOutputStream());
        final DataInputStream in = new DataInputStream(
                socket.getInputStream());
        out.writeUTF("Message");
        out.writeUTF(message.userFrom);
        out.writeUTF(message.userTo);
        out.writeUTF(message.message);
        boolean status = in.readBoolean();
        socket.close();
        return status;
    }  
    public boolean receiveMessage(String userName) throws IOException {
        Socket socket = new Socket(destinationAddress, PORT);
        final DataOutputStream out =
                new DataOutputStream(socket.getOutputStream());
        final DataInputStream in = new DataInputStream(
                socket.getInputStream());
        out.writeUTF("MessageFinder");
        out.writeUTF(userName);
        boolean isEmpty = in.readBoolean();
        if(isEmpty) {
            return false;
        }
        Message message = new Message();
        message.userFrom = in.readUTF();
        message.userTo = in.readUTF();
        message.message = in.readUTF();
        openMessage(message);
        return true;
    }
    public void openMessage(Message message) {   
        int flag = 0;
        for(ChatPanel chat : openDialogs) {
            if(chat.getFriendName().equals(message.userFrom)) {
                chat.receiveMessage(message);
                flag++;
            }
        }
        if(flag == 0) {
            mainPanel.openDialog(message.userFrom);
            openDialogs.get(openDialogs.size()-1).receiveMessage(message);
        }
    }
}
