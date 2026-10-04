package com.gonzalovega.clientmanagement.dto.ClientDto;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClientResponseDto {

    private Integer id;

    private String name;

    private String email;

    private String phoneNumber;

    private String address;

    private LocalDate joinDate;

    private String comment;

    private LocalDate terminationDate;

    private String createdBy;

    private Integer partnerId;

    private Boolean support;

    private String url;

    private Boolean active;

    private Integer versionLock;

    private Integer databaseId;

    private Integer responsibleId;

    private Integer operativeSystemId;

    private String databaseName;
    private String partnerName;
    private String OperativeSystemName;
    private String responsibleName;
}
