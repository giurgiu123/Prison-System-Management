package com.example.penitenciarv1.Interfaces.popUps.solitudeRoom;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AddToSolitudePopUp extends Application {

    private String inmateID;
    private int guardianUserID;

    public AddToSolitudePopUp() {

    }

    public AddToSolitudePopUp(String inmateID, int guardianID) {
        this.inmateID = inmateID;
        this.guardianUserID = guardianID;
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(AddToSolitudePopUp.class.getResource("/add_to_solitude_room.fxml"));
        Parent root = fxmlLoader.load();

        AddToSolitudeController controller = fxmlLoader.getController();
        controller.setInmateID(inmateID);
        controller.setGuardianUserID(guardianUserID);

        Scene rootScene = new Scene(root, 500, 420);
        stage.setScene(rootScene);
        stage.setTitle("Add To Solitude");
        stage.show();

    }
}
