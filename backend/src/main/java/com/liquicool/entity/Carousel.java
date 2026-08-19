package com.liquicool.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "carousel")
public class Carousel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String title;

    @Column(length = 500)
    private String subtitle;

    @Column(length = 500)
    private String imageUrl;

    @Column(length = 500)
    private String linkUrl;

    private Integer sortOrder;

    private Integer status;

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

    public Boolean getEnabled() {
        return this.status != null && this.status == 1;
    }

    public void setEnabled(Boolean enabled) {
        this.status = Boolean.TRUE.equals(enabled) ? 1 : 0;
    }
}
