package com.gonzalovega.clientmanagement.assemblers;

import com.gonzalovega.clientmanagement.dto.ConnectionDto.ConnectionRequestDto;
import com.gonzalovega.clientmanagement.dto.ConnectionDto.ConnectionResponseDto;
import com.gonzalovega.clientmanagement.models.ConnectionModel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ConnectionAssembler {

    /**
     * Converts a ConnectionRequestDto to a ConnectionModel.
     *
     * @param dto The ConnectionRequestDto to convert.
     * @return A ConnectionModel with properties copied from the dto.
     */
    public ConnectionModel toModel(ConnectionRequestDto dto) {
        if (dto == null) return null;
        ConnectionModel model = new ConnectionModel();
        BeanUtils.copyProperties(dto, model);
        model.setVersionLock(dto.getVersionLock());
        return model;
    }

    public void updateModel(ConnectionRequestDto dto, ConnectionModel model) {

        if (dto == null || model == null) return;
        BeanUtils.copyProperties(dto, model, "id", "versionLock", "active");
    }

    /**
     * Converts a ConnectionModel to a ConnectionResponseDto.
     *
     * @param model The ConnectionModel to convert.
     * @return A ConnectionResponseDto with properties copied from the model, or null if the model is null.
     */
    public ConnectionResponseDto toResponse(ConnectionModel model) {

        if (model == null) return null;
        ConnectionResponseDto response = new ConnectionResponseDto();
        BeanUtils.copyProperties(model, response);
        if(model.getClientId()!=null){
            response.setClientId(model.getClientId().getId());
        }
        if(model.getTypeConnectionId()!=null){
            response.setTypeConnectionId(model.getTypeConnectionId().getId());
            response.setTypeConnectionName(model.getTypeConnectionId().getName());
        }
        response.setId(model.getId());
        response.setVersionLock(model.getVersionLock());

        return response;
    }
}