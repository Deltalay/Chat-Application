package utils;

import java.io.Serializable;
import java.util.Scanner;

public class NewUser extends User {
    private String dob;
    private String email;
    private static final long serialVersionUID = 1L;
    private static final int MIN_PASSWORD_LENGTH = 8;
    
	public NewUser() {
        super("", ""); 
		Scanner scanner = new Scanner(System.in);
		System.out.println("Please Fill the information");
		
		while (true) {
			
			System.out.print("Enter username: ");
			this.username = scanner.nextLine();
			
            System.out.print("Enter password (min 8 characters): ");
            this.password = scanner.nextLine();

            try {
                validate(username, password);
                break; // Exit loop if password is valid
            } catch (InvalidCredentialException e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Please try again.");
            }
        }
		
		System.out.print("Enter email: ");
		this.email = scanner.nextLine();
		
		System.out.print("Enter date of birth (YYYY-MM-DD): ");
		this.dob = scanner.nextLine();

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
    
    public static void validate(String username, String password) throws InvalidCredentialException {
    	
        if (password.isBlank() || username.isBlank()) {
        	throw new InvalidCredentialException("User credential musn't be empty");
        }
        
        if (password.length() < MIN_PASSWORD_LENGTH) {
            throw new InvalidCredentialException("Password must be at least " + MIN_PASSWORD_LENGTH + " characters long.");
        }
      
    }
	

}
