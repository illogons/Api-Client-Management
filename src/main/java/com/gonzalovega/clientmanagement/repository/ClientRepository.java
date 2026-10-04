package com.gonzalovega.clientmanagement.repository;

import com.gonzalovega.clientmanagement.models.ClientModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<ClientModel, Integer>, JpaSpecificationExecutor<ClientModel> {

    List<ClientModel> findByActiveTrue();

    Boolean existsByIdAndActiveTrue(Integer clientId);

    Optional<ClientModel> findByIdAndActiveTrue(Integer clientId);
}