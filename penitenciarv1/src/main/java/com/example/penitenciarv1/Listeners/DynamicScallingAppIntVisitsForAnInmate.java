package com.example.penitenciarv1.Listeners;

import com.example.penitenciarv1.Database.DatabaseConnector;
import com.example.penitenciarv1.Entities.Visit;
import com.example.penitenciarv1.Interfaces.GuardianInterface;
import com.example.penitenciarv1.Interfaces.popUps.cancelVisit.CancelVisitPopUp;
import com.example.penitenciarv1.Services.WrapperClassArrayListAndInt;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.Statement;
import java.util.ArrayList;

public class DynamicScallingAppIntVisitsForAnInmate extends Application {

    private int idUserGuardian;
    private String idInmate;
    private String inmateName;
    private String guardianUserName;

    public DynamicScallingAppIntVisitsForAnInmate(String idInmate, int idUserGuardian, String inmateName, String guardianUserName) {
        this.idUserGuardian = idUserGuardian;
        this.idInmate = idInmate;
        this.inmateName = inmateName;
        this.guardianUserName = guardianUserName;
    }

    public DynamicScallingAppIntVisitsForAnInmate() {

    }

    DatabaseConnector dbConn = new DatabaseConnector();

    @Override
    public void start(Stage stage) throws Exception {
        VBox vbox = new VBox();
        vbox.setSpacing(20);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-padding: 20; -fx-background-color: linear-gradient(to bottom, #e3f2fd, #bbdefb);");

        Label title = new Label("Visits");
        title.setFont(Font.font("Arial", 24));
        title.setTextFill(Color.DARKBLUE);

        TableView<Visit> visits = new TableView<>();

        int [] visitsIds = loadVisits(visits);

        //Coloane
        TableColumn<Visit, String> visitorName = new TableColumn<>("Visitor Name");
        visitorName.setCellValueFactory(visit -> visit.getValue().getInmateName());

        TableColumn<Visit, String> startDate = new TableColumn<>("Start Date");
        startDate.setCellValueFactory(visit -> visit.getValue().getStartTime());

        TableColumn<Visit, String> endDate = new TableColumn<>("End Date");
        endDate.setCellValueFactory(visit -> visit.getValue().getEndTime());

        TableColumn<Visit, String> action = new TableColumn<>("Action");
        action.setCellFactory(param -> new TableCell<Visit, String>(){
            final Button cancelVisit = new Button("Cancel");

            @Override
            protected void updateItem(String s, boolean empty) {
                super.updateItem(s, empty);
                if(empty){
                    setGraphic(null);
                    setText(null);
                }else{
                    cancelVisit.setOnAction(event-> {
                        Visit visit = getTableRow().getItem();
                        int rowIndex = getTableRow().getIndex();
                        if(visit != null){
                            getTableView().getItems().remove(visit);
                            dbConn.deleteVisit(visitsIds[rowIndex]);
                            Stage newStage = new Stage();
                            //getInmateName aici are visitor name
                            //am refolosit clasa de Visit
                            CancelVisitPopUp newPopUp = new CancelVisitPopUp(visit.getInmateName(), inmateName);
                            try {
                                newPopUp.start(newStage);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                    setGraphic(cancelVisit);
                    setText(null);
                }

            }
        });

        //adaugare coloane in tabel
        visits.getColumns().addAll(visitorName, startDate, endDate, action);


        visits.widthProperty().addListener((observable, oldValue, newValue) -> {
           double totalWidth = newValue.doubleValue();
           visitorName.setPrefWidth(totalWidth*0.25);
           startDate.setPrefWidth(totalWidth*0.30);
           endDate.setPrefWidth(totalWidth*0.30);
           action.setPrefWidth(totalWidth*0.15);
        });

        Button goBack = new Button("Go Back");
        goBack.setStyle("-fx-background-color: linear-gradient(to right, #42a5f5, #1e88e5);"
                + "-fx-text-fill: white; -fx-font-size: 16; -fx-padding: 10 20; -fx-background-radius: 20;");
        goBack.setOnMouseEntered(e -> goBack.setStyle("-fx-background-color: linear-gradient(to right, #1e88e5, #42a5f5);"
                + "-fx-text-fill: white; -fx-font-size: 16; -fx-padding: 10 20; -fx-background-radius: 20;"));
        goBack.setOnMouseExited(e -> goBack.setStyle("-fx-background-color: linear-gradient(to right, #42a5f5, #1e88e5);"
                + "-fx-text-fill: white; -fx-font-size: 16; -fx-padding: 10 20; -fx-background-radius: 20;"));

        goBack.setOnAction(event -> {
            DynamicScalingAppIntGardianDetinut newScene = new DynamicScalingAppIntGardianDetinut(idUserGuardian, guardianUserName);
            Stage newStage = new Stage();
            stage.close();
            newScene.start(newStage);
        });
        goBack.setAlignment(Pos.TOP_LEFT);
        goBack.setPrefHeight(20);

        vbox.getChildren().addAll(title, visits, goBack);

        //Scena
        Scene newScene = new Scene(vbox, 1000, 600);
        stage.setScene(newScene);
        stage.setTitle("Visits");
        stage.show();

    }

    public int[] loadVisits(TableView<Visit> visits) {
        String inmateUsername = dbConn.getInmateUsername(Integer.parseInt(idInmate));
        int [] visitsId = new int[10];
        try(Statement statement = dbConn.conn.createStatement()){
            ArrayList<Visit> visitsList = new ArrayList<>();
            WrapperClassArrayListAndInt wrapperClass = dbConn.getProgramariPtDetinut(inmateUsername);
            visitsList = wrapperClass.getVisitList();
            visitsId = wrapperClass.getVisitId();
            if(visitsList.isEmpty()){
                System.out.println("No visits found");
            }else {
                for(Visit visit : visitsList){
                    visits.getItems().add(visit);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return visitsId;
    }
}
