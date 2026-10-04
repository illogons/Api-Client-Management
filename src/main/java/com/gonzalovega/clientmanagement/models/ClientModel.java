package com.gonzalovega.clientmanagement.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "client")
public class ClientModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "JOIN_DATE")
    private LocalDate joinDate;

    @Column(name = "TERMINATION_DATE")
    private LocalDate terminationDate;

    @CreatedBy
    @Column(name = "CREATED_BY", updatable = false)
    private String createdBy;

    @Column(name = "ACTIVE",nullable = false)
    private Boolean active = true;

    @Version
    @Column(name = "VERSION_LOCK", nullable = false)
    private Integer versionLock = 0;

    @Lob
    @Column( name = "COMMENT", columnDefinition = "TEXT")
    private String comment;

    @Column( name = "SUPPORT", nullable = false)
    private Boolean support;

    @ManyToOne
    @JoinColumn( name = "PARTNER_ID")
    private PartnersModel partnerId;

    @ManyToOne
    @JoinColumn( name= "RESPONSIBLE_ID")
    private ResponsibleModel responsibleId;

    @OneToOne(mappedBy = "clientId", cascade = CascadeType.ALL)
    private ClientVPNModel clientVpn;

    @Column(name = "URL")
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name =  "DATABASE_ID", nullable=false)
    private DatabaseModel databaseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "OPERATIVE_SYSTEM_ID", nullable=false)
    private OperativeSystemModel operativeSystemId;

    @OneToMany(mappedBy = "clientId",cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<UpdateModel> updateModels;

    @OneToMany(mappedBy = "clientId", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ClientResourceModel> clientResourceModels;

    @OneToMany(mappedBy = "clientId", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ConnectionModel> connectionModels;


}
