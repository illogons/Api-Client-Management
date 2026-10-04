package com.gonzalovega.clientmanagement.dto.DatabaseDto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder

public class DatabaseSearchRequestDto {
    private String name;

    private Boolean active;
}