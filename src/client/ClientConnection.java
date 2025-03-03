package client;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
//import java.net.UnknownHostException;
import java.util.Scanner;

import utils.Message;
import utils.ChatSessionRequest;
import utils.Connection;
import utils.User;
import utils.NewUser;

public class ClientConnection implements Connection {
	
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

	@Override
	public void register(NewUser newuser) throws IOException, ClassNotFoundException{
		System.out.println("Sending registration request...");
		oos.writeObject(newuser);
		oos.flush();
		System.out.println("Waiting for server response...");	

		String response = (String) ois.readObject();
		if (response.equals("Account create failed!")) {
  		
			System.out.println("Account create failed! Please try again");
		}
		else {
			System.out.println("\033[H\033[2J");
			System.out.println(response);
			ChatClient.isAuthenticated = true;
			startCommunication(newuser, oos, ois);
		}
	}

	@Override
	public void authenticate(User user) throws IOException, ClassNotFoundException {
		
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

	public void startCommunication(NewUser user, ObjectOutputStream oos, ObjectInputStream ois) throws IOException {
		// TODO Auto-generated method stub
		
		System.out.println("Type '/msg <Username>' to message someone");
		Scanner scanner = new Scanner(System.in);

        // Start the message receiver thread
        new Thread(new MessageReceiver(this.ois, this)).start();

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
                System.out.println("Exited private chat");
        		System.out.println("Type '/msg <Username>' to message someone");
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
		return ois.readObject();
	}

}
