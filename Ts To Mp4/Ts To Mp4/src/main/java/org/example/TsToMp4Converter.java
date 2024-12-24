package org.example;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TsToMp4Converter {

    public static void main(String[] args) {
        JFrame frame = new JFrame("TS to MP4 Converter");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JLabel inputLabel = new JLabel("Input .ts File:");
        inputLabel.setBounds(10, 10, 100, 25);
        frame.add(inputLabel);

        JTextField inputText = new JTextField();
        inputText.setBounds(120, 10, 200, 25);
        frame.add(inputText);

        JButton inputButton = new JButton("Browse");
        inputButton.setBounds(330, 10, 100, 25);
        frame.add(inputButton);

        JLabel outputLabel = new JLabel("Output .mp4 File:");
        outputLabel.setBounds(10, 50, 100, 25);
        frame.add(outputLabel);

        JTextField outputText = new JTextField();
        outputText.setBounds(120, 50, 200, 25);
        frame.add(outputText);

        JButton outputButton = new JButton("Browse");
        outputButton.setBounds(330, 50, 100, 25);
        frame.add(outputButton);

        JButton convertButton = new JButton("Convert");
        convertButton.setBounds(150, 100, 100, 25);
        frame.add(convertButton);

        JLabel statusLabel = new JLabel("");
        statusLabel.setBounds(10, 130, 400, 25);
        frame.add(statusLabel);

        // Input file selection
        inputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    String inputPath = fileChooser.getSelectedFile().getAbsolutePath();
                    inputText.setText(inputPath);
                }
            }
        });

        // Output file selection
        outputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int result = fileChooser.showSaveDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    String outputPath = fileChooser.getSelectedFile().getAbsolutePath();
                    // Ensure the output file ends with .mp4
                    if (!outputPath.toLowerCase().endsWith(".mp4")) {
                        outputPath += ".mp4";
                    }
                    outputText.setText(outputPath);
                }
            }
        });

        // Convert button action
        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputFilePath = inputText.getText();
                String outputFilePath = outputText.getText();

                if (inputFilePath.isEmpty() || outputFilePath.isEmpty()) {
                    statusLabel.setText("Please select both input and output files.");
                    return;
                }

                try {
                    String ffmpegCommand = String.format("ffmpeg -i \"%s\" -c copy \"%s\"", inputFilePath, outputFilePath);
                    Process process = Runtime.getRuntime().exec(ffmpegCommand);

                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                    String line;
                    StringBuilder output = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        output.append(line).append("\n");
                    }

                    int exitCode = process.waitFor();
                    if (exitCode == 0) {
                        statusLabel.setText("Conversion successful!");
                    } else {
                        statusLabel.setText("Error: " + output.toString());
                    }
                } catch (Exception ex) {
                    statusLabel.setText("Conversion failed: " + ex.getMessage());
                }
            }
        });

        frame.setVisible(true);
    }
}
