package com.example.hotels.controllers;

import com.example.hotels.models.Hebergement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class ReservationClient {
    @javafx.fxml.FXML
    private ImageView imagehebergement;
    @javafx.fxml.FXML
    private Label CountryCity;
    @javafx.fxml.FXML
    private Label nameheber;
    @javafx.fxml.FXML
    private Label optionheber;
    @javafx.fxml.FXML
    private Label priceheber;
    @javafx.fxml.FXML
    private Label typeheber;
    @javafx.fxml.FXML
    private Label ratingheber;
    @javafx.fxml.FXML
    private DatePicker departuredate;
    @javafx.fxml.FXML
    private TextArea requestarea;
    @javafx.fxml.FXML
    private Spinner childrenspinner;
    @javafx.fxml.FXML
    private DatePicker arrivaldate;
    @javafx.fxml.FXML
    private Spinner adultspinner;
    @javafx.fxml.FXML
    private Spinner roomspinner;
    @javafx.fxml.FXML
    private Button btnCancel;
    @javafx.fxml.FXML
    private Button btnReserve;

    public void setHebergementData(Hebergement hebergement) {
        if (hebergement != null) {
            Image img = new Image(hebergement.getPhoto(), 200, 150, true, true);
            imagehebergement.setImage(img);
            CountryCity.setText(hebergement.getCountry() + " , " + hebergement.getCity());
            nameheber.setText(hebergement.getName());
            optionheber.setText(hebergement.getOptions()); // (ou autre champ selon ton modèle)
            priceheber.setText(String.format("%.2f TND", hebergement.getPricePerNight()));
            typeheber.setText(hebergement.getType());
            ratingheber.setText(getStarRating(hebergement.getRating()));

        }
    }

    private String getStarRating(double rating) {
        int fullStars = (int) rating;
        boolean halfStar = (rating - fullStars) >= 0.5;

        StringBuilder stars = new StringBuilder();

        for (int i = 0; i < fullStars; i++) {
            stars.append("★"); // étoile pleine
        }

        if (halfStar) {
            stars.append("☆"); // étoile vide pour demi-étoile (ou utilise autre chose si tu veux)
        }

        while (stars.length() < 5) {
            stars.append("☆"); // compléter jusqu'à 5 étoiles
        }

        return stars.toString();

    }

    @FXML
    private void handleCancelAction(ActionEvent event) {
        try {
            // Charger le fichier FXML de ClientAcc
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hotels/fxml/clien_acc.fxml"));
            Parent root = loader.load();

            // Récupérer le stage de la scène actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Appliquer la nouvelle scène au stage actuel
            Scene newScene = new Scene(root);
            stage.setScene(newScene);
            stage.show();  // Afficher le stage avec la nouvelle scène
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }



}
