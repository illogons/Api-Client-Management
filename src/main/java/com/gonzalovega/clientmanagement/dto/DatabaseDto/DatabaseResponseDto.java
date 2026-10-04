package com.gonzalovega.clientmanagement.dto.DatabaseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DatabaseResponseDto {

    private Integer id;

    private String name;

    private Boolean active;

    private Integer versionLock;
}
