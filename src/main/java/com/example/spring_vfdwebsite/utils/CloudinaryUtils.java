package com.example.spring_vfdwebsite.utils;

import java.util.List;

import org.springframework.stereotype.Component;

import com.cloudinary.Cloudinary;
import com.cloudinary.api.ApiResponse;
import com.cloudinary.utils.ObjectUtils;

@Component
public class CloudinaryUtils {

    private final Cloudinary cloudinary;

    public CloudinaryUtils(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    /**
     * Extract public ID from full Cloudinary URL
     */
    public static String extractPublicId(String fileUrl) {
        String[] parts = fileUrl.split("/");
        String filenameWithExt = parts[parts.length - 1];
        String filenameWithoutExt = filenameWithExt.contains(".")
                ? filenameWithExt.substring(0, filenameWithExt.lastIndexOf('.'))
                : filenameWithExt;

        int uploadIndex = fileUrl.indexOf("/upload/");
        String path = fileUrl.substring(uploadIndex + "/upload/".length(), fileUrl.lastIndexOf('/'));

        return path.isEmpty() ? filenameWithoutExt : path + "/" + filenameWithoutExt;
    }

    /**
     * Map Cloudinary format to MIME type
     */
    public static String mapFormatToMimeType(String format) {
        return switch (format.toLowerCase()) {
            case "pdf" -> "application/pdf";
            case "png" -> "image/png";
            case "jpg", "jpeg" -> "image/jpeg";
            case "doc", "docx" -> "application/msword";
            default -> "application/octet-stream";
        };
    }

    /**
     * Get metadata from Cloudinary by fileUrl
     */
    public ApiResponse getMetadata(String fileUrl) throws Exception {
        String publicId = extractPublicId(fileUrl);
        return cloudinary.api().resource(publicId, ObjectUtils.emptyMap());
    }

    /**
     * Delete file from Cloudinary
     * 
     * @param fileUrl full Cloudinary URL
     */
    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty())
            return;
        try {
            String publicId = extractPublicId(fileUrl);
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (Exception e) {
            System.err.println("Failed to delete file from Cloudinary: " + e.getMessage());
        }
    }

    /**
     * Delete multiple files from Cloudinary
     * 
     * @param fileUrls List of full Cloudinary URLs
     */
    public void deleteFiles(List<String> fileUrls) {
        if (fileUrls == null || fileUrls.isEmpty())
            return;

        for (String fileUrl : fileUrls) {
            deleteFile(fileUrl);
        }
    }
}