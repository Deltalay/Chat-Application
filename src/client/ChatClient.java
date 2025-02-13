package client;

import java.io.IOException;
import java.util.Scanner;

import utils.User;

public class ChatClient {
	
	static String SERVER_ADDRESS = "172.23.2.163";
	static int SERVER_PORT = 3001;
	static boolean isConnected = false;
	
	public static void main(String[] args) throws IOException {
		Scanner scanner = new Scanner(System.in);
		ClientConnection con = null;
		
//		System.out.println("Client");
		System.out.println("Please Login");
		while (!isConnected) {
			User user = new User();

			
			con = new ClientConnection(SERVER_ADDRESS, SERVER_PORT, user);
			
		}
//		con.
		
	}
}
