package com.example.penitenciarv1;


import java.io.IOException;

import com.example.penitenciarv1.Database.DatabaseConnector;
import com.example.penitenciarv1.Entities.User;
import com.example.penitenciarv1.Interfaces.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelloController {


    private DatabaseConnector databaseConnector = new DatabaseConnector();
    @FXML
    private TextField usr_txt;
    @FXML
    private PasswordField pw_txt;

    public void setUsrTxt(TextField usr_txt) {
        this.usr_txt = usr_txt;
    }

    public void setPwTxt(PasswordField pw_txt) {
        this.pw_txt = pw_txt;
    }

    @FXML
    public void login_btn_onClick(ActionEvent e) throws Exception {
        String username = usr_txt.getText();
        String password = pw_txt.getText();
        System.out.println(username + password);

        User newUser = databaseConnector.checkAndReturnUser(username, password);
        if (newUser != null) {

            // set username ul în sesiune
            Session.setCurrentUsername(username);

            Stage stage = (Stage)((Node) e.getSource()).getScene().getWindow();
            stage.close();
            Stage newStage = new Stage();
            if(newUser.getAccessRights() == 0){
                System.out.println("Access rights revoked");
                WardenDashboard newInterface = new WardenDashboard();
                newInterface.start(newStage, databaseConnector, newUser);

            }
            if (newUser.getAccessRights() == 1)
            {
                GuardianInterface newInterfataGardian = new GuardianInterface(newUser.getId());
                newInterfataGardian.start(newStage);
            }
            if (newUser.getAccessRights() == 2)
            {
                PrisonerInterface newPrisonerInterface = new PrisonerInterface(newUser.getId());
                newPrisonerInterface.start(newStage);
            };
            if (newUser.getAccessRights() == 3){
                InterfataVizitator newInterfataVizitator = new InterfataVizitator();
                newInterfataVizitator.start(newStage, databaseConnector, newUser);
            }

//
//            /// /////////////aici se face un if sau case in functie de shift apelam pentru alta imagine
            //Interfatagardian.changeBackground(scene2, "blue");

//            System.out.println("inainte");
//            Stage newStage = new Stage();
           // newStage.setScene(scene2);
            //pentru setare minim si maxim
//            primaryStage.setMinWidth(500);
//            primaryStage.setMinHeight(500);

           // newStage.show();
        } else {

            Stage currentStage = (Stage) usr_txt.getScene().getWindow();
            currentStage.close();
            // Creăm o nouă fereastră pentru Failed Login
            Stage failedLoginStage = new Stage();
            VBox layout = new VBox(20);
            layout.setStyle("-fx-background-color: linear-gradient(to bottom, #ff7e5f, #feb47b);");
            layout.setPadding(new Insets(20));
            layout.setAlignment(Pos.CENTER);

            // Etichetă pentru mesajul de eroare
            Label errorMessage = new Label("Wrong username or password.");
            errorMessage.setStyle("-fx-font-size: 18px; -fx-text-fill: white; -fx-font-weight: bold;");

            // Buton pentru revenirea la pagina de login
            Button backButton = new Button("Back to Login");
            backButton.setStyle(
                    "-fx-font-size: 16px; " +
                            "-fx-font-weight: bold; " +
                            "-fx-text-fill: white; " +
                            "-fx-background-color: linear-gradient(to right, #ff416c, #ff4b2b); " +
                            "-fx-background-radius: 15; " +
                            "-fx-border-radius: 15; " +
                            "-fx-padding: 10 20;"
            );
            backButton.setOnMouseEntered(el -> backButton.setStyle(
                    "-fx-font-size: 16px; " +
                            "-fx-font-weight: bold; " +
                            "-fx-text-fill: white; " +
                            "-fx-background-color: linear-gradient(to right, #ff4b2b, #ff416c); " +
                            "-fx-background-radius: 15; " +
                            "-fx-border-radius: 15; " +
                            "-fx-padding: 10 20;"
            ));
            backButton.setOnMouseExited(el -> backButton.setStyle(
                    "-fx-font-size: 16px; " +
                            "-fx-font-weight: bold; " +
                            "-fx-text-fill: white; " +
                            "-fx-background-color: linear-gradient(to right, #ff416c, #ff4b2b); " +
                            "-fx-background-radius: 15; " +
                            "-fx-border-radius: 15; " +
                            "-fx-padding: 10 20;"
            ));
            backButton.setOnAction(el -> {
                // Închidem fereastra curentă și revenim la pagina de login
                failedLoginStage.close();
                try {
                    Stage loginStage = new Stage();
                    HelloApplication loginApp = new HelloApplication();
                    loginApp.start(loginStage);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            // Adăugăm componentele în layout
            layout.getChildren().addAll(errorMessage, backButton);

            // Configurăm scena și fereastra
            Scene failedLoginScene = new Scene(layout, 400, 200);
            failedLoginStage.setScene(failedLoginScene);
            failedLoginStage.setTitle("Login Failed");
            failedLoginStage.centerOnScreen();
            failedLoginStage.show();

            System.out.println("Wrong username or password. Showing failed login screen.");
        }

    }



    @FXML
    public void failed_login_btn_onClick(ActionEvent e) throws IOException {
        Stage stage = (Stage)((Node) e.getSource()).getScene().getWindow();
        Stage returnToLogin = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource(("hello-view.fxml")));
        returnToLogin.setScene(new Scene(root));
        returnToLogin.show();
        stage.close();
    }


    @FXML
    public void onEnter(ActionEvent ae) {
        try {
            login_btn_onClick(ae);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Problem when introducing your data!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}