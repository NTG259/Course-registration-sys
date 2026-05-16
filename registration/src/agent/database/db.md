# THIẾT KẾ CƠ SỞ DỮ LIỆU TOÀN CỤC
## HỆ THỐNG ĐĂNG KÝ HỌC PHẦN NHIỀU CƠ SỞ

---

## 1. Danh sách các bảng và Thuộc tính (Data Dictionary)

### 1.1. Bảng CoSo (Cơ sở)
*Quản lý thông tin về các cơ sở đào tạo của trường (Ví dụ: Cơ sở 1, Cơ sở 2).*

| Tên thuộc tính | Kiểu dữ liệu | Ràng buộc | Mô tả |
| :--- | :--- | :--- | :--- |
| **MaCS** | VARCHAR(10) | Khóa chính | Mã định danh cơ sở |
| TenCS | NVARCHAR(100) | Not Null | Tên cơ sở |
| DiaChi | NVARCHAR(200) | | Địa chỉ của cơ sở |

### 1.2. Bảng Khoa (Khoa / Viện)
*Mỗi cơ sở sẽ quản lý một số Khoa/Viện đào tạo nhất định.*

| Tên thuộc tính | Kiểu dữ liệu | Ràng buộc | Mô tả |
| :--- | :--- | :--- | :--- |
| **MaKhoa** | VARCHAR(10) | Khóa chính | Mã định danh khoa |
| TenKhoa | NVARCHAR(100) | Not Null | Tên khoa |
| MaCS | VARCHAR(10) | Khóa ngoại | Thuộc cơ sở nào (`CoSo.MaCS`) |

### 1.3. Bảng LopChuyenNganh (Lớp chuyên ngành)
*Quản lý lớp sinh hoạt của sinh viên (Ví dụ: D23CQCN01-B).*

| Tên thuộc tính | Kiểu dữ liệu | Ràng buộc | Mô tả |
| :--- | :--- | :--- | :--- |
| **MaLop** | VARCHAR(15) | Khóa chính | Mã lớp hành chính/chuyên ngành |
| TenLop | NVARCHAR(100) | Not Null | Tên lớp |
| MaKhoa | VARCHAR(10) | Khóa ngoại | Lớp thuộc khoa nào (`Khoa.MaKhoa`) |

### 1.4. Bảng SinhVien (Sinh viên)
*Lưu trữ thông tin cá nhân và lớp học cố định của sinh viên.*

| Tên thuộc tính | Kiểu dữ liệu | Ràng buộc | Mô tả |
| :--- | :--- | :--- | :--- |
| **MaSV** | VARCHAR(15) | Khóa chính | Mã số sinh viên (MSSV) |
| HoTen | NVARCHAR(100) | Not Null | Họ và tên sinh viên |
| NgaySinh | DATE | Not Null | Ngày sinh |
| GioiTinh | NVARCHAR(10) | | Giới tính |
| MaLop | VARCHAR(15) | Khóa ngoại | Lớp sinh hoạt cố định (`LopChuyenNganh.MaLop`) |

### 1.5. Bảng GiangVien (Giảng viên)
*Thông tin giảng viên thuộc các khoa quản lý.*

| Tên thuộc tính | Kiểu dữ liệu | Ràng buộc | Mô tả |
| :--- | :--- | :--- | :--- |
| **MaGV** | VARCHAR(15) | Khóa chính | Mã định danh giảng viên |
| HoTen | NVARCHAR(100) | Not Null | Họ và tên giảng viên |
| HocVi | NVARCHAR(50) | | Học vị (Thạc sĩ, Tiến sĩ,...) |
| MaKhoa | VARCHAR(10) | Khóa ngoại | Giảng viên thuộc khoa nào (`Khoa.MaKhoa`) |

### 1.6. Bảng HocPhan (Học phần / Môn học)
*Danh mục các môn học trong chương trình đào tạo chuẩn của toàn trường.*

| Tên thuộc tính | Kiểu dữ liệu | Ràng buộc | Mô tả |
| :--- | :--- | :--- | :--- |
| **MaHP** | VARCHAR(10) | Khóa chính | Mã học phần |
| TenHP | NVARCHAR(100) | Not Null | Tên học phần |
| SoTinChi | INT | Not Null, > 0 | Số tín chỉ của học phần |
| PhanTramGiuaKy | INT | Từ 0 đến 100 | Tỷ trọng điểm giữa kỳ (%) |
| PhanTramCuoiKy | INT | Từ 0 đến 100 | Tỷ trọng điểm cuối kỳ (%) |

### 1.7. Bảng PhongHoc (Phòng học)
*Quản lý cơ sở vật chất phòng học tại từng cơ sở.*

| Tên thuộc tính | Kiểu dữ liệu | Ràng buộc | Mô tả |
| :--- | :--- | :--- | :--- |
| **MaPhong** | VARCHAR(15) | Khóa chính | Mã phòng học (Ví dụ: A2-301) |
| TenPhong | NVARCHAR(50) | Not Null | Tên hiển thị phòng học |
| SucChua | INT | Not Null, > 0 | Số lượng ghế ngồi tối đa |
| MaCS | VARCHAR(10) | Khóa ngoại | Phòng thuộc cơ sở nào (`CoSo.MaCS`) |

### 1.8. Bảng LopHocPhan (Lớp học phần)
*Các lớp cụ thể được mở theo từng học kỳ để sinh viên vào đăng ký học.*

| Tên thuộc tính | Kiểu dữ liệu | Ràng buộc | Mô tả |
| :--- | :--- | :--- | :--- |
| **MaLopHP** | VARCHAR(20) | Khóa chính | Mã lớp học phần |
| MaHP | VARCHAR(10) | Khóa ngoại | Thuộc học phần nào (`HocPhan.MaHP`) |
| MaGV | VARCHAR(15) | Khóa ngoại | Giảng viên phụ trách (`GiangVien.MaGV`) |
| HocKy | INT | Not Null (1, 2, 3) | Học kỳ mở lớp |
| NamHoc | VARCHAR(15) | Not Null (Ví dụ: 2025-2026) | Năm học mở lớp |
| SisoToiDa | INT | Not Null | Giới hạn số lượng SV tối đa |
| SiSoHienTai | INT | Default 0 | Số lượng SV thực tế đã đăng ký |
| MaCS | VARCHAR(10) | Khóa ngoại | Lớp này được học tại cơ sở nào (`CoSo.MaCS`) |

### 1.9. Bảng LichHoc (Lịch học chi tiết)
*Một Lớp học phần có thể có một hoặc nhiều buổi học trong tuần.*

| Tên thuộc tính | Kiểu dữ liệu | Ràng buộc | Mô tả |
| :--- | :--- | :--- | :--- |
| **MaLich** | INT | Khóa chính, Auto | ID tự tăng quản lý lịch |
| MaLopHP | VARCHAR(20) | Khóa ngoại | Thuộc lớp học phần nào (`LopHocPhan.MaLopHP`) |
| MaPhong | VARCHAR(15) | Khóa ngoại | Xếp lịch tại phòng nào (`PhongHoc.MaPhong`) |
| Thu | INT | Từ 2 đến 8 | Ngày trong tuần (2 = Thứ hai, 8 = Chủ nhật) |
| TietBatDau | INT | Từ 1 đến 16 | Tiết bắt đầu buổi học |
| SoTiet | INT | Not Null, > 0 | Số tiết diễn ra của buổi học |

### 1.10. Bảng DangKy (Đăng ký học phần)
*Bảng trung gian thể hiện kết quả sinh viên đăng ký thành công vào lớp học phần.*

| Tên thuộc tính | Kiểu dữ liệu | Ràng buộc | Mô tả |
| :--- | :--- | :--- | :--- |
| **MaSV** | VARCHAR(15) | Khóa chính, Khóa ngoại | Sinh viên đăng ký (`SinhVien.MaSV`) |
| **MaLopHP** | VARCHAR(20) | Khóa chính, Khóa ngoại | Lớp học phần được chọn (`LopHocPhan.MaLopHP`) |
| NgayDangKy | DATETIME | Default NOW() | Thời gian thực hiện đăng ký |
| TrangThai | NVARCHAR(30) | | Trạng thái (Đang chờ, Thành công, Hủy) |

---

## 2. Mối quan hệ giữa các bảng (Relationships)

Dưới đây là mô tả logic các mối quan hệ (Ràng buộc toàn vẹn khóa ngoại) ràng buộc giữa các thực thể trong toàn hệ thống:

1. **CoSo — Khoa (1 - Nhiều):** Một cơ sở chứa nhiều khoa đào tạo. Một khoa thuộc về duy nhất một cơ sở. (`Khoa.MaCS` liên kết đến `CoSo.MaCS`).
2. **Khoa — LopChuyenNganh (1 - Nhiều):** Một khoa quản lý nhiều lớp chuyên ngành sinh hoạt. (`LopChuyenNganh.MaKhoa` liên kết đến `Khoa.MaKhoa`).
3. **Khoa — GiangVien (1 - Nhiều):** Giảng viên nhân sự thuộc sự quản lý hành chính của một khoa cụ thể. (`GiangVien.MaKhoa` liên kết đến `Khoa.MaKhoa`).
4. **LopChuyenNganh — SinhVien (1 - Nhiều):** Một lớp chuyên ngành chứa nhiều sinh viên. Một sinh viên chỉ thuộc một lớp cố định. (`SinhVien.MaLop` liên kết đến `LopChuyenNganh.MaLop`).
5. **CoSo — PhongHoc (1 - Nhiều):** Cơ sở vật chất phòng học thuộc quyền quản lý và định vị theo cơ sở. (`PhongHoc.MaCS` liên kết đến `CoSo.MaCS`).
6. **HocPhan — LopHocPhan (1 - Nhiều):** Một học phần lý thuyết có thể được mở thành nhiều lớp học phần khác nhau trong cùng kỳ học hoặc qua các năm. (`LopHocPhan.MaHP` liên kết đến `HocPhan.MaHP`).
7. **GiangVien — LopHocPhan (1 - Nhiều):** Một giảng viên có thể được phân công giảng dạy nhiều lớp học phần. (`LopHocPhan.MaGV` liên kết đến `GiangVien.MaGV`).
8. **CoSo — LopHocPhan (1 - Nhiều):** Xác định lớp học phần đó được tổ chức học vật lý tại cơ sở nào (phục vụ bài toán sinh viên cơ sở này đăng ký học chéo sang lớp cơ sở khác). (`LopHocPhan.MaCS` liên kết đến `CoSo.MaCS`).
9. **LopHocPhan — LichHoc (1 - Nhiều):** Một lớp học phần có thể có nhiều buổi học (Ví dụ: Học lý thuyết thứ 2 tại phòng A, học thực hành thứ 5 tại phòng Lab). (`LichHoc.MaLopHP` liên kết đến `LopHocPhan.MaLopHP`).
10. **PhongHoc — LichHoc (1 - Nhiều):** Một phòng học tại một thời điểm (Thứ, Tiết) chỉ chứa một lịch học của một lớp học phần. (`LichHoc.MaPhong` liên kết đến `PhongHoc.MaPhong`).
11. **SinhVien — LopHocPhan (Nhiều - Nhiều):** Mối quan hệ nhiều - nhiều giữa Sinh viên và Lớp học phần được giải quyết thông qua bảng trung gian **DangKy**.
    * Một Sinh viên có thể đăng ký học nhiều Lớp học phần khác nhau.
    * Một Lớp học phần chứa danh sách gồm nhiều Sinh viên tham gia học.