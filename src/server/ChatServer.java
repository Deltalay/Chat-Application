package server;	

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatServer {
	
	private static final int PORT = 3001;
//	private static CopyOnWriteArrayList<Socket> clientSockets = new CopyOnWriteArrayList<>();
//	private static ConcurrentMap<String, Socket> clientSockets = new ConcurrentHashMap<>();
//	private static ConcurrentMap<String, ClientHandler> clientSockets = new ConcurrentHashMap<>();
	
	public static void main(String[] args) {
		try (ServerSocket serverSocket = new ServerSocket(PORT)) {
			System.out.println("Server Started!");
			
			ClientManager cManager = new ClientManager();
			
			while (true) {
				Socket clientSocket = serverSocket.accept();
				
//				new ClientHandler(clientSocket).start();;
//				clientSockets.add(clientSocket);
				
				ClientHandler cHandler = new ClientHandler(clientSocket, cManager);
				cHandler.start();
				
				while(cHandler.username == null) {
					
				}
				
				cManager.addClient(cHandler.username, cHandler);

//				clientSockets.put(cHandler.username, cHandler);
				
			}

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public static void broadcastMessages(String message, ClientHandler CH) {
//		for (Socket client : clientSockets) {
//			try {
//				
//				PrintWriter out = new PrintWriter(client.getOutputStream(), true);
//				if (!message.equals("close")) 
//					out.println(CH.username + ": " + message);
//				
//			} catch (IOException e) {
////				System.out.println("Error sending messages");
////				clientSockets.remove(client);
//			}
//		}
		
//	    System.out.println("Broadcasting message to " + clientSockets.size() + " clients.");

//		for (Map.Entry<String, ClientHandler> entry: clientSockets.entrySet()) {
//			
//			try {
//				
//				PrintWriter pWriter = new PrintWriter(entry.getValue().socket.getOutputStream(), true);
//				
//				if (!message.equals("close")) 
//					pWriter.println(CH.username + ": " + message); 
//				
//			} catch (IOException e) {
//				System.out.println("Error sending message to " + entry.getKey() + ": " + e.getMessage());
//				clientSockets.remove(entry.getKey());
//
//			}
//		}
	}
	
//	public static void removeDisconnectedClient(String username) {
//		
//		clientSockets.remove(username);
//	}
}
