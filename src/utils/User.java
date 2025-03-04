package utils;

import java.util.Scanner;

public class User  extends NewUser  {
    private static final long serialVersionUID = 1L;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public User() {
        super("", "","",""); 
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
    }
    public static void validate(String username, String password) throws InvalidCredentialException {
    	
        if (password.isBlank() || username.isBlank()) {
        	throw new InvalidCredentialException("User credential musn't be empty");
        }
      
    }
    public boolean isNewUser() {
        return false;  // Returning false for login users
    }

}
