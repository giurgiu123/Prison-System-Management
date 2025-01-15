package com.example.penitenciarv1.Listeners;

import com.example.penitenciarv1.Database.DatabaseConnector;
import com.example.penitenciarv1.Entities.Guardian;
import com.example.penitenciarv1.Interfaces.GuardianInterface;
import javafx.application.Application;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.function.Function;

public class DynamicScalingAppIntGardianColegiBloc extends Application {
    private int idUserGardian;
    public DynamicScalingAppIntGardianColegiBloc() {

    }

    public DynamicScalingAppIntGardianColegiBloc(int idGardian) {
        this.idUserGardian = idGardian;
    }


    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();
        root.setSpacing(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-padding: 20; -fx-background-color: linear-gradient(to bottom, #e3f2fd, #bbdefb);");


        Label titleLabel = new Label("Coleagues from same Block");
        titleLabel.setFont(Font.font("Arial", 24));
        titleLabel.setTextFill(Color.DARKBLUE);

        TreeTableView<Guardian>treeTableView = new TreeTableView<>();
        TreeItem<Guardian> rootItem = new TreeItem<>(new Guardian("", "", "", ""));
        rootItem.setExpanded(true);
        treeTableView.setRoot(rootItem);
        treeTableView.setShowRoot(false);

        TreeTableColumn<Guardian, String> idColumn = createColumn("ID", guardian -> guardian.getId());
        TreeTableColumn<Guardian, String> usernameColumn = createColumn("Username", guardian -> guardian.getUsername());
        TreeTableColumn<Guardian, String> floorColumn = createColumn("Floor", guardian -> guardian.getFloor());
        TreeTableColumn<Guardian, String> blockColumn = createColumn("Detention Block", guardian -> guardian.getDetentionBlock());

        treeTableView.getColumns().addAll(idColumn, usernameColumn, floorColumn, blockColumn);

        //dimensiune coloane
        treeTableView.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            double totalWidth = newWidth.doubleValue();
            idColumn.setPrefWidth(totalWidth * 0.20);
            usernameColumn.setPrefWidth(totalWidth * 0.30);
            floorColumn.setPrefWidth(totalWidth * 0.20);
            blockColumn.setPrefWidth(totalWidth * 0.30);
        });

        DatabaseConnector dbConnector = new DatabaseConnector();
        try(Statement statement = dbConnector.conn.createStatement()){
            int idGuardian = dbConnector.getGuardianId(idUserGardian);
            System.out.printf("idGuardian = %d\n", idGuardian);
            ArrayList<Guardian> guardians = dbConnector.getGuardianColleaguesSameBlock(idGuardian);
            if(guardians.size() == 0){
                System.out.println("No guardians found");
            }
            for(Guardian guardian : guardians){
                StringProperty id = guardian.getId();
                StringProperty username = guardian.getUsername();
                StringProperty floor = guardian.getFloor();
                StringProperty block = guardian.getDetentionBlock();

                TreeItem<Guardian> treeItem = new TreeItem<>(new Guardian(id.get(), username.get(), floor.get(), block.get()));
                rootItem.getChildren().add(treeItem);
            }

            System.out.println("Numarul de elemente incarcate: " + rootItem.getChildren().size());
        }catch (Exception e){
            System.out.println("Error in connecting to the database");
            e.printStackTrace();
        }

        Button goBackButton = new Button("Go Back");
        goBackButton.setStyle("-fx-background-color: linear-gradient(to right, #42a5f5, #1e88e5);"
                + "-fx-text-fill: white; -fx-font-size: 16; -fx-padding: 10 20; -fx-background-radius: 20;");
        goBackButton.setOnMouseEntered(e -> goBackButton.setStyle("-fx-background-color: linear-gradient(to right, #1e88e5, #42a5f5);"
                + "-fx-text-fill: white; -fx-font-size: 16; -fx-padding: 10 20; -fx-background-radius: 20;"));
        goBackButton.setOnMouseExited(e -> goBackButton.setStyle("-fx-background-color: linear-gradient(to right, #42a5f5, #1e88e5);"
                + "-fx-text-fill: white; -fx-font-size: 16; -fx-padding: 10 20; -fx-background-radius: 20;"));

        goBackButton.setOnAction(event -> {
            GuardianInterface gin = new GuardianInterface(idUserGardian);
            Stage stage = new Stage();
            primaryStage.close();
            gin.start(stage);
        });
        goBackButton.setAlignment(Pos.TOP_LEFT);
        goBackButton.setPrefHeight(20);

        root.getChildren().addAll(titleLabel, treeTableView ,goBackButton);

        Scene scene = new Scene(root, 1400, 600);
        primaryStage.setTitle("Coleagues from same Block");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private TreeTableColumn<Guardian, String> createColumn(String title, Function<Guardian, ObservableValue<String>> mapper) {
        TreeTableColumn<Guardian, String> column = new TreeTableColumn<>(title);
        column.setCellValueFactory(cellData -> mapper.apply(cellData.getValue().getValue()));
        return column;
    }

    public static void main(String[] args) {
        launch(args);
    }
}


