package client;

import java.util.Scanner;

import utils.User;

public class ChatClient {
	
	static String SERVER_ADDRESS = "172.23.0.85";
	static int SERVER_PORT = 3001;
	static boolean isConnected = false;
	
	public static void main(String[] args){
		Connection con = null;
		
//		System.out.println("Client");
		System.out.println("Please Login");
		while (!isConnected) {
			User user = new User();
			user.login();
			con = new Connection(SERVER_ADDRESS, SERVER_PORT, user);
			con.login();
			
		}
//		con.
		
	}
}
