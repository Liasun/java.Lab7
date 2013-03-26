/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bsu.rfe;

import bsu.rfe.serverOperations.communicationServer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author hlebA
 */
public class ChatPanel extends JPanel {

    private JTextArea textAreaIncoming;
    private JTextArea textAreaOutgoing;
    private static final int INCOMING_AREA_DEFAULT_ROWS = 10;
    private static final int OUTGOING_AREA_DEFAULT_ROWS = 5;
    private JScrollPane scrollPaneIncoming;
    private JScrollPane scrollPaneOutgoing;
    private JPanel messagePanel;
    private JButton sendButton;
    private String userName;
    private String friendName;
    private communicationServer server;

    public ChatPanel(String userName, String friendName, MainPanel mainPanel) {
        this.userName = userName;
        this.friendName = friendName;
        createTextAreas();
        createMessagePanel();
        createGlobalLayout();
        this.server = mainPanel.server;
    }

    private void createTextAreas() {
        textAreaIncoming = new JTextArea(INCOMING_AREA_DEFAULT_ROWS, 0);
        textAreaIncoming.setEditable(false);
        scrollPaneIncoming = new JScrollPane(textAreaIncoming);

        textAreaOutgoing = new JTextArea(OUTGOING_AREA_DEFAULT_ROWS, 0);
        scrollPaneOutgoing = new JScrollPane(textAreaOutgoing);
    }

    private void createMessagePanel() {
        messagePanel = new JPanel();
        messagePanel.setBorder(
                BorderFactory.createTitledBorder("Сообщение"));
        createSendButton();
        createMessagePanelLayout();

    }

    private void createSendButton() {
        sendButton = new JButton("Отправить");
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    sendMessage();
                } catch (IOException ex) {
                }
            }
        });
    }

    private void createMessagePanelLayout() {
        GroupLayout layout2 = new GroupLayout(messagePanel);
        messagePanel.setLayout(layout2);
        layout2.setHorizontalGroup(layout2.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout2.createParallelGroup(Alignment.TRAILING)
                .addGroup(layout2.createSequentialGroup()
                .addComponent(scrollPaneOutgoing)
                .addComponent(sendButton)))
                .addContainerGap());
        layout2.setVerticalGroup(layout2.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollPaneOutgoing)
                .addGap(10)
                .addComponent(sendButton)
                .addContainerGap());
    }

    private void createGlobalLayout() {
        GroupLayout layout1 = new GroupLayout(this);
        setLayout(layout1);
        layout1.setHorizontalGroup(layout1.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout1.createParallelGroup()
                .addComponent(scrollPaneIncoming)
                .addComponent(messagePanel))
                .addContainerGap());
        layout1.setVerticalGroup(layout1.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollPaneIncoming)
                .addGap(10)
                .addComponent(messagePanel)
                .addContainerGap());
    }

    private void sendMessage() throws IOException {
        Message message = prepareMessage();

        if (server.sendMessage(message) == false) {
            JOptionPane.showMessageDialog(ChatPanel.this, "connection error", "message didnt send", JOptionPane.WARNING_MESSAGE);
            return;
        }
        textAreaIncoming.append(" (" + userName + "): "
                + message.message + "\n");

    }

    private Message prepareMessage() {
        Message message = new Message();
        message.userTo = friendName;
        message.userFrom = userName;
        message.message = textAreaOutgoing.getText();
        if (message.message == null) {
            message.message = " ";
        }
        textAreaOutgoing.setText("");
        return message;

    }

    public void receiveMessage(Message message) {
        textAreaIncoming.append(" (" + message.userFrom + "): "
                + message.message + "\n");
    }

    public String getFriendName() {
        return friendName;
    }
}
