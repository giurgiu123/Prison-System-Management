package com.example.penitenciarv1.Interfaces;

import com.example.penitenciarv1.HelloApplication;
import com.example.penitenciarv1.Listeners.DynamicScalingAppIntGardianColegiPenitenciar;
import com.example.penitenciarv1.Listeners.DynamicScalingAppIntGardianDetinut;
import com.example.penitenciarv1.Listeners.DynamicScalingAppIntGardianColegiBloc;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GuardianInterface extends Application {

    private int idUserGardian;
    private String username;
    private VBox root;
    public GuardianInterface() {

    }

    public AnchorPane getContent(Stage primaryStage) {
        AnchorPane pane = new AnchorPane();
        startTheEngine(primaryStage);

        pane.getChildren().add(root);
        return pane;
    }


    public GuardianInterface(int idGardian) {
        this.idUserGardian = idGardian;
    }

    public GuardianInterface(int idGardian, String username) {
        this.idUserGardian = idGardian;
        this.username = username;
    }
    public void startTheEngine(Stage primaryStage) {
        root = new VBox();

        root.setPrefSize(600, 400);
        AnchorPane anchorPaneVbox1 = new AnchorPane();


        // MenuBar
        MenuBar menuBar = new MenuBar();
        menuBar.setStyle("-fx-background-color: linear-gradient(to right, #71a2ff ,#070c3a); -fx-padding: 5;");
        Menu coleagues = new Menu("Coleagues");
        MenuItem sameDetentionBlock = new MenuItem("In the same detention block");
        coleagues.getItems().add(sameDetentionBlock);
        MenuItem wholePrison = new MenuItem("In the whole prison");
        coleagues.getItems().add(wholePrison);
        Menu inmates = new Menu("Inmates");
        MenuItem prisonersOnShift = new MenuItem("Manage inmates");
        inmates.getItems().add(prisonersOnShift);
        Menu account = new Menu("Account");
        MenuItem logOut = new MenuItem("Log out");
        account.getItems().add(logOut);
        menuBar.getMenus().addAll(coleagues, inmates, account);

        AnchorPane.setTopAnchor(menuBar, 0.0);
        AnchorPane.setLeftAnchor(menuBar, 0.0);
        AnchorPane.setRightAnchor(menuBar, 0.0);
        anchorPaneVbox1.getChildren().add(menuBar);

        //Go to prisoners list
        prisonersOnShift.setOnAction(e -> {
            DynamicScalingAppIntGardianDetinut newInterfataPrisoners = new DynamicScalingAppIntGardianDetinut(idUserGardian, username);
            Stage newStage = new Stage();
            primaryStage.close();
            newInterfataPrisoners.start(newStage);
        });

        sameDetentionBlock.setOnAction(e -> {
            DynamicScalingAppIntGardianColegiBloc newInterfataColegi =  new DynamicScalingAppIntGardianColegiBloc(idUserGardian, username);
            System.out.println("guardian interfata" + idUserGardian);
            Stage newStage = new Stage();
            primaryStage.close();
            newInterfataColegi.start(newStage);
        });

        wholePrison.setOnAction(e -> {
            DynamicScalingAppIntGardianColegiPenitenciar newInterfataTotiColegii = new DynamicScalingAppIntGardianColegiPenitenciar(idUserGardian, username);
            Stage newStage = new Stage();
            primaryStage.close();
            try {
                newInterfataTotiColegii.start(newStage);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        });

        logOut.setOnAction(e -> {
            HelloApplication logIn = new HelloApplication();
            Stage newScene = new Stage();
            logIn.start(newScene);
            primaryStage.close();

        });

        VBox helloScreen = new VBox();
        helloScreen.setAlignment(Pos.CENTER);
        helloScreen.setStyle("-fx-background-color: linear-gradient(to right, #71a2ff ,#070c3a);");
        String text = "Hello " + username + "!";
        Label helloText = new Label(text);
        helloText.setStyle("-fx-text-fill: white;");
        helloText.setFont(Font.font("Arial", 35));
        helloScreen.getChildren().add(helloText);



        root.getChildren().addAll(anchorPaneVbox1, helloScreen);

        VBox.setVgrow(helloScreen, Priority.ALWAYS);

    }
    @Override
    public void start(Stage primaryStage) {

        // Pane 1 - Root with MenuBar and StackPane
        startTheEngine(primaryStage);
        Scene scene = new Scene(root, 800, 600);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Guardian Interface");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
