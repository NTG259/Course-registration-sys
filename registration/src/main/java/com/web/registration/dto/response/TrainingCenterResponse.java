package com.web.registration.dto.response;

import java.time.LocalDateTime;

public record TrainingCenterResponse(
        Long id,
        String code,
        String name,
        String address,
        String phone,
        String email,
        LocalDateTime createdAt
) {}
