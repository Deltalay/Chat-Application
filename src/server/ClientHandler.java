package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import client.User;
import utils.DbConnection;

public class ClientHandler extends Thread {
	
	public Socket socket;
	public String username;
	String password;
	User user;
	
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
				
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				user = (User) ois.readObject();
				username = user.getUsername();
				password = user.getPassword();
								
//				db.run(user.username, user.password);
				isAuthenticated = db.run(username, password);
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				
				if (isAuthenticated) oos.writeObject("Login successful!");
				
                else {
                	
//                	pWriter.println("Invalid username or password. Please try again.");
//                	ois.close();
//                	socket.close();
                	
                    oos.writeObject("login failed");
                    socket.close();
                    System.out.println("Login failed. Socket closed.");
                	
                }
                
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
//			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		} finally {
			
			try {
				socket.close();
				pWriter.close();
				bReader.close();
				
			} catch (IOException e) {
				
//				e.printStackTrace();
			}
			
		}
		
	}
}