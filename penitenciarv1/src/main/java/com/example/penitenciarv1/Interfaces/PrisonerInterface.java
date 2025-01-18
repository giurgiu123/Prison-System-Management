package com.example.penitenciarv1.Interfaces;

import com.example.penitenciarv1.Database.DatabaseConnector;
import com.example.penitenciarv1.Entities.Inmates;
import com.example.penitenciarv1.HelloApplication;
import com.example.penitenciarv1.Listeners.DynamicScallingAppIntLaundry;
import com.example.penitenciarv1.Listeners.DynamicScallingAppIntPrisonerFutureTasks;
import com.example.penitenciarv1.Listeners.DynamicScallingAppIntPrisonerPastTask;
import com.example.penitenciarv1.Listeners.DynamicScallingAppPrisonerVisit;
import com.example.penitenciarv1.Listeners.DynamicScalingAppDailySchedule;

import java.sql.*;


import eu.hansolo.toolbox.properties.StringProperty;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.ResultSet;

public class PrisonerInterface extends Application {

    private int idUserDetinut;
   // Exemplu nume
    private String detinutUsername = Session.getCurrentUsername(); // Exemplu username
    private String detinutName;
    private String detinutName1   = getDetinutNameByUsername(detinutUsername);
    public PrisonerInterface() {
    }

    public PrisonerInterface(int idDetinut) {
        this.idUserDetinut = idDetinut;
    }

    public PrisonerInterface(int idDetinut, String detinutUsername) {
        this.idUserDetinut = idDetinut;
        this.detinutName = detinutUsername;
    }

    @Override
    public void start(Stage primaryStage) {

        // Root Layout
        VBox root = new VBox();
        root.setPrefSize(600, 400);

        // AnchorPane for MenuBar
        AnchorPane anchorPaneVbox1 = new AnchorPane();

        // StackPane for dynamic content
        StackPane stackPane = new StackPane();
        stackPane.setPrefSize(600, 400);

//        // MenuBar
        MenuBar menuBar = new MenuBar();
        menuBar.setStyle("-fx-background-color: linear-gradient(to right, #71a2ff ,#070c3a); -fx-padding: 5;");


        // Menu: Tasks
        Menu tasksMenu = new Menu("Tasks");
        MenuItem currentTasks = new MenuItem("Current Tasks");
        MenuItem completedTasks = new MenuItem("Completed Tasks");
        tasksMenu.getItems().addAll(currentTasks, completedTasks);

        // Menu: Schedule
        Menu scheduleMenu = new Menu("Schedule");
        MenuItem dailySchedule = new MenuItem("Daily Schedule");
        MenuItem visitSchedule = new MenuItem("Visit Schedule");
        MenuItem laundrySchedule = new MenuItem("Laundry Schedule");
        scheduleMenu.getItems().addAll(dailySchedule, visitSchedule, laundrySchedule);

        // Menu: Account
        Menu accountMenu = new Menu("Account");
        MenuItem profile = new MenuItem("Profile");
        MenuItem sentenceRemaining = new MenuItem("Sentence Remaining");
        MenuItem logout = new MenuItem("Logout");
        accountMenu.getItems().addAll(profile, sentenceRemaining, logout);


        menuBar.getMenus().addAll(tasksMenu, scheduleMenu, accountMenu);

        // Anchor MenuBar
        AnchorPane.setTopAnchor(menuBar, 0.0);
        AnchorPane.setLeftAnchor(menuBar, 0.0);
        AnchorPane.setRightAnchor(menuBar, 0.0);
        anchorPaneVbox1.getChildren().add(menuBar);

        // Event Handling
        profile.setOnAction(e -> {
            stackPane.getChildren().clear();
            stackPane.getChildren().add(createAccountDetails());
        });

        currentTasks.setOnAction(e -> {
            stackPane.getChildren().clear();
            stackPane.getChildren().add(createContent("Current Tasks", "List of your current tasks."));
        });

        completedTasks.setOnAction(e -> {
            stackPane.getChildren().clear();
            stackPane.getChildren().add(createContent("Completed Tasks", "Tasks you have completed."));
        });

        dailySchedule.setOnAction(e -> {
            stackPane.getChildren().clear();
            stackPane.getChildren().add(createContent("Daily Schedule", "Your daily activities."));
        });

        visitSchedule.setOnAction(e -> {
            stackPane.getChildren().clear();
            stackPane.getChildren().add(createContent("Visit Schedule", "Details about visitor appointments."));
        });

        laundrySchedule.setOnAction(e -> {
            stackPane.getChildren().clear();
            stackPane.getChildren().add(createContent("Laundry Schedule", "Your scheduled laundry appointments."));
        });

        sentenceRemaining.setOnAction(e -> {
            stackPane.getChildren().clear();
            stackPane.getChildren().add(createContent("Sentence Remaining", "Time left in your sentence."));
        });

        logout.setOnAction(e -> {
            primaryStage.close();
        });

        VBox helloScreen = new VBox();
        helloScreen.setStyle("-fx-background-color: linear-gradient(to right, #71a2ff ,#070c3a);");
        helloScreen.setAlignment(Pos.CENTER);
        String text = "Hello " + detinutUsername + "!";
        Label helloText = new Label(text);
        helloText.setStyle("-fx-text-fill: white;");
        helloText.setFont(Font.font("Arial", 35));
        helloScreen.getChildren().add(helloText);

        VBox.setVgrow(helloScreen, Priority.ALWAYS);


        // Add components to root
        root.getChildren().addAll(anchorPaneVbox1, helloScreen);

        // Eveniment pentru profil


        // Scene Setup
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Detinut Interface");
        primaryStage.show();
        profile.setOnAction(e -> {
            primaryStage.close();
            openProfileWindow(primaryStage,"Profile");});
        sentenceRemaining.setOnAction(e -> {
            primaryStage.close();
            openProfileWindow(primaryStage, "Sentence Remaining");});
        currentTasks.setOnAction(e ->  {
            primaryStage.close();
            openProfileWindow(primaryStage, "Current");});
        completedTasks.setOnAction(e -> {
            primaryStage.close();
            openProfileWindow(primaryStage, "Completed");});
        dailySchedule.setOnAction(e ->  {
            primaryStage.close();
            openProfileWindow(primaryStage, "Daily Schedule");});
        visitSchedule.setOnAction(e -> {
            primaryStage.close();
            openProfileWindow(primaryStage, "Visit Schedule");});
        laundrySchedule.setOnAction(e -> {
            primaryStage.close();
            openProfileWindow(primaryStage, "Laundry");});

        //Pentru logout
        logout.setOnAction(e -> {
            // Închide fereastra curentă
            primaryStage.close();

            // Deschide noua interfață
            HelloApplication loginInterface = new HelloApplication();
            Stage loginStage = new Stage();
            try {
                loginInterface.start(loginStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

    }

    public String getDetinutNameByUsername(String username) {
        String detinutName = null; // Inițializează cu null
        DatabaseConnector dbConnector = new DatabaseConnector();
        String query = "SELECT d.nume AS NumeDetinut " +
                "FROM Detinut d " +
                "INNER JOIN Utilizator u ON d.fk_id_utilizator = u.id_utilizator " +
                "WHERE u.username = ?";

        try (PreparedStatement preparedStatement = dbConnector.conn.prepareStatement(query)) {
            preparedStatement.setString(1, username); // Setează valoarea parametrului
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    detinutName = resultSet.getString("NumeDetinut");
                } else {
                    System.out.println("No detinut found for username: " + username);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return detinutName; // Returnează numele deținutului sau null dacă nu este găsit
    }


    /// ///////////////////////////////////////////////////////////////////////
    private void openProfileWindow(Stage mainStage, String taskType) {
        // Fereastra nouă



        if ("Profile".equalsIgnoreCase(taskType)) {
            // Preia detaliile deținutului din baza de date
            DatabaseConnector dbConnector = new DatabaseConnector(); // Inițializare conexiune
             String username = Session.getCurrentUsername();
             String detinutName = getDetinutNameByUsername(username);
            String query = "SELECT u.username AS Username, d.nume AS NumeDetinut " +
                    "FROM Detinut d " +
                    "INNER JOIN Utilizator u ON d.fk_id_utilizator = u.id_utilizator " +
                    "WHERE u.id_utilizator = '" + idUserDetinut + "'";



            try (Statement statement = dbConnector.conn.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {

                if (resultSet.next()) {
                    username = resultSet.getString("Username");
                    detinutName = resultSet.getString("NumeDetinut");
                } else {
                    System.out.println("No data found for user ID: " + idUserDetinut);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }


            Stage profileStage = new Stage();
            VBox profileLayout = new VBox();
            profileLayout.setSpacing(25);
            profileLayout.setAlignment(Pos.CENTER);
            profileLayout.setStyle(
                    "-fx-background-color: linear-gradient(to bottom, #1e3c72, #2a5298); " +
                            "-fx-padding: 40; " +
                            "-fx-border-radius: 20; " +
                            "-fx-background-radius: 20;"
            );

            // Profile image with circular styling
            Inmates inmate = new Inmates();
            inmate.setName(detinutName);

            ImageView imageView = createImageView(inmate);
            if (imageView != null) {
                imageView.setFitWidth(130);
                imageView.setFitHeight(130);
                imageView.setPreserveRatio(true);
                imageView.setStyle(
                        "-fx-effect: dropshadow(gaussian, rgba(255, 255, 255, 0.3), 15, 0.3, 0, 4); " +
                                "-fx-background-color: white; " +
                                "-fx-background-radius: 65; " +
                                "-fx-border-radius: 65; " +
                                "-fx-padding: 5;"
                );
            }

            // Title with a glowing effect
            Text title = new Text("Profile Details");
            title.setFont(Font.font("Arial", 24));
            title.setStyle(
                    "-fx-font-weight: bold; " +
                            "-fx-fill: linear-gradient(to right, #ffffff, #dcdde1); " +
                            "-fx-effect: dropshadow(gaussian, rgba(255,255,255,0.3), 10, 0.5, 0, 0);"
            );

            // Profile details styled as cards
            VBox detailsCard = new VBox();
            detailsCard.setSpacing(10);
            detailsCard.setAlignment(Pos.CENTER);
            detailsCard.setStyle(
                    "-fx-background-color: linear-gradient(to bottom, #ffffff, #f9f9f9); " +
                            "-fx-padding: 20; " +
                            "-fx-border-color: rgba(0, 0, 0, 0.1); " +
                            "-fx-border-radius: 15; " +
                            "-fx-background-radius: 15; " +
                            "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.2), 10, 0, 0, 6);"
            );

            Text name = new Text(detinutName + " (" + username + ")");
            name.setFont(Font.font("Arial", 18));
            name.setStyle("-fx-text-fill: #34495e; -fx-font-weight: bold;");


            detailsCard.getChildren().addAll(name);

            // Back button with gradient hover effects
            Button backButton = new Button("Back to Main Window");
            backButton.setStyle(
                    "-fx-background-color: linear-gradient(to right, #3498db, #2980b9); " +
                            "-fx-text-fill: white; " +
                            "-fx-font-size: 16; " +
                            "-fx-padding: 10 20 10 20; " +
                            "-fx-border-radius: 20; " +
                            "-fx-background-radius: 20; " +
                            "-fx-cursor: hand;"
            );
            backButton.setOnMouseEntered(e -> backButton.setStyle(
                    "-fx-background-color: linear-gradient(to right, #2980b9, #3498db); " +
                            "-fx-text-fill: white; " +
                            "-fx-font-size: 16; " +
                            "-fx-padding: 10 20 10 20; " +
                            "-fx-border-radius: 20; " +
                            "-fx-background-radius: 20; " +
                            "-fx-cursor: hand;"
            ));
            backButton.setOnMouseExited(e -> backButton.setStyle(
                    "-fx-background-color: linear-gradient(to right, #3498db, #2980b9); " +
                            "-fx-text-fill: white; " +
                            "-fx-font-size: 16; " +
                            "-fx-padding: 10 20 10 20; " +
                            "-fx-border-radius: 20; " +
                            "-fx-background-radius: 20; " +
                            "-fx-cursor: hand;"
            ));
            backButton.setOnAction(e -> {
                profileStage.close();
                mainStage.show();
            });

            // Add components to layout
            if (imageView != null) {
                profileLayout.getChildren().add(imageView);
            } else {
                profileLayout.getChildren().add(new Text("Image not found"));
            }
            profileLayout.getChildren().addAll(title, detailsCard, backButton);

            // Scene setup
            Scene profileScene = new Scene(profileLayout, 500, 400);
            profileStage.setScene(profileScene);
            profileStage.setTitle("Profile Details");
            profileStage.setResizable(false); // Disable resizing for premium appearance

            // Show the profile window and hide the main window
            mainStage.hide();
            profileStage.show();
        }

        if ("Sentence Remaining".equalsIgnoreCase(taskType)) {
            Stage sentenceStage = new Stage();

            // Root layout with gradient background
            VBox rootLayout = new VBox();
            rootLayout.setSpacing(40);
            rootLayout.setAlignment(Pos.CENTER);
            rootLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #74b9ff, #0984e3); -fx-padding: 30;");

            // Title with enhanced styling
            Text title = new Text("Sentence Remaining");
            title.setFont(Font.font("Arial", 28));
            title.setStyle("-fx-font-weight: bold; -fx-text-fill: #ffffff;");

            // Details in a card-like layout
            VBox cardLayout = new VBox();
            cardLayout.setAlignment(Pos.CENTER);
            cardLayout.setSpacing(15);
            cardLayout.setStyle(
                    "-fx-background-color: #ffffff; "
                            + "-fx-padding: 20; "
                            + "-fx-border-radius: 12; "
                            + "-fx-background-radius: 12; "
                            + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.2), 12, 0, 0, 8);"
            );

            // Sentence details with premium styling
            Text sentenceDetails = new Text("Fetching remaining time...");
            sentenceDetails.setFont(Font.font("Arial", 18));
            sentenceDetails.setStyle("-fx-text-fill: #2d3436;");

            Text motivationalQuote = new Text("\"Every day is one step closer to freedom.\"");
            motivationalQuote.setFont(Font.font("Italic", 14));
            motivationalQuote.setStyle("-fx-text-fill: #636e72;");

            // Decorative icon
            ImageView icon = new ImageView();
            try {
                Image image = new Image(getClass().getResource("/com/example/penitenciarv1/images/sentence_icon.png").toExternalForm());
                icon.setImage(image);
                icon.setFitWidth(100);
                icon.setFitHeight(100);
                icon.setPreserveRatio(true);
            } catch (Exception e) {
                System.out.println("Error loading sentence icon: " + e.getMessage());
            }

            // Add elements to the card layout
            if (icon != null) {
                cardLayout.getChildren().add(icon);
            }
            cardLayout.getChildren().addAll(sentenceDetails, motivationalQuote);

            // Back button with hover effect
            Button backButton = new Button("Back to Main Window");
            backButton.setStyle(
                    "-fx-background-color: #74b9ff; "
                            + "-fx-text-fill: white; "
                            + "-fx-font-size: 16; "
                            + "-fx-padding: 10 20 10 20; "
                            + "-fx-border-radius: 20; "
                            + "-fx-background-radius: 20;"
            );
            backButton.setOnMouseEntered(e -> backButton.setStyle(
                    "-fx-background-color: #0984e3; "
                            + "-fx-text-fill: white; "
                            + "-fx-font-size: 16; "
                            + "-fx-padding: 10 20 10 20; "
                            + "-fx-border-radius: 20; "
                            + "-fx-background-radius: 20;"
            ));
            backButton.setOnMouseExited(e -> backButton.setStyle(
                    "-fx-background-color: #74b9ff; "
                            + "-fx-text-fill: white; "
                            + "-fx-font-size: 16; "
                            + "-fx-padding: 10 20 10 20; "
                            + "-fx-border-radius: 20; "
                            + "-fx-background-radius: 20;"
            ));
            backButton.setOnAction(e -> {
                sentenceStage.close();
                mainStage.show();
            });
            // Fetch remaining sentence dynamically
            // String username = "john_doe"; // Replace with dynamic username if needed
            String currentUsername = Session.getCurrentUsername();

            DatabaseConnector myInstance = new DatabaseConnector();
            String remainingTime = myInstance.fetchRemainingSentence(currentUsername);

        //    String remainingTime = fetchRemainingSentence(username);
            if (remainingTime != null) {
                sentenceDetails.setText(remainingTime);
            } else {
                sentenceDetails.setText("No sentence data found.");
            }

            // Add components to the root layout
            rootLayout.getChildren().addAll(title, cardLayout, backButton);

            // Scene setup
            Scene scene = new Scene(rootLayout, 500, 500);
            sentenceStage.setScene(scene);
            sentenceStage.setTitle("Sentence Remaining");
            sentenceStage.setResizable(false);

            // Show sentence window and hide main window
            mainStage.hide();
            sentenceStage.show();
        }


        else if ("Current".equalsIgnoreCase(taskType)) {
            // Obține username-ul curent din sesiune
            String currentUsername = Session.getCurrentUsername();

            DynamicScallingAppIntPrisonerFutureTasks prisonerInterface = new DynamicScallingAppIntPrisonerFutureTasks(currentUsername); // Inițializează clasa cu username-ul utilizatorului
            System.out.println("Opening DynamicScallingAppIntPrisoner for Guardian username: " + currentUsername);

            Stage newStage = new Stage();
            try {
                prisonerInterface.start(newStage); // Pornește noua interfață
            } catch (Exception ex) {
                ex.printStackTrace(); // Gestionare erori
            }
        }


        else if ("Completed".equalsIgnoreCase(taskType)) {
            String currentUsername = Session.getCurrentUsername();

            DynamicScallingAppIntPrisonerPastTask prisonerInterface = new DynamicScallingAppIntPrisonerPastTask(currentUsername); // Inițializează clasa cu username-ul utilizatorului
            System.out.println("Opening DynamicScallingAppIntPrisoner for Guardian username: " + currentUsername);

            Stage newStage = new Stage();
            try {
                prisonerInterface.start(newStage); // Pornește noua interfață
            } catch (Exception ex) {
                ex.printStackTrace(); // Gestionare erori
            }

        } else if ("Daily Schedule".equalsIgnoreCase(taskType)) {
            String currentUsername = Session.getCurrentUsername();

            DynamicScalingAppDailySchedule prisonerInterface = new DynamicScalingAppDailySchedule(currentUsername); // Inițializează clasa cu username-ul utilizatorului
            System.out.println("Opening DynamicScallingAppIntPrisoner for Guardian username: " + currentUsername);

            Stage newStage = new Stage();
            try {
                prisonerInterface.start(newStage); // Pornește noua interfață
            } catch (Exception ex) {
                ex.printStackTrace(); // Gestionare erori
            }
        } else if ("Visit Schedule".equalsIgnoreCase(taskType)) {
            String currentUsername = Session.getCurrentUsername();

            DynamicScallingAppPrisonerVisit prisonerInterface = new DynamicScallingAppPrisonerVisit(currentUsername); // Inițializează clasa cu username-ul utilizatorului
            System.out.println("Opening DynamicScallingAppIntPrisoner for Guardian username: " + currentUsername);

            Stage newStage = new Stage();
            try {
                prisonerInterface.start(newStage); // Pornește noua interfață
            } catch (Exception ex) {
                ex.printStackTrace(); // Gestionare erori
            }
        } else if ("Laundry".equalsIgnoreCase(taskType)) {
            String currentUsername = Session.getCurrentUsername();

            DynamicScallingAppIntLaundry prisonerInterface = new DynamicScallingAppIntLaundry(currentUsername); // Inițializează clasa cu username-ul utilizatorului
            System.out.println("Opening DynamicScallingAppIntPrisoner for Guardian username: " + currentUsername);

            Stage newStage = new Stage();
            try {
                prisonerInterface.start(newStage); // Pornește noua interfață
            } catch (Exception ex) {
                ex.printStackTrace(); // Gestionare erori
            }
        }

}




    private ImageView createImageView(Inmates inmate) {
        try {
            Image image = new Image(getClass().getResource("/com/example/penitenciarv1/images/pozadetinut.png").toExternalForm());

            image = setImagePath("../Avatars/Inmates/"+ inmate.getName().get() +".jpg", image);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            imageView.setPreserveRatio(true);
            return imageView;
        } catch (Exception e) {
            System.out.println("Error loading image: " + e.getMessage());
            return null;
        }
    }

    private Image setImagePath(String imagePathString, Image image) {
        ///  format is:
        /// "../Avatars/Inmates/"+ person.getName().get() +".jpg" for each inmate
        /// replace Inmates with Guardians for guardians

        if(PrisonerInterface.class.getResource(imagePathString) != null){
            String imagePath = PrisonerInterface.class.getResource(imagePathString).toExternalForm();
            image = new Image(imagePath);
        }
        else{
            System.out.println(PrisonerInterface.class.getResource(imagePathString));
            System.out.println(imagePathString);
        }
        return image;
    }

    private AnchorPane createAccountDetails() {
        AnchorPane pane = new AnchorPane();
        pane.setPrefSize(600, 400);

        VBox detailsBox = new VBox();
        detailsBox.setSpacing(10);
        detailsBox.setAlignment(Pos.TOP_RIGHT);

        // Guest image
        Image image = new Image(getClass().getResource("/com/example/penitenciarv1/images/pozadetinut.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);

        // Profile details
        Text title = new Text("Profile Details");
        title.setFont(Font.font("Arial", 18));
        title.setStyle("-fx-font-weight: bold;");

        Text name = new Text(detinutName + " (" + detinutUsername + ")");
        name.setFont(Font.font("Arial", 14));

        detailsBox.getChildren().addAll(imageView, title, name);

        // Align VBox to the top-right
        AnchorPane.setTopAnchor(detailsBox, 20.0);
        AnchorPane.setRightAnchor(detailsBox, 20.0);

        pane.getChildren().add(detailsBox);

        return pane;
    }

    private AnchorPane createContent(String title, String description) {
        AnchorPane pane = new AnchorPane();
        pane.setPrefSize(600, 400);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        AnchorPane.setTopAnchor(titleLabel, 20.0);
        AnchorPane.setLeftAnchor(titleLabel, 20.0);

        Label descriptionLabel = new Label(description);
        AnchorPane.setTopAnchor(descriptionLabel, 60.0);
        AnchorPane.setLeftAnchor(descriptionLabel, 20.0);

        pane.getChildren().addAll(titleLabel, descriptionLabel);
        return pane;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
