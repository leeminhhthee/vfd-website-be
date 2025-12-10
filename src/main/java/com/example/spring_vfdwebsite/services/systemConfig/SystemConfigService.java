package com.example.spring_vfdwebsite.services.systemConfig;

import java.util.Optional;

import com.example.spring_vfdwebsite.entities.SystemConfig;

public interface SystemConfigService {

    String ACTIVITY_LOG_KEY = "activity_log.enabled";

    Optional<String> getConfigValueByKey(String key);

    boolean isActivityLogEnabledFallback(boolean fallback);

    SystemConfig setConfigValue(String key, String value);
}
