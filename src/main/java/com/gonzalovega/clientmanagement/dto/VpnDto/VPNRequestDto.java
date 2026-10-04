package com.gonzalovega.clientmanagement.dto.VpnDto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VPNRequestDto {

    private Integer id;

    @NotNull( message = "cannot be null")
    @NotBlank(message = "cannot be blnck")
    private String name;

    private Boolean active;

    private String url;

    private Integer versionLock;

}
