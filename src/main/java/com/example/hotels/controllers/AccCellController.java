package com.example.hotels.controllers;

import com.example.hotels.models.Hebergement;
import com.example.hotels.services.ServiceHebergement;
import com.example.hotels.view.AccCellFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;

public class AccCellController implements Initializable {

    @FXML
    private Label price_lbl;
    @FXML
    private Label type_lbl;
    @FXML
    private Label status_lbl;
    @FXML
    private Label name_lbl;
    @FXML
    private Label city_lbl;
    @FXML
    private Label country_lbl;
    private  Hebergement hebergement;
    private ListView<Hebergement> listHebergement; // pas @FXML !

    // Setter pour la lier manuellement
    public void setListHebergement(ListView<Hebergement> listHebergement) {
        this.listHebergement = listHebergement;
    }


    private ObservableList<Hebergement> list;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnEdit;
    private ServiceHebergement service = new ServiceHebergement(); // à ajouter


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        btnEdit.setOnAction(event -> {
            if (hebergement != null) {
                openModifyHebergementWindow(hebergement);
            }
        });

        btnDelete.setOnAction(event -> {
            if (hebergement != null) {
                deleteHebergement(hebergement);
            }
        });
    }



    private void openModifyHebergementWindow(Hebergement hebergement) {
        try {
            // Charger le fichier FXML de la scène de modification de l'hébergement
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/hotels/fxml/hotel_add.fxml")); // Chemin correct ici
            Parent root = loader.load();

            // Récupérer le contrôleur de hotel_add
            HotelAdd controller = loader.getController();

            // Passer les données au contrôleur
            controller.initData(hebergement);

            // Obtenir la scène actuelle et changer le root (la vue)
            Scene currentScene = btnEdit.getScene(); // Ici tu utilises n'importe quel composant de ta fenêtre actuelle pour obtenir la scène
            currentScene.setRoot(root); // Remplacer le contenu de la scène avec la nouvelle vue

            // Tu peux aussi ajuster des propriétés de la scène si nécessaire
            // Ajuste la hauteur de la fenêtre

            // Pas besoin de fermer la fenêtre ou de créer un nouveau Stage, on remplace juste le contenu de la scène actuelle

        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    private void deleteHebergement(Hebergement selectedHebergement) {
        // Afficher une boîte de dialogue de confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer l’hébergement");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cet hébergement ?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Supprimer de la base de données
                service.supprimer(selectedHebergement.getId());

                // Supprimer de la ListView seulement si elle est disponible
                if (listHebergement != null) {
                    listHebergement.getItems().remove(selectedHebergement);

                    // Rafraîchir la liste
                    updateAccData();
                } else {
                    System.out.println("⚠️ listHebergement est null, impossible de rafraîchir.");
                }
            }
        });
    }



    public void setHebergement(Hebergement hebergement) {
        this.hebergement = hebergement;
        updateAccData();
    }

    private void updateAccData() {
        if (hebergement != null) {
            country_lbl.setText(hebergement.getCountry());
            status_lbl.setText(hebergement.getStatus());
            city_lbl.setText(hebergement.getCity());
            name_lbl.setText(hebergement.getName());
            type_lbl.setText(hebergement.getType());
            price_lbl.setText(String.format("TND %.2f", hebergement.getPricePerNight()));



        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void showErrorAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}