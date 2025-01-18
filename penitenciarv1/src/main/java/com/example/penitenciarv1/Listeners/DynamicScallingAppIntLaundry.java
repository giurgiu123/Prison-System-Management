package com.example.penitenciarv1.Listeners;

import com.example.penitenciarv1.Database.DatabaseConnector;
import com.example.penitenciarv1.Interfaces.PrisonerInterface;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.ResultSet;
import java.sql.Statement;

public class DynamicScallingAppIntLaundry extends Application {

    private String detinutUsername;

    public DynamicScallingAppIntLaundry() {
    }

    public DynamicScallingAppIntLaundry(String detinutUsername) {
        this.detinutUsername = detinutUsername;
    }

    public static class Laundry {
        private final StringProperty camera;
        private final StringProperty scheduleDate;
        private final StringProperty timeSlot;

        public Laundry(String camera, String scheduleDate, String timeSlot) {
            this.camera = new SimpleStringProperty(camera);
            this.scheduleDate = new SimpleStringProperty(scheduleDate);
            this.timeSlot = new SimpleStringProperty(timeSlot);
        }

        public StringProperty cameraProperty() {
            return camera;
        }

        public StringProperty scheduleDateProperty() {
            return scheduleDate;
        }

        public StringProperty timeSlotProperty() {
            return timeSlot;
        }
    }

    @Override
    public void start(Stage primaryStage) {
        VBox layout = new VBox();
        layout.setSpacing(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 20; -fx-background-color: linear-gradient(to bottom, #e8f5e9, #c8e6c9);");

        // Title Label
        Label titleLabel = new Label("Program Spălătorie");
        titleLabel.setFont(Font.font("Arial", 24));
        titleLabel.setTextFill(Color.DARKGREEN);

        // Laundry Table
        TableView<Laundry> laundryTable = new TableView<>();

        // Columns
        TableColumn<Laundry, String> cameraColumn = new TableColumn<>("Camera");
        cameraColumn.setCellValueFactory(cellData -> cellData.getValue().cameraProperty());

        TableColumn<Laundry, String> dateColumn = new TableColumn<>("Data");
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().scheduleDateProperty());

        TableColumn<Laundry, String> timeSlotColumn = new TableColumn<>("Interval Orar");
        timeSlotColumn.setCellValueFactory(cellData -> cellData.getValue().timeSlotProperty());

        // Add columns to the table
        laundryTable.getColumns().addAll(cameraColumn, dateColumn, timeSlotColumn);
        laundryTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Load laundry schedules from database
        loadLaundrySchedules(laundryTable);

        // Row double-click to show details
        laundryTable.setRowFactory(tv -> {
            TableRow<Laundry> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Laundry rowData = row.getItem();
                    showLaundryDetails(rowData);
                }
            });
            return row;
        });

        // Back Button
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: linear-gradient(to right, #66bb6a, #43a047);"
                + "-fx-text-fill: white; -fx-font-size: 16; -fx-padding: 10 20; -fx-background-radius: 20;");
        backButton.setOnMouseEntered(e -> backButton.setStyle("-fx-background-color: linear-gradient(to right, #43a047, #66bb6a);"
                + "-fx-text-fill: white; -fx-font-size: 16; -fx-padding: 10 20; -fx-background-radius: 20;"));
        backButton.setOnMouseExited(e -> backButton.setStyle("-fx-background-color: linear-gradient(to right, #66bb6a, #43a047);"
                + "-fx-text-fill: white; -fx-font-size: 16; -fx-padding: 10 20; -fx-background-radius: 20;"));
        backButton.setOnAction(e -> {
            PrisonerInterface prisonerInterface = new PrisonerInterface();
            Stage newStage = new Stage();
            primaryStage.close();
            try {
                prisonerInterface.start(newStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Add elements to layout
        layout.getChildren().addAll(titleLabel, laundryTable, backButton);

        // Scene setup
        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Program Spălătorie");
        primaryStage.show();
    }

    private void loadLaundrySchedules(TableView<Laundry> laundryTable) {
        DatabaseConnector dbConnector = new DatabaseConnector();
        try (Statement statement = dbConnector.conn.createStatement()) {
            String query = "CALL GetProgramSpalatorieV4('" + detinutUsername + "')";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String camera = resultSet.getString("Camera");            // Camera
                String date = resultSet.getString("ProgramareData");      // Data programării
                String startTime = resultSet.getString("OraInceput");     // Ora de început
                String endTime = resultSet.getString("OraSfarsit");       // Ora de sfârșit

                // Combine start and end times into a single time slot
                String timeSlot = startTime + " - " + endTime;

                // Add the Laundry object to the table
                laundryTable.getItems().add(new Laundry(camera, date, timeSlot));
            }
        } catch (Exception e) {
            showErrorDialog("Error loading laundry schedules",
                    "A problem occurred while loading laundry schedules:\n" + e.getMessage());
        }
    }

    private void showLaundryDetails(Laundry laundry) {
        Stage detailStage = new Stage(StageStyle.UTILITY);
        VBox detailLayout = new VBox();
        detailLayout.setAlignment(Pos.CENTER);
        detailLayout.setSpacing(20);
        detailLayout.setStyle("-fx-padding: 20; -fx-background-color: #ffffff;");

        Label titleLabel = new Label("Detalii Program Spălătorie");
        titleLabel.setFont(Font.font("Arial", 20));
        titleLabel.setTextFill(Color.DARKGREEN);

        Label cameraLabel = new Label("Camera: " + laundry.cameraProperty().get());
        Label dateLabel = new Label("Data: " + laundry.scheduleDateProperty().get());
        Label timeSlotLabel = new Label("Interval Orar: " + laundry.timeSlotProperty().get());

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> detailStage.close());

        detailLayout.getChildren().addAll(titleLabel, cameraLabel, dateLabel, timeSlotLabel, closeButton);

        Scene detailScene = new Scene(detailLayout, 400, 300);
        detailStage.setScene(detailScene);
        detailStage.setTitle("Detalii Program Spălătorie");
        detailStage.show();
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
