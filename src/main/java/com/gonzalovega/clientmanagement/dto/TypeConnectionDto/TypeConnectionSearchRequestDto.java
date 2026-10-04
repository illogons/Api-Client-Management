package com.gonzalovega.clientmanagement.dto.TypeConnectionDto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class TypeConnectionSearchRequestDto {

    private String name;

    private Boolean active;
}
