package com.liquicool.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "sys_config")
public class SysConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String configKey;

    @Column(columnDefinition = "TEXT")
    private String configValue;

    @Column(columnDefinition = "TEXT")
    private String remark;

    private java.time.LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.updatedAt = java.time.LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = java.time.LocalDateTime.now();
    }

    public String getDescription() {
        return this.remark;
    }

    public void setDescription(String description) {
        this.remark = description;
    }
}
