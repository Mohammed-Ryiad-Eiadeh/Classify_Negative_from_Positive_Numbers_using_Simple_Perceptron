package PerceptronModel.org;

import ActivationFun.org.ActivationFunction;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.SplittableRandom;
import java.util.stream.IntStream;

public class Perceptron implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L; // Add this line
    private double weight;
    private double bias;
    private final double learningRate;
    private final int epoch;
    private final ActivationFunction activationFunction;
    private int errorCounter;

    public Perceptron(double weight, double bias, ActivationFunction activationFunction, double learningRate, int epoch) {
        this.weight = weight;
        this.bias = bias;
        this.activationFunction = activationFunction;
        this.learningRate = learningRate;
        this.epoch = epoch;
    }

    public void train(HashMap<Integer, Double> trainSet) {
        System.out.println("MLP performance : ");
        IntStream.range(0, epoch).forEach(iter -> {
            {
                int errorCounter = 0;
                for (int instance = 0; instance < trainSet.size(); instance++) {
                    int p = trainSet.keySet().toArray(Integer[]::new)[instance];
                    double predictedOutcome = activationFunction.applyAsDouble(weight * p + bias);
                    double groundTruth = trainSet.get(p);
                    if (predictedOutcome != groundTruth) {
                        errorCounter++;
                        weight = adjustWeightBasedOnMLPUniversalLearningRule(p, weight, predictedOutcome, groundTruth);
                        bias = adjustBiasBasedOnMLPUniversalLearningRule(bias, predictedOutcome, groundTruth);
                    }
                }
                System.out.println("The error rate in epoch " + iter + " is : " + ((double) errorCounter / trainSet.size()));
            }
        });
    }

    private double adjustWeightBasedOnMLPUniversalLearningRule(double p, double wOld, double predictedLabel, double groundTruth) {
        return wOld + learningRate * (groundTruth - predictedLabel) * p;
    }

    private double adjustBiasBasedOnMLPUniversalLearningRule(double bOld, double predictedLabel, double groundTruth) {
        return bOld + (groundTruth - predictedLabel);
    }

    public double getValidationAccuracy(HashMap<Integer, Double> trainSet, float validationPortion, int seed) {
        int validationSetSize = (int) (validationPortion * trainSet.size());
        HashMap<Integer, Double> validationSet = new HashMap<>();
        SplittableRandom rand = new SplittableRandom(seed);
        for (int i = 0; i < validationSetSize; i++) {
            int random = rand.nextInt(trainSet.size());
            int key = trainSet.keySet().toArray(Integer[]::new)[random];
            validationSet.put(key, trainSet.get(key));
        }
        errorCounter = 0;
        for (int instance = 0; instance < validationSet.size(); instance++) {
            int p = validationSet.keySet().toArray(Integer[]::new)[instance];
            double predictedOutcome = activationFunction.applyAsDouble(weight * p + bias);
            double groundTruth = validationSet.get(p);
            if (predictedOutcome != groundTruth) {
                errorCounter++;
            }
        }
        return 1 - ((double) errorCounter / validationSet.size());
    }

    public double test(HashMap<Integer,Double> testSet) {
        errorCounter = 0;
        for (int instance = 0; instance < testSet.size(); instance++) {
            int p = testSet.keySet().toArray(Integer[]::new)[instance];
            double predictedOutcome = activationFunction.applyAsDouble(weight * p + bias);
            double groundTruth = testSet.get(p);
            if (predictedOutcome != groundTruth) {
                errorCounter++;
            }
        }
        return 1 - ((double) errorCounter / testSet.size());
    }

    public int predict(double value) {
        return (int) activationFunction.applyAsDouble(weight * value + bias);
    }

    public double getCurrentWeight() {
        return weight;
    }

    public double getCurrentBias() {
        return bias;
    }

    public ActivationFunction getCurrentActivationFunction() {
        return activationFunction;
    }

    public double getCurrentLearningRate() {
        return learningRate;
    }

    public int getNumberOfEpoch() {
        return epoch;
    }
}
