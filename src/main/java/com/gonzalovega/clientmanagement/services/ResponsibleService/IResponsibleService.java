package com.gonzalovega.clientmanagement.services.ResponsibleService;

import com.gonzalovega.clientmanagement.dto.PartnerDto.PartnersRequestDTO;
import com.gonzalovega.clientmanagement.dto.PartnerDto.PartnersResponseDTO;
import com.gonzalovega.clientmanagement.dto.PartnerDto.PartnersSearchDTO;
import com.gonzalovega.clientmanagement.dto.ResponsibleDto.ResponsibleRequestDto;
import com.gonzalovega.clientmanagement.dto.ResponsibleDto.ResponsibleResponseDto;
import com.gonzalovega.clientmanagement.dto.ResponsibleDto.ResponsibleSearchDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IResponsibleService {

    List<ResponsibleResponseDto> getAllResponsibles();

    List<ResponsibleResponseDto> getActiveResponsibles();

    ResponsibleResponseDto getResponsiblesById(Integer id);

    ResponsibleResponseDto createResponsibles(ResponsibleRequestDto dto);

    ResponsibleResponseDto updateResponsibles(Integer id, ResponsibleRequestDto dto);

    Page<ResponsibleResponseDto> SearchResponsibles(ResponsibleSearchDto filter, int page, int size);

    void deleteResponsibles(Integer id);
}
