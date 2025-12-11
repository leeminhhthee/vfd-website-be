package com.example.spring_vfdwebsite.services.activityLog;

import java.util.List;

import com.example.spring_vfdwebsite.dtos.activityLogDTOs.ActivityLogCreateRequestDto;
import com.example.spring_vfdwebsite.dtos.activityLogDTOs.ActivityLogResponseDto;
import com.example.spring_vfdwebsite.dtos.activityLogDTOs.PaginatedAcivityLogResponseDto;
import com.example.spring_vfdwebsite.entities.User;

public interface ActivityLogService {
    List<ActivityLogResponseDto> getAllActivityLogs();

    ActivityLogResponseDto createActivityLogResponseDto(User user, ActivityLogCreateRequestDto dto);

    void deleteActivityLog(List<Integer> ids);

    List<String> getActionTypes();

    // Paginated Activity Logs
    PaginatedAcivityLogResponseDto getPaginatedActivityLogs(int pageNumber, int pageSize);

    // 10 Latest Activity Logs
    List<ActivityLogResponseDto> getLatestActivityLogs();
}
