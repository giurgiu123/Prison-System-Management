package com.example.penitenciarv1.Interfaces.popUps;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class NoActionPopUp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        VBox root = new VBox();
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #ff7e5f, #feb47b);");
        root.setSpacing(40);
        root.setAlignment(Pos.CENTER);

        Label label = new Label("No action was performed");
        label.setFont(Font.font("Arial", 24));
        label.setTextFill(Color.WHITE);

        Button button = new Button("OK");
        button.setFont(Font.font("Arial", 18));
        button.setTextFill(Color.WHITE);

        button.setStyle(
                "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-color: linear-gradient(to right, #ff416c, #ff4b2b); " +
                        "-fx-background-radius: 15; " +
                        "-fx-border-radius: 15; " +
                        "-fx-padding: 10 20;"
        );
        button.setOnMouseEntered(el -> button.setStyle(
                "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-color: linear-gradient(to right, #ff4b2b, #ff416c); " +
                        "-fx-background-radius: 15; " +
                        "-fx-border-radius: 15; " +
                        "-fx-padding: 10 20;"
        ));
        button.setOnMouseExited(el -> button.setStyle(
                "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-color: linear-gradient(to right, #ff416c, #ff4b2b); " +
                        "-fx-background-radius: 15; " +
                        "-fx-border-radius: 15; " +
                        "-fx-padding: 10 20;"
        ));

        button.setOnAction(e -> {
           Stage window = (Stage) button.getScene().getWindow();
           window.close();
        });
        root.getChildren().addAll(label, button);


        Scene scene = new Scene(root, 400, 200);
        stage.setScene(scene);
        stage.show();
    }
}
