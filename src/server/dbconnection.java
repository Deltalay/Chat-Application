package server;

import java.sql.*;

public class dbconnection {
	
    String url="jdbc:mysql://localhost:3306/";
    String dbusername = "root";
    String dbpassword = ".";
    
    private String username;
    private String password;
    private String email;
    
    boolean run(String InputName,String InputPassword) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, dbusername, dbpassword);
            Statement stmt = conn.createStatement();
            
            String checkDbQuery = "SHOW DATABASES LIKE 'chat'";
            ResultSet rs = stmt.executeQuery(checkDbQuery);
            
            if (!rs.next()) {
            	
            	String createDbQuery = "CREATE DATABASE chat";
            	stmt.executeUpdate(createDbQuery);
            	System.out.println("Database 'chat' created.");
            }
            
            conn.close();
          
            conn = DriverManager.getConnection(url + "chat", dbusername, dbpassword);
            stmt = conn.createStatement();
            
            String checkTableQuery = "SHOW TABLES LIKE 'users'";
            rs = stmt.executeQuery(checkTableQuery);
            
            if (!rs.next()) {
            	
            	String createTableQuery = "CREATE TABLE users (" 
            							+ "username VARCHAR(255) PRIMARY KEY, " 
            							+ "email VARCHAR(255) NOT NULL, "
            							+ "password VARCHAR(255) NOT NULL)";
            	stmt.execute(createTableQuery);
            	System.out.println("Created 'users' table.");
            }
            
            
            String query = "SELECT * FROM users";
            rs = stmt.executeQuery(query);
            
            while (rs.next()) {
            	
                username = rs.getString("username");
                password = rs.getString("password");  
                
                if (username.equals(InputName) && password.equals(InputPassword)) {
                    conn.close(); // Close connection before returning
                    return true;
                }
            }
            conn.close();
            
        }catch(Exception e) {
        	
            e.printStackTrace();
        }
        
        return false;
    }
    
    public void setUser(String user) {
    	
        this.username = user;
    }
    
    public void setPassword(String password) {
    	
        this.password = password;
    }
    
    public String getUser() {
    	
        return username;
    }
    
    public String getPassword() {
    	
        return password;
    }
    
    public void setEmail(String email) {
    	
        this.email = email;
    }
    
    public String getEmail() {
    	
        return email;
    }
    
}
