package utils;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class UserObjectOutputStream extends ObjectOutputStream {
	
	public UserObjectOutputStream (OutputStream out) throws IOException {
		super(out);
	}
	
	public void WriteUserObject(Object user) throws IOException {
		
		if (user instanceof User || user instanceof NewUser) writeObject(user);
		else throw new IOException("Unexpected object: " + user.getClass());
	
	}
}
