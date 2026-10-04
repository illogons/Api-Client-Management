package com.gonzalovega.clientmanagement.services.UserService;

import com.gonzalovega.clientmanagement.dto.UsersDto.UsersRequestDto;
import com.gonzalovega.clientmanagement.dto.UsersDto.UsersResponseDto;
import com.gonzalovega.clientmanagement.dto.UsersDto.UsersSearchDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IUsersService {

    List<UsersResponseDto> getAllUsers();

    List<UsersResponseDto> getActiveUsers();

    UsersResponseDto getUserById(Integer id);

    UsersResponseDto createUser(UsersRequestDto dto);

    UsersResponseDto updateUpdate(Integer id, UsersRequestDto dto);

    Page<UsersResponseDto> SearchUsers(UsersSearchDto filter, int page, int size);

    void deleteUser(Integer id);
}
