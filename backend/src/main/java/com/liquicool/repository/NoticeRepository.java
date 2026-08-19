package com.liquicool.repository;

import com.liquicool.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    Page<Notice> findByStatusAndTitleContainingIgnoreCase(Integer status, String keyword, Pageable pageable);

    Page<Notice> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);
}
