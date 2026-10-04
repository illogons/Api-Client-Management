package com.gonzalovega.clientmanagement.dto.ClientResourceDto;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder

public class ClientResourceSearchRequestDto {

    private Integer clientId;
    private List<Integer> clientIds;

    private Integer resourceId;

    private Boolean personalized;

    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean active;
}