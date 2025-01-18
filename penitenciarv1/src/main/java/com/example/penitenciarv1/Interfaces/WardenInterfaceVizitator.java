package com.example.penitenciarv1.Interfaces;

import com.example.penitenciarv1.Database.DatabaseConnector;
import com.example.penitenciarv1.Entities.User;
import com.example.penitenciarv1.Entities.Visit;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.function.Function;

public class WardenInterfaceVizitator extends Application {
    VBox root;


    public void start(DatabaseConnector databaseConnector,  Stage primaryStage, User newUser) throws Exception {
        root = new VBox();
        root.setPrefSize(600, 1000);
        root.setSpacing(20);
        Label textLab = new Label("Appointments");
        textLab.setFont(Font.font("Arial", 24));
        textLab.setTextFill(Color.DARKBLUE);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-padding: 20; -fx-background-color: linear-gradient(to bottom, #e3f2fd, #bbdefb);");
        primaryStage.setTitle("New JavaFX Interface");
        TextField searchField = new TextField();
        VBox vBox = new VBox(searchField);
        searchField.setPromptText("Search...");
        root.getChildren().addAll(textLab, vBox);
        TreeTableView<Visit> treeTableView = new TreeTableView<>();
        treeTableView.setRoot(null);
        TreeItem<Visit> rootItem = new TreeItem<>(new Visit("", "", "", "", ""));
        treeTableView.setRoot(rootItem);
        rootItem.setExpanded(true);
        treeTableView.setShowRoot(false);
        TreeTableColumn<Visit, String> col0 = createColumn("Nume Prizionier", visit -> visit.getInmateName());
        TreeTableColumn<Visit, String> col2 = createColumn("Start Time", visit-> visit.getStartTime());
        TreeTableColumn<Visit, String> col1 = createColumn("End Time", visit-> visit.getEndTime());

        TreeTableColumn<Visit, String> col3 = createColumn("Programming Type", person -> person.getVisitType());
        TreeTableColumn<Visit, String> col4 = createColumn("Visitor Name", person -> person.visitorNameProperty());
        TreeTableColumn<Visit, String> col5 = new TreeTableColumn<>("Actions");
        // cancel visit

        col5.setCellFactory(param -> new TreeTableCell<>(){
            private final Button button = new Button("Cancel");

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                button.setStyle("-fx-background-color: red");
                if (empty || getTreeTableRow().getTreeItem() == null) {
                    setGraphic(null);
                } else {
                    // Set action for the button
                    button.setOnAction(event -> {
                        int value = Integer.valueOf(getTreeTableRow().getTreeItem().getValue().getIdVisit().get());
                        System.out.println("Button clicked for: " + value);
                        TreeItem<Visit> toBeDeleted =  getTreeTableRow().getTreeItem();
                        TreeItem<Visit> parent = toBeDeleted.getParent();
                        parent.getChildren().remove(toBeDeleted);
                        databaseConnector.deleteVisit(value);
                    });

                    setGraphic(button);
                }
            }

        });

        treeTableView.getColumns().addAll(col0, col2, col1, col3, col4, col5);
        ArrayList<Visit> data = addAllInmateVisits(databaseConnector, newUser);
        addDataToTreeTable(rootItem, data);
        // Set the Scene on the Stage
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                treeTableView.setRoot(rootItem); // Reset to original root
            } else {
                TreeItem<Visit> filteredRoot = createFilteredTree(rootItem, newValue.toLowerCase());
                treeTableView.setRoot(filteredRoot);
            }
        });
        treeTableView.widthProperty().addListener((observable, oldValue, newValue) -> {
            resizeTable(treeTableView, newValue);
        });

        primaryStage.show();
        // this one starts it already

        root.getChildren().add(treeTableView);

        // Show the Stage

    }
    TreeItem<Visit> createFilteredTree(TreeItem<Visit> root, String query) {
        TreeItem<Visit> filteredRoot = new TreeItem<>(root.getValue());

        for (TreeItem<Visit> child : root.getChildren()) {
            TreeItem<Visit> filteredChild = createFilteredTree(child, query);
            if (!filteredChild.getChildren().isEmpty() || child.getValue().getInmateName().get().toLowerCase().contains(query)
                    || child.getValue().getVisitorName().toLowerCase().contains(query)) {
                filteredRoot.getChildren().add(filteredChild);
            }
        }

        return filteredRoot;
    }
    void addDataToTreeTable(TreeItem<Visit> rootItem, ArrayList<Visit> data) {
        System.out.println("addDataToTreeTable");
        System.out.println(data.size());
        for(int i = 0; i < data.size(); i++) {

            TreeItem<Visit> item = new TreeItem<>(data.get(i));

            rootItem.getChildren().add(item);
        }
    }
    @Override
    public void start(Stage primaryStage) throws Exception {

    }
    TreeTableColumn<Visit, String> createColumn(String title, Function<Visit, ObservableValue<String>> mapper) {
        TreeTableColumn<Visit, String> column = new TreeTableColumn<>(title);
        column.setCellValueFactory(cellData -> mapper.apply(cellData.getValue().getValue()));
        return column;
    }
    private ArrayList<Visit> addAllInmateVisits(DatabaseConnector databaseConnector, User newUser) {

        ArrayList<Visit> data = databaseConnector.getAllVisits();

        return data;

    }
    private void resizeTable(TreeTableView table, Number newVal) {
        double totalWidth = newVal.doubleValue();
        table.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
        for(int i = 0; i < table.getColumns().size(); i++) {
            TreeTableColumn tableColumn = (TreeTableColumn) table.getColumns().get(i);
            tableColumn.setPrefWidth(totalWidth * 0.15);
        }

    }

}
