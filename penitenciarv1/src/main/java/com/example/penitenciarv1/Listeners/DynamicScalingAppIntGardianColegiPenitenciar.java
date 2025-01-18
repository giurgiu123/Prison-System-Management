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
    private String username;

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

    public DynamicScalingAppIntGardianColegiPenitenciar(int idGardian) {
        this.idGardian = idGardian;
    }

    public DynamicScalingAppIntGardianColegiPenitenciar(int id, String username) {
        this.idGardian = id;
        this.username = username;
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
            GuardianInterface gin = new GuardianInterface(idGardian, username);
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
