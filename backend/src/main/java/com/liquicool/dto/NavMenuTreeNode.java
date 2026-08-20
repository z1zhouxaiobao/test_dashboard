package com.liquicool.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class NavMenuTreeNode {
    private Long id;
    private Long parentId;
    private String moduleCode;
    private Integer levelNo;
    private String nameZh;
    private String nameTw;
    private String nameEn;
    private String code;
    private String linkPath;
    private Integer sortOrder;
    private Integer status;
    private List<NavMenuTreeNode> children = new ArrayList<>();
}
