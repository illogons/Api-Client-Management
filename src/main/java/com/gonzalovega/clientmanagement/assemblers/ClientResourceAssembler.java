package com.gonzalovega.clientmanagement.assemblers;

import com.gonzalovega.clientmanagement.dto.ClientResourceDto.ClientResourceRequestDto;
import com.gonzalovega.clientmanagement.dto.ClientResourceDto.ClientResourceResponseDto;
import com.gonzalovega.clientmanagement.models.*;
import org.springframework.stereotype.Component;

@Component
public class ClientResourceAssembler {

    public ClientResourceModel toModel(ClientResourceRequestDto dto,
                                      ClientModel client,
                                      ResourceModel resource) {
        if (dto == null) return null;

        ClientResourceModel model = new ClientResourceModel();

        model.setClientId(client);
        model.setResourceId(resource);



        model.setPersonalized(dto.getPersonalized());
        model.setStartDate(dto.getStartDate());
        model.setEndDate(dto.getEndDate());
        model.setActive(true);


        return model;
    }

    public ClientResourceResponseDto toResponse(ClientResourceModel model) {
        if (model == null) return null;

        ClientResourceResponseDto response = new ClientResourceResponseDto();

        response.setId(model.getId());

        if (model.getClientId() != null) {
            response.setClientId(model.getClientId().getId());
        }
        if (model.getResourceId() != null) {
            response.setResourceId(model.getResourceId().getId());
        }
        response.setPersonalized(model.getPersonalized());
        response.setStartDate(model.getStartDate());
        response.setEndDate(model.getEndDate());
        response.setActive(model.getActive());
        response.setVersionLock(model.getVersionLock());

        return response;
    }
}