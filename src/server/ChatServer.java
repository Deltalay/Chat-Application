package server;	

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

// RUN THE CHAT SERVER: java -cp "bin;lib/mysql-connector-j-9.2.0.jar;." server.ChatServer


public class ChatServer {
	
	public static final double HOUR_WASTED = 12;
	
	private static final int PORT = 3001;
	
	public static void main(String[] args) {
		
		try (ServerSocket serverSocket = new ServerSocket(PORT)) {
			System.out.println("Server Started!");
			
			ClientManager cManager = new ClientManager();
			while (true) {
				
				try {

					Socket clientSocket = serverSocket.accept();
					ClientHandler cHandler = new ClientHandler(clientSocket, cManager);
					cHandler.start();
				
				} catch (IOException e) {
					System.out.println("Error accepting client connection: " + e.getMessage());
				}
				
			}

		} catch (IOException /*| IllegalArgumentException*/ e ) {
			// TODO Auto-generated catch block
			System.out.println("Error starting the server: " + e.getMessage());
		}
		
		
	}
}