package com.example.spring_vfdwebsite.services.document;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.spring_vfdwebsite.dtos.documentDTOs.DocumentCreateRequestDto;
import com.example.spring_vfdwebsite.dtos.documentDTOs.DocumentResponseDto;
import com.example.spring_vfdwebsite.dtos.documentDTOs.DocumentUpdateRequestDto;
import com.example.spring_vfdwebsite.entities.Document;
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

    private final DocumentJpaRepository documentRepository;
    private final UserJpaRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final CloudinaryUtils cloudinaryUtils;

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
    public DocumentResponseDto createDocument(DocumentCreateRequestDto dto) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((org.springframework.security.core.userdetails.User) principal).getUsername();

        User currentUser = userRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));

        String fileName = dto.getFileName();
        String fileType = dto.getFileType();
        Long fileSize = dto.getFileSize();

        // Nếu chỉ có fileUrl, lấy metadata
        if (dto.getFileUrl() != null && (fileName == null || fileType == null || fileSize == null)) {
            try {
                var response = cloudinaryUtils.getMetadata(dto.getFileUrl());
                if (fileName == null)
                    fileName = response.get("original_filename") + "." + response.get("format");
                if (fileType == null)
                    fileType = CloudinaryUtils.mapFormatToMimeType((String) response.get("format"));
                if (fileSize == null)
                    fileSize = ((Number) response.get("bytes")).longValue();
            } catch (Exception e) {
                System.err.println("⚠️ Failed to fetch metadata from Cloudinary: " + e.getMessage());
            }
        }

        Document document = Document.builder()
                .title(dto.getTitle())
                .category(dto.getCategory())
                .fileName(fileName)
                .fileUrl(dto.getFileUrl())
                .fileType(fileType)
                .fileSize(fileSize)
                .uploadedBy(currentUser)
                .build();

        document = documentRepository.save(document);

        // Publish event
        eventPublisher.publishEvent(new DocumentCreatedEvent(document.getId(), document));

        return toDto(document);
    }

    // ===================== Update =====================
    @Override
    @Transactional
    @CachePut(value = "documents", key = "#dto.id")
    @CacheEvict(value = "documents", key = "'all'")
    public DocumentResponseDto updateDocument(DocumentUpdateRequestDto dto) {
        Document document = documentRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Document with id " + dto.getId() + " not found"));

        if (dto.getTitle() != null)
            document.setTitle(dto.getTitle());
        if (dto.getCategory() != null)
            document.setCategory(dto.getCategory());

        if (dto.getFileUrl() != null) {
            document.setFileUrl(dto.getFileUrl());

            if (dto.getFileName() == null || dto.getFileType() == null || dto.getFileSize() == null) {
                try {
                    var response = cloudinaryUtils.getMetadata(dto.getFileUrl());
                    if (dto.getFileName() == null)
                        document.setFileName(response.get("original_filename") + "." + response.get("format"));
                    else
                        document.setFileName(dto.getFileName());

                    if (dto.getFileType() == null)
                        document.setFileType(CloudinaryUtils.mapFormatToMimeType((String) response.get("format")));
                    else
                        document.setFileType(dto.getFileType());

                    if (dto.getFileSize() == null)
                        document.setFileSize(((Number) response.get("bytes")).longValue());
                    else
                        document.setFileSize(dto.getFileSize());

                } catch (Exception e) {
                    System.err.println("⚠️ Failed to fetch metadata from Cloudinary: " + e.getMessage());
                }
            } else {
                document.setFileName(dto.getFileName());
                document.setFileType(dto.getFileType());
                document.setFileSize(dto.getFileSize());
            }
        }

        document = documentRepository.save(document);

        // Publish event
        eventPublisher.publishEvent(new DocumentUpdatedEvent(document.getId(), document));

        return toDto(document);
    }

    // ===================== Delete =====================
    @Override
    @Transactional
    @CacheEvict(value = "documents", key = "'all'")
    public void deleteDocument(Integer id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Document with id " + id + " not found"));

        if (document.getFileUrl() != null) {
            cloudinaryUtils.deleteFile(document.getFileUrl());
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
                .category(document.getCategory())
                .fileName(document.getFileName())
                .fileUrl(document.getFileUrl())
                .fileType(document.getFileType())
                .fileSize(FileUtils.formatFileSize(document.getFileSize()))
                .createdAt(document.getCreatedAt())
                .updatedAt(document.getUpdatedAt())
                .uploadedBy(uploadedByDto)
                .build();
    }
}
