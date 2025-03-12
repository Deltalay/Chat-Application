package utils;

import java.io.Serializable;

public class ChatSessionRequest implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	private String sender;
	private String receiver;
	private int receiverId;
	private User user;
//	private int numberOfMessages;
	
	public ChatSessionRequest(User user, String receiver) {
		this.user = user;
		this.receiver = receiver;
		
	}
	public ChatSessionRequest(User user) {
		this.user = user;
		this.receiver = "";
		
	}
	
	public User getUser() {
		
		return this.user;
	}
	
	public String getReceiver() {
		
		return receiver;
	}
	
	public int getReceiverId() {
	
		return receiverId;
	}
	
	public void setReceiverId(int receiverId) {
	
		this.receiverId = receiverId;
	}
	
	
}
