package com.gonzalovega.clientmanagement.assemblers;

import com.gonzalovega.clientmanagement.dto.ResponsibleDto.ResponsibleRequestDto;
import com.gonzalovega.clientmanagement.dto.ResponsibleDto.ResponsibleResponseDto;
import com.gonzalovega.clientmanagement.models.ResponsibleModel;
import com.gonzalovega.clientmanagement.repository.ResponsibleRespository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ResponsibleAssembler {

    public ResponsibleModel toModel(ResponsibleRequestDto dto){

        ResponsibleModel responsibleModel = new ResponsibleModel();
        BeanUtils.copyProperties(dto, responsibleModel, "id", "versionLock", "active");

        return responsibleModel;
    }

    public void toUpdate(ResponsibleRequestDto dto, ResponsibleModel model){
        if (dto == null || model == null) return;
        BeanUtils.copyProperties(dto, model, "id", "versionLock", "active");
    }

    public ResponsibleResponseDto toResponse(ResponsibleModel model){

        ResponsibleResponseDto responsibleRequestDto = new ResponsibleResponseDto();
        BeanUtils.copyProperties(model, responsibleRequestDto);

        return responsibleRequestDto;
    }
}
