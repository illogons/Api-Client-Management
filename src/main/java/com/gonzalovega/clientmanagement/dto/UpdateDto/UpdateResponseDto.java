package com.gonzalovega.clientmanagement.dto.UpdateDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateResponseDto {

    private Integer id;

    private Integer versionId;

    private Integer clientId;

    private LocalDate launchDate;

    private Integer userId;

    private boolean active;

    private Integer versionLock;

    private LocalDate terminationDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime futureDate;
    private Boolean future;


}
