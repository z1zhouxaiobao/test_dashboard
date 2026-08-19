package com.liquicool.repository;

import com.liquicool.entity.Carousel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarouselRepository extends JpaRepository<Carousel, Long> {

    Page<Carousel> findByStatusAndTitleContainingIgnoreCase(Integer status, String keyword, Pageable pageable);

    Page<Carousel> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);

    Page<Carousel> findByStatus(Integer status, Pageable pageable);
}
