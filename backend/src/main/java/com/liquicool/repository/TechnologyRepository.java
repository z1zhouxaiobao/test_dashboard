package com.liquicool.repository;

import com.liquicool.entity.Technology;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TechnologyRepository extends JpaRepository<Technology, Long> {

    Page<Technology> findByStatusAndTitleContainingIgnoreCase(Integer status, String keyword, Pageable pageable);

    Page<Technology> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);
}
