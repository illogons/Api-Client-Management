package com.gonzalovega.clientmanagement.assemblers;

import com.gonzalovega.clientmanagement.dto.ClientVpnDto.ClientVpnRequestDto;
import com.gonzalovega.clientmanagement.dto.ClientVpnDto.ClientVpnResponseDto;
import com.gonzalovega.clientmanagement.models.ClientVPNModel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ClientVpnAssembler {

    public  ClientVPNModel toModel(ClientVpnRequestDto dto) {
        ClientVPNModel model = new ClientVPNModel();
        BeanUtils.copyProperties(dto, model, "id", "active", "versionLock","clientId", "vpnId");
        return model;
    }

    public void toUpdate(ClientVpnRequestDto dto, ClientVPNModel model ) {

        if (dto == null || model == null) return;
        BeanUtils.copyProperties(dto, model,  "id", "active", "versionLock");

    }

    public ClientVpnResponseDto toResponse(ClientVPNModel model) {
        ClientVpnResponseDto dto = new ClientVpnResponseDto();
        BeanUtils.copyProperties(model, dto);


        if( model.getClientId() != null){
            dto.setClientId(model.getClientId().getId());
        }
        if( model.getVpnId() != null){
            dto.setVpnId(model.getVpnId().getId());
        }
        return dto;
    }

}
