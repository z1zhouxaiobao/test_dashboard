package com.liquicool.controller;

import com.liquicool.common.ApiResponse;
import com.liquicool.common.PageResult;
import com.liquicool.dto.ContactSettingsDto;
import com.liquicool.dto.DashboardStatsResponse;
import com.liquicool.dto.NavMenuTreeNode;
import com.liquicool.entity.*;
import com.liquicool.service.AdminService;
import com.liquicool.service.ContactSettingsService;
import com.liquicool.service.NavMenuService;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "后台管理")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private NavMenuService navMenuService;

    @Autowired
    private ContactSettingsService contactSettingsService;

    @Operation(summary = "仪表盘统计")
    @GetMapping("/dashboard/stats")
    public ApiResponse<DashboardStatsResponse> dashboardStats() {
        return ApiResponse.ok(adminService.getDashboardStats());
    }

    @GetMapping("/nav-menus")
    public ApiResponse<PageResult<NavMenu>> listNavMenus(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int size) {
        return ApiResponse.ok(navMenuService.listAdmin(keyword, page, size));
    }

    @GetMapping("/nav-menus/tree")
    public ApiResponse<List<NavMenuTreeNode>> navMenuTree() {
        return ApiResponse.ok(navMenuService.getPortalTree());
    }

    @GetMapping("/nav-menus/all")
    public ApiResponse<List<NavMenu>> navMenuAll() {
        return ApiResponse.ok(navMenuService.listAllFlat());
    }

    @GetMapping("/nav-menus/{id}")
    public ApiResponse<NavMenu> getNavMenu(@PathVariable Long id) {
        return ApiResponse.ok(navMenuService.getById(id));
    }

    @PostMapping("/nav-menus")
    public ApiResponse<NavMenu> createNavMenu(@RequestBody NavMenu menu) {
        return ApiResponse.ok(navMenuService.save(menu));
    }

    @PutMapping("/nav-menus/{id}")
    public ApiResponse<NavMenu> updateNavMenu(@PathVariable Long id, @RequestBody NavMenu menu) {
        menu.setId(id);
        return ApiResponse.ok(navMenuService.save(menu));
    }

    @DeleteMapping("/nav-menus/{id}")
    public ApiResponse<Void> deleteNavMenu(@PathVariable Long id) {
        navMenuService.delete(id);
        return ApiResponse.ok(null);
    }

    @GetMapping("/users")
    public ApiResponse<PageResult<SysUser>> listUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.ok(adminService.listUsers(keyword, page, size));
    }

    @GetMapping("/users/{id}")
    public ApiResponse<SysUser> getUser(@PathVariable Long id) {
        return ApiResponse.ok(adminService.getUser(id));
    }

    @PostMapping("/users")
    public ApiResponse<SysUser> createUser(@RequestBody SysUser user) {
        return ApiResponse.ok(adminService.saveUser(user));
    }

    @PutMapping("/users/{id}")
    public ApiResponse<SysUser> updateUser(@PathVariable Long id, @RequestBody SysUser user) {
        user.setId(id);
        return ApiResponse.ok(adminService.saveUser(user));
    }

    @DeleteMapping("/users/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ApiResponse.ok(null);
    }

    @GetMapping("/carousels")
    public ApiResponse<PageResult<Carousel>> listCarousels(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.ok(adminService.listCarousels(keyword, page, size));
    }

    @GetMapping("/carousels/{id}")
    public ApiResponse<Carousel> getCarousel(@PathVariable Long id) {
        return ApiResponse.ok(adminService.getCarousel(id));
    }

    @PostMapping("/carousels")
    public ApiResponse<Carousel> createCarousel(@RequestBody Carousel carousel) {
        return ApiResponse.ok(adminService.saveCarousel(carousel));
    }

    @PutMapping("/carousels/{id}")
    public ApiResponse<Carousel> updateCarousel(@PathVariable Long id, @RequestBody Carousel carousel) {
        carousel.setId(id);
        return ApiResponse.ok(adminService.saveCarousel(carousel));
    }

    @DeleteMapping("/carousels/{id}")
    public ApiResponse<Void> deleteCarousel(@PathVariable Long id) {
        adminService.deleteCarousel(id);
        return ApiResponse.ok(null);
    }

    @GetMapping("/news")
    public ApiResponse<PageResult<News>> listNews(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.ok(adminService.listNews(keyword, page, size));
    }

    @GetMapping("/news/{id}")
    public ApiResponse<News> getNews(@PathVariable Long id) {
        return ApiResponse.ok(adminService.getNews(id));
    }

    @PostMapping("/news")
    public ApiResponse<News> createNews(@RequestBody News news) {
        return ApiResponse.ok(adminService.saveNews(news));
    }

    @PutMapping("/news/{id}")
    public ApiResponse<News> updateNews(@PathVariable Long id, @RequestBody News news) {
        news.setId(id);
        return ApiResponse.ok(adminService.saveNews(news));
    }

    @DeleteMapping("/news/{id}")
    public ApiResponse<Void> deleteNews(@PathVariable Long id) {
        adminService.deleteNews(id);
        return ApiResponse.ok(null);
    }

    @GetMapping("/notices")
    public ApiResponse<PageResult<Notice>> listNotices(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.ok(adminService.listNotices(keyword, page, size));
    }

    @GetMapping("/notices/{id}")
    public ApiResponse<Notice> getNotice(@PathVariable Long id) {
        return ApiResponse.ok(adminService.getNotice(id));
    }

    @PostMapping("/notices")
    public ApiResponse<Notice> createNotice(@RequestBody Notice notice) {
        return ApiResponse.ok(adminService.saveNotice(notice));
    }

    @PutMapping("/notices/{id}")
    public ApiResponse<Notice> updateNotice(@PathVariable Long id, @RequestBody Notice notice) {
        notice.setId(id);
        return ApiResponse.ok(adminService.saveNotice(notice));
    }

    @DeleteMapping("/notices/{id}")
    public ApiResponse<Void> deleteNotice(@PathVariable Long id) {
        adminService.deleteNotice(id);
        return ApiResponse.ok(null);
    }

    @GetMapping("/products")
    public ApiResponse<PageResult<Product>> listProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.ok(adminService.listProducts(keyword, page, size));
    }

    @GetMapping("/products/{id}")
    public ApiResponse<Product> getProduct(@PathVariable Long id) {
        return ApiResponse.ok(adminService.getProduct(id));
    }

    @PostMapping("/products")
    public ApiResponse<Product> createProduct(@RequestBody Product product) {
        return ApiResponse.ok(adminService.saveProduct(product));
    }

    @PutMapping("/products/{id}")
    public ApiResponse<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        product.setId(id);
        return ApiResponse.ok(adminService.saveProduct(product));
    }

    @DeleteMapping("/products/{id}")
    public ApiResponse<Void> deleteProduct(@PathVariable Long id) {
        adminService.deleteProduct(id);
        return ApiResponse.ok(null);
    }

    @GetMapping("/technologies")
    public ApiResponse<PageResult<Technology>> listTechnologies(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.ok(adminService.listTechnologies(keyword, page, size));
    }

    @GetMapping("/technologies/{id}")
    public ApiResponse<Technology> getTechnology(@PathVariable Long id) {
        return ApiResponse.ok(adminService.getTechnology(id));
    }

    @PostMapping("/technologies")
    public ApiResponse<Technology> createTechnology(@RequestBody Technology technology) {
        return ApiResponse.ok(adminService.saveTechnology(technology));
    }

    @PutMapping("/technologies/{id}")
    public ApiResponse<Technology> updateTechnology(@PathVariable Long id, @RequestBody Technology technology) {
        technology.setId(id);
        return ApiResponse.ok(adminService.saveTechnology(technology));
    }

    @DeleteMapping("/technologies/{id}")
    public ApiResponse<Void> deleteTechnology(@PathVariable Long id) {
        adminService.deleteTechnology(id);
        return ApiResponse.ok(null);
    }

    @GetMapping("/honors")
    public ApiResponse<PageResult<Honor>> listHonors(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.ok(adminService.listHonors(keyword, page, size));
    }

    @GetMapping("/honors/{id}")
    public ApiResponse<Honor> getHonor(@PathVariable Long id) {
        return ApiResponse.ok(adminService.getHonor(id));
    }

    @PostMapping("/honors")
    public ApiResponse<Honor> createHonor(@RequestBody Honor honor) {
        return ApiResponse.ok(adminService.saveHonor(honor));
    }

    @PutMapping("/honors/{id}")
    public ApiResponse<Honor> updateHonor(@PathVariable Long id, @RequestBody Honor honor) {
        honor.setId(id);
        return ApiResponse.ok(adminService.saveHonor(honor));
    }

    @DeleteMapping("/honors/{id}")
    public ApiResponse<Void> deleteHonor(@PathVariable Long id) {
        adminService.deleteHonor(id);
        return ApiResponse.ok(null);
    }

    @GetMapping("/cases")
    public ApiResponse<PageResult<CaseStudy>> listCases(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.ok(adminService.listCases(keyword, page, size));
    }

    @GetMapping("/cases/{id}")
    public ApiResponse<CaseStudy> getCase(@PathVariable Long id) {
        return ApiResponse.ok(adminService.getCase(id));
    }

    @PostMapping("/cases")
    public ApiResponse<CaseStudy> createCase(@RequestBody CaseStudy caseStudy) {
        return ApiResponse.ok(adminService.saveCase(caseStudy));
    }

    @PutMapping("/cases/{id}")
    public ApiResponse<CaseStudy> updateCase(@PathVariable Long id, @RequestBody CaseStudy caseStudy) {
        caseStudy.setId(id);
        return ApiResponse.ok(adminService.saveCase(caseStudy));
    }

    @DeleteMapping("/cases/{id}")
    public ApiResponse<Void> deleteCase(@PathVariable Long id) {
        adminService.deleteCase(id);
        return ApiResponse.ok(null);
    }

    @GetMapping("/consultations")
    public ApiResponse<PageResult<Consultation>> listConsultations(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.ok(adminService.listConsultations(keyword, page, size));
    }

    @GetMapping("/consultations/{id}")
    public ApiResponse<Consultation> getConsultation(@PathVariable Long id) {
        return ApiResponse.ok(adminService.getConsultation(id));
    }

    @PutMapping("/consultations/{id}")
    public ApiResponse<Consultation> updateConsultation(@PathVariable Long id, @RequestBody Consultation consultation) {
        consultation.setId(id);
        return ApiResponse.ok(adminService.saveConsultation(consultation));
    }

    @DeleteMapping("/consultations/{id}")
    public ApiResponse<Void> deleteConsultation(@PathVariable Long id) {
        adminService.deleteConsultation(id);
        return ApiResponse.ok(null);
    }

    @GetMapping("/feedbacks")
    public ApiResponse<PageResult<Feedback>> listFeedbacks(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.ok(adminService.listFeedbacks(keyword, page, size));
    }

    @GetMapping("/feedbacks/{id}")
    public ApiResponse<Feedback> getFeedback(@PathVariable Long id) {
        return ApiResponse.ok(adminService.getFeedback(id));
    }

    @PutMapping("/feedbacks/{id}")
    public ApiResponse<Feedback> updateFeedback(@PathVariable Long id, @RequestBody Feedback feedback) {
        feedback.setId(id);
        return ApiResponse.ok(adminService.saveFeedback(feedback));
    }

    @DeleteMapping("/feedbacks/{id}")
    public ApiResponse<Void> deleteFeedback(@PathVariable Long id) {
        adminService.deleteFeedback(id);
        return ApiResponse.ok(null);
    }

    @GetMapping("/contact-settings")
    public ApiResponse<ContactSettingsDto> getContactSettings() {
        return ApiResponse.ok(contactSettingsService.getSettings());
    }

    @PutMapping("/contact-settings")
    public ApiResponse<ContactSettingsDto> saveContactSettings(@RequestBody ContactSettingsDto dto) {
        return ApiResponse.ok(contactSettingsService.saveSettings(dto));
    }

    @GetMapping("/configs")
    public ApiResponse<PageResult<SysConfig>> listConfigs(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.ok(adminService.listConfigs(keyword, page, size));
    }

    @GetMapping("/configs/{id}")
    public ApiResponse<SysConfig> getConfig(@PathVariable Long id) {
        return ApiResponse.ok(adminService.getConfig(id));
    }

    @PostMapping("/configs")
    public ApiResponse<SysConfig> createConfig(@RequestBody SysConfig config) {
        return ApiResponse.ok(adminService.saveConfig(config));
    }

    @PutMapping("/configs/{id}")
    public ApiResponse<SysConfig> updateConfig(@PathVariable Long id, @RequestBody SysConfig config) {
        config.setId(id);
        return ApiResponse.ok(adminService.saveConfig(config));
    }

    @DeleteMapping("/configs/{id}")
    public ApiResponse<Void> deleteConfig(@PathVariable Long id) {
        adminService.deleteConfig(id);
        return ApiResponse.ok(null);
    }

    @GetMapping("/operate-logs")
    public ApiResponse<PageResult<OperateLog>> listOperateLogs(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.ok(adminService.listOperateLogs(keyword, page, size));
    }

    @DeleteMapping("/operate-logs/{id}")
    public ApiResponse<Void> deleteOperateLog(@PathVariable Long id) {
        adminService.deleteOperateLog(id);
        return ApiResponse.ok(null);
    }
}
