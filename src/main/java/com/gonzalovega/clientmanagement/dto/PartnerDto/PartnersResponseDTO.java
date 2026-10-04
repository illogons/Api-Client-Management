package com.gonzalovega.clientmanagement.dto.PartnerDto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PartnersResponseDTO {

    private Integer id;

    @NotNull( message = "Name obligatory")
    private String name;

    private String dm_uuid;

    private Integer versionLock;

    private Boolean active;


}
