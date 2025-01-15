package com.example.penitenciarv1.Listeners;

import com.example.penitenciarv1.Database.DatabaseConnector;
import com.example.penitenciarv1.Entities.Guardian;
import com.example.penitenciarv1.Entities.Inmates;
import com.example.penitenciarv1.Interfaces.GuardianInterface;
import com.example.penitenciarv1.Interfaces.popUps.newCell.AddToNewCellPopUp;
import com.example.penitenciarv1.Interfaces.popUps.solitudeRoom.AddToSolitudePopUp;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.Statement;
import java.util.ArrayList;


public class DynamicScalingAppIntGardianDetinut extends Application {

    private int idUserGardian;

    public DynamicScalingAppIntGardianDetinut() {

    }
    public DynamicScalingAppIntGardianDetinut(int idUserGardian) {
        this.idUserGardian = idUserGardian;
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

    public void startTheEngine(VBox root, Stage primaryStage) {
        root.setSpacing(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-padding: 20; -fx-background-color: linear-gradient(to bottom, #e3f2fd, #bbdefb);");

        //Titlu
        Label titleLabel = new Label("Prisoners on Shift");
        titleLabel.setFont(Font.font("Arial", 24));
        titleLabel.setTextFill(Color.DARKBLUE);


        // Configurare Tabel
        TableView<Inmates> tableView = new TableView<>();

        // Coloane
        TableColumn<Inmates, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(inmate -> inmate.getValue().getid());

        TableColumn<Inmates, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(inmate -> inmate.getValue().getName());

        TableColumn<Inmates, String> cellColumn = new TableColumn<>("Cell");
        cellColumn.setCellValueFactory(inmate -> inmate.getValue().getIdCelula());

        TableColumn<Inmates, String> sentenceColumn = new TableColumn<>("Sentence");
        sentenceColumn.setCellValueFactory(inmate -> inmate.getValue().getSentenceRemained());

        TableColumn<Inmates, String> professionColumn = new TableColumn<>("Profession");
        professionColumn.setCellValueFactory(inmate -> inmate.getValue().getProfession());


        TableColumn<Inmates, String> col6 = new TableColumn<>("Actions");
        col6.setCellFactory(param -> new TableCell<Inmates, String>() {
            final Button addToSolitude = new Button("Add to Solitude");
            final Button cancelVisit = new Button("Cancel Visit");
            final Button manageTask = new Button("Manage Tasks");
            final Button moveToAnotherCell = new Button("Move To Another Cell");

            // HBox ca sa tina mai multe butoane
            final HBox buttonContainer = new HBox(10);  // Horizontal box with 10px spacing
            {
                buttonContainer.getChildren().addAll(addToSolitude, cancelVisit, manageTask, moveToAnotherCell);
                buttonContainer.setSpacing(5);  // Optional spacing between buttons
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    addToSolitude.setOnAction(event -> {
                        Inmates inmate = getTableRow().getItem();
                        if (inmate != null) {
                            AddToSolitudePopUp newPopUp = new AddToSolitudePopUp(inmate.getid().get(), idUserGardian);
                            Stage stage = new Stage();
                            try {
                                newPopUp.start(stage);
                            }catch (Exception e) {
                                e.printStackTrace();
                            }
                            primaryStage.close();
                        }
                    });

                    cancelVisit.setOnAction(event -> {
                        Inmates inmate = getTableRow().getItem();
                        if (inmate != null) {
                            DynamicScallingAppIntVisitsForAnInmate newWindow = new DynamicScallingAppIntVisitsForAnInmate(inmate.getid().get(), idUserGardian, inmate.getName().get());
                            Stage stage = new Stage();
                            try{
                                newWindow.start(stage);
                            }catch (Exception e) {
                                e.printStackTrace();
                            }
                            primaryStage.close();
                        }
                    });
                    setGraphic(buttonContainer);
                    setText(null);

                    manageTask.setOnAction(event-> {
                        Inmates inmate = getTableRow().getItem();
                        if(inmate != null) {
                            DynamicScallingAppIntManageTasks newWindow = new DynamicScallingAppIntManageTasks(idUserGardian, inmate.getid().get());
                            Stage stage = new Stage();
                            try{
                                newWindow.start(stage);
                            }catch (Exception e) {
                                e.printStackTrace();
                            }
                            primaryStage.close();
                        }
                    });

                    moveToAnotherCell.setOnAction(event -> {
                        Inmates inmate = getTableRow().getItem();
                        if (inmate != null) {
                            AddToNewCellPopUp popUp = new AddToNewCellPopUp(inmate.getid().get(), idUserGardian);
                            Stage stage = new Stage();
                            try {
                                popUp.start(stage);
                            }catch (Exception e) {
                                e.printStackTrace();
                            }
                            primaryStage.close();
                        }
                    });
                }
            }
        });

        tableView.getColumns().addAll(idColumn, nameColumn, cellColumn, sentenceColumn, professionColumn, col6);


        // Încărcare date din baza de date
        loadInmatesOnShift(tableView);


        // Autosize: Listener pentru ajustarea automată a lățimii coloanelor
        tableView.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            double totalWidth = newWidth.doubleValue();
            idColumn.setPrefWidth(totalWidth * 0.10);
            nameColumn.setPrefWidth(totalWidth * 0.15);
            cellColumn.setPrefWidth(totalWidth * 0.20);
            sentenceColumn.setPrefWidth(totalWidth * 0.10);
            professionColumn.setPrefWidth(totalWidth * 0.15);
            col6.setPrefWidth(totalWidth * 0.30);
        });







        root.getChildren().addAll(titleLabel, tableView);
        //scene layout
        backButtonSettere(primaryStage,root);

    }
    public void backButtonSettere(Stage primaryStage, VBox root) {
        Button goBack = new Button("Go Back");
        goBack.setStyle("-fx-background-color: linear-gradient(to right, #42a5f5, #1e88e5);"
                + "-fx-text-fill: white; -fx-font-size: 16; -fx-padding: 10 20; -fx-background-radius: 20;");
        goBack.setOnMouseEntered(e -> goBack.setStyle("-fx-background-color: linear-gradient(to right, #1e88e5, #42a5f5);"
                + "-fx-text-fill: white; -fx-font-size: 16; -fx-padding: 10 20; -fx-background-radius: 20;"));
        goBack.setOnMouseExited(e -> goBack.setStyle("-fx-background-color: linear-gradient(to right, #42a5f5, #1e88e5);"
                + "-fx-text-fill: white; -fx-font-size: 16; -fx-padding: 10 20; -fx-background-radius: 20;"));

        goBack.setOnAction(event -> {
            GuardianInterface gi = new GuardianInterface(idUserGardian);
            Stage newStage = new Stage();
            primaryStage.close();
            gi.start(newStage);
        });
        goBack.setAlignment(Pos.TOP_LEFT);
        goBack.setPrefHeight(20);
        root.getChildren().add(goBack);

    }
        @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();
        startTheEngine(root, primaryStage);

        // Scenă
        Scene scene = new Scene(root, 1500, 600);
        primaryStage.setTitle("Tabel cu Deținuți");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadInmatesOnShift(TableView<Inmates> tableView) {
        DatabaseConnector dbConnector = new DatabaseConnector();
        int idGardian = dbConnector.getGuardianId(idUserGardian);
        try(Statement statement = dbConnector.conn.createStatement()){
            ArrayList<Inmates> inmates = dbConnector.getInmatesOnShift(idGardian);
            if(inmates.isEmpty()){
                System.out.println("No inmates found");
            }else {
                for(Inmates inmate : inmates){
                    tableView.getItems().add(inmate);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
