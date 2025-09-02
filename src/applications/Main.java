package applications;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main application class for EduVault Academic Management System
 * This class initializes the JavaFX application and loads the login screen
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the login FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Parent root = loader.load();
            
            // Set up the scene
            Scene scene = new Scene(root);
            
            // Configure and show the primary stage
            primaryStage.setScene(scene);
            primaryStage.setTitle("EduVault - Login");
            primaryStage.setMinWidth(500);
            primaryStage.setMinHeight(600);
            primaryStage.show();
        } catch (Exception e) {
            System.err.println("Failed to start application: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}