package com.gonzalovega.clientmanagement.services.VpnService;

import com.gonzalovega.clientmanagement.dto.VpnDto.VPNRequestDto;
import com.gonzalovega.clientmanagement.dto.VpnDto.VPNResponseDto;
import com.gonzalovega.clientmanagement.dto.VpnDto.VPNSearchDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IVPNService {
    List<VPNResponseDto> getAllVPN();

    List<VPNResponseDto> getActiveVPN();

    VPNResponseDto getVPNUserById(Integer id);

    VPNResponseDto createVPN(VPNRequestDto dto);

    VPNResponseDto updateVPNUpdate(Integer id, VPNRequestDto dto);

    Page<VPNResponseDto> SearchVPN(VPNSearchDto filter, int page, int size);

    void deleteVPN(Integer id);
}
