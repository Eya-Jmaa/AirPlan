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

public class HotelAdmin {

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
    private TableColumn<Hebergement, Boolean> colStatus;
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

    public void initialize() {
        masterList.setAll(service.afficher());
        tableaccomodation.setItems(masterList);
        addButtonsToTable();

        namefilteragence.textProperty().addListener((obs, oldVal, newVal) -> filterList());
        countryfilteragence.textProperty().addListener((obs, oldVal, newVal) -> filterList());
        dispocomboagence.valueProperty().addListener((obs, oldVal, newVal) -> filterList());
        typecombifilteragence.valueProperty().addListener((obs, oldVal, newVal) -> filterList());
        typecombifilteragence.getItems().addAll("Hotel", "House", "Apartment", "Villa", "Hostel", "Bungalow");
        dispocomboagence.getItems().addAll("true", "false");

        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("pricePerNight"));
        colCapacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colRating.setCellValueFactory(new PropertyValueFactory<>("rating"));

        loadHebergements();

        tableaccomodation.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && tableaccomodation.getSelectionModel().getSelectedItem() != null) {
                Hebergement selected = tableaccomodation.getSelectionModel().getSelectedItem();
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hotels/fxml/hotel_info_agence.fxml"));
                    Parent root = loader.load();
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
    }

    private void loadHebergements() {
        masterList.setAll(service.afficher());
        tableaccomodation.setItems(masterList);
    }

    public void switch_add(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/hotels/fxml/hotel_add.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void filterList() {
        String name = namefilteragence.getText().toLowerCase();
        String country = countryfilteragence.getText().toLowerCase();
        String dispo = dispocomboagence.getValue();
        String type = typecombifilteragence.getValue();

        ObservableList<Hebergement> filteredList = masterList.filtered(h -> {
            boolean match = true;
            if (!name.isEmpty()) match &= h.getName().toLowerCase().contains(name);
            if (!country.isEmpty()) match &= h.getCountry().toLowerCase().contains(country);
            if (dispo != null && !dispo.isEmpty()) match &= String.valueOf(h.isDisponibility()).equals(dispo);
            if (type != null && !type.isEmpty()) match &= h.getType().equalsIgnoreCase(type);
            return match;
        });

        tableaccomodation.setItems(filteredList);
        tableaccomodation.refresh();
        addButtonsToTable();
    }

    @FXML
    private void onReset(ActionEvent event) {
        namefilteragence.clear();
        countryfilteragence.clear();
        dispocomboagence.setValue("");
        typecombifilteragence.setValue(null);
        loadHebergements();
        addButtonsToTable();
    }

    private void addButtonsToTable() {
        colActions.setCellFactory(param -> new TableCell<Hebergement, Void>() {
            Button btnAccept = new Button();
            ImageView acceptIcon = new ImageView(new Image(getClass().getResourceAsStream("/com/example/hotels/images/add.png")));
            Button btnReject = new Button();
            ImageView rejectIcon = new ImageView(new Image(getClass().getResourceAsStream("/com/example/hotels/images/annuler.png")));
            private final HBox pane = new HBox(10, btnAccept, btnReject);
            ServiceHebergement service = new ServiceHebergement();

            {
                acceptIcon.setFitWidth(16);
                acceptIcon.setFitHeight(16);
                btnAccept.setGraphic(acceptIcon);
                btnAccept.setTooltip(new Tooltip("Accept"));

                rejectIcon.setFitWidth(16);
                rejectIcon.setFitHeight(16);
                btnReject.setGraphic(rejectIcon);
                btnReject.setTooltip(new Tooltip("Refuse"));

                btnAccept.setOnAction(event -> {
                    Hebergement selected = getTableView().getItems().get(getIndex());
                    selected.setStatus("accepted");
                    service.modifier(selected);
                    masterList.set(masterList.indexOf(selected), selected);
                    showInfo("Statut de '" + selected.getName() + "' mis à jour : accepté");
                    getTableView().refresh();
                });

                btnReject.setOnAction(event -> {
                    Hebergement selected = getTableView().getItems().get(getIndex());
                    selected.setStatus("refused");
                    service.modifier(selected);
                    masterList.set(masterList.indexOf(selected), selected);
                    showInfo("Statut de '" + selected.getName() + "' mis à jour : refusé");
                    getTableView().refresh();
                });

                pane.setAlignment(Pos.CENTER);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableView().getItems().get(getIndex()) == null) {
                    setGraphic(null);
                } else {
                    Hebergement h = getTableView().getItems().get(getIndex());

                    // Désactiver si déjà accepté ou refusé
                    btnAccept.setDisable("accepted".equalsIgnoreCase(h.getStatus()));
                    btnReject.setDisable("refused".equalsIgnoreCase(h.getStatus()));

                    setGraphic(pane);
                }
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
