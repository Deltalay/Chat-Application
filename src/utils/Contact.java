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
    
    public Contact(String contactName, String lastMessage) {
        this.contactName = contactName;
        this.lastMessage = lastMessage;
    }
    
    public String getContact() {
        return contactName;
    }
    
    public String getMessage() {
        return lastMessage;
    }


}
