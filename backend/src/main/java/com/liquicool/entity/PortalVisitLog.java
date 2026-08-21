package com.liquicool.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "portal_visit_log")
public class PortalVisitLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String path;

    @Column(length = 200)
    private String pageTitle;

    @Column(length = 500)
    private String referer;

    @Column(length = 64)
    private String ip;

    @Column(length = 500)
    private String userAgent;

    @Column(length = 20)
    private String locale;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
