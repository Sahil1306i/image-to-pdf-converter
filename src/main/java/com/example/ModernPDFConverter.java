package com.example;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Modern Desktop GUI application for PDF operations
 * Inspired by Stirling PDF and iLovePDF interfaces
 */
public class ModernPDFConverter extends JFrame {

    private ImageToPDFService pdfService;
    private JPanel mainPanel;
    private JPanel headerPanel;
    private JPanel contentPanel;
    private JPanel footerPanel;
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    
    // Colors for modern theme
    private static final Color PRIMARY_COLOR = new Color(52, 152, 219);
    private static final Color SECONDARY_COLOR = new Color(46, 204, 113);
    private static final Color ACCENT_COLOR = new Color(155, 89, 182);
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(52, 73, 94);
    private static final Color LIGHT_TEXT_COLOR = new Color(127, 140, 141);

    public ModernPDFConverter() {
        this.pdfService = new ImageToPDFService();
        
        initializeComponents();
        setupLayout();
        
        setTitle("PDF Converter Pro - Modern PDF Tools");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1000, 700));
    }

    private void initializeComponents() {
        // Set modern look and feel
        try {
            FlatLightLaf.setup();
            UIManager.put("Button.arc", 8);
            UIManager.put("Component.arc", 8);
            UIManager.put("ProgressBar.arc", 8);
            UIManager.put("TextComponent.arc", 8);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initialize panels
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        setupHeader();
        setupContent();
        setupFooter();
    }

    private void setupHeader() {
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        
        // Title section
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        
        titleLabel = new JLabel("PDF Converter Pro");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(TEXT_COLOR);
        
        subtitleLabel = new JLabel("Professional PDF tools for all your document needs");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(LIGHT_TEXT_COLOR);
        
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.CENTER);
        
        // Search/Filter panel (for future use)
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);
        
        JTextField searchField = createModernTextField("Search tools...", 250);
        searchPanel.add(searchField);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(searchPanel, BorderLayout.EAST);
        
        // Add subtle shadow
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0, 0, 0, 10)),
            new EmptyBorder(30, 40, 30, 40)
        ));
    }

    private void setupContent() {
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        
        // Create categories panel
        JPanel categoriesPanel = createCategoriesPanel();
        
        // Create tools grid
        JPanel toolsGrid = createToolsGrid();
        
        contentPanel.add(categoriesPanel, BorderLayout.NORTH);
        contentPanel.add(toolsGrid, BorderLayout.CENTER);
    }

    private JPanel createCategoriesPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 30, 0));
        
        String[] categories = {"All Tools", "Convert", "Merge & Split", "Organize", "Security", "Optimize"};
        
        for (int i = 0; i < categories.length; i++) {
            JButton categoryBtn = createCategoryButton(categories[i], i == 0);
            panel.add(categoryBtn);
        }
        
        return panel;
    }

    private JButton createCategoryButton(String text, boolean selected) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBorder(new EmptyBorder(12, 20, 12, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        if (selected) {
            button.setBackground(PRIMARY_COLOR);
            button.setForeground(Color.WHITE);
        } else {
            button.setBackground(Color.WHITE);
            button.setForeground(TEXT_COLOR);
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(new Color(248, 249, 250));
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    button.setBackground(Color.WHITE);
                }
            });
        }
        
        return button;
    }

    private JPanel createToolsGrid() {
        JPanel gridPanel = new JPanel(new GridLayout(0, 4, 25, 25));
        gridPanel.setOpaque(false);
        
        // Image to PDF tool (main function)
        gridPanel.add(createToolCard(
            "Images to PDF", 
            "Convert JPG, PNG, and other images to PDF format",
            PRIMARY_COLOR,
            "📄",
            this::showImageToPDFDialog
        ));
        
        // Future tools (placeholders)
        gridPanel.add(createToolCard(
            "PDF to Images", 
            "Extract images from PDF documents",
            SECONDARY_COLOR,
            "🖼️",
            this::showComingSoon
        ));
        
        gridPanel.add(createToolCard(
            "Merge PDFs", 
            "Combine multiple PDF files into one",
            ACCENT_COLOR,
            "🔗",
            this::showComingSoon
        ));
        
        gridPanel.add(createToolCard(
            "Split PDF", 
            "Split PDF into multiple documents",
            new Color(231, 76, 60),
            "✂️",
            this::showComingSoon
        ));
        
        gridPanel.add(createToolCard(
            "Compress PDF", 
            "Reduce PDF file size without quality loss",
            new Color(243, 156, 18),
            "🗜️",
            this::showComingSoon
        ));
        
        gridPanel.add(createToolCard(
            "Protect PDF", 
            "Add password protection to PDF files",
            new Color(52, 73, 94),
            "🔒",
            this::showComingSoon
        ));
        
        gridPanel.add(createToolCard(
            "OCR PDF", 
            "Extract text from scanned PDF documents",
            new Color(26, 188, 156),
            "👁️",
            this::showComingSoon
        ));
        
        gridPanel.add(createToolCard(
            "Rotate PDF", 
            "Rotate PDF pages to correct orientation",
            new Color(142, 68, 173),
            "🔄",
            this::showComingSoon
        ));
        
        return gridPanel;
    }

    private JPanel createToolCard(String title, String description, Color accentColor, String emoji, Runnable action) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 0, 0, 10), 1),
            new EmptyBorder(25, 25, 25, 25)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Icon and title panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        
        JLabel iconLabel = new JLabel(emoji);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(new EmptyBorder(15, 0, 10, 0));
        
        topPanel.add(iconLabel, BorderLayout.NORTH);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Description
        JTextArea descArea = new JTextArea(description);
        descArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descArea.setForeground(LIGHT_TEXT_COLOR);
        descArea.setOpaque(false);
        descArea.setEditable(false);
        descArea.setWrapStyleWord(true);
        descArea.setLineWrap(true);
        descArea.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        // Action button
        JButton actionButton = new JButton("Use Tool");
        actionButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        actionButton.setBackground(accentColor);
        actionButton.setForeground(Color.WHITE);
        actionButton.setBorder(new EmptyBorder(12, 20, 12, 20));
        actionButton.setFocusPainted(false);
        actionButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        actionButton.addActionListener(e -> action.run());
        
        card.add(topPanel, BorderLayout.NORTH);
        card.add(descArea, BorderLayout.CENTER);
        card.add(actionButton, BorderLayout.SOUTH);
        
        // Hover effects
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(accentColor, 2),
                    new EmptyBorder(24, 24, 24, 24)
                ));
                card.setBackground(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 5));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 0, 0, 10), 1),
                    new EmptyBorder(25, 25, 25, 25)
                ));
                card.setBackground(CARD_COLOR);
            }
        });
        
        return card;
    }

    private void setupFooter() {
        footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(0, 0, 0, 10)),
            new EmptyBorder(20, 40, 20, 40)
        ));
        
        JLabel footerLabel = new JLabel("© 2024 PDF Converter Pro - All rights reserved");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footerLabel.setForeground(LIGHT_TEXT_COLOR);
        
        JPanel linksPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        linksPanel.setOpaque(false);
        
        JLabel aboutLabel = createFooterLink("About");
        JLabel helpLabel = createFooterLink("Help");
        JLabel privacyLabel = createFooterLink("Privacy Policy");
        
        linksPanel.add(aboutLabel);
        linksPanel.add(helpLabel);
        linksPanel.add(privacyLabel);
        
        footerPanel.add(footerLabel, BorderLayout.WEST);
        footerPanel.add(linksPanel, BorderLayout.EAST);
    }

    private JLabel createFooterLink(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setForeground(PRIMARY_COLOR);
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                label.setText("<html><u>" + text + "</u></html>");
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                label.setText(text);
            }
        });
        
        return label;
    }

    private JTextField createModernTextField(String placeholder, int width) {
        JTextField field = new JTextField(placeholder);
        field.setPreferredSize(new Dimension(width, 40));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 0, 0, 20), 1),
            new EmptyBorder(8, 15, 8, 15)
        ));
        field.setForeground(LIGHT_TEXT_COLOR);
        
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(TEXT_COLOR);
                }
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(LIGHT_TEXT_COLOR);
                }
            }
        });
        
        return field;
    }

    private void setupLayout() {
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }

    private void showImageToPDFDialog() {
        ImageToPDFDialog dialog = new ImageToPDFDialog(this, pdfService);
        dialog.setVisible(true);
    }

    private void showComingSoon() {
        JOptionPane.showMessageDialog(
            this,
            "This feature is coming soon!\nStay tuned for updates.",
            "Coming Soon",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ModernPDFConverter().setVisible(true);
        });
    }
}

/**
 * Modern dialog for Image to PDF conversion
 */
class ImageToPDFDialog extends JDialog {
    
    private ImageToPDFService pdfService;
    private DefaultListModel<String> listModel;
    private JList<String> imageList;
    private JLabel statusLabel;
    private JProgressBar progressBar;
    private JButton convertButton;
    
    private static final Color PRIMARY_COLOR = new Color(52, 152, 219);
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(52, 73, 94);
    private static final Color LIGHT_TEXT_COLOR = new Color(127, 140, 141);

    public ImageToPDFDialog(Frame parent, ImageToPDFService pdfService) {
        super(parent, "Convert Images to PDF", true);
        this.pdfService = pdfService;
        this.listModel = new DefaultListModel<>();
        
        initializeComponents();
        setupLayout();
        
        setSize(800, 600);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void initializeComponents() {
        imageList = new JList<>(listModel);
        imageList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        imageList.setCellRenderer(new ModernListCellRenderer());
        imageList.setBackground(CARD_COLOR);
        imageList.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        statusLabel = new JLabel("Ready to convert images to PDF");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusLabel.setForeground(TEXT_COLOR);
        
        progressBar = new JProgressBar();
        progressBar.setVisible(false);
        progressBar.setStringPainted(true);
        
        convertButton = new JButton("Convert to PDF");
        convertButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        convertButton.setBackground(PRIMARY_COLOR);
        convertButton.setForeground(Color.WHITE);
        convertButton.setBorder(new EmptyBorder(12, 30, 12, 30));
        convertButton.setFocusPainted(false);
        convertButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(CARD_COLOR);
        headerPanel.setBorder(new EmptyBorder(25, 30, 25, 30));
        
        JLabel titleLabel = new JLabel("Convert Images to PDF");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_COLOR);
        
        JLabel subtitleLabel = new JLabel("Select images and convert them to a single PDF document");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(LIGHT_TEXT_COLOR);
        
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        // Content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        contentPanel.setOpaque(false);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        buttonsPanel.setOpaque(false);
        buttonsPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        JButton addButton = createModernButton("📁 Add Images", PRIMARY_COLOR);
        JButton removeButton = createModernButton("🗑️ Remove Selected", new Color(231, 76, 60));
        JButton clearButton = createModernButton("🧹 Clear All", new Color(149, 165, 166));
        
        addButton.addActionListener(this::addImages);
        removeButton.addActionListener(this::removeImages);
        clearButton.addActionListener(e -> {
            listModel.clear();
            updateStatus("All images cleared");
        });
        
        buttonsPanel.add(addButton);
        buttonsPanel.add(removeButton);
        buttonsPanel.add(clearButton);
        
        // List panel
        JScrollPane scrollPane = new JScrollPane(imageList);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 20), 1));
        scrollPane.setBackground(CARD_COLOR);
        
        contentPanel.add(buttonsPanel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Footer
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(CARD_COLOR);
        footerPanel.setBorder(new EmptyBorder(20, 30, 25, 30));
        
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setOpaque(false);
        statusPanel.add(statusLabel, BorderLayout.WEST);
        statusPanel.add(progressBar, BorderLayout.CENTER);
        
        convertButton.addActionListener(this::convertToPDF);
        
        footerPanel.add(statusPanel, BorderLayout.NORTH);
        footerPanel.add(convertButton, BorderLayout.SOUTH);
        
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private JButton createModernButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void addImages(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileFilter(new FileNameExtensionFilter(
                "Image files", "jpg", "jpeg", "png", "gif", "bmp", "tiff", "tif"));

        int result = fileChooser.showOpenDialog(this);
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

    private void removeImages(ActionEvent e) {
        int[] selectedIndices = imageList.getSelectedIndices();
        if (selectedIndices.length > 0) {
            for (int i = selectedIndices.length - 1; i >= 0; i--) {
                listModel.remove(selectedIndices[i]);
            }
            updateStatus(selectedIndices.length + " image(s) removed. Total: " + listModel.size());
        } else {
            JOptionPane.showMessageDialog(this,
                    "Please select images to remove.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void convertToPDF(ActionEvent e) {
        if (listModel.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please add images before converting.",
                    "No Images",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("converted_images.pdf"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF files", "pdf"));

        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File outputFile = fileChooser.getSelectedFile();
            String outputPath = outputFile.getAbsolutePath();
            if (!outputPath.toLowerCase().endsWith(".pdf")) {
                outputPath += ".pdf";
            }

            convertInBackground(outputPath);
        }
    }

    private void convertInBackground(String outputPath) {
        convertButton.setEnabled(false);
        progressBar.setVisible(true);
        updateStatus("Converting images to PDF...");

        SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
            @Override
            protected Void doInBackground() throws Exception {
                List<String> imagePaths = new ArrayList<>();
                for (int i = 0; i < listModel.size(); i++) {
                    imagePaths.add(listModel.getElementAt(i));
                    publish((i + 1) * 100 / listModel.size());
                }

                pdfService.convertImagesToPDF(imagePaths, outputPath);
                return null;
            }

            @Override
            protected void process(List<Integer> chunks) {
                int progress = chunks.get(chunks.size() - 1);
                progressBar.setValue(progress);
            }

            @Override
            protected void done() {
                convertButton.setEnabled(true);
                progressBar.setVisible(false);
                try {
                    get();
                    updateStatus("PDF created successfully!");
                    JOptionPane.showMessageDialog(ImageToPDFDialog.this,
                            "PDF created successfully!\n" + outputPath,
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    updateStatus("Error: " + e.getMessage());
                    JOptionPane.showMessageDialog(ImageToPDFDialog.this,
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

    private static class ModernListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            if (value instanceof String) {
                String path = (String) value;
                File file = new File(path);
                setText("📄 " + file.getName());
                setToolTipText(path);
            }
            
            setBorder(new EmptyBorder(8, 12, 8, 12));
            setFont(new Font("Segoe UI", Font.PLAIN, 13));
            
            if (isSelected) {
                setBackground(new Color(52, 152, 219, 30));
                setForeground(new Color(52, 73, 94));
            } else {
                setBackground(Color.WHITE);
                setForeground(new Color(52, 73, 94));
            }
            
            return this;
        }
    }
}
