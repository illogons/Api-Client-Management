package com.gonzalovega.clientmanagement.dto.OperativeSystemDto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OperativeSystemResponseDto {

    private Integer id;

    private String name;

    private String version;

    private boolean active;

    private Integer versionLock;
}