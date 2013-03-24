/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bsu.rfe;

import java.awt.Toolkit;
import static java.awt.image.ImageObserver.HEIGHT;
import static java.awt.image.ImageObserver.WIDTH;
import javax.swing.JFrame;

/**
 *
 * @author hleb
 */
public class ChatFrame extends JFrame{
    public ChatFrame() {
        
    }
    public ChatFrame(String userName,ChatPanel chatPanel) {
        super("Chat with " + userName);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setSize(600, 480);
        setResizable(false);
        setVisible(true);
        getContentPane().add(chatPanel);
        setLocation((kit.getScreenSize().width - WIDTH) / 4, (kit.getScreenSize().height - HEIGHT) / 4);
    }
}
