package com.gonzalovega.clientmanagement.dto.ConnectionDto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConnectionRequestDto {

    private Integer id;

    @NotNull(message = "Client id is required")
    private Integer clientId;

    private String dmUuid;

    @NotNull(message = "Type connection id is required")
    private Integer typeConnectionId;

    @NotNull(message = "details are required")
    private String details;

    private Integer versionLock;
}