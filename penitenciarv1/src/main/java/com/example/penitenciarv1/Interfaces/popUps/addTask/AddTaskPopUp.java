package com.example.penitenciarv1.Interfaces.popUps.addTask;

import com.example.penitenciarv1.Interfaces.popUps.newCell.AddToNewCellPopUp;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AddTaskPopUp extends Application {


    private String inmateId;
    private int guardianId;

    public AddTaskPopUp(String inmateId, int guardianId) {
        this.inmateId = inmateId;
        this.guardianId = guardianId;
    }

    public AddTaskPopUp() {

    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(AddToNewCellPopUp.class.getResource("/add_task.fxml"));
        Parent root = fxmlLoader.load();

        AddTaskController controller = fxmlLoader.getController();
        controller.setInmateId(inmateId);
        controller.setGuardianId(guardianId);

        Scene rootScene = new Scene(root, 600, 600);
        stage.setScene(rootScene);
        stage.setTitle("Add Task");
        stage.show();
    }
}
