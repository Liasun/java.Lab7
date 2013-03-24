/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bsu.rfe;

/**
 *
 * @author hleb
 */
public class Peer {
    private String name;
    private String password;
    private boolean connectStatus;
    
    public Peer(){
        
    }
    public Peer(String name, String password) {
        this.name = name;
        this.password = password;
        connectStatus = false;
    }
    public String getName() {
        return name;
    }
    public String getPassword() {
        return password;
    }
    public boolean isConnected(){
        return connectStatus;
    }
    public void setConnected(boolean status){
        connectStatus = status;
    }
    
}
