package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import client.Connection;
import utils.ChatHistory;
import utils.Message;
import utils.ChatSessionRequest;
import utils.User;

public class ClientHandler extends Thread implements Connection {
	
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
		
	}
	
	@Override
	public void authenticate(User user, ObjectInputStream ois, ObjectOutputStream oos) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		
		System.out.println("g");
		user = (User) ois.readObject();
		
		username = user.getUsername();
		password = user.getPassword();
//		System.out.println(username + password);
						
		isAuthenticated = db.check_login(username, password);
			
		if (isAuthenticated) oos.writeObject("Login successful!");
		
        else {
        	
            oos.writeObject("login failed");
            System.out.println(username + " login failed.");	
        }
		
	}	
	
	public void run() {
		
		try {
			
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());			
			
			while (!isAuthenticated) 
				authenticate(user, ois, oos);
			
			System.out.println(username + " Connected to Server ");
			cManager.addClient(username, this);
			
			Object receivedObject;
			
			while (true) {
				
				receivedObject = ois.readObject();
				
				if (receivedObject instanceof Message) {
					
					message = (Message) receivedObject;
					
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
			
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
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

	@Override
	public void sendMessage(Object message, ObjectOutputStream oos) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object receiveMessage(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

}