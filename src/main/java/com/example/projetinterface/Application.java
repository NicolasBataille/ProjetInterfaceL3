package com.example.projetinterface;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("AITraining.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        final Button startButton = (Button) scene.lookup("#startButton");   //récupère l'entité bouton


        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    AITrainingController.trainAI(stage);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        stage.setTitle("Morpion");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}