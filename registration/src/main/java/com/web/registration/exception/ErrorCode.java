package com.web.registration.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Training Center
    TRAINING_CENTER_NOT_FOUND("TRAINING_CENTER_NOT_FOUND", 404, "Cơ sở đào tạo không tồn tại"),
    TRAINING_CENTER_CODE_DUPLICATED("TRAINING_CENTER_CODE_DUPLICATED", 409, "Mã cơ sở đào tạo đã tồn tại"),

    // Auth
    USER_NOT_FOUND("USER_NOT_FOUND", 404, "Người dùng không tồn tại"),
    INVALID_CREDENTIALS("INVALID_CREDENTIALS", 401, "Tên đăng nhập hoặc mật khẩu không đúng"),
    USERNAME_DUPLICATED("USERNAME_DUPLICATED", 409, "Tên đăng nhập đã tồn tại"),
    EMAIL_DUPLICATED("EMAIL_DUPLICATED", 409, "Email đã tồn tại"),
    STUDENT_CODE_DUPLICATED("STUDENT_CODE_DUPLICATED", 409, "Mã sinh viên đã tồn tại"),
    LECTURER_CODE_DUPLICATED("LECTURER_CODE_DUPLICATED", 409, "Mã giảng viên đã tồn tại"),
    INVALID_REFRESH_TOKEN("INVALID_REFRESH_TOKEN", 401, "Refresh token không hợp lệ hoặc đã hết hạn"),
    ROLE_NOT_FOUND("ROLE_NOT_FOUND", 500, "Vai trò không tồn tại trong hệ thống"),

    // Common
    VALIDATION_FAILED("VALIDATION_FAILED", 400, "Dữ liệu đầu vào không hợp lệ"),
    UNAUTHORIZED("UNAUTHORIZED", 401, "Chưa xác thực"),
    FORBIDDEN("FORBIDDEN", 403, "Không có quyền thực hiện thao tác này");

    private final String code;
    private final int httpStatus;
    private final String message;
}
