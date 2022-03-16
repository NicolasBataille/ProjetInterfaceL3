package com.example.projetinterface;

import ai.*;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

import java.util.HashMap;

import static ai.Test.*;

public class AITrainingController {


    @FXML
    public static void trainAI(Stage stage) throws Exception{

        //définit les paramètres d'entraînement de l'IA
        HashMap<Integer, Coup> coups = loadGames("./src/main/resources/dataset/Tic_tac_initial_results.csv");
        saveGames(coups, "./src/main/resources/train_dev_test/", 0.7);

        ConfigFileLoader cfl = new ConfigFileLoader();
        cfl.loadConfigFile("./src/main/resources/config.txt");
        Config config = cfl.get("F");	//charge le modèle facile
        System.out.println("Test.main() : "+config);
        double epochs = 100000 ;
        HashMap<Integer, Coup> mapTrain = loadCoupsFromFile("./src/main/resources/train_dev_test/train.txt");



        Scene scene = stage.getScene();
        final Label text = (Label) scene.lookup("#testLabel");  //récup le label de test
        final ProgressBar progressBar = (ProgressBar) scene.lookup("#progressBar");
        LearningTask learningTask = new LearningTask(9, mapTrain, config.hiddenLayerSize, config.learningRate, config.numberOfhiddenLayers, true, epochs);

        //on gère les bind de la progressbar et du label
        text.textProperty().unbind();
        text.textProperty().bind(learningTask.messageProperty());

        progressBar.progressProperty().unbind();
        progressBar.progressProperty().bind(learningTask.progressProperty());

        //lance le training en parallèle
        new Thread(learningTask).start();

    }


}