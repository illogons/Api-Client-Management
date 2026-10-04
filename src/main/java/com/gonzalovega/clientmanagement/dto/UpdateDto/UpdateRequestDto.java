package com.gonzalovega.clientmanagement.dto.UpdateDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
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
public class UpdateRequestDto {

    private Integer id;

    @NotNull(message = "client id is obligatory")
    private Integer clientId;

    @NotNull(message = "version id is obligatory")
    private Integer versionId;

    @NotNull(message = "date is obligatory")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate launchDate;

    private Integer userId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate terminationDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime futureDate;

    private Integer versionLock;



}
