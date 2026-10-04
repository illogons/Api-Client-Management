package com.gonzalovega.clientmanagement.assemblers;

import com.gonzalovega.clientmanagement.dto.OperativeSystemDto.OperativeSystemRequestDto;
import com.gonzalovega.clientmanagement.dto.OperativeSystemDto.OperativeSystemResponseDto;
import com.gonzalovega.clientmanagement.models.OperativeSystemModel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class OperativeSystemAssembler {

    /**
     * Converts an OperativeSystemRequestDto to a UserModel.
     *
     * @param dto The OperativeSystemRequestDto to convert.
     * @return A UserModel with properties copied from the dto.
     */
    public OperativeSystemModel toModel(OperativeSystemRequestDto dto) {

        OperativeSystemModel model = new OperativeSystemModel();
        BeanUtils.copyProperties(dto, model, "id", "versionLock");
        return model;
    }

    /**
     * Updates an existing OperativeSystemModel with properties from an OperativeSystemRequestDto.
     *
     * @param dto   The OperativeSystemRequestDto containing updated properties.
     * @param model The existing OperativeSystemModel to update.
     */
    public void updateModel(OperativeSystemRequestDto dto, OperativeSystemModel model) {

        if (dto == null || model == null) return;
        BeanUtils.copyProperties(dto, model, "id", "versionLock", "active");
    }

    /**
     * Converts an OperativeSystemRequestDto to a UserModel, including the version lock.
     *
     * @param model The OperativeSystemRequestDto to convert.
     * @return A UserModel with properties copied from the dto, including the version lock.
     */
    public OperativeSystemResponseDto toResponse(OperativeSystemModel model) {

        if (model == null) return null;
        OperativeSystemResponseDto response = new OperativeSystemResponseDto();
        BeanUtils.copyProperties(model, response);

        response.setId(model.getId());
        response.setVersionLock(model.getVersionLock());

        return response;
    }
}