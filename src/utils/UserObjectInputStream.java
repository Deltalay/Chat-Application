package utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class UserObjectInputStream extends ObjectInputStream {
;
	public UserObjectInputStream(InputStream in) throws IOException {
		super(in);
		// TODO Auto-generated constructor stub
	}
	
	public Object readUserObject() throws ClassNotFoundException, IOException {
		Object receivedObject = readObject();
		
		if (receivedObject instanceof User || receivedObject instanceof NewUser) return receivedObject;
		
		else throw new IOException("Wrong Class Type " + receivedObject.getClass());
	}

}