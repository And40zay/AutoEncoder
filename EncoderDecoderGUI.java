import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class EncoderDecoderGUI extends JFrame {
    
    private JTextField messageField;
    private JTextArea outputArea;
    private JTextArea decodeInputArea;
    private JTextArea decodeOutputArea;
    private JTabbedPane tabbedPane;
    
    public EncoderDecoderGUI() {
        setTitle("Neural Network Encoder/Decoder");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Encode", createEncodePanel());
        tabbedPane.addTab("Decode", createDecodePanel());
        tabbedPane.addTab("About", createAboutPanel());
        
        add(tabbedPane);
    }
    
    private JPanel createEncodePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Message to Encode"));
        
        messageField = new JTextField();
        messageField.setFont(new Font("Monospaced", Font.PLAIN, 14));
        inputPanel.add(messageField, BorderLayout.CENTER);
        
        JButton encodeButton = new JButton("Encode Message");
        encodeButton.addActionListener(e -> encodeMessage());
        inputPanel.add(encodeButton, BorderLayout.EAST);
        
        JPanel outputPanel = new JPanel(new BorderLayout(5, 5));
        outputPanel.setBorder(BorderFactory.createTitledBorder("Encoder Output"));
        
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        outputPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton copyButton = new JButton("Copy to Clipboard");
        copyButton.addActionListener(e -> copyToClipboard());
        bottomPanel.add(copyButton);
        
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(outputPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createDecodePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Paste Encoder Output Here"));
        
        decodeInputArea = new JTextArea(10, 40);
        decodeInputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane inputScroll = new JScrollPane(decodeInputArea);
        inputPanel.add(inputScroll, BorderLayout.CENTER);
        
        JButton decodeButton = new JButton("Decode Message");
        decodeButton.addActionListener(e -> decodeMessage());
        inputPanel.add(decodeButton, BorderLayout.SOUTH);
        
        JPanel outputPanel = new JPanel(new BorderLayout(5, 5));
        outputPanel.setBorder(BorderFactory.createTitledBorder("Decoded Message"));
        
        decodeOutputArea = new JTextArea();
        decodeOutputArea.setEditable(false);
        decodeOutputArea.setFont(new Font("Monospaced", Font.BOLD, 14));
        JScrollPane outputScroll = new JScrollPane(decodeOutputArea);
        outputPanel.add(outputScroll, BorderLayout.CENTER);
        
        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(outputPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createAboutPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea aboutText = new JTextArea(
            "Neural Network Encoder/Decoder\n\n" +
            "How it works:\n" +
            "1. Enter a message and click 'Encode'\n" +
            "2. The system trains a neural network to compress your message\n" +
            "3. Copy all the output values\n" +
            "4. Switch to 'Decode' tab and paste the values\n" +
            "5. Click 'Decode' to recover the original message\n\n" +
            "Technical Details:\n" +
            "• Uses a 3-layer autoencoder neural network\n" +
            "• Compresses message to half its original size\n" +
            "• Training takes ~10,000 epochs for accuracy"
        );
        aboutText.setEditable(false);
        aboutText.setFont(new Font("Arial", Font.PLAIN, 12));
        aboutText.setMargin(new Insets(20, 20, 20, 20));
        panel.add(aboutText);
        return panel;
    }
    
    private void encodeMessage() {
        String message = messageField.getText().trim();
        if (message.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a message!");
            return;
        }
        
        try {
            Random rand = new Random(42);
            double[] input = new double[message.length()];
            for (int i = 0; i < message.length(); i++) {
                input[i] = message.charAt(i) / 127.0;
            }
            
            int inputSize = message.length();
            int hiddenSize = Math.max(1, inputSize / 2);
            
            NeuralNetwork.MultiLayerPerceptron ae = 
                new NeuralNetwork.MultiLayerPerceptron(inputSize, hiddenSize, inputSize, rand);
            
            double[][] training = {input};
            ae.train(training, training, 5000, 0.3);
            
            double[] encoded = ae.hiddenLayer.forward(input);
            
            StringBuilder output = new StringBuilder();
            output.append("// Original: ").append(message).append("\n\n");
            output.append("double[] ENCODED = {");
            for (int i = 0; i < encoded.length; i++) {
                if (i > 0) output.append(", ");
                output.append(String.format("%.10f", encoded[i]));
            }
            output.append("};\n\n");
            output.append("int INPUT_SIZE = ").append(inputSize).append(";\n\n");
            output.append("double[][] OUTPUT_WEIGHTS = {\n");
            for (int i = 0; i < inputSize; i++) {
                output.append("    {");
                double[] w = ae.getOutputNeurons()[i].getWeights();
                for (int j = 0; j < w.length; j++) {
                    if (j > 0) output.append(", ");
                    output.append(String.format("%.10f", w[j]));
                }
                output.append("}");
                if (i < inputSize - 1) output.append(",\n");
                else output.append("\n");
            }
            output.append("};\n\n");
            output.append("double[] OUTPUT_BIASES = {");
            for (int i = 0; i < inputSize; i++) {
                if (i > 0) output.append(", ");
                output.append(String.format("%.10f", ae.getOutputNeurons()[i].getBias()));
            }
            output.append("};\n");
            
            outputArea.setText(output.toString());
            
        } catch (Exception e) {
            outputArea.setText("Error: " + e.getMessage());
        }
    }
    
    private void decodeMessage() {
        String text = decodeInputArea.getText();
        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please paste encoder output!");
            return;
        }
        
        try {
            double[] encoded = parseEncoded(text);
            int inputSize = parseInputSize(text);
            double[][] outputWeights = parseOutputWeights(text);
            double[] outputBiases = parseOutputBiases(text);
            
            if (encoded == null || outputWeights == null || inputSize == 0) {
                decodeOutputArea.setText("Invalid format!");
                return;
            }
            
            double[] output = new double[inputSize];
            for (int i = 0; i < inputSize; i++) {
                double sum = outputBiases[i];
                for (int j = 0; j < encoded.length; j++) {
                    sum += outputWeights[i][j] * encoded[j];
                }
                output[i] = 1.0 / (1.0 + Math.exp(-sum));
            }
            
            StringBuilder message = new StringBuilder();
            for (int i = 0; i < inputSize; i++) {
                char c = (char) Math.round(output[i] * 127);
                if (c != 0) message.append(c);
            }
            
            decodeOutputArea.setText(message.toString());
            
        } catch (Exception e) {
            decodeOutputArea.setText("Error: " + e.getMessage());
        }
    }
    
    private double[] parseEncoded(String text) {
        try {
            int start = text.indexOf("double[] ENCODED = {");
            if (start == -1) return null;
            start = text.indexOf("{", start) + 1;
            int end = text.indexOf("}", start);
            String[] parts = text.substring(start, end).trim().split(",");
            double[] result = new double[parts.length];
            for (int i = 0; i < parts.length; i++) {
                result[i] = Double.parseDouble(parts[i].trim());
            }
            return result;
        } catch (Exception e) {
            return null;
        }
    }
    
    private int parseInputSize(String text) {
        try {
            int start = text.indexOf("int INPUT_SIZE = ");
            if (start == -1) return 0;
            start = text.indexOf("=", start) + 1;
            int end = text.indexOf(";", start);
            return Integer.parseInt(text.substring(start, end).trim());
        } catch (Exception e) {
            return 0;
        }
    }
    
    private double[][] parseOutputWeights(String text) {
        try {
            int start = text.indexOf("double[][] OUTPUT_WEIGHTS = {");
            if (start == -1) return null;
            start = text.indexOf("{", start + 10);
            java.util.ArrayList<double[]> list = new java.util.ArrayList<>();
            while (start != -1 && start < text.indexOf("};", start)) {
                int end = text.indexOf("}", start);
                if (end == -1) break;
                String[] parts = text.substring(start + 1, end).trim().split(",");
                double[] weights = new double[parts.length];
                for (int i = 0; i < parts.length; i++) {
                    weights[i] = Double.parseDouble(parts[i].trim());
                }
                list.add(weights);
                start = text.indexOf("{", end);
            }
            return list.toArray(new double[0][0]);
        } catch (Exception e) {
            return null;
        }
    }
    
    private double[] parseOutputBiases(String text) {
        try {
            int start = text.indexOf("double[] OUTPUT_BIASES = {");
            if (start == -1) return null;
            start = text.indexOf("{", start) + 1;
            int end = text.indexOf("}", start);
            String[] parts = text.substring(start, end).trim().split(",");
            double[] result = new double[parts.length];
            for (int i = 0; i < parts.length; i++) {
                result[i] = Double.parseDouble(parts[i].trim());
            }
            return result;
        } catch (Exception e) {
            return null;
        }
    }
    
    private void copyToClipboard() {
        String text = outputArea.getText();
        if (text.isEmpty()) return;
        java.awt.datatransfer.StringSelection selection = 
            new java.awt.datatransfer.StringSelection(text);
        java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
        JOptionPane.showMessageDialog(this, "Copied to clipboard!");
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new EncoderDecoderGUI().setVisible(true);
        });
    }
}
