package com.gonzalovega.clientmanagement.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "client_resource")
public class ClientResourceModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENT_ID", nullable=false)
    private ClientModel clientId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RESOURCE_ID", nullable=false)
    private ResourceModel resourceId;

    @Column(name = "PERSONALIZED")
    private Boolean personalized = false;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    @Column(name = "ACTIVE", nullable = false)
    private Boolean active = true;

    @Version
    @Column(name = "VERSION_LOCK", nullable = false)
    private Integer versionLock = 0;


}