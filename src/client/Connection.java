package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Connection {
	private String address;
	private int port;
	
	Connection(String address, int port) {
		this.address = address;
		this.port = port;
	}
	
	void run() {
		try (Socket socket = new Socket(address, port)) {
			PrintWriter pw = new PrintWriter(socket.getOutputStream());
			
			pw.println("This message is sent from a client");
			pw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
