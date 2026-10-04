package com.gonzalovega.clientmanagement.dto.ClientVpnDto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientVpnResponseDto {

    private Integer id;

    private String dm_uuid;

    private Boolean active;

    private Integer clientId;

    private Integer port;

    private String user;

    private String password;

    private String description;

    private String url;

    private Integer vpnId;

    private Integer versionLock;
}
