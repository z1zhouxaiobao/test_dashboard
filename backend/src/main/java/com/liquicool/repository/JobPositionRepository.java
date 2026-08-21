package com.liquicool.repository;

import com.liquicool.entity.JobPosition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobPositionRepository extends JpaRepository<JobPosition, Long> {

    Page<JobPosition> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);

    List<JobPosition> findByStatusOrderBySortOrderAscIdAsc(Integer status);
}
