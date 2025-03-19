package client;

import javafx.application.Platform;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

import utils.ChatHistory;
import utils.Message;
import utils.Contact;
import utils.ContactList;


public class MessageReceiver implements Runnable {

//	private Socket socket;
	ObjectInputStream ois;
	Message message;
	ChatHistory cHistory;
	ContactList cList;
	ClientConnection cConnection;
	Contact cContact;

	public MessageReceiver(ObjectInputStream ois, ClientConnection cConnection) {
//		this.socket = socket;
		this.ois = ois;
		this.cConnection = cConnection;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		Object receivedObject;
		
		try {
			
			String line; 
			while (true) {
				
				receivedObject = cConnection.receiveMessage(ois);
				
				if (receivedObject instanceof Message) {

					message = (Message) receivedObject;
//					line = message.getContent();
//					
//					if (line.equals("close")) break;
//					
//					if (message.getReceiver().equals("")) 
//						System.out.println(message.getSender() + "(BroadCast): " + line);
//					
//					else System.out.println(message.getSender() + ": " + line);
					cConnection.messageList.add(message);
				}

				if (receivedObject instanceof ContactList){
					cList = (ContactList) receivedObject;
					List<Contact> listContact = cList.getContacts();
					
					for (Contact contact: listContact) {
						cConnection.contactsList.add(contact);
					}

				}

				if (receivedObject instanceof ChatHistory) {
					
					cHistory = (ChatHistory) receivedObject;
					List<Message> oldMessages = cHistory.getMessages();
					cConnection.messageList.clear();
					
					for (Message msg: oldMessages) {
						
						line = msg.getContent();
						System.out.println(msg.getSender() + ": " + line);
						cConnection.addMessage(msg);
					}
				}
				
//				if (receivedObject instanceof Contact) {
//					contact = (Contact) receivedObject;
//					cConnection.addContact(contact);
//
//				}
			}
			
		} catch (IOException e) {
			
//			System.out.println("Error receiving message from server");
		} catch (ClassNotFoundException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
