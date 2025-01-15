package com.example.penitenciarv1.Interfaces.popUps.solitudeRoom;

import com.example.penitenciarv1.Database.DatabaseConnector;
import com.example.penitenciarv1.Interfaces.popUps.NoActionPopUp;
import com.example.penitenciarv1.Listeners.DynamicScalingAppIntGardianDetinut;
import com.example.penitenciarv1.Services.DateTimeValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.CallableStatement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.zip.CheckedOutputStream;

public class AddToSolitudeController {

    private String inmateID;
    private int guardianUserID;

    public void setInmateID(String inmateID) {
        this.inmateID = inmateID;
    }

    public void setGuardianUserID(int guardianUserID) {
        this.guardianUserID = guardianUserID;
    }
    DatabaseConnector dbConnector = new DatabaseConnector();


    @FXML
    private TextField enterFinalTimeField;

    @FXML
    private Button button;

    @FXML
    private Button backButton;


    @FXML
    void backToMainScene(ActionEvent event) {
        Stage currStage = (Stage) backButton.getScene().getWindow();
        currStage.close();
        Stage newStage = new Stage();
        openMainStage(newStage);
        Stage noActionPopUp = new Stage();
        NoActionPopUp newPopUp = new NoActionPopUp();
        try {
            newPopUp.start(noActionPopUp);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @FXML
    void updateAndExit(ActionEvent event) {
        String finalTime = enterFinalTimeField.getText();
        boolean isValid = DateTimeValidator.isValidDateTime(finalTime);
        if (isValid) {
            int [] carceraIdAndStatus = dbConnector.getCarceraIdAndOcupationStatus(dbConnector.getGuardianId(guardianUserID));
            int carceraId = carceraIdAndStatus[0];
            int ocupationStatus = carceraIdAndStatus[1];
            System.out.println(carceraId);
            System.out.println(ocupationStatus);

            //ocupation status = 1 -> libera
            if(ocupationStatus == 1) {
                //adaug o noua incarcerare in tabela cu istoric
                dbConnector.newRegistrationToSolitude(inmateID, carceraId, finalTime);
                //schimb status carcera in care adaug detinut
                dbConnector.updateCarceraStatus(carceraId);
                //deschid mainstage detinuti
                Stage mainStage = new Stage();
                openMainStage(mainStage);
            }else{
                //deschis fereastra noua care arata cat timp mai are pana poate adauga detinut
                CountdownPopUp newPopUp = new CountdownPopUp(dbConnector.getLastIncarceration(carceraId));
                Stage newStage = new Stage();
                try {
                    newPopUp.start(newStage);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }else{
            enterFinalTimeField.clear();
            InvalidDatePopUp popup = new InvalidDatePopUp();
            Stage invalidDatePopUp = new Stage();
            try {
                popup.start(invalidDatePopUp);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    void openMainStage(Stage mainStage) {
        DynamicScalingAppIntGardianDetinut mainIntGuardian = new DynamicScalingAppIntGardianDetinut(guardianUserID);
        Stage currStage = (Stage) button.getScene().getWindow();
        currStage.close();
        mainIntGuardian.start(mainStage);
    }

   @FXML
   public void initialize() {

   }


}
