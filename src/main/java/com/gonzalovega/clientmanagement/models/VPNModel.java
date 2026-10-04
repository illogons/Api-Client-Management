package com.gonzalovega.clientmanagement.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "VPN")
public class VPNModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column( name = "NAME")
    private String name;

    @Column( name = "ACTIVE")
    private Boolean active = true;

    @Column(name = "URL")
    private String url;

    @Version
    @Column( name = "VERSION_LOCK")
    private Integer versionLock = 0;

    @OneToMany(mappedBy = "vpnId", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ClientVPNModel> clientVpn;




}
