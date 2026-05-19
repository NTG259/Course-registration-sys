package com.web.registration.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoResponse {

    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String role;
}
