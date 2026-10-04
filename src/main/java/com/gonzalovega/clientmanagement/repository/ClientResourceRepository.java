package com.gonzalovega.clientmanagement.repository;

import com.gonzalovega.clientmanagement.models.ClientResourceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientResourceRepository extends JpaRepository<ClientResourceModel,Integer>, JpaSpecificationExecutor<ClientResourceModel> {

    List<ClientResourceModel> findAllByActiveTrue();

    List<ClientResourceModel> findAllByClientIdIdAndActiveTrue(Integer clientId);

    Optional<ClientResourceModel> findByIdAndActiveTrue(Integer id);

    boolean existsByResourceId_IdAndActiveTrue(Integer resourceId);
}