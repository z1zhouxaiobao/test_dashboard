package com.liquicool.service;

import com.liquicool.common.PageResult;
import com.liquicool.dto.ConsultationRequest;
import com.liquicool.dto.FeedbackRequest;
import com.liquicool.dto.PortalOverviewResponse;
import com.liquicool.entity.*;
import com.liquicool.enums.ConsultationStatus;
import com.liquicool.exception.BusinessException;
import com.liquicool.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PortalService {

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
    private CaseStudyRepository caseStudyRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private ConsultationRepository consultationRepository;

    public PortalOverviewResponse getOverview() {
        PortalOverviewResponse response = new PortalOverviewResponse();
        response.setProductCount(productRepository.findAll().stream().filter(p -> p.getStatus() != null && p.getStatus() == 1).count());
        response.setCaseCount(caseStudyRepository.findAll().stream().filter(c -> c.getStatus() != null && c.getStatus() == 1).count());
        response.setNewsCount(newsRepository.findAll().stream().filter(n -> n.getStatus() != null && n.getStatus() == 1).count());
        response.setTechnologyCount(technologyRepository.findAll().stream().filter(t -> t.getStatus() != null && t.getStatus() == 1).count());
        response.setHonorCount(honorRepository.count());
        return response;
    }

    public PageResult<Carousel> listCarousels(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "sortOrder"));
        Page<Carousel> result = carouselRepository.findByStatus(1, pageRequest);
        return new PageResult<>(result.getTotalElements(), page, size, result.getContent());
    }

    public PageResult<News> listNews(String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "publishTime"));
        String kw = keyword == null ? "" : keyword;
        Page<News> result = newsRepository.findByStatusAndTitleContainingIgnoreCase(1, kw, pageRequest);
        return new PageResult<>(result.getTotalElements(), page, size, result.getContent());
    }

    @Transactional
    public News getNewsDetail(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new BusinessException("新闻不存在"));
        if (news.getStatus() == null || news.getStatus() != 1) {
            throw new BusinessException("新闻不存在或已下架");
        }
        news.setViewCount(news.getViewCount() == null ? 1 : news.getViewCount() + 1);
        return newsRepository.save(news);
    }

    public PageResult<Notice> listNotices(String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        String kw = keyword == null ? "" : keyword;
        Page<Notice> result = noticeRepository.findByStatusAndTitleContainingIgnoreCase(1, kw, pageRequest);
        return new PageResult<>(result.getTotalElements(), page, size, result.getContent());
    }

    public Notice getNoticeDetail(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new BusinessException("公告不存在"));
        if (notice.getStatus() == null || notice.getStatus() != 1) {
            throw new BusinessException("公告不存在或已下架");
        }
        return notice;
    }

    public PageResult<Product> listProducts(String keyword, String category, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "sortOrder"));
        String kw = keyword == null ? "" : keyword;
        Page<Product> result;
        if (category != null && !category.trim().isEmpty()) {
            String cat = category.trim();
            if (kw.isEmpty()) {
                result = productRepository.findByStatusAndCategory(1, cat, pageRequest);
            } else {
                result = productRepository.findByStatusAndCategoryAndNameContainingIgnoreCase(1, cat, kw, pageRequest);
            }
        } else {
            result = productRepository.findByStatusAndNameContainingIgnoreCase(1, kw, pageRequest);
        }
        return new PageResult<>(result.getTotalElements(), page, size, result.getContent());
    }

    public Product getProductDetail(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new BusinessException("产品不存在"));
        if (product.getStatus() == null || product.getStatus() != 1) {
            throw new BusinessException("产品不存在或已下架");
        }
        return product;
    }

    public PageResult<Technology> listTechnologies(String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "sortOrder"));
        String kw = keyword == null ? "" : keyword;
        Page<Technology> result = technologyRepository.findByStatusAndTitleContainingIgnoreCase(1, kw, pageRequest);
        return new PageResult<>(result.getTotalElements(), page, size, result.getContent());
    }

    public Technology getTechnologyDetail(Long id) {
        Technology technology = technologyRepository.findById(id)
                .orElseThrow(() -> new BusinessException("技术不存在"));
        if (technology.getStatus() == null || technology.getStatus() != 1) {
            throw new BusinessException("技术不存在或已下架");
        }
        return technology;
    }

    public PageResult<Honor> listHonors(String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "sortOrder"));
        String kw = keyword == null ? "" : keyword;
        Page<Honor> result = honorRepository.findByTitleContainingIgnoreCase(kw, pageRequest);
        return new PageResult<>(result.getTotalElements(), page, size, result.getContent());
    }

    public Honor getHonorDetail(Long id) {
        return honorRepository.findById(id)
                .orElseThrow(() -> new BusinessException("荣誉不存在"));
    }

    public PageResult<CaseStudy> listCases(String keyword, String industry, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        String kw = keyword == null ? "" : keyword;
        Page<CaseStudy> result;
        if (industry != null && !industry.trim().isEmpty()) {
            String ind = industry.trim();
            if (kw.isEmpty()) {
                result = caseStudyRepository.findByStatusAndIndustry(1, ind, pageRequest);
            } else {
                result = caseStudyRepository.findByStatusAndIndustryAndTitleContainingIgnoreCase(1, ind, kw, pageRequest);
            }
        } else {
            result = caseStudyRepository.findByStatusAndTitleContainingIgnoreCase(1, kw, pageRequest);
        }
        return new PageResult<>(result.getTotalElements(), page, size, result.getContent());
    }

    public CaseStudy getCaseDetail(Long id) {
        CaseStudy caseStudy = caseStudyRepository.findById(id)
                .orElseThrow(() -> new BusinessException("案例不存在"));
        if (caseStudy.getStatus() == null || caseStudy.getStatus() != 1) {
            throw new BusinessException("案例不存在或已下架");
        }
        return caseStudy;
    }

    @Transactional
    public Feedback submitFeedback(FeedbackRequest request) {
        Feedback feedback = new Feedback();
        feedback.setUserId(null);
        feedback.setContactName(request.getContactName());
        feedback.setContent(request.getContent());
        feedback.setContact(request.getContact());
        feedback.setStatus("待处理");
        return feedbackRepository.save(feedback);
    }

    @Transactional
    public Consultation submitConsultation(ConsultationRequest request) {
        Consultation consultation = new Consultation();
        consultation.setUserId(null);
        consultation.setProductId(request.getProductId());
        consultation.setContactName(request.getContactName());
        consultation.setPhone(request.getPhone());
        consultation.setCompany(request.getCompany());
        consultation.setEmail(request.getEmail());
        consultation.setNeedType(request.getNeedType());
        consultation.setContent(request.getContent());
        consultation.setStatus(ConsultationStatus.待处理);
        return consultationRepository.save(consultation);
    }
}
