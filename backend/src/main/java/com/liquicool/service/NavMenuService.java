package com.liquicool.service;

import com.liquicool.common.PageResult;
import com.liquicool.dto.NavMenuTreeNode;
import com.liquicool.entity.NavMenu;
import com.liquicool.exception.BusinessException;
import com.liquicool.repository.NavMenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NavMenuService {

    @Autowired
    private NavMenuRepository navMenuRepository;

    public List<NavMenuTreeNode> getPortalTree() {
        List<NavMenu> all = navMenuRepository.findByStatusOrderBySortOrderAscIdAsc(1);
        return buildTree(all);
    }

    public List<NavMenuTreeNode> getPortalTreeByModule(String moduleCode) {
        List<NavMenu> all = navMenuRepository.findByModuleCodeAndStatusOrderBySortOrderAscIdAsc(moduleCode, 1);
        return buildTree(all);
    }

    public PageResult<NavMenu> listAdmin(String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "sortOrder").and(Sort.by("id")));
        String kw = keyword == null ? "" : keyword.trim();
        Page<NavMenu> result;
        if (!StringUtils.hasText(kw)) {
            result = navMenuRepository.findAll(pageRequest);
        } else {
            result = navMenuRepository.findByNameZhContainingIgnoreCaseOrCodeContainingIgnoreCase(kw, kw, pageRequest);
        }
        return new PageResult<>(result.getTotalElements(), page, size, result.getContent());
    }

    public List<NavMenu> listAllFlat() {
        return navMenuRepository.findAllByOrderBySortOrderAscIdAsc();
    }

    public NavMenu getById(Long id) {
        return navMenuRepository.findById(id).orElseThrow(() -> new BusinessException("导航菜单不存在"));
    }

    public NavMenu save(NavMenu menu) {
        if (!StringUtils.hasText(menu.getNameZh())) {
            throw new BusinessException("中文名称不能为空");
        }
        if (!StringUtils.hasText(menu.getModuleCode())) {
            throw new BusinessException("所属模块不能为空");
        }
        if (menu.getLevelNo() == null) {
            menu.setLevelNo(menu.getParentId() == null ? 1 : 2);
        }
        if (menu.getId() != null) {
            NavMenu existing = getById(menu.getId());
            if (menu.getParentId() != null && menu.getParentId().equals(menu.getId())) {
                throw new BusinessException("父级不能选择自己");
            }
            existing.setParentId(menu.getParentId());
            existing.setModuleCode(menu.getModuleCode());
            existing.setLevelNo(menu.getLevelNo());
            existing.setNameZh(menu.getNameZh());
            existing.setNameTw(menu.getNameTw());
            existing.setNameEn(menu.getNameEn());
            existing.setCode(menu.getCode());
            existing.setLinkPath(menu.getLinkPath());
            existing.setSortOrder(menu.getSortOrder());
            existing.setStatus(menu.getStatus());
            return navMenuRepository.save(existing);
        }
        return navMenuRepository.save(menu);
    }

    public void delete(Long id) {
        List<NavMenu> children = navMenuRepository.findByParentIdAndStatusOrderBySortOrderAscIdAsc(id, 1);
        if (!children.isEmpty()) {
            // 也检查全部子节点
            List<NavMenu> anyChildren = navMenuRepository.findAll().stream()
                    .filter(m -> id.equals(m.getParentId()))
                    .collect(Collectors.toList());
            if (!anyChildren.isEmpty()) {
                throw new BusinessException("请先删除子菜单");
            }
        } else {
            List<NavMenu> anyChildren = navMenuRepository.findAll().stream()
                    .filter(m -> id.equals(m.getParentId()))
                    .collect(Collectors.toList());
            if (!anyChildren.isEmpty()) {
                throw new BusinessException("请先删除子菜单");
            }
        }
        navMenuRepository.deleteById(id);
    }

    private List<NavMenuTreeNode> buildTree(List<NavMenu> all) {
        Map<Long, NavMenuTreeNode> map = new HashMap<>();
        List<NavMenuTreeNode> roots = new ArrayList<>();
        for (NavMenu menu : all) {
            map.put(menu.getId(), toNode(menu));
        }
        for (NavMenu menu : all) {
            NavMenuTreeNode node = map.get(menu.getId());
            if (menu.getParentId() == null || !map.containsKey(menu.getParentId())) {
                roots.add(node);
            } else {
                map.get(menu.getParentId()).getChildren().add(node);
            }
        }
        return roots;
    }

    private NavMenuTreeNode toNode(NavMenu menu) {
        NavMenuTreeNode node = new NavMenuTreeNode();
        node.setId(menu.getId());
        node.setParentId(menu.getParentId());
        node.setModuleCode(menu.getModuleCode());
        node.setLevelNo(menu.getLevelNo());
        node.setNameZh(menu.getNameZh());
        node.setNameTw(menu.getNameTw());
        node.setNameEn(menu.getNameEn());
        node.setCode(menu.getCode());
        node.setLinkPath(menu.getLinkPath());
        node.setSortOrder(menu.getSortOrder());
        node.setStatus(menu.getStatus());
        return node;
    }
}
