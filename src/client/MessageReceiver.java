package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

import utils.ChatHistory;
import utils.Message;

public class MessageReceiver implements Runnable {

//	private Socket socket;
	ObjectInputStream ois;
	Message message;
	ChatHistory cHistory;

	public MessageReceiver(ObjectInputStream ois) {
//		this.socket = socket;
		this.ois = ois;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		Object receivedObject;
		
		try {
			
			String line; 
			while (true) {
				
				receivedObject = ois.readObject();
				
				if (receivedObject instanceof Message) {
					
					message = (Message) receivedObject;
					line = message.getContent();
					
					if (line.equals("close")) break;
					
					if (message.getReceiver().equals("")) 
						System.out.println(message.getSender() + "(BroadCast): " + line);
					
					else System.out.println(message.getSender() + ": " + line);
				
				}
				
				if (receivedObject instanceof ChatHistory) {
					
					cHistory = (ChatHistory) receivedObject;
					List<Message> oldMessages = cHistory.getMessages();
					
					for (Message msg: oldMessages) {
						
						line = msg.getContent();
						System.out.println(msg.getSender() + ": " + line);
					}
				}
			}
			
		} catch (IOException e) {
			
//			System.out.println("Error receiving message from server");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
