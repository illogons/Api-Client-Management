package com.gonzalovega.clientmanagement.dto.ResourceDto;

import lombok.*;
import com.gonzalovega.clientmanagement.ResourceType;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder

public class ResourceSearchRequestDto {

    private String name;

    private String description;

    private ResourceType typeApp;

    private LocalDate launchDateFrom;

    private LocalDate launchDateTo;

    private Boolean active;
}