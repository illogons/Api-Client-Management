package com.gonzalovega.clientmanagement.services.PartnerService;


import com.gonzalovega.clientmanagement.dto.PartnerDto.PartnersRequestDTO;
import com.gonzalovega.clientmanagement.dto.PartnerDto.PartnersResponseDTO;
import com.gonzalovega.clientmanagement.dto.PartnerDto.PartnersSearchDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IPartnersService {

    List<PartnersResponseDTO> getAllPartners();

    List<PartnersResponseDTO> getActivePartners();

    PartnersResponseDTO getPartnersById(Integer id);

    PartnersResponseDTO createPartners(PartnersRequestDTO dto);

    PartnersResponseDTO updatePartners(Integer id, PartnersRequestDTO dto);

    Page<PartnersResponseDTO> SearchPartners(PartnersSearchDTO filter, int page, int size);

    void deletePartners(Integer id);

}
