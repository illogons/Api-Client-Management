package com.gonzalovega.clientmanagement.dto.VersionDto;

import lombok.*;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder

public class VersionSearchRequestDto {

    private String version;

    private LocalDate launchDateFrom;

    private LocalDate launchDateTo;

    private Boolean active;
}