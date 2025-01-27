package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {
	
	public Socket socket;
	public String username;
	
	
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
			
//			pWriter.println("Welcome, What's ur name");
//						
			this.username = bReader.readLine();
			System.out.println(username + " Connected");
			
			String message;
			
			while ((message = bReader.readLine()) != null) {
				
				if (message.equals("close")) {
					System.out.println(username + " is disconnected");
//					ChatServer.removeDisconnectedClient(username);
					cManager.removeclient(message);
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
			
//			e.printStackTrace();
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
