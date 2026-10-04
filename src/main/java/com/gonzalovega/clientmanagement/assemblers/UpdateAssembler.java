package com.gonzalovega.clientmanagement.assemblers;

import lombok.extern.slf4j.Slf4j;
import com.gonzalovega.clientmanagement.dto.UpdateDto.UpdateRequestDto;
import com.gonzalovega.clientmanagement.dto.UpdateDto.UpdateResponseDto;
import com.gonzalovega.clientmanagement.models.UpdateModel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@Slf4j
public class UpdateAssembler {

    public UpdateModel toModel(UpdateRequestDto dto){
        UpdateModel model = new UpdateModel();
        BeanUtils.copyProperties(dto, model,"id", "versionLock");
        return model;
    }

    public void updateModel(UpdateRequestDto dto, UpdateModel model) {
        BeanUtils.copyProperties(dto, model, "id", "active", "versionLock");
    }

    public UpdateResponseDto toResponse(UpdateModel model){
        UpdateResponseDto responseDto = new UpdateResponseDto();

        BeanUtils.copyProperties(model, responseDto, "future", "versionId", "clientId", "userId");

        if (model.getVersionId() != null)
            responseDto.setVersionId(model.getVersionId().getId());
        if (model.getClientId() != null)
            responseDto.setClientId(model.getClientId().getId());
        if (model.getUserId() != null)
            responseDto.setUserId(model.getUserId().getId());

        responseDto.setFuture(model.getFutureDate() != null ? model.getFutureDate().isAfter(LocalDateTime.now()) : null
        );

        responseDto.setLaunchDate(model.getLaunchDate());
        return responseDto;
    }
}
