package client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import utils.User;

import java.io.IOException;

public interface Connection {
	
    void authenticate(User user, ObjectInputStream ois, ObjectOutputStream oos) throws IOException, ClassNotFoundException;
    void sendMessage(Object message, ObjectOutputStream oos) throws IOException;
    Object receiveMessage(ObjectInputStream ois) throws IOException, ClassNotFoundException;
}