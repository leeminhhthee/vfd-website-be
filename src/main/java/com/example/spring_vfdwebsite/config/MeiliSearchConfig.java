package com.example.spring_vfdwebsite.config;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MeiliSearchConfig {

    @Value("${spring.meilisearch.host}")
    private String host;

    @Value("${spring.meilisearch.api.key}")
    private String apiKey;

    @Bean
    public Client meiliSearchClient() {
        // Tạo kết nối đến MeiliSearch Server
        return new Client(new Config(host, apiKey));
    }
}