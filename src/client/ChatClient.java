package client;

import java.util.Scanner;

import utils.User;

public class ChatClient {
	
	static String SERVER_ADDRESS = "192.168.100.38";
	static int SERVER_PORT = 3001;
	static boolean isConnected = false;
	
	public static void main(String[] args){
		Connection con = null;
		
//		System.out.println("Client");
		System.out.println("Please Login");
		while (!isConnected) {
			System.out.println("What tf is ur name");
			Scanner scanner = new Scanner(System.in);
			String username = scanner.nextLine();
			
			System.out.println("What tf is ur password");
			String password = scanner.nextLine();
			User user = new User(username, password);
			con = new Connection(SERVER_ADDRESS, SERVER_PORT, user);
			con.login();
			
		}
//		con.
		
	}
}
