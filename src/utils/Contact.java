package utils;

import java.io.Serializable;


public class Contact implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    private String contactName;
    private int sender_id; // Dak joal lg sin
    private String lastMessage;
    private boolean isSender;
    
    public Contact(String contactName, String lastMessage, boolean isSender) {
        this.contactName = contactName;
        this.lastMessage = lastMessage;
        this.isSender = isSender;
    }
    
    public Contact(String contactName, String lastMessage) {
        this.contactName = contactName;
        this.lastMessage = lastMessage;
    }
    
    public String getContact() {
    
    	return contactName;
    }
    
    public void setContact(String contactName) {
    	
    	this.contactName = contactName;
    }
    
    public String getMessage() {
        
    	return lastMessage;
    }
    
    public void setLastMessage(String lastMessage) {
    	
    	this.lastMessage = lastMessage;
    }
    
    public boolean isSender() {
        
    	return isSender;
    }


}
