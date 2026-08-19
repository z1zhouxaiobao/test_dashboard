package com.liquicool.repository;

import com.liquicool.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {

    Page<News> findByStatusAndTitleContainingIgnoreCase(Integer status, String keyword, Pageable pageable);

    Page<News> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);

    @Query("SELECT n FROM News n WHERE n.status = 1 ORDER BY n.viewCount DESC")
    List<News> findTopByViewCount(org.springframework.data.domain.Pageable pageable);
}
