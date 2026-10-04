package com.gonzalovega.clientmanagement.repository;

import com.gonzalovega.clientmanagement.models.ClientModel;
import com.gonzalovega.clientmanagement.models.ClientVPNModel;
import com.gonzalovega.clientmanagement.models.UpdateModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientVpnRepository extends JpaRepository<ClientVPNModel, Integer>, JpaSpecificationExecutor<ClientVPNModel> {

    Optional<ClientVPNModel> findById(Integer id);

    boolean existsByClientId(ClientModel client);

    Optional<ClientVPNModel> findByIdAndActiveTrue(Integer id);

    List<ClientVPNModel> findAllByActiveTrue();

    List<ClientVPNModel> findAllByClientIdIdAndActiveTrue(Integer clientId);



}
