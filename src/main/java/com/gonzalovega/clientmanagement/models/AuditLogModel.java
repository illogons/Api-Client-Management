package com.gonzalovega.clientmanagement.models;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "audit_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLogModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "HTTP_METHOD", nullable = false)
    private String httpMethod;

    @Column(name = "METHOD", nullable = false)
    private String method;

    @Column(name = "ENTITY_NAME", nullable = false)
    private String entityName;

    @Column(name = "ENDPOINT", nullable = false)
    private String endpoint;

    @CreatedBy
    @Column(name = "CREATED_BY", updatable = false)
    private String username;

    @Column(name = "EXECUTION_TIME", nullable = false)
    private Long executionTime;

    @Column(columnDefinition = "TEXT")
    private String details;

    @Column(name = "TIMESTAMP", nullable = false)
    private LocalDateTime timestamp;
}
