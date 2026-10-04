package com.gonzalovega.clientmanagement.services.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.gonzalovega.clientmanagement.assemblers.UsersAssembler;
import com.gonzalovega.clientmanagement.dto.UsersDto.UsersRequestDto;
import com.gonzalovega.clientmanagement.dto.UsersDto.UsersResponseDto;
import com.gonzalovega.clientmanagement.dto.UsersDto.UsersSearchDto;
import com.gonzalovega.clientmanagement.models.UsersModel;
import com.gonzalovega.clientmanagement.repository.UsersRepository;
import com.gonzalovega.clientmanagement.specifications.UsersSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements IUsersService {

    private final UsersRepository usersRepository;
    private final UsersAssembler usersAssembler;

    @Transactional
    @Override
    public List<UsersResponseDto> getAllUsers() {
        log.info("getUsers() - start");

        List<UsersResponseDto> lista = usersRepository.findAll()
                .stream().map(usersAssembler::toResponse)
                .toList();

        log.info("getUsers() - end");
        return lista;
    }

    @Transactional
    @Override
    public List<UsersResponseDto> getActiveUsers() {
        log.info("getActiveUsers() - start");

        List<UsersResponseDto> lista = usersRepository.findAllByActiveTrue()
                .stream()
                .filter(UsersModel::getActive)
                .map(usersAssembler::toResponse)
                .toList();

        log.info("getActiveUsers() - end");
        return lista;
    }




    @Transactional
    @Override
    public UsersResponseDto getUserById(Integer id) {
        log.info("getUserById() - start");

        UsersModel entiti = usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resource not found with ID: " + id));

        log.info("getUserById() - end");

        return usersAssembler.toResponse(entiti);

    }

    @Transactional
    @Override
    public UsersResponseDto createUser(UsersRequestDto dto) {
        log.info("createUser() - start");

        UsersModel users= usersAssembler.toModel(dto);
        UsersModel createdUser = usersRepository.save(users);

        log.info("createUser() - end");
        return usersAssembler.toResponse(createdUser);

    }

    @Transactional
    @Override
    public UsersResponseDto updateUpdate(Integer id, UsersRequestDto dto) {
        log.info("updateUser() - start");

        UsersModel users= usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resource not found with ID: " + id));

        if(!users.getId().equals(dto.getId())){
            log.error("ID mismatch: Path ID {} does not match DTO ID {}", id, dto.getId());
            throw new IllegalArgumentException("ID in path and request body must match.");
        }

        if (!users.getVersionLock().equals(dto.getVersionLock())) {
            log.warn("Version mismatch for ID {}: DB={}, Request={}", id, users.getVersionLock(), dto.getVersionLock());
            throw new RuntimeException("Data was modified by another user. Please refresh.");
        }

        usersAssembler.updateModel(dto, users);

        UsersModel updatedUser = usersRepository.save(users);
        log.info("updateUser() - end");

        return usersAssembler.toResponse(updatedUser);
    }


    @Transactional
    @Override
    public Page<UsersResponseDto> SearchUsers(UsersSearchDto filter, int page, int size) {
        log.info("searchUpdates - start (filter={}, page={}, size={})", filter, page, size);

        Pageable pageable = PageRequest.of(page, size);

        org.springframework.data.domain.Page<UsersResponseDto> results = usersRepository
                .findAll(UsersSpecifications.byFilter(filter), pageable)
                .map(usersAssembler::toResponse);

        log.info("searchUpdates - end (returnedElements={}, totalElements={}, totalPages={})",
                results.getNumberOfElements(), results.getTotalElements(), results.getTotalPages());

        return results;
    }

    public void deleteUser(Integer id) {
        log.info("deleteUser() - start");

        UsersModel user = usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resource not found with ID: " + id));

        user.setActive(false);
        usersRepository.save(user);
        log.info("deleteUser() - end");
    }








}
