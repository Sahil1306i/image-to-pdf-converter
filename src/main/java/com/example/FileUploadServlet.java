package com.example;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Servlet for handling image uploads and PDF conversion
 */
@WebServlet("/upload")
public class FileUploadServlet extends HttpServlet {

    private static final String UPLOAD_DIRECTORY = "uploads";
    private static final int MEMORY_THRESHOLD = 1024 * 1024 * 3; // 3MB
    private static final int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB

    private final ImageToPDFService pdfService = new ImageToPDFService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check if request contains multipart content
        if (!ServletFileUpload.isMultipartContent(request)) {
            response.getWriter().println("Error: Form must have enctype=multipart/form-data.");
            return;
        }

        // Configure upload settings
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setFileSizeMax(MAX_FILE_SIZE);
        upload.setSizeMax(MAX_REQUEST_SIZE);

        // Construct the directory path to store upload file
        String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        List<String> uploadedFiles = new ArrayList<>();

        try {
            // Parse the request's content to extract file data
            @SuppressWarnings("unchecked")
            List<FileItem> formItems = upload.parseRequest(request);

            if (formItems != null && formItems.size() > 0) {
                // Iterate over form's fields
                for (FileItem item : formItems) {
                    // Process only fields that are not form fields (i.e., file fields)
                    if (!item.isFormField()) {
                        String fileName = new File(item.getName()).getName();
                        if (isValidImageFile(fileName)) {
                            String filePath = uploadPath + File.separator + fileName;
                            File storeFile = new File(filePath);

                            // Save the file on disk
                            item.write(storeFile);
                            uploadedFiles.add(filePath);
                        }
                    }
                }
            }

            if (uploadedFiles.isEmpty()) {
                response.getWriter().println("Error: No valid image files were uploaded.");
                return;
            }

            // Convert images to PDF
            String pdfFileName = "converted_" + UUID.randomUUID().toString() + ".pdf";
            String pdfPath = uploadPath + File.separator + pdfFileName;

            pdfService.convertImagesToPDF(uploadedFiles, pdfPath);

            // Set response to download the PDF
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + pdfFileName + "\"");

            // Stream the PDF file to the response
            try (FileInputStream fileInputStream = new FileInputStream(pdfPath);
                 OutputStream responseOutputStream = response.getOutputStream()) {

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    responseOutputStream.write(buffer, 0, bytesRead);
                }
            }

            // Clean up uploaded files and generated PDF
            for (String filePath : uploadedFiles) {
                new File(filePath).delete();
            }
            new File(pdfPath).delete();

        } catch (Exception ex) {
            response.getWriter().println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private boolean isValidImageFile(String fileName) {
        if (fileName == null) return false;
        String lowerCase = fileName.toLowerCase();
        return lowerCase.endsWith(".jpg") || lowerCase.endsWith(".jpeg") ||
                lowerCase.endsWith(".png") || lowerCase.endsWith(".gif") ||
                lowerCase.endsWith(".bmp") || lowerCase.endsWith(".tiff") ||
                lowerCase.endsWith(".tif");
    }
}
