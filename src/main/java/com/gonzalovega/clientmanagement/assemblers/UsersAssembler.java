package com.gonzalovega.clientmanagement.assemblers;

import com.gonzalovega.clientmanagement.dto.UsersDto.UsersRequestDto;
import com.gonzalovega.clientmanagement.dto.UsersDto.UsersResponseDto;
import com.gonzalovega.clientmanagement.models.UsersModel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class UsersAssembler {

    public UsersModel toModel(UsersRequestDto dto) {
        UsersModel usersModel = new UsersModel();

        BeanUtils.copyProperties(dto, usersModel, "id", "versionLock");

        return usersModel;
    }

    public void updateModel(UsersRequestDto dto, UsersModel model) {

        BeanUtils.copyProperties(dto, model,"versionLock","active", "id");
    }

    public UsersResponseDto toResponse(UsersModel usersModel) {
        UsersResponseDto usersDto = new UsersResponseDto();

        BeanUtils.copyProperties(usersModel, usersDto);

        return usersDto;

    }
}
