package utils;

import java.io.Serializable;
import java.util.Scanner;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String username;
    protected String password;
    private String email;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public User() {
    	
    	while (true) {
	    	Scanner scanner = new Scanner(System.in);
			System.out.println("Please Login");
			
			System.out.print("Enter username: ");
			this.username = scanner.nextLine();
			
			System.out.print("Enter password: ");
			this.password = scanner.nextLine();
			
			if (this.username.isBlank() || this.password.isBlank()) 
				System.out.println("Credential musn't be empty. Please try again");
			else break;
    	}
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    

}
