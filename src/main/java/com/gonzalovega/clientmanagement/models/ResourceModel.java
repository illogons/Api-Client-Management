package com.gonzalovega.clientmanagement.models;

import jakarta.persistence.*;
import lombok.*;
import com.gonzalovega.clientmanagement.ResourceType;

import java.time.LocalDate;

@Entity
@Table(name = "resource", uniqueConstraints = {@UniqueConstraint(columnNames = "NAME")})
@Getter @Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ResourceModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "ID")
    private Integer id;

    @Column(name="NAME",nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE_APP", nullable = false)
    private ResourceType typeApp;

    @Column(name = "DESCRIPTION", columnDefinition = "TEXT")
    private String description;

    @Column(name = "LAUNCH_DATE")
    private LocalDate launchDate;

    @Column(name="ACTIVE",nullable = false)
    private boolean active = true;

    @Version
    @Column(name = "VERSION_LOCK", nullable = false)
    private Integer versionLock = 0;

}