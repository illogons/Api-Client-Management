package com.gonzalovega.clientmanagement.repository;

import com.gonzalovega.clientmanagement.models.ClientVPNModel;
import com.gonzalovega.clientmanagement.models.ConnectionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConnectionRepository extends JpaRepository<ConnectionModel, Integer>, JpaSpecificationExecutor<ConnectionModel> {

    List<ConnectionModel> findByActiveTrue();

    Optional<ConnectionModel> findByIdAndActiveTrue(Integer idConnection);

    Boolean existsByClientId_IdAndActiveTrue(Integer clientId);

    Boolean existsByTypeConnectionId_IdAndActiveTrue(Integer typeConnectionId);

    List<ConnectionModel> findAllByClientIdIdAndActiveTrue(Integer clientId);

}