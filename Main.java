import java.util.Random;

public class Main {
    
    static final int MAX_LENGTH = 10; // Fixed maximum length
    
    static double[] stringToDoubleArray(String str) {
        double[] result = new double[MAX_LENGTH];
        for (int i = 0; i < MAX_LENGTH; i++) {
            if (i < str.length()) {
                result[i] = str.charAt(i) / 127.0;
            } else {
                result[i] = 0; // Padding for shorter strings
            }
        }
        return result;
    }
    
    static String doubleArrayToString(double[] arr) {
        StringBuilder sb = new StringBuilder();
        for (double d : arr) {
            char c = (char) Math.round(d * 127);
            if (c != 0) { // Stop at padding (null character)
                sb.append(c);
            }
        }
        return sb.toString();
    }
    
    public static void main(String[] args) {
        Random rand = new Random();
        
        // You can change this to any message up to MAX_LENGTH characters
        String message = "Launch";
        System.out.println("Original Message: " + message);
        
        double[] originalVector = stringToDoubleArray(message);
        
        // Print the vector
        System.out.print("Vector (padded to " + MAX_LENGTH + "): ");
        for (double d : originalVector) {
            System.out.print(String.format("%.3f ", d));
        }
        System.out.println("\n");
        
        // Create ONE autoencoder that works for all messages up to MAX_LENGTH
        int compressedSize = MAX_LENGTH / 2; // Compress 10 chars to 5 numbers
        
        System.out.println("Creating autoencoder with fixed input size: " + MAX_LENGTH);
        System.out.println("Compressed size: " + compressedSize);
        
        NeuralNetwork.MultiLayerPerceptron autoencoder = 
            new NeuralNetwork.MultiLayerPerceptron(MAX_LENGTH, compressedSize, MAX_LENGTH, rand);
        
        // Train on multiple messages for better generalization
        String[] trainingMessages = {"Hello", "World", "Neural", "Network", "AI", "Java"};
        double[][] trainingData = new double[trainingMessages.length][MAX_LENGTH];
        
        for (int i = 0; i < trainingMessages.length; i++) {
            trainingData[i] = stringToDoubleArray(trainingMessages[i]);
        }
        
        System.out.println("Training on " + trainingMessages.length + " messages...");
        autoencoder.train(trainingData, trainingData, 10000, 0.2);
        
        // Test with your message
        double[] encoded = autoencoder.hiddenLayer.forward(originalVector);
        System.out.print("\nEncoded (compressed to " + compressedSize + " numbers): ");
        for (double d : encoded) {
            System.out.print(String.format("%.3f ", d));
        }
        System.out.println();
        
        double[] decodedVector = autoencoder.forward(originalVector);
        String decodedMessage = doubleArrayToString(decodedVector);
        
        System.out.print("Decoded vector (padded): ");
        for (double d : decodedVector) {
            System.out.print(String.format("%.3f ", d));
        }
        System.out.println();
        System.out.println("Decoded Message: " + decodedMessage);
        System.out.println("Match: " + (message.equals(decodedMessage) ? "YES" : "NO"));
        
        // Test with another message without recreating the autoencoder
        System.out.println("\n--- Testing another message ---");
        String anotherMessage = "Python";
        double[] anotherVector = stringToDoubleArray(anotherMessage);
        double[] anotherDecoded = autoencoder.forward(anotherVector);
        String anotherResult = doubleArrayToString(anotherDecoded);
        
        System.out.println("Original: " + anotherMessage);
        System.out.println("Decoded: " + anotherResult);
        System.out.println("Match: " + (anotherMessage.equals(anotherResult) ? "YES" : "NO"));
    }
}