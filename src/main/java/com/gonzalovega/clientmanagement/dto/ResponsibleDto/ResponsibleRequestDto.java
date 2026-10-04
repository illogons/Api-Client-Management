package com.gonzalovega.clientmanagement.dto.ResponsibleDto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponsibleRequestDto {

    private Integer id;

    @NotNull(message = "name obligatory")
    private String Name;

    private Boolean active;

    private Integer versionLock;

}
