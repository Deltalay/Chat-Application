package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import utils.*;

// HOUR WASTED: 1
public class ClientConnection implements Connection {

    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    public User user;
    private Stage primaryStage;
	public ObservableList<Message> messageList;
    public ListView<Message> chatListView;
    public ObservableList<Contact> contactsList;
    private ListView<Contact> contactsListView;
    String receiver = "";


    public ClientConnection(String address, int port, Stage primaryStage) throws IOException {
        this.socket = new Socket(address, port);
        this.oos = new ObjectOutputStream(socket.getOutputStream());
        this.ois = new ObjectInputStream(socket.getInputStream());
        this.primaryStage = primaryStage;
        this.messageList = FXCollections.observableArrayList();
        this.contactsList = FXCollections.observableArrayList();
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

    public void startChatUI() {
        primaryStage.setTitle("Chat App - " + user.getUsername());

        HBox mainLayout = new HBox(0);
        VBox chatLayout = new VBox(0);
        
        Label receiverName = new Label(receiver);
        receiverName.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        // Sidebar for Contacts
        contactsListView = new ListView<>(contactsList);
        contactsListView.setStyle("-fx-border-width: 0; -fx-background-color: transparent;");
        VBox.setVgrow(contactsListView, Priority.ALWAYS);
        contactsListView.setCellFactory(param -> new ListCell<Contact>() {
            @Override
            protected void updateItem(Contact contact, boolean empty) {
                super.updateItem(contact, empty);
                
                if (empty || contact == null) {
                    setText(null);
                    setStyle("-fx-background-color: #FEFEFE");
                    
                } else {
                    setStyle("-fx-background-color: #FEFEFE; -fx-text-fill: #212121");
                    
                    Label nameLabel = new Label(contact.getContact());
                    Label lastMessageLabel;
                    
                    nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #212121");
                    
                    if (isSelected()) setStyle("-fx-background-color: #EFEFEF");

                    else setStyle("-fx-background-color: #FEFEFE; ");
                    
                    if (contact.isSender()) {
                    	
                        lastMessageLabel = new Label("Me: " + contact.getMessage());
                    } else {
                    	
                        lastMessageLabel = new Label(contact.getContact() + ": " + contact.getMessage());
                    }
                    
                    // TODO: Get actual last message
                    lastMessageLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: normal; -fx-text-fill: gray;");
                    
                    Circle circle = new Circle(20, Color.web("#ECECEE"));
                     
                    circle.setStrokeWidth(1);                    

                    GridPane contactBox = new GridPane(); 
                    contactBox.setHgap(10);
                    contactBox.setPadding(new Insets(8, 0, 8, 6));
                    
                    contactBox.add(circle, 0, 0);
                    GridPane.setRowSpan(circle, 2);
                    contactBox.add(nameLabel, 1, 0);
                    contactBox.add(lastMessageLabel, 1, 1);

                    setGraphic(contactBox);
                    
                    setOnMouseEntered(event -> {
                    	if (!isSelected()) setStyle("-fx-background-color: #FAFAFA; -fx-text-fill: #212121;");
                    });

                    setOnMouseExited(event -> {
                        if (!isSelected()) setStyle("-fx-background-color: #FEFEFE; -fx-text-fill: #212121;");
                    });
                }
            }
        });

        contactsListView.setOnMouseClicked(event -> {
            Contact selectedContact = contactsListView.getSelectionModel().getSelectedItem();
            if (selectedContact != null) {
            	
            	if (this.receiver.isBlank() && mainLayout.getChildren().contains(chatLayout)) {
                	mainLayout.getChildren().remove(chatLayout);
                } else if (!this.receiver.isBlank() && !mainLayout.getChildren().contains(chatLayout)) 
                	mainLayout.getChildren().add(chatLayout);

            	if (!this.receiver.equals(selectedContact.getContact())) {
            		receiverName.setText(selectedContact.getContact());
                	openChatSession(selectedContact.getContact());
                }
            }
        });
        
        new Thread(new MessageReceiver(this.ois, this)).start();
        Label contactError = new Label();
        contactError.setStyle("-fx-text-fill: red;");
        
        HBox findUser = new HBox(10);
        
        TextField inputUser = new TextField();
        inputUser.setPromptText("Type username");

        Button findBtn = new Button("Find");
        
        findBtn.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5px 10px; -fx-background-radius: 10;");
        
        findUser.getChildren().addAll(inputUser, findBtn);
        
        Button viewProfileButton = new Button("View Profile");
        viewProfileButton.setStyle(
            "-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-size: 14px; " +
            "-fx-padding: 8px; -fx-background-radius: 100; -fx-min-width: 40px; " + 
            "-fx-min-height: 24px; -fx-margin: 16px;"
        );
        
        VBox.setMargin(viewProfileButton, new Insets(0, 0, 0, 15));
        
        viewProfileButton.setOnAction(e -> {
            Profile profilePage = new Profile(this,primaryStage);
            profilePage.start(primaryStage);
        });
        
        Label findContactSectionHeader = new Label("Find New Contact");
        findContactSectionHeader.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        VBox findContactSection = new VBox(5, findContactSectionHeader, findUser);
        findContactSection.setPadding(new Insets(10, 0, 0, 15));
        
        findBtn.setOnAction(e -> {
        	contactError.setText("");
        	
        	Contact newContact = new Contact(inputUser.getText(), "");
        	boolean isContactExist = false;
        	
        	for (Contact contact: contactsList) {
        		if (contact.getContact().equals(newContact.getContact())) {
        			contactError.setText("You already had contact with " + newContact.getContact());
        			findContactSection.getChildren().add(contactError);
        			isContactExist = true;
        			break;
        		}
        	}
        	
        	if (isContactExist) System.out.println(newContact.getContact()); // Display Error
        	else {
        		try {
        			oos.writeObject(newContact);
        			oos.flush();
        		} catch (IOException e2) {
        			System.out.println("1" + e2.getMessage());
        		}
        	}
        	
        	// TODO Create interface for this message
//        	else System.out.println("Contact already existed.");
        	
        	
        });
        
        VBox sidebar = new VBox(5, viewProfileButton, findContactSection, contactsListView);
        sidebar.setPadding(new Insets(15, 0, 15, 0));
        sidebar.setBorder(new Border(new BorderStroke(
        		Color.web("#ebebeb"), 
                BorderStrokeStyle.SOLID, 
                null, 
                new BorderWidths(0, 1, 0, 0)
        )));

        // Chat Area
        chatListView = new ListView<>(messageList);
        chatListView.setStyle("-fx-border-width: 0; -fx-background-color: transparent;");
        VBox.setVgrow(chatListView, Priority.ALWAYS);
        chatListView.setCellFactory(param -> new ListCell<Message>() {
            @Override
            protected void updateItem(Message msg, boolean empty) {
                super.updateItem(msg, empty);
                if (empty || msg == null) {
                    setText(null);
                    setStyle("-fx-background-color: #F4F6FA");
                    setGraphic(null);

                } else {
                	setStyle("-fx-background-color: #F4F6FA; -fx-text-fill: black; -fx-width: 300px");
                	Text chatText = new Text();
                	VBox messageBox = new VBox(chatText);
                	messageBox.setPrefHeight(50);
                	messageBox.setPrefWidth(50);
                	messageBox.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                	
                	HBox chatBox = new HBox(messageBox);
                	
                	
                	chatBox.setPadding(new Insets(8));
//                	chatBox.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                	setGraphic(chatBox);
                	
                    if (msg.getSender().equals(user.getUsername())) {
                        chatText.setText("Me: " + msg.getContent());
                        chatBox.setAlignment(Pos.CENTER_RIGHT);

                    } else {
                        chatText.setText(msg.getSender() + ": " + msg.getContent());
                        chatBox.setAlignment(Pos.CENTER_LEFT);
                    }
                }
            }
        });
        
        TextField messageField = new TextField();
        messageField.setPromptText("Type a message...");
        Button sendButton = new Button("Send");
        messageField.setPrefWidth(300);
        sendButton.setOnAction(e -> {
            String content = messageField.getText();
            if (!content.isEmpty()) {
                Message message = new Message(user.getUsername(), receiver, content);
                try {
                    sendMessage(message, oos);
                    addMessage(message);
                    LocalDateTime now = LocalDateTime.now();

	                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	                String formattedNow = now.format(formatter);
	                System.out.println(formattedNow);
	                System.out.println("Total Contact: " + contactsList.size());
	                
	                messageField.clear();
                } catch (IOException ex) {
                    System.out.println("Error sending message: " + ex.getMessage());
                }
            }
        });

        HBox messageBox = new HBox(10, messageField, sendButton);
        messageBox.setAlignment(Pos.CENTER);     
        messageBox.setPrefHeight(50);
        
        HBox chatHeader = new HBox(receiverName);
        chatHeader.setPrefHeight(50);
        chatHeader.setPadding(new Insets(16));
        chatHeader.setAlignment(Pos.CENTER_LEFT); 
        chatHeader.setBorder(new Border(new BorderStroke(
        		Color.web("#ebebeb"), 
                BorderStrokeStyle.SOLID, 
                null, 
                new BorderWidths(0, 0, 1, 0)
        )));
        HBox.setHgrow(chatHeader, Priority.ALWAYS);
        
        chatLayout.getChildren().addAll(chatHeader, chatListView, messageBox);
        chatLayout.setAlignment(Pos.CENTER);
        HBox.setHgrow(chatLayout, Priority.ALWAYS);
        
        mainLayout.getChildren().add(sidebar);
        mainLayout.setStyle("-fx-background-color: white;");

        Scene chatScene = new Scene(mainLayout, primaryStage.getWidth() - 14, primaryStage.getHeight() - 40);
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

        } catch (IOException e) {
            System.out.println("Error opening chat session: " + e.getMessage());
        }
    }

    public void addContact(Contact contact) {

    	System.out.println("Adding contact: " + contact.getContact());

        Platform.runLater(() -> contactsList.add(contact));
        contactsListView.refresh();

    }

    public void addMessage(Message message) {
        Platform.runLater(() -> this.messageList.add(message));
    }
    
    public void addMessageHistory(List<Message> chatHistory) {
    	ObservableList<Message> observableNewMessages = FXCollections.observableArrayList(chatHistory);
    	Platform.runLater(() -> {
    	    messageList.setAll(observableNewMessages);
    	});
    }
    
    public void updateContactList(int index, Contact contact) {
    	Platform.runLater(() -> contactsList.set(index, contact));
//    	Platform.runLater(() -> contactsList.get(index).setLastMessage(contact.getMessage()));
//    	contactsListView.refresh();
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
