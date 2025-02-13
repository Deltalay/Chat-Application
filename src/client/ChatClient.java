package client;

import java.io.IOException;
import java.util.Scanner;

import utils.User;

public class ChatClient {
	
	static String SERVER_ADDRESS = "172.23.2.163";
	static int SERVER_PORT = 3001;
	static boolean isConnected = false;
	
	public static void main(String[] args) throws IOException {
		
//		System.out.println("Client");
		
		User user = new User();
		new ClientConnection().connect(SERVER_ADDRESS, SERVER_PORT, user);;
		
	}
}
