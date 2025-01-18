package com.example.penitenciarv1.Interfaces.popUps.newCell;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AddToNewCellPopUp extends Application {

    private String inmateId;
    private int guardianId;
    private String guardianUsername;

    public AddToNewCellPopUp() {

    }

    public AddToNewCellPopUp(String inmateId, int guardianId, String guardianUsername) {
        this.inmateId = inmateId;
        this.guardianId = guardianId;
        this.guardianUsername = guardianUsername;
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(AddToNewCellPopUp.class.getResource("/add_to_new_cell.fxml"));
        Parent root = fxmlLoader.load();

        AddToNewCellController controller = fxmlLoader.getController();
        controller.setInmateId(inmateId);
        controller.setGuardianId(guardianId);
        controller.setGuardianUserName(guardianUsername);


        controller.customInitialize();

        Scene scene = new Scene(root, 600, 500);
        stage.setScene(scene);
        stage.setTitle("Add to New Cell");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
