package client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import utils.User;

import java.io.IOException;

public interface Connection {
	
    void connect(String address, int port, User user) throws IOException;
    void authenticate(User user, ObjectInputStream ois, ObjectOutputStream oos) throws IOException, ClassNotFoundException;
}