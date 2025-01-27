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
	boolean loginIn;
	InputStreamReader ireader;
	
	Connection(String address, int port) {
		this.address = address;
		this.port = port;
	}
	
	
	void run(){
//	    System.out.println("What is your name? ");
	    Scanner scanner = new Scanner(System.in);
//	    

	    try (Socket socket = new Socket(address, port)) {
	    	System.out.println("Please Login");
            ireader = new InputStreamReader(socket.getInputStream());
            BufferedReader br = new BufferedReader(ireader);
            PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);

            boolean loggedIn = false;

            // Login Loop
            while (!loggedIn) {
                String systemMessage = br.readLine();
                System.out.println(systemMessage);

                System.out.print("Type: ");
                String username = scanner.nextLine();
				System.out.print("\033[1A"); // Move up one line
				System.out.print("\033[2K"); // Clear the line
				System.out.print("\033[1A"); // Move up another line
				System.out.print("\033[2K"); // Clear the line
                pw.println(username);

                systemMessage = br.readLine();
                System.out.println(systemMessage);

                System.out.print("Type: ");
                String password = scanner.nextLine();
				System.out.print("\033[1A"); // Move up one line
				System.out.print("\033[2K"); // Clear the line
				System.out.print("\033[1A"); // Move up another line
				System.out.print("\033[2K"); // Clear the line
                pw.println(password);

                // Check login result from server
                systemMessage = br.readLine();
                if (systemMessage.equals("Login successful!")) {
                    loggedIn = true;
					System.out.print("\033[1A"); // Move up another line
					System.out.print("\033[2K"); // Clear the line
					System.out.println(systemMessage);
                }else{
					System.out.print("\033[1A"); // Move up another line
					System.out.print("\033[2K"); // Clear the line
					System.out.println(systemMessage);
				}
            }
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
