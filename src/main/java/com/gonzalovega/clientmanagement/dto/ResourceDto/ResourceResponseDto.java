package com.gonzalovega.clientmanagement.dto.ResourceDto;

import lombok.*;
import com.gonzalovega.clientmanagement.ResourceType;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResourceResponseDto {

    private Integer id;

    private ResourceType typeApp;

    private LocalDate launchDate;

    private String name;

    private String description;

    private boolean active;

    private Integer versionLock;
}