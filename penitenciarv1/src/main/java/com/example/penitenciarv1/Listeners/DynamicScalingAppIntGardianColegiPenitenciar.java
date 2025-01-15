package com.example.penitenciarv1.Listeners;

import com.example.penitenciarv1.Database.DatabaseConnector;
import com.example.penitenciarv1.Entities.Guardian;
import com.example.penitenciarv1.Interfaces.GuardianInterface;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.Statement;
import java.util.ArrayList;

public class DynamicScalingAppIntGardianColegiPenitenciar extends Application {

    private int idGardian;

    public DynamicScalingAppIntGardianColegiPenitenciar() {

    }
    public AnchorPane getContentPane(Stage primaryStage, VBox contentArea) {
        AnchorPane pane = new AnchorPane();
        VBox root = new VBox();
        root.prefWidthProperty().bind(Bindings.min(contentArea.widthProperty(), 1000));
        root.prefWidthProperty().bind(contentArea.widthProperty());
        root.prefHeightProperty().bind(contentArea.heightProperty());
       System.out.println(contentArea.getWidth());
        startTheEngine(root, primaryStage);
        pane.getChildren().add(root);
        AnchorPane.setTopAnchor(root, 0.0);
        AnchorPane.setBottomAnchor(root, 0.0);
        AnchorPane.setLeftAnchor(root, 0.0);
        AnchorPane.setRightAnchor(root, 0.0);
        return pane;
    }
    public DynamicScalingAppIntGardianColegiPenitenciar(int id) {
        this.idGardian = id;
    }
    public void backButtonSetter(Stage primaryStage, VBox root) {
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: linear-gradient(to right, #42a5f5, #1e88e5);"
                + "-fx-text-fill: white; -fx-font-size: 16; -fx-padding: 10 20; -fx-background-radius: 20;");
        backButton.setOnMouseEntered(e -> backButton.setStyle("-fx-background-color: linear-gradient(to right, #1e88e5, #42a5f5);"
                + "-fx-text-fill: white; -fx-font-size: 16; -fx-padding: 10 20; -fx-background-radius: 20;"));
        backButton.setOnMouseExited(e -> backButton.setStyle("-fx-background-color: linear-gradient(to right, #42a5f5, #1e88e5);"
                + "-fx-text-fill: white; -fx-font-size: 16; -fx-padding: 10 20; -fx-background-radius: 20;"));

        backButton.setOnAction(e -> {
            GuardianInterface gin = new GuardianInterface(idGardian);
            Stage stage = new Stage();
            primaryStage.close();
            gin.start(stage);
        });
        root.getChildren().add(backButton);
    }
    public void startTheEngine(VBox root, Stage primaryStage) {

        root.setSpacing(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-padding: 20; -fx-background-color: linear-gradient(to bottom, #e3f2fd, #bbdefb);");

        //Titlu
        Label titleLabel = new Label("Coleagues from whole Prison");
        titleLabel.setFont(Font.font("Arial", 24));
        titleLabel.setTextFill(Color.DARKBLUE);

        //Tabel pt vizualizare colegi
        TableView<Guardian> coleaguesTable = new TableView<>();

        //Coloanele
        TableColumn<Guardian, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(guardian -> guardian.getValue().getId());

        TableColumn<Guardian, String> nameCol = new TableColumn<>("Username");
        nameCol.setCellValueFactory(guardian -> guardian.getValue().getUsername());

        TableColumn<Guardian, String> floorCol = new TableColumn<>("Floor");
        floorCol.setCellValueFactory(guardian -> guardian.getValue().getFloor());

        TableColumn<Guardian, String> blockCol = new TableColumn<>("Block");
        blockCol.setCellValueFactory(guardian -> guardian.getValue().getDetentionBlock());

        coleaguesTable.getColumns().addAll(idCol, nameCol, floorCol, blockCol);
        coleaguesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        coleaguesTable.prefHeightProperty().bind(root.heightProperty().multiply(0.8));
        coleaguesTable.prefWidthProperty().bind(root.widthProperty());
        //load guardians from db
        loadColeaguesWholePrison(coleaguesTable);

        //back button

        root.getChildren().addAll(titleLabel, coleaguesTable);
        backButtonSetter(primaryStage, root);

        //scene layout


    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        //VBox principal
        VBox root = new VBox();
        startTheEngine(root, primaryStage);
        Scene scene = new Scene(root, 1400, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Coleagues from whole Prison");
        primaryStage.show();

    }

    private void loadColeaguesWholePrison(TableView<Guardian> table) {
        DatabaseConnector dbConnector = new DatabaseConnector();
        int idGuardian = dbConnector.getGuardianId(idGardian);
        try(Statement statement = dbConnector.conn.createStatement()){
            ArrayList<Guardian> guardians = dbConnector.getGuardianColleaguesWholePrison(idGuardian);
            if(guardians == null){
                System.out.println("Error: Guardians is null");
            }else {
                for(Guardian g : guardians){
                    System.out.println(g.getUsername());
                    table.getItems().add(g);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public AnchorPane getContent() {
        AnchorPane newContentPane = new AnchorPane();

        return newContentPane;
    }
}
//package com.example.penitenciarv1.Listeners;
//
//import com.example.penitenciarv1.Database.DatabaseConnector;
//import com.example.penitenciarv1.Entities.Guardian;
//import com.example.penitenciarv1.Interfaces.GuardianInterface;
//import javafx.application.Application;
//import javafx.beans.binding.Bindings;
//import javafx.geometry.Pos;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableView;
//import javafx.scene.layout.AnchorPane;
//import javafx.scene.layout.VBox;
//import javafx.scene.paint.Color;
//import javafx.scene.text.Font;
//import javafx.stage.Stage;
//
//import java.sql.Statement;
//import java.util.ArrayList;
//
//public class DynamicScalingAppIntGardianColegiPenitenciar extends Application {
//
//    private int idGardian;
//
//    public DynamicScalingAppIntGardianColegiPenitenciar() {
//    }
//
//    public DynamicScalingAppIntGardianColegiPenitenciar(int id) {
//        this.idGardian = id;
//    }
//
//    /**
//     * Creates and returns an autosizable AnchorPane to embed in a parent container.
//     */
//    public AnchorPane getContentPane(Stage primaryStage, VBox contentArea) {
//        AnchorPane pane = new AnchorPane();
//        VBox root = new VBox();
//        root.setAlignment(Pos.CENTER);
//        root.setSpacing(20);
//
//        // Bind the size of the root VBox to the content area's size for autosizing
//        root.prefWidthProperty().bind(contentArea.widthProperty());
//        root.prefHeightProperty().bind(contentArea.heightProperty());
//
//        startTheEngine(root, primaryStage);
//        pane.getChildren().add(root);
//
//        // Ensure the AnchorPane resizes with its parent
//        AnchorPane.setTopAnchor(root, 0.0);
//        AnchorPane.setBottomAnchor(root, 0.0);
//        AnchorPane.setLeftAnchor(root, 0.0);
//        AnchorPane.setRightAnchor(root, 0.0);
//
//        return pane;
//    }
//
//    /**
//     * Sets up the main interface components and binds them for autosizing.
//     */
//    public void startTheEngine(VBox root, Stage primaryStage) {
//        root.setStyle("-fx-padding: 20; -fx-background-color: linear-gradient(to bottom, #e3f2fd, #bbdefb);");
//
//        // Title Label
//        Label titleLabel = new Label("Colleagues from Whole Prison");
//        titleLabel.setFont(Font.font("Arial", 24));
//        titleLabel.setTextFill(Color.DARKBLUE);
//
//        // Table for Colleagues
//        TableView<Guardian> colleaguesTable = new TableView<>();
//        colleaguesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
//        colleaguesTable.prefHeightProperty().bind(root.heightProperty().multiply(0.8));
//        colleaguesTable.prefWidthProperty().bind(root.widthProperty());
//
//        // Columns
//        TableColumn<Guardian, String> idCol = new TableColumn<>("ID");
//        idCol.setCellValueFactory(guardian -> guardian.getValue().getId());
//
//        TableColumn<Guardian, String> nameCol = new TableColumn<>("Username");
//        nameCol.setCellValueFactory(guardian -> guardian.getValue().getUsername());
//
//        TableColumn<Guardian, String> floorCol = new TableColumn<>("Floor");
//        floorCol.setCellValueFactory(guardian -> guardian.getValue().getFloor());
//
//        TableColumn<Guardian, String> blockCol = new TableColumn<>("Block");
//        blockCol.setCellValueFactory(guardian -> guardian.getValue().getDetentionBlock());
//
//        colleaguesTable.getColumns().addAll(idCol, nameCol, floorCol, blockCol);
//
//        // Load data into the table
//        loadColleaguesWholePrison(colleaguesTable);
//
//        // Add Back Button
//        backButtonSetter(primaryStage, root);
//
//        // Add all elements to the root
//        root.getChildren().addAll(titleLabel, colleaguesTable);
//    }
//
//    /**
//     * Adds a back button to the interface.
//     */
//    public void backButtonSetter(Stage primaryStage, VBox root) {
//        Button backButton = new Button("Back");
//        backButton.setStyle("-fx-background-color: linear-gradient(to right, #42a5f5, #1e88e5);"
//                + "-fx-text-fill: white; -fx-font-size: 16; -fx-padding: 10 20; -fx-background-radius: 20;");
//        backButton.setOnMouseEntered(e -> backButton.setStyle("-fx-background-color: linear-gradient(to right, #1e88e5, #42a5f5);"
//                + "-fx-text-fill: white; -fx-font-size: 16; -fx-padding: 10 20; -fx-background-radius: 20;"));
//        backButton.setOnMouseExited(e -> backButton.setStyle("-fx-background-color: linear-gradient(to right, #42a5f5, #1e88e5);"
//                + "-fx-text-fill: white; -fx-font-size: 16; -fx-padding: 10 20; -fx-background-radius: 20;"));
//
//        backButton.setOnAction(e -> {
//            GuardianInterface gin = new GuardianInterface(idGardian);
//            Stage stage = new Stage();
//            primaryStage.close();
//            gin.start(stage);
//        });
//        root.getChildren().add(backButton);
//    }
//
//    /**
//     * Loads all colleagues from the database into the table.
//     */
//    private void loadColleaguesWholePrison(TableView<Guardian> table) {
//        DatabaseConnector dbConnector = new DatabaseConnector();
//        int idGuardian = dbConnector.getGuardianId(idGardian);
//        try (Statement statement = dbConnector.conn.createStatement()) {
//            ArrayList<Guardian> guardians = dbConnector.getGuardianColleaguesWholePrison(idGuardian);
//            if (guardians == null) {
//                System.out.println("Error: Guardians is null");
//            } else {
//                for (Guardian g : guardians) {
//                    table.getItems().add(g);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Start method for standalone testing.
//     */
//    @Override
//    public void start(Stage primaryStage) {
//        VBox root = new VBox();
//        startTheEngine(root, primaryStage);
//
//        Scene scene = new Scene(root, 1400, 600);
//        primaryStage.setScene(scene);
//        primaryStage.setTitle("Colleagues from Whole Prison");
//        primaryStage.show();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}