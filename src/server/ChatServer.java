package server;	

import java.io.IOException;
import java.net.ServerSocket;

public class ChatServer {
	
	private static final int PORT = 3001;
	
	public static void main(String[] args) {
		try (ServerSocket serverSocket = new ServerSocket(PORT)) {
			
			while (true) {
				new ClientHandler(serverSocket.accept()).start();;
					
			}

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
