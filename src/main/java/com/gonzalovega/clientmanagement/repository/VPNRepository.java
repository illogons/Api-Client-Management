package com.gonzalovega.clientmanagement.repository;

import com.gonzalovega.clientmanagement.models.UsersModel;
import com.gonzalovega.clientmanagement.models.VPNModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VPNRepository extends JpaRepository<VPNModel, Integer>, JpaSpecificationExecutor<VPNModel> {

    Optional<VPNModel> findById(Integer id);

    Optional<VPNModel> findByIdAndActiveTrue(Integer id);


    Boolean existsByIdAndActiveTrue(Integer vpnId);

    List<VPNModel> findAllByActiveTrue();
}
