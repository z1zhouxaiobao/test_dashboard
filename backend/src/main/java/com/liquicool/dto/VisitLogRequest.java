package com.liquicool.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class VisitLogRequest {

    @NotBlank(message = "访问路径不能为空")
    @Size(max = 500)
    private String path;

    @Size(max = 200)
    private String pageTitle;

    @Size(max = 500)
    private String referer;

    @Size(max = 20)
    private String locale;
}
