package utils;

import java.io.Serializable;
import java.util.List;

public class ChatHistory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Message> messages;

	public ChatHistory(List<Message> messages) {
		
		this.messages = messages;
	}
	
	public List<Message> getMessages() {
		
        return messages;
    }
	
}
