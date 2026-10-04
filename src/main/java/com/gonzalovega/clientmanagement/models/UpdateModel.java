package com.gonzalovega.clientmanagement.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "update_version",
        uniqueConstraints = @UniqueConstraint(columnNames = {"CLIENT_ID", "VERSION_ID"}))
public class UpdateModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENT_ID", nullable = false)
    private ClientModel clientId;

    @ManyToOne
    @JoinColumn(name = "VERSION_ID", nullable = false)
    private VersionModel versionId;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private UsersModel userId;

    @Version
    @Column(name = "VERSION_LOCK", nullable = false)
    private Integer versionLock = 0;


    @Column(name = "LAUNCH_DATE", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate launchDate;

    @Column(name = "FUTURE_DATE")
    private LocalDateTime futureDate;

    @Column(name = "TERMINATION_DATE")
    private LocalDate terminationDate;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

}
