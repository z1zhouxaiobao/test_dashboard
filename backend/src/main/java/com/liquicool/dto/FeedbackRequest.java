package com.liquicool.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class FeedbackRequest {

    private String contactName;

    @NotBlank(message = "反馈内容不能为空")
    private String content;

    private String contact;
}
