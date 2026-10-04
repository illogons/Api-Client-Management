package com.gonzalovega.clientmanagement.dto.ClientVpnDto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientVpnSearchDto {


    private String name;
    private List<String> names;

    private Boolean active;

    private Integer clientId;
    private List<Integer> clientIds;

    private Integer vpnId;
    private List<Integer> vpnIds;

}
