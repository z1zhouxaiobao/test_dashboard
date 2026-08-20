package com.liquicool.repository;

import com.liquicool.entity.NavMenu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NavMenuRepository extends JpaRepository<NavMenu, Long> {

    List<NavMenu> findByStatusOrderBySortOrderAscIdAsc(Integer status);

    List<NavMenu> findByModuleCodeAndStatusOrderBySortOrderAscIdAsc(String moduleCode, Integer status);

    List<NavMenu> findByParentIdAndStatusOrderBySortOrderAscIdAsc(Long parentId, Integer status);

    List<NavMenu> findByLevelNoAndStatusOrderBySortOrderAscIdAsc(Integer levelNo, Integer status);

    Optional<NavMenu> findByCode(String code);

    Page<NavMenu> findByNameZhContainingIgnoreCaseOrCodeContainingIgnoreCase(
            String nameZh, String code, Pageable pageable);

    List<NavMenu> findAllByOrderBySortOrderAscIdAsc();
}
