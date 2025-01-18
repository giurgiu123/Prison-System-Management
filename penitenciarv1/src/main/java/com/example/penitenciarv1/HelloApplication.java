//package com.example.penitenciarv1;
//
//import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//
//import javafx.scene.layout.StackPane;
//import javafx.stage.Stage;
//
//public class HelloApplication extends Application {
//    @Override
//    public void start(Stage primaryStage) {
//        try {
//
//            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
//
//            Parent root = fxmlLoader.load();
//
//
//            primaryStage.setTitle("LogIn");
//            Scene scene = new Scene(root);
//            primaryStage.setScene(scene);
//            primaryStage.show();
//            StackPane root2 = new StackPane();
//            root.setId("pane");
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.err.println("Error loading FXML file. Ensure the file path is correct and the file exists.");
//        }
//    }
//
//    private void changeBackground(Scene scene, String color) {
//        scene.getRoot().setStyle(String.format(
//                "-fx-background-color: %s;" +
//                        "-fx-background-position: center; " +
//                        "-fx-background-repeat: no-repeat;",
//                color));
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
/// /////////////////// /////////////////// /////////////////// /////////////////// /////////////////// /////////////////// /////////////////// /////////////////// /////////////////// ///////////////////
package com.example.penitenciarv1;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Instanțierea Controllerului
        HelloController helloController = new HelloController();

        // Creare layout principal
        VBox root = new VBox(15); // Spacing between elements
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER); // Center all elements
        root.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #142468,#283a8c, #71a3fa);" + // Premium gradient
                        "-fx-border-radius: 20; " +
                        "-fx-background-radius: 20;"
        );

        // Mesaj de bun venit
        Label welcomeLabel = new Label("Welcome! Please log in.");
        welcomeLabel.setStyle(
                "-fx-font-size: 26px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: #ffffff;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0.5, 1, 1);"
        );

        // Câmp pentru username
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                        "-fx-background-radius: 15; " +
                        "-fx-border-radius: 15; " +
                        "-fx-padding: 10; " +
                        "-fx-font-size: 16px;"
        );
        usernameField.setMaxWidth(300); // Fixed width for responsiveness

        // Câmp pentru parolă
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                        "-fx-background-radius: 15; " +
                        "-fx-border-radius: 15; " +
                        "-fx-padding: 10; " +
                        "-fx-font-size: 16px;"
        );
        passwordField.setMaxWidth(300);

        // Setăm câmpurile în HelloController
        helloController.setUsrTxt(usernameField);
        helloController.setPwTxt(passwordField);

        // Buton de login
        Button loginButton = new Button("Login");
        loginButton.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-color: linear-gradient(to right, #6a11cb, #2575fc);" +
                        "-fx-border-radius: 15;" +
                        "-fx-background-radius: 15;" +
                        "-fx-padding: 10 30;"
        );
        loginButton.setOnMouseEntered(e -> loginButton.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-color: linear-gradient(to right, #2575fc, #6a11cb);" +
                        "-fx-border-radius: 15;" +
                        "-fx-background-radius: 15;" +
                        "-fx-padding: 10 30;"
        ));
        loginButton.setOnMouseExited(e -> loginButton.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-color: linear-gradient(to right, #6a11cb, #2575fc);" +
                        "-fx-border-radius: 15;" +
                        "-fx-background-radius: 15;" +
                        "-fx-padding: 10 30;"
        ));
        loginButton.setOnAction(e -> {
            try {
                helloController.login_btn_onClick(e); // Apelează logica din controller
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        // Asociere acțiuni "Enter"
        usernameField.setOnAction(e -> {
            try {
                helloController.login_btn_onClick(e);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        passwordField.setOnAction(e -> {
            try {
                helloController.login_btn_onClick(e);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        VBox.setVgrow(usernameField, Priority.ALWAYS);
        VBox.setVgrow(passwordField, Priority.ALWAYS);
        root.getChildren().addAll(welcomeLabel, usernameField, passwordField, loginButton);

        Scene scene = new Scene(root, 450, 400); // Slightly larger window
        primaryStage.setScene(scene);
        primaryStage.setTitle("Premium Login Interface");
        primaryStage.setMinWidth(400);
        primaryStage.setMinHeight(300);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
/////////////////// /////////////////// /////////////////// /////////////////// /////////////////// /////////////////// /////////////////// /////////////////// /////////////////// /////////////////// ///////////////////