package com.gonzalovega.clientmanagement.dto.TypeConnectionDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TypeConnectionResponseDto {

    private Integer id;

    private String name;

    private Boolean active;

    private Integer versionLock;
}
