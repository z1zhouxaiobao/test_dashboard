package com.liquicool.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 生产环境由后端托管 Vue 静态资源时，处理前端路由刷新。
 */
@Controller
public class SpaController {

    @GetMapping({
            "/",
            "/login",
            "/register",
            "/portal",
            "/portal/",
            "/portal/**",
            "/admin",
            "/admin/",
            "/admin/**"
    })
    public String forward() {
        return "forward:/index.html";
    }
}
