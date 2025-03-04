package utils;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public interface Connection {
	
    void authenticate(NewUser user) throws IOException, ClassNotFoundException;
    void register(NewUser newuser) throws IOException, ClassNotFoundException;
    void sendMessage(Object message, ObjectOutputStream oos) throws IOException;
    Object receiveMessage(ObjectInputStream ois) throws IOException, ClassNotFoundException;
}