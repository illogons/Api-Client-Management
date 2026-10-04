package com.gonzalovega.clientmanagement.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;



@Entity
@Table(name = "connection")
@Getter
@Setter
public class ConnectionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "CLIENT_ID", nullable = false)
    private ClientModel clientId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TYPE_CONNECTION_ID")
    private TypeConnectionModel typeConnectionId;

    @Column(name = "DM_UUID", length = 80)
    private String dmUuid;

    @Size(max = 500, message = "Details must not exceed 500 characters")
    @Column(name = "DETAILS", nullable = false)
    private String details;

    @Column(name = "ACTIVE",nullable = false)
    private Boolean active = true;

    @Version
    @Column(name = "VERSION_LOCK", nullable = false)
    private Integer versionLock = 0;
}
