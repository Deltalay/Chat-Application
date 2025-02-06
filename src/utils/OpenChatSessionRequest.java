package utils;

import java.io.Serializable;

public class OpenChatSessionRequest implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String sender;
	private String receiver;
//	private int numberOfMessages;
	
	public OpenChatSessionRequest(String sender, String receiver) {
		this.sender = sender;
		this.receiver = receiver;
		
	}
	
	public String getSender() {
		
		return sender;
	}
	
	public String getReceiver() {
		
		return receiver;
	}
	
	
}
