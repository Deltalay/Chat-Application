package server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import utils.Message;


public class DbConnection {

    private String url = "jdbc:mysql://localhost:3306/";
    private String dbusername = "root";
    private String dbPassword = getDbPassword();

    private String username;
    private String password;
    private String dob;
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
  
    
    public DbConnection() {
        initializeDatabase();
    } 
    
    public void initializeDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, dbusername, dbPassword);
            Statement stmt = conn.createStatement();
    
            // Check if the chat database exists
            String checkDbQuery = "SHOW DATABASES LIKE 'chat'";
            ResultSet rs = stmt.executeQuery(checkDbQuery);
            
            if (!rs.next()) {
                String createDbQuery = "CREATE DATABASE chat";
                stmt.executeUpdate(createDbQuery);
                System.out.println("Database 'chat' created.");
            }
    
            conn.close();
            
            // Connect to the chat database
            conn = DriverManager.getConnection(url + "chat", dbusername, dbPassword);
            stmt = conn.createStatement();
    
            // Check if the users table exists
            String checkTableQuery = "SHOW TABLES LIKE 'users'";
            rs = stmt.executeQuery(checkTableQuery);
    
            if (!rs.next()) {
                String createTableQuery = "CREATE TABLE users (" 
                                        + "username VARCHAR(255) PRIMARY KEY, " 
                                        + "email VARCHAR(255) NOT NULL, "
                                        + "dob DATE NOT NULL, "
                                        + "password VARCHAR(255) NOT NULL)";
                stmt.execute(createTableQuery);
                System.out.println("Created 'users' table.");
            }
            
            checkTableQuery = "SHOW TABLES LIKE 'messages'";
            rs = stmt.executeQuery(checkTableQuery);
            
            if (!rs.next()) {
            	
            	String createTableQuery = "CREATE TABLE messages (" 
										+ "id INT AUTO_INCREMENT PRIMARY KEY, " 
										+ "username VARCHAR(63) NOT NULL, "
										+ "receiver VARCHAR(63) NOT NULL, "
										+ "message_text TEXT) ";
				
            	stmt.execute(createTableQuery);
				System.out.println("Created 'messages' table.");
            }
    
            conn.close();
        } catch (Exception e) {
        	
            e.printStackTrace();
        }
    }   
    
    public boolean check_login(String InputName,String InputPassword) {
    	
        try {
        	
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            Connection conn = DriverManager.getConnection(url, dbusername, dbPassword);
            Statement stmt = conn.createStatement();
            ResultSet rs;
          
            conn = DriverManager.getConnection(url + "chat", dbusername, dbPassword);
            stmt = conn.createStatement();
            
            
            
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, InputName);
            pstmt.setString(2, InputPassword);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
            	
                conn.close();
                return true;
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
            
            ResultSet rs ;

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
            Connection conn = DriverManager.getConnection(url + "chat", dbusername, dbPassword);
            Statement stmt = conn.createStatement();
            
            String query = "SELECT username, email FROM users WHERE username = ? OR email = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, email);
        
            ResultSet rs = pstmt.executeQuery();
        
            if (rs.next()) {
                conn.close();
                return true;
            }

            
                conn.close();
            
        } catch(Exception e) {
        	
            e.printStackTrace();
            return true;
        }
        return false;
    }
    public void save_user(String dob,String email, String username, String password){
        
    	try {
    		
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/chat";
            Connection conn = DriverManager.getConnection(url, dbusername, dbPassword);
            Statement stmt = conn.createStatement();
            
            String query = "INSERT INTO users (username, password, dob, email) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
        
        // Set the values for the prepared statement
            pstmt.setString(1, username);
            pstmt.setString(2, password);
        
        // Check if dob is in the correct date format (YYYY-MM-DD)
            try {
                java.sql.Date sqlDate = java.sql.Date.valueOf(dob);  // Converts string to SQL Date
                pstmt.setDate(3, sqlDate);  // Set dob as Date
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid date format. Expected YYYY-MM-DD");
                return;  // Exit if the date format is invalid
            }
        
            pstmt.setString(4, email);
        
        // Execute the update
            pstmt.executeUpdate();
            
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
