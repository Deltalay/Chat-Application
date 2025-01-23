package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientHandler extends Thread {
	
	private Socket socket;
	InputStreamReader isr;
	
	ClientHandler(Socket socket) {
		this.socket = socket;
	}
	
	public void run() {
		
//		System.out.println("Client Connected");
		
		try {
			
			isr = new InputStreamReader(socket.getInputStream());
			BufferedReader br = new BufferedReader(isr);
			
			String username; 
			username = br.readLine();
			System.out.println(username + " Connected");
			
			String str = null;
			while (true) {

				str = br.readLine();
			
//				if (str.equals("close")) System.out.println(username + " is closed");
				
				if (str != null) {
					System.out.println(username + ": " + str);
					str = null;
				}
				
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
