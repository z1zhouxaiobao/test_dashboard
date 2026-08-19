package com.liquicool.entity;

import com.liquicool.enums.ProductCategory;
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

    @Column(length = 500)
    private String coverUrl;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private ProductCategory category;

    @Column(length = 500)
    private String summary;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "TEXT")
    private String specs;

    @Column(length = 100)
    private String priceHint;

    private Integer status;

    private Integer sortOrder;

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
