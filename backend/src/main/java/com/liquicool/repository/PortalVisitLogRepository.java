package com.liquicool.repository;

import com.liquicool.entity.PortalVisitLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface PortalVisitLogRepository extends JpaRepository<PortalVisitLog, Long> {

    Page<PortalVisitLog> findByPathContainingIgnoreCaseOrIpContainingIgnoreCaseOrPageTitleContainingIgnoreCase(
            String path, String ip, String pageTitle, Pageable pageable);

    boolean existsByIpAndCreatedAtGreaterThanEqual(String ip, LocalDateTime startOfDay);
}
