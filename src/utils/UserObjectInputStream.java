package utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class UserObjectInputStream extends ObjectInputStream {

	User user;
	public UserObjectInputStream(InputStream in) throws IOException {
		super(in);
		// TODO Auto-generated constructor stub
	}
	
	public User readUserObject() throws ClassNotFoundException, IOException {
		Object receivedObject = readObject();
		
		if (receivedObject instanceof User) return (User) receivedObject;
		
		else throw new IOException("Wrong Class Type " + receivedObject.getClass());
	}

}

