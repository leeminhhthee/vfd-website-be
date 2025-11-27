package com.example.spring_vfdwebsite.services.news;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;

import com.example.spring_vfdwebsite.dtos.newsDTOs.TitleSummarizationDto;

import org.springframework.http.MediaType;

@Service
public class TitleSummarizationService {

    @Value("${spring.google.gemini.api.url}")
    private String apiUrl;

    @Value("${spring.google.gemini.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public TitleSummarizationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<String> generateTitle(String newsContent) {
        // 1. Tạo URL có chứa Key
        String finalUrl = apiUrl + "?key=" + apiKey;

        // 2. Tạo Prompt (Câu lệnh cho AI)
        String prompt = "Bạn là biên tập viên thể thao. Dưới đây là nội dung bài viết: \n"
                + "\"" + newsContent + "\"\n"
                + "Hãy gợi ý 3 tiêu đề bài báo hấp dẫn, ngắn gọn (dưới 18 từ) cho bài viết này. "
                + "Trả về kết quả dưới dạng gạch đầu dòng, không cần lời dẫn.";

        // 3. Tạo Body request
        TitleSummarizationDto.Request requestBody = new TitleSummarizationDto.Request(prompt);

        // 4. Tạo Header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TitleSummarizationDto.Request> entity = new HttpEntity<>(requestBody, headers);

        try {
            // 5. Gọi API
            ResponseEntity<TitleSummarizationDto.Response> response = restTemplate.postForEntity(
                    finalUrl, entity, TitleSummarizationDto.Response.class);

            // 6. Lấy kết quả trả về
            if (response.getBody() != null && !response.getBody().getCandidates().isEmpty()) {
                String rawText = response.getBody().getCandidates().get(0).getContent().getParts().get(0).getText();
                return Arrays.stream(rawText.split("\n")) // Cắt dòng dựa trên ký tự xuống dòng
                        .map(line -> line.replace("*", "").replace("-", "").trim()) // Xóa dấu *, dấu - và khoảng trắng
                                                                                    // thừa
                        .filter(line -> !line.isEmpty()) // Loại bỏ các dòng trống
                        .collect(Collectors.toList()); // Gom lại thành List<String>
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.singletonList("Lỗi khi gọi AI: " + e.getMessage());
        }

        return Collections.singletonList("Không tạo được tiêu đề.");
    }

    public List<String> generateTags(String newsContent) {
        // 1. Tạo URL
        String finalUrl = apiUrl + "?key=" + apiKey;

        // 2. Tạo Prompt chuyên dụng để sinh Keywords
        // Yêu cầu AI trả về dạng: "Từ khóa 1, Từ khóa 2, Từ khóa 3"
        String prompt = "Bạn là chuyên gia SEO và biên tập viên bóng chuyền. Dựa vào nội dung bài viết sau: \n"
                + "\"" + newsContent + "\"\n"
                + "Hãy liệt kê 5 đến 8 từ khóa (tags) quan trọng nhất để người dùng tìm kiếm bài viết này (bao gồm cả các từ khóa ẩn liên quan về ngữ nghĩa như tên giải đấu, tên viết tắt, tên cầu thủ). "
                + "Chỉ trả về danh sách các từ khóa ngăn cách nhau bởi dấu phẩy. Không đánh số thứ tự, không xuống dòng, không thêm lời dẫn.";

        // 3. Tạo Request
        TitleSummarizationDto.Request requestBody = new TitleSummarizationDto.Request(prompt);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TitleSummarizationDto.Request> entity = new HttpEntity<>(requestBody, headers);

        try {
            // 4. Gọi API
            ResponseEntity<TitleSummarizationDto.Response> response = restTemplate.postForEntity(
                    finalUrl, entity, TitleSummarizationDto.Response.class);

            // 5. Xử lý kết quả
            if (response.getBody() != null && !response.getBody().getCandidates().isEmpty()) {
                String rawText = response.getBody().getCandidates().get(0).getContent().getParts().get(0).getText();

                // rawText ví dụ: "Bóng chuyền nữ, VTV Cup 2025, Thanh Thúy, Đội tuyển Việt Nam"

                return Arrays.stream(rawText.split(",")) // Cắt bằng dấu phẩy
                        .map(tag -> tag.trim()) // Xóa khoảng trắng 2 đầu (VD: " VTV Cup " -> "VTV Cup")
                        .filter(tag -> !tag.isEmpty()) // Bỏ tag rỗng
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Nếu lỗi tags thì trả về list rỗng (để không làm hỏng luồng lưu bài viết)
            // Khác với Title, Tags là phụ nên lỗi thì bỏ qua
            return Collections.emptyList();
        }

        return Collections.emptyList();
    }

}
