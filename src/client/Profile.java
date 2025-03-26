package client;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import utils.*;

class Profile {
    private ClientConnection clientConnection;
    private Stage primaryStage;
    private int userID;

    public Profile(ClientConnection clientConnection, Stage primaryStage, int Id) {
        this.clientConnection = clientConnection;
        this.primaryStage = primaryStage;
        this.userID = Id;
    }

    public void start() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        // Back button at the top left
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.TOP_LEFT);
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-size: 14px;");
        backButton.setOnAction(e -> clientConnection.startChatUI());
        topBar.getChildren().add(backButton);
        root.setTop(topBar);
        
        // User details using a GridPane
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(15);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20));
        
        Label usernameLabel = new Label("Username:");
        Label usernameValue = new Label(clientConnection.user.getUsername());
        Button usernameChangeButton = new Button("Change");
        usernameChangeButton.setOnAction(e -> changeUsername());
        
        Label passwordLabel = new Label("Password:");
        Label passwordValue = new Label("");
        Button passwordChangeButton = new Button("Change");
        passwordChangeButton.setOnAction(e -> changePassword());
        
        Label gmailLabel = new Label("Gmail:");
        Label gmailValue = new Label(clientConnection.user.getEmail());
        Button gmailChangeButton = new Button("Change");
        gmailChangeButton.setOnAction(e -> changeGmail());
        
        Label dobLabel = new Label("Date of Birth:");
        Label dobValue = new Label(clientConnection.user.getDob());
        Button dobChangeButton = new Button("Change");
        dobChangeButton.setOnAction(e -> changeDob());
        
        // Styling labels
        usernameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        passwordLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        gmailLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        dobLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        usernameValue.setStyle("-fx-font-size: 16px;");
        passwordValue.setStyle("-fx-font-size: 16px;");
        gmailValue.setStyle("-fx-font-size: 16px;");
        dobValue.setStyle("-fx-font-size: 16px;");
        
        // Adding elements to GridPane
        gridPane.add(usernameLabel, 0, 0);
        gridPane.add(usernameValue, 1, 0);
        gridPane.add(usernameChangeButton, 2, 0);
        
        gridPane.add(passwordLabel, 0, 1);
        gridPane.add(passwordValue, 1, 1);
        gridPane.add(passwordChangeButton, 2, 1);
        
        gridPane.add(gmailLabel, 0, 2);
        gridPane.add(gmailValue, 1, 2);
        gridPane.add(gmailChangeButton, 2, 2);
        
        gridPane.add(dobLabel, 0, 3);
        gridPane.add(dobValue, 1, 3);
        gridPane.add(dobChangeButton, 2, 3);
        
        root.setCenter(gridPane);

        Scene scene = new Scene(root, primaryStage.getWidth(), primaryStage.getHeight());
        primaryStage.setTitle("Profile Page");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    private void changeUsername() {
        System.out.println("Change Username clicked");
    }

    private void changePassword() {
        System.out.println("Change Password clicked");
    }

    private void changeGmail() {
        System.out.println("Change Gmail clicked");
    }

    private void changeDob() {
        System.out.println("Change DOB clicked");
    }
}
