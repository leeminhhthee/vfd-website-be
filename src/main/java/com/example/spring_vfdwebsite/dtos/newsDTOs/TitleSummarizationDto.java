package com.example.spring_vfdwebsite.dtos.newsDTOs;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

public class TitleSummarizationDto {

    // 1. Cấu trúc Request gửi đi
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private List<Content> contents;

        public Request(String text) {
            this.contents = List.of(new Content(text));
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Content {
        private List<Part> parts;

        // Constructor cho Request
        public Content(String text) {
            this.parts = List.of(new Part(text));
        }

        // Constructor mặc định cho Jackson mapping
        public Content() {
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Part {
        private String text;

        public Part(String text) {
            this.text = text;
        }

        public Part() {
        }
    }

    // 2. Cấu trúc Response nhận về
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response {
        private List<Candidate> candidates;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Candidate {
        private Content content;
    }

}
