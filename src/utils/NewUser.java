package utils;

import java.io.Serializable;
import java.util.Scanner;

public class NewUser extends User{
    private String dob;
    private String email;
    private static final long serialVersionUID = 1L;
    
	public NewUser() {
		super();
		Scanner scanner = new Scanner(System.in);
		
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
	

}
