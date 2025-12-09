package com.example.spring_vfdwebsite.services.matchSchedule;

import com.example.spring_vfdwebsite.dtos.matchScheduleDTOs.fillDataDTOs.MatchAiDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

@Service
public class GeminiOcrService {

    @Value("${spring.google.gemini.api.url}")
    private String apiUrl;

    @Value("${spring.google.gemini.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public GeminiOcrService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public List<MatchAiDto> extractScheduleFromImageUrl(String imageUrl) {
        String finalUrl = apiUrl + "?key=" + apiKey;
        int maxRetries = 3;
        int attempt = 0;

        while (attempt < maxRetries) {
            try {
                attempt++;
                // 1. Tải ảnh từ URL về dạng byte[] để gửi cho Gemini (An toàn hơn gửi link trực
                // tiếp)
                String base64Image = downloadImageAsBase64(imageUrl);

                // 2. Tạo Prompt
                String prompt = """
                                            Bạn là trợ lý nhập liệu giải bóng chuyền. Hãy phân tích hình ảnh lịch thi đấu này.
                                            Trích xuất danh sách các trận đấu thành định dạng JSON.
                                            Các trường cần lấy:
                                            - id: Số thứ tự trận đấu, bắt đầu từ 1, tăng dần cho mỗi trận đấu
                                            - round: Tên vòng đấu (GROUP|ROUND_OF_16|QUARTER_FINAL|SEMI_FINAL|FINAL|THIRD_PLACE|null), lưu ý nếu extract được thông tin vòng đấu thì map đúng với Enum đã cho (tức là viết hoa, gạch dưới thay cho dấu cách hoặc dấu gạch nối, ví dụ: ROUND_OF_16 chứ không được viết ROUND-OF-16), nếu không xác định được thì để null
                                            - groupTable: Tên bảng (Bảng A, Bảng B...)
                                            - matchDate: Ngày giờ thi đấu (Định dạng ISO 8601)
                                            - teamA: Tên đội 1
                                            - teamB: Tên đội 2

                                            Chỉ trả về một mảng JSON thuần túy (Array of objects), không dùng Markdown, không ```json.
                                            Ví dụ: {
                          "match_schedules": [
                            {
                              "id": number,
                              "round": string | null,
                              "groupTable": string | null,
                              "matchDate": string (ISO, ví dụ: 2025-11-01T18:00:00),
                              "teamA": string,
                              "teamB": string
                            }
                          ]
                        }
                                            """;

                // 3. Cấu trúc Request Body
                Map<String, Object> requestBody = new HashMap<>();
                Map<String, Object> content = new HashMap<>();

                Map<String, String> textPart = new HashMap<>();
                textPart.put("text", prompt);

                Map<String, Object> imagePart = new HashMap<>();
                Map<String, String> inlineData = new HashMap<>();
                inlineData.put("mime_type", "image/jpeg"); // Mặc định jpeg, Gemini tự nhận diện
                inlineData.put("data", base64Image);
                imagePart.put("inline_data", inlineData);

                content.put("parts", List.of(textPart, imagePart));
                requestBody.put("contents", List.of(content));

                // 4. Gửi Request
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

                ResponseEntity<String> response = restTemplate.postForEntity(finalUrl, entity, String.class);

                // 5. Parse kết quả
                return parseGeminiResponse(response.getBody());

            } catch (HttpServerErrorException.ServiceUnavailable e) {
                // Nếu gặp lỗi 503 (Overloaded)
                System.err.println("Gemini bị quá tải (Lần thử " + attempt + "). Đang thử lại...");
                if (attempt == maxRetries) {
                    // Hết lượt thử thì trả về rỗng hoặc ném lỗi tùy bạn
                    return Collections.emptyList();
                }
                try {
                    Thread.sleep(2000); // Nghỉ 2 giây rồi thử lại
                } catch (InterruptedException ignored) {
                }
            } catch (Exception e) {
                e.printStackTrace();
                return Collections.emptyList(); // Lỗi khác thì dừng luôn
            }
        }
        return Collections.emptyList();

    }

    // --- Helper Methods ---

    private String downloadImageAsBase64(String imageUrl) throws Exception {
        URL url = new URL(imageUrl);
        try (InputStream is = url.openStream();
                ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            return Base64.getEncoder().encodeToString(os.toByteArray());
        }
    }

    private List<MatchAiDto> parseGeminiResponse(String rawResponse) {
        try {
            // 1. Lấy text JSON thô từ cấu trúc response phức tạp của Gemini
            JsonNode root = objectMapper.readTree(rawResponse);
            if (!root.path("candidates").has(0)) {
                return Collections.emptyList();
            }

            String jsonText = root.path("candidates").get(0)
                    .path("content").path("parts").get(0)
                    .path("text").asText();

            // 2. Làm sạch chuỗi (xóa markdown ```json nếu có)
            jsonText = jsonText.replace("```json", "").replace("```", "").trim();

            // 3. Đọc chuỗi JSON đã làm sạch thành JsonNode để kiểm tra cấu trúc
            JsonNode actualDataNode = objectMapper.readTree(jsonText);

            // TRƯỜNG HỢP 1: AI trả về Mảng chuẩn [...] -> Parse luôn
            if (actualDataNode.isArray()) {
                return objectMapper.readValue(jsonText, new TypeReference<List<MatchAiDto>>() {
                });
            }

            // TRƯỜNG HỢP 2: AI trả về Object {...} -> Tìm mảng bên trong
            if (actualDataNode.isObject()) {
                // Thường AI sẽ dùng key "matches", "data", hoặc "result"
                // Ta ưu tiên tìm key "matches" trước
                if (actualDataNode.has("matches")) {
                    String matchesJson = actualDataNode.get("matches").toString();
                    return objectMapper.readValue(matchesJson, new TypeReference<List<MatchAiDto>>() {
                    });
                }

                // Nếu không thấy key "matches", tìm bất kỳ key nào chứa Mảng
                Iterator<String> fieldNames = actualDataNode.fieldNames();
                while (fieldNames.hasNext()) {
                    String fieldName = fieldNames.next();
                    JsonNode childNode = actualDataNode.get(fieldName);
                    if (childNode.isArray()) {
                        return objectMapper.readValue(childNode.toString(), new TypeReference<List<MatchAiDto>>() {
                        });
                    }
                }
            }

            // Nếu không phải mảng, cũng không phải object chứa mảng -> Lỗi
            System.err.println("AI trả về format không hỗ trợ: " + jsonText);
            return Collections.emptyList();

        } catch (

        Exception e) {
            System.err.println("Lỗi parse JSON từ Gemini: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
