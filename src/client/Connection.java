package client;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import utils.Message;
import utils.OpenChatSessionRequest;
import utils.User;

public class Connection {
	
	private String address;
	private int port;
	boolean loginIn;
	InputStreamReader ireader;
	User user;
	Socket socket;
	OpenChatSessionRequest openChatReq;
	
	Connection(String address, int port, User user) {
		this.address = address;
		this.port = port;
		this.user = user;
	}
	

	
	void login() {
		Scanner scanner = new Scanner(System.in);

	    try {
			socket = new Socket(address, port);
//            ireader = new InputStreamReader(socket.getInputStream());

			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(user);
			oos.flush();

			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
      		String response = (String) ois.readObject();
      
      		if (response.equals("login failed")) {
      		
	            socket.close();
	            System.out.println("Please Login again");
	      	}
      		
			else {
				System.out.println("\033[H\033[2J");
				System.out.println(response);
				ChatClient.isConnected = true;
				
				start(oos, ois);
			}
      		
	    } catch (IOException | ClassNotFoundException e) {
	      
	    	e.printStackTrace();
	    } 
	}
	
	void start(ObjectOutputStream oos, ObjectInputStream ois) {
		
		Scanner scanner = new Scanner(System.in);
		
	    try {
	    	
//          ireader = new InputStreamReader(socket.getInputStream());
//          PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
//			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			
	      	new Thread(new MessageReceiver(ois)).start();
	      	
			String content = null;
			String receiver = "";
			Message message = null;

	      	while (true) {

				content = scanner.nextLine();
				System.out.print("\033[1A");
		        System.out.print("\033[2K");

//		        if(message.getContent().equals("close")) {
//		        	System.out.println("See you next time.");
//			        pw.println(message);
//			        pw.flush();	
//		        	break;
//		        }
//		        
		        if (content.startsWith("/msg")) {
		        	
					System.out.print("\033[H\033[2J");
					System.out.flush();

					String[] parts = content.split(" ");
                    receiver = parts[1];
                    
                    System.out.println("========== Private Chat With " + receiver + " ==========\n");
                    openChatReq = new OpenChatSessionRequest(user.getUsername(), receiver);
                    
                    oos.writeObject(openChatReq);
                    oos.flush();
                    
                    content = scanner.nextLine();
    		        System.out.print("\033[1A");
    		        System.out.print("\033[2K");
                    
//					message.setContent(message.getSender() + " is currently chatting with " + receiver);
//			        pw.println("/msg " + receiver);
//			        pw.flush();	
		        }
		        
		        message = new Message(user.getUsername(), receiver, content);
//		        message.setContent(content);
		        
		        oos.writeObject(message);
				oos.flush();	        
 
	      	}
	    }
	    catch (IOException e) {
	    	
	      e.printStackTrace();
	    }
	}

}
