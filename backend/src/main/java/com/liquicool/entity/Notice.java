package com.liquicool.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "notice")
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String title;

    @Column(length = 200)
    private String titleTw;

    @Column(length = 200)
    private String titleEn;

    @Column(length = 50)
    private String type;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "TEXT")
    private String contentTw;

    @Column(columnDefinition = "TEXT")
    private String contentEn;

    private Integer status;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = 1;
        }
    }

    public Boolean getEnabled() {
        return this.status != null && this.status == 1;
    }

    public void setEnabled(Boolean enabled) {
        this.status = Boolean.TRUE.equals(enabled) ? 1 : 0;
    }
}
