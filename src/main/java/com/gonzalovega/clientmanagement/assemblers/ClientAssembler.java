package com.gonzalovega.clientmanagement.assemblers;

import com.gonzalovega.clientmanagement.dto.ClientDto.ClientRequestDto;
import com.gonzalovega.clientmanagement.dto.ClientDto.ClientResponseDto;
import com.gonzalovega.clientmanagement.models.ClientModel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ClientAssembler {

    /**
     * Converts a ClientRequestDto to a ClientModel.
     *
     * @param dto The ClientRequestDto to convert.
     * @return A ClientModel with properties copied from the dto.
     */
    public ClientModel toModel(ClientRequestDto dto) {
        if(dto == null) return null;

        ClientModel model = new ClientModel();

        BeanUtils.copyProperties(dto, model,
                "id",
                "createdBy",
                "versionLock",
                "databaseId",
                "operativeSystemId",
                "partnerId"
        );

        if (dto.getDatabaseId() != null) {
            model.setDatabaseId(model.getDatabaseId());
        }

        if (dto.getOperativeSystemId() != null) {
            model.setOperativeSystemId(model.getOperativeSystemId());
        }

        return model;
    }

    /**
     * Converts a ClientModel to a ClientResponseDto.
     *
     * @param model The ClientModel to convert.
     * @return A ClientResponseDto with properties copied from the model, or null if the model is null.
     */
    public ClientResponseDto toResponse(ClientModel model) {
        if (model == null) return null;

        ClientResponseDto response = new ClientResponseDto();
        BeanUtils.copyProperties(model, response,
                "databaseId",
                "operativeSystemId",
                "partnerId"
        );

        if (model.getPartnerId() != null) {
            response.setPartnerId(model.getPartnerId().getId());
            response.setPartnerName(model.getPartnerId().getName());
        }

        if (model.getDatabaseId() != null) {
            response.setDatabaseId(model.getDatabaseId().getId());
            response.setDatabaseName(model.getDatabaseId().getName());
        }

        if (model.getOperativeSystemId() != null) {
            response.setOperativeSystemId(model.getOperativeSystemId().getId());
            response.setOperativeSystemName(model.getOperativeSystemId().getName());
        }

        if(model.getResponsibleId() != null) {
            response.setResponsibleId(model.getResponsibleId().getId());
            response.setResponsibleName(model.getResponsibleId().getName());
        }

        return response;
    }
}
