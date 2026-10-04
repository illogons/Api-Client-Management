package com.gonzalovega.clientmanagement.dto.VpnDto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VPNResponseDto {

    private Integer id;

    private String name;

    private String url;

    private Boolean active;

    private Integer versionLock;


}
