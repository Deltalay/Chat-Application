package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import utils.DbConnection;

public class ClientHandler extends Thread {
	
	public Socket socket;
	public String username;
	String password;
	
	DbConnection db = new DbConnection();
	boolean isAuthenticated = false;
	
	InputStreamReader isReader;
	BufferedReader bReader;
	PrintWriter pWriter;
	ClientManager cManager;
	
	ClientHandler(Socket socket, ClientManager cManager) {
		
		this.socket = socket;
		this.cManager = cManager;
	}
	
	public void run() {
		
		try {
			
			isReader = new InputStreamReader(socket.getInputStream());
			bReader = new BufferedReader(isReader);
			pWriter = new PrintWriter(socket.getOutputStream(), true);
			
			while (!isAuthenticated) {
				
				pWriter.println("Enter your username");
				username = bReader.readLine();
				
				pWriter.println("Enter your password");
				password = bReader.readLine();
				
				db.run(username,password);
				isAuthenticated = db.run(username, password);
				
				if (isAuthenticated) pWriter.println("Login successful!");
                else pWriter.println("Invalid username or password. Please try again.");
                
			}
			
			System.out.println(username + " Connected to server ");
			cManager.addClient(username, this);
			
			String message;
	
			while ((message = bReader.readLine()) != null) {
				
                if (message.equals("close")) {
                	
                    System.out.println(username + " is disconnected");
                    cManager.removeclient(username);
                    break;
                }
                
                if (message.startsWith("/msg")) {
                    
                    String[] parts = message.split(" ");
                    String sendTo = parts[1];
                    String privateMessage = message.substring(message.indexOf(sendTo) + sendTo.length() + 1);
                    
                    cManager.privateMessage(privateMessage, username, sendTo);
                 } 

                else cManager.broadcastMessages(message, this);
				
				System.out.println(username + ": " + message);

			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
			try {
				socket.close();
				pWriter.close();
				bReader.close();
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
		}
		
	}
}