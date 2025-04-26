package com.example.hotels.controllers;

import com.example.hotels.models.Hebergement;
import com.example.hotels.services.ServiceHebergement;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Hotel {


    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TextField countryField;
    @FXML
    private ComboBox<String> combotypeacc;


    @FXML
    private FlowPane hebergementContainer;
    private List<Hebergement> allHebergements = new ArrayList<>();

    public void initialize() {
        combotypeacc.getItems().addAll("Hotel", "House", "Apartment", "Villa","Hostel","Bungalow");

        // Créer une instance du service
        ServiceHebergement service = new ServiceHebergement();

        // Charger tous les hébergements avec un status "accepted"
        allHebergements = service.afficher().stream()
                .filter(h -> h.getStatus().equalsIgnoreCase("accepted"))
                .collect(Collectors.toList());

        // Afficher les hébergements filtrés
        displayHebergements(allHebergements);

        // Filtrage dynamique sur le champ de pays
        countryField.textProperty().addListener((obs, oldVal, newVal) -> {
            filterHebergementsByCountry(newVal.toLowerCase().trim());
        });
    }

    // Méthode pour afficher les hébergements dans le FlowPane
    private void displayHebergements(List<Hebergement> list) {
        hebergementContainer.getChildren().clear(); // On vide le FlowPane avant d'ajouter
        for (Hebergement h : list) {
            hebergementContainer.getChildren().add(createHebergementCard(h));
        }
    }

    // Méthode pour filtrer les hébergements selon le pays
    private void filterHebergementsByCountry(String country) {
        List<Hebergement> filtered = allHebergements.stream()
                .filter(h -> h.getCountry().toLowerCase().contains(country))
                .collect(Collectors.toList());

        displayHebergements(filtered); // Mettre à jour l'affichage avec les hébergements filtrés
    }



    public AnchorPane createHebergementCard(Hebergement h) {
        AnchorPane card = new AnchorPane();
        card.setPrefSize(200, 250);
        card.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 12; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        VBox contentBox = new VBox(8);
        contentBox.setPadding(new Insets(10));
        contentBox.setAlignment(Pos.CENTER);

        // Image centrée
        ImageView imageView = new ImageView();
        try {
            Image img = new Image(h.getPhoto(), 180, 100, true, true);
            imageView.setImage(img);
            imageView.setSmooth(true);
            imageView.setStyle("-fx-border-radius: 10; -fx-background-radius: 10;");

            StackPane imagePane = new StackPane();
            imagePane.getChildren().add(imageView);
            StackPane.setAlignment(imageView, Pos.CENTER);

            contentBox.getChildren().add(imagePane);
        } catch (Exception e) {
            System.out.println("Erreur chargement image: " + h.getPhoto());
        }

        // Nom
        Label nameLabel = new Label(h.getName());
        nameLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");

        // Country et City
        Label locationLabel = new Label(h.getCity() + ", " + h.getCountry());
        locationLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #666;");

        // Étoiles
        HBox starsBox = new HBox(2);
        starsBox.setAlignment(Pos.CENTER);
        for (int i = 0; i < h.getRating(); i++) {
            Label star = new Label("★");
            star.setStyle("-fx-text-fill: gold; -fx-font-size: 14px;");
            starsBox.getChildren().add(star);
        }

        // Prix
        Label priceLabel = new Label("Prix: " + h.getPricePerNight() + "€");
        priceLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #333;");
        priceLabel.setAlignment(Pos.CENTER_LEFT);

        contentBox.getChildren().addAll(nameLabel, locationLabel, starsBox, priceLabel);
        card.getChildren().add(contentBox);

        // Double clic
        card.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hotels/fxml/hotel_info_agence.fxml"));
                    Parent root = loader.load();

                    HotelInfo controller = loader.getController();
                    controller.setHebergementDetails(h);

                    Stage stage = new Stage();
                    stage.setTitle("Détails de l'hébergement");
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        return card;
    }


}


