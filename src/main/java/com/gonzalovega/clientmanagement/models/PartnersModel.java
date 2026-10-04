package com.gonzalovega.clientmanagement.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Getter
@NoArgsConstructor
@Table( name = "Partners", uniqueConstraints = @UniqueConstraint(columnNames = "NAME"))
@Setter
@Entity
public class PartnersModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( name = "ID")
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ACTIVE", nullable = false)
    private Boolean active = true;

    @Column(name = "DM_UUID")
    private String dm_uuid;

    @Column(name = "VERSION_LOCK")
    @Version
    private Integer versionLock = 0;

    @OneToMany(mappedBy = "partnerId", fetch = FetchType.LAZY)
    private List<ClientModel> clients;

}
