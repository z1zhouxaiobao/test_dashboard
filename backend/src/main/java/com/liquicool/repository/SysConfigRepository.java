package com.liquicool.repository;

import com.liquicool.entity.SysConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SysConfigRepository extends JpaRepository<SysConfig, Long> {

    Optional<SysConfig> findByConfigKey(String configKey);

    Page<SysConfig> findByConfigKeyContainingIgnoreCaseOrRemarkContainingIgnoreCase(
            String configKey, String remark, Pageable pageable);
}
