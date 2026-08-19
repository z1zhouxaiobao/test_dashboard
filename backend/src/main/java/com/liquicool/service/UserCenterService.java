package com.liquicool.service;

import com.liquicool.common.PageResult;
import com.liquicool.dto.ConsultationRequest;
import com.liquicool.dto.FavoriteRequest;
import com.liquicool.dto.FeedbackRequest;
import com.liquicool.dto.ProfileUpdateRequest;
import com.liquicool.entity.Consultation;
import com.liquicool.entity.Favorite;
import com.liquicool.entity.Feedback;
import com.liquicool.entity.SysUser;
import com.liquicool.enums.ConsultationStatus;
import com.liquicool.enums.FavoriteTargetType;
import com.liquicool.exception.BusinessException;
import com.liquicool.repository.ConsultationRepository;
import com.liquicool.repository.FavoriteRepository;
import com.liquicool.repository.FeedbackRepository;
import com.liquicool.repository.SysUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserCenterService {

    @Autowired
    private SysUserRepository sysUserRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private ConsultationRepository consultationRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Transactional
    public SysUser updateProfile(Long userId, ProfileUpdateRequest request) {
        SysUser user = sysUserRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        if (request.getNickname() != null) {
            user.setNickname(request.getNickname());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        return sysUserRepository.save(user);
    }

    @Transactional
    public SysUser updateAvatar(Long userId, String avatarUrl) {
        SysUser user = sysUserRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        user.setAvatar(avatarUrl);
        return sysUserRepository.save(user);
    }

    @Transactional
    public Favorite addFavorite(Long userId, FavoriteRequest request) {
        return favoriteRepository.findByUserIdAndTargetTypeAndTargetId(
                userId, request.getTargetType(), request.getTargetId()
        ).orElseGet(() -> {
            Favorite favorite = new Favorite();
            favorite.setUserId(userId);
            favorite.setTargetType(request.getTargetType());
            favorite.setTargetId(request.getTargetId());
            return favoriteRepository.save(favorite);
        });
    }

    @Transactional
    public void removeFavorite(Long userId, FavoriteTargetType targetType, Long targetId) {
        favoriteRepository.deleteByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId);
    }

    public PageResult<Favorite> listFavorites(Long userId, FavoriteTargetType targetType, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Favorite> result;
        if (targetType != null) {
            result = favoriteRepository.findByUserIdAndTargetType(userId, targetType, pageRequest);
        } else {
            result = favoriteRepository.findByUserId(userId, pageRequest);
        }
        return new PageResult<>(result.getTotalElements(), page, size, result.getContent());
    }

    @Transactional
    public Consultation submitConsultation(Long userId, ConsultationRequest request) {
        Consultation consultation = new Consultation();
        consultation.setUserId(userId);
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

    public PageResult<Consultation> listMyConsultations(Long userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Consultation> result = consultationRepository.findByUserId(userId, pageRequest);
        return new PageResult<>(result.getTotalElements(), page, size, result.getContent());
    }

    @Transactional
    public Feedback submitFeedback(Long userId, FeedbackRequest request) {
        Feedback feedback = new Feedback();
        feedback.setUserId(userId);
        feedback.setContent(request.getContent());
        feedback.setContact(request.getContact());
        feedback.setStatus("待处理");
        return feedbackRepository.save(feedback);
    }
}
