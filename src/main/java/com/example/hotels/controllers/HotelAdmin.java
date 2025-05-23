package com.example.hotels.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.Objects;

public class HotelAdmin {

    public Button add_btn;
    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;


    public void switch_add(ActionEvent event ) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/hotels/fxml/hotel_add.fxml"));
        stage= (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    
}
