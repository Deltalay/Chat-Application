package client;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import utils.Message;
import utils.ChatSessionRequest;
import utils.Connection;
import utils.User;

public class ClientConnection implements Connection {
	
//	private String address;
//	private int port;
	boolean loginIn;
	InputStreamReader ireader;
	User user;
	Socket socket;
	ChatSessionRequest openChatReq;
	ObjectInputStream ois;
	ObjectOutputStream oos;
	
	ClientConnection(String address, int port) throws IOException {
		this.socket = new Socket(address, port);
		this.oos = new ObjectOutputStream(socket.getOutputStream());
        this.ois = new ObjectInputStream(socket.getInputStream());
	}
	
	public void connect() throws UnknownHostException, IOException {
		
//		socket = new Socket(address, port);
//        System.out.println("Connected to server at " + address + ":" + port);
        
//        this.oos = new ObjectOutputStream(socket.getOutputStream());
//        this.ois = new ObjectInputStream(socket.getInputStream());
        
        do {
        	try {
        		
        		authenticate(user);
        	} catch (Exception e) {
        		
        	}
        } while (!ChatClient.isAuthenticated);
        
        startCommunication(user, oos, ois);
        
	}



	@Override
	public void authenticate(User user) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
//		Scanner scanner = new Scanner(System.in);
//
//		System.out.println("Please Login");
//		
//		System.out.print("Enter username: ");
//		user.setUsername(scanner.nextLine());
//		
//		System.out.print("Enter password: ");
//		user.setPassword(scanner.nextLine());
		
		oos.writeObject(user);
		oos.flush();

  		String response = (String) ois.readObject();
  
  		if (response.equals("login failed")) {
  		
            System.out.println("Please Login again");
      	}
  		
		else {
			System.out.println("\033[H\033[2J");
			System.out.println(response);
			ChatClient.isAuthenticated = true;
			
			startCommunication(user, oos, ois);
		}
	}

	public void startCommunication(User user, ObjectOutputStream oos, ObjectInputStream ois) throws IOException {
		// TODO Auto-generated method stub
		
		Scanner scanner = new Scanner(System.in);

        // Start the message receiver thread
        new Thread(new MessageReceiver(this.ois)).start();

        String content;
        String receiver = "";
        Message message;

        while (true) {
            content = scanner.nextLine();
            System.out.print("\033[1A");
            System.out.print("\033[2K");

            if (content.startsWith("/msg")) {
                System.out.print("\033[H\033[2J");
                System.out.flush();

                String[] parts = content.split(" ");
                receiver = parts[1];

                System.out.println("========== Private Chat With " + receiver + " ==========\n");
                ChatSessionRequest openChatReq = new ChatSessionRequest(user.getUsername(), receiver);
                oos.writeObject(openChatReq);
                oos.flush();

                content = scanner.nextLine();
                System.out.print("\033[1A");
                System.out.print("\033[2K");
            }

            if (content.startsWith("/quit")) {
                System.out.print("\033[H\033[2J");
                System.out.flush();

                receiver = "";
                System.out.println("Exited private chat. Returning to public chat...");
                ChatSessionRequest openChatReq = new ChatSessionRequest(user.getUsername());
                oos.writeObject(openChatReq);
                oos.flush();
                continue;
            }

            message = new Message(user.getUsername(), receiver, content);
            sendMessage(message, oos);
        }
	}


	public void disconnect() throws IOException {
		
		if (socket != null && !socket.isClosed()) {
			
            socket.close();
            System.out.println("Disconnected from server.");
        }
	}

	@Override
	public void sendMessage(Object message, ObjectOutputStream oos) throws IOException {
		// TODO Auto-generated method stub
		
		oos.writeObject(message);
        oos.flush();
	}

	@Override
	public Object receiveMessage(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

}
