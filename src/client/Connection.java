package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Connection {
	
	private String address;
	private int port;
	boolean loginIn;
	InputStreamReader ireader;
	User user;
	Socket socket;
	
	Connection(String address, int port, User user) {
		this.address = address;
		this.port = port;
		this.user = user;
	}
	

	
	void login(){
		Scanner scanner = new Scanner(System.in);

	    try {
			socket = new Socket(address, port);
            ireader = new InputStreamReader(socket.getInputStream());
            PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);

			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(user);
			oos.flush();

			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
      		String response = (String) ois.readObject();
      
      	if (response.equals("login failed")) {
            socket.close();
            System.out.println("Please Login again");}
			else {
				System.out.println("\033[H\033[2J");
				System.out.println(response);
				ChatClient.isConnected = true;
			}
	    }
	    catch (IOException | ClassNotFoundException e) {
	      
	    } 
	}
	void start(){
		Scanner scanner = new Scanner(System.in);
	    try {
            ireader = new InputStreamReader(socket.getInputStream());
            PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);

	      	new Thread(new MessageReceiver(socket)).start();
	      	
	      	String message = null;
	      	while (true) {
		        message = scanner.nextLine();
		        System.out.print("\033[1A");
		        System.out.print("\033[2K");
		        

		        if(message.equals("close")) {
		        	System.out.println("See you next time.");
			        pw.println(message);
			        pw.flush();	
		        	break;
		        }
		        

		        pw.println(message);
		        pw.flush();		        

		        
	      	}
	    }
	    catch (IOException e) {
	      e.printStackTrace();
	    }
	}

}
