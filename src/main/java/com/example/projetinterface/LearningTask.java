package com.example.projetinterface;

import ai.Coup;
import ai.MultiLayerPerceptron;
import ai.SigmoidalTransferFunction;
import javafx.concurrent.Task;

import java.util.HashMap;

public class LearningTask extends Task<Double> {

    //attributs nécessaires pour l'entrainement de l'IA
    private int size;
    private HashMap<Integer, Coup> mapTrain;
    private int h;
    private double lr;
    private int l;
    private boolean verbose;
    private double epochs;


    public LearningTask(int size, HashMap<Integer, Coup> mapTrain, int h, double lr, int l, boolean verbose, double epochs){
        this.size = size;
        this.mapTrain = mapTrain;
        this.h = h;
        this.lr = lr;
        this.l = l;
        this.verbose = verbose;
        this. epochs = epochs;
    }

    protected Double call() throws Exception {
        try {

            if ( verbose ) {
                System.out.println();
                System.out.println("START TRAINING ...");
                System.out.println();
            }
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

                //update du message donnant la valeur de l'erreur
                if ( i % 10000 == 0 && verbose){
                    this.updateMessage("erreur : " + (error/(double)i));
                }
                //update de la valeur de la progressbar
                if(i % 1000 == 0){
                    this.updateProgress(i, epochs);
                }
            }
            if ( verbose ){
                this.updateMessage("Fin du train !");
            }

            if(net.save("src/main/resources/models/F.srl")){
                System.out.println("good");
            }
            else{
                System.out.println("aps good");
            }
            System.out.println(error);
            return error;

        }
        catch (Exception e) {
            System.out.println("Test.learn()");
            e.printStackTrace();
            System.exit(-1);
        }

        return null;
    }


}
