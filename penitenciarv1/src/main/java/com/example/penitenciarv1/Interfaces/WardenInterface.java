package com.example.penitenciarv1.Interfaces;

import com.example.penitenciarv1.Database.DatabaseConnector;
import com.example.penitenciarv1.Entities.User;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WardenInterface extends Application {


    public void start(DatabaseConnector databaseConnector,  Stage primaryStage, User newUser) throws Exception {
        VBox root = new VBox();
        root.setPrefSize(600, 400);
        root.setSpacing(20);

        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-padding: 20; -fx-background-color: linear-gradient(to bottom, #e3f2fd, #bbdefb);");
        primaryStage.setTitle("New JavaFX Interface");
        Scene scene = new Scene(root, 400, 300);
        // Set the Scene on the Stage
        primaryStage.setScene(scene);
        primaryStage.show();

        WardenVizitator vizWarden = new WardenVizitator(databaseConnector, primaryStage, newUser);
        vizWarden.start(primaryStage);

        // Show the Stage

    }

    @Override
    public void start(Stage primaryStage) throws Exception {

    }
}
