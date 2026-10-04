package com.gonzalovega.clientmanagement.assemblers;

import com.gonzalovega.clientmanagement.dto.VersionDto.VersionRequestDto;
import com.gonzalovega.clientmanagement.dto.VersionDto.VersionResponseDto;
import com.gonzalovega.clientmanagement.models.VersionModel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class VersionAssembler {

    /**
     * Converts a VersionRequestDto to a VersionModel.
     *
     * @param dto The VersionRequestDto to convert.
     * @return A VersionModel with properties copied from the dto, and launchDate set to current date if not provided.
     */
    public VersionModel toModel(VersionRequestDto dto) {

        VersionModel model = new VersionModel();
        BeanUtils.copyProperties(dto, model,"id", "versionLock");
        return model;
    }

    /**
     * Updates an existing VersionModel with properties from a VersionRequestDto.
     *
     * @param dto   The VersionRequestDto containing updated properties.
     * @param model The existing VersionModel to update.
     */
    public void updateModel(VersionRequestDto dto, VersionModel model) {

        if (dto == null || model == null) return;
        BeanUtils.copyProperties(dto, model, "id", "versionLock", "active");
    }

    /**
     * Converts a VersionModel to a VersionResponseDto.
     *
     * @param model The VersionModel to convert.
     * @return A VersionResponseDto with properties copied from the model, including the id and version lock, or null if the model is null.
     */
    public VersionResponseDto toResponse(VersionModel model) {

        if (model == null) return null;
        VersionResponseDto response = new VersionResponseDto();
        BeanUtils.copyProperties(model, response);

        response.setId(model.getId());
        response.setVersionLock(model.getVersionLock());

        return response;
    }
}
