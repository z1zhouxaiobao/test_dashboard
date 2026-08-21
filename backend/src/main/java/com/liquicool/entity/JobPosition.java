package com.liquicool.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "job_position")
public class JobPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String title;

    @Column(length = 200)
    private String titleTw;

    @Column(length = 200)
    private String titleEn;

    /** 列表摘要（始终展示） */
    @Column(length = 500)
    private String summary;

    @Column(length = 500)
    private String summaryTw;

    @Column(length = 500)
    private String summaryEn;

    /** 岗位职责（展开后展示，可较长） */
    @Column(columnDefinition = "TEXT")
    private String responsibilities;

    @Column(columnDefinition = "TEXT")
    private String responsibilitiesTw;

    @Column(columnDefinition = "TEXT")
    private String responsibilitiesEn;

    /** 任职要求（展开后展示，多行） */
    @Column(columnDefinition = "TEXT")
    private String requirements;

    @Column(columnDefinition = "TEXT")
    private String requirementsTw;

    @Column(columnDefinition = "TEXT")
    private String requirementsEn;

    @Column(length = 100)
    private String location;

    @Column(length = 100)
    private String locationTw;

    @Column(length = 100)
    private String locationEn;

    /** 1=发布 0=下架 */
    private Integer status;

    private Integer sortOrder;

    @Column(updatable = false)
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
        if (this.sortOrder == null) {
            this.sortOrder = 0;
        }
        if (this.status == null) {
            this.status = 1;
        }
    }
}
