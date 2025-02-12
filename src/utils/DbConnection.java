package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DbConnection {

    String url = "jdbc:mysql://localhost:3306/";
    String dbusername = "root";
    String dbPassword = "";
    
    private String username;
    private String password;
    private String email;
    
    private String getDbPassword() {
//    	System.out.println(System.getProperty("user.dir"));

    	String readPassword = "";
    	
    	try (BufferedReader br = new BufferedReader(new FileReader("info.txt"))) {
    		
            readPassword = br.readLine();
            
        } catch (IOException e) {
        	
            e.printStackTrace();  
        }
    	
    	return readPassword;
    }
  
    
    public boolean check_login(String InputName,String InputPassword) {
    	
        try {
        	
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, dbusername, dbPassword);
            Statement stmt = conn.createStatement();
            
            String checkDbQuery = "SHOW DATABASES LIKE 'chat'";
            ResultSet rs = stmt.executeQuery(checkDbQuery);
            
            if (!rs.next()) {
            	
            	String createDbQuery = "CREATE DATABASE chat";
            	stmt.executeUpdate(createDbQuery);
            	System.out.println("Database 'chat' created.");
            }
            
            conn.close();
          
            conn = DriverManager.getConnection(url + "chat", dbusername, dbPassword);
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
            
        } catch(Exception e) {
        	
            e.printStackTrace();
        }
        
        return false;
    }
    
    public List<Message> private_chat(String sender, String receiver) {

        List <Message> messageList = new ArrayList<>();
        Message message;
        
        try {
        	
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url + "chat", dbusername, dbPassword);
            Statement stmt = conn.createStatement();
            
            String checkTableQuery = "SHOW TABLES LIKE 'messages'";
            ResultSet rs = stmt.executeQuery(checkTableQuery);
            
            if (!rs.next()) {
            	
            	String createTableQuery = "CREATE TABLE messages (" 
										+ "id INT AUTO_INCREMENT PRIMARY KEY, " 
										+ "username VARCHAR(63) NOT NULL, "
										+ "receiver VARCHAR(63) NOT NULL, "
										+ "message_text TEXT) ";
				
            	stmt.execute(createTableQuery);
				System.out.println("Created 'messages' table.");
            }

            String query = "SELECT username, receiver, message_text FROM messages";
            rs = stmt.executeQuery(query);
            
            while (rs.next()) {
            	
                String s = rs.getString("username");
                String r = rs.getString("receiver");
                String m = rs.getString("message_text");  
                
                message = new Message(s, r, m);
                
                if ((sender.equals(s) && receiver.equals(r)) || (sender.equals(r) && receiver.equals(s))) 
                    messageList.add(message);
                
            }
//            for (int i = 0; i < messageList.size(); i++) {
//            	
//                MHistory += messageList.get(i);
//                if (i < messageList.size() - 1) MHistory += "\n";
//                
//            }
            
            conn.close();
            
        } catch(Exception e) {
        	
            e.printStackTrace();
        }
        
        return messageList;
    }
    
    public void save_message(String sender, String receiver, String message){
        
    	try {
    		
            Class.forName("com.mysql.cj.jdbc.Driver");
//            url = "jdbc:mysql://localhost:3306/chat";
            Connection conn = DriverManager.getConnection(url + "chat", dbusername, dbPassword);
            Statement stmt = conn.createStatement();
            
            String query = "INSERT INTO messages (username, receiver, message_text) VALUES ('" + sender + "', '" + receiver + "', '" + message + "');";
            stmt.executeUpdate(query);
            
            conn.close();
            
        } catch(Exception e) {
        	
            e.printStackTrace();
        }
    }
    public boolean check_user(String email, String username){
        
    	try {
    		
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url + "users", dbusername, dbPassword);
            Statement stmt = conn.createStatement();
            
            String query = "SELECT username, emailFROM users";
            ResultSet rs = stmt.executeQuery(query);
            rs = stmt.executeQuery(query);
            
            while (rs.next()) {
            	
                String s = rs.getString("username");
                String m = rs.getString("email"); 
                
                if (!username.equals(s) || !email.equals(m)) {
                    conn.close(); // Close connection before returning
                    return false;
                }
            }
            
            conn.close();
            
        } catch(Exception e) {
        	
            e.printStackTrace();
        }
        return true;
    }
    public void save_user(String email, String username, String password){
        
    	try {
    		
            Class.forName("com.mysql.cj.jdbc.Driver");
//            url = "jdbc:mysql://localhost:3306/chat";
            Connection conn = DriverManager.getConnection(url + "users", dbusername, dbPassword);
            Statement stmt = conn.createStatement();
            
            String query = "INSERT INTO messages (username, password, email) VALUES ('" + username + "', '" + password + "', '" + email + "');";
            stmt.executeUpdate(query);
            
            conn.close();
            
        } catch(Exception e) {
        	
            e.printStackTrace();
        }
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
