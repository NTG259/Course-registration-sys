# API Documentation — Xác thực & Phân quyền (Auth)

**Base URL:** `/api/auth`  
**Content-Type:** `application/json`

---

## Response chuẩn khi lỗi

```json
{
  "code": "ERROR_CODE_STRING",
  "httpStatus": 4xx,
  "message": "Mô tả lỗi"
}
```

---

## 1. Đăng nhập

**`POST /api/auth/login`** — Public

**Request body:**
```json
{
  "username": "johndoe",
  "password": "secret123"
}
```

**Response `200 OK`:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "550e8400-e29b-41d4-a716-446655440000",
  "tokenType": "Bearer",
  "expiresIn": 900,
  "userInfo": {
    "id": 1,
    "username": "johndoe",
    "email": "john@example.com",
    "fullName": "John Doe",
    "role": "STUDENT"
  }
}
```

**Lỗi có thể xảy ra:**

| Code | HTTP | Mô tả |
|------|------|-------|
| `INVALID_CREDENTIALS` | 401 | Sai tên đăng nhập hoặc mật khẩu |
| `VALIDATION_FAILED` | 400 | username/password để trống |

---

## 2. Đăng ký sinh viên

**`POST /api/auth/register/student`** — Public

**Request body:**
```json
{
  "username": "sv001",
  "email": "sv001@ptit.edu.vn",
  "password": "Abc123456",
  "fullName": "Nguyễn Văn A",
  "gender": "MALE",
  "dateOfBirth": "2003-05-15",
  "studentCode": "B21DCCN001",
  "major": "Công nghệ thông tin",
  "className": "D21CQCN01-B",
  "trainingCenterId": 1
}
```

> `gender`: `MALE` | `FEMALE` | `OTHER` (tùy chọn)  
> `dateOfBirth`: định dạng `YYYY-MM-DD` (tùy chọn)  
> `major`, `className`: tùy chọn

**Response `201 Created`:** — cùng format `AuthResponse` như Login

**Lỗi có thể xảy ra:**

| Code | HTTP | Mô tả |
|------|------|-------|
| `USERNAME_DUPLICATED` | 409 | Tên đăng nhập đã tồn tại |
| `EMAIL_DUPLICATED` | 409 | Email đã tồn tại |
| `STUDENT_CODE_DUPLICATED` | 409 | Mã sinh viên đã tồn tại |
| `TRAINING_CENTER_NOT_FOUND` | 404 | Cơ sở đào tạo không tồn tại |
| `VALIDATION_FAILED` | 400 | Dữ liệu đầu vào không hợp lệ |

---

## 3. Tạo tài khoản giảng viên

**`POST /api/auth/register/lecturer`** — Yêu cầu role: `ADMIN`, `LOCAL_ADMIN`

**Header:**
```
Authorization: Bearer <accessToken>
```

**Request body:**
```json
{
  "username": "gv001",
  "email": "gv001@ptit.edu.vn",
  "password": "Abc123456",
  "fullName": "Trần Thị B",
  "gender": "FEMALE",
  "dateOfBirth": "1985-03-20",
  "lecturerCode": "GV001",
  "department": "Khoa CNTT",
  "academicRank": "Tiến sĩ",
  "trainingCenterId": 1
}
```

> `gender`, `dateOfBirth`, `department`, `academicRank`: tùy chọn

**Response `201 Created`:**
```json
{
  "id": 5,
  "username": "gv001",
  "email": "gv001@ptit.edu.vn",
  "fullName": "Trần Thị B",
  "role": "LECTURER"
}
```

**Lỗi có thể xảy ra:**

| Code | HTTP | Mô tả |
|------|------|-------|
| `UNAUTHORIZED` | 401 | Chưa đăng nhập |
| `FORBIDDEN` | 403 | Không có quyền (không phải ADMIN/LOCAL_ADMIN) |
| `USERNAME_DUPLICATED` | 409 | Tên đăng nhập đã tồn tại |
| `EMAIL_DUPLICATED` | 409 | Email đã tồn tại |
| `LECTURER_CODE_DUPLICATED` | 409 | Mã giảng viên đã tồn tại |
| `TRAINING_CENTER_NOT_FOUND` | 404 | Cơ sở đào tạo không tồn tại |
| `VALIDATION_FAILED` | 400 | Dữ liệu đầu vào không hợp lệ |

---

## 4. Tạo tài khoản Admin

**`POST /api/auth/register/admin`** — Yêu cầu role: `LOCAL_ADMIN`

**Header:**
```
Authorization: Bearer <accessToken>
```

**Request body:**
```json
{
  "username": "admin_cs1",
  "email": "admin.cs1@ptit.edu.vn",
  "password": "Abc123456",
  "fullName": "Lê Văn C",
  "gender": "MALE",
  "dateOfBirth": "1990-01-10",
  "trainingCenterId": 1
}
```

> `gender`, `dateOfBirth`: tùy chọn

**Response `201 Created`:** — cùng format `UserInfoResponse` với `role: "ADMIN"`

**Lỗi có thể xảy ra:**

| Code | HTTP | Mô tả |
|------|------|-------|
| `UNAUTHORIZED` | 401 | Chưa đăng nhập |
| `FORBIDDEN` | 403 | Không có quyền (không phải LOCAL_ADMIN) |
| `USERNAME_DUPLICATED` | 409 | Tên đăng nhập đã tồn tại |
| `EMAIL_DUPLICATED` | 409 | Email đã tồn tại |
| `TRAINING_CENTER_NOT_FOUND` | 404 | Cơ sở đào tạo không tồn tại |
| `VALIDATION_FAILED` | 400 | Dữ liệu đầu vào không hợp lệ |

---

## 5. Làm mới token

**`POST /api/auth/refresh`** — Public

**Request body:**
```json
{
  "refreshToken": "550e8400-e29b-41d4-a716-446655440000"
}
```

**Response `200 OK`:** — cùng format `AuthResponse` (refresh token mới + access token mới)

> Refresh token cũ bị thu hồi (rotation), không dùng lại được.

**Lỗi có thể xảy ra:**

| Code | HTTP | Mô tả |
|------|------|-------|
| `INVALID_REFRESH_TOKEN` | 401 | Token không tồn tại, đã thu hồi hoặc hết hạn |
| `VALIDATION_FAILED` | 400 | refreshToken để trống |

---

## 6. Đăng xuất

**`POST /api/auth/logout`** — Yêu cầu đã đăng nhập

**Header:**
```
Authorization: Bearer <accessToken>
```

**Request body:**
```json
{
  "refreshToken": "550e8400-e29b-41d4-a716-446655440000"
}
```

**Response `204 No Content`**

> Refresh token được đánh dấu `revoked`, không dùng lại được.  
> Nếu token không tồn tại, vẫn trả `204` (idempotent).

---

## Sử dụng Access Token

Thêm header vào mọi request cần xác thực:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

- **Access token** có hiệu lực **15 phút** (`expiresIn: 900`)
- **Refresh token** có hiệu lực **7 ngày**
- Khi access token hết hạn, gọi `POST /api/auth/refresh` để lấy cặp token mới

---

## Ma trận phân quyền

| Endpoint | PUBLIC | STUDENT | LECTURER | ADMIN | LOCAL_ADMIN |
|----------|:------:|:-------:|:--------:|:-----:|:-----------:|
| POST /api/auth/login | ✅ | | | | |
| POST /api/auth/register/student | ✅ | | | | |
| POST /api/auth/register/lecturer | | | | ✅ | ✅ |
| POST /api/auth/register/admin | | | | | ✅ |
| POST /api/auth/refresh | ✅ | | | | |
| POST /api/auth/logout | | ✅ | ✅ | ✅ | ✅ |

---

## Tài khoản mặc định (seed)

Tài khoản `LOCAL_ADMIN` được tạo tự động khi ứng dụng khởi động lần đầu:

| Field | Giá trị |
|-------|---------|
| username | `localadmin` |
| password | `Admin@123456` |
| role | `LOCAL_ADMIN` |

> Đổi mật khẩu ngay sau khi deploy production.
