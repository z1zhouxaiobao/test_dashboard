package com.liquicool.repository;

import com.liquicool.entity.Favorite;
import com.liquicool.enums.FavoriteTargetType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Page<Favorite> findByUserId(Long userId, Pageable pageable);

    Page<Favorite> findByUserIdAndTargetType(Long userId, FavoriteTargetType targetType, Pageable pageable);

    Optional<Favorite> findByUserIdAndTargetTypeAndTargetId(Long userId, FavoriteTargetType targetType, Long targetId);

    void deleteByUserIdAndTargetTypeAndTargetId(Long userId, FavoriteTargetType targetType, Long targetId);
}
