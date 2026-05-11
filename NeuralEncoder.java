import java.util.Random;
import java.util.Scanner;

public class NeuralEncoder {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random rand = new Random(42);
        
        System.out.print("Enter message to encode: ");
        String message = scanner.nextLine();
        
        if (message.isEmpty()) {
            System.out.println("Message cannot be empty!");
            return;
        }
        
        double[] input = new double[message.length()];
        for (int i = 0; i < message.length(); i++) {
            input[i] = message.charAt(i) / 127.0;
        }
        
        int inputSize = message.length();
        int hiddenSize = Math.max(1, inputSize / 2);
        
        System.out.println("\nTraining neural network on: " + message);
        
        NeuralNetwork.MultiLayerPerceptron ae = 
            new NeuralNetwork.MultiLayerPerceptron(inputSize, hiddenSize, inputSize, rand);
        
        double[][] training = {input};
        ae.train(training, training, 5000, 0.3);
        
        double[] encoded = ae.hiddenLayer.forward(input);
        
        System.out.println("\n========== ENCODED NUMBERS ==========");
        for (int i = 0; i < encoded.length; i++) {
            System.out.printf("%.10f", encoded[i]);
            if (i < encoded.length - 1) System.out.print(", ");
        }
        System.out.println("\n=====================================\n");
        
        double[] decoded = ae.forward(input);
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < inputSize; i++) {
            char c = (char) Math.round(decoded[i] * 127);
            if (c != 0) result.append(c);
        }
        
        System.out.println("Verified decode: " + result.toString());
        
        scanner.close();
    }
}
