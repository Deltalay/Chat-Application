package server;	

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatServer {
	
	private static final int PORT = 3001;
	private static CopyOnWriteArrayList<Socket> clientSockets = new CopyOnWriteArrayList<>();
	
	public static void main(String[] args) {
		try (ServerSocket serverSocket = new ServerSocket(PORT)) {
			
			while (true) {
				Socket clientSocket = serverSocket.accept();
				clientSockets.add(clientSocket);
				
				new ClientHandler(clientSocket).start();;
				
			}

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void broadcastMessages(String message, String username) {
		for (Socket client : clientSockets) {
			try {
				
				PrintWriter out = new PrintWriter(client.getOutputStream(), true);
				if (!message.equals("close")) 
					out.println(username + ": " + message);
				
			} catch (IOException e) {
//				System.out.println("Error sending messages");
				clientSockets.remove(client);
			}
		}
	}
}