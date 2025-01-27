package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Connection {
	
	private String address;
	private int port;
	
	InputStreamReader ireader;
	
	Connection(String address, int port) {
		this.address = address;
		this.port = port;
	}
	
	void run(){
	    System.out.println("What is your name? ");
		Scanner scanner = new Scanner(System.in);
		
    	System.out.print("Type: ");
	    String username = scanner.nextLine();
	    System.out.print("\033[1A"); // Move up one line
	    System.out.print("\033[2K"); // Clear the line
	    System.out.print("\033[1A"); // Move up another line
	    System.out.print("\033[2K"); // Clear the line
//	    

	    try (Socket socket = new Socket(address, port)) {
	    	
	    	System.out.println("connected to server");
	    	ireader = new InputStreamReader(socket.getInputStream());
	    	BufferedReader br = new BufferedReader(ireader);
	    	
//	    	String systemMessage = br.readLine();
//	    	System.out.println(systemMessage);
	    	System.out.println("Welcome to the group chat, " + username);
	    	
	    	PrintWriter pw = new PrintWriter(socket.getOutputStream());

	      	pw.println(username);
	      	pw.flush();
	      	
	      	new Thread(new MessageReceiver(socket)).start();
	      	
	      	String message = null;
	      	while (true) {
//		        System.out.print("Type: ");
		        message = scanner.nextLine();
		        System.out.print("\033[1A"); // Move up one line
		        System.out.print("\033[2K"); // Clear the line
		        

		        if(message.equals("close")) {
		        	System.out.println("See you next time.");
			        pw.println(message);
			        pw.flush();	
		        	break;
		        }
		        

		        pw.println(message);
		        pw.flush();		        

//		        System.out.println(username+": " + message);
		        
	      	}
	    }
	    catch (IOException e) {
	      e.printStackTrace();
	    } 
	}
}
