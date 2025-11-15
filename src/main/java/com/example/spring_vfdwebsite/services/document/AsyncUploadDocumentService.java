package com.example.spring_vfdwebsite.services.document;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.spring_vfdwebsite.entities.Document;
import com.example.spring_vfdwebsite.exceptions.EntityNotFoundException;
import com.example.spring_vfdwebsite.repositories.DocumentJpaRepository;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;              
import org.slf4j.LoggerFactory;

@Service
public class AsyncUploadDocumentService {
    private final Cloudinary cloudinary;
    private final DocumentJpaRepository documentRepository;
    private static final Logger logger = LoggerFactory.getLogger(AsyncUploadDocumentService.class);

    // Constructor injection
    public AsyncUploadDocumentService(Cloudinary cloudinary, DocumentJpaRepository documentRepository) {
        this.cloudinary = cloudinary;
        this.documentRepository = documentRepository;
    }

    @Async // Quan trọng: Đánh dấu method này chạy bất đồng bộ
    @Transactional // Chạy trong một transaction mới
    public void uploadFileToCloudinary(Integer documentId, byte[] fileBytes, String originalFilename) {
        logger.info("Bắt đầu upload file cho Document ID: {}", documentId);
        try {
            // 1. Tác vụ upload chậm (chạy trong nền)
            Map uploadResult = cloudinary.uploader().upload(fileBytes,
                    ObjectUtils.asMap(
                            "folder", "vfd/documents",
                            "resource_type", "auto",
                            "public_id", originalFilename + "_" + documentId // Gợi ý: Dùng ID để tránh trùng
                    ));

            String fileUrl = (String) uploadResult.get("secure_url");

            // 2. Cập nhật lại bản ghi DB khi upload xong
            Document document = documentRepository.findById(documentId)
                    .orElseThrow(() -> new EntityNotFoundException("Document not found: " + documentId));

            document.setFileUrl(fileUrl);
            document.setStatus(Document.DocumentStatus.COMPLETED); // Đổi trạng thái
            documentRepository.save(document);

            logger.info("Upload thành công cho Document ID: {}", documentId);

        } catch (IOException e) {
            logger.error("Upload thất bại cho Document ID: {}", documentId, e);
            
            // Xử lý khi lỗi: Cập nhật trạng thái FAILED
            documentRepository.findById(documentId).ifPresent(doc -> {
                doc.setStatus(Document.DocumentStatus.FAILED);
                documentRepository.save(doc);
            });
        }
    }
}
