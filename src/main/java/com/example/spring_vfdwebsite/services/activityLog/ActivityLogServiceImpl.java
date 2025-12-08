package com.example.spring_vfdwebsite.services.activityLog;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.spring_vfdwebsite.dtos.activityLogDTOs.ActivityLogCreateRequestDto;
import com.example.spring_vfdwebsite.dtos.activityLogDTOs.ActivityLogResponseDto;
import com.example.spring_vfdwebsite.dtos.activityLogDTOs.PaginatedAcivityLogResponseDto;
import com.example.spring_vfdwebsite.entities.ActivityLog;
import com.example.spring_vfdwebsite.entities.User;
import com.example.spring_vfdwebsite.exceptions.EntityNotFoundException;
import com.example.spring_vfdwebsite.repositories.ActivityLogJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActivityLogServiceImpl implements ActivityLogService {
    private final ActivityLogJpaRepository activityLogRepository;

    // Lấy tất cả activity logs kèm thông tin user
    @Override
    @Cacheable(value = "activity-logs", key = "'all'")
    @Transactional(readOnly = true)
    public List<ActivityLogResponseDto> getAllActivityLogs() {
        List<ActivityLog> activityLogs = activityLogRepository.findAllWithUser();
        return activityLogs.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // Paginated Activity Logs
    @Override
    // @Cacheable(value = "activity-logs", key = "'paginated-' + #pageNumber + '-' +
    // #pageSize")
    @Transactional(readOnly = true)
    public PaginatedAcivityLogResponseDto getPaginatedActivityLogs(int pageNumber, int pageSize) {

        Sort sort = Sort.by("createdAt").descending();
        // Tạo Pageable object với page và size
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        // Lấy dữ liệu phân trang từ repository
        Page<ActivityLog> activityLogPage = this.activityLogRepository.findAll(pageable);

        // Chuyển đổi Page<Student> thành List<StudentResponseDto>
        List<ActivityLogResponseDto> activityLogDtos = activityLogPage.getContent().stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        // Tạo response DTO với thông tin phân trang
        return PaginatedAcivityLogResponseDto.builder()
                .data(activityLogDtos)
                .pageNumber(activityLogPage.getNumber())
                .pageSize(activityLogPage.getSize())
                .totalRecords(activityLogPage.getTotalElements())
                .totalPages(activityLogPage.getTotalPages())
                .hasNext(activityLogPage.hasNext())
                .hasPrevious(activityLogPage.hasPrevious())
                .build();
    }

    // Tạo log mới từ
    @Override
    @Transactional
    @CacheEvict(value = "activity-logs", allEntries = true)
    public ActivityLogResponseDto createActivityLogResponseDto(User user, ActivityLogCreateRequestDto dto) {
        if (user == null) {
            throw new IllegalArgumentException("User must not be null");
        }

        ActivityLog activityLog = ActivityLog.builder()
                .actionType(dto.getActionType())
                .targetTable(dto.getTargetTable())
                .targetId(dto.getTargetId())
                .description(dto.getDescription())
                .user(user)
                .build();

        activityLog = activityLogRepository.save(activityLog);

        return toDto(activityLog);
    }

    // Xóa log theo id
    @Override
    @CacheEvict(value = "activity-logs", allEntries = true)
    public void deleteActivityLog(List<Integer> ids) {
        // for (Integer id : ids) {
        // if (!activityLogRepository.existsById(id)) {
        // throw new EntityNotFoundException("ActivityLog not found with id: " + id);
        // }
        // activityLogRepository.deleteById(id);
        // }
        List<Integer> existingIds = activityLogRepository.findAllById(ids)
                .stream()
                .map(ActivityLog::getId)
                .toList();

        if (existingIds.size() != ids.size()) {
            throw new EntityNotFoundException("Some IDs do not exist");
        }

        activityLogRepository.deleteAllByIdInBatch(existingIds);
    }

    public List<String> getActionTypes() {
        return List.of("CREATE", "READ", "UPDATE", "DELETE", "LOGIN", "LOGOUT", "REFRESH_TOKEN", "REGISTER_INIT",
                "REGISTER_COMPLETE", "SEND_CHANGE_PASSWORD_OTP", "CHANGE_PASSWORD_WITH_OTP", "SEND_FORGOT_PASSWORD_OTP",
                "RESEND_OTP", "PASSWORD_RESET_INIT", "PASSWORD_RESET_COMPLETE", "EXTRACT_MATCHES", "SAVE_DRAFTS",
                "APPROVE_DRAFTS", "CHANGE_STATUS");
    }

    // Mapper entity sang DTO
    private ActivityLogResponseDto toDto(ActivityLog activityLog) {
        ActivityLogResponseDto.UserDto userDto = null;
        if (activityLog.getUser() != null) {
            userDto = ActivityLogResponseDto.UserDto.builder()
                    .id(activityLog.getUser().getId())
                    .fullName(activityLog.getUser().getFullName())
                    .email(activityLog.getUser().getEmail())
                    .build();
        } else {
            userDto = ActivityLogResponseDto.UserDto.builder()
                    .id(null)
                    .fullName("N/A")
                    .email("null")
                    .build();
        }

        return ActivityLogResponseDto.builder()
                .id(activityLog.getId())
                .actionType(activityLog.getActionType())
                .targetTable(activityLog.getTargetTable())
                .targetId(activityLog.getTargetId())
                .description(activityLog.getDescription())
                .createdAt(activityLog.getCreatedAt())
                .user(userDto)
                .build();
    }
}
