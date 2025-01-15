package com.example.penitenciarv1.Listeners;

import com.example.penitenciarv1.Database.DatabaseConnector;
import com.example.penitenciarv1.Interfaces.PrisonerInterface;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public  class DynamicScalingAppDailySchedule extends Application {

    private String detinutUsername;

    public DynamicScalingAppDailySchedule() {
    }
    // metoda pentru set username
    public void setDetaineeUsername(String username) {
        this.detinutUsername = username;
    }


    public DynamicScalingAppDailySchedule(String detinutUsername) {
        this.detinutUsername = detinutUsername;
    }
    public Node getContent() {
        // Layout principal
        VBox layout = new VBox();
        layout.setSpacing(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 20; -fx-background-color: linear-gradient(to bottom, #e3f2fd, #bbdefb);");

        // Titlu
        Label titleLabel = new Label("Program Zilnic");
        titleLabel.setFont(Font.font("Arial", 24));
        titleLabel.setTextFill(Color.DARKBLUE);

        // Tabel pentru programul zilnic
        TableView<ScheduleItem> dailyScheduleTable = new TableView<>();

        // Definirea coloanelor
        TableColumn<ScheduleItem, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());

        TableColumn<ScheduleItem, String> descriptionColumn = new TableColumn<>("Descriere");
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

        TableColumn<ScheduleItem, String> difficultyColumn = new TableColumn<>("Dificultate");
        difficultyColumn.setCellValueFactory(cellData -> cellData.getValue().difficultyProperty());

        TableColumn<ScheduleItem, String> startDateColumn = new TableColumn<>("Data Început");
        startDateColumn.setCellValueFactory(cellData -> cellData.getValue().startDateProperty());

        TableColumn<ScheduleItem, String> endDateColumn = new TableColumn<>("Data Sfârșit");
        endDateColumn.setCellValueFactory(cellData -> cellData.getValue().endDateProperty());

        // Adăugarea coloanelor în tabel
        dailyScheduleTable.getColumns().addAll(idColumn, descriptionColumn, difficultyColumn, startDateColumn, endDateColumn);
        dailyScheduleTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Încărcarea datelor în tabel
        loadDailySchedule(dailyScheduleTable);

        // Adăugarea elementelor în layout
        layout.getChildren().addAll(titleLabel, dailyScheduleTable);

        // Returnăm layout-ul principal ca nod
        return layout;
    }


    public static class ScheduleItem {
        private final StringProperty id;
        private final StringProperty description;
        private final StringProperty difficulty;
        private final StringProperty startDate;
        private final StringProperty endDate;

        public ScheduleItem(String id, String description, String difficulty, String startDate, String endDate) {
            this.id = new SimpleStringProperty(id);
            this.description = new SimpleStringProperty(description);
            this.difficulty = new SimpleStringProperty(difficulty);
            this.startDate = new SimpleStringProperty(startDate);
            this.endDate = new SimpleStringProperty(endDate);
        }

        public StringProperty idProperty() {
            return id;
        }

        public StringProperty descriptionProperty() {
            return description;
        }

        public StringProperty difficultyProperty() {
            return difficulty;
        }

        public StringProperty startDateProperty() {
            return startDate;
        }

        public StringProperty endDateProperty() {
            return endDate;
        }
    }

    @Override
    public void start(Stage primaryStage) {
        VBox layout = new VBox();
        layout.setSpacing(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 20; -fx-background-color: linear-gradient(to bottom, #e3f2fd, #bbdefb);");

        // Title Label
        Label titleLabel = new Label("Program Zilnic");
        titleLabel.setFont(Font.font("Arial", 24));
        titleLabel.setTextFill(Color.DARKBLUE);

        // Daily Schedule Table
        TableView<ScheduleItem> dailyScheduleTable = new TableView<>();

        // Columns
        TableColumn<ScheduleItem, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());

        TableColumn<ScheduleItem, String> descriptionColumn = new TableColumn<>("Descriere");
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

        TableColumn<ScheduleItem, String> difficultyColumn = new TableColumn<>("Dificultate");
        difficultyColumn.setCellValueFactory(cellData -> cellData.getValue().difficultyProperty());

        TableColumn<ScheduleItem, String> startDateColumn = new TableColumn<>("Data Început");
        startDateColumn.setCellValueFactory(cellData -> cellData.getValue().startDateProperty());

        TableColumn<ScheduleItem, String> endDateColumn = new TableColumn<>("Data Sfârșit");
        endDateColumn.setCellValueFactory(cellData -> cellData.getValue().endDateProperty());

        // Add columns to the table
        dailyScheduleTable.getColumns().addAll(idColumn, descriptionColumn, difficultyColumn, startDateColumn, endDateColumn);
        dailyScheduleTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Load daily schedule from database
        loadDailySchedule(dailyScheduleTable);

        // Back Button
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: linear-gradient(to right, #42a5f5, #1e88e5);"
                + "-fx-text-fill: white; -fx-font-size: 16; -fx-padding: 10 20; -fx-background-radius: 20;");
        backButton.setOnMouseEntered(e -> backButton.setStyle("-fx-background-color: linear-gradient(to right, #1e88e5, #42a5f5);"
                + "-fx-text-fill: white; -fx-font-size: 16; -fx-padding: 10 20; -fx-background-radius: 20;"));
        backButton.setOnMouseExited(e -> backButton.setStyle("-fx-background-color: linear-gradient(to right, #42a5f5, #1e88e5);"
                + "-fx-text-fill: white; -fx-font-size: 16; -fx-padding: 10 20; -fx-background-radius: 20;"));


        backButton.setOnAction(e -> {
            backButtonFuction(primaryStage);
        });

        // Add elements to layout
        layout.getChildren().addAll(titleLabel, dailyScheduleTable, backButton);

        // Scene setup
        Scene scene = new Scene(layout, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Program Zilnic");
        primaryStage.show();
    }
    public void backButtonFuction(Stage primaryStage) {
        PrisonerInterface prisonerInterface = new PrisonerInterface();
        Stage newStage = new Stage();
        primaryStage.close();
        try {
            prisonerInterface.start(newStage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void loadDailySchedule(TableView<ScheduleItem> dailyScheduleTable) {
        DatabaseConnector dbConnector = new DatabaseConnector();
        try (Statement statement = dbConnector.conn.createStatement()) {
            // Query for daily schedule
            String query = "CALL GetDailyScheduleByUsernameV2('" + detinutUsername + "')";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String id = resultSet.getString("ID_Task");            // ID Task
                String description = resultSet.getString("Descriere"); // Descriere Task
                String difficulty = resultSet.getString("Dificultate"); // Dificultate Task
                String startDate = resultSet.getString("DataInceput"); // Data Început
                String endDate = resultSet.getString("DataSfarsit");   // Data Sfârșit

                // Add the item to the table
                dailyScheduleTable.getItems().add(new ScheduleItem(id, description, difficulty, startDate, endDate));
            }
        } catch (Exception e) {
            showErrorDialog("Error loading daily schedule",
                    "A problem occurred while loading the daily schedule:\n" + e.getMessage());
        }
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
