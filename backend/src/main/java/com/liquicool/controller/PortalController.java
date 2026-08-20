package com.liquicool.controller;

import com.liquicool.common.ApiResponse;
import com.liquicool.common.PageResult;
import com.liquicool.dto.NavMenuTreeNode;
import com.liquicool.dto.PortalOverviewResponse;
import com.liquicool.entity.*;
import com.liquicool.service.NavMenuService;
import com.liquicool.service.PortalService;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "门户公开接口")
@RestController
@RequestMapping("/api/portal")
public class PortalController {

    @Autowired
    private PortalService portalService;

    @Autowired
    private NavMenuService navMenuService;

    @Operation(summary = "门户导航树（含二级三级）")
    @GetMapping("/nav-menus")
    public ApiResponse<List<NavMenuTreeNode>> navMenus(
            @RequestParam(required = false) String moduleCode) {
        if (moduleCode != null && !moduleCode.trim().isEmpty()) {
            return ApiResponse.ok(navMenuService.getPortalTreeByModule(moduleCode.trim()));
        }
        return ApiResponse.ok(navMenuService.getPortalTree());
    }

    @Operation(summary = "统计概览")
    @GetMapping("/overview")
    public ApiResponse<PortalOverviewResponse> overview() {
        return ApiResponse.ok(portalService.getOverview());
    }

    @Operation(summary = "轮播图列表")
    @GetMapping("/carousels")
    public ApiResponse<PageResult<Carousel>> carousels(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.ok(portalService.listCarousels(page, size));
    }

    @Operation(summary = "新闻列表")
    @GetMapping("/news")
    public ApiResponse<PageResult<News>> news(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.ok(portalService.listNews(keyword, page, size));
    }

    @Operation(summary = "新闻详情")
    @GetMapping("/news/{id}")
    public ApiResponse<News> newsDetail(@PathVariable Long id) {
        return ApiResponse.ok(portalService.getNewsDetail(id));
    }

    @Operation(summary = "公告列表")
    @GetMapping("/notices")
    public ApiResponse<PageResult<Notice>> notices(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.ok(portalService.listNotices(keyword, page, size));
    }

    @Operation(summary = "公告详情")
    @GetMapping("/notices/{id}")
    public ApiResponse<Notice> noticeDetail(@PathVariable Long id) {
        return ApiResponse.ok(portalService.getNoticeDetail(id));
    }

    @Operation(summary = "产品列表")
    @GetMapping("/products")
    public ApiResponse<PageResult<Product>> products(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.ok(portalService.listProducts(keyword, category, page, size));
    }

    @Operation(summary = "产品详情")
    @GetMapping("/products/{id}")
    public ApiResponse<Product> productDetail(@PathVariable Long id) {
        return ApiResponse.ok(portalService.getProductDetail(id));
    }

    @Operation(summary = "技术列表")
    @GetMapping("/technologies")
    public ApiResponse<PageResult<Technology>> technologies(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.ok(portalService.listTechnologies(keyword, page, size));
    }

    @Operation(summary = "技术详情")
    @GetMapping("/technologies/{id}")
    public ApiResponse<Technology> technologyDetail(@PathVariable Long id) {
        return ApiResponse.ok(portalService.getTechnologyDetail(id));
    }

    @Operation(summary = "荣誉列表")
    @GetMapping("/honors")
    public ApiResponse<PageResult<Honor>> honors(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.ok(portalService.listHonors(keyword, page, size));
    }

    @Operation(summary = "荣誉详情")
    @GetMapping("/honors/{id}")
    public ApiResponse<Honor> honorDetail(@PathVariable Long id) {
        return ApiResponse.ok(portalService.getHonorDetail(id));
    }

    @Operation(summary = "案例列表")
    @GetMapping("/cases")
    public ApiResponse<PageResult<CaseStudy>> cases(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String industry,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.ok(portalService.listCases(keyword, industry, page, size));
    }

    @Operation(summary = "案例详情")
    @GetMapping("/cases/{id}")
    public ApiResponse<CaseStudy> caseDetail(@PathVariable Long id) {
        return ApiResponse.ok(portalService.getCaseDetail(id));
    }
}
