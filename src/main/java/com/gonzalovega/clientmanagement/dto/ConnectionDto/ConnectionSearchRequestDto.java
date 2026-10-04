package com.gonzalovega.clientmanagement.dto.ConnectionDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ConnectionSearchRequestDto {

    private Integer clientId;
    List<Integer> clientIds;

    private Integer typeConnectionId;

    private String details;

    private Boolean active;

    private String dmUuid;

}
