package com.gonzalovega.clientmanagement.services.ClientVpnService;

import com.gonzalovega.clientmanagement.dto.ClientVpnDto.ClientVpnRequestDto;
import com.gonzalovega.clientmanagement.dto.ClientVpnDto.ClientVpnResponseDto;
import com.gonzalovega.clientmanagement.dto.ClientVpnDto.ClientVpnSearchDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IClientVpnService {

    List<ClientVpnResponseDto> getAllCV();

    List<ClientVpnResponseDto> getActiveCV();

    ClientVpnResponseDto getCVById(Integer id);

    ClientVpnResponseDto createCV(ClientVpnRequestDto dto);

    ClientVpnResponseDto updateCV(Integer id, ClientVpnRequestDto dto);

    Page<ClientVpnResponseDto> SearchCV(ClientVpnSearchDto filter, int page, int size);

    void deleteVPN(Integer id);
}
