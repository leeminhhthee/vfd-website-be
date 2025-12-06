package com.example.spring_vfdwebsite.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.spring_vfdwebsite.dtos.newsDTOs.NewsCreateRequestDto;
import com.example.spring_vfdwebsite.dtos.newsDTOs.NewsResponseDto;
import com.example.spring_vfdwebsite.dtos.newsDTOs.NewsUpdateRequestDto;
import com.example.spring_vfdwebsite.services.news.NewsService;
import com.example.spring_vfdwebsite.services.news.TitleSummarizationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
@Tag(name = "News Controller", description = "API endpoints for managing news")
public class NewsController {
        private final NewsService newsService;
        private final TitleSummarizationService titleSummarizationService;

        // ===================== Get All News =====================
        @GetMapping
        @Operation(summary = "Get all news", description = "Retrieve a list of all news", responses = {
                        @ApiResponse(responseCode = "200", description = "List of news", content = @Content(mediaType = "application/json", schema = @Schema(implementation = NewsResponseDto.class)))
        })
        public ResponseEntity<List<NewsResponseDto>> getAllNews() {
                List<NewsResponseDto> newsList = newsService.getAllNews();
                return ResponseEntity.ok(newsList);
        }

        // ===================== Get News By ID =====================
        @GetMapping("/{id}")
        @Operation(summary = "Get news by ID", description = "Retrieve news details by ID", responses = {
                        @ApiResponse(responseCode = "200", description = "News found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = NewsResponseDto.class))),
                        @ApiResponse(responseCode = "404", description = "News not found")
        })
        public ResponseEntity<NewsResponseDto> getNewsById(
                        @Parameter(description = "ID of the news", required = true) @PathVariable("id") Integer id) {
                NewsResponseDto news = newsService.getNewsById(id);
                return ResponseEntity.ok(news);
        }

        // ===================== Get News By Slug =====================
        @GetMapping("/slug/{slug}")
        @Operation(summary = "Get news by Slug", description = "Retrieve news details by Slug", responses = {
                        @ApiResponse(responseCode = "200", description = "News found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = NewsResponseDto.class))),
                        @ApiResponse(responseCode = "404", description = "News not found")
        })
        public ResponseEntity<NewsResponseDto> getNewsBySlug(
                        @Parameter(description = "Slug of the news", required = true) @PathVariable("slug") String slug) {
                NewsResponseDto news = newsService.getNewsBySlug(slug);
                return ResponseEntity.ok(news);
        }

        // ===================== Get News By Id and Slug =====================
        @GetMapping("/{id}/{slug}")
        @Operation(summary = "Get news by ID and Slug", description = "Retrieve news details by ID and Slug", responses = {
                        @ApiResponse(responseCode = "200", description = "News found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = NewsResponseDto.class))),
                        @ApiResponse(responseCode = "404", description = "News not found")
        })
        public ResponseEntity<NewsResponseDto> getNewsByIdSlug(
                        @Parameter(description = "ID of the news", required = true) @PathVariable("id") Integer id,
                        @Parameter(description = "Slug of the news", required = true) @PathVariable("slug") String slug) {
                NewsResponseDto news = newsService.getNewsByIdSlug(id, slug);
                return ResponseEntity.ok(news);
        }

        // ===================== Create News =====================
        @PostMapping
        @Operation(summary = "Create new news", description = "Create a new news item with details", responses = {
                        @ApiResponse(responseCode = "200", description = "News created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = NewsResponseDto.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid input")
        })
        public ResponseEntity<NewsResponseDto> createNews(@RequestBody NewsCreateRequestDto dto) {
                NewsResponseDto createdNews = newsService.createNews(dto);
                return ResponseEntity.ok(createdNews);
        }

        // ===================== Update News =====================
        @PatchMapping("/{id}")
        @Operation(summary = "Update news", description = "Update news details", responses = {
                        @ApiResponse(responseCode = "200", description = "News updated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = NewsResponseDto.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid input"),
                        @ApiResponse(responseCode = "404", description = "News not found")
        })
        public ResponseEntity<NewsResponseDto> updateNews(
                        @Parameter(description = "ID of the news", required = true) @PathVariable("id") Integer id,
                        @RequestBody NewsUpdateRequestDto dto) {
                dto.setId(id);
                NewsResponseDto updatedNews = newsService.updateNews(id, dto);
                return ResponseEntity.ok(updatedNews);
        }

        // ===================== Delete News =====================
        @DeleteMapping("/{id}")
        @Operation(summary = "Delete news", description = "Delete a news item by ID", responses = {
                        @ApiResponse(responseCode = "204", description = "News deleted"),
                        @ApiResponse(responseCode = "404", description = "News not found")
        })
        public ResponseEntity<Void> deleteNews(
                        @Parameter(description = "ID of the news", required = true) @PathVariable("id") Integer id) {
                newsService.deleteNews(id);
                return ResponseEntity.noContent().build();
        }

        // Title Summarization using AI
        @PostMapping("/suggest-title")
        public ResponseEntity<?> suggestTitle(@RequestBody Map<String, String> payload) {
                String content = payload.get("content");

                if (content == null || content.length() < 50) {
                        return ResponseEntity.badRequest().body("Nội dung bài viết quá ngắn để tóm tắt.");
                }

                List<String> suggestedTitles = titleSummarizationService.generateTitle(content);

                // Trả về JSON object
                return ResponseEntity.ok(Map.of("titles", suggestedTitles));
        }
}