package com.example.hotels.controllers;

import com.example.hotels.models.Hebergement;
import com.example.hotels.services.ServiceHebergement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.io.IOException;


public class HotelAgence {

    public Button add_btn;
    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TableView<Hebergement> tableaccomodation;
    @FXML
    private TableColumn<Hebergement, String> colName;
    @FXML
    private TableColumn<Hebergement, String> colType;
    @FXML
    private TableColumn<Hebergement, String> colCountry;
    @FXML
    private TableColumn<Hebergement, String> colCity;
    @FXML
    private TableColumn<Hebergement, Double> colPrice;
    @FXML
    private TableColumn<Hebergement, Integer> colCapacity;
    @FXML
    private TableColumn<Hebergement, Boolean> colDispo;
    @FXML
    private TableColumn<Hebergement, Integer> colRating;
    @FXML
    private TableColumn<Hebergement, Void> colActions;

    @FXML
    private TextField namefilteragence;
    @FXML
    private TextField countryfilteragence;
    @FXML
    private ComboBox<String> typecombifilteragence;
    @FXML
    private ComboBox<String> dispocomboagence;

    private ObservableList<Hebergement> masterList = FXCollections.observableArrayList();
    private ServiceHebergement service = new ServiceHebergement();




//affichaaageeeee

    public void initialize() {
        //recherche

        masterList.setAll(service.afficher());
        tableaccomodation.setItems(masterList);
        addButtonsToTable();


        namefilteragence.textProperty().addListener((obs, oldVal, newVal) -> filterList());
        countryfilteragence.textProperty().addListener((obs, oldVal, newVal) -> filterList());
        dispocomboagence.valueProperty().addListener((obs, oldVal, newVal) -> filterList());
        typecombifilteragence.valueProperty().addListener((obs, oldVal, newVal) -> filterList());
        typecombifilteragence.getItems().addAll("Hotel", "House", "Apartment", "Villa","Hostel","Bungalow");
        dispocomboagence.getItems().addAll("true", "false");




        // Initialisation des colonnes
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("pricePerNight"));
        colCapacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        colDispo.setCellValueFactory(new PropertyValueFactory<>("disponibility"));
        colRating.setCellValueFactory(new PropertyValueFactory<>("rating"));

        // Charger les hébergements
        loadHebergements();


        tableaccomodation.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && tableaccomodation.getSelectionModel().getSelectedItem() != null) {
                Hebergement selected = tableaccomodation.getSelectionModel().getSelectedItem();

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hotels/fxml/hotel_info_agence.fxml"));
                    Parent root = loader.load();

                    // Passe les données au contrôleur de la fenêtre des détails
                    HotelInfo controller = loader.getController();
                    controller.setHebergementDetails(selected);

                    Stage stage = new Stage();
                    stage.setTitle("Détails de l'hébergement");
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        // Ajouter les actions (modifier / supprimer)
        colActions.setCellFactory(param -> new TableCell<Hebergement, Void>() {
            Button btnEdit = new Button();
            ImageView editIcon = new ImageView(new Image(getClass().getResourceAsStream("/com/example/hotels/images/modifier.png")));
            Button btnDelete = new Button();
            ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/com/example/hotels/images/annuler.png")));
            private final HBox pane = new HBox(10, btnEdit, btnDelete);

            {
                // Configurer les boutons
                editIcon.setFitWidth(16);
                editIcon.setFitHeight(16);
                btnEdit.setGraphic(editIcon);
                //btnEdit.setStyle("-fx-background-color: transparent;");

                deleteIcon.setFitWidth(16);
                deleteIcon.setFitHeight(16);
                btnDelete.setGraphic(deleteIcon);
                //btnDelete.setStyle("-fx-background-color: transparent;");

                // Action Modifier
                btnEdit.setOnAction(event -> {
                    Hebergement selected = getTableView().getItems().get(getIndex());


                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hotels/fxml/hotel_add.fxml"));
                        Parent root = loader.load();
                        stage= (Stage) ((Node)event.getSource()).getScene().getWindow();
                        scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();

                        // Passer l'hébergement au contrôleur
                        HotelAdd controller = loader.getController();
                        controller.initData(selected);

                        loadHebergements(); // rafraîchir après modif
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });


                // Action Supprimer
                btnDelete.setOnAction(event -> {
                    Hebergement selected = getTableView().getItems().get(getIndex());

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation de suppression");
                    alert.setHeaderText("Supprimer l’hébergement");
                    alert.setContentText("Êtes-vous sûr de vouloir supprimer cet hébergement ?");

                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            // Supprimer dans la base de données
                            service.supprimer(selected.getId());

                            // Supprimer dans la ObservableList de la TableView
                            getTableView().getItems().remove(selected);

                            // Actualiser la table avec la nouvelle liste d'éléments
                            loadHebergements();
                        }
                    });
                });




                pane.setAlignment(Pos.CENTER);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }



    //load tab

    private void loadHebergements() {
        ServiceHebergement service = new ServiceHebergement();
        ObservableList<Hebergement> list = FXCollections.observableArrayList(service.afficher());
        tableaccomodation.setItems(list);
    }


    //switch add
        public void switch_add(ActionEvent event ) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/hotels/fxml/hotel_add.fxml"));
        stage= (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
    //list



    //recherche

    private void filterList() {
        String name = namefilteragence.getText().toLowerCase();
        String country = countryfilteragence.getText().toLowerCase();
        String dispo = dispocomboagence.getValue();
        String type = typecombifilteragence.getValue();

        ObservableList<Hebergement> filteredList = masterList.filtered(h -> {
            boolean match = true;

            if (!name.isEmpty()) {
                match &= h.getName().toLowerCase().contains(name);
            }
            if (!country.isEmpty()) {
                match &= h.getCountry().toLowerCase().contains(country);
            }
            if (dispo != null && !dispo.isEmpty()) {
                match &= String.valueOf(h.isDisponibility()).equals(dispo);
            }
            if (type != null && !type.isEmpty()) {
                match &= h.getType().equalsIgnoreCase(type);
            }

            return match;
        });

        tableaccomodation.setItems(filteredList);
        tableaccomodation.refresh(); // Important
        addButtonsToTable();
    }


    @FXML
    private void onReset(ActionEvent event) {
        // Réinitialiser les filtres
        namefilteragence.clear();
        countryfilteragence.clear();
        dispocomboagence.setValue("");
        typecombifilteragence.setValue(null);

        // Recharger les données actualisées après suppression
        loadHebergements();  // Recharger la table avec les éléments actuels depuis la base de données

        // Réinitialiser les boutons de la table
        addButtonsToTable();
    }


    private void addButtonsToTable() {
        colActions.setCellFactory(param -> new TableCell<Hebergement, Void>() {
            Button btnEdit = new Button();
            ImageView editIcon = new ImageView(new Image(getClass().getResourceAsStream("/com/example/hotels/images/modifier.png")));
            Button btnDelete = new Button();
            ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/com/example/hotels/images/annuler.png")));
            private final HBox pane = new HBox(10, btnEdit, btnDelete);

            {
                editIcon.setFitWidth(16);
                editIcon.setFitHeight(16);
                btnEdit.setGraphic(editIcon);

                deleteIcon.setFitWidth(16);
                deleteIcon.setFitHeight(16);
                btnDelete.setGraphic(deleteIcon);

                btnEdit.setOnAction(event -> {
                    Hebergement selected = getTableView().getItems().get(getIndex());
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hotels/fxml/hotel_add.fxml"));
                        Parent root = loader.load();
                        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();

                        HotelAdd controller = loader.getController();
                        controller.initData(selected);

                        loadHebergements(); // rafraîchir après modif
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                btnDelete.setOnAction(event -> {
                    Hebergement selected = getTableView().getItems().get(getIndex());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText("Voulez-vous vraiment supprimer cet hébergement ?");
                    alert.setContentText(selected.getName());

                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            service.supprimer(selected.getId());
                            getTableView().getItems().remove(selected);
                            showInfo("Hébergement supprimé !");
                        }
                    });
                });

                pane.setAlignment(Pos.CENTER);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });



    }
    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }








}
