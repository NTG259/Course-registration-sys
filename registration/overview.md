# Tổng quan Hệ thống Đăng ký Tín chỉ (Course Registration System)

Tài liệu này mô tả tổng quan về các vai trò và chức năng tương ứng trong Hệ thống Đăng ký Tín chỉ dựa trên phạm vi yêu cầu.

## Phạm vi Hệ thống (Scope)
Hệ thống tập trung vào các nhóm chức năng cốt lõi sau:
1. Quản lý cơ sở đào tạo
2. Quản lý sinh viên
3. Quản lý giảng viên
4. Quản lý học phần
5. Quản lý lớp học phần
6. Quản lý phòng học và lịch học
7. Đăng ký học phần
8. Hủy đăng ký học phần
9. Tra cứu kết quả đăng ký
10. Thống kê theo cơ sở và toàn trường

---

## Phân bổ Chức năng theo Vai trò

Hệ thống phân quyền các chức năng trên cho 4 vai trò: **ADMIN**, **LOCAL_ADMIN**, **LECTURE**, và **STUDENT**.

### 1. ADMIN (Quản trị viên hệ thống)
Quản lý tổng thể ở cấp độ toàn trường.
* **Quản lý cơ sở đào tạo**: Thêm mới, cập nhật, xóa, quản lý danh sách các cơ sở đào tạo của trường.
* **Quản lý sinh viên**: Quản lý toàn bộ hồ sơ, dữ liệu sinh viên của toàn trường.
* **Quản lý giảng viên**: Quản lý toàn bộ danh sách và thông tin giảng viên toàn trường.
* **Quản lý học phần**: Quản lý danh mục học phần, quy định tín chỉ dùng chung cho toàn trường.
* **Thống kê toàn trường**: Xem báo cáo tổng hợp và thống kê dữ liệu về tình hình đăng ký học phần trên quy mô toàn trường.

### 2. LOCAL_ADMIN (Quản lý cơ sở / Giáo vụ)
Quản lý các hoạt động học vụ diễn ra trong phạm vi một cơ sở đào tạo cụ thể.
* **Quản lý sinh viên**: Quản lý thông tin, hồ sơ của sinh viên thuộc cơ sở mình quản lý.
* **Quản lý giảng viên**: Quản lý danh sách, thông tin của giảng viên thuộc cơ sở.
* **Quản lý học phần**: Theo dõi các học phần được áp dụng giảng dạy tại cơ sở.
* **Quản lý lớp học phần**: Quyết định mở lớp, đóng lớp, giới hạn số lượng sinh viên, và phân công giảng viên cho các lớp học phần tại cơ sở.
* **Quản lý phòng học và lịch học**: Quản lý danh sách phòng học, xếp lịch học, ca học cho các lớp học phần của cơ sở.
* **Thống kê theo cơ sở**: Xuất báo cáo, thống kê số liệu đăng ký học phần, tình trạng lớp học phần trong phạm vi cơ sở.

### 3. LECTURE (Giảng viên)
Tập trung vào công tác giảng dạy được phân công.
* **Quản lý lớp học phần**: Xem danh sách các lớp học phần mình được phân công giảng dạy và danh sách sinh viên của lớp.
* **Quản lý phòng học và lịch học**: Tra cứu lịch giảng dạy cá nhân (phòng học, ca học, thời gian) của các lớp học phần phụ trách.

### 4. STUDENT (Sinh viên)
Người dùng cuối thực hiện quy trình đăng ký tín chỉ và theo dõi lịch học.
* **Đăng ký học phần**: Tra cứu các lớp học phần đang mở và tiến hành đăng ký.
* **Hủy đăng ký học phần**: Thực hiện rút hoặc hủy lớp học phần đã đăng ký (trong khoảng thời gian hệ thống quy định).
* **Tra cứu kết quả đăng ký**: Xem lại danh sách các học phần đã đăng ký thành công hoặc thất bại.
* **Quản lý phòng học và lịch học**: Xem thời khóa biểu cá nhân, tra cứu phòng học và thời gian của các học phần đã đăng ký.
