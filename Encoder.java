import java.util.Random;

public class Encoder {
    public static void main(String[] args) {
        // 🔧 CHANGE THIS TO YOUR MESSAGE
        String message = "Get Lost";
        
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                    ENCODER OUTPUT                          ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println("\nOriginal message: " + message);
        
        // Convert message to numbers
        double[] input = new double[message.length()];
        for (int i = 0; i < message.length(); i++) {
            input[i] = message.charAt(i) / 127.0;
        }
        
        // Create and train autoencoder
        int inputSize = message.length();
        int hiddenSize = Math.max(1, inputSize / 2);
        Random rand = new Random(42);
        
        NeuralNetwork.MultiLayerPerceptron ae = 
            new NeuralNetwork.MultiLayerPerceptron(inputSize, hiddenSize, inputSize, rand);
        
        double[][] training = {input};
        ae.train(training, training, 10000, 0.3);
        
        // Get encoded message
        double[] encoded = ae.hiddenLayer.forward(input);
        
        System.out.println("\n┌────────────────────────────────────────────────────────────┐");
        System.out.println("│  COPY EVERYTHING BELOW AND PASTE INTO Decoder.java       │");
        System.out.println("└────────────────────────────────────────────────────────────┘\n");
        
        // Output everything the decoder needs
        System.out.println("// ═══════════════════════════════════════════════════════════");
        System.out.println("// PASTE THESE VALUES INTO Decoder.java");
        System.out.println("// ═══════════════════════════════════════════════════════════\n");
        
        System.out.println("double[] ENCODED = {");
        System.out.print("    ");
        for (int i = 0; i < encoded.length; i++) {
            System.out.printf("%.10f", encoded[i]);
            if (i < encoded.length - 1) System.out.print(", ");
        }
        System.out.println("\n};");
        
        System.out.println("\nint INPUT_SIZE = " + inputSize + ";");
        System.out.println("int HIDDEN_SIZE = " + hiddenSize + ";\n");
        
        System.out.println("double[][] OUTPUT_WEIGHTS = {");
        for (int i = 0; i < inputSize; i++) {
            System.out.print("    {");
            double[] weights = ae.getOutputNeurons()[i].getWeights();
            for (int j = 0; j < weights.length; j++) {
                System.out.printf("%.10f", weights[j]);
                if (j < weights.length - 1) System.out.print(", ");
            }
            System.out.print("}");
            if (i < inputSize - 1) System.out.println(",");
            else System.out.println();
        }
        System.out.println("};\n");
        
        System.out.println("double[] OUTPUT_BIASES = {");
        System.out.print("    ");
        for (int i = 0; i < inputSize; i++) {
            System.out.printf("%.10f", ae.getOutputNeurons()[i].getBias());
            if (i < inputSize - 1) System.out.print(", ");
        }
        System.out.println("\n};");
        
        System.out.println("\n// ═══════════════════════════════════════════════════════════");
        System.out.println("// END OF VALUES TO COPY");
        System.out.println("// ═══════════════════════════════════════════════════════════\n");
        
        System.out.println("Encoded numbers (human readable):");
        for (double d : encoded) {
            System.out.printf("%.4f ", d);
        }
        System.out.println();
    }
}