package server;	

import java.io.IOException;
import java.net.ServerSocket;

public class ChatServer {
	
	private static final int PORT = 3001;
	
	public static void main(String[] args) {
		try (ServerSocket serverSocket = new ServerSocket(PORT)) {
			
			ClientHandler ch = new ClientHandler(serverSocket.accept());
			ch.run();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
