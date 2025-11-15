package com.example.spring_vfdwebsite.services.document;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.spring_vfdwebsite.dtos.documentDTOs.DocumentCreateRequestDto;
import com.example.spring_vfdwebsite.dtos.documentDTOs.DocumentResponseDto;
import com.example.spring_vfdwebsite.dtos.documentDTOs.DocumentUpdateRequestDto;
import com.example.spring_vfdwebsite.entities.Document;
import com.example.spring_vfdwebsite.entities.Document.DocumentStatus;
import com.example.spring_vfdwebsite.entities.User;
import com.example.spring_vfdwebsite.events.document.DocumentCreatedEvent;
import com.example.spring_vfdwebsite.events.document.DocumentDeletedEvent;
import com.example.spring_vfdwebsite.events.document.DocumentUpdatedEvent;
import com.example.spring_vfdwebsite.exceptions.EntityNotFoundException;
import com.example.spring_vfdwebsite.repositories.DocumentJpaRepository;
import com.example.spring_vfdwebsite.repositories.UserJpaRepository;
import com.example.spring_vfdwebsite.utils.CloudinaryUtils;
import com.example.spring_vfdwebsite.utils.FileUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final Cloudinary cloudinary;
    private final DocumentJpaRepository documentRepository;
    private final UserJpaRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    private AsyncUploadDocumentService asyncUploadService;

    // ===================== Get all =====================
    @Override
    @Cacheable(value = "documents", key = "'all'")
    @Transactional(readOnly = true)
    public List<DocumentResponseDto> getAllDocuments() {
        System.out.println("🔥 Fetching all documents from the database...");
        return documentRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // ===================== Get By Id =====================
    @Override
    @Cacheable(value = "documents", key = "#id")
    @Transactional(readOnly = true)
    public DocumentResponseDto getDocumentById(Integer id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Document with id " + id + " not found"));
        return toDto(document);
    }

    // ===================== Create =====================
    @Override
    @Transactional
    @CacheEvict(value = "documents", allEntries = true)
    public DocumentResponseDto createDocument(DocumentCreateRequestDto dto, MultipartFile file) throws IOException {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((org.springframework.security.core.userdetails.User) principal).getUsername();

        User currentUser = userRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));

        byte[] fileBytes = file.getBytes();
        String originalFilename = file.getOriginalFilename();
        String fileType = file.getContentType();
        Long fileSize = file.getSize();

        Document document = Document.builder()
                .title(dto.getTitle())
                .category(Document.DocumentCategory.fromValue(dto.getCategory()))
                .fileName(originalFilename)
                .fileUrl(null) // Url chưa có
                .fileType(fileType)
                .fileSize(fileSize)
                .uploadedBy(currentUser)
                .status(DocumentStatus.UPLOADING) // Trạng thái đang upload
                .build();

        document = documentRepository.save(document);

        asyncUploadService.uploadFileToCloudinary(document.getId(), fileBytes, originalFilename);

        // Publish event
        eventPublisher.publishEvent(new DocumentCreatedEvent(document.getId(), document));

        return toDto(document);
    }

    // ===================== Update =====================
    @Override
    @Transactional
    @CachePut(value = "documents", key = "#dto.id")
    @CacheEvict(value = "documents", allEntries = true)
    public DocumentResponseDto updateDocument(DocumentUpdateRequestDto dto, MultipartFile file) throws IOException {
        Document document = documentRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Document with id " + dto.getId() + " not found"));

        if (dto.getTitle() != null)
            document.setTitle(dto.getTitle());
        if (dto.getCategory() != null)
            document.setCategory(Document.DocumentCategory.fromValue(dto.getCategory()));

        if (file != null && !file.isEmpty()) {
            // === CÓ FILE MỚI: CẬP NHẬT VÀ UPLOAD LẠI FILE ===
            // Lấy byte[] ngay lập tức
            byte[] fileBytes = file.getBytes();
            String originalFilename = file.getOriginalFilename();

            // Cập nhật các trường thông tin file và trạng thái
            document.setFileName(originalFilename);
            document.setFileType(file.getContentType());
            document.setFileSize(file.getSize());
            document.setStatus(DocumentStatus.UPLOADING); // Đặt trạng thái
            document.setFileUrl(null); // Xóa URL cũ, chờ URL mới

            // Lưu vào DB (Nhanh)
            document = documentRepository.save(document);

            // "Bắn" (Fire) tác vụ upload bất đồng bộ (Nhanh)
            asyncUploadService.uploadFileToCloudinary(document.getId(), fileBytes, originalFilename);

            // Publish event (Nhanh)
            eventPublisher.publishEvent(new DocumentUpdatedEvent(document.getId(), document));

            // Trả về DTO ngay
            return toDto(document);

        } else {
            // === KHÔNG CÓ FILE MỚI: CHỈ CẬP NHẬT METADATA ===
            // (Giữ logic cũ của bạn nếu DTO có thể ghi đè URL/tên file)
            if (dto.getFileName() != null)
                document.setFileName(dto.getFileName());
            if (dto.getFileUrl() != null)
                document.setFileUrl(dto.getFileUrl());
            if (dto.getFileType() != null)
                document.setFileType(dto.getFileType());
            if (dto.getFileSize() != null)
                document.setFileSize(dto.getFileSize());

            // Lưu vào DB (Nhanh)
            document = documentRepository.save(document);

            // Publish event
            eventPublisher.publishEvent(new DocumentUpdatedEvent(document.getId(), document));

            return toDto(document);
        }
    }

    // ===================== Delete =====================
    @Override
    @Transactional
    @CacheEvict(value = "documents", key = "'all'")
    public void deleteDocument(Integer id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Document with id " + id + " not found"));

        // 🔹 Xoá file trên Cloudinary (nếu có)
        if (document.getFileUrl() != null) {
            try {
                String publicId = CloudinaryUtils.extractPublicId(document.getFileUrl());
                cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            } catch (Exception e) {
                // throw new RuntimeException("Failed to delete file from Cloudinary: " +
                // e.getMessage());
                System.err.println("⚠️ Failed to delete file from Cloudinary: " + e.getMessage());
            }
        }
        documentRepository.delete(document);
        // Publish event
        eventPublisher.publishEvent(new DocumentDeletedEvent(id));
    }

    // ===== Helper mapping =====
    private DocumentResponseDto toDto(Document document) {
        DocumentResponseDto.UploadedByDto uploadedByDto = DocumentResponseDto.UploadedByDto.builder()
                .id(document.getUploadedBy().getId())
                .fullName(document.getUploadedBy().getFullName())
                .email(document.getUploadedBy().getEmail())
                .imageUrl(document.getUploadedBy().getImageUrl())
                .build();

        return DocumentResponseDto.builder()
                .id(document.getId())
                .title(document.getTitle())
                .category(document.getCategory().getValue())
                .fileName(document.getFileName())
                .fileUrl(document.getFileUrl())
                .fileType(document.getFileType())
                .fileSize(FileUtils.formatFileSize(document.getFileSize()))
                .createdAt(document.getCreatedAt())
                .updatedAt(document.getUpdatedAt())
                .uploadedBy(uploadedByDto)
                .status(document.getStatus().name())
                .build();
    }
}
