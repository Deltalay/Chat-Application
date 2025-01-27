package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {
	
	private Socket socket;
	InputStreamReader isr;
	dbconnection db=new dbconnection();
	boolean isAuthenticated = false;
	String username; 
	String password;
	
	ClientHandler(Socket socket) {
		this.socket = socket;
	}
	
	public void run() {
		
//		System.out.println("Client Connected");
		
		try {
			isr = new InputStreamReader(socket.getInputStream());
			BufferedReader br = new BufferedReader(isr);
			PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
			
			while(!isAuthenticated) {
				pw.println("Enter your username");
				username = br.readLine();
				
				pw.println("Enter your password");
				password = br.readLine();
				db.run(username,password);
				isAuthenticated = db.run(username, password);
				if (isAuthenticated) {
                    pw.println("Login successful!");
                } else {
                    pw.println("Invalid username or password. Please try again.");
                }
			}
			System.out.println(username + " Connected to server ");
			String str = null;
			
//			while (true) {
//
//				str = br.readLine();
//			
////				if (str.equals("close")) System.out.println(username + " is closed");
//				
//				if (str != null) {
//					System.out.println(username + ": " + str);
//					pw.println(str);
//					pw.flush();
//					str = null;
//				}
//				
//				
//			}
			
			while ((str = br.readLine()) != null) {
                if (str.equals("close")) {
                    System.out.println(username + " is disconnected");
                    break;
                }

                ChatServer.broadcastMessages(str, username);
                System.out.println(username + ": " + str);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}