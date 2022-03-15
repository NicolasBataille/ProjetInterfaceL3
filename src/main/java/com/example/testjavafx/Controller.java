package com.example.testjavafx;

import ai.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import static ai.Test.*;

public class Controller {

    @FXML
    private ProgressBar progressBar;
    private Label label;
    private LearningTask learningTask;
    private Button startButton;



    public static void testThread(Stage stage) throws Exception{

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



//Test issu de la vidéo tuto de kouakou
//    @FXML
//    public void testThread() throws Exception{
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    //Partie qui gère le train de l'IA
//                    //.....
//                    //
//
//                    //permet d'effectuer des actions sur le JavaFX thread (donc modifier la progress bar par exemple)
//                    Platform.runLater(new Runnable() {
//                        @Override
//                        public void run() {
//
//                        }
//                    });
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//
//            }
//        }).start();
//    }




//    @FXML
//    protected void testThread(){
//        new EventHandler<ActionEvent>(){
//
//            @Override
//            public void handle(ActionEvent actionEvent) {
//                startButton.setDisable(true);
//                progressBar.setProgress(0.0);
//
//                learningTask = new LearningTask();
//
//                progressBar.progressProperty().unbind();
//                progressBar.progressProperty().bind(learningTask.progressProperty());
//
//                new Thread(learningTask).start();
//
//            }
//        };
//    }




    @FXML
    protected void affichageLearn(int size, HashMap<Integer, Coup> mapTrain, int h, double lr, int l, boolean verbose, double epochs){
        try {



            if ( verbose ) {
                System.out.println();
                System.out.println("START TRAINING ...");
                System.out.println();
            }
            //
            //			int[] layers = new int[]{ size, 128, 128, size };
            int[] layers = new int[l+2];
            layers[0] = size ;
            for (int i = 0; i < l; i++) {
                layers[i+1] = h ;
            }
            layers[layers.length-1] = size ;
            //
            double error = 0.0 ;
            MultiLayerPerceptron net = new MultiLayerPerceptron(layers, lr, new SigmoidalTransferFunction());

            if ( verbose ) {
                System.out.println("---");
                System.out.println("Load data ...");
                System.out.println("---");
            }
            //TRAINING ...
            for(int i = 0; i < epochs; i++){

                Coup c = null ;
                while ( c == null )
                    c = mapTrain.get((int)(Math.round(Math.random() * mapTrain.size())));

                error += net.backPropagate(c.in, c.out);

                if ( i % 10000 == 0 && verbose) System.out.println("Error at step "+i+" is "+ (error/(double)i));
            }
            if ( verbose )
                System.out.println("Learning completed!");


        }
        catch (Exception e) {
            System.out.println("Test.learn()");
            e.printStackTrace();
            System.exit(-1);
        }


    }


}