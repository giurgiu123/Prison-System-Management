package com.example.penitenciarv1.Interfaces;

import com.example.penitenciarv1.Database.DatabaseConnector;
import com.example.penitenciarv1.HelloApplication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class InterfataGardian extends Application {

    public void start(Stage primaryStage, DatabaseConnector databaseConnector) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("interfatagardian.fxml"));

            Parent root = fxmlLoader.load();
            //databaseConnector.getInmatesFromDatabase();
            primaryStage.setTitle("Gardian-Meniu principal");
//            Scene scene = new Scene(root);
//            primaryStage.setScene(scene);
//            primaryStage.show();
//          StackPane root2 = new StackPane();
            root.setId("pane");
            Scene scene2 = new Scene(root, 600, 450);
            scene2.getStylesheets().addAll(this.getClass().getResource("demo.css").toExternalForm());

            /// /////////////aici se face un if sau case in functie de shift apelam pentru alta imagine
            changeBackground(scene2, "cazulcorpB");

            primaryStage.setScene(scene2);
            //pentru setare minim si maxim
            primaryStage.setMinWidth(500);
            primaryStage.setMinHeight(500);

            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading FXML file. Ensure the file path is correct and the file exists.");
        }
    }

    static public void changeBackground(Scene scene, String imageName) {
        String imagePath = HelloApplication.class.getResource("images/" + imageName + ".png").toExternalForm();
        scene.getRoot().setStyle(String.format(

                        "-fx-background-image: url('%s'); " +
                        "-fx-background-position: center; " +
                        "-fx-background-repeat: no-repeat;",
                        imagePath

        ));
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

    }
}