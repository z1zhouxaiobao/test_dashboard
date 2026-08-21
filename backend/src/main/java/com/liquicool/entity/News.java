package com.liquicool.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "news")
public class News {

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

    private Integer viewCount;

    private Integer status;

    private LocalDateTime publishTime;

    private LocalDateTime createdAt;

    @Transient
    public Boolean getPublished() {
        return this.status != null && this.status == 1;
    }

    public void setPublished(Boolean published) {
        this.status = Boolean.TRUE.equals(published) ? 1 : 0;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.viewCount == null) {
            this.viewCount = 0;
        }
        if (this.status == null) {
            this.status = 1;
        }
        if (this.publishTime == null) {
            this.publishTime = LocalDateTime.now();
        }
    }
}
