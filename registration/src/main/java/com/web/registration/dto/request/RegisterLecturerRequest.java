package com.web.registration.dto.request;

import com.web.registration.entity.Gender;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterLecturerRequest {

    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Size(min = 3, max = 50, message = "Tên đăng nhập từ 3-50 ký tự")
    private String username;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    @Size(max = 100, message = "Email tối đa 100 ký tự")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, max = 100, message = "Mật khẩu từ 6-100 ký tự")
    private String password;

    @NotBlank(message = "Họ tên không được để trống")
    @Size(max = 100, message = "Họ tên tối đa 100 ký tự")
    private String fullName;

    private Gender gender;

    private LocalDate dateOfBirth;

    @NotBlank(message = "Mã giảng viên không được để trống")
    @Size(max = 20, message = "Mã giảng viên tối đa 20 ký tự")
    private String lecturerCode;

    @Size(max = 100, message = "Khoa/Bộ môn tối đa 100 ký tự")
    private String department;

    @Size(max = 100, message = "Học hàm/học vị tối đa 100 ký tự")
    private String academicRank;

    @NotNull(message = "Cơ sở đào tạo không được để trống")
    private Long trainingCenterId;
}
