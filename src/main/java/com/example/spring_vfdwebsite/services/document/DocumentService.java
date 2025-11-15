package com.example.spring_vfdwebsite.services.document;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.spring_vfdwebsite.dtos.documentDTOs.DocumentCreateRequestDto;
import com.example.spring_vfdwebsite.dtos.documentDTOs.DocumentResponseDto;
import com.example.spring_vfdwebsite.dtos.documentDTOs.DocumentUpdateRequestDto;

public interface DocumentService {

    List<DocumentResponseDto> getAllDocuments();

    DocumentResponseDto getDocumentById(Integer id);

    DocumentResponseDto createDocument(DocumentCreateRequestDto dto, MultipartFile file) throws IOException;

    DocumentResponseDto updateDocument(DocumentUpdateRequestDto dto, MultipartFile file) throws IOException;

    void deleteDocument(Integer id);
}
