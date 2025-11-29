// package com.example.spring_vfdwebsite.services.matchSchedule;

// import java.util.Collections;
// import java.util.List;
// import java.util.Map;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;

// import com.example.spring_vfdwebsite.dtos.matchScheduleDTOs.fillDataDTOs.MatchAiDto;
// import com.fasterxml.jackson.core.JsonProcessingException;
// import com.fasterxml.jackson.databind.JsonNode;
// import com.fasterxml.jackson.databind.ObjectMapper;

// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;

// import org.springframework.web.reactive.function.client.WebClient;
// import org.springframework.web.reactive.function.client.WebClientResponseException;

// @Service
// @Slf4j
// @RequiredArgsConstructor
// public class OpenRouterService {

//     private final WebClient webClient;
//     private final ObjectMapper objectMapper = new ObjectMapper();

//     @Value("${spring.openrouter.api.key}")
//     private String apiKey;

//     @Value("${spring.openrouter.api.url}")
//     private String apiUrl;

//     /**
//      * MAIN METHOD: Extract matches from image URL
//      */
//     public List<MatchAiDto> extractMatchesFromImageUrl(String imageUrl) {
//         try {
//             String rawResponse = callOpenRouter(imageUrl);
//             log.info("Raw AI response: {}", rawResponse);

//             String json = extractJsonArray(rawResponse);
//             if (json == null) {
//                 log.error("AI response does not contain valid JSON array");
//                 return Collections.emptyList();
//             }

//             return parseJsonToDto(json);

//         } catch (WebClientResponseException e) {
//             log.error("OpenRouter API request failed: status={}, body={}", e.getRawStatusCode(), e.getResponseBodyAsString(), e);
//             return Collections.emptyList();
//         } catch (Exception e) {
//             log.error("AI parsing failed", e);
//             return Collections.emptyList();
//         }
//     }

//     // =========================
//     // 1️⃣ CALL OPENROUTER
//     // =========================
//     private String callOpenRouter(String imageUrl) throws JsonProcessingException {

//         String prompt = "You are a system that analyzes volleyball match schedules. "
//                 + "Extract all match data from the image. "
//                 + "RETURN ONLY a JSON ARRAY, NO markdown, NO explanation. "
//                 + "Schema: [{\"round\": \"GROUP|QUARTER_FINAL|SEMI_FINAL|FINAL|THIRD_PLACE|null\", "
//                 + "\"group\": \"A|B|C|null\", "
//                 + "\"matchDate\": \"yyyy-MM-ddTHH:mm\", "
//                 + "\"teamA\": \"string\", "
//                 + "\"teamB\": \"string\"}]";

//         // Build request body for Gemma free model
//         Map<String, Object> requestBody = Map.of(
//                 "model", "google/gemma-3-12b-it:free",
//                 "messages", List.of(
//                         Map.of(
//                                 "role", "user",
//                                 "content", List.of(
//                                         Map.of("type", "text", "text", prompt),
//                                         Map.of("type", "input_image_url", "input_image_url", Map.of("url", imageUrl))
//                                 )
//                         )
//                 )
//         );

//         String requestJson = objectMapper.writeValueAsString(requestBody);
//         log.info("OpenRouter request body: {}", requestJson);

//         // Call OpenRouter API
//         return webClient.post()
//                 .uri(apiUrl + "/responses")
//                 .header("Authorization", "Bearer " + apiKey)
//                 .header("Content-Type", "application/json; charset=UTF-8")
//                 .bodyValue(requestBody)
//                 .retrieve()
//                 .bodyToMono(String.class)
//                 .block();
//     }

//     // =========================
//     // 2️⃣ EXTRACT JSON ARRAY
//     // =========================
//     private String extractJsonArray(String raw) {
//         if (raw == null || raw.isEmpty()) return null;

//         int start = raw.indexOf('[');
//         int end = raw.lastIndexOf(']');
//         if (start == -1 || end == -1 || end <= start) return null;

//         return raw.substring(start, end + 1);
//     }

//     // =========================
//     // 3️⃣ PARSE JSON → DTO
//     // =========================
//     private List<MatchAiDto> parseJsonToDto(String json) {
//         if (json == null || json.isEmpty()) return Collections.emptyList();

//         try {
//             JsonNode root = objectMapper.readTree(json);
//             if (!root.isArray()) return Collections.emptyList();

//             return objectMapper.convertValue(
//                     root,
//                     objectMapper.getTypeFactory().constructCollectionType(List.class, MatchAiDto.class));
//         } catch (JsonProcessingException e) {
//             log.error("Failed to parse JSON to DTO", e);
//             return Collections.emptyList();
//         }
//     }
// }
