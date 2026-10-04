package com.gonzalovega.clientmanagement.assemblers;

import com.gonzalovega.clientmanagement.dto.DatabaseDto.DatabaseRequestDto;
import com.gonzalovega.clientmanagement.dto.DatabaseDto.DatabaseResponseDto;
import com.gonzalovega.clientmanagement.models.DatabaseModel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class DatabaseAssembler {

    /**
     * Converts a DatabaseRequestDto to a DbModel.
     *
     * @param dto The DatabaseRequestDto to convert.
     * @return A DbModel with properties copied from the dto.
     */
    public DatabaseModel toModel(DatabaseRequestDto dto){
        if(dto == null) return null;
        DatabaseModel model = new DatabaseModel();
        BeanUtils.copyProperties(dto,model, "id", "versionLock");
        return model;
    }

    /**
     * Updates an existing DatabaseModel with properties from a DatabaseRequestDto.
     *
     * @param dto   The DatabaseRequestDto containing updated properties.
     * @param model The existing DatabaseModel to update.
     */
    public void updateModel(DatabaseRequestDto dto, DatabaseModel model) {

        if (dto == null || model == null) return;
        BeanUtils.copyProperties(dto, model, "id", "versionLock", "active");
    }

    /**
     * Converts a DbModel to a DatabaseResponseDto.
     *
     * @param model The DbModel to convert.
     * @return A DatabaseResponseDto with properties copied from the model, including the id and version lock.
     */
    public DatabaseResponseDto toResponse(DatabaseModel model){
        if(model == null) return null;
        DatabaseResponseDto dto = new DatabaseResponseDto();
        BeanUtils.copyProperties(model, dto);
        dto.setId(model.getId());
        dto.setVersionLock(model.getVersionLock());
        return dto;
    }
}
