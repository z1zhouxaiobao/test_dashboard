package com.liquicool.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "case_study")
public class CaseStudy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String title;

    @Column(length = 200)
    private String titleTw;

    @Column(length = 200)
    private String titleEn;

    @Column(length = 500)
    private String coverUrl;

    @Column(length = 100)
    private String clientName;

    @Column(length = 100)
    private String industry;

    @Column(length = 100)
    private String industryTw;

    @Column(length = 100)
    private String industryEn;

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

    private Integer status;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = 1;
        }
    }
}
