package com.example.penitenciarv1.Controllers;

import com.example.penitenciarv1.Database.DatabaseConnector;
import com.example.penitenciarv1.Entities.Inmates;
import com.example.penitenciarv1.Entities.Sentence;
import com.example.penitenciarv1.Entities.Visit;
import com.example.penitenciarv1.HelloApplication;
import com.example.penitenciarv1.Interfaces.InterfataVizitator;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.function.Function;

public class ListItem extends ListCell<Inmates> {
    private final ImageView imageView = new ImageView(){
        @Override public double minHeight(double width) { return 80; } //some hard stop value
        @Override public double minWidth(double height) { return 80; }
    }; // I've added this so the imageview can be resized
    // JavaFx ImageView won't resize on fitWidthProperty bind to VBox
    // on stackoverflow
    // we make a treetabelview with the sentences
    private final Label textLabel = new Label();
    private final Label iconLabel = new Label();
    private DatabaseConnector databaseConnector = null;
    private final VBox imageBox = new VBox(imageView);
    // put everything up of here in a Vbox, we have the details of each inmate
    private final TreeTableView theTable = new TreeTableView();
    boolean everyColumnIsEntered = false;
    // the sentences of each inmate

    private final VBox detailsDetinutHBox = new VBox(imageBox, textLabel, iconLabel);
    private final VBox sentinteDetinutVBox = new VBox(theTable);

    // composed of details and sentences
    private final HBox layout = new HBox(detailsDetinutHBox, sentinteDetinutVBox);

    String imagePath = HelloApplication.class.getResource("images/pozadetinut.png").toExternalForm();
    Image image = new Image(imagePath);

    public ListItem(ListView<Inmates> listView, DatabaseConnector databaseConnector) {
        super();
        iconLabel.setFont(Font.font(15.0)); // Configure font size
        layout.setMaxHeight(350);
        layout.setMinHeight(350);
        layout.prefWidthProperty().bind(listView.widthProperty()/*.divide(10/9)*/);
        this.databaseConnector = databaseConnector;

        //I want each element to be fixed vertical and resizeable horizontal
        modifyLayouts();

    }

    public void modifyLayouts(){
        // set heights and widths for each
        detailsDetinutHBox.prefWidthProperty().bind(layout.widthProperty().multiply(0.3));
        detailsDetinutHBox.prefHeightProperty().bind(layout.heightProperty());

        sentinteDetinutVBox.prefWidthProperty().bind(layout.widthProperty().multiply(0.7));
        sentinteDetinutVBox.prefHeightProperty().bind(layout.heightProperty());
        // also add a center dynamic

        sentinteDetinutVBox.setAlignment(Pos.CENTER);

        // we finished with the big layouts
        // we got to what is in each
        detailsDetinutHBox.setAlignment(javafx.geometry.Pos.BOTTOM_CENTER);
        // for debug reasons only
        // we test if the boxes are in parallel
        textLabel.prefHeightProperty().bind(detailsDetinutHBox.heightProperty().multiply(0.15));
        textLabel.prefWidthProperty().bind(detailsDetinutHBox.widthProperty());


        iconLabel.prefHeightProperty().bind(detailsDetinutHBox.heightProperty().multiply(0.15));
        iconLabel.prefWidthProperty().bind(detailsDetinutHBox.widthProperty());
        iconLabel.setWrapText(true);

        // se modifica toate dupa layout size
        imageBox.setAlignment(javafx.geometry.Pos.TOP_CENTER);
        imageBox.prefWidthProperty().bind(detailsDetinutHBox.widthProperty());
        imageBox.prefHeightProperty().bind(detailsDetinutHBox.heightProperty().multiply(0.7));


        imageView.fitWidthProperty().bind(imageBox.widthProperty());
        imageView.fitHeightProperty().bind(imageBox.heightProperty());
        imageView.setPreserveRatio(true);
//        Rectangle clip = new Rectangle(imageView.getFitWidth(), imageView.getFitHeight()); // Match ImageView dimensions
//        clip.setArcWidth(30);  // Set rounded corner width
//        clip.setArcHeight(30); // Set rounded corner height
//          this thing should add corners
//        // Apply the clip to the ImageView
//        imageView.setClip(clip);

        imageView.setStyle("-fx-alignment: center");

        // we put here the sentences table
        theTable.widthProperty().addListener((obs, oldVal, newVal) -> {
            resizeTable(theTable, newVal);
        });
        theTable.prefHeightProperty().bind(sentinteDetinutVBox.heightProperty().multiply(0.90));
        // imaginea va fi 1/2 din detaliiDetinutVBox
        //theTable.getStyleClass().add(getClass().getResource("../Interfaces/notSelectableRow.css").toExternalForm());

        layout.getStylesheets().add(getClass().getResource("ListItem.css").toExternalForm());
        iconLabel.getStyleClass().add(getClass().getResource("ListItem.css").toExternalForm());
        textLabel.getStyleClass().add(getClass().getResource("ListItem.css").toExternalForm());

    }
    public void loadTheTableOfSentences(String inmateId){
        TreeItem<Sentence> rootItem = new TreeItem<>(new Sentence());
        rootItem.setExpanded(true);
        theTable.setRoot(rootItem);
        theTable.setShowRoot(false);
        //
        theTable.setSelectionModel(null);



        // columns that should be named after their titles
        TreeTableColumn<Sentence, String> col2 = createColumn("Categorie", sentence -> sentence.categoryProperty());
        TreeTableColumn<Sentence, String> col5 = createColumn("Specific reason", sentence -> sentence.specificReasonProperty());
        TreeTableColumn<Sentence, String> col3 = createColumn("Start Time", sentence -> sentence.startTimeProperty());
        TreeTableColumn<Sentence, String> col4 = createColumn("End Time", person -> person.endTimeProperty());
        // now we need to add the date
        theTable.setRowFactory(tv -> {
            TreeTableRow<String> row = new TreeTableRow<>();

            row.setMinHeight(50); // Set minimum height
            return row;
        });

        theTable.getColumns().addAll(col2, col5, col3, col4);
        ArrayList<Sentence> theListOfSentences =  addItemsFromDatabase(inmateId);
        addDataToTreeTable(rootItem, theListOfSentences);
    }
    private void addDataToTreeTable(TreeItem<Sentence> rootItem, ArrayList<Sentence> data) {
        for(int i = 0; i < data.size(); i++) {

            TreeItem<Sentence> item = new TreeItem<>(data.get(i));

            rootItem.getChildren().add(item);
        }
    }

    private ArrayList<Sentence> addItemsFromDatabase(String inmateId) {
        ArrayList<Sentence> theListOfSentences = databaseConnector.getAllSentencesOfOneInmate(Integer.valueOf(inmateId));

        return theListOfSentences;
    }

    private TreeTableColumn<Sentence, String> createColumn(String title, Function<Sentence, ObservableValue<String>> mapper) {
        TreeTableColumn<Sentence, String> column = new TreeTableColumn<>(title);
        column.setCellValueFactory(cellData -> mapper.apply(cellData.getValue().getValue()));
        return column;
    }
    private void resizeTable(TreeTableView table, Number newVal) {
        double totalWidth = newVal.doubleValue();
        table.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
        for(int i = 0; i < table.getColumns().size(); i++) {
            TreeTableColumn tableColumn = (TreeTableColumn) table.getColumns().get(i);
            tableColumn.setPrefWidth(totalWidth * 0.15);
        }

    }
    @Override
    protected void updateItem(Inmates person, boolean empty) {
        super.updateItem(person, empty);

        if (empty || person == null) {
            setText(null);
            setGraphic(null);

        } else {
            textLabel.setText(person.getName().get());

            if(everyColumnIsEntered == false)
            {
                loadTheTableOfSentences(person.getid().get());
                everyColumnIsEntered = true;
            }
            // /Avatars/Inmates/"+ person.getName().get() +".jpg
            setImagePath("../Avatars/Inmates/"+ person.getName().get() +".jpg");
            imageView.setImage(image);
            iconLabel.setText("Sentence left: (" + person.getSentenceRemained().get() + ")");
            modifyLayouts();
            setGraphic(layout); // Set the VBox as the cell's graphic

        }
    }

    private void setImagePath(String imagePathString) {
        ///  format is:
        /// "../Avatars/Inmates/"+ person.getName().get() +".jpg" for each inmate
        /// replace Inmates with Guardians for guardians
        if(InterfataVizitator.class.getResource(imagePathString) != null){
            imagePath = InterfataVizitator.class.getResource(imagePathString).toExternalForm();
            image = new Image(imagePath);
        }
        else{
            System.out.println(InterfataVizitator.class.getResource(imagePathString));
            System.out.println(imagePathString);
        }
    }
}


