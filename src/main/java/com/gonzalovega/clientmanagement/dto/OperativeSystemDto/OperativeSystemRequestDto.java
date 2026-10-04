package com.gonzalovega.clientmanagement.dto.OperativeSystemDto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class OperativeSystemRequestDto {

    private Integer id;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    private String version;

    private Integer versionLock;
}