package com.liquicool.controller;

import com.liquicool.common.ApiResponse;
import com.liquicool.dto.LoginRequest;
import com.liquicool.dto.RegisterRequest;
import com.liquicool.dto.UserInfoResponse;
import com.liquicool.exception.BusinessException;
import com.liquicool.service.AuthService;
import com.liquicool.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Tag(name = "认证")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public ApiResponse<UserInfoResponse> login(@Validated @RequestBody LoginRequest request) {
        return ApiResponse.ok(authService.login(request));
    }

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public ApiResponse<UserInfoResponse> register(@Validated @RequestBody RegisterRequest request) {
        return ApiResponse.ok(authService.register(request));
    }

    @Operation(summary = "获取当前用户信息")
    @GetMapping("/me")
    public ApiResponse<UserInfoResponse> me(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new BusinessException(401, "未登录或Token无效");
        }
        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            throw new BusinessException(401, "Token已过期或无效");
        }
        Long userId = jwtUtil.getUserId(token);
        return ApiResponse.ok(authService.getCurrentUser(userId));
    }
}
