package com.example.spring_vfdwebsite.services.document;

import java.util.List;

import com.example.spring_vfdwebsite.dtos.documentDTOs.DocumentCreateRequestDto;
import com.example.spring_vfdwebsite.dtos.documentDTOs.DocumentResponseDto;
import com.example.spring_vfdwebsite.dtos.documentDTOs.DocumentUpdateRequestDto;

public interface DocumentService {

    List<DocumentResponseDto> getAllDocuments();

    DocumentResponseDto getDocumentById(Integer id);

    DocumentResponseDto createDocument(DocumentCreateRequestDto dto);

    DocumentResponseDto updateDocument(DocumentUpdateRequestDto dto);

    void deleteDocument(Integer id);
}
