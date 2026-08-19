package com.liquicool.controller;

import com.liquicool.common.ApiResponse;
import com.liquicool.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "文件上传")
@RestController
@RequestMapping("/api/file")
public class FileUploadController {

    @Autowired
    private FileStorageService fileStorageService;

    @Operation(summary = "上传文件")
    @PostMapping("/upload")
    public ApiResponse<Map<String, String>> upload(@RequestParam("file") MultipartFile file) {
        String url = fileStorageService.store(file);
        Map<String, String> data = new HashMap<>();
        data.put("url", url);
        return ApiResponse.ok(data);
    }
}
