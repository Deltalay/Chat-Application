package utils;

import java.util.Scanner;

public class User extends NewUser  {
    private static final long serialVersionUID = 1L;
//    String username;
//    String password;
    
    public User(String username, String password) {
    	super();
        this.username = username;
        this.password = password;
    }
    
    public static User createUserObject(Scanner scanner) {
    	
    	System.out.println("Please Fill the information for login"); 
    	String username;
    	String password;
    	
    	while (true) {
    		
    		System.out.print("Enter username: ");
    		username = scanner.nextLine();
    		
    		System.out.print("Enter password (min 8 characters): ");
    		password = scanner.nextLine();
    		
    		try {
    			System.out.println(username + " " + password);
    			validate(username, password);
    			return new User(username, password);
    			
    		} catch (InvalidCredentialException e) {
    			
    			System.out.println("Error: " + e.getMessage());
                System.out.println("Please try again.");
    		}
    	}

    }
    
    public static void validate(String username, String password) throws InvalidCredentialException {
    	
        if (password.isBlank() || username.isBlank()) {
        	throw new InvalidCredentialException("User credential musn't be empty");
        }
      
    }
    
//    public boolean isNewUser() {
//        return false;  // Returning false for login users
//    }

}
