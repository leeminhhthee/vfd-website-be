package com.example.spring_vfdwebsite.services.systemConfig;

import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.spring_vfdwebsite.entities.SystemConfig;
import com.example.spring_vfdwebsite.repositories.SystemConfigJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SystemConfigServiceImpl implements SystemConfigService {

    private final SystemConfigJpaRepository systemConfigJpaRepository;

    @Override
    @Cacheable(value = "system-configs", key = "#key")
    public Optional<String> getConfigValueByKey(String key) {
        return systemConfigJpaRepository.findByKey(key).map(SystemConfig::getValue);
    }

    @Override
    public boolean isActivityLogEnabledFallback(boolean fallback) {
        Optional<String> configValueOpt = getConfigValueByKey(ACTIVITY_LOG_KEY);
        return configValueOpt.map(value -> Boolean.parseBoolean(value)).orElse(fallback);
    }

    @Override
    @CacheEvict(value = "system-configs", allEntries = true)
    public SystemConfig setConfigValue(String key, String value) {
        Optional<SystemConfig> configOpt = systemConfigJpaRepository.findByKey(key);
        SystemConfig config;
        if (configOpt.isPresent()) {
            config = configOpt.get();
            config.setValue(value);
        } else {
            config = SystemConfig.builder()
                    .key(key)
                    .value(value)
                    .build();
        }
        return systemConfigJpaRepository.save(config);
    }
    
}
