package com.example.testjavafx;

import ai.Coup;
import ai.MultiLayerPerceptron;
import ai.SigmoidalTransferFunction;
import javafx.concurrent.Task;

import java.util.HashMap;

public class LearningTask extends Task<Double> {

    protected void call(int size, HashMap<Integer, Coup> mapTrain, int h, double lr, int l, boolean verbose, double epochs) throws Exception {
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

    @Override
    protected Double call() throws Exception {
        for(int i = 0; i<100; i++){
            this.updateProgress(i, i);
        }

        return 0.0;
    }
}
