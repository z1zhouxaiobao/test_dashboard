package com.liquicool.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String name;

    @Column(length = 200)
    private String nameTw;

    @Column(length = 200)
    private String nameEn;

    @Column(length = 500)
    private String coverUrl;

    /** 对应导航三级分类 code 或名称 */
    @Column(length = 80)
    private String category;

    @Column(length = 500)
    private String summary;

    @Column(length = 500)
    private String summaryTw;

    @Column(length = 500)
    private String summaryEn;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "TEXT")
    private String contentTw;

    @Column(columnDefinition = "TEXT")
    private String contentEn;

    @Column(columnDefinition = "TEXT")
    private String specs;

    @Column(length = 100)
    private String priceHint;

    private Integer status;

    private Integer sortOrder;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = 1;
        }
        if (this.sortOrder == null) {
            this.sortOrder = 0;
        }
    }
}
