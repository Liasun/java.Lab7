/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bsu.rfe;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import bsu.rfe.serverOperations.communicationServer;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author hlebA
 */
public class AuthenticationPanel extends JFrame {

    JLabel currentStatus;
    JTextField loginField;
    JTextField passwordField;
    JButton loginButton;
    String destinationAddress;
    int PORT;
    private JFileChooser fileChooser = new JFileChooser();
    communicationServer server;

    public AuthenticationPanel() {
        super("Authentication");
        setSize(300, 200);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - WIDTH) / 4, (kit.getScreenSize().height - HEIGHT) / 4);
        setResizable(false);
        creatFields();
        creatLoginButton();
        creatLayout();
        File selectedFile = new File("/home/hleb/Documents/lab/Lab7/config.txt");
        readFile(selectedFile);
        setServer();


    }

    private void creatLayout() {
        GroupLayout layout = new GroupLayout(getContentPane());
        setLayout(layout);
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                .addGap(75)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(loginField)
                .addComponent(passwordField)
                .addComponent(loginButton)
                .addGroup(layout.createSequentialGroup()
                .addComponent(currentStatus))));
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                .addGap(55)
                .addComponent(loginField)
                .addGap(5)
                .addComponent(passwordField)
                .addGap(10)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(loginButton))
                .addGap(15)
                .addComponent(currentStatus));
    }

    private void creatMainPanel(String userName) {
        MainPanel mainPanel = new MainPanel(userName, destinationAddress, PORT);
        mainPanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void creatLoginButton() {
        loginButton = new JButton("Enter Chat");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                int status = 1;
                String userName = loginField.getText();
                String userPassword = passwordField.getText();
                status = server.logIn(userName, userPassword);
                if (status == 0) {
                    creatMainPanel(userName);
                    dispose();
                }
                if (status == 1) {
                    JOptionPane.showMessageDialog(AuthenticationPanel.this, "connection error", "connection failed", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                currentStatus = new JLabel("something wrong......");
                creatLayout();
            }
        });
    }

    private void creatFields() {
        currentStatus = new JLabel(" ");
        loginField = new JTextField("user name ");
        loginField.setMaximumSize(new Dimension(150, 20));
        passwordField = new JTextField("password ");
        passwordField.setMaximumSize(new Dimension(150, 20));
    }

    private void setServer() {
        server = new communicationServer(destinationAddress, PORT);
    }

    protected void readFile(File selectedFile) {
        try {
            DataInputStream in = new DataInputStream(new FileInputStream(selectedFile));
            destinationAddress = in.readUTF();
            PORT = in.readInt();
            in.close();
        } catch (IOException ex) {
            destinationAddress = "127.0.0.1";
            PORT = 4567;
        }
    }
}
