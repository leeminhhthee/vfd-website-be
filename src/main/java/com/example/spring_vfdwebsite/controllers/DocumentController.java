package com.example.spring_vfdwebsite.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.spring_vfdwebsite.dtos.documentDTOs.DocumentCreateRequestDto;
import com.example.spring_vfdwebsite.dtos.documentDTOs.DocumentResponseDto;
import com.example.spring_vfdwebsite.dtos.documentDTOs.DocumentUpdateRequestDto;
import com.example.spring_vfdwebsite.services.document.DocumentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@Tag(name = "Document", description = "APIs for managing documents")
public class DocumentController {

        private final DocumentService documentService;

        // ===================== Get All =====================
        @Operation(summary = "Get all documents", description = "Retrieve a list of all documents", responses = {
                        @ApiResponse(responseCode = "200", description = "List of documents", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DocumentResponseDto.class)))
        })
        @GetMapping
        public ResponseEntity<List<DocumentResponseDto>> getAllDocuments() {
                List<DocumentResponseDto> documents = documentService.getAllDocuments();
                return ResponseEntity.ok(documents);
        }

        // ===================== Get By Id =====================
        @Operation(summary = "Get document by ID", description = "Retrieve document details by ID", responses = {
                        @ApiResponse(responseCode = "200", description = "Document found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DocumentResponseDto.class))),
                        @ApiResponse(responseCode = "404", description = "Document not found")
        })
        @GetMapping("/{id}")
        public ResponseEntity<DocumentResponseDto> getDocumentById(
                        @Parameter(description = "ID of the document", required = true) @PathVariable("id") Integer id) {
                DocumentResponseDto document = documentService.getDocumentById(id);
                return ResponseEntity.ok(document);
        }

        // ===================== Create =====================
        @Operation(summary = "Create new document", description = "Upload a new document along with metadata", responses = {
                        @ApiResponse(responseCode = "200", description = "Document created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DocumentResponseDto.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid input")
        })
        @PostMapping
        public ResponseEntity<DocumentResponseDto> createDocument(
                        @RequestBody DocumentCreateRequestDto dto) {

                DocumentResponseDto createdDocument = documentService.createDocument(dto);
                return ResponseEntity.ok(createdDocument);
        }

        // ===================== Update =====================
        @Operation(summary = "Update document", description = "Update document metadata and optionally replace file", responses = {
                        @ApiResponse(responseCode = "200", description = "Document updated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DocumentResponseDto.class))),
                        @ApiResponse(responseCode = "404", description = "Document not found")
        })
        @PatchMapping(value = "/{id}")
        public ResponseEntity<DocumentResponseDto> updateDocument(
                        @PathVariable("id") Integer id,
                        @RequestBody DocumentUpdateRequestDto dto) {

                dto.setId(id);
                DocumentResponseDto updatedDocument = documentService.updateDocument(dto);
                return ResponseEntity.ok(updatedDocument);
        }

        // ===================== Delete =====================
        @Operation(summary = "Delete document", description = "Delete a document by ID", responses = {
                        @ApiResponse(responseCode = "204", description = "Document deleted"),
                        @ApiResponse(responseCode = "404", description = "Document not found")
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteDocument(
                        @Parameter(description = "ID of the document", required = true) @PathVariable("id") Integer id) {
                documentService.deleteDocument(id);
                return ResponseEntity.noContent().build();
        }
}
