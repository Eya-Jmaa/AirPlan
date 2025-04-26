package com.example.hotels.controllers;

import com.example.hotels.models.Hebergement;
import com.example.hotels.services.ServiceHebergement;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HotelAdd  {

    @FXML private TextField namefield;

    @FXML private TextField cityfield;
    @FXML private TextField addressfield;
    @FXML private TextField countryfield;
    @FXML private TextField pricefield;
    @FXML private Label albumlabel;
    @FXML private TextArea descriptionfield;
    @FXML private HBox getStarBox;
    @FXML private Spinner capacityspinner;
    @FXML private CheckBox wifi, pool, meals, air, parking;
    @FXML private ComboBox<String> typeCombo;
    @FXML private Label fileLabel;
    @FXML private HBox starBox;
    @FXML private Stage stage;
    @FXML private Scene scene;

    private File selectedFile;
    private final IntegerProperty rating = new SimpleIntegerProperty(0);
    private List<String> selectedOptions = new ArrayList<>();
    private Hebergement hebergementToEdit ;
    boolean disponibility;

    @FXML
    public void initialize() {

        SpinnerValueFactory<Integer> capacityFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 5000, 0); // min, max, valeur par défaut
        capacityspinner.setValueFactory(capacityFactory);
        typeCombo.getItems().addAll("Hotel", "House", "Apartment", "Villa","Hostel","Bungalow");
        setupOptionCheckboxes();

        // Setup stars
        int maxStars = 5;
        Label[] stars = new Label[maxStars];

        for (int i = 0; i < maxStars; i++) {
            final int index = i;
            Label star = new Label("☆");
            star.setStyle("-fx-font-size: 24px; -fx-text-fill: #f39c12; -fx-cursor: hand;");
            star.setOnMouseClicked(e -> rating.set(index + 1));
            stars[i] = star;
            starBox.getChildren().add(star);
        }

        rating.addListener((obs, oldVal, newVal) -> {
            for (int i = 0; i < maxStars; i++) {
                stars[i].setText(i < newVal.intValue() ? "★" : "☆");
            }
        });



    }

    @FXML
    private void handleChooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file");
        selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            fileLabel.setText(selectedFile.getAbsolutePath());
        } else {
            fileLabel.setText("No file selected");
        }
    }
    @FXML
    private void handleChooseAlbum() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir des images pour l'album");

// Filtrer uniquement les images
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png", "*.jpeg");
        fileChooser.getExtensionFilters().add(imageFilter);

// Choisir plusieurs fichiers
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);

        if (selectedFiles != null) {
            List<String> imagePaths = new ArrayList<>();
            for (File file : selectedFiles) {
                imagePaths.add(file.getAbsolutePath());
            }

            // Exemple : stocker le chemin en base séparé par des virgules
            String albumPath = String.join("\n", imagePaths);
            albumlabel.setText(albumPath);
            System.out.println("Album sélectionné : " + albumPath);}

    }


    private void setupOptionCheckboxes() {
        setupCheckboxListener(wifi, "Wi-Fi");
        setupCheckboxListener(pool, "Pool");
        setupCheckboxListener(meals, "All Meals Included");
        setupCheckboxListener(air, "Air Conditioning");
        setupCheckboxListener(parking, "Parking");
    }

    private void setupCheckboxListener(CheckBox checkbox, String label) {
        checkbox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            if (isNowSelected) {
                selectedOptions.add(label);
            } else {
                selectedOptions.remove(label);
            }
            String joined = String.join(", ", selectedOptions);
            System.out.println("Options sélectionnées : " + joined);
        });

    }



    private int getRatingFromStars() {
        int rating = 0;
        for (Node node : getStarBox.getChildren()) {
            if (node instanceof ImageView) {
                ImageView star = (ImageView) node;
                if (star.getStyleClass().contains("selected")) {
                    rating++;
                }
            }
        }
        return rating;
    }

    @FXML
    private void handleSubmit(ActionEvent event) {
        try {
            // Validation simple
            if (namefield.getText().isEmpty() || typeCombo.getValue() == null || cityfield.getText().isEmpty()) {
                showAlert("Veuillez remplir les champs obligatoires.", Alert.AlertType.WARNING);
                return;
            }

            String name = namefield.getText();
            String type = typeCombo.getValue();
            String city = cityfield.getText();
            String address = addressfield.getText();
            String country = countryfield.getText();
            double price = Double.parseDouble(pricefield.getText());
            String photo = fileLabel.getText();
            String album = albumlabel.getText();
            String description = descriptionfield.getText();
            int rating = this.rating.get();
            int capacity = (int) capacityspinner.getValue();
            disponibility = capacity != 0;
            String options = String.join(", ", selectedOptions);
            String status = "waiting";

            ServiceHebergement sh = new ServiceHebergement();

            if (hebergementToEdit == null) {
                // ➕ Ajout
                Hebergement h = new Hebergement(name, type, city, address, country, price, disponibility, photo, album, description, options, rating, capacity,status);

                sh.ajouter(h);
                showAlert("Ajouté avec succès !", Alert.AlertType.INFORMATION);
                clearForm();
            } else {
                // 🔁 Modification
                hebergementToEdit.setName(name);
                hebergementToEdit.setType(type);
                hebergementToEdit.setCity(city);
                hebergementToEdit.setAddress(address);
                hebergementToEdit.setCountry(country);
                hebergementToEdit.setPricePerNight(price);
                hebergementToEdit.setPhoto(photo);
                hebergementToEdit.setAlbum(album);
                hebergementToEdit.setDescription(description);
                hebergementToEdit.setOptions(options);
                hebergementToEdit.setRating(rating);
                hebergementToEdit.setCapacity(capacity);
                hebergementToEdit.setDisponibility(disponibility);


                sh.modifier(hebergementToEdit);
                showAlert("Modification réussie !", Alert.AlertType.INFORMATION);
                hebergementToEdit = null;
            }

            // 🧼 Réinitialisation du formulaire


        } catch (NumberFormatException e) {
            showAlert("Le prix doit être un nombre valide.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Une erreur est survenue.", Alert.AlertType.ERROR);
        }
    }
    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearForm() {
        namefield.clear();
        typeCombo.setValue(null);
        cityfield.clear();
        addressfield.clear();
        countryfield.clear();
        pricefield.clear();
        fileLabel.setText("");
        albumlabel.setText("");
        descriptionfield.clear();

        // Réinitialiser les CheckBox
        wifi.setSelected(false);
        pool.setSelected(false);
        meals.setSelected(false);
        air.setSelected(false);
        parking.setSelected(false);
        selectedOptions.clear();

        capacityspinner.getValueFactory().setValue(0);
        rating.set(0);
        disponibility = false;
    }



    public void initData(Hebergement h) {
        this.hebergementToEdit = h;
        namefield.setText(h.getName());
        typeCombo.setValue(h.getType());
        cityfield.setText(h.getCity());
        addressfield.setText(h.getAddress());
        countryfield.setText(h.getCountry());
        pricefield.setText(String.valueOf(h.getPricePerNight()));
        fileLabel.setText(h.getPhoto()); // ou extraire le nom du fichier si nécessaire
        albumlabel.setText(h.getAlbum()); // si jamais tu veux le réutiliser
        descriptionfield.setText(h.getDescription());
        rating.set(h.getRating());
        capacityspinner.getValueFactory().setValue(h.getCapacity());
        // Si la capacité est > 0, on suppose que c'est disponible
        // Mais tu peux aussi utiliser : disponibiliteCheckBox.setSelected(h.isDisponibility());
        disponibility=(h.getCapacity() != 0);

        String options = h.getOptions(); // exemple: "Wi-Fi, Pool, Parking"

        if (options.contains("Wi-Fi")) wifi.setSelected(true);
        if (options.contains("Pool")) pool.setSelected(true);
        if (options.contains("Parking")) parking.setSelected(true);
        if (options.contains("All Meals Included")) meals.setSelected(true);
        if (options.contains("Air Conditioning")) air.setSelected(true);




    }

    public void switch_admin(ActionEvent event ) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/hotels/fxml/Accommodation.fxml")));
        stage= (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

}