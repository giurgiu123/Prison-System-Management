//package com.example.penitenciarv1.Listeners;
//
//import com.example.penitenciarv1.Database.DatabaseConnector;
//import com.example.penitenciarv1.Interfaces.PrisonerInterface;
//import javafx.application.Application;
//import javafx.beans.property.SimpleStringProperty;
//import javafx.beans.property.StringProperty;
//import javafx.geometry.Pos;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.layout.VBox;
//import javafx.stage.Stage;
//
//import java.sql.ResultSet;
//import java.sql.Statement;
//
//public class DynamicScallingAppIntPrisoner extends Application {
//
//    private String detinutUsername = "ionpopescu123"; // Exemplu pentru testare
//
//    public DynamicScallingAppIntPrisoner() {
//    }
//
//    public DynamicScallingAppIntPrisoner(String detinutUsername) {
//        this.detinutUsername = detinutUsername;
//    }
//
//    public static class Task {
//        private final StringProperty id;
//        private final StringProperty description;
//        private final StringProperty difficulty;
//        private final StringProperty startTime;
//        private final StringProperty endTime;
//
//        public Task(String id, String description, String difficulty, String startTime, String endTime) {
//            this.id = new SimpleStringProperty(id);
//            this.description = new SimpleStringProperty(description);
//            this.difficulty = new SimpleStringProperty(difficulty);
//            this.startTime = new SimpleStringProperty(startTime);
//            this.endTime = new SimpleStringProperty(endTime);
//        }
//
//        public StringProperty idProperty() {
//            return id;
//        }
//
//        public StringProperty descriptionProperty() {
//            return description;
//        }
//
//        public StringProperty difficultyProperty() {
//            return difficulty;
//        }
//
//        public StringProperty startTimeProperty() {
//            return startTime;
//        }
//
//        public StringProperty endTimeProperty() {
//            return endTime;
//        }
//    }
//
//    @Override
//    public void start(Stage primaryStage) {
//        VBox taskLayout = new VBox();
//        taskLayout.setSpacing(20);
//        taskLayout.setAlignment(Pos.CENTER);
//        taskLayout.setStyle("-fx-padding: 20; -fx-background-color: #f5f5f5;");
//
//        // Tabel pentru sarcini
//        TableView<Task> taskTable = new TableView<>();
//
//        // Coloane tabel
//        TableColumn<Task, String> idColumn = new TableColumn<>("ID Task");
//        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
//
//        TableColumn<Task, String> descriptionColumn = new TableColumn<>("Descriere");
//        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
//
//        TableColumn<Task, String> difficultyColumn = new TableColumn<>("Dificultate");
//        difficultyColumn.setCellValueFactory(cellData -> cellData.getValue().difficultyProperty());
//
//        TableColumn<Task, String> startColumn = new TableColumn<>("Început");
//        startColumn.setCellValueFactory(cellData -> cellData.getValue().startTimeProperty());
//
//        TableColumn<Task, String> endColumn = new TableColumn<>("Sfârșit");
//        endColumn.setCellValueFactory(cellData -> cellData.getValue().endTimeProperty());
//
//        // Adăugăm coloane
//        taskTable.getColumns().addAll(idColumn, descriptionColumn, difficultyColumn, startColumn, endColumn);
//        taskTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
//
//        // Încărcare sarcini
//        DatabaseConnector dbConnector = new DatabaseConnector();
//        try (Statement statement = dbConnector.conn.createStatement()) {
//            String query = "CALL GetTaskuriViitoare('" + detinutUsername + "')";
//            ResultSet resultSet = statement.executeQuery(query);
//
//            while (resultSet.next()) {
//                String idTask = resultSet.getString("ID_Task");
//                String description = resultSet.getString("Descriere");
//                String difficulty = resultSet.getString("Dificultate");
//                String startTime = resultSet.getString("Inceput");
//                String endTime = resultSet.getString("Sfarsit");
//
//                taskTable.getItems().add(new Task(idTask, description, difficulty, startTime, endTime));
//            }
//        } catch (Exception e) {
//            System.out.println("Error loading tasks: " + e.getMessage());
//        }
//
//        // Buton pentru revenire
//        Button backButton = new Button("Back");
//        backButton.setOnAction(e -> {
//            PrisonerInterface prisonerInterface = new PrisonerInterface();
//            Stage newStage = new Stage();
//            primaryStage.close();
//            try {
//                prisonerInterface.start(newStage);
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        });
//
//        taskLayout.getChildren().addAll(new Label("Sarcinile Viitoare"), taskTable, backButton);
//
//        // Scenă
//        Scene scene = new Scene(taskLayout, 800, 600);
//        primaryStage.setScene(scene);
//        primaryStage.setTitle("Sarcinile Viitoare");
//        primaryStage.show();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
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

public class DynamicScallingAppIntPrisonerPastTask extends Application {

    private String detinutUsername; // Example username


    public DynamicScallingAppIntPrisonerPastTask() {

    }
    public DynamicScallingAppIntPrisonerPastTask(String detinutUsername) {
        this.detinutUsername = detinutUsername;
    }

    public static class Task {
        private final StringProperty id;
        private final StringProperty description;
        private final StringProperty difficulty;
        private final StringProperty startTime;
        private final StringProperty endTime;

        public Task(String id, String description, String difficulty, String startTime, String endTime) {
            this.id = new SimpleStringProperty(id);
            this.description = new SimpleStringProperty(description);
            this.difficulty = new SimpleStringProperty(difficulty);
            this.startTime = new SimpleStringProperty(startTime);
            this.endTime = new SimpleStringProperty(endTime);
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

        public StringProperty startTimeProperty() {
            return startTime;
        }

        public StringProperty endTimeProperty() {
            return endTime;
        }
    }

    @Override
    public void start(Stage primaryStage) {
        VBox layout = new VBox();
        layout.setSpacing(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 20; -fx-background-color: linear-gradient(to bottom, #e3f2fd, #bbdefb);");


        // Title Label
        Label titleLabel = new Label("Sarcinile Trecute");
        titleLabel.setFont(Font.font("Arial", 24));
        titleLabel.setTextFill(Color.DARKBLUE);

        // Task Table
        TableView<Task> taskTable = new TableView<>();

        // Columns
        TableColumn<Task, String> idColumn = new TableColumn<>("ID Task");
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());

        TableColumn<Task, String> descriptionColumn = new TableColumn<>("Descriere");
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

        TableColumn<Task, String> difficultyColumn = new TableColumn<>("Dificultate");
        difficultyColumn.setCellValueFactory(cellData -> cellData.getValue().difficultyProperty());

        TableColumn<Task, String> startColumn = new TableColumn<>("Început");
        startColumn.setCellValueFactory(cellData -> cellData.getValue().startTimeProperty());

        TableColumn<Task, String> endColumn = new TableColumn<>("Sfârșit");
        endColumn.setCellValueFactory(cellData -> cellData.getValue().endTimeProperty());

        // Add columns to the table
        taskTable.getColumns().addAll(idColumn, descriptionColumn, difficultyColumn, startColumn, endColumn);
        taskTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Load tasks from database
        loadfutureTasks(taskTable);

        // Row double-click to show details
        taskTable.setRowFactory(tv -> {
            TableRow<Task> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Task rowData = row.getItem();
                    showTaskDetails(rowData);
                }
            });
            return row;
        });

        // Back Button
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: linear-gradient(to right, #42a5f5, #1e88e5);"
                + "-fx-text-fill: white; -fx-font-size: 16; -fx-padding: 10 20; -fx-background-radius: 20;");
        backButton.setOnMouseEntered(e -> backButton.setStyle("-fx-background-color: linear-gradient(to right, #1e88e5, #42a5f5);"
                + "-fx-text-fill: white; -fx-font-size: 16; -fx-padding: 10 20; -fx-background-radius: 20;"));
        backButton.setOnMouseExited(e -> backButton.setStyle("-fx-background-color: linear-gradient(to right, #42a5f5, #1e88e5);"
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
        layout.getChildren().addAll(titleLabel, taskTable, backButton);

        // Scene setup
        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Sarcinile din trecut");
        primaryStage.show();
    }

    private void loadfutureTasks(TableView<Task> taskTable) {
        DatabaseConnector dbConnector = new DatabaseConnector();
        try (Statement statement = dbConnector.conn.createStatement()) {
            String query = "CALL GetTaskuriTrecute('" + detinutUsername + "')";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String idTask = resultSet.getString("ID_Task");
                String description = resultSet.getString("Descriere");
                String difficulty = resultSet.getString("Dificultate");
                String startTime = resultSet.getString("Inceput");
                String endTime = resultSet.getString("Sfarsit");

                taskTable.getItems().add(new Task(idTask, description, difficulty, startTime, endTime));
            }
        } catch (Exception e) {
            showErrorDialog("Error loading tasks", "A problem occurred while loading tasks:\n" + e.getMessage());
        }
    }

    private void showTaskDetails(Task task) {
        Stage detailStage = new Stage(StageStyle.UTILITY);
        VBox detailLayout = new VBox();
        detailLayout.setAlignment(Pos.CENTER);
        detailLayout.setSpacing(20);
        detailLayout.setStyle("-fx-padding: 20; -fx-background-color: #ffffff;");

        Label titleLabel = new Label("Detalii Task");
        titleLabel.setFont(Font.font("Arial", 20));
        titleLabel.setTextFill(Color.DARKBLUE);

        Label idLabel = new Label("ID: " + task.idProperty().get());
        Label descriptionLabel = new Label("Descriere: " + task.descriptionProperty().get());
        Label difficultyLabel = new Label("Dificultate: " + task.difficultyProperty().get());
        Label startTimeLabel = new Label("Început: " + task.startTimeProperty().get());
        Label endTimeLabel = new Label("Sfârșit: " + task.endTimeProperty().get());

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> detailStage.close());

        detailLayout.getChildren().addAll(titleLabel, idLabel, descriptionLabel, difficultyLabel, startTimeLabel, endTimeLabel, closeButton);

        Scene detailScene = new Scene(detailLayout, 400, 300);
        detailStage.setScene(detailScene);
        detailStage.setTitle("Detalii Task");
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
