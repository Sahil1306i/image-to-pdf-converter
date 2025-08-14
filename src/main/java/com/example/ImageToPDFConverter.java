package com.example;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Desktop GUI application for converting images to PDF
 */
public class ImageToPDFConverter extends JFrame {

    private ImageToPDFService pdfService;
    private JList<String> imageList;
    private DefaultListModel<String> listModel;
    private JButton addImagesButton;
    private JButton removeImageButton;
    private JButton clearAllButton;
    private JButton convertButton;
    private JLabel statusLabel;

    public ImageToPDFConverter() {
        this.pdfService = new ImageToPDFService();
        this.listModel = new DefaultListModel<>();
        this.imageList = new JList<>(listModel);

        initializeComponents();
        setupLayout();
        setupEventHandlers();

        setTitle("Image to PDF Converter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
    }

    private void initializeComponents() {
        addImagesButton = new JButton("Add Images");
        removeImageButton = new JButton("Remove Selected");
        clearAllButton = new JButton("Clear All");
        convertButton = new JButton("Convert to PDF");
        statusLabel = new JLabel("Ready to convert images to PDF");

        imageList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        imageList.setBorder(BorderFactory.createTitledBorder("Selected Images"));

        convertButton.setFont(convertButton.getFont().deriveFont(Font.BOLD, 14f));
        convertButton.setBackground(new Color(0, 120, 215));
        convertButton.setForeground(Color.WHITE);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Top panel with buttons
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(addImagesButton);
        topPanel.add(removeImageButton);
        topPanel.add(clearAllButton);

        // Center panel with image list
        JScrollPane scrollPane = new JScrollPane(imageList);
        scrollPane.setPreferredSize(new Dimension(580, 300));

        // Bottom panel with convert button and status
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(convertButton, BorderLayout.CENTER);
        bottomPanel.add(statusLabel, BorderLayout.SOUTH);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void setupEventHandlers() {
        addImagesButton.addActionListener(new AddImagesActionListener());
        removeImageButton.addActionListener(new RemoveImagesActionListener());
        clearAllButton.addActionListener(e -> {
            listModel.clear();
            updateStatus("All images cleared");
        });
        convertButton.addActionListener(new ConvertToPDFActionListener());
    }

    private class AddImagesActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setMultiSelectionEnabled(true);
            fileChooser.setFileFilter(new FileNameExtensionFilter(
                    "Image files", "jpg", "jpeg", "png", "gif", "bmp", "tiff", "tif"));

            int result = fileChooser.showOpenDialog(ImageToPDFConverter.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File[] selectedFiles = fileChooser.getSelectedFiles();
                int addedCount = 0;

                for (File file : selectedFiles) {
                    String filePath = file.getAbsolutePath();
                    if (!listModel.contains(filePath)) {
                        listModel.addElement(filePath);
                        addedCount++;
                    }
                }

                updateStatus(addedCount + " image(s) added. Total: " + listModel.size());
            }
        }
    }

    private class RemoveImagesActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int[] selectedIndices = imageList.getSelectedIndices();
            if (selectedIndices.length > 0) {
                // Remove from highest index to lowest to avoid index shifting issues
                for (int i = selectedIndices.length - 1; i >= 0; i--) {
                    listModel.remove(selectedIndices[i]);
                }
                updateStatus(selectedIndices.length + " image(s) removed. Total: " + listModel.size());
            } else {
                JOptionPane.showMessageDialog(ImageToPDFConverter.this,
                        "Please select images to remove.",
                        "No Selection",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private class ConvertToPDFActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (listModel.isEmpty()) {
                JOptionPane.showMessageDialog(ImageToPDFConverter.this,
                        "Please add images before converting.",
                        "No Images",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File("output.pdf"));
            fileChooser.setFileFilter(new FileNameExtensionFilter("PDF files", "pdf"));

            int result = fileChooser.showSaveDialog(ImageToPDFConverter.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File outputFile = fileChooser.getSelectedFile();
                String outputPath = outputFile.getAbsolutePath();
                if (!outputPath.toLowerCase().endsWith(".pdf")) {
                    outputPath += ".pdf";
                }

                convertInBackground(outputPath);
            }
        }
    }

    private void convertInBackground(String outputPath) {
        convertButton.setEnabled(false);
        updateStatus("Converting images to PDF...");

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                List<String> imagePaths = new ArrayList<>();
                for (int i = 0; i < listModel.size(); i++) {
                    imagePaths.add(listModel.getElementAt(i));
                }

                pdfService.convertImagesToPDF(imagePaths, outputPath);
                return null;
            }

            @Override
            protected void done() {
                convertButton.setEnabled(true);
                try {
                    get(); // This will throw an exception if conversion failed
                    updateStatus("PDF created successfully: " + outputPath);
                    JOptionPane.showMessageDialog(ImageToPDFConverter.this,
                            "PDF created successfully!\n" + outputPath,
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    updateStatus("Error: " + e.getMessage());
                    JOptionPane.showMessageDialog(ImageToPDFConverter.this,
                            "Error creating PDF: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
    }

    private void updateStatus(String message) {
        statusLabel.setText(message);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            new ImageToPDFConverter().setVisible(true);
        });
    }
}
