package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MessageReceiver implements Runnable {

	private Socket socket;

	public MessageReceiver(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
			
			String line;
			while ((line = in.readLine()) != null) {
				
				if (line.equals("close")) break;
				
				System.out.println(line);
			}
		} catch (IOException e) {
			
//			System.out.println("Error receiving message from server");
		}
	}

}
