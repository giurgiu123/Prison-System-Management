package com.example.penitenciarv1.Listeners;

import javafx.stage.Stage;

public class WardenSchedulreViewer extends DynamicScalingAppDailySchedule{
    public WardenSchedulreViewer() {
        super();
    }
    @Override
    public void backButtonFuction(Stage primaryStage) {
        primaryStage.close();
    }
}
