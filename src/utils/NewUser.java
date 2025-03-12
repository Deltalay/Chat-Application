package utils;

import java.io.Serializable;
import java.util.Scanner;

public class NewUser implements Serializable {
    protected String username;
    protected String password;
    private int userId;
    private String dob;
    private String email;
    private static final long serialVersionUID = 1L;
    private static final int MIN_PASSWORD_LENGTH = 8;
    
	public NewUser() {
		
    }
	
    public NewUser(String username, String password, String dob, String email){
        this.username = username;
        this.password = password;
        this.dob = dob;
        this.email = email;

    }
    
    public static NewUser createNewUserObject(Scanner scanner) {
    	
    	System.out.println("Please Fill the information for register");
		
    	String username;
    	String password;
    	String email;
    	String dob;
    	
		while (true) {
			
			System.out.print("Enter username: ");
			username = scanner.nextLine();
			
            System.out.print("Enter password (min 8 characters): ");
            password = scanner.nextLine();

            System.out.print("Enter email: ");
		    email = scanner.nextLine();
		
		    System.out.print("Enter date of birth (YYYY-MM-DD): ");
		    dob = scanner.nextLine();

            try {
                validate(username, password,dob,email);
                return new NewUser(username, password, dob, email);
                		
            } catch (InvalidCredentialException e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Please try again.");
            }
        }
    }
    
    public String getDob() {
        return dob;
    }
    
    public void setDob(String dob) {
        this.dob = dob;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }
    
    public static void validate(String username, String password,String dob,String email) throws InvalidCredentialException {
    	
        if (password.isBlank() || username.isBlank() || dob.isBlank() || email.isBlank()) {
        	throw new InvalidCredentialException("User credential musn't be empty");
        }
        
        else if (password.length() < MIN_PASSWORD_LENGTH) {
            throw new InvalidCredentialException("Password must be at least " + MIN_PASSWORD_LENGTH + " characters long.");
        }
      
    }
//    public boolean isNewUser() {
//        return true;  // Always return true for new users
//    }

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}
