package com.example.penitenciarv1.Interfaces;

import com.example.penitenciarv1.Database.DatabaseConnector;
import com.example.penitenciarv1.Entities.Inmates;
import com.example.penitenciarv1.Entities.User;
import com.example.penitenciarv1.Entities.Visit;
import com.example.penitenciarv1.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class WardenVizitator extends InterfataVizitator{
    public WardenVizitator(DatabaseConnector databaseConnector, Stage stage2, User newUser) {
        super(stage2, databaseConnector, newUser);

    }
    public WardenVizitator(){
        super();
    }


}
