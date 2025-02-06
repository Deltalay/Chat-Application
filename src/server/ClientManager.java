package server;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import utils.DbConnection;
import utils.Message;

public class ClientManager {
	
	private Map<String, ClientHandler> clientSockets = new ConcurrentHashMap<>();
	private Map<String, String> chatSessions = new ConcurrentHashMap<>();
	
	DbConnection db = new DbConnection();

	public void addClient(String username, ClientHandler cHandler) {
		
		clientSockets.put(username, cHandler);
	}
	
	public void removeclient(String username) {
		
		clientSockets.remove(username);
	}
	public void addClientChatSession(String sender, String receiver) {
		
		chatSessions.put(sender, receiver);
	}
	
	
	public void broadcastMessages(Message message, ClientHandler cHandler) {
		
		for (Map.Entry<String, ClientHandler> entry: clientSockets.entrySet()) {
			
			try {
				
				entry.getValue().oos.writeObject(message);
				
			} catch (IOException e) {
				
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public List<Message> getOldMessages(String sendFrom, String sendTo) {
		
		List<Message> messageHistory = db.private_chat(sendFrom, sendTo);
		
		return messageHistory;
	}
	
	public void sendPrivateMessage(Message message) {
		
		String sender = message.getSender();
		String receiver = message.getReceiver();
		String messageContent = message.getContent();
		
//		db.save_message(sendFrom, sendTo, message);
		
		try {
			clientSockets.get(sender).oos.writeObject(message);	
		
			if (isReceiverChatSessionOpen(sender, receiver)) 
					clientSockets.get(receiver).oos.writeObject(message);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	
	public boolean isReceiverChatSessionOpen(String sender, String receiver) {
		
		return clientSockets.containsKey(receiver) && chatSessions.get(receiver).equals(sender);
    }
	
}
