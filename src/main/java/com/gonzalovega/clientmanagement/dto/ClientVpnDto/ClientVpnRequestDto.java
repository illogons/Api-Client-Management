package com.gonzalovega.clientmanagement.dto.ClientVpnDto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientVpnRequestDto {

    private Integer id;

    @NotNull( message = "cannor be null")
    private String dm_uuid;

    private Boolean active;

    private Integer port;

    private String user;

    private String password;

    private String description;

    private String url;

    @NotNull( message = "cannor be null")
    private Integer clientId;

    @NotNull( message = "cannor be null")
    private Integer vpnId;

    private Integer versionLock;
}
