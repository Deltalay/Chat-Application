package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientHandler {
	
	private Socket socket;
	
	InputStreamReader isr;
	
	ClientHandler(Socket socket) {
		this.socket = socket;
	}
	
	public void run() {
		
		System.out.println("Client Connected");
		
		try {
			
			isr = new InputStreamReader(socket.getInputStream());
			BufferedReader br = new BufferedReader(isr);
			
			String str = br.readLine();
			System.out.println("Client: " + str);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
