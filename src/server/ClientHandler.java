package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {
	
	public Socket socket;
	public String username;
	
	dbconnection db=new dbconnection();
	boolean isAuthenticated = false;
	String username; 
	String password;

	InputStreamReader isReader;
	BufferedReader bReader;
	PrintWriter pWriter;
	ClientManager cManager;
	
	ClientHandler(Socket socket, ClientManager cManager) {
		
		this.socket = socket;
		this.cManager = cManager;
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
//				pw.println(str);
//				pw.flush();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
			try {
				socket.close();
				pWriter.close();
				bReader.close();
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
		}
		
	}
}