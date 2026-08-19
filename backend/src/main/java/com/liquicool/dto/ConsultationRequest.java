package com.liquicool.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ConsultationRequest {

    private Long productId;

    @NotBlank(message = "联系人不能为空")
    private String contactName;

    @NotBlank(message = "联系电话不能为空")
    private String phone;

    private String company;

    private String email;

    private String needType;

    @NotBlank(message = "咨询内容不能为空")
    private String content;
}
