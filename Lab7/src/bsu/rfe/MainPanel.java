/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bsu.rfe;

import bsu.rfe.serverOperations.communicationServer;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.awt.image.ImageObserver.HEIGHT;
import static java.awt.image.ImageObserver.WIDTH;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

/**
 *
 * @author hlebA
 */
public class MainPanel extends JFrame {

    JPanel findPanel;
    JButton findUserButton;
    JTextField findUserTextField;
    JTabbedPane tabbedPane;
    String userName;
    
    ArrayList<ChatPanel> openDialogs = new ArrayList<ChatPanel>();
    communicationServer server;

    public MainPanel() {
    }

    public MainPanel(String userName,String destinationAddress,int PORT) {
        super("Hello, " + userName);
        this.userName = userName;
        server = new communicationServer(userName, openDialogs, this,destinationAddress,PORT);
        server.startServer();
        Toolkit kit = Toolkit.getDefaultToolkit();
        createLayout();
        setSize(800, 600);
        setResizable(false);
        setVisible(true);
        setLocation((kit.getScreenSize().width - WIDTH) / 4, (kit.getScreenSize().height - HEIGHT) / 4);

    }

    private void createLayout() {
        tabbedPane = new JTabbedPane();
        JPanel content = new JPanel();
        createFindPanel();
        content.setLayout(new BorderLayout());
        content.add(tabbedPane, BorderLayout.CENTER);
        content.add(findPanel, BorderLayout.NORTH);
        getContentPane().add(content);
        pack();
    }

    private void createButton() {
        findUserButton = new JButton("talk");
        findUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    findUser();
                } catch (IOException ex) {
                }
            }
        });
    }

    private void createFields() {
        findUserTextField = new JTextField("user name");
        findUserTextField.setMinimumSize(new Dimension(300, 20));
    }

    private void createFindPanel() {
        findPanel = new JPanel();
        createButton();
        createFields();
        createFindPanelLayout();
    }

    private void createFindPanelLayout() {

        GroupLayout layout = new GroupLayout(findPanel);
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                .addGap(100)
                .addComponent(findUserTextField)
                .addGap(15)
                .addComponent(findUserButton)
                .addGap(100));
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                .addGap(30)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(findUserTextField))
                .addComponent(findUserButton)
                .addGap(30));
        findPanel.setLayout(layout);

    }

    private void createNewChat() {
        ChatPanel chatPanel = new ChatPanel(userName, findUserTextField.getText(), this);
        openDialogs.add(chatPanel);
        tabbedPane.addTab(findUserTextField.getText(), chatPanel);
    }

    public void openDialog(String user) {
        ChatPanel chatPanel = new ChatPanel(userName, user, this);
        openDialogs.add(chatPanel);
        tabbedPane.addTab(user, chatPanel);
    }

    private int setFocus() {
        for (int k = 0; k < tabbedPane.getComponentCount(); k++) {
            if (tabbedPane.getTitleAt(k).equals(findUserTextField.getText())) {
                tabbedPane.setSelectedIndex(k);
                return 1;
            }
        }
        return 0;
    }

    private void writeAfkMessage() {
        Message message = new Message();
        message.message = "i am not online";
        message.userFrom = findUserTextField.getText();
        message.userTo = "user";
        server.messageServer.openMessage(message);
    }

    private void findUser() throws IOException {
        int status = server.findUser(findUserTextField.getText());
        if (status > 0) {
            if (setFocus() == 1) {
                return;
            }
            createNewChat();
            setFocus();
            if (status == 1) {
                writeAfkMessage();
            }
        } else {
            findUserTextField.setText("unknown user");
        }
    }
}
