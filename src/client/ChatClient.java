package client;

import java.io.IOException;
import java.util.Scanner;

import utils.User;
import utils.NewUser;

public class ChatClient {
	
	static String SERVER_ADDRESS = "172.23.1.209";
	static int SERVER_PORT = 3001;
	static boolean isAuthenticated = false;
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		
		ClientConnection cc = new ClientConnection(SERVER_ADDRESS, SERVER_PORT);
		Scanner scanner = new Scanner(System.in);
//		
		System.out.println("Welcome bro!\n/login to login\n/register to register");
		do {
			String choice = scanner.nextLine();
			if (choice.startsWith("/login")) {
				
				User user = new User();
				cc.authenticate(user);
			} else if (choice.startsWith("/register")) {
				NewUser newUser = new NewUser();
				cc.Register(newUser);
			} else {
				System.out.println("dak command ort trv dak mdong tt");
			}
			
		} while (!isAuthenticated);
	}
}
