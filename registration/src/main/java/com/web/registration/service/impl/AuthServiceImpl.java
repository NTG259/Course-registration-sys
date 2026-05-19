package com.web.registration.service.impl;

import com.web.registration.config.JwtProperties;
import com.web.registration.dto.request.*;
import com.web.registration.dto.response.AuthResponse;
import com.web.registration.dto.response.UserInfoResponse;
import com.web.registration.entity.*;
import com.web.registration.exception.BusinessException;
import com.web.registration.exception.ErrorCode;
import com.web.registration.repository.*;
import com.web.registration.security.JwtTokenProvider;
import com.web.registration.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final StudentRepository studentRepository;
    private final LecturerRepository lecturerRepository;
    private final AdminRepository adminRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TrainingCenterRepository trainingCenterRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final JwtProperties jwtProperties;

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        return buildAuthResponse(user);
    }

    @Override
    @Transactional
    public AuthResponse registerStudent(RegisterStudentRequest request) {
        validateUserUniqueness(request.getUsername(), request.getEmail());
        if (studentRepository.existsByStudentCode(request.getStudentCode())) {
            throw new BusinessException(ErrorCode.STUDENT_CODE_DUPLICATED);
        }

        TrainingCenter trainingCenter = findTrainingCenter(request.getTrainingCenterId());
        Role role = findRole("STUDENT");

        User user = userRepository.save(User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .gender(request.getGender())
                .dateOfBirth(request.getDateOfBirth())
                .role(role)
                .build());

        studentRepository.save(Student.builder()
                .user(user)
                .studentCode(request.getStudentCode())
                .major(request.getMajor())
                .className(request.getClassName())
                .trainingCenter(trainingCenter)
                .build());

        return buildAuthResponse(user);
    }

    @Override
    @Transactional
    public UserInfoResponse registerLecturer(RegisterLecturerRequest request) {
        validateUserUniqueness(request.getUsername(), request.getEmail());
        if (lecturerRepository.existsByLecturerCode(request.getLecturerCode())) {
            throw new BusinessException(ErrorCode.LECTURER_CODE_DUPLICATED);
        }

        TrainingCenter trainingCenter = findTrainingCenter(request.getTrainingCenterId());
        Role role = findRole("LECTURER");

        User user = userRepository.save(User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .gender(request.getGender())
                .dateOfBirth(request.getDateOfBirth())
                .role(role)
                .build());

        lecturerRepository.save(Lecturer.builder()
                .user(user)
                .lecturerCode(request.getLecturerCode())
                .department(request.getDepartment())
                .academicRank(request.getAcademicRank())
                .trainingCenter(trainingCenter)
                .build());

        return toUserInfoResponse(user);
    }

    @Override
    @Transactional
    public UserInfoResponse registerAdmin(RegisterAdminRequest request) {
        validateUserUniqueness(request.getUsername(), request.getEmail());

        TrainingCenter trainingCenter = findTrainingCenter(request.getTrainingCenterId());
        Role role = findRole("ADMIN");

        User user = userRepository.save(User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .gender(request.getGender())
                .dateOfBirth(request.getDateOfBirth())
                .role(role)
                .build());

        adminRepository.save(Admin.builder()
                .user(user)
                .trainingCenter(trainingCenter)
                .build());

        return toUserInfoResponse(user);
    }

    @Override
    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN));

        if (refreshToken.getRevoked() || refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);

        User user = refreshToken.getUser();
        return buildAuthResponse(user);
    }

    @Override
    @Transactional
    public void logout(RefreshTokenRequest request) {
        refreshTokenRepository.findByToken(request.getRefreshToken()).ifPresent(token -> {
            token.setRevoked(true);
            refreshTokenRepository.save(token);
        });
    }

    private AuthResponse buildAuthResponse(User user) {
        String roleName = user.getRole().getName();
        String accessToken = jwtTokenProvider.generateAccessToken(user.getUsername(), roleName);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken();

        refreshTokenRepository.save(RefreshToken.builder()
                .token(newRefreshToken)
                .user(user)
                .expiresAt(LocalDateTime.now().plusSeconds(jwtProperties.getRefreshTokenExpirationMs() / 1000))
                .build());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .expiresIn(jwtProperties.getAccessTokenExpirationMs() / 1000)
                .userInfo(toUserInfoResponse(user))
                .build();
    }

    private void validateUserUniqueness(String username, String email) {
        if (userRepository.existsByUsername(username)) {
            throw new BusinessException(ErrorCode.USERNAME_DUPLICATED);
        }
        if (userRepository.existsByEmail(email)) {
            throw new BusinessException(ErrorCode.EMAIL_DUPLICATED);
        }
    }

    private TrainingCenter findTrainingCenter(Long id) {
        return trainingCenterRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.TRAINING_CENTER_NOT_FOUND));
    }

    private Role findRole(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new BusinessException(ErrorCode.ROLE_NOT_FOUND));
    }

    private UserInfoResponse toUserInfoResponse(User user) {
        return UserInfoResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole().getName())
                .build();
    }
}
