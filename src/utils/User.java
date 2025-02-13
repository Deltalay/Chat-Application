package utils;

import java.io.Serializable;
import java.util.Scanner;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private String email;

    private transient Scanner scanner = new Scanner(System.in);
    public User() {

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
