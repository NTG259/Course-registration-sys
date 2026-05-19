package com.web.registration.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingCenterRequest {

    @NotBlank(message = "Mã cơ sở đào tạo không được để trống")
    @Size(max = 20, message = "Mã cơ sở đào tạo tối đa 20 ký tự")
    private String code;

    @NotBlank(message = "Tên cơ sở đào tạo không được để trống")
    @Size(max = 255, message = "Tên cơ sở đào tạo tối đa 255 ký tự")
    private String name;

    @Size(max = 255, message = "Địa chỉ tối đa 255 ký tự")
    private String address;

    @Size(max = 20, message = "Số điện thoại tối đa 20 ký tự")
    private String phone;

    @Email(message = "Email không hợp lệ")
    @Size(max = 100, message = "Email tối đa 100 ký tự")
    private String email;
}
