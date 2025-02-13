package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import utils.ChatHistory;
import utils.Message;
import utils.ChatSessionRequest;
import utils.User;

public class ClientHandler extends Thread {
	
	public Socket socket;
	public String username;
	String password;
	User user;
	
	DbConnection db = new DbConnection();
	boolean isAuthenticated = false;
	
	ClientManager cManager;
	Message message;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	ChatSessionRequest openChatReq;
	ChatHistory cHistory;
	
	ClientHandler(Socket socket, ClientManager cManager) {
		
		this.socket = socket;
		this.cManager = cManager;
		try {
			
			this.ois = new ObjectInputStream(socket.getInputStream());
			this.oos = new ObjectOutputStream(socket.getOutputStream());
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	public void run() {
		
		try {	
			
//			ois = new ObjectInputStream(socket.getInputStream());
//			oos = new ObjectOutputStream(socket.getOutputStream());
			while (!isAuthenticated) {
					
				user = (User) ois.readObject();
				username = user.getUsername();
				password = user.getPassword();
								
//				db.run(user.username, user.password);
				isAuthenticated = db.check_login(username, password);
					
				if (isAuthenticated) oos.writeObject("Login successful!");
				
                else {
                	
//                	pWriter.println("Invalid username or password. Please try again.");
//                	ois.close();
//                	socket.close();
                	
                    oos.writeObject("login failed");
//                    socket.close();	
                    System.out.println("Login failed. Socket closed.");
                	
                }
                
			}
			
			System.out.println(username + " Connected to Server ");
			cManager.addClient(username, this);
			
//			cManager.addClientConnection(username, "");
			
			Object receivedObject;
			
			while (true) {
				
				receivedObject = ois.readObject();
				
				if (receivedObject instanceof Message) {
					
					message = (Message) receivedObject;
					
//					cManager.broadcastMessages(message.getContent(), this);
					if (message.getReceiver() == "") cManager.broadcastMessages(message, this);
					else {
						cManager.sendPrivateMessage(message);
						db.save_message(message.getSender(), message.getReceiver(), message.getContent());}
					
					System.out.println(message.getSender() + " to " + message.getReceiver() + ": " + message.getContent());
					
				}
				
				if (receivedObject instanceof ChatSessionRequest) {
					
					openChatReq = (ChatSessionRequest) receivedObject;
					String sender = openChatReq.getSender();
					String receiver;
					if(openChatReq.getReceiver() == "") receiver="";
					else {
						receiver = openChatReq.getReceiver();
					
					List<Message> oldMessages = cManager.getOldMessages(sender, receiver);
					cHistory = new ChatHistory(oldMessages);
					
					oos.writeObject(cHistory);
					oos.flush();}
					cManager.addClientChatSession(sender, receiver);
					
					
				}
			}	
	
//			while ((message = bReader.readLine()) != null) {
//				
//                if (message.equals("close")) {
//                	
//                    System.out.println(username + " is disconnected");
//                    cManager.removeclient(username);
//                    break;
//                }
//                
//                if (message.startsWith("/msg")) {
//                    
//                    String[] parts = message.split(" ");
//                    String sendTo = parts[1];
//					pWriter.println("\033[H\033[2J"); 
//                	pWriter.flush();
//                    cManager.privateMessage(username, sendTo);
//					while ((message = bReader.readLine()) != null) {
//						cManager.addClientConnection(username, sendTo);
//						if (message.equals("/quit")) {
//							pWriter.println("\033[H\033[2J");
//                			pWriter.flush();
//							System.out.println(username + " quit private message with " + sendTo);
//							cManager.addClientConnection(username, "");
//							break;
//						}
//						
//						else cManager.sendprivateMessage(username, sendTo, message);
//						
//						System.out.println(username + ": " + message);
//		
//					}
//
//                } 
//
//                else cManager.broadcastMessages(message, this);
//				
//				System.out.println(username + ": " + message);
//
//			}
			
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			cManager.removeclient(username);
			System.out.println(username + " is disconnected");
			
		} finally {
			
			try {
				
				if (ois != null) ois.close();
	            if (oos != null) oos.close();
	            if (socket != null) socket.close();
				
			} catch (IOException e) {
				if (username != null) {
        cManager.removeclient(username);
        System.out.println(username + " is disconnected");
    } else {
        System.out.println("Client disconnected before authentication.");
    }
//				e.printStackTrace();
			}
			
		}
		
	}
}