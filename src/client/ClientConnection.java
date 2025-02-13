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
import utils.User;

public class ClientConnection implements Connection {
	
	private String address;
	private int port;
	boolean loginIn;
	InputStreamReader ireader;
	User user;
	Socket socket;
	ChatSessionRequest openChatReq;
	
	ClientConnection(String address, int port, User user) throws IOException {
		this.address = address;
		this.port = port;
		this.user = user;
		connect(address, port, user);
	}
	

	@Override
	public void connect(String address, int port, User user) throws UnknownHostException, IOException {
		// TODO Auto-generated method stub
		
		socket = new Socket(address, port);
        System.out.println("Connected to server at " + address + ":" + port);
        
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        
        do {
        	try {
        		
        		authenticate(user, ois, oos);
        	} catch (Exception e) {
        		
        		
        	}
        } while (!ChatClient.isConnected);
        
        startCommunication(oos, ois);
        
	}



	@Override
	public void authenticate(User user, ObjectInputStream ois, ObjectOutputStream oos) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);

		System.out.print("Enter username: ");
		user.setUsername(scanner.nextLine());
		
		System.out.print("Enter password: ");
		user.setPassword(scanner.nextLine());
		
		oos.writeObject(user);
		oos.flush();

  		String response = (String) ois.readObject();
  
  		if (response.equals("login failed")) {
  		
            socket.close();
            System.out.println("Please Login again");
      	}
  		
		else {
			System.out.println("\033[H\033[2J");
			System.out.println(response);
			ChatClient.isConnected = true;
			
			startCommunication(oos, ois);
		}
	}

	public void startCommunication(ObjectOutputStream oos, ObjectInputStream ois) throws IOException {
		// TODO Auto-generated method stub
		
		Scanner scanner = new Scanner(System.in);

        // Start the message receiver thread
        new Thread(new MessageReceiver(ois)).start();

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
            oos.writeObject(message);
            oos.flush();
        }
	}


	public void disconnect() throws IOException {
		
		if (socket != null && !socket.isClosed()) {
			
            socket.close();
            System.out.println("Disconnected from server.");
        }
	}

}
