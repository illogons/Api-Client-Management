package com.gonzalovega.clientmanagement.assemblers;

import com.gonzalovega.clientmanagement.dto.VpnDto.VPNRequestDto;
import com.gonzalovega.clientmanagement.dto.VpnDto.VPNResponseDto;
import com.gonzalovega.clientmanagement.models.VPNModel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class VPNAssembler {

    public static VPNModel toModel(VPNRequestDto Dto) {

        VPNModel vpnModel = new VPNModel();
        BeanUtils.copyProperties(Dto, vpnModel, "id", "active", "versionLock");
        return vpnModel;
    }

    public void toUpdate(VPNRequestDto dto, VPNModel model) {

        if (dto == null || model == null) return;
        BeanUtils.copyProperties(dto, model, "id", "versionLock", "active");
    }

    public VPNResponseDto toResponse(VPNModel vpnModel) {

        VPNResponseDto dto = new VPNResponseDto();
        BeanUtils.copyProperties(vpnModel, dto);
        return dto;
    }

}
