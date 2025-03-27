package server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import utils.Message;
import utils.User;
import utils.Contact;


public class DbConnection {

    private String url = "jdbc:mysql://localhost:3306/";
    private String dbusername = "root";
    private String dbPassword = getDbPassword();
    ClientHandler cHandler;

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
    
    public DbConnection(ClientHandler cHandler) {
    	this.cHandler = cHandler;
    	initializeDatabase();
    }
    
    public void initializeDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); //
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
            
            conn = DriverManager.getConnection(url + "chat", dbusername, dbPassword);
            stmt = conn.createStatement();
    
            String checkTableQuery = "SHOW TABLES LIKE 'users'";
            rs = stmt.executeQuery(checkTableQuery);
    
            if (!rs.next()) {
                String createTableQuery = "CREATE TABLE users ("
                						+ "id INT AUTO_INCREMENT PRIMARY KEY, " 
                                        + "username VARCHAR(255) UNIQUE, " 
                                        + "email VARCHAR(255) NOT NULL UNIQUE, "
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
										+ "sender_id INT NOT NULL, "
										+ "receiver_id INT NOT NULL, "
										+ "message_text TEXT, "
										+ "sent_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
										+ "FOREIGN KEY (sender_id) REFERENCES users(id),"
										+ "FOREIGN KEY (receiver_id) REFERENCES users(id))";
				
            	stmt.execute(createTableQuery);
				System.out.println("Created 'messages' table.");
            }
    
            conn.close();
        } catch (ClassNotFoundException | SQLException e ) {
        	
            
        } finally {
        	
        }
    }   
    
    public void check_login(String InputName,String InputPassword) {
    	
        try {
        	
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            Connection conn = DriverManager.getConnection(url, dbusername, dbPassword);
            Statement stmt = conn.createStatement();
            ResultSet rs;
          
            conn = DriverManager.getConnection(url + "chat", dbusername, dbPassword);
//            stmt = conn.createStatement();
            
            
            
            String query = "SELECT id,email,dob FROM users WHERE username = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, InputName);
            pstmt.setString(2, InputPassword);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
            	
                int userId = rs.getInt("id");
                email = rs.getString("email");
                dob = rs.getString("dob");
//                ClientHandler.user.
//                System.out.println(user_id);
                cHandler.isAuthenticated = true;
                cHandler.user.setUserId(userId);
                cHandler.user.setEmail(email);
                cHandler.user.setDob(dob);
                conn.close();
                
            }
            
            conn.close();
            
        } catch(ClassNotFoundException | SQLException e) {
        	
            e.printStackTrace();
        }
        
//        return n;
    }
    
    public List<Message> private_chat(int sender, int receiver) {

        List <Message> messageList = new ArrayList<>();
        Message message;
        
        try {
        	
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url + "chat", dbusername, dbPassword);
            Statement stmt = conn.createStatement();
            
            ResultSet rs ;

            String query = "SELECT s.username as 'Sender', r.username as 'Receiver', m.message_text as 'Message', m.sent_time "
            			 + "FROM messages m "
            			 + "JOIN users s ON m.sender_id = s.id "
            			 + "JOIN users r ON m.receiver_id = r.id "
            			 + "WHERE (sender_id = " + sender + " "
            			 + "AND receiver_id = " + receiver + ") "
            			 + "OR (sender_id = " + receiver + " "
            			 + "AND receiver_id = " + sender + ") ORDER BY m.id";
            
            rs = stmt.executeQuery(query);
            
            while (rs.next()) {
            	
            	String s = rs.getString("Sender");
            	String r = rs.getString("Receiver");
                String m = rs.getString("Message");
                Timestamp timestamp = rs.getTimestamp("sent_time");
                
                message = new Message(s, r, m, timestamp);
                messageList.add(message);
                
            }
            
            conn.close();
            
        } catch(SQLException | ClassNotFoundException e) {
        	
            e.printStackTrace();
        }
        
        return messageList;
    }

    public List<Contact> getAllContact(int sender){
        List <Contact> contactList = new ArrayList<>();
        
        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url + "chat", dbusername, dbPassword);
            Statement stmt = conn.createStatement();
            
            ResultSet rs ;

            String query = "SELECT u.id, u.username AS contact, " +
                            "(SELECT m.message_text FROM messages m " +
                            "WHERE (m.sender_id = u.id AND m.receiver_id = " + sender + ") " +
                            "   OR (m.receiver_id = u.id AND m.sender_id = " + sender + ") " +
                            "ORDER BY m.id DESC LIMIT 1) AS last_message, " +
                            "(SELECT m.sent_time FROM messages m " +
                            "WHERE (m.sender_id = u.id AND m.receiver_id = " + sender + ") " +
                            "   OR (m.receiver_id = u.id AND m.sender_id = " + sender + ") " +
                            "ORDER BY m.id DESC LIMIT 1) AS last_sent_time, " +
                            "(SELECT CASE " +
                            "          WHEN m.sender_id = " + sender + " THEN TRUE " +
                            "          ELSE FALSE " +
                            "       END " +
                            "FROM messages m " +
                            "WHERE (m.sender_id = u.id AND m.receiver_id = " + sender + ") " +
                            "   OR (m.receiver_id = u.id AND m.sender_id = " + sender + ") " +
                            "ORDER BY m.id DESC LIMIT 1) AS sender_is_last " +
                            "FROM users u " +
                            "WHERE u.id IN ( " +
                            "   SELECT sender_id FROM messages WHERE receiver_id = " + sender + " " +
                            "   UNION " +
                            "   SELECT receiver_id FROM messages WHERE sender_id = " + sender + " " +
                            ") " +
                            "AND u.id != " + sender + ";";


            			
            			
//            		"SELECT u.id, u.username AS contact, " +
//                                    "(SELECT message_text FROM messages m " +
//                                    " WHERE (m.sender_id = u.id AND m.receiver_id = " + sender + ") " +
//                                    "    OR (m.receiver_id = u.id AND m.sender_id = " + sender + ") " +
//                                    " ORDER BY m.id DESC LIMIT 1) AS last_message " +
//                                    "FROM users u " +
//                                    "WHERE u.id IN ( " +
//                                    "    SELECT sender_id FROM messages WHERE receiver_id = " + sender + " " +
//                                    "    UNION " +
//                                    "    SELECT receiver_id FROM messages WHERE sender_id = " + sender + " " +
//                                    ")";

                        rs = stmt.executeQuery(query);

                        while (rs.next()) {
                        String contact = rs.getString("contact");
                        String message = rs.getString("last_message");
                        boolean isSender = rs.getBoolean("sender_is_last");
                        Timestamp sendTime = rs.getTimestamp("last_sent_time");
            
                        contactList.add(new Contact(contact, message, isSender, sendTime));
                            
                    }
                        
                        conn.close();
                        
                    } catch(SQLException | ClassNotFoundException e) {
                        
                        e.printStackTrace();
                    }
                    
           return contactList;

    }
    
    public void save_message(int senderId, int receiverId, String message) {
    	
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            
            Connection conn = DriverManager.getConnection(url + "chat", dbusername, dbPassword);
            
            String query = "INSERT INTO messages (sender_id, receiver_id, message_text, sent_time) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";
            PreparedStatement stmt = conn.prepareStatement(query);
            
            stmt.setInt(1, senderId);
            stmt.setInt(2, receiverId);
            stmt.setString(3, message);

            stmt.executeUpdate();
            conn.close();
 
        } catch(ClassNotFoundException | SQLException e) {
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
            
        } catch(ClassNotFoundException | SQLException e) {
        	
            e.printStackTrace();
            return true;
        }
        return false;
    }
    
    public void save_user(String dob,String email, String username, String password){
        
    	try {
    		
            Class.forName("com.mysql.cj.jdbc.Driver");
//            String url = "jdbc:mysql://localhost:3306/chat";
            Connection conn = DriverManager.getConnection(url + "chat", dbusername, dbPassword);
//            Statement stmt = conn.createStatement();
            
            String query = "INSERT INTO users (username, password, dob, email) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);

            pstmt.setString(1, username);
            pstmt.setString(2, password);
 
            try {
            	
                java.sql.Date sqlDate = java.sql.Date.valueOf(dob);  
                pstmt.setDate(3, sqlDate);
                
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid date format. Expected YYYY-MM-DD");
                return;  
            }
        
            pstmt.setString(4, email);
            pstmt.executeUpdate();
            
                conn.close();
            
        } catch(ClassNotFoundException | SQLException e) {
        	
            e.printStackTrace();
        }
    }
    
    public boolean findReceiver(String receiver) {
    	
        // TODO COMPLETE THE MISSING LOGIC TO FIND IF THE RECEIVER EXISTS
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection conn = DriverManager.getConnection(url + "chat", dbusername, dbPassword);
            String query = "SELECT COUNT(*) FROM users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, receiver);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
 
                int count = rs.getInt(1);
                return count > 0; 
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    
    public int findReceiverId(String receiver) {

    	int receiverId = 0;

    	
    	try {
    		
    		Class.forName("com.mysql.cj.jdbc.Driver");
    		Connection conn = DriverManager.getConnection(url + "chat", dbusername, dbPassword);
    		String query = "SELECT id FROM users WHERE username = ?";
    		
    		PreparedStatement pstmt = conn.prepareStatement(query);
    		pstmt.setString(1, receiver);
    		
    		ResultSet rs = pstmt.executeQuery();
    		if (rs.next()) {
    			receiverId = rs.getInt("id");
    			conn.close();
    			return receiverId;
    		}
    		
    		conn.close();
    		
    	} catch (SQLException | ClassNotFoundException e) {
    		
    	}
  	
    	return receiverId;
    	
    }

    public void change(User user) {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish connection to the database
            Connection conn = DriverManager.getConnection(url + "chat", dbusername, dbPassword);

            // Get the change request from the User object (we assume only one change at a time)
            Map<String, String> changes = user.getChanges();
            
            if (changes.isEmpty()) {
                System.out.println("No changes to apply.");
                return;
            }
            
            // Retrieve the field name (key) and its new value from the map
            for (Map.Entry<String, String> entry : changes.entrySet()) {
                String field = entry.getKey();
                String newValue = entry.getValue();
                
                // Define the SQL query based on the field name
                String query = "";
                switch (field) {
                    case "username":
                        query = "UPDATE users SET username = ? WHERE id = ?";
                        break;
                    case "password":
                        query = "UPDATE users SET password = ? WHERE id = ?";
                        break;
                    case "dob":
                        query = "UPDATE users SET dob = ? WHERE id = ?";
                        break;
                    case "gmail":
                        query = "UPDATE users SET email = ? WHERE id = ?";
                        break;
                    // Add more cases if there are other fields to update
                    default:
                        System.out.println("Field not recognized: " + field);
                        return;
                }

                // Prepare the SQL statement
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setString(1, newValue);
                    pstmt.setInt(2, user.getUserId()); // assuming the user object has the user ID
                    
                    // Execute the update
                    int affectedRows = pstmt.executeUpdate();
                    
                    if (affectedRows > 0) {
                        System.out.println("Successfully updated " + field + " to " + newValue);
                    } else {
                        System.out.println("Update failed for " + field);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("Error updating " + field + ": " + e.getMessage());
                }
            }
            
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Database connection error: " + e.getMessage());
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
