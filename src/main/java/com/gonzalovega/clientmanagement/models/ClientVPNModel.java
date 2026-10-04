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
@Table(name = "client_VPN")
public class ClientVPNModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column( name = "DM_UUID")
    private String dm_uuid;

    @Column( name = "ACTIVE")
    private Boolean active = true;

    @Column( name = "PORT")
    private Integer port;

    @Column(name = "URL")
    private String url;

    @Column( name = "USER")
    private String user;

    @Column(name = "PASSWORD")
    private String password;

    @Lob
    @Column(name = "DESCRIPTION")
    private String description;

    @Version
    @Column( name = "VERSION_LOCK", nullable = false)
    private Integer versionLock = 0;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "CLIENT_ID", nullable = false, unique = true)
    private ClientModel clientId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VPN_ID")
    private VPNModel vpnId;



}
