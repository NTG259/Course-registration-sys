package com.web.registration.service;

import com.web.registration.dto.request.*;
import com.web.registration.dto.response.AuthResponse;
import com.web.registration.dto.response.UserInfoResponse;

public interface AuthService {

    AuthResponse login(LoginRequest request);

    AuthResponse registerStudent(RegisterStudentRequest request);

    UserInfoResponse registerLecturer(RegisterLecturerRequest request);

    UserInfoResponse registerAdmin(RegisterAdminRequest request);

    AuthResponse refreshToken(RefreshTokenRequest request);

    void logout(RefreshTokenRequest request);
}
