package com.example.spring_vfdwebsite.services.news;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.spring_vfdwebsite.dtos.searchDTOs.NewsIndexDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.model.Searchable;
import com.meilisearch.sdk.model.TypoTolerance;
import java.util.HashMap;

@Service
public class SearchService {

    private final Client client;
    private final ObjectMapper objectMapper;
    private final String INDEX_NAME = "news"; // Tên bảng trong MeiliSearch

    public SearchService(Client client, ObjectMapper objectMapper) {
        this.client = client;
        this.objectMapper = objectMapper;
        // Tự động tạo Index nếu chưa có
        createIndexIfNotExists();
    }

    private void createIndexIfNotExists() {
        // 1. Tách phần TẠO INDEX ra riêng
        try {
            client.createIndex(INDEX_NAME, "id");
        } catch (Exception e) {
            // Nếu Index đã có rồi thì bỏ qua, không in lỗi để đỡ rác console
        }

        // 2. Phần CẤU HÌNH (Luôn chạy dù index mới hay cũ)
        try {
            Index index = client.index(INDEX_NAME);

            // --- Code cũ của bạn (Giữ nguyên) ---
            index.updateSearchableAttributesSettings(new String[] { "title", "aiTags", "type", "content" });
            index.updateSortableAttributesSettings(new String[] { "createdAt" });

            // --- Code thêm mới: Cấu hình Gõ sai chính tả (SDK 0.15.0) ---
            TypoTolerance typoTolerance = new TypoTolerance();
            typoTolerance.setEnabled(true);

            // Chỉnh sửa độ nhạy: Từ 4 ký tự cho phép sai 1, Từ 8 ký tự cho phép sai 2
            HashMap<String, Integer> minWordSize = new HashMap<>();
            minWordSize.put("oneTypo", 4);
            minWordSize.put("twoTypos", 8);
            typoTolerance.setMinWordSizeForTypos(minWordSize);
        } catch (Exception e) {
            System.err.println("Lỗi cấu hình MeiliSearch: " + e.getMessage());
        }
    }

    // Hàm gọi khi Admin THÊM hoặc SỬA bài viết
    public void addNewsToIndex(NewsIndexDto newsDto) {
        try {
            Index index = client.index(INDEX_NAME);
            // Convert DTO sang JSON string
            String document = objectMapper.writeValueAsString(List.of(newsDto));
            index.addDocuments(document);
        } catch (Exception e) {
            System.err.println("Lỗi khi thêm bài viết vào Index: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Hàm gọi khi Admin XÓA bài viết
    public void deleteNewsFromIndex(String newsId) {
        try {
            Index index = client.index(INDEX_NAME);
            index.deleteDocument(newsId);
        } catch (Exception e) {
            System.err.println("Lỗi khi xóa bài viết khỏi Index: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Searchable searchNews(String keyword) {
        try {
            Index index = client.index(INDEX_NAME);

            // Cấu hình tìm kiếm
            SearchRequest searchRequest = new SearchRequest(keyword)
                    .setLimit(5) // Chỉ lấy 5 kết quả cho Autocomplete
                    .setAttributesToHighlight(new String[] { "title" }) // Highlight từ khóa tìm thấy
                    .setShowMatchesPosition(true);

            return index.search(searchRequest);
        } catch (Exception e) {
            System.err.println("Lỗi khi tìm kiếm: " + e.getMessage());
            return null;
        }
    }
}
