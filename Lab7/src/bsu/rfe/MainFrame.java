/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bsu.rfe;


import javax.swing.JFrame;
import bsu.rfe.authentication.*;

/**
 *
 * @author hlebA
 */
public class MainFrame {
 
    public MainFrame() {
    }
    public static void main(String[] args) {
        AuthenticationPanel authenticationPanel =  new AuthenticationPanel();
        authenticationPanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        authenticationPanel.setVisible(true);
        
    }
}
 