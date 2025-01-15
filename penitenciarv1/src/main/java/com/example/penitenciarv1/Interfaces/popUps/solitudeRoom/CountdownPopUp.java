package com.example.penitenciarv1.Interfaces.popUps.solitudeRoom;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CountdownPopUp extends Application {

    private String targetTime;

    public CountdownPopUp(String targetTime) {
        this.targetTime = targetTime;
    }

    @Override
    public void start(Stage stage) throws Exception {
        VBox root = new VBox();
        root.setSpacing(40);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #ff7e5f, #feb47b);");
        root.setAlignment(Pos.CENTER);

        Label label = new Label("Sorry, this Solitude Room is Full!");
        label.setFont(Font.font("Arial", 20));
        label.setTextFill(Color.WHITE);

        Label countdownLabel = new Label();
        countdownLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14");

        Timeline timeline = new Timeline(
            new KeyFrame(javafx.util.Duration.seconds(1), event -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime targetTime;
                try {
                    targetTime = LocalDateTime.parse(this.targetTime, formatter);
                } catch (Exception e) {
                    countdownLabel.setText("Error parsing time");
                    return;
                }
                LocalDateTime now = LocalDateTime.now();

                Duration duration = Duration.between(now, targetTime);

                if(duration.isNegative() || duration.isZero()) {
                    countdownLabel.setText("Time's up! You can add new inmate!");
                }else {
                    //extrag orele, min si sec
                    long hours = duration.toHours();
                    long minutes = duration.toMinutes() % 60;
                    long seconds = duration.toSeconds() % 60;
                    countdownLabel.setText(String.format("Time remaining: %02d:%02d:%02d", hours, minutes, seconds));
                }
            })
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        Button backButton = new Button("Back");
        backButton.setStyle(
                "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-color: linear-gradient(to right, #ff416c, #ff4b2b); " +
                        "-fx-background-radius: 15; " +
                        "-fx-border-radius: 15; " +
                        "-fx-padding: 10 20;"
        );
        backButton.setOnMouseEntered(el -> backButton.setStyle(
                "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-color: linear-gradient(to right, #ff4b2b, #ff416c); " +
                        "-fx-background-radius: 15; " +
                        "-fx-border-radius: 15; " +
                        "-fx-padding: 10 20;"
        ));
        backButton.setOnMouseExited(el -> backButton.setStyle(
                "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-color: linear-gradient(to right, #ff416c, #ff4b2b); " +
                        "-fx-background-radius: 15; " +
                        "-fx-border-radius: 15; " +
                        "-fx-padding: 10 20;"
        ));

        backButton.setOnAction(event -> {
            stage.close();
        });

        root.getChildren().addAll(label, countdownLabel, backButton);

        Scene scene = new Scene(root, 400, 350);
        stage.setScene(scene);
        stage.setTitle("Solitude Room");
        stage.show();











    }
}
