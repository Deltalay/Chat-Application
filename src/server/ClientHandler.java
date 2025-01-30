package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {
	
	public Socket socket;
	public String username;
	String password;
	
	Dbconnection db=new Dbconnection();
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
		
//		System.out.println("Client Connected");
		
		try {
			isReader = new InputStreamReader(socket.getInputStream());
			bReader = new BufferedReader(isReader);
			pWriter = new PrintWriter(socket.getOutputStream(), true);
			
			while(!isAuthenticated) {
				
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
			
			cManager.addClientConnection(username, "");
			
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
					pWriter.println("\033[H\033[2J"); // Clears the screen
                	pWriter.flush();
                    cManager.privateMessage(username, sendTo);
					while ((message = bReader.readLine()) != null) {
						cManager.addClientConnection(username, sendTo);
						if (message.equals("/quit")) {
							pWriter.println("\033[H\033[2J"); // Clears the screen
                			pWriter.flush();
							System.out.println(username + " quit private message with " + sendTo);
							cManager.addClientConnection(username, "");
							break;
						}
						
						else cManager.sendprivateMessage(username, sendTo, message);
						
						System.out.println(username + ": " + message);
		
					}

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