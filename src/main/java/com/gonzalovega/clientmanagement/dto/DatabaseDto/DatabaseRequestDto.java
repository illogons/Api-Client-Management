package com.gonzalovega.clientmanagement.dto.DatabaseDto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DatabaseRequestDto {

    private Integer id;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    private Integer versionLock;
}
