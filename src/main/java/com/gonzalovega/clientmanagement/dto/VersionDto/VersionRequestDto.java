package com.gonzalovega.clientmanagement.dto.VersionDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VersionRequestDto {

    private Integer id;

    @NotBlank(message = "Version cannot be blank")
    private String version;

    @NotNull(message = "Launch date cannot be null")
    private LocalDate launchDate;

    private Integer versionLock;

    private String changelog;

    private String breakingChanges;
}
