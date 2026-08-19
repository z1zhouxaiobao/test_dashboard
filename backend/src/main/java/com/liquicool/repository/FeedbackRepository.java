package com.liquicool.repository;

import com.liquicool.entity.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    Page<Feedback> findByUserId(Long userId, Pageable pageable);

    Page<Feedback> findByContentContainingIgnoreCase(String keyword, Pageable pageable);
}
