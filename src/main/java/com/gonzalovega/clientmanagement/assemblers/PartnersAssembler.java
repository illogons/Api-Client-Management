package com.gonzalovega.clientmanagement.assemblers;

import com.gonzalovega.clientmanagement.dto.PartnerDto.PartnersRequestDTO;
import com.gonzalovega.clientmanagement.dto.PartnerDto.PartnersResponseDTO;
import com.gonzalovega.clientmanagement.models.PartnersModel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class PartnersAssembler {


    public PartnersModel toModel(PartnersRequestDTO request) {
        if (request == null) return null;
        PartnersModel model = new PartnersModel();
        BeanUtils.copyProperties(request, model, "id", "versionLock", "active");
        return model;
    }

    public void updateModel(PartnersModel model, PartnersRequestDTO dto) {
        if (dto == null || model == null) return;
        BeanUtils.copyProperties(dto, model, "id", "versionLock","active");
    }

    public PartnersResponseDTO toResponse(PartnersModel model) {
        if (model == null) return null;
        PartnersResponseDTO response = new PartnersResponseDTO();
        BeanUtils.copyProperties(model, response);

        return response;
    }


}
