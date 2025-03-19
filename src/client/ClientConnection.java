package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;



import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import utils.*;

// HOUR WASTED: 1
public class ClientConnection implements Connection {

    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private User user;
    private Stage primaryStage;
    public ObservableList<Message> messageList;
    private ListView<Message> chatListView;
    public ObservableList<Contact> contactsList;  // ✅ Make sure this is declared
    private ListView<Contact> contactsListView;
    String receiver = "";


    public ClientConnection(String address, int port, Stage primaryStage) throws IOException {
        this.socket = new Socket(address, port);
        this.oos = new ObjectOutputStream(socket.getOutputStream());
        this.ois = new ObjectInputStream(socket.getInputStream());
        this.primaryStage = primaryStage;
        this.messageList = FXCollections.observableArrayList();
        this.contactsList = FXCollections.observableArrayList();  // ✅ Initialize it
    }

    @Override
    public void register(NewUser newuser) throws IOException, ClassNotFoundException {
        oos.writeObject(newuser);
        oos.flush();
        Object response = ois.readObject();
        
        if (response instanceof LoginSuccessResponse) {
            this.user = ((LoginSuccessResponse) response).getUser();
            ChatClient.isAuthenticated = true;
            Platform.runLater(() -> startChatUI());
        } else {
            System.out.println("Account creation failed! Try again.");
        }
    }

    @Override
    public void authenticate(User user) throws IOException, ClassNotFoundException {
        oos.writeObject(user);
        oos.flush();
        Object response = ois.readObject();
        
        if (response instanceof LoginSuccessResponse) {
            this.user = ((LoginSuccessResponse) response).getUser();
            ChatClient.isAuthenticated = true;
            Platform.runLater(() -> startChatUI());
        } else {
            System.out.println("Login failed. Please try again.");
        }
    }

    private void startChatUI() {
        primaryStage.setTitle("Chat App - " + user.getUsername());

        Label receiverName = new Label(receiver);

        // Sidebar for Contacts
        contactsListView = new ListView<>(contactsList);
        contactsListView.setCellFactory(param -> new ListCell<Contact>() {
            @Override
            protected void updateItem(Contact contact, boolean empty) {
                super.updateItem(contact, empty);
                if (empty || contact == null) {
                    setText(null);
                } else {
                    setText(contact.getContact());
                }
            }
        });

        contactsListView.setOnMouseClicked(event -> {
            Contact selectedContact = contactsListView.getSelectionModel().getSelectedItem();
            if (selectedContact != null) {

//            	receiver = selectedContact.getContact();
            	receiverName.setText(selectedContact.getContact());
                openChatSession(selectedContact.getContact());
            }
        });
        
        new Thread(new MessageReceiver(this.ois, this)).start();
      
        
        HBox findUser = new HBox(10);
        
        TextField inputUser = new TextField();
        inputUser.setPromptText("Type username");

        Button findBtn = new Button("Find");
        findBtn.setOnAction(e -> {
        	
        	if (!isContactExist(inputUser.getText())) 
        	{
        		Contact newContact = new Contact(inputUser.getText(), "");
        		addContact(newContact);
        	} 
        	
        	// TODO Create interface for this message
        	else System.out.println("Contact already existed.");
        	
        	
        });
        
        findUser.getChildren().addAll(findBtn, inputUser);

        // Chat Area
        chatListView = new ListView<>(messageList);
        chatListView.setCellFactory(param -> new ListCell<Message>() {
            @Override
            protected void updateItem(Message msg, boolean empty) {
                super.updateItem(msg, empty);
                if (empty || msg == null) {
                    setText(null);
                    setStyle("");

                } else {
                    if (msg.getSender().equals(user.getUsername())) {
                        setText("Me: " + msg.getContent());
                        setAlignment(Pos.CENTER_RIGHT);
                        setStyle("-fx-background-color: #0084ff; -fx-text-fill: white; -fx-padding: 10px;");
                    } else {
                        setText(msg.getSender() + ": " + msg.getContent());
                        setAlignment(Pos.CENTER_LEFT);
                        setStyle("-fx-background-color: #f1f0f0; -fx-text-fill: black; -fx-padding: 10px;");
                    }
                }
            }
        });

        // Message Input Field
        TextField messageField = new TextField();
        messageField.setPromptText("Type a message...");
        Button sendButton = new Button("Send");
        sendButton.setOnAction(e -> {
            String content = messageField.getText();
            if (!content.isEmpty()) {
                Message message = new Message(user.getUsername(), receiver, content);
                try {
                    sendMessage(message, oos);
//                    addMessage(message);

                    messageField.clear();
                } catch (IOException ex) {
                    System.out.println("Error sending message: " + ex.getMessage());
                }
            }
        });

        HBox messageBox = new HBox(10, messageField, sendButton);
        messageBox.setAlignment(Pos.CENTER);

        // Main Layout

        VBox chatLayout = new VBox(10, receiverName, chatListView, messageBox);
        chatLayout.setAlignment(Pos.CENTER);
        chatLayout.setStyle("-fx-background-color: white; -fx-padding: 10px;");
        
        HBox mainLayout = new HBox(10, findUser, contactsListView, chatLayout);

        mainLayout.setStyle("-fx-padding: 20px;");

        Scene chatScene = new Scene(mainLayout, primaryStage.getWidth(), primaryStage.getHeight());
        primaryStage.setScene(chatScene);
        primaryStage.show();
		primaryStage.setMaximized(true);
    }

    private void openChatSession(String receiver) {
        try {

        	this.receiver = receiver;
        	ChatSessionRequest chatSession = new ChatSessionRequest(this.user, receiver);
	        oos.writeObject(chatSession);
	        oos.flush();
	        messageList.clear();

        } catch (IOException e) {
            System.out.println("Error opening chat session: " + e.getMessage());
        }
    }

    public void addContact(Contact contact) {

    	System.out.println("Adding contact: " + contact.getContact());

        Platform.runLater(() -> contactsList.add(contact));  // ✅ Now contactsList is properly initialized
        contactsListView.refresh();

    }

    public void addMessage(Message message) {
        Platform.runLater(() -> messageList.add(message));
    }

    public boolean isContactExist(String contact) {
		
		for (Contact existedContact: contactsList) 
			if (existedContact.getContact().equals(contact)) return true;
		
		return false;
	}

    @Override
    public void sendMessage(Object message, ObjectOutputStream oos) throws IOException {
        oos.writeObject(message);
        oos.flush();
    }

    @Override
    public Object receiveMessage(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        return ois.readObject();
    }
}
