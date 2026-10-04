package com.gonzalovega.clientmanagement.dto.UsersDto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsersSearchDto {

    private String name;

    private Boolean active;

    private List<String> names;

}
