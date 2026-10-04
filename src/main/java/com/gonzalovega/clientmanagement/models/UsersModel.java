package com.gonzalovega.clientmanagement.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
@Table( name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "NAME"))
public class UsersModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column( name= "ACTIVE")
    private Boolean active = true;

    @Version
    @Column(name= "VERSION_LOCK")
    private Integer versionLock = 0;

    @OneToMany(mappedBy = "userId", fetch = FetchType.LAZY)
    private List<UpdateModel> updates;
}
