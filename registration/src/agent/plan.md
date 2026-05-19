## Chức năng: Xác thực & Phân quyền (Authentication & Authorization)

### Mục tiêu
Cho phép người dùng đăng nhập bằng JWT và đăng ký tài khoản theo từng vai trò:
- **LOCAL_ADMIN** — Quản trị viên tối cao toàn hệ thống (seed data, không đăng ký qua API)
- **ADMIN** — Quản trị viên một cơ sở đào tạo (được LOCAL_ADMIN tạo)
- **LECTURER** — Giảng viên (được ADMIN / LOCAL_ADMIN tạo)
- **STUDENT** — Sinh viên (tự đăng ký hoặc được ADMIN tạo)

---

### Schema DB bổ sung

```sql
-- Profile của ADMIN (tương tự students / lecturers)
CREATE TABLE admins (
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    user_id BIGINT UNIQUE NOT NULL,
    training_center_id BIGINT NOT NULL,
    CONSTRAINT FK_admins_users
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT FK_admins_training_centers
        FOREIGN KEY (training_center_id) REFERENCES training_centers(id)
);

-- Lưu refresh token để hỗ trợ logout & rotation
CREATE TABLE refresh_tokens (
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    token VARCHAR(512) UNIQUE NOT NULL,
    user_id BIGINT NOT NULL,
    expires_at DATETIME NOT NULL,
    revoked BIT DEFAULT 0,
    CONSTRAINT FK_refresh_tokens_users
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
```

> Thêm 2 đoạn SQL trên vào `src/agent/database/db.md`.

---

### Các bước thực hiện

#### Bước 1 — Entity bổ sung
- [ ] Tạo `Admin` entity (ánh xạ table `admins` — `user`, `trainingCenter`)
- [ ] Tạo `RefreshToken` entity (ánh xạ table `refresh_tokens` — `token`, `user`, `expiresAt`, `revoked`)

#### Bước 2 — Repository
- [ ] `UserRepository` — `findByUsername(String)`, `findByEmail(String)`, `existsByUsername(String)`, `existsByEmail(String)`
- [ ] `RoleRepository` — `findByName(String)`
- [ ] `AdminRepository` — `findByUser(User)`, `existsByUser(User)`
- [ ] `RefreshTokenRepository` — `findByToken(String)`, `deleteByUser(User)`

#### Bước 3 — DTO

**Request:**
- [ ] `LoginRequest` — `username` (NotBlank), `password` (NotBlank)
- [ ] `RegisterStudentRequest` — `username`, `email`, `password`, `fullName`, `gender`, `dateOfBirth`, `studentCode`, `major`, `className`, `trainingCenterId`
- [ ] `RegisterLecturerRequest` — `username`, `email`, `password`, `fullName`, `gender`, `dateOfBirth`, `lecturerCode`, `department`, `academicRank`, `trainingCenterId`
- [ ] `RegisterAdminRequest` — `username`, `email`, `password`, `fullName`, `gender`, `dateOfBirth`, `trainingCenterId`
- [ ] `RefreshTokenRequest` — `refreshToken` (NotBlank)

**Response:**
- [ ] `AuthResponse` — `accessToken`, `refreshToken`, `tokenType = "Bearer"`, `expiresIn` (giây), `userInfo`
- [ ] `UserInfoResponse` — `id`, `username`, `email`, `fullName`, `role`

#### Bước 4 — JWT Infrastructure

- [ ] `JwtProperties` (`@ConfigurationProperties(prefix = "jwt")`):
  - `secret` — HMAC-SHA256, min 256-bit, base64-encoded
  - `accessTokenExpirationMs` — ví dụ `900000` (15 phút)
  - `refreshTokenExpirationMs` — ví dụ `604800000` (7 ngày)

- [ ] `JwtTokenProvider`:
  - `generateAccessToken(String username, String role)` → JWT signed bằng HMAC-SHA256
    - Claims: `sub = username`, `roles = [role]`, `iat`, `exp`
  - `generateRefreshToken()` → UUID string (lưu vào DB, không phải JWT)
  - `validateToken(String token)` → boolean
  - `extractUsername(String token)` → String

  > Dùng `com.nimbusds.jose` + `com.nimbusds.jwt` (đã có trên classpath qua `spring-boot-starter-security-oauth2-resource-server`).

- [ ] Cấu hình Resource Server dùng cùng secret để validate:
  ```properties
  spring.security.oauth2.resourceserver.jwt.secret-key=<base64-secret>
  ```

#### Bước 5 — Security Infrastructure

- [ ] `CustomUserDetailsService implements UserDetailsService`:
  - `loadUserByUsername(String username)` — tìm User, map sang `UserDetails` (username, passwordHash, `ROLE_<roleName>`)

- [ ] Cập nhật `SecurityConfig`:
  - Thêm `PasswordEncoder` bean (BCrypt)
  - Thêm `AuthenticationManager` bean
  - Permit public: `POST /api/auth/login`, `POST /api/auth/register/student`, `POST /api/auth/refresh`
  - Giữ `oauth2ResourceServer` với JWT converter `roles` claim → `ROLE_` prefix (đã có)

#### Bước 6 — ErrorCode bổ sung

Thêm vào enum `ErrorCode`:

| code (String)               | httpStatus | message                                  |
|-----------------------------|------------|------------------------------------------|
| `USER_NOT_FOUND`            | 404        | Người dùng không tồn tại                |
| `INVALID_CREDENTIALS`       | 401        | Tên đăng nhập hoặc mật khẩu không đúng  |
| `USERNAME_DUPLICATED`       | 409        | Tên đăng nhập đã tồn tại                |
| `EMAIL_DUPLICATED`          | 409        | Email đã tồn tại                         |
| `STUDENT_CODE_DUPLICATED`   | 409        | Mã sinh viên đã tồn tại                 |
| `LECTURER_CODE_DUPLICATED`  | 409        | Mã giảng viên đã tồn tại                |
| `INVALID_REFRESH_TOKEN`     | 401        | Refresh token không hợp lệ hoặc hết hạn |
| `ROLE_NOT_FOUND`            | 500        | Vai trò không tồn tại trong hệ thống    |

#### Bước 7 — Service

- [ ] Tạo interface `AuthService`
- [ ] Implement `AuthServiceImpl`:

  **`login(LoginRequest)`** → `AuthResponse`
  1. Xác thực qua `AuthenticationManager.authenticate()`
  2. Load User từ DB, lấy role name
  3. Sinh `accessToken` (JWT) và `refreshToken` (UUID)
  4. Lưu `RefreshToken` vào DB với `expiresAt`
  5. Trả về `AuthResponse`

  **`registerStudent(RegisterStudentRequest)`** → `AuthResponse`
  1. Kiểm tra `username`, `email`, `studentCode` chưa tồn tại
  2. Tạo `User` với role `STUDENT`, mã hóa password BCrypt
  3. Tạo `Student` profile (liên kết `trainingCenter`)
  4. Sinh token, trả về `AuthResponse`

  **`registerLecturer(RegisterLecturerRequest)`** → `UserInfoResponse`
  1. Kiểm tra `username`, `email`, `lecturerCode` chưa tồn tại
  2. Tạo `User` với role `LECTURER`
  3. Tạo `Lecturer` profile

  **`registerAdmin(RegisterAdminRequest)`** → `UserInfoResponse`
  1. Kiểm tra `username`, `email` chưa tồn tại
  2. Tạo `User` với role `ADMIN`
  3. Tạo `Admin` profile (liên kết `trainingCenter`)

  **`refreshToken(RefreshTokenRequest)`** → `AuthResponse`
  1. Tìm `RefreshToken` trong DB — kiểm tra `revoked = false` và `expiresAt` chưa qua
  2. Sinh `accessToken` mới
  3. Rotate: thu hồi token cũ (`revoked = true`), tạo và lưu token mới
  4. Trả về `AuthResponse`

  **`logout(String refreshToken)`** → void
  1. Tìm và đánh dấu `revoked = true`

#### Bước 8 — Controller

- [ ] `AuthController` (`@RestController`, `@RequestMapping("/api/auth")`):

  | Method | Endpoint              | Quyền                        |
  |--------|-----------------------|------------------------------|
  | POST   | `/login`              | Public                       |
  | POST   | `/register/student`   | Public                       |
  | POST   | `/register/lecturer`  | `ROLE_ADMIN`, `ROLE_LOCAL_ADMIN` |
  | POST   | `/register/admin`     | `ROLE_LOCAL_ADMIN`           |
  | POST   | `/refresh`            | Public                       |
  | POST   | `/logout`             | Đã đăng nhập                 |

  - Dùng `@PreAuthorize("hasAnyRole('ADMIN','LOCAL_ADMIN')")` cho `/register/lecturer`
  - Dùng `@PreAuthorize("hasRole('LOCAL_ADMIN')")` cho `/register/admin`
  - Lấy `refreshToken` từ current JWT principal cho `/logout`

#### Bước 9 — Data Initialization

- [ ] `DataInitializer implements ApplicationRunner`:
  1. Upsert 4 roles: `LOCAL_ADMIN`, `ADMIN`, `LECTURER`, `STUDENT`
  2. Tạo tài khoản `LOCAL_ADMIN` mặc định nếu chưa có:
     - `username`: lấy từ `app.default-admin.username` (application.properties)
     - `password`: lấy từ `app.default-admin.password` (application.properties), mã hóa BCrypt

#### Bước 10 — application.properties bổ sung

```properties
# JWT
jwt.secret=<base64-encoded-256-bit-key>
jwt.access-token-expiration-ms=900000
jwt.refresh-token-expiration-ms=604800000

# Resource Server dùng cùng secret
spring.security.oauth2.resourceserver.jwt.secret-key=${jwt.secret}

# Default LOCAL_ADMIN
app.default-admin.username=localadmin
app.default-admin.password=Admin@123456
```

#### Bước 11 — Test
- [ ] Unit test `AuthServiceImpl` (mock repo, mock `JwtTokenProvider`, mock `AuthenticationManager`)
- [ ] Integration test `AuthController` — login thành công, sai mật khẩu, register, refresh, logout

---

### Ma trận phân quyền

| Endpoint                          | Public | STUDENT | LECTURER | ADMIN | LOCAL_ADMIN |
|-----------------------------------|:------:|:-------:|:--------:|:-----:|:-----------:|
| POST /api/auth/login              |   ✅   |         |          |       |             |
| POST /api/auth/register/student   |   ✅   |         |          |       |             |
| POST /api/auth/register/lecturer  |        |         |          |  ✅   |     ✅      |
| POST /api/auth/register/admin     |        |         |          |       |     ✅      |
| POST /api/auth/refresh            |   ✅   |         |          |       |             |
| POST /api/auth/logout             |        |   ✅    |    ✅    |  ✅   |     ✅      |
| GET  /api/training-centers        |   ✅   |         |          |       |             |
| POST /api/training-centers        |        |         |          |       |     ✅      |
| PUT  /api/training-centers/{id}   |        |         |          |       |     ✅      |
| DELETE /api/training-centers/{id} |        |         |          |       |     ✅      |

---

### Cấu trúc file dự kiến

```
src/main/java/com/web/registration/
├── entity/
│   ├── Admin.java                          ← MỚI
│   └── RefreshToken.java                   ← MỚI
├── repository/
│   ├── UserRepository.java                 ← MỚI
│   ├── RoleRepository.java                 ← MỚI
│   ├── AdminRepository.java                ← MỚI
│   └── RefreshTokenRepository.java         ← MỚI
├── dto/
│   ├── request/
│   │   ├── LoginRequest.java               ← MỚI
│   │   ├── RegisterStudentRequest.java     ← MỚI
│   │   ├── RegisterLecturerRequest.java    ← MỚI
│   │   ├── RegisterAdminRequest.java       ← MỚI
│   │   └── RefreshTokenRequest.java        ← MỚI
│   └── response/
│       ├── AuthResponse.java               ← MỚI
│       └── UserInfoResponse.java           ← MỚI
├── config/
│   ├── SecurityConfig.java                 ← CẬP NHẬT
│   ├── JwtProperties.java                  ← MỚI
│   └── DataInitializer.java                ← MỚI
├── security/
│   ├── JwtTokenProvider.java               ← MỚI
│   └── CustomUserDetailsService.java       ← MỚI
├── service/
│   ├── AuthService.java                    ← MỚI
│   └── impl/
│       └── AuthServiceImpl.java            ← MỚI
├── controller/
│   └── AuthController.java                 ← MỚI
└── exception/
    └── ErrorCode.java                      ← CẬP NHẬT (thêm 8 mã lỗi mới)
```
