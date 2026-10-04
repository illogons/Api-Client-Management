package com.gonzalovega.clientmanagement.assemblers;

import com.gonzalovega.clientmanagement.dto.TypeConnectionDto.TypeConnectionRequestDto;
import com.gonzalovega.clientmanagement.dto.TypeConnectionDto.TypeConnectionResponseDto;
import com.gonzalovega.clientmanagement.models.TypeConnectionModel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class TypeConnectionAssembler {

    /**
     * Converts a TypeConnectionRequestDto to a TypeConnectionModel.
     *
     * @param dto The TypeConnectionRequestDto to convert.
     * @return A TypeConnectionModel with properties copied from the dto.
     */
    public TypeConnectionModel toModel(TypeConnectionRequestDto dto){

        TypeConnectionModel model = new TypeConnectionModel();
        BeanUtils.copyProperties(dto, model, "id", "versionLock");
        return model;
    }

    /**
     * Updates an existing TypeConnectionModel with properties from a TypeConnectionRequestDto.
     *
     * @param dto   The TypeConnectionRequestDto containing updated properties.
     * @param model The existing TypeConnectionModel to update.
     */
    public void updateModel(TypeConnectionRequestDto dto, TypeConnectionModel model) {

        if (dto == null || model == null) return;
        BeanUtils.copyProperties(dto, model, "id", "versionLock", "active");
    }

    /**
     * Converts a TypeConnectionModel to a TypeConnectionResponseDto.
     *
     * @param model The TypeConnectionModel to convert.
     * @return A TypeConnectionResponseDto with properties copied from the model, or null if the model is null.
     */
    public TypeConnectionResponseDto toResponse ( TypeConnectionModel model){

        if (model == null) return null;
        TypeConnectionResponseDto response = new TypeConnectionResponseDto();
        BeanUtils.copyProperties(model, response);
        response.setId(model.getId());
        response.setVersionLock(model.getVersionLock());

        return response;
    }
}
