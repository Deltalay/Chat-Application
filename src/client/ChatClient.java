package client;

import java.io.IOException;
import java.util.Scanner;

import utils.User;
import utils.NewUser;

public class ChatClient {
	
	static String SERVER_ADDRESS = "172.23.1.122";
	static int SERVER_PORT = 3001;
	static boolean isAuthenticated = false;
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		
		ClientConnection cc = new ClientConnection(SERVER_ADDRESS, SERVER_PORT);
		Scanner scanner = new Scanner(System.in);
		String choice = "";
//		
		System.out.println("Welcome!\nType '/login' to login\nType '/register' to register");
		do {
			if (scanner.hasNextLine()) {
				choice = scanner.nextLine();
			
				if (choice.startsWith("/login")) {
					
					User user = new User();
					cc.authenticate(user);
				} else if (choice.startsWith("/register")) {
					NewUser newUser = new NewUser();
					cc.register(newUser);
				} else {
					System.out.println("Invalid command, please try again");
				}
			}
			
		} while (!isAuthenticated);
		
	}
}
