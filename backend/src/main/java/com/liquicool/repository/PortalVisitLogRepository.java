package com.liquicool.repository;

import com.liquicool.entity.PortalVisitLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortalVisitLogRepository extends JpaRepository<PortalVisitLog, Long> {

    Page<PortalVisitLog> findByPathContainingIgnoreCaseOrIpContainingIgnoreCaseOrPageTitleContainingIgnoreCase(
            String path, String ip, String pageTitle, Pageable pageable);
}
