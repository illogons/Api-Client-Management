package com.gonzalovega.clientmanagement.dto.ClientResourceDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ClientResourceRequestDto {

    private Integer Id;

    @NotNull(message = "Client ID is required")
    private Integer clientId;

    @NotNull(message = "Resource ID is required")
    private Integer resourceId;

    private Boolean personalized;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private Integer versionLock;


}