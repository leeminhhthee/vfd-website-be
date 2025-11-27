package com.example.spring_vfdwebsite.controllers;

import com.example.spring_vfdwebsite.services.news.SearchService;
import com.meilisearch.sdk.model.Searchable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/search")
@RequiredArgsConstructor
@Tag(name = "Search Controller", description = "API endpoints for searching news articles")
public class SearchController {

    private final SearchService searchService;

    // API: GET /api/public/search?q=bong chuyen
    @Operation(summary = "Search news articles", description = "Search for news articles based on a query string", responses = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Searchable.class)))
    })
    @GetMapping
    public ResponseEntity<?> search(@RequestParam("q") String query) {
        Searchable result = searchService.searchNews(query);
        return ResponseEntity.ok(result);
    }
}
