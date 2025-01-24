package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Connection {
	private String address;
	private int port;
	private String username;
	
	Connection(String address, int port) {
		this.address = address;
		this.port = port;
	}
	
	void run(){
		System.out.println("What is your name? ");
		Scanner scanner = new Scanner(System.in);
		System.out.print("Type: ");
		username = scanner.nextLine();
		System.out.print("\033[1A"); // Move up one line
        System.out.print("\033[2K"); // Clear the line
        System.out.print("\033[1A"); // Move up another line
        System.out.print("\033[2K"); // Clear the line
		try (Socket socket = new Socket(address, port)) {
			PrintWriter pw = new PrintWriter(socket.getOutputStream());
			System.out.println("connected to server");
			pw.println(username);
			pw.flush();
			String message = null;
			while (true) {
				System.out.print("Type: ");
				message = scanner.nextLine();
				System.out.print("\033[1A"); // Move up one line
                System.out.print("\033[2K"); // Clear the line
                System.out.println(username+": " + message);
				pw.println(message);
				pw.flush();
				if(message.equals("close")){
					break;
				}
			}

		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

}
