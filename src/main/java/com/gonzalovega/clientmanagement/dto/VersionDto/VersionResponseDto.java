package com.gonzalovega.clientmanagement.dto.VersionDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VersionResponseDto {

    private Integer id;

    private String version;

    private LocalDate launchDate;

    private Boolean active;

    private Integer versionLock;

    private String changelog;

    private String breakingChanges;

}
