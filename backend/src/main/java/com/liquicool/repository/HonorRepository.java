package com.liquicool.repository;

import com.liquicool.entity.Honor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HonorRepository extends JpaRepository<Honor, Long> {

    Page<Honor> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);
}
