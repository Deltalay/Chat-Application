package server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import utils.DbConnection;

public class ClientManager {
	
	private Map<String, ClientHandler> clientSockets = new ConcurrentHashMap<>();
	private Map<String, String> clientconnection = new ConcurrentHashMap<>();
	
	DbConnection db = new DbConnection();

	public void addClient(String username, ClientHandler cHandler) {
		
		clientSockets.put(username, cHandler);
	}
	
	public void removeclient(String username) {
		
		clientSockets.remove(username);
	}
	public void addClientConnection(String username1, String username2) {
		clientconnection.put(username1, username2);
	}
	
	
	public void broadcastMessages(String message, ClientHandler cHandler) {
		
		for (Map.Entry<String, ClientHandler> entry: clientSockets.entrySet()) {
			
			entry.getValue().pWriter.println(cHandler.username + "(Broadcast): " + message);
		}
	}
	
	public void privateMessage(String sendFrom, String sendTo) {
		
		clientSockets.get(sendFrom).pWriter.println("Private message with " + sendTo +" :");
		String history = db.private_chat(sendFrom, sendTo);
		clientSockets.get(sendFrom).pWriter.println(history);
	}
	
	public void sendprivateMessage(String sendFrom, String sendTo, String message) {
		
		db.save_message(sendFrom, sendTo, message);
		clientSockets.get(sendFrom).pWriter.println(sendFrom + ": " + message);
		if (clientSockets.containsKey(sendTo)) {
			if (clientconnection.get(sendTo) != null && clientconnection.get(sendTo).equals(sendFrom)) {
				clientSockets.get(sendTo).pWriter.println(sendFrom + ": " + message);
			}
		}

	}
	
}
