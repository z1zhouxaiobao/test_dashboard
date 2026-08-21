package com.liquicool.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "nav_menu")
public class NavMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 父级ID，一级菜单为 null */
    private Long parentId;

    /**
     * 所属一级模块编码：
     * PRODUCTS / SOLUTIONS / NEWS / ABOUT / CONTACT
     */
    @Column(length = 40, nullable = false)
    private String moduleCode;

    /** 层级：1/2/3 */
    private Integer levelNo;

    @Column(length = 100, nullable = false)
    private String nameZh;

    @Column(length = 100)
    private String nameTw;

    @Column(length = 100)
    private String nameEn;

    /** 分类编码，产品/方案筛选用 */
    @Column(length = 80)
    private String code;

    /** 跳转路径，如 /portal/products?category=xxx */
    @Column(length = 255)
    private String linkPath;

    private Integer sortOrder;

    /** 1启用 0停用 */
    private Integer status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.status == null) {
            this.status = 1;
        }
        if (this.sortOrder == null) {
            this.sortOrder = 0;
        }
        if (this.levelNo == null) {
            this.levelNo = 1;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
