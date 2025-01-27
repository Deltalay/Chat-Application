package server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientManager {
	
	private Map<String, ClientHandler> clientSockets = new ConcurrentHashMap<>();
	
	public void addClient(String username, ClientHandler cHandler) {
		
		clientSockets.put(username, cHandler);
	}
	
	public void removeclient(String username) {
		
		clientSockets.remove(username);
	}
	
	public void broadcastMessages(String message, ClientHandler cHandler) {
		
		for (Map.Entry<String, ClientHandler> entry: clientSockets.entrySet()) {
			
			entry.getValue().pWriter.println(cHandler.username + ": " + message);
		}
	}
	
	public void privateMessage(String message, String sendFrom, String sendTo) {
		
		clientSockets.get(sendTo).pWriter.println("Private message from " + sendFrom + ": " + message);
	}
	
}
