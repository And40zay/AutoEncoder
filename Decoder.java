public class Decoder {
    
    static double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }
    
    public static void main(String[] args) {
        
        // ═══════════════════════════════════════════════════════════
        // 🔽 PASTE THE VALUES FROM ENCODER HERE 🔽
        // ═══════════════════════════════════════════════════════════
        
       
double[] ENCODED = {
    0.5703827683, 0.9475675147, 0.4129202499, 0.0447479766
};

int INPUT_SIZE = 8;
int HIDDEN_SIZE = 4;

double[][] OUTPUT_WEIGHTS = {
    {0.6331817037, 0.2991974432, 0.7422887320, 0.9505862300},
    {-0.5730144597, 0.7895834438, 0.2941038565, -0.9362055692},
    {1.2952049559, 0.5513028773, 1.2074915953, 0.0388258644},
    {0.8353196577, -1.3022005787, -0.1135327211, 0.9756357240},
    {0.0758952317, 0.1420566901, 0.0521980419, 1.0115430906},
    {1.2022949769, 0.3837863913, 1.1410801997, 0.0285826611},
    {0.5916412721, 1.0879828699, -0.5885783072, -0.9173203600},
    {0.2154703788, 1.3996104994, 1.1526059536, 0.0265472885}
};

double[] OUTPUT_BIASES = {
    -0.7563803835, 0.8561297981, 0.5942005225, -0.3274477265, 0.1541919353, 0.4150554874, 1.1757103996, 0.4294466088
};

        
        // ═══════════════════════════════════════════════════════════
        // 🔼 DO NOT EDIT BELOW THIS LINE 🔼
        // ═══════════════════════════════════════════════════════════
        
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                    DECODER OUTPUT                          ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        
        System.out.print("\nEncoded numbers received: ");
        for (double d : ENCODED) {
            System.out.printf("%.4f ", d);
        }
        System.out.println("\n");
        
        // Decode: go from encoded -> output layer
        double[] output = new double[INPUT_SIZE];
        for (int i = 0; i < INPUT_SIZE; i++) {
            double sum = OUTPUT_BIASES[i];
            for (int j = 0; j < HIDDEN_SIZE; j++) {
                sum += OUTPUT_WEIGHTS[i][j] * ENCODED[j];
            }
            output[i] = sigmoid(sum);
        }
        
        // Convert back to string
        StringBuilder decodedMessage = new StringBuilder();
        for (int i = 0; i < INPUT_SIZE; i++) {
            char c = (char) Math.round(output[i] * 127);
            if (c != 0 && c != '\0') {
                decodedMessage.append(c);
            }
        }
        
        System.out.println("✅ Decoded message: " + decodedMessage.toString());
        System.out.println();
    }
}