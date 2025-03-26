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
					boolean hasContact = false;
					
//					for (Contact contact: cConnection.contactsList) 
//					{
//						if (message.getSender().equals(contact.getContact())) 
//						{
//							hasContact = true;
//							contact.setLastMessage(message.getContent());
//						}
//					}
//					
					for (int i = 0; i < cConnection.contactsList.size(); i++) 
					{
						if (message.getSender().equals(cConnection.contactsList.get(i).getContact())) 
						{
							hasContact = true;
							cConnection.updateContactList(i, new Contact(message.getSender(), message.getContent()));
						}
					}
					
					System.out.println("Sender: " + message.getSender() + ", Receiver: " + message.getReceiver() + ", Content: " + message.getContent());
					
					if (!hasContact) cConnection.addContact(new Contact(message.getSender(), message.getContent()));
					
					if (cConnection.receiver.equals(message.getSender())) cConnection.addMessage(message);
					
				}

				if (receivedObject instanceof ContactList){
					cList = (ContactList) receivedObject;
					List<Contact> listContact = cList.getContacts();
					
					for (Contact contact: listContact) cConnection.contactsList.add(contact);
					
				}
				
				if (receivedObject instanceof Contact) {
					
					cContact = (Contact) receivedObject;
					if (cContact.getContact().isBlank()) System.out.println("User does not exist");
					else cConnection.addContact(cContact);
				}

				if (receivedObject instanceof ChatHistory) {
					
					cHistory = (ChatHistory) receivedObject;
					List<Message> oldMessages = cHistory.getMessages();		
//					for (Message msg: oldMessages) {
//						
//						line = msg.getContent();
//						cConnection.addMessage(msg);
//					}
					
					cConnection.addMessageHistory(oldMessages);
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
