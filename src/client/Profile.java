package client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import utils.*;

class Profile extends Application {
    private ClientConnection clientConnection;
    private Stage primaryStage; // Store reference to main stage

    public Profile(ClientConnection clientConnection, Stage primaryStage) {
        this.clientConnection = clientConnection;
        this.primaryStage = primaryStage;
    }

    @Override
    public void start(Stage profileStage) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20px; -fx-alignment: center;");

        Label titleLabel = new Label("Profile Page");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-size: 14px;");
        backButton.setOnAction(e -> clientConnection.startChatUI()); // Navigate back

        layout.getChildren().addAll(titleLabel, backButton);

        Scene scene = new Scene(layout, primaryStage.getWidth(), primaryStage.getHeight()); // Set dimensions
        profileStage.setTitle("Profile Page");
        profileStage.setScene(scene);
        profileStage.setMaximized(true); // Maximize window
        profileStage.show();
    }
}
