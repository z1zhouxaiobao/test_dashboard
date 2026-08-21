package com.liquicool.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "honor")
public class Honor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 简体中文标题（默认） */
    @Column(length = 200)
    private String title;

    @Column(length = 200)
    private String titleTw;

    @Column(length = 200)
    private String titleEn;

    @Column(length = 500)
    private String coverUrl;

    @Column(length = 20)
    private String awardYear;

    @Column(length = 200)
    private String issuer;

    @Column(length = 200)
    private String issuerTw;

    @Column(length = 200)
    private String issuerEn;

    @Column(length = 500)
    private String summary;

    @Column(length = 500)
    private String summaryTw;

    @Column(length = 500)
    private String summaryEn;

    private Integer sortOrder;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.sortOrder == null) {
            this.sortOrder = 0;
        }
    }
}
