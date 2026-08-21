package com.liquicool.service;

import com.liquicool.common.PageResult;
import com.liquicool.dto.DashboardStatsResponse;
import com.liquicool.entity.*;
import com.liquicool.enums.ConsultationStatus;
import com.liquicool.exception.BusinessException;
import com.liquicool.repository.*;
import com.liquicool.util.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private SysUserRepository sysUserRepository;
    @Autowired
    private CarouselRepository carouselRepository;
    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private NoticeRepository noticeRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private TechnologyRepository technologyRepository;
    @Autowired
    private HonorRepository honorRepository;
    @Autowired
    private JobPositionRepository jobPositionRepository;
    @Autowired
    private CaseStudyRepository caseStudyRepository;
    @Autowired
    private ConsultationRepository consultationRepository;
    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private SysConfigRepository sysConfigRepository;
    @Autowired
    private OperateLogRepository operateLogRepository;
    @Autowired
    private PortalVisitLogRepository portalVisitLogRepository;

    public DashboardStatsResponse getDashboardStats() {
        DashboardStatsResponse stats = new DashboardStatsResponse();
        stats.setUserCount(sysUserRepository.count());
        stats.setProductCount(productRepository.count());
        stats.setConsultationCount(consultationRepository.count());
        stats.setNewsCount(newsRepository.count());

        Map<String, Long> consultationByStatus = new LinkedHashMap<>();
        for (ConsultationStatus status : ConsultationStatus.values()) {
            consultationByStatus.put(status.name(), consultationRepository.countByStatus(status));
        }
        stats.setConsultationByStatus(consultationByStatus);

        LocalDateTime sevenDaysAgo = LocalDate.now().atStartOfDay().minusDays(6);
        List<Object[]> trendRows = consultationRepository.countByDaySince(sevenDaysAgo);
        Map<String, Long> trendMap = new LinkedHashMap<>();
        for (int i = 0; i < 7; i++) {
            String day = LocalDate.now().minusDays(6 - i).toString();
            trendMap.put(day, 0L);
        }
        for (Object[] row : trendRows) {
            String day = row[0].toString();
            Long count = Long.valueOf(row[1].toString());
            trendMap.put(day, count);
        }
        List<Map<String, Object>> trendList = trendMap.entrySet().stream().map(entry -> {
            Map<String, Object> item = new HashMap<>();
            item.put("date", entry.getKey());
            item.put("count", entry.getValue());
            return item;
        }).collect(Collectors.toList());
        stats.setConsultationTrend7Days(trendList);

        List<Object[]> categoryRows = productRepository.countGroupByCategory();
        List<Map<String, Object>> categoryList = categoryRows.stream().map(row -> {
            Map<String, Object> item = new HashMap<>();
            item.put("category", row[0] != null ? row[0].toString() : "未知");
            item.put("count", Long.valueOf(row[1].toString()));
            return item;
        }).collect(Collectors.toList());
        stats.setProductCategoryDistribution(categoryList);

        List<News> topNews = newsRepository.findTopByViewCount(PageRequest.of(0, 10));
        List<Map<String, Object>> newsTop = topNews.stream().map(news -> {
            Map<String, Object> item = new HashMap<>();
            item.put("id", news.getId());
            item.put("title", news.getTitle());
            item.put("viewCount", news.getViewCount());
            return item;
        }).collect(Collectors.toList());
        stats.setNewsViewTop(newsTop);

        return stats;
    }

    public PageResult<SysUser> listUsers(String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        String kw = keyword == null ? "" : keyword;
        Page<SysUser> result = sysUserRepository.findByUsernameContainingIgnoreCaseOrNicknameContainingIgnoreCase(kw, kw, pageRequest);
        result.getContent().forEach(u -> u.setPassword(null));
        return new PageResult<>(result.getTotalElements(), page, size, result.getContent());
    }

    public SysUser getUser(Long id) {
        SysUser user = sysUserRepository.findById(id).orElseThrow(() -> new BusinessException("用户不存在"));
        user.setPassword(null);
        return user;
    }

    @Transactional
    public SysUser saveUser(SysUser user) {
        if (user.getId() == null) {
            if (sysUserRepository.existsByUsername(user.getUsername())) {
                throw new BusinessException("用户名已存在");
            }
            if (!StringUtils.hasText(user.getPassword())) {
                user.setPassword(Md5Util.md5("123456"));
            } else if (user.getPassword().length() != 32) {
                user.setPassword(Md5Util.md5(user.getPassword()));
            }
        } else {
            SysUser existing = sysUserRepository.findById(user.getId())
                    .orElseThrow(() -> new BusinessException("用户不存在"));
            if (StringUtils.hasText(user.getPassword()) && user.getPassword().length() != 32) {
                existing.setPassword(Md5Util.md5(user.getPassword()));
            }
            existing.setNickname(user.getNickname());
            existing.setAvatar(user.getAvatar());
            existing.setPhone(user.getPhone());
            existing.setEmail(user.getEmail());
            existing.setRole(user.getRole());
            existing.setStatus(user.getStatus());
            user = existing;
        }
        SysUser saved = sysUserRepository.save(user);
        saved.setPassword(null);
        return saved;
    }

    @Transactional
    public void deleteUser(Long id) {
        sysUserRepository.deleteById(id);
    }

    public PageResult<Carousel> listCarousels(String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "sortOrder"));
        String kw = keyword == null ? "" : keyword;
        Page<Carousel> result = carouselRepository.findByTitleContainingIgnoreCase(kw, pageRequest);
        return new PageResult<>(result.getTotalElements(), page, size, result.getContent());
    }

    public Carousel getCarousel(Long id) {
        return carouselRepository.findById(id).orElseThrow(() -> new BusinessException("轮播图不存在"));
    }

    @Transactional
    public Carousel saveCarousel(Carousel carousel) {
        if (carousel.getId() != null && carousel.getCreatedAt() == null) {
            Carousel existing = getCarousel(carousel.getId());
            carousel.setCreatedAt(existing.getCreatedAt());
        }
        return carouselRepository.save(carousel);
    }

    @Transactional
    public void deleteCarousel(Long id) {
        carouselRepository.deleteById(id);
    }

    public PageResult<News> listNews(String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "publishTime"));
        String kw = keyword == null ? "" : keyword;
        Page<News> result = newsRepository.findByTitleContainingIgnoreCase(kw, pageRequest);
        return new PageResult<>(result.getTotalElements(), page, size, result.getContent());
    }

    public News getNews(Long id) {
        return newsRepository.findById(id).orElseThrow(() -> new BusinessException("新闻不存在"));
    }

    @Transactional
    public News saveNews(News news) {
        if (news.getId() != null) {
            News existing = newsRepository.findById(news.getId())
                    .orElseThrow(() -> new BusinessException("新闻不存在"));
            // 阅读量只由门户详情累加，后台保存时不覆盖
            news.setViewCount(existing.getViewCount());
            if (news.getCreatedAt() == null) {
                news.setCreatedAt(existing.getCreatedAt());
            }
            if (news.getPublishTime() == null) {
                news.setPublishTime(existing.getPublishTime());
            }
            if (news.getStatus() == null) {
                news.setStatus(existing.getStatus());
            }
        }
        return newsRepository.save(news);
    }

    @Transactional
    public void deleteNews(Long id) {
        newsRepository.deleteById(id);
    }

    public PageResult<Notice> listNotices(String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        String kw = keyword == null ? "" : keyword;
        Page<Notice> result = noticeRepository.findByTitleContainingIgnoreCase(kw, pageRequest);
        return new PageResult<>(result.getTotalElements(), page, size, result.getContent());
    }

    public Notice getNotice(Long id) {
        return noticeRepository.findById(id).orElseThrow(() -> new BusinessException("公告不存在"));
    }

    @Transactional
    public Notice saveNotice(Notice notice) {
        if (notice.getId() != null && notice.getCreatedAt() == null) {
            Notice existing = getNotice(notice.getId());
            notice.setCreatedAt(existing.getCreatedAt());
        }
        return noticeRepository.save(notice);
    }

    @Transactional
    public void deleteNotice(Long id) {
        noticeRepository.deleteById(id);
    }

    public PageResult<Product> listProducts(String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "sortOrder"));
        String kw = keyword == null ? "" : keyword;
        Page<Product> result = productRepository.findByNameContainingIgnoreCase(kw, pageRequest);
        return new PageResult<>(result.getTotalElements(), page, size, result.getContent());
    }

    public Product getProduct(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new BusinessException("产品不存在"));
    }

    @Transactional
    public Product saveProduct(Product product) {
        if (product.getId() != null && product.getCreatedAt() == null) {
            Product existing = getProduct(product.getId());
            product.setCreatedAt(existing.getCreatedAt());
        }
        return productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public PageResult<Technology> listTechnologies(String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "sortOrder"));
        String kw = keyword == null ? "" : keyword;
        Page<Technology> result = technologyRepository.findByTitleContainingIgnoreCase(kw, pageRequest);
        return new PageResult<>(result.getTotalElements(), page, size, result.getContent());
    }

    public Technology getTechnology(Long id) {
        return technologyRepository.findById(id).orElseThrow(() -> new BusinessException("技术不存在"));
    }

    @Transactional
    public Technology saveTechnology(Technology technology) {
        if (technology.getId() != null && technology.getCreatedAt() == null) {
            Technology existing = getTechnology(technology.getId());
            technology.setCreatedAt(existing.getCreatedAt());
        }
        return technologyRepository.save(technology);
    }

    @Transactional
    public void deleteTechnology(Long id) {
        technologyRepository.deleteById(id);
    }

    public PageResult<Honor> listHonors(String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "sortOrder"));
        String kw = keyword == null ? "" : keyword;
        Page<Honor> result = honorRepository.findByTitleContainingIgnoreCase(kw, pageRequest);
        return new PageResult<>(result.getTotalElements(), page, size, result.getContent());
    }

    public Honor getHonor(Long id) {
        return honorRepository.findById(id).orElseThrow(() -> new BusinessException("荣誉不存在"));
    }

    @Transactional
    public Honor saveHonor(Honor honor) {
        if (honor.getId() != null) {
            Honor existing = getHonor(honor.getId());
            if (honor.getCreatedAt() == null) {
                honor.setCreatedAt(existing.getCreatedAt());
            }
        }
        return honorRepository.save(honor);
    }

    @Transactional
    public void deleteHonor(Long id) {
        honorRepository.deleteById(id);
    }

    public PageResult<JobPosition> listJobs(String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "sortOrder").and(Sort.by(Sort.Direction.DESC, "id")));
        String kw = keyword == null ? "" : keyword;
        Page<JobPosition> result = jobPositionRepository.findByTitleContainingIgnoreCase(kw, pageRequest);
        return new PageResult<>(result.getTotalElements(), page, size, result.getContent());
    }

    public JobPosition getJob(Long id) {
        return jobPositionRepository.findById(id).orElseThrow(() -> new BusinessException("岗位不存在"));
    }

    @Transactional
    public JobPosition saveJob(JobPosition job) {
        if (job.getId() != null && job.getCreatedAt() == null) {
            JobPosition existing = getJob(job.getId());
            job.setCreatedAt(existing.getCreatedAt());
        }
        return jobPositionRepository.save(job);
    }

    @Transactional
    public void deleteJob(Long id) {
        jobPositionRepository.deleteById(id);
    }

    public PageResult<CaseStudy> listCases(String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        String kw = keyword == null ? "" : keyword;
        Page<CaseStudy> result = caseStudyRepository.findByTitleContainingIgnoreCase(kw, pageRequest);
        return new PageResult<>(result.getTotalElements(), page, size, result.getContent());
    }

    public CaseStudy getCase(Long id) {
        return caseStudyRepository.findById(id).orElseThrow(() -> new BusinessException("案例不存在"));
    }

    @Transactional
    public CaseStudy saveCase(CaseStudy caseStudy) {
        if (caseStudy.getId() != null && caseStudy.getCreatedAt() == null) {
            CaseStudy existing = getCase(caseStudy.getId());
            caseStudy.setCreatedAt(existing.getCreatedAt());
        }
        return caseStudyRepository.save(caseStudy);
    }

    @Transactional
    public void deleteCase(Long id) {
        caseStudyRepository.deleteById(id);
    }

    public PageResult<Consultation> listConsultations(String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        String kw = keyword == null ? "" : keyword;
        Page<Consultation> result = consultationRepository.findByContactNameContainingIgnoreCaseOrPhoneContainingOrCompanyContaining(
                kw, kw, kw, pageRequest);
        return new PageResult<>(result.getTotalElements(), page, size, result.getContent());
    }

    public Consultation getConsultation(Long id) {
        return consultationRepository.findById(id).orElseThrow(() -> new BusinessException("咨询不存在"));
    }

    @Transactional
    public Consultation saveConsultation(Consultation consultation) {
        if (consultation.getId() != null) {
            Consultation existing = consultationRepository.findById(consultation.getId())
                    .orElseThrow(() -> new BusinessException("咨询不存在"));
            existing.setStatus(consultation.getStatus());
            existing.setReply(consultation.getReply());
            return consultationRepository.save(existing);
        }
        return consultationRepository.save(consultation);
    }

    @Transactional
    public void deleteConsultation(Long id) {
        consultationRepository.deleteById(id);
    }

    public PageResult<Feedback> listFeedbacks(String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        String kw = keyword == null ? "" : keyword;
        Page<Feedback> result = feedbackRepository.findByContentContainingIgnoreCase(kw, pageRequest);
        return new PageResult<>(result.getTotalElements(), page, size, result.getContent());
    }

    public Feedback getFeedback(Long id) {
        return feedbackRepository.findById(id).orElseThrow(() -> new BusinessException("反馈不存在"));
    }

    @Transactional
    public Feedback saveFeedback(Feedback feedback) {
        if (feedback.getId() != null) {
            Feedback existing = feedbackRepository.findById(feedback.getId())
                    .orElseThrow(() -> new BusinessException("反馈不存在"));
            existing.setStatus(feedback.getStatus());
            existing.setReply(feedback.getReply());
            return feedbackRepository.save(existing);
        }
        return feedbackRepository.save(feedback);
    }

    @Transactional
    public void deleteFeedback(Long id) {
        feedbackRepository.deleteById(id);
    }

    public PageResult<SysConfig> listConfigs(String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));
        String kw = keyword == null ? "" : keyword;
        Page<SysConfig> result = sysConfigRepository.findByConfigKeyContainingIgnoreCaseOrRemarkContainingIgnoreCase(kw, kw, pageRequest);
        return new PageResult<>(result.getTotalElements(), page, size, result.getContent());
    }

    public SysConfig getConfig(Long id) {
        return sysConfigRepository.findById(id).orElseThrow(() -> new BusinessException("配置不存在"));
    }

    @Transactional
    public SysConfig saveConfig(SysConfig config) {
        return sysConfigRepository.save(config);
    }

    @Transactional
    public void deleteConfig(Long id) {
        sysConfigRepository.deleteById(id);
    }

    public PageResult<OperateLog> listOperateLogs(String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        String kw = keyword == null ? "" : keyword;
        Page<OperateLog> result = operateLogRepository.findByUsernameContainingIgnoreCaseOrActionContainingIgnoreCase(kw, kw, pageRequest);
        return new PageResult<>(result.getTotalElements(), page, size, result.getContent());
    }

    @Transactional
    public void deleteOperateLog(Long id) {
        operateLogRepository.deleteById(id);
    }

    @Transactional
    public OperateLog saveOperateLog(OperateLog log) {
        return operateLogRepository.save(log);
    }

    public PageResult<PortalVisitLog> listVisitLogs(String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        String kw = keyword == null ? "" : keyword.trim();
        Page<PortalVisitLog> result;
        if (!StringUtils.hasText(kw)) {
            result = portalVisitLogRepository.findAll(pageRequest);
        } else {
            result = portalVisitLogRepository
                    .findByPathContainingIgnoreCaseOrIpContainingIgnoreCaseOrPageTitleContainingIgnoreCase(
                            kw, kw, kw, pageRequest);
        }
        return new PageResult<>(result.getTotalElements(), page, size, result.getContent());
    }

    @Transactional
    public void deleteVisitLog(Long id) {
        portalVisitLogRepository.deleteById(id);
    }

    @Transactional
    public void deleteAllVisitLogs() {
        portalVisitLogRepository.deleteAllInBatch();
    }
}
