package com.example;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Arrays;

/**
 * Core service class for converting images to PDF
 */
public class ImageToPDFService {

    // Supported image formats
    private static final String[] SUPPORTED_FORMATS = {
            ".jpg", ".jpeg", ".png", ".gif", ".bmp", ".tiff", ".tif"
    };

    /**
     * Converts multiple images into a single PDF document
     * @param imagePaths List of image file paths
     * @param outputPath Output PDF file path
     * @throws IOException if there's an error processing files
     */
    public void convertImagesToPDF(List<String> imagePaths, String outputPath) throws IOException {
        try (PDDocument document = new PDDocument()) {

            for (String imagePath : imagePaths) {
                if (isValidImageFile(imagePath)) {
                    addImageToDocument(document, imagePath);
                    System.out.println("Added image: " + imagePath);
                } else {
                    System.err.println("Skipping unsupported file: " + imagePath);
                }
            }

            if (document.getNumberOfPages() == 0) {
                throw new IOException("No valid images found to convert");
            }

            document.save(outputPath);
            System.out.println("PDF created successfully: " + outputPath);
        }
    }

    /**
     * Adds a single image as a new page in the PDF document
     */
    private void addImageToDocument(PDDocument document, String imagePath) throws IOException {
        try {
            // Load the image
            PDImageXObject image = PDImageXObject.createFromFile(imagePath, document);

            // Calculate page size and scaling
            PDRectangle pageSize = calculateOptimalPageSize(image);
            PDPage page = new PDPage(pageSize);
            document.addPage(page);

            // Calculate image position and scaling
            ImageDimensions dimensions = calculateImageDimensions(image, pageSize);

            // Draw the image on the page
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.drawImage(image,
                        dimensions.x,
                        dimensions.y,
                        dimensions.scaledWidth,
                        dimensions.scaledHeight);
            }

        } catch (IOException e) {
            System.err.println("Failed to process image: " + imagePath);
            throw e;
        }
    }

    /**
     * Calculates optimal page size based on image dimensions
     */
    private PDRectangle calculateOptimalPageSize(PDImageXObject image) {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        // Use A4 as default, but adjust if image has different aspect ratio
        if (imageWidth > imageHeight) {
            // Landscape orientation
            return new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth());
        } else {
            // Portrait orientation
            return PDRectangle.A4;
        }
    }

    /**
     * Calculates image dimensions and position for optimal fitting
     */
    private ImageDimensions calculateImageDimensions(PDImageXObject image, PDRectangle pageSize) {
        int originalWidth = image.getWidth();
        int originalHeight = image.getHeight();
        float pageWidth = pageSize.getWidth();
        float pageHeight = pageSize.getHeight();

        // Calculate scaling ratio to fit image within page bounds
        float scaleX = pageWidth / originalWidth;
        float scaleY = pageHeight / originalHeight;
        float scale = Math.min(scaleX, scaleY) * 0.9f; // Leave some margin

        float scaledWidth = originalWidth * scale;
        float scaledHeight = originalHeight * scale;

        // Center the image on the page
        float x = (pageWidth - scaledWidth) / 2;
        float y = (pageHeight - scaledHeight) / 2;

        return new ImageDimensions(x, y, scaledWidth, scaledHeight);
    }

    /**
     * Validates if the file is a supported image format
     */
    private boolean isValidImageFile(String filePath) {
        if (filePath == null) return false;

        String lowerCasePath = filePath.toLowerCase();
        return Arrays.stream(SUPPORTED_FORMATS)
                .anyMatch(lowerCasePath::endsWith);
    }

    /**
     * Helper class to store image dimensions and position
     */
    private static class ImageDimensions {
        final float x, y, scaledWidth, scaledHeight;

        ImageDimensions(float x, float y, float scaledWidth, float scaledHeight) {
            this.x = x;
            this.y = y;
            this.scaledWidth = scaledWidth;
            this.scaledHeight = scaledHeight;
        }
    }
}
