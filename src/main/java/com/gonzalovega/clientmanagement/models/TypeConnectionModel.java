package com.gonzalovega.clientmanagement.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "type_connection", uniqueConstraints = {@UniqueConstraint(columnNames = "NAME_TYPE_CONECTION")})
public class TypeConnectionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NAME_TYPE_CONECTION", nullable = false, unique = true)
    private String name;

    @Column(name="ACTIVE", nullable = false)
    private Boolean active = true;

    @Version
    @Column(name = "VERSION_LOCK", nullable = false)
    private Integer versionLock = 0;
}
