/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bsu.rfe;

import bsu.rfe.MainPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import bsu.rfe.serverOperations.communicationServer;

/**
 *
 * @author hlebA
 */
public class AuthenticationPanel extends JFrame {

    JLabel currentStatus;
    JTextField loginField;
    JTextField passwordField;
    JButton loginButton;
    communicationServer server = new communicationServer();

    public AuthenticationPanel() {
        super("Authentication");
        setSize(300, 200);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - WIDTH) / 4, (kit.getScreenSize().height - HEIGHT) / 4);
        setResizable(false);
        creatFields();
        creatLoginButton();
        creatLayout();

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
                .addComponent(currentStatus)
                );
    }

    private void creatMainPanel(String userName) {
        MainPanel mainPanel = new MainPanel(userName);
        mainPanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void creatLoginButton() {
        loginButton = new JButton("Enter Chat");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                boolean status = false;
                String userName = loginField.getText();
                String userPassword = passwordField.getText();
                    status = server.logIn(userName, userPassword);
                if (status) {
                    creatMainPanel(userName);
                    dispose();
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
}
