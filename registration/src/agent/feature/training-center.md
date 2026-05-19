# API: Quản lý Cơ sở đào tạo

Base URL: `/api/training-centers`

> Tất cả request phải có header `Authorization: Bearer <JWT token>`.
> Các method `POST`, `PUT`, `DELETE` yêu cầu token có role `ADMIN`.
> JWT phải chứa claim `roles` dạng mảng, ví dụ: `"roles": ["ADMIN"]`.

---

## 1. Lấy danh sách tất cả cơ sở đào tạo

**GET** `/api/training-centers`

| | |
|---|---|
| Quyền | Đã đăng nhập |
| Request body | Không có |

**Response `200 OK`**
```json
[
  {
    "id": 1,
    "code": "PTIT-HN",
    "name": "Học viện Công nghệ Bưu chính Viễn thông - Hà Nội",
    "address": "122 Hoàng Quốc Việt, Hà Nội",
    "phone": "024.37545451",
    "email": "ptit@ptit.edu.vn",
    "createdAt": "2024-01-01T08:00:00"
  }
]
```

---

## 2. Lấy chi tiết cơ sở đào tạo theo ID

**GET** `/api/training-centers/{id}`

| | |
|---|---|
| Quyền | Đã đăng nhập |
| Path param | `id` (Long) — ID cơ sở đào tạo |

**Response `200 OK`**
```json
{
  "id": 1,
  "code": "PTIT-HN",
  "name": "Học viện Công nghệ Bưu chính Viễn thông - Hà Nội",
  "address": "122 Hoàng Quốc Việt, Hà Nội",
  "phone": "024.37545451",
  "email": "ptit@ptit.edu.vn",
  "createdAt": "2024-01-01T08:00:00"
}
```

**Response `404 Not Found`**
```json
{
  "code": "TRAINING_CENTER_NOT_FOUND",
  "httpStatus": 404,
  "message": "Cơ sở đào tạo không tồn tại"
}
```

---

## 3. Tạo mới cơ sở đào tạo

**POST** `/api/training-centers`

| | |
|---|---|
| Quyền | `ADMIN` |
| Content-Type | `application/json` |

**Request body**
```json
{
  "code": "PTIT-HCM",
  "name": "Học viện Công nghệ Bưu chính Viễn thông - TP.HCM",
  "address": "11 Nguyễn Đình Chiểu, TP.HCM",
  "phone": "028.38291742",
  "email": "ptithcm@ptit.edu.vn"
}
```

| Trường | Kiểu | Bắt buộc | Ràng buộc |
|--------|------|----------|-----------|
| `code` | String | Có | Không rỗng, tối đa 20 ký tự, unique |
| `name` | String | Có | Không rỗng, tối đa 255 ký tự |
| `address` | String | Không | Tối đa 255 ký tự |
| `phone` | String | Không | Tối đa 20 ký tự |
| `email` | String | Không | Định dạng email hợp lệ, tối đa 100 ký tự |

**Response `201 Created`**
```json
{
  "id": 2,
  "code": "PTIT-HCM",
  "name": "Học viện Công nghệ Bưu chính Viễn thông - TP.HCM",
  "address": "11 Nguyễn Đình Chiểu, TP.HCM",
  "phone": "028.38291742",
  "email": "ptithcm@ptit.edu.vn",
  "createdAt": "2024-06-01T09:00:00"
}
```

**Response `409 Conflict`** — Mã code đã tồn tại
```json
{
  "code": "TRAINING_CENTER_CODE_DUPLICATED",
  "httpStatus": 409,
  "message": "Mã cơ sở đào tạo đã tồn tại"
}
```

**Response `400 Bad Request`** — Dữ liệu không hợp lệ
```json
{
  "code": "VALIDATION_FAILED",
  "httpStatus": 400,
  "message": "Dữ liệu đầu vào không hợp lệ"
}
```

---

## 4. Cập nhật cơ sở đào tạo

**PUT** `/api/training-centers/{id}`

| | |
|---|---|
| Quyền | `ADMIN` |
| Path param | `id` (Long) — ID cơ sở đào tạo |
| Content-Type | `application/json` |

**Request body** — cấu trúc giống POST

**Response `200 OK`** — trả về object đã cập nhật (cấu trúc giống POST response)

**Response `404 Not Found`** — ID không tồn tại

**Response `409 Conflict`** — Mã code trùng với bản ghi khác

---

## 5. Xóa cơ sở đào tạo

**DELETE** `/api/training-centers/{id}`

| | |
|---|---|
| Quyền | `ADMIN` |
| Path param | `id` (Long) — ID cơ sở đào tạo |

**Response `204 No Content`** — Xóa thành công, không có body

**Response `404 Not Found`**
```json
{
  "code": "TRAINING_CENTER_NOT_FOUND",
  "httpStatus": 404,
  "message": "Cơ sở đào tạo không tồn tại"
}
```

---

## Error Codes

| code | httpStatus | Nguyên nhân |
|------|------------|-------------|
| `TRAINING_CENTER_NOT_FOUND` | 404 | ID không tồn tại trong database |
| `TRAINING_CENTER_CODE_DUPLICATED` | 409 | `code` đã được dùng bởi cơ sở đào tạo khác |
| `VALIDATION_FAILED` | 400 | Request body vi phạm ràng buộc validation |
| `UNAUTHORIZED` | 401 | Không có hoặc token hết hạn |
| `FORBIDDEN` | 403 | Token hợp lệ nhưng không có role `ADMIN` |
