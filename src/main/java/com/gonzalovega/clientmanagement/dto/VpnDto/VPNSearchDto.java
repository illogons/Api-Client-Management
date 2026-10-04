package com.gonzalovega.clientmanagement.dto.VpnDto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VPNSearchDto {

    private String name;
    private List<String> names;

    private Boolean active;
}
