package com.gonzalovega.clientmanagement.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "data_base", uniqueConstraints = {@UniqueConstraint(columnNames = "NAME")})
public class DatabaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @Column(name= "ACTIVE", nullable = false)
    private Boolean active = true;

    @Version
    @Column(name = "VERSION_LOCK", nullable = false)
    private Integer versionLock = 0;

    @OneToMany(mappedBy = "databaseId", fetch = FetchType.LAZY)
    private List<ClientModel> clients;
}
