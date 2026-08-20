package com.liquicool.controller;

import com.liquicool.common.ApiResponse;
import com.liquicool.common.PageResult;
import com.liquicool.context.UserContext;
import com.liquicool.dto.ConsultationRequest;
import com.liquicool.dto.FeedbackRequest;
import com.liquicool.dto.ProfileUpdateRequest;
import com.liquicool.entity.Consultation;
import com.liquicool.entity.Feedback;
import com.liquicool.entity.SysUser;
import com.liquicool.service.UserCenterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "用户中心")
@RestController
@RequestMapping("/api/user")
public class UserCenterController {

    @Autowired
    private UserCenterService userCenterService;

    @Operation(summary = "修改个人资料")
    @PutMapping("/profile")
    public ApiResponse<SysUser> updateProfile(@RequestBody ProfileUpdateRequest request) {
        return ApiResponse.ok(userCenterService.updateProfile(UserContext.getUserId(), request));
    }

    @Operation(summary = "修改头像")
    @PutMapping("/avatar")
    public ApiResponse<SysUser> updateAvatar(@RequestBody Map<String, String> body) {
        String avatar = body.get("avatar");
        return ApiResponse.ok(userCenterService.updateAvatar(UserContext.getUserId(), avatar));
    }

    @Operation(summary = "提交咨询")
    @PostMapping("/consultations")
    public ApiResponse<Consultation> submitConsultation(@Validated @RequestBody ConsultationRequest request) {
        return ApiResponse.ok(userCenterService.submitConsultation(UserContext.getUserId(), request));
    }

    @Operation(summary = "我的咨询列表")
    @GetMapping("/consultations")
    public ApiResponse<PageResult<Consultation>> listConsultations(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.ok(userCenterService.listMyConsultations(UserContext.getUserId(), page, size));
    }

    @Operation(summary = "提交反馈")
    @PostMapping("/feedbacks")
    public ApiResponse<Feedback> submitFeedback(@Validated @RequestBody FeedbackRequest request) {
        return ApiResponse.ok(userCenterService.submitFeedback(UserContext.getUserId(), request));
    }
}
