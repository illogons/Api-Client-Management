package com.gonzalovega.clientmanagement.dto.ClientResourceDto;

import lombok.*;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ClientResourceResponseDto {

    private Integer id;

    private Integer clientId;

    private Integer resourceId;

    private Boolean personalized;

    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean active;

    private Integer versionLock;
}