package com.liquicool.entity;

import com.liquicool.enums.ConsultationStatus;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "consultation")
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long productId;

    @Column(length = 50)
    private String contactName;

    @Column(length = 20)
    private String phone;

    @Column(length = 100)
    private String company;

    @Column(length = 100)
    private String email;

    @Column(length = 50)
    private String needType;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ConsultationStatus status;

    @Column(columnDefinition = "TEXT")
    private String reply;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.status == null) {
            this.status = ConsultationStatus.待处理;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public String getTitle() {
        return this.needType;
    }

    public String getUsername() {
        return this.contactName;
    }
}
