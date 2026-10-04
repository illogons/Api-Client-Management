package com.gonzalovega.clientmanagement.dto.OperativeSystemDto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder

public class OperativeSystemSearchRequestDto {

    private String name;

    private String version;

    private Boolean active;
}