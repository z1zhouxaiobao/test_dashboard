package com.liquicool.dto;

import com.liquicool.enums.UserRole;
import lombok.Data;

@Data
public class UserInfoResponse {

    private Long id;
    private String username;
    private String nickname;
    private String avatar;
    private String phone;
    private String email;
    private UserRole role;
    private Integer status;
    private String token;
}
