package com.gonzalovega.clientmanagement.dto.UsersDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsersResponseDto {

    private Integer id;

    private Integer versionLock;

    private Boolean active;

    private String name;


}
