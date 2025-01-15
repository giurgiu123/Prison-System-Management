package com.example.penitenciarv1.Interfaces.popUps.addTask;

import com.example.penitenciarv1.Database.DatabaseConnector;
import com.example.penitenciarv1.Interfaces.popUps.solitudeRoom.InvalidDatePopUp;
import com.example.penitenciarv1.Listeners.DynamicScalingAppIntGardianDetinut;
import com.example.penitenciarv1.Listeners.DynamicScallingAppIntManageTasks;
import com.example.penitenciarv1.Services.DateTimeValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddTaskController implements Initializable {

    DatabaseConnector databaseConnector = new DatabaseConnector();

    private String inmateId;
    private int guardianId;

    public void setInmateId(String inmateId) {
        this.inmateId = inmateId;
    }

    public void setGuardianId(int guardianId) {
        this.guardianId = guardianId;
    }

    @FXML
    private Button addTaskBtn;

    @FXML
    private ComboBox<String> comboBox;

    @FXML
    private TextField endDateTextBox;

    @FXML
    private TextField startDateTextBox;

    @FXML
    private TextField taskDescriptionTextBox;

    @FXML
    void updateBDAndExit(ActionEvent event) {
        String comboBoxSel = comboBox.getValue();
        String endDateSel = endDateTextBox.getText();
        String startDateSel = startDateTextBox.getText();
        String taskDescriptionSel = taskDescriptionTextBox.getText();

        if(comboBoxSel != null && !endDateSel.isEmpty() && !startDateSel.isEmpty() && !taskDescriptionSel.isEmpty()) {
            boolean isStartValid = DateTimeValidator.isValidDateTime(startDateSel);
            boolean isEndValid = DateTimeValidator.isValidDateTime(endDateSel);
            if(!isStartValid || !isEndValid) {
                Stage invalidDateStage = new Stage();
                invalidDateStage.setTitle("Invalid Date");
                InvalidDatePopUp newPopUp = new InvalidDatePopUp();
                try{
                    newPopUp.start(invalidDateStage);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else{
                //adaug in baza de date un nou task
                databaseConnector.addTask(comboBoxSel, startDateSel, endDateSel, taskDescriptionSel, Integer.parseInt(inmateId));
                Stage newStage = new Stage();
                Stage currStage = (Stage) addTaskBtn.getScene().getWindow();
                currStage.close();
                DynamicScallingAppIntManageTasks newInt = new DynamicScallingAppIntManageTasks(guardianId, inmateId);
                try {
                    newInt.start(currStage);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }else{
            Stage newStage = new Stage();
            String labelText = "Some";
            if(comboBoxSel == null){
                labelText = "Difficulty";
            }else if(endDateSel.isEmpty()){
                labelText = "End Date";
            }else if(startDateSel.isEmpty()){
                labelText = "Start Date";
            }else if(taskDescriptionSel.isEmpty()){
                labelText = "Task Description";
            }
            EmptyField newPopUp = new EmptyField();
            try{
                newPopUp.setLabelText(labelText);
                newPopUp.start(newStage);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String [] comboBoxSel = {"1", "2", "3", "4", "5"};
        comboBox.getItems().addAll(comboBoxSel);
    }
}
