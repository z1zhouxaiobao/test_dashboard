package com.liquicool.entity;

import com.liquicool.enums.FavoriteTargetType;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "favorite", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"userId", "targetType", "targetId"})
})
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private FavoriteTargetType targetType;

    private Long targetId;

    private LocalDateTime createdAt;

    @Transient
    private String username;

    @Transient
    private String targetName;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
