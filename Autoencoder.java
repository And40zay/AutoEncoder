import java.util.Random;

public class Autoencoder{
    NeuralNetwork.MultiLayerPerceptron autoEncoder;
    int inputSize;
    int latentSize;

    public Autoencoder(int inputSize, int latentSize, Random rand){
        this.inputSize = inputSize;
        this.latentSize = latentSize;
        this.autoEncoder = new NeuralNetwork.MultiLayerPerceptron(inputSize, latentSize, inputSize, rand);
    }
    public int getLatentSize() {
        return latentSize;
    }
    public double[] encode(double[] input){

        return autoEncoder.getHiddenOutput(input);
    }
    public double[] decode(double[] latent){
        return autoEncoder.getOutputFromHidden(latent);
    }
    public double[] forward(double[] input){
        return autoEncoder.forward(input);
    }
    public void train(double[][] data, int epochs, double learningRate)
    {
        double[][] targets = data; // For autoencoder, targets are the same as inputs
        autoEncoder.train(data, targets, epochs, learningRate);
    } 
    public double[][] getLatentRepresentations(double[][] data)
    {
        double[][] latentVectors  = new double[data.length][latentSize];
        for (int i = 0; i < data.length; i++) {
            latentVectors[i] = encode(data[i]);
        }
        return latentVectors;
    }
    public double getReconstructionError(double[] sample)
    {
        double[] reconstructed = forward(sample);
        double error = 0.0;
        for (int i = 0; i < sample.length; i++) 
        {
            error += Math.pow(sample[i] - reconstructed[i], 2);
        }
        return error / sample.length;
    }
    public int getInputSize() {
        return inputSize;
    }
    

}
