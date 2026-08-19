package com.liquicool.repository;

import com.liquicool.entity.CaseStudy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaseStudyRepository extends JpaRepository<CaseStudy, Long> {

    Page<CaseStudy> findByStatusAndTitleContainingIgnoreCase(Integer status, String keyword, Pageable pageable);

    Page<CaseStudy> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);
}
