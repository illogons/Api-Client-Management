package com.gonzalovega.clientmanagement.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "operative_system", uniqueConstraints = {@UniqueConstraint(name = "Uk_name_version", columnNames = {"Name", "Version"})})
@Getter @Setter
public class OperativeSystemModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name="NAME", nullable = false)
    private String name;

    @Column(name = "VERSION")
    private String version;

    @Column(name = "ACTIVE", nullable = false)
    private Boolean active = true;

    @Version
    @Column(name = "VERSION_LOCK", nullable = false)
    private Integer versionLock = 0;

    @OneToMany(mappedBy = "operativeSystemId", fetch = FetchType.LAZY)
    private Set<ClientModel> clientModels;
}