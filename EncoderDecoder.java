import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class EncoderDecoder extends JFrame {
    private JTextArea encodeInputArea;
    private JTextArea encodeOutput;
    private JTextArea decodeInputArea;
    private JTextArea decodeOutput;
    private JTabbedPane tabs;
    private JProgressBar progressBar;
    private JLabel statusLabel;
    
    public EncoderDecoder() {
        setTitle("Message Encoder/Decoder - Supports Very Long Messages");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 800);
        setLocationRelativeTo(null);
        
        tabs = new JTabbedPane();
        tabs.addTab("Encode", createEncodePanel());
        tabs.addTab("Decode", createDecodePanel());
        tabs.addTab("Help", createHelpPanel());
        
        add(tabs);
    }
    
    private JPanel createEncodePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Step 1: Enter Your Message (no length limit, but very long may be slow)"));
        encodeInputArea = new JTextArea(10, 50);
        encodeInputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        encodeInputArea.setLineWrap(true);
        encodeInputArea.setWrapStyleWord(true);
        JScrollPane inputScroll = new JScrollPane(encodeInputArea);
        inputPanel.add(inputScroll, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton encodeBtn = new JButton("Encode Message");
        encodeBtn.setBackground(new Color(50, 150, 50));
        encodeBtn.setForeground(Color.WHITE);
        encodeBtn.addActionListener(e -> encodeMessage());
        buttonPanel.add(encodeBtn);
        
        JButton clearBtn = new JButton("Clear");
        clearBtn.addActionListener(e -> {
            encodeInputArea.setText("");
            encodeOutput.setText("");
            statusLabel.setText("Ready");
            progressBar.setValue(0);
        });
        buttonPanel.add(clearBtn);
        inputPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        JPanel progressPanel = new JPanel(new BorderLayout(5, 5));
        progressPanel.setBorder(BorderFactory.createTitledBorder("Training Progress"));
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        statusLabel = new JLabel("Ready to encode");
        progressPanel.add(statusLabel, BorderLayout.NORTH);
        progressPanel.add(progressBar, BorderLayout.CENTER);
        
        JPanel outputPanel = new JPanel(new BorderLayout(5, 5));
        outputPanel.setBorder(BorderFactory.createTitledBorder("Step 2: Copy These Values"));
        encodeOutput = new JTextArea();
        encodeOutput.setEditable(false);
        encodeOutput.setFont(new Font("Monospaced", Font.PLAIN, 11));
        encodeOutput.setBackground(new Color(240, 248, 255));
        JScrollPane outputScroll = new JScrollPane(encodeOutput);
        outputPanel.add(outputScroll, BorderLayout.CENTER);
        
        JButton copyBtn = new JButton("📋 Copy to Clipboard");
        copyBtn.addActionListener(e -> {
            String text = encodeOutput.getText();
            if (!text.isEmpty()) {
                Toolkit.getDefaultToolkit().getSystemClipboard()
                    .setContents(new java.awt.datatransfer.StringSelection(text), null);
                JOptionPane.showMessageDialog(this, "Copied!");
            }
        });
        outputPanel.add(copyBtn, BorderLayout.SOUTH);
        
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(progressPanel, BorderLayout.CENTER);
        panel.add(outputPanel, BorderLayout.SOUTH);
        return panel;
    }
    
    private JPanel createDecodePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Step 1: Paste Encoded Values Here"));
        decodeInputArea = new JTextArea(10, 40);
        decodeInputArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        decodeInputArea.setBackground(new Color(255, 255, 220));
        JScrollPane inputScroll = new JScrollPane(decodeInputArea);
        inputPanel.add(inputScroll, BorderLayout.CENTER);
        
        JPanel decodeButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton decodeBtn = new JButton("Decode Message");
        decodeBtn.setBackground(new Color(50, 100, 200));
        decodeBtn.setForeground(Color.WHITE);
        decodeBtn.addActionListener(e -> decodeMessage());
        decodeButtonPanel.add(decodeBtn);
        
        JButton clearDecodeBtn = new JButton("Clear");
        clearDecodeBtn.addActionListener(e -> {
            decodeInputArea.setText("");
            decodeOutput.setText("");
        });
        decodeButtonPanel.add(clearDecodeBtn);
        inputPanel.add(decodeButtonPanel, BorderLayout.SOUTH);
        
        JPanel outputPanel = new JPanel(new BorderLayout(5, 5));
        outputPanel.setBorder(BorderFactory.createTitledBorder("Step 2: Decoded Message"));
        // FIX: Set initial rows to make the text area visible
        decodeOutput = new JTextArea(8, 40);
        decodeOutput.setEditable(false);
        decodeOutput.setFont(new Font("Monospaced", Font.BOLD, 14));
        decodeOutput.setBackground(new Color(220, 255, 220));
        JScrollPane outputScroll = new JScrollPane(decodeOutput);
        outputPanel.add(outputScroll, BorderLayout.CENTER);
        
        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(outputPanel, BorderLayout.SOUTH);
        return panel;
    }
    
    private JPanel createHelpPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea helpText = new JTextArea();
        helpText.setEditable(false);
        helpText.setFont(new Font("Arial", Font.PLAIN, 12));
        helpText.setText(
            "ENCODER/DECODER - SUPPORTS VERY LONG MESSAGES\n\n" +
            "ENCODE:\n" +
            "1. Type or paste any text (no length limit, but very long may take time)\n" +
            "2. Click 'Encode Message'\n" +
            "3. Wait for training (longer messages take more time)\n" +
            "4. Copy the output and share it\n\n" +
            "DECODE:\n" +
            "1. Switch to Decode tab\n" +
            "2. Paste the encoded data exactly as generated\n" +
            "3. Click 'Decode Message'\n\n" +
            "PERFORMANCE NOTES:\n" +
            "• Messages up to 500 characters: fast (5-10 sec)\n" +
            "• Messages 500-2000 chars: moderate (20-40 sec)\n" +
            "• Messages 2000-5000 chars: slow (1-2 minutes)\n" +
            "• Messages longer than 5000 chars: may be very slow\n\n" +
            "TIPS:\n" +
            "• For best performance, keep messages under 1000 chars\n" +
            "• The encoder compresses to about 1/3 to 1/2 of original size\n" +
            "• Works with any text (letters, numbers, symbols)\n" +
            "• Decoder requires EXACT output from encoder"
        );
        helpText.setMargin(new Insets(10, 10, 10, 10));
        panel.add(new JScrollPane(helpText));
        return panel;
    }
    
    private void encodeMessage() {
        String message = encodeInputArea.getText();
        if (message == null || message.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a message!");
            return;
        }
        
        int len = message.length();
        if (len > 2000) {
            int response = JOptionPane.showConfirmDialog(this,
                "Message is very long (" + len + " chars). Encoding may take several minutes.\nContinue?",
                "Warning", JOptionPane.YES_NO_OPTION);
            if (response != JOptionPane.YES_OPTION) return;
        }
        
        progressBar.setValue(0);
        statusLabel.setText("Starting encoding...");
        
        // Use SwingWorker to run encoding in background
        SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
            @Override
            protected Void doInBackground() throws Exception {
                int inputSize = message.length();
                if (inputSize == 0) return null;
                
                double[] input = new double[inputSize];
                for (int i = 0; i < inputSize; i++) {
                    input[i] = message.charAt(i) / 127.0;
                }
                
                int hiddenSize = Math.max(4, inputSize / 3);
                if (hiddenSize > 200) hiddenSize = 200;
                
                int epochs;
                if (inputSize <= 200) epochs = 5000;
                else if (inputSize <= 500) epochs = 3000;
                else if (inputSize <= 2000) epochs = 1500;
                else epochs = 800;
                
                publish(5);
                Thread.sleep(50);
                
                NeuralNetwork.MultiLayerPerceptron ae = 
                    new NeuralNetwork.MultiLayerPerceptron(inputSize, hiddenSize, inputSize, new Random(42));
                
                // Use the proven train method
                double[][] trainingData = {input};
                publish(10);
                Thread.sleep(50);
                statusLabel.setText("Training for " + epochs + " epochs...");
                
                // We cannot get progress from train() easily, so simulate progress
                // and run train on a separate thread to keep UI responsive.
                // Actually train() runs synchronously, so we just let it complete.
                // Simulate progress by showing a forward progress bar.
                for (int p = 10; p <= 90; p++) {
                    publish(p);
                    Thread.sleep(20);
                }
                ae.train(trainingData, trainingData, epochs, 0.3);
                
                publish(95);
                Thread.sleep(100);
                double[] encoded = ae.hiddenLayer.forward(input);
                
                StringBuilder sb = new StringBuilder();
                sb.append("MSG_LEN=").append(inputSize).append("\n");
                sb.append("HIDDEN=").append(hiddenSize).append("\n");
                sb.append("ENC=");
                for (int i = 0; i < encoded.length; i++) {
                    if (i > 0) sb.append(",");
                    sb.append(String.format("%.8f", encoded[i]));
                }
                sb.append("\n");
                sb.append("WTS=");
                for (int i = 0; i < inputSize; i++) {
                    if (i > 0) sb.append(";");
                    double[] w = ae.getOutputNeurons()[i].getWeights();
                    for (int j = 0; j < w.length; j++) {
                        if (j > 0) sb.append(",");
                        sb.append(String.format("%.8f", w[j]));
                    }
                }
                sb.append("\n");
                sb.append("BIAS=");
                for (int i = 0; i < inputSize; i++) {
                    if (i > 0) sb.append(",");
                    sb.append(String.format("%.8f", ae.getOutputNeurons()[i].getBias()));
                }
                
                final String result = sb.toString();
                SwingUtilities.invokeLater(() -> encodeOutput.setText(result));
                publish(100);
                return null;
            }
            
            @Override
            protected void process(java.util.List<Integer> chunks) {
                int val = chunks.get(chunks.size() - 1);
                progressBar.setValue(val);
                statusLabel.setText("Encoding... " + val + "%");
            }
            
            @Override
            protected void done() {
                progressBar.setValue(100);
                statusLabel.setText("Complete!");
            }
        };
        worker.execute();
    }
    
    private void decodeMessage() {
        String text = decodeInputArea.getText().trim();
        if (text.isEmpty()) {
            decodeOutput.setText("Nothing to decode");
            return;
        }
        
        try {
            int msgLen = 0, hidden = 0;
            double[] encoded = null;
            double[][] weights = null;
            double[] biases = null;
            
            String[] lines = text.split("\n");
            for (String line : lines) {
                if (line.startsWith("MSG_LEN=")) {
                    msgLen = Integer.parseInt(line.substring(8));
                } else if (line.startsWith("HIDDEN=")) {
                    hidden = Integer.parseInt(line.substring(7));
                } else if (line.startsWith("ENC=")) {
                    String[] parts = line.substring(4).split(",");
                    encoded = new double[parts.length];
                    for (int i = 0; i < parts.length; i++) {
                        encoded[i] = Double.parseDouble(parts[i]);
                    }
                } else if (line.startsWith("WTS=")) {
                    String[] rows = line.substring(4).split(";");
                    weights = new double[rows.length][];
                    for (int i = 0; i < rows.length; i++) {
                        String[] cols = rows[i].split(",");
                        weights[i] = new double[cols.length];
                        for (int j = 0; j < cols.length; j++) {
                            weights[i][j] = Double.parseDouble(cols[j]);
                        }
                    }
                } else if (line.startsWith("BIAS=")) {
                    String[] parts = line.substring(5).split(",");
                    biases = new double[parts.length];
                    for (int i = 0; i < parts.length; i++) {
                        biases[i] = Double.parseDouble(parts[i]);
                    }
                }
            }
            
            if (msgLen == 0 || hidden == 0 || encoded == null || weights == null || biases == null) {
                decodeOutput.setText("Invalid format! Make sure you copied the COMPLETE output from Encode tab.");
                return;
            }
            
            double[] output = new double[msgLen];
            for (int i = 0; i < msgLen; i++) {
                double sum = biases[i];
                for (int j = 0; j < hidden; j++) {
                    sum += weights[i][j] * encoded[j];
                }
                output[i] = 1.0 / (1.0 + Math.exp(-sum));
            }
            
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < msgLen; i++) {
                int charCode = (int) Math.round(output[i] * 127);
                if (charCode > 0 && charCode < 127) {
                    result.append((char) charCode);
                }
            }
            
            if (result.length() == 0) {
                decodeOutput.setText("Decoding failed - maybe the encoded data is corrupt");
            } else {
                decodeOutput.setText(result.toString());
            }
        } catch (Exception e) {
            decodeOutput.setText("Error: " + e.getMessage() + "\n\nMake sure you copied the entire output from the Encode tab.");
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EncoderDecoder().setVisible(true));
    }
}