package com.gonzalovega.clientmanagement.assemblers;

import com.gonzalovega.clientmanagement.dto.ResourceDto.ResourceRequestDto;
import com.gonzalovega.clientmanagement.dto.ResourceDto.ResourceResponseDto;
import com.gonzalovega.clientmanagement.models.ResourceModel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ResourceAssembler {

    /**
     * Converts a ResourceRequestDto to a ResourceModel.
     *
     * @param dto The ResourceRequestDto to convert.
     * @return A ResourceModel with properties copied from the dto, or null if the dto is null.
     */
    public ResourceModel toModel(ResourceRequestDto dto) {

        ResourceModel model = new ResourceModel();
        BeanUtils.copyProperties(dto, model, "id", "versionLock");
        return model;
    }

    /**
     * Updates an existing ResourceModel with properties from a ResourceRequestDto.
     *
     * @param dto   The ResourceRequestDto containing updated properties.
     * @param model The existing ResourceModel to update.
     */
    public void updateModel(ResourceRequestDto dto, ResourceModel model) {

        if (dto == null || model == null) return;
        BeanUtils.copyProperties(dto, model, "id", "versionLock", "active");
    }

    /**
     * Converts a ResourceModel to a ResourceResponseDto.
     *
     * @param model The ResourceModel to convert.
     * @return A ResourceResponseDto with properties copied from the model, including the id and version lock, or null if the model is null.
     */
    public ResourceResponseDto toResponse(ResourceModel model) {

        if (model == null) return null;
        ResourceResponseDto response = new ResourceResponseDto();
        BeanUtils.copyProperties(model, response);

        response.setId(model.getId());
        response.setVersionLock(model.getVersionLock());
        return response;
    }
}