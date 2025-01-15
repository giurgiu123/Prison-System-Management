package com.example.penitenciarv1.Listeners;

import com.example.penitenciarv1.Database.DatabaseConnector;
import com.example.penitenciarv1.Entities.Task;
import com.example.penitenciarv1.Interfaces.popUps.addTask.AddTaskController;
import com.example.penitenciarv1.Interfaces.popUps.addTask.AddTaskPopUp;
import com.mysql.cj.xdevapi.Table;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.Statement;
import java.util.ArrayList;

public class DynamicScallingAppIntManageTasks extends Application{

    private int idUserGardian;
    private String idDetinut;

    public DynamicScallingAppIntManageTasks(int idUserGardian, String idDetinut) {
        this.idUserGardian = idUserGardian;
        this.idDetinut = idDetinut;
    }

    public DynamicScallingAppIntManageTasks(){

    }

    DatabaseConnector dbConn = new DatabaseConnector();

    @Override
    public void start(Stage stage) throws Exception {
        VBox root = new VBox();
        root.setSpacing(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-padding: 20; -fx-background-color: linear-gradient(to bottom, #e3f2fd, #bbdefb);");

        Label title = new Label("Manage Tasks");
        title.setTextFill(Color.DARKBLUE);
        title.setFont(Font.font("Arian", 24));

        TableView<Task> tasks = new TableView<>();

        //Coloane
        TableColumn<Task, String> id = new TableColumn<>("ID");
        id.setCellValueFactory(task -> task.getValue().idProperty());

        TableColumn<Task, String> description = new TableColumn<>("Description");
        description.setCellValueFactory(task -> task.getValue().descriptionProperty());

        TableColumn<Task, String> dificulty = new TableColumn<>("Dificulty");
        dificulty.setCellValueFactory(tast -> tast.getValue().difficultyProperty());

        TableColumn<Task, String> startDate = new TableColumn<>("Start Date");
        startDate.setCellValueFactory(task -> task.getValue().startTimeProperty());

        TableColumn<Task, String> endDate = new TableColumn<>("End Date");
        endDate.setCellValueFactory(task -> task.getValue().endTimeProperty());

        TableColumn<Task, String> actions = new TableColumn<>("Actions");
        actions.setCellFactory(param -> new TableCell<Task, String>() {
            final Button markComplete = new Button("Mark Complete");


            @Override
            protected void updateItem(String s, boolean empty) {
                super.updateItem(s, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                }else{
                    markComplete.setOnAction(event -> {
                        Task task = getTableRow().getItem();
                        if(task != null){
                            getTableView().getItems().remove(task);
                            System.out.println("asa: " + Integer.parseInt(task.getId()));
                            dbConn.deleteTask(Integer.parseInt(task.getId()), Integer.parseInt(idDetinut));
                        }
                    });
                    setGraphic(markComplete);
                    setText(null);
                }
            }
        });

        tasks.getColumns().addAll(id, description, dificulty, startDate, endDate, actions);

        tasks.widthProperty().addListener((observable, oldValue, newValue) -> {
           double totalWidth = newValue.doubleValue();
           id.setPrefWidth(totalWidth*0.1);
           description.setPrefWidth(totalWidth*0.15);
           dificulty.setPrefWidth(totalWidth*0.15);
           startDate.setPrefWidth(totalWidth*0.2);
           endDate.setPrefWidth(totalWidth*0.2);
           actions.setPrefWidth(totalWidth*0.2);
        });

        loadData(tasks);

        Button goBack = new Button("Go Back");
        goBack.setStyle("-fx-background-color: linear-gradient(to right, #42a5f5, #1e88e5);"
                + "-fx-text-fill: white; -fx-font-size: 16; -fx-padding: 10 20; -fx-background-radius: 20;");
        goBack.setOnMouseEntered(e -> goBack.setStyle("-fx-background-color: linear-gradient(to right, #1e88e5, #42a5f5);"
                + "-fx-text-fill: white; -fx-font-size: 16; -fx-padding: 10 20; -fx-background-radius: 20;"));
        goBack.setOnMouseExited(e -> goBack.setStyle("-fx-background-color: linear-gradient(to right, #42a5f5, #1e88e5);"
                + "-fx-text-fill: white; -fx-font-size: 16; -fx-padding: 10 20; -fx-background-radius: 20;"));

        goBack.setOnAction(event -> {
            DynamicScalingAppIntGardianDetinut newScene = new DynamicScalingAppIntGardianDetinut(idUserGardian);
            Stage newStage = new Stage();
            stage.close();
            newScene.start(newStage);
        });

        Button addNewTask = new Button("Add New Task");
        addNewTask.setStyle("-fx-background-color: linear-gradient(to right, #42a5f5, #1e88e5);"
                + "-fx-text-fill: white; -fx-font-size: 16; -fx-padding: 10 20; -fx-background-radius: 20;");
        addNewTask.setOnMouseEntered(e -> addNewTask.setStyle("-fx-background-color: linear-gradient(to right, #1e88e5, #42a5f5);"
                + "-fx-text-fill: white; -fx-font-size: 16; -fx-padding: 10 20; -fx-background-radius: 20;"));
        addNewTask.setOnMouseExited(e -> addNewTask.setStyle("-fx-background-color: linear-gradient(to right, #42a5f5, #1e88e5);"
                + "-fx-text-fill: white; -fx-font-size: 16; -fx-padding: 10 20; -fx-background-radius: 20;"));

        addNewTask.setOnAction(event -> {
            AddTaskPopUp newPopUp = new AddTaskPopUp(idDetinut, idUserGardian);
            Stage newStage = new Stage();
            try {
                newPopUp.start(newStage);
            }catch (Exception e){
                e.printStackTrace();
            }
            stage.close();
        });

        root.getChildren().addAll(title, tasks, addNewTask, goBack);

        Scene scene = new Scene(root, 1000, 500);
        stage.setScene(scene);
        stage.setTitle("Manage Tasks");
        stage.show();
    }

    private void loadData(TableView<Task> tasks){
        ArrayList<Task> tasksList = new ArrayList<>();
        try(Statement statement = dbConn.conn.createStatement()){
            String inmateUsername = dbConn.getInmateUsername(Integer.parseInt(idDetinut));
            tasksList = dbConn.getTasksDetinut(inmateUsername);
            if(tasksList.isEmpty()){
                System.out.println("Nu exista task-uri");
            }else{
                for(Task task : tasksList){
                    tasks.getItems().add(task);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
