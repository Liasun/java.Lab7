/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bsu.rfe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.Timer;

/**
 *
 * @author hlebA
 */
public class Server {

    ArrayList<Peer> userBD = new ArrayList<Peer>();
    ArrayList<Peer> onlineUsers = new ArrayList<Peer>();
    ArrayList<Message> waitingMessages = new ArrayList<Message>();
    private static final int SERVER_PORT = 4567;
    private static final int DELAY = 30000;
    private Timer onlineTimer = new Timer(DELAY, new ActionListener() {
        public void actionPerformed(ActionEvent event) {
            onlineCheck();
        }
    });

    public Server() {
        createPeers();
        onlineTimer.start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverStart();
                } catch (IOException ex) {
                }
            }
        }).start();
    }

    private void userAuthentication(DataInputStream in,
            DataOutputStream out) throws IOException {
        final String senderName = in.readUTF();
        final String senderPassword = in.readUTF();
        for (Peer peer : userBD) {
            if (peer.getName().equals(senderName)) {
                if (peer.getPassword().equals(senderPassword)) {
                    out.writeBoolean(true);
                    userEntry(peer);
                    return;
                }
                out.writeBoolean(false);
                System.out.println(senderName + " ->  connected -> authentication -> FAIL");
                return;
            }
        }
        out.writeBoolean(false);
        System.out.println(senderName + " ->  connected -> authentication -> FAIL ");
    }

    private void serverStart() throws IOException {
        final ServerSocket serverSocket =
                new ServerSocket(SERVER_PORT);
        while (!Thread.interrupted()) {
            final Socket socket = serverSocket.accept();
            socketProcessing((socket));
        }

    }

    private void socketProcessing(Socket socket) throws IOException {
        final DataInputStream in = new DataInputStream(
                socket.getInputStream());
        final DataOutputStream out =
                new DataOutputStream(socket.getOutputStream());
        final String connectType = in.readUTF();
        choseTask(connectType, in, out);
        socket.close();

    }

    private void choseTask(String connectType, DataInputStream in, DataOutputStream out)
            throws IOException {
        if (connectType.equals("Authentication")) {
            userAuthentication(in, out);
            return;
        }
        if (connectType.equals("Message")) {
            receiveMessage(in, out);
        }
        if (connectType.equals("MessageFinder")) {
            checkForMessage(in, out);
        }
        if (connectType.equals("FindUser")) {
            findUser(in, out);
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
    }

    private void createPeers() {
        userBD.add(new Peer("hleb", "081093"));
        userBD.add(new Peer("masha", "081092"));
        userBD.add(new Peer("admin", "admin"));
    }

    private void userEntry(Peer user) {
        user.setConnected(true);
        onlineUsers.add(user);
        System.out.println(user.getName() + " ->  connected -> authentication -> OK ");
    }

    private void onlineCheck() {
        System.out.println("server " + " ->  start online checking");
        for (Iterator<Peer> peerIterator = onlineUsers.iterator(); peerIterator.hasNext();) {
            Peer peer = peerIterator.next();
            if (peer.isConnected() == false) {
                System.out.println(peer.getName() + " ->  disconnected");
                peerIterator.remove();
                continue;
            }
            peer.setConnected(false);

        }
        System.out.println("server " + " ->  online checking DONE");
    }

    private void receiveMessage(DataInputStream in,
            DataOutputStream out) throws IOException {
        Message message = new Message();
        message.userFrom = in.readUTF();
        message.userTo = in.readUTF();
        message.message = in.readUTF();
        out.writeBoolean(true);
        System.out.println(message.userFrom + " ->  send message to " + message.userTo);
        waitingMessages.add(message);
    }

    private void checkForMessage(DataInputStream in,
            DataOutputStream out) throws IOException {
        String userName = in.readUTF();
        setOnline(userName);
        for (Iterator<Message> messageIterator = waitingMessages.iterator(); messageIterator.hasNext();) {
            Message message = messageIterator.next();
            if (userName.equals(message.userTo)) {
                out.writeBoolean(false);
                sendMessage(message, out);
                messageIterator.remove();
                return;
            }
        }
        out.writeBoolean(true);
    }

    private void sendMessage(Message message, DataOutputStream out) throws IOException {
        out.writeUTF(message.userFrom);
        out.writeUTF(message.userTo);
        out.writeUTF(message.message);
        System.out.println("server " + " ->  send message ->  " + message.userTo);
    }

    private void setOnline(String userName) {
        for (Peer peer : onlineUsers) {
            if (peer.getName().equals(userName)) {
                peer.setConnected(true);
                return;
            }
        }
    }

    private void findUser(DataInputStream in,
            DataOutputStream out) throws IOException {
        String userName = in.readUTF();
        for (Peer peer : userBD) {
            if (peer.getName().equals(userName)) {
                out.writeBoolean(true);
                for (Peer peer1 : onlineUsers) {
                    if (peer1.getName().equals(userName)) {
                        out.writeBoolean(true);
                        return;
                    }
                }
                break;
            }
        }

        out.writeBoolean(false);
    }
}
