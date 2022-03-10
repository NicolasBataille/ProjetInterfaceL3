package com.example.testjavafx;

import ai.Coup;
import ai.MultiLayerPerceptron;
import ai.SigmoidalTransferFunction;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.util.HashMap;

public class Controller {

    @FXML
    private ProgressBar progressBar;
    private Label label;
    private LearningTask learningTask;
    private Button startButton;





    @FXML
    protected void testThread(){
        new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent actionEvent) {
                startButton.setDisable(true);
                progressBar.setProgress(0.0);

                learningTask = new LearningTask();

                progressBar.progressProperty().unbind();
                progressBar.progressProperty().bind(learningTask.progressProperty());

                new Thread(learningTask).start();

            }
        };
    }




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