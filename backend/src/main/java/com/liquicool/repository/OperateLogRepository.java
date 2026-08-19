package com.liquicool.repository;

import com.liquicool.entity.OperateLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperateLogRepository extends JpaRepository<OperateLog, Long> {

    Page<OperateLog> findByUsernameContainingIgnoreCaseOrActionContainingIgnoreCase(
            String username, String action, Pageable pageable);
}
