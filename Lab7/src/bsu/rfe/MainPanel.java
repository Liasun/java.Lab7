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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
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
    JRadioButton openTab;
    JRadioButton openWindow;
    ButtonGroup buttonGroup;
    JTabbedPane tabbedPane;
    String userName;
    
    ArrayList<ChatPanel> openDialogs = new ArrayList<ChatPanel>();
    communicationServer server;
    

    public MainPanel() {
    }

    public MainPanel(String userName) {
        super("Hello, " + userName);
        this.userName = userName;
        server = new communicationServer(userName,openDialogs, this);
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
                    int status = server.findUser(findUserTextField.getText());
                    if (status > 0) {
                        createNewChat();
                        if (status == 1) {
                            Message message = new Message();
                            message.message = "i am not online";
                            message.userFrom = findUserTextField.getText();
                            message.userTo = "user";
                            server.messageServer.openMessage(message);
                        }
                    } else {
                        findUserTextField.setText("unknown user");
                    }
                } catch (IOException ex) {
                    System.out.println("1");
                }
            }
        });
    }

    private void createFields() {
        findUserTextField = new JTextField("user name");
        findUserTextField.setMaximumSize(new Dimension(150, 20));
    }

    private void createRadioButtons() {
        openTab = new JRadioButton("tab");
        openWindow = new JRadioButton("window");
        openTab.setSelected(true);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(openTab);
        buttonGroup.add(openWindow);

    }

    private void createFindPanel() {
        findPanel = new JPanel();
        createButton();
        createFields();
        createRadioButtons();
        createFindPanelLayout();


    }

    private void createFindPanelLayout() {
        Box radioButtonBox = Box.createHorizontalBox();
        radioButtonBox.add(openTab);
        radioButtonBox.add(openWindow);
        radioButtonBox.add(Box.createHorizontalGlue());

        Box verticalBox = Box.createVerticalBox();
        verticalBox.add(Box.createVerticalGlue());
        verticalBox.add(findUserTextField);
        verticalBox.add(Box.createVerticalStrut(10));
        verticalBox.add(radioButtonBox);
        verticalBox.add(Box.createVerticalGlue());

        Box mainBox = Box.createHorizontalBox();
        verticalBox.add(Box.createVerticalGlue());
        mainBox.add(verticalBox);
        mainBox.add(findUserButton);
        verticalBox.add(Box.createVerticalGlue());

        findPanel.add(mainBox, BorderLayout.CENTER);

    }

    private void createNewChat() {
        ChatPanel chatPanel = new ChatPanel(userName,findUserTextField.getText(),this);
        openDialogs.add(chatPanel);
        if (openTab.isSelected()) {
            tabbedPane.addTab(findUserTextField.getText(), chatPanel);
        } else {
            ChatFrame chatFrame = new ChatFrame(findUserTextField.getText(), chatPanel);
        }
    } 
    
    public void openDialog(String user) {
        ChatPanel chatPanel = new ChatPanel(userName,user,this);
        openDialogs.add(chatPanel);
        tabbedPane.addTab(user, chatPanel);
    }
    
}
 