package com.gonzalovega.clientmanagement.dto.ConnectionDto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConnectionResponseDto {

    private Integer id;

    private Integer clientId;

    private Integer typeConnectionId;
    private String typeConnectionName;

    private String dmUuid;

    private String details;

    private Boolean active;

    private Integer versionLock;
}
