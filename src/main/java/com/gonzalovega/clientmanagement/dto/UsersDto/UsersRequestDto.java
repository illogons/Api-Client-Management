package com.gonzalovega.clientmanagement.dto.UsersDto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsersRequestDto {

    private Integer id;

    @NotNull(message = "the name is obligatory")
    private String name;

    private Integer versionLock;
}
