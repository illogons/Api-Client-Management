package com.gonzalovega.clientmanagement.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table( name = "Responsible", uniqueConstraints = @UniqueConstraint(columnNames = "NAME"))
@Setter
public class ResponsibleModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( name = "ID")
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ACTIVE", nullable = false)
    private Boolean active = true;

    @Column(name = "VERSION_LOCK")
    @Version
    private Integer versionLock = 0;

    @OneToMany(mappedBy = "responsibleId", fetch = FetchType.LAZY)
    private List<ClientModel> clients;

}
