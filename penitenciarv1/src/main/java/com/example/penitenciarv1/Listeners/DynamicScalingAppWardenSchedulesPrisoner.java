package com.example.penitenciarv1.Listeners;

import com.example.penitenciarv1.Database.DatabaseConnector;
import com.example.penitenciarv1.Interfaces.WardenDashboard;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.Statement;

public class DynamicScalingAppWardenSchedulesPrisoner extends Application {


    public static class Detinut {
        private final StringProperty id;
        private final StringProperty nume;

        public Detinut(String id, String nume) {
            this.id = new SimpleStringProperty(id);
            this.nume = new SimpleStringProperty(nume);
        }

        public StringProperty idProperty() {
            return id;
        }

        public StringProperty numeProperty() {
            return nume;
        }
    }

    public AnchorPane getContent() {
        ObservableList<Detinut> observableList = FXCollections.observableArrayList();
        FilteredList<Detinut> filteredData = new FilteredList<>(observableList, p -> true);

        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        TextField searchBar = new TextField();
        searchBar.setPromptText("Căutați după nume...");
        searchBar.setFont(Font.font("Arial", 14));
        searchBar.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #cccccc; -fx-border-radius: 10; -fx-padding: 10; -fx-font-size: 14px;");

        TableView<Detinut> table = new TableView<>();
        table.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-radius: 10; -fx-padding: 10; -fx-box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);");

        TableColumn<Detinut, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        idColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");

        TableColumn<Detinut, String> numeColumn = new TableColumn<>("Nume");
        numeColumn.setCellValueFactory(cellData -> cellData.getValue().numeProperty());
        numeColumn.setStyle("-fx-alignment: CENTER-LEFT; -fx-font-size: 14px;");

        TableColumn<Detinut, Void> actionColumnDetails = new TableColumn<>("Acțiuni");
        actionColumnDetails.setStyle("-fx-alignment: CENTER;");
        actionColumnDetails.setCellFactory(param -> new TableCell<>() {
            private final Button actionButton = new Button("Vezi detalii");

            {
                actionButton.setOnAction(event -> {
                    Detinut detinut = getTableView().getItems().get(getIndex());
                    showDetails(detinut);
                });
                actionButton.setStyle("-fx-background-color: #5cb85c; -fx-text-fill: white; -fx-border-radius: 5; -fx-padding: 5; -fx-font-size: 13px;");
                actionButton.setOnMouseEntered(e -> actionButton.setStyle("-fx-background-color: #4cae4c; -fx-text-fill: white;"));
                actionButton.setOnMouseExited(e -> actionButton.setStyle("-fx-background-color: #5cb85c; -fx-text-fill: white;"));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(actionButton);
                }
            }
        });

        TableColumn<Detinut, Void> actionColumnSchedule = new TableColumn<>("Program");
        actionColumnSchedule.setStyle("-fx-alignment: CENTER;");
        actionColumnSchedule.setCellFactory(param -> new TableCell<>() {
            private final Button actionButton = new Button("Vezi program");

            {
                actionButton.setOnAction(event -> {
                    Detinut detinut = getTableView().getItems().get(getIndex());
                    openDailySchedule(detinut);
                });
                actionButton.setStyle("-fx-background-color: #0275d8; -fx-text-fill: white; -fx-border-radius: 5; -fx-padding: 5; -fx-font-size: 13px;");
                actionButton.setOnMouseEntered(e -> actionButton.setStyle("-fx-background-color: #025aa5; -fx-text-fill: white;"));
                actionButton.setOnMouseExited(e -> actionButton.setStyle("-fx-background-color: #0275d8; -fx-text-fill: white;"));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(actionButton);
                }
            }
        });

        table.getColumns().addAll(idColumn, numeColumn, actionColumnDetails, actionColumnSchedule);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        loadPrisoners(observableList);
        table.setItems(filteredData);

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(detinut -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return detinut.numeProperty().get().toLowerCase().contains(lowerCaseFilter);
            });
        });

        Label titleLabel = new Label("Lista Deținuților");
        titleLabel.setFont(Font.font("Arial", 24));
        titleLabel.setTextFill(Color.DARKSLATEGRAY);

        root.getChildren().addAll(titleLabel, searchBar, table);

        // Returnează VBox-ul într-un AnchorPane
        AnchorPane contentPane = new AnchorPane(root);
        AnchorPane.setTopAnchor(root, 0.0);
        AnchorPane.setBottomAnchor(root, 0.0);
        AnchorPane.setLeftAnchor(root, 0.0);
        AnchorPane.setRightAnchor(root, 0.0);
        return contentPane;

    }

    @Override
    public void start(Stage primaryStage) {
        ObservableList<Detinut> observableList = FXCollections.observableArrayList();
        FilteredList<Detinut> filteredData = new FilteredList<>(observableList, p -> true);

        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        // Premium background with gradient
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #ffffff, #e6e9f0); -fx-border-color: #a3a3a3; -fx-border-radius: 10; -fx-border-width: 1; -fx-padding: 15;");

        TextField searchBar = new TextField();
        searchBar.setPromptText("Căutați după nume...");
        searchBar.setFont(Font.font("Arial", 14));
        searchBar.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #cccccc; -fx-border-radius: 10; -fx-padding: 10; -fx-font-size: 14px;");

        TableView<Detinut> table = new TableView<>();
        table.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-radius: 10; -fx-padding: 10; -fx-box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);");

        TableColumn<Detinut, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        idColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");

        TableColumn<Detinut, String> numeColumn = new TableColumn<>("Nume");
        numeColumn.setCellValueFactory(cellData -> cellData.getValue().numeProperty());
        numeColumn.setStyle("-fx-alignment: CENTER-LEFT; -fx-font-size: 14px;");

        TableColumn<Detinut, Void> actionColumnDetails = new TableColumn<>("Acțiuni");
        actionColumnDetails.setStyle("-fx-alignment: CENTER;");
        actionColumnDetails.setCellFactory(param -> new TableCell<>() {
            private final Button actionButton = new Button("Vezi detalii");

            {
                actionButton.setOnAction(event -> {
                    Detinut detinut = getTableView().getItems().get(getIndex());
                    showDetails(detinut);
                });
                actionButton.setStyle("-fx-background-color: #5cb85c; -fx-text-fill: white; -fx-border-radius: 5; -fx-padding: 5; -fx-font-size: 13px;");
                actionButton.setOnMouseEntered(e -> actionButton.setStyle("-fx-background-color: #4cae4c; -fx-text-fill: white;"));
                actionButton.setOnMouseExited(e -> actionButton.setStyle("-fx-background-color: #5cb85c; -fx-text-fill: white;"));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(actionButton);
                }
            }
        });

        TableColumn<Detinut, Void> actionColumnSchedule = new TableColumn<>("Program");
        actionColumnSchedule.setStyle("-fx-alignment: CENTER;");
        actionColumnSchedule.setCellFactory(param -> new TableCell<>() {
            private final Button actionButton = new Button("Vezi program");

            {
                actionButton.setOnAction(event -> {
                    Detinut detinut = getTableView().getItems().get(getIndex());
                    openDailySchedule(detinut);
                });
                actionButton.setStyle("-fx-background-color: #0275d8; -fx-text-fill: white; -fx-border-radius: 5; -fx-padding: 5; -fx-font-size: 13px;");
                actionButton.setOnMouseEntered(e -> actionButton.setStyle("-fx-background-color: #025aa5; -fx-text-fill: white;"));
                actionButton.setOnMouseExited(e -> actionButton.setStyle("-fx-background-color: #0275d8; -fx-text-fill: white;"));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(actionButton);
                }
            }
        });

        table.getColumns().addAll(idColumn, numeColumn, actionColumnDetails, actionColumnSchedule);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        loadPrisoners(observableList);
        table.setItems(filteredData);

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(detinut -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return detinut.numeProperty().get().toLowerCase().contains(lowerCaseFilter);
            });
        });

        Label titleLabel = new Label("Lista Deținuților");
        titleLabel.setFont(Font.font("Arial", 24));
        titleLabel.setTextFill(Color.DARKSLATEGRAY);

        root.getChildren().addAll(titleLabel, searchBar, table);

        Scene scene = new Scene(root, 950, 750);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Deținuți - Dynamic Scaling App Premium");
        primaryStage.show();
    }

    private void loadPrisoners(ObservableList<Detinut> observableList) {
        DatabaseConnector dbConnector = new DatabaseConnector();
        try (Statement statement = dbConnector.conn.createStatement()) {
            String query = "SELECT * FROM Detinut";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String id = String.valueOf(resultSet.getInt("id_detinut"));
                String nume = resultSet.getString("nume");
                observableList.add(new Detinut(id, nume));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDetails(Detinut detinut) {
        DatabaseConnector dbConnector = new DatabaseConnector();
        try (Statement statement = dbConnector.conn.createStatement()) {
            String query = "SELECT * FROM Detinut WHERE id_detinut = " + detinut.idProperty().get();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                String id = resultSet.getString("id_detinut");
                String nume = resultSet.getString("nume");
                String profesie = resultSet.getString("profesie");
                String fkCelula = resultSet.getString("fk_id_celula");
                String fkUtilizator = resultSet.getString("fk_id_utilizator");

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Detalii Deținut");
                alert.setHeaderText("Informații despre deținut");
                alert.setContentText(
                        "ID: " + id + "\n" +
                                "Nume: " + nume + "\n" +
                                "Profesie: " + profesie + "\n" +
                                "ID Celula: " + fkCelula + "\n" +
                                "ID Utilizator: " + fkUtilizator
                );
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openDailySchedule(Detinut detinut) {
        DatabaseConnector dbConnector = new DatabaseConnector();
        String username = "";
        try (Statement statement = dbConnector.conn.createStatement()) {
            String query = "SELECT u.username " +
                    "FROM Detinut d " +
                    "JOIN Utilizator u ON d.fk_id_utilizator = u.id_utilizator " +
                    "WHERE d.nume = '" + detinut.numeProperty().get() + "'";
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                username = resultSet.getString("username");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!username.isEmpty()) {
            try {
                System.out.println("opening schedule pt username: " + username);
                WardenSchedulreViewer dailyScheduleApp = new WardenSchedulreViewer();
                dailyScheduleApp.setDetaineeUsername(username); // Set the detainee's username
                dailyScheduleApp.start(new Stage()); // Launch in a new stage
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Eroare");
            alert.setHeaderText("Deținut neidentificat");
            alert.setContentText("Nu s-a găsit niciun utilizator asociat cu acest deținut.");
            alert.showAndWait();
        }
    }



    public static void main(String[] args) {
        launch(args);
    }
}
