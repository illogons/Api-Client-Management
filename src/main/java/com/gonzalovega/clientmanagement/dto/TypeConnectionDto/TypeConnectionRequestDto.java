package com.gonzalovega.clientmanagement.dto.TypeConnectionDto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TypeConnectionRequestDto {

    private Integer id;

    @NotNull(message = "Name type id cannot be null")
    private String name;

    private Integer versionLock;
}
