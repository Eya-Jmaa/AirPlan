package com.example.hotels.controllers;

import com.example.hotels.models.Hebergement;
import com.example.hotels.services.ServiceHebergement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ClientAcc {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private FlowPane flowPane;

    ServiceHebergement service = new ServiceHebergement();
    @FXML
    private ComboBox pricecombo;
    @FXML
    private TextField destinationfiled;

    private List<Hebergement> hotelList = new ArrayList<>();
    private List<Hebergement> displayedHotels = new ArrayList<>();


    @FXML
    public void initialize() {
          // Marge autour du FlowPane
        scrollPane.setBackground(null);
        flowPane.setBackground(null);

        flowPane.setHgap(20);
        flowPane.setVgap(20);
        flowPane.setPadding(new Insets(20));

        pricecombo.getItems().addAll("Ascending Price", "Descending Price", "Rating");
        pricecombo.setOnAction(event -> sortHotels());
        destinationfiled.textProperty().addListener((observable, oldValue, newValue) -> filterHotels());


        hotelList = service.afficher();
        displayedHotels = new ArrayList<>(hotelList);
        updateHotelCards(displayedHotels);


    }
    private void filterHotels() {
        String keyword = destinationfiled.getText().toLowerCase();
        displayedHotels = hotelList.stream()
                .filter(hotel -> hotel.getName().toLowerCase().contains(keyword)
                        || hotel.getCountry().toLowerCase().contains(keyword))

                .collect(Collectors.toList());
        updateHotelCards(displayedHotels);
    }


    private void sortHotels() {
        String selected = (String) pricecombo.getSelectionModel().getSelectedItem();

        if (selected == null) return;

        switch (selected) {
            case "Ascending Price":
                displayedHotels.sort(Comparator.comparingDouble(Hebergement::getPricePerNight));
                break;
            case "Descending Price":
                displayedHotels.sort(Comparator.comparingDouble(Hebergement::getPricePerNight).reversed());
                break;
            case "Rating":
                displayedHotels.sort(Comparator.comparingDouble(Hebergement::getRating).reversed());
                break;
        }

        updateHotelCards(displayedHotels);
    }


    private void updateHotelCards(List<Hebergement> hebergements) {
        flowPane.getChildren().clear();
        for (Hebergement hebergement : hebergements) {
            VBox card = createCard(hebergement);
            flowPane.getChildren().add(card);
        }
    }


    private void loadHebergements() {

        List<Hebergement> hebergements = service.afficher();


        for (Hebergement h : hebergements) {
            VBox card = createCard(h);
            flowPane.getChildren().add(card);
        }
    }

    private VBox createCard(Hebergement hebergement) {
        VBox carte = new VBox();
        carte.setPrefWidth(250);
        carte.setPadding(new Insets(10));
        carte.setSpacing(10);
        carte.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10; "
                + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5); "
                + "-fx-margin: 10;");
        ImageView imageView = new ImageView();
        try {
            Image img = new Image(hebergement.getPhoto(), 200, 150, true, true);
            imageView.setImage(img);
            imageView.setFitWidth(200);
            imageView.setFitHeight(150);
            imageView.setSmooth(true);
            imageView.setPreserveRatio(true);
            imageView.setStyle("-fx-border-radius: 10; -fx-background-radius: 10;");
        } catch (Exception e) {
            System.out.println("Erreur chargement image: " + hebergement.getPhoto());
        }

        Label labelTitre = new Label(hebergement.getName());
        labelTitre.setFont(new Font("Arial", 16));
        labelTitre.setTextFill(Color.web("#333"));

        Label labelDescription = new Label(hebergement.getDescription());
        labelDescription.setFont(new Font("Arial", 12));
        labelDescription.setWrapText(true);
        labelDescription.setTextFill(Color.GRAY);

        Label labelPrix = new Label(String.format("%.2f TND ", hebergement.getPricePerNight()));
        labelPrix.setFont(new Font("Arial", 14));
        labelPrix.setTextFill(Color.web("#009688"));

        // Étoiles
        HBox starsBox = new HBox(2);

        for (int i = 0; i < hebergement.getRating(); i++) {
            Label star = new Label("★");
            star.setStyle("-fx-text-fill: gold; -fx-font-size: 14px;");
            starsBox.getChildren().add(star);
        }
    // favoris

        Image imgHeartEmpty = new Image(getClass().getResource("/com/example/hotels/images/heart_empty.png").toExternalForm());
        Image imgHeartFull = new Image(getClass().getResource("/com/example/hotels/images/heart_full.png").toExternalForm());



        ImageView heartView = new ImageView(imgHeartEmpty);
        heartView.setFitWidth(24);
        heartView.setFitHeight(24);


        Button btnFavori = new Button();
        btnFavori.setGraphic(heartView);
        btnFavori.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");

        final boolean[] isFavori = {false}; // état favori


        btnFavori.setOnAction(e -> {
            isFavori[0] = !isFavori[0];
            if (isFavori[0]) {
                heartView.setImage(imgHeartFull);  // Cœur plein rouge
            } else {
                heartView.setImage(imgHeartEmpty); // Cœur vide
            }
        });

    //res

        Button btnReserver = new Button("Réserver");
        btnReserver.setStyle("-fx-background-color: #588b8b; -fx-text-fill: white; -fx-background-radius: 5;");
        btnReserver.setVisible(false); // Caché au début

        btnReserver.setOnAction(event -> openReservation(hebergement, event));



        HBox btnContainer = new HBox(btnReserver);
        btnContainer.setAlignment(Pos.BOTTOM_RIGHT);
        btnContainer.setPadding(new Insets(0, 5, 5, 0));


// Survol
        carte.setOnMouseEntered(e -> {
            btnReserver.setVisible(true); // Cacher/montrer juste le bouton
            carte.setStyle("-fx-background-color: #f1f1f1; -fx-background-radius: 10; "
                    + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 5);");
        });
        carte.setOnMouseExited(e -> {
            btnReserver.setVisible(false);
            carte.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10; "
                    + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");
        });


        carte.getChildren().addAll(imageView, labelTitre, labelDescription, labelPrix, starsBox, btnFavori, btnContainer);



        // Double clic
        carte.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Double-clic
                openHotelInfo(hebergement);
            }
        });

        return carte;
    }



    private void openHotelInfo(Hebergement hebergement) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hotels/fxml/hotel_info_client.fxml"));
            Parent root = loader.load();

            HotelInfoClient hotelInfoController = loader.getController();

            hotelInfoController.setHebergementDetails(hebergement);

            Scene currentScene = scrollPane.getScene();
            currentScene.setRoot(root);  // Remplacer le contenu de la scène avec la nouvelle vue




        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void openReservation(Hebergement hebergement, ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hotels/fxml/reservationclient.fxml"));
            Parent root = loader.load();


            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();


            ReservationClient controller = loader.getController();


            controller.setHebergementData(hebergement);


            Scene newScene = new Scene(root);
            stage.setScene(newScene);
            stage.show();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }





}
