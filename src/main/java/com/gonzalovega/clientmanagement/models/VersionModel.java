package com.gonzalovega.clientmanagement.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "version",uniqueConstraints = {@UniqueConstraint(columnNames = "VERSION")})
@Getter
@Setter
public class VersionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "VERSION", nullable = false, unique = true)
    private String version;

    @Column(name = "LAUNCH_DATE", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate launchDate;

    @Column(name = "ACTIVE", nullable = false)
    private Boolean active = true;

    @Version
    @Column(name = "VERSION_LOCK", nullable = false)
    private Integer versionLock = 0;

    @Lob
    @Column(name = "CHANGELOG", columnDefinition = "TEXT")
    private String changelog;

    @Lob
    @Column(name = "BREAKING_CHANGES", columnDefinition = "TEXT")
    private String breakingChanges;

    @PrePersist
    protected void onCreate() {
        if (this.launchDate == null) {
            this.launchDate = LocalDate.now();
        }
    }

    @OneToMany(mappedBy = "versionId", fetch = FetchType.LAZY)
    private List<UpdateModel> updateModels;
}