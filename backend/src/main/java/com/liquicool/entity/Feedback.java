package com.liquicool.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(length = 50)
    private String contactName;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(length = 100)
    private String contact;

    @Column(length = 20)
    private String status;

    @Column(columnDefinition = "TEXT")
    private String reply;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = "待处理";
        }
    }

    public String getName() {
        return this.contactName;
    }

    public String getPhone() {
        return this.contact;
    }
}
