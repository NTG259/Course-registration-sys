package com.web.registration.controller;

import com.web.registration.dto.request.*;
import com.web.registration.dto.response.AuthResponse;
import com.web.registration.dto.response.UserInfoResponse;
import com.web.registration.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register/student")
    public ResponseEntity<AuthResponse> registerStudent(@Valid @RequestBody RegisterStudentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerStudent(request));
    }

    @PostMapping("/register/lecturer")
    @PreAuthorize("hasAnyRole('ADMIN', 'LOCAL_ADMIN')")
    public ResponseEntity<UserInfoResponse> registerLecturer(@Valid @RequestBody RegisterLecturerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerLecturer(request));
    }

    @PostMapping("/register/admin")
    @PreAuthorize("hasRole('LOCAL_ADMIN')")
    public ResponseEntity<UserInfoResponse> registerAdmin(@Valid @RequestBody RegisterAdminRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerAdmin(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshTokenRequest request) {
        authService.logout(request);
        return ResponseEntity.noContent().build();
    }
}
