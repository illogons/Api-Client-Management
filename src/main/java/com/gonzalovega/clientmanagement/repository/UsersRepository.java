package com.gonzalovega.clientmanagement.repository;

import com.gonzalovega.clientmanagement.models.UsersModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<UsersModel,Integer>, JpaSpecificationExecutor<UsersModel> {

    Optional<UsersModel> findById(Integer id);

    List<UsersModel> findAll();

    List<UsersModel> findAllByActiveTrue();
}
