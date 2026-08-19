package com.liquicool.repository;

import com.liquicool.entity.SysUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SysUserRepository extends JpaRepository<SysUser, Long> {

    Optional<SysUser> findByUsername(String username);

    boolean existsByUsername(String username);

    Page<SysUser> findByUsernameContainingIgnoreCaseOrNicknameContainingIgnoreCase(
            String username, String nickname, Pageable pageable);
}
