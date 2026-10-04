package com.gonzalovega.clientmanagement.dto.ResponsibleDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponsibleResponseDto {

    private Integer id;

    private String Name;

    private Boolean active;

    private Integer versionLock;
}
