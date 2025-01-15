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

public class DynamicScallingAppPrisonerVisit extends Application {

    private String detinutUsername;

    public DynamicScallingAppPrisonerVisit() {
    }

    public DynamicScallingAppPrisonerVisit(String detinutUsername) {
        this.detinutUsername = detinutUsername;
    }

    public static class Visitor {
        private final StringProperty name;
        private final StringProperty visitStart;
        private final StringProperty visitEnd;

        public Visitor(String name, String visitStart, String visitEnd) {
            this.name = new SimpleStringProperty(name);
            this.visitStart = new SimpleStringProperty(visitStart);
            this.visitEnd = new SimpleStringProperty(visitEnd);
        }

        public StringProperty nameProperty() {
            return name;
        }

        public StringProperty visitStartProperty() {
            return visitStart;
        }

        public StringProperty visitEndProperty() {
            return visitEnd;
        }
    }

    @Override
    public void start(Stage primaryStage) {
        VBox layout = new VBox();
        layout.setSpacing(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 20; -fx-background-color: linear-gradient(to bottom, #ffebee, #ffcdd2);");

        // Title Label
        Label titleLabel = new Label("Vizitatori");
        titleLabel.setFont(Font.font("Arial", 24));
        titleLabel.setTextFill(Color.DARKRED);

        // Visitor Table
        TableView<Visitor> visitorTable = new TableView<>();

        // Columns
        TableColumn<Visitor, String> nameColumn = new TableColumn<>("Nume Vizitator");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        TableColumn<Visitor, String> visitStartColumn = new TableColumn<>("Început Vizită");
        visitStartColumn.setCellValueFactory(cellData -> cellData.getValue().visitStartProperty());

        TableColumn<Visitor, String> visitEndColumn = new TableColumn<>("Sfârșit Vizită");
        visitEndColumn.setCellValueFactory(cellData -> cellData.getValue().visitEndProperty());

        // Add columns to the table
        visitorTable.getColumns().addAll(nameColumn, visitStartColumn, visitEndColumn);
        visitorTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Load visitors from database
        loadVisitors(visitorTable);

        // Row double-click to show details
        visitorTable.setRowFactory(tv -> {
            TableRow<Visitor> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Visitor rowData = row.getItem();
                    showVisitorDetails(rowData);
                }
            });
            return row;
        });

        // Back Button
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: linear-gradient(to right, #e57373, #f44336);"
                + "-fx-text-fill: white; -fx-font-size: 16; -fx-padding: 10 20; -fx-background-radius: 20;");
        backButton.setOnMouseEntered(e -> backButton.setStyle("-fx-background-color: linear-gradient(to right, #f44336, #e57373);"
                + "-fx-text-fill: white; -fx-font-size: 16; -fx-padding: 10 20; -fx-background-radius: 20;"));
        backButton.setOnMouseExited(e -> backButton.setStyle("-fx-background-color: linear-gradient(to right, #e57373, #f44336);"
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
        layout.getChildren().addAll(titleLabel, visitorTable, backButton);

        // Scene setup
        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Vizitatori");
        primaryStage.show();
    }

    private void loadVisitors(TableView<Visitor> visitorTable) {
        DatabaseConnector dbConnector = new DatabaseConnector();
        try (Statement statement = dbConnector.conn.createStatement()) {
       //     String query = "CALL GetVisitorScheduleByUsername('\" + detinutUsername + \"')";
            String query = "CALL GetVisitorScheduleByUsernamePerfUPDATED1('" + detinutUsername + "')";


            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String name = resultSet.getString("VizitatorNume");         // Nume Vizitator
                String visitStart = resultSet.getString("DataInceputVizita");  // Început Vizită
                String visitEnd = resultSet.getString("DataSfarsitVizita");    // Sfârșit Vizită

                // Add the Visitor object to the table
                visitorTable.getItems().add(new Visitor(name, visitStart, visitEnd));
            }
        } catch (Exception e) {
            showErrorDialog("Error loading visitors",
                    "A problem occurred while loading visitor data:\n" + e.getMessage());
        }
    }

    private void showVisitorDetails(Visitor visitor) {
        Stage detailStage = new Stage(StageStyle.UTILITY);
        VBox detailLayout = new VBox();
        detailLayout.setAlignment(Pos.CENTER);
        detailLayout.setSpacing(20);
        detailLayout.setStyle("-fx-padding: 20; -fx-background-color: #ffffff;");

        Label titleLabel = new Label("Detalii Vizitator");
        titleLabel.setFont(Font.font("Arial", 20));
        titleLabel.setTextFill(Color.DARKRED);

        Label nameLabel = new Label("Nume Vizitator: " + visitor.nameProperty().get());
        Label visitStartLabel = new Label("Început Vizită: " + visitor.visitStartProperty().get());
        Label visitEndLabel = new Label("Sfârșit Vizită: " + visitor.visitEndProperty().get());

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> detailStage.close());

        detailLayout.getChildren().addAll(titleLabel, nameLabel, visitStartLabel, visitEndLabel, closeButton);

        Scene detailScene = new Scene(detailLayout, 400, 300);
        detailStage.setScene(detailScene);
        detailStage.setTitle("Detalii Vizitator");
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

