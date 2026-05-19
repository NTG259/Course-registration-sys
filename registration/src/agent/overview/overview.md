# PHÂN TÍCH VAI TRÒ VÀ CHỨC NĂNG CỦA CÁC ĐỐI TƯỢNG TRONG HỆ THỐNG

Hệ thống quản lý đăng ký học phần nhiều cơ sở được phân quyền dựa trên 3 đối tượng sử dụng cốt lõi nhằm đảm bảo tính toàn vẹn dữ liệu, tối ưu hóa hiệu năng truy xuất cục bộ và hỗ trợ các giao dịch phân tán liên cơ sở.

---

## 1. Admin Toàn Trường (Super Administrator)

### 📌 Tổng quan vai trò
Admin toàn trường đại diện cho Ban quản lý đào tạo cấp cao nhất của nhà trường (ví dụ: Phòng Đào tạo trung tâm). Đối tượng này có toàn quyền quản trị trên toàn hệ thống, chịu trách nhiệm quản lý các danh mục dữ liệu dùng chung toàn trường và giám sát số liệu tổng hợp từ tất cả các cơ sở đào tạo.

### 🛠️ Các chức năng chính
* **Quản lý danh mục học phần:** Thêm, sửa, xóa các môn học/học phần trong chương trình đào tạo chuẩn toàn trường (tác động lên bảng `HocPhan`). Dữ liệu này sau đó sẽ được hệ thống tự động nhân bản (replicate) xuống tất cả các site cơ sở để phục vụ tra cứu tại chỗ.
* **Quản lý cấu trúc tổ chức:** Thêm mới hoặc cập nhật thông tin về các Cơ sở đào tạo (`CoSo`) và các Khoa/Viện trực thuộc (`Khoa`).
* **Cấu hình tham số hệ thống:** Thiết lập thời gian bắt đầu/kết thúc các đợt đăng ký học phần, giới hạn số tín chỉ tối đa/tối thiểu mà một sinh viên được phép đăng ký trong một học kỳ.
* **Thống kê và Báo cáo toàn trường:** Thực hiện các truy vấn phân tán để xem các báo cáo tổng hợp:
    * Thống kê tổng số lượng sinh viên đăng ký học phần phân rã theo từng cơ sở.
    * Tìm kiếm các học phần có số lượng sinh viên đăng ký đông nhất toàn trường.
    * Xuất danh sách và tỷ lệ sinh viên thực hiện đăng ký chéo cơ sở.
    * Đánh giá tỷ lệ lấp đầy tổng quan của các lớp học phần trên toàn hệ thống.

---

## 2. Giáo Vụ Cơ Sở (Site Administrator / Staff)

### 📌 Tổng quan vai trò
Giáo vụ cơ sở là những cán bộ quản lý đào tạo trực tiếp tại một cơ sở nhất định (ví dụ: Giáo vụ Cơ sở 1, Giáo vụ Cơ sở 2). Đối tượng này có quyền quản trị cục bộ, chỉ được phép thao tác và quản lý trên các dòng dữ liệu thuộc phạm vi cơ sở của mình đảm nhiệm, không có quyền can thiệp vào dữ liệu nội bộ của cơ sở khác.

### 🛠️ Các chức năng chính
* **Quản lý thông tin người học và nhân sự cục bộ:** Thêm, sửa, xóa và chuẩn hóa hồ sơ dữ liệu của Sinh viên (`SinhVien`) và Giảng viên (`GiangVien`) thuộc cơ sở mình quản lý (phân mảnh ngang dẫn xuất).
* **Quản lý cơ sở vật chất:** Khai báo và quản lý danh mục các phòng học (`PhongHoc`), sức chứa của từng phòng học vật lý tại cơ sở.
* **Quản lý mở lớp học phần:** * Khởi tạo các Lớp học phần (`LopHocPhan`) dựa trên danh mục học phần chung của trường, quy định sĩ số tối đa cho từng lớp.
    * Xếp lịch học chi tiết (`LichHoc`): Phân công giảng viên giảng dạy, xếp phòng học, chọn thứ và tiết học cụ thể trong tuần.
* **Thống kê và Báo cáo cấp cơ sở:** * Theo dõi tiến độ và số lượng bản ghi đăng ký học phần của sinh viên thuộc cơ sở mình.
    * Thống kê số lượng lớp học phần đã mở theo từng khoa hoặc bộ môn thuộc cơ sở quản lý.

---

## 3. Sinh Viên (Student)

### 📌 Tổng quan vai trò
Sinh viên là người dùng cuối (End-user) tham gia vào hệ thống với mục đích thực hiện các tác vụ liên quan đến kế hoạch học tập cá nhân. Sinh viên có quyền truy cập, tra cứu thông tin học phần toàn trường nhưng chỉ có quyền ghi/sửa đổi đối với dữ liệu đăng ký cá nhân của chính mình.

### 🛠️ Các chức năng chính
* **Tra cứu và Tìm kiếm thông tin:**
    * Xem danh mục các học phần mở trong học kỳ (dữ liệu nhân bản có sẵn tại site).
    * Tra cứu lịch học, thông tin giảng viên và số chỗ còn trống (sĩ số hiện tại so với sĩ số tối đa) của các lớp học phần được mở tại cơ sở mình và các cơ sở khác (để phục vụ nhu cầu đăng ký chéo).
* **Thực hiện Đăng ký học phần:** Thao tác thêm mới bản ghi vào bảng `DangKy`. Đây là chức năng cốt lõi kích hoạt cơ chế kiểm soát đồng thời (Transaction/Locking) tại server nhằm giải quyết tranh chấp dữ liệu khi nhiều sinh viên cùng click đăng ký vào một lớp học phần có giới hạn sĩ số tại cùng một thời điểm.
* **Hủy đăng ký học phần:** Thực hiện rút bớt môn học hoặc đổi lớp học phần trong khoảng thời gian hệ thống cho phép (Xóa hoặc cập nhật trạng thái bản ghi trong bảng `DangKy`), hệ thống sẽ tự động giảm sĩ số hiện tại của lớp học phần tương ứng.
* **Xem kết quả và Thời khóa biểu cá nhân:** Hệ thống tự động tổng hợp dữ liệu từ các lớp học phần sinh viên đã đăng ký thành công để xuất ra lịch học chi tiết theo dạng thời khóa biểu tuần/học kỳ cho sinh viên.