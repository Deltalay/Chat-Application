package server;

import java.sql.*;

public class dbconnection {
    String url="jdbc:mysql://localhost:3306/chat";
    String dbusername = "root";
    String dbpassword = "";
    private String username;
    private String password;
    private String email;
    boolean run(String InputName,String InputPassword){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, dbusername, dbpassword);
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM users";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                username = rs.getString("username");
                password = rs.getString("password");
                if (username.equals(InputName) && password.equals(InputPassword)) {
                    conn.close(); // Close connection before returning
                    return true;
                }
            }
            conn.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public void setUser(String user){
        this.username = user;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public String getUser(){
        return username;
    }
    public String getPassword(){
        return password;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return email;
    }
}
