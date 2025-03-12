package utils;

import java.io.Serializable;

public class LoginSuccessResponse implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User user;
	public String message;
	
	public LoginSuccessResponse(User user, String message) {
		this.user = user;
		this.message = message;
	}
	
	public User getUser() {
		return this.user;
	}
	
	
}
