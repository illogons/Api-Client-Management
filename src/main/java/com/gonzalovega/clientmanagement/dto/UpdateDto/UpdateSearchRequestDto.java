package com.gonzalovega.clientmanagement.dto.UpdateDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateSearchRequestDto {

    private Integer clientId;

    private Integer versionId;

    private Integer userId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate launchDate;

    private Boolean active;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate terminationDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime futureDate;
    private Boolean future;
}
